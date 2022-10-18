package com.vrd.gsaf.home.homeTab.media;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.compnayDetail.CompanyDetails;
import com.vrd.gsaf.api.responses.compnayDetail.Data;
import com.vrd.gsaf.api.responses.fairMedia.FairMedia;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Media extends Fragment implements View.OnClickListener, MediaAdapter.OnFullScreenClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    protected HttpProxyCacheServer proxy;
    protected MainApp app = new MainApp();
    protected double current_pos, total_duration;
    protected Handler mHandler, handler;
    protected DisplayMetrics displayMetrics = new DisplayMetrics();
    ImageView pause;
    ImageView fullScreen;
    ImageView orientationImage;
    ConstraintLayout.LayoutParams lp;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    com.vrd.gsaf.api.responses.fairMedia.Data mediaData;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView;
    private RecyclerView recyclerView;
    private MediaAdapter adapter;
    private ConstraintLayout videoViewLayout;
    private VideoView videoPlayer;
    private SeekBar seekBar;
    private boolean isFullScreen = false;
    private long mLastClickTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        view = inflater.inflate(R.layout.fragment_media, container, false);
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }


    @SuppressLint("CutPasteId")
    private void initializeViews() {
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        backImageView = view.findViewById(R.id.backImageView);
        videoViewLayout = view.findViewById(R.id.videoViewLayoutMedia2);
        videoPlayer = view.findViewById(R.id.videoPlayerMedia);
        seekBar = view.findViewById(R.id.seekBarMedia);
        fullScreen = view.findViewById(R.id.fullScreenImageViewMedia);
        orientationImage = view.findViewById(R.id.orientationImageViewMedia);
        pause = view.findViewById(R.id.pauseMedia);
        lp = (ConstraintLayout.LayoutParams) videoViewLayout.getLayoutParams();
        setClickListeners();
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        Singleton.getInstance().getYoutubePLayers().clear();


    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        if (Helper.isInternetConnected()) {
            if (Singleton.getInstance().getHomeState().equals("stands")) {
                Helper.showProgressBar(progressBar);
                callApiForCompanyDetail(false);
            } else if (Singleton.getInstance().getHomeState().equals("reception")) {
                callApiForFairMedia();
                // manageRecyclerViewForReception();
            }
        }
        retryLayout();
        // manageRecyclerView();
    }

    private void callApiForFairMedia() {

        Helper.showProgressBar(progressBar);
        String url = "api/fair/media/list/" + Singleton.getInstance().getFairData().getFair().getId().toString();
        Call<FairMedia> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getFairMedia(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<FairMedia>() {
            @Override
            public void onResponse(Call<FairMedia> call, Response<FairMedia> response) {
                Helper.hideProgressBar(progressBar);
                //Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        mediaData = response.body().getData();
                        manageRecyclerViewForReception(response.body().getData());
                        // prepareListData(response.body().getData().getFaqList());
                    } else {
                        Helper.somethingWentWrong(getView());
                    }
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }
            }

            @Override
            public void onFailure(Call<FairMedia> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.failureResponse(progressBar, getView());
            }

        });

    }


    private void retryLayout() {
        View retryLayout;
        retryLayout = view.findViewById(R.id.somethingWentWrongLayout);
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Helper.isInternetConnected()) {
                    if (Singleton.getInstance().getHomeState().equals("stands")) {
                        Helper.showProgressBar(progressBar);
                        callApiForCompanyDetail(false);
                    } else if (Singleton.getInstance().getHomeState().equals("reception")) {
                        callApiForFairMedia();
                        // manageRecyclerViewForReception();
                    }
                }
            }
        });
    }

    private void callApiForCompanyDetail(boolean pagination) {

        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());

        Call<CompanyDetails> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getCompanyDetail(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<CompanyDetails>() {
            @Override
            public void onResponse(Call<CompanyDetails> call, Response<CompanyDetails> response) {
                Helper.hideProgressBar(progressBar);
                Helper.hideLayouts(getView());

                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        manageRecyclerView(response.body().getData());
                        //  setData(response.body().getData());
                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<CompanyDetails> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
            }

        });

    }

    private void manageRecyclerView(Data data) {
        Singleton.getInstance().getMediaList().clear();
        MediaModel model = null;
        for (int i = 0; i < data.getCompanyList().get(0).getCompanyMedia().size(); i++) {
            if (data.getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaType().equals("video")) {
                model = null;
                model = new MediaModel(null, data.getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaVideo(), null);
                Singleton.getInstance().setMediaList(model);
            } else if (data.getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaType().equals("embed")) {
                model = null;
                model = new MediaModel(data.getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaVideo(), null, null);
                Singleton.getInstance().setMediaList(model);
            }
        }

        if (Singleton.getInstance().getMediaList().size() > 0) {


            adapter = new MediaAdapter(getParentFragmentManager());
            adapter.setOnFullScreenClickListener(this);


            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        } else
            Helper.noDataFound(view);

    }


    private void manageRecyclerViewForReception(com.vrd.gsaf.api.responses.fairMedia.Data data) {
        Singleton.getInstance().getMediaList().clear();
        MediaModel model = null;
        for (int i = 0; i < data.getMediaList().size(); i++) {
            if (data.getMediaList().get(i).getFairMediaType().equals("video")) {
                model = null;
                model = new MediaModel(null, data.getMediaList().get(i).getFairMediaVideo(), null);
                Singleton.getInstance().setMediaList(model);
            } else if (data.getMediaList().get(i).getFairMediaType().equals("embed")) {
                model = null;
                model = new MediaModel(data.getMediaList().get(i).getFairMediaVideo(), null, null);
                Singleton.getInstance().setMediaList(model);
            } else if (data.getMediaList().get(i).getFairMediaType().equals("image")) {
                model = null;
                model = new MediaModel(null, null, data.getMediaList().get(i).getFairMediaImage());
                Singleton.getInstance().setMediaList(model);
            }
        }

        if (Singleton.getInstance().getMediaList().size() > 0) {

            for (int i = 0; i < Singleton.getInstance().getMediaList().size() / 2; i++) {
                MediaModel temp = Singleton.getInstance().getMediaList().get(i);
                Singleton.getInstance().getMediaList().set(i, Singleton.getInstance().getMediaList().get(Singleton.getInstance().getMediaList().size() - i - 1));
                Singleton.getInstance().getMediaList().set(Singleton.getInstance().getMediaList().size() - i - 1, temp);
            }


            adapter = new MediaAdapter(getParentFragmentManager());
            adapter.setOnFullScreenClickListener(this);


            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        } else
            Helper.noDataFound(view);

    }


    private void onBackPress() {
        FragmentManager fm = getParentFragmentManager();
        fm.popBackStackImmediate();
        //fm.popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void startTimer() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                isViewClicked = false;
            }
        }, MIN_CLICK_INTERVAL);

    }

    @Override
    public void onClick(View view) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;

        mLastClickTime = currentClickTime;

        if (elapsedTime <= MIN_CLICK_INTERVAL)
            return;
        if (!isViewClicked) {
            isViewClicked = true;
            startTimer();
        } else {
            return;
        }
        switch (view.getId()) {
            case R.id.backImageView:
                requireActivity().onBackPressed();
                break;
        }
    }


    private void setVideoView() {
        //  seekBar.seek(100);
        proxy = MainApp.getProxy(Singleton.getInstance().getActivity());
        String proxyUrl = proxy.getProxyUrl(Singleton.getInstance().getVideoUrl());
        videoPlayer.setVideoPath(proxyUrl);
        videoPlayer.seekTo(Singleton.getInstance().getSeekTo());
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(proxyUrl,
                MediaStore.Images.Thumbnails.MINI_KIND);

        // fullScreenVideoView.requestFocus();
        videoPlayer.setOnPreparedListener(mp -> {
            setVideoProgress();
        });

        videoPlayer.setOnCompletionListener(mp -> {
            videoPlayer.pause();
            pause.setImageResource(R.drawable.play);

            // seekBar.setProgress(0);
        });

        if (Singleton.getInstance().getPlaying()) {
            videoPlayer.start();
            pause.setImageResource(R.drawable.play);

        }
        exitFullScreen();
        orientationImage();
        setPause();


    }


    private void orientationImage() {
        orientationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                potraiteMode();
                Singleton.getInstance().getBottomNavigationView().setVisibility(View.VISIBLE);
                Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            }
        });
    }

    private void exitFullScreen() {
        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoPlayer.pause();
                videoViewLayout.setVisibility(View.GONE);
                Singleton.getInstance().getBottomNavigationView().setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);

            }
        });

    }

    private void setVideoProgress() {
        //get the video duration
        current_pos = videoPlayer.getCurrentPosition();
        total_duration = videoPlayer.getDuration();

        //display video duration
        //total.setText(timeConversion((long) total_duration));
        //current.setText(timeConversion((long) current_pos));
        seekBar.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = videoPlayer.getCurrentPosition();
                    //current.setText(timeConversion((long) current_pos));
                    seekBar.setProgress((int) current_pos);
                    handler.postDelayed(this, 100);
                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 100);

        //seekbar change listner
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 100) {
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                videoPlayer.seekTo((int) current_pos);
            }
        });


    }

    //pause video
    protected void setPause() {
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoPlayer.isPlaying()) {
                    videoPlayer.pause();
                    pause.setImageResource(R.drawable.play);
                } else {
                    videoPlayer.start();
                    pause.setImageResource(R.drawable.pause);
                }
            }
        });
    }

    @Override
    public void fullScreenClick() {
        Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
        videoViewLayout.setVisibility(View.VISIBLE);
        setVideoView();
        recyclerView.setVisibility(View.GONE);

    }

    @Override
    public void orientationChange() {
        if (Singleton.getInstance().getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            landscapeMode();
        }
    }


    private void potraiteMode() {
        videoViewLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void landscapeMode() {
        if (recyclerView != null) {
            recyclerView.setVisibility(View.GONE);
            videoViewLayout.setVisibility(View.VISIBLE);

            lp = (ConstraintLayout.LayoutParams) videoViewLayout.getLayoutParams();
            Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            lp.height = displayMetrics.heightPixels - 100;
            lp.width = displayMetrics.widthPixels;
            isFullScreen = true;
            fullScreen.setVisibility(View.GONE);
            // menuImageView.setVisibility(View.GONE);
            orientationImage.setVisibility(View.VISIBLE);
            Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
            lp.setMargins(Helper.getSizeInSp(0), Helper.getSizeInSp(0), Helper.getSizeInSp(0), Helper.getSizeInSp(0));
            videoViewLayout.setLayoutParams(lp);
            if (videoPlayer.isPlaying())
                videoPlayer.stopPlayback();

            setVideoView();
        }
        //videoLayoutVisibility(true);
        // libraryPlayer(AppConstants.RECEPTION_VIDEO, true);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("Waqas", 123);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        //playVideo();
        super.onResume();
//      if (mediaData!=null) manageRecyclerViewForReception(mediaData);
        try {
            Helper.setActiveItemBottomNav(0);

        } catch (Exception e) {
            Helper.showToast("asds");
        }
        // playVideo();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}

