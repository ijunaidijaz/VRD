package com.vrd.gsaf.home.companies.companyStand;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.smarteist.autoimageslider.SliderView;
import com.vrd.gsaf.R;
import com.vrd.gsaf.activity.ExoPlayerActivity;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.compnayDetail.CompanyDetails;
import com.vrd.gsaf.api.responses.standPoll.StandPoll;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.fragments.MatchingFragment;
import com.vrd.gsaf.home.companies.companyStand.imageSlider.SliderAdapter;
import com.vrd.gsaf.home.companies.companyStand.imageSlider.SliderItems;
import com.vrd.gsaf.home.companies.companyStand.standPoll.PollAdapter;
import com.vrd.gsaf.home.companies.companyStand.standPollResult.PollResultAdapter;
import com.vrd.gsaf.home.conferenceAgenda.ConferenceAgenda;
import com.vrd.gsaf.home.courses.Courses;
import com.vrd.gsaf.home.dashboard.recruiters.Recruiters;
import com.vrd.gsaf.home.fullScreenMedia.FullScreenMedia;
import com.vrd.gsaf.home.goodies.GoodiesList;
import com.vrd.gsaf.home.homeTab.ButtonsAdapter;
import com.vrd.gsaf.home.homeTab.Home;
import com.vrd.gsaf.home.homeTab.documents.Documents;
import com.vrd.gsaf.home.homeTab.media.Media;
import com.vrd.gsaf.home.jobs.CompanyDetail;
import com.vrd.gsaf.home.jobs.Jobs;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompnayStand extends Fragment implements View.OnClickListener, View.OnTouchListener, ButtonsAdapter.OnShareClickedListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    public ButtonsAdapter adapter;
    public VideoView mainPlayer;
    public CardView sliderCardView;
    protected ConstraintLayout recyclerLayout;
    protected float dX;
    protected float dY;
    protected int lastAction;
    protected RecyclerView recyclerView;
    protected DisplayMetrics displayMetrics = new DisplayMetrics();
    protected HttpProxyCacheServer proxy;
    protected MainApp app = new MainApp();
    protected double current_pos, total_duration;
    //    FragmentCompnayStandBinding binding;
    com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView youTubePlayerView;
    ConstraintLayout videoViewLayout, tollBar, standYoutubeLayout;
    SeekBar seekBar;
    ImageView pause;
    //ImageView fullScreen;
    ImageView orientationImage;
    VideoView videoPlayer;
    private final MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            // Your code goes here
            videoViewLayoutVisibility(false);

            return true;
        }
    };
    MediaPlayer videoMediaPlay = new MediaPlayer();
    @NonNull
    com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayerCopy;
    String poll = "0";
    long lastDown;
    long lastDuration;
    RelativeLayout floatingButton;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    YouTubePlayer player;
    ImageView bannerImageView;
    private ProgressBar progressBar;
    private ImageView standFullScreen, fullScreenImageViewStandMp4, standCompanyLogoImageView, videoViewBackgroundImageView, backImageView, floatingToggle, standImageView, crossButtonsLayoutImageView, thumbnailImageView;
    private View view;
    private long mLastClickTime;
    private int index;
    private TextView titleTxt;
    private SliderView sliderView;
    private String goodies = "0";
    private Response<CompanyDetails> companyDetailResponse = null;
    private int dy, dx, diff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        binding = FragmentCompnayStandBinding.inflate(inflater, container, false);
//        view = binding.getRoot();
        Helper.lockOrientation();

        view = inflater.inflate(R.layout.fragment_compnay_stand, container, false);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            index = bundle.getInt("index");
        }
        Helper.hideKeyboard();
        initializeViews();
        Singleton.getInstance().setPreviousHomeState(Singleton.getInstance().getHomeState());
        detectBackStackChange();

        return view;
    }

    private void detectBackStackChange() {
//        getParentFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                if (getFragmentManager() != null) {
//                    if(Singleton.getInstance().getCompanyMedia()!=null) {
//                        Handler handler1 = new Handler();
//
//                        handler1.postDelayed(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                setMainVideo();
//                            }
//                        }, 2000);
//                    }
//
//                }
//            }
//        });
    }

    private void initializeViews() {
        tollBar = view.findViewById(R.id.tollBar);
        bannerImageView = view.findViewById(R.id.bannerImageView);
        standYoutubeLayout = view.findViewById(R.id.standYoutubeLayout);
        standFullScreen = view.findViewById(R.id.standFullScreen);
        fullScreenImageViewStandMp4 = view.findViewById(R.id.fullScreenImageViewStandMp4);
        sliderCardView = view.findViewById(R.id.sliderCardView);
        crossButtonsLayoutImageView = view.findViewById(R.id.crossButtonsLayoutImageView);
        standImageView = view.findViewById(R.id.standImageView);
        mainPlayer = view.findViewById(R.id.idExoPlayerVIew);
        mainPlayer.setVisibility(View.VISIBLE);
        recyclerLayout = view.findViewById(R.id.recyclerLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        floatingToggle = view.findViewById(R.id.draggable_view);
        floatingToggle.setOnClickListener(this);
        floatingButton = view.findViewById(R.id.floating_button_layout);
        floatingButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor())));
        floatingToggle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavInnerTextColor())));
        floatingButton.bringToFront();

        progressBar = view.findViewById(R.id.progressBar);
        titleTxt = view.findViewById(R.id.titleTxt);
        try {
            titleTxt.setText(Singleton.getInstance().getCompaniesData().get(index).getCompanyName());
        } catch (Exception e) {

        }
        backImageView = view.findViewById(R.id.backImageView);
        thumbnailImageView = view.findViewById(R.id.thumbnailImageView);
        thumbnailImageView.setVisibility(View.VISIBLE);
        videoViewBackgroundImageView = view.findViewById(R.id.videoViewBackgroundImageView);
        standCompanyLogoImageView = view.findViewById(R.id.standCompanyLogoImageView);
        youTubePlayerView = view.findViewById(R.id.youtubePlayer);
        sliderView = view.findViewById(R.id.imageSlider);

        videoViewLayout = view.findViewById(R.id.videoViewLayoutStand);
        seekBar = view.findViewById(R.id.seekBarMedia);
        pause = view.findViewById(R.id.pauseMedia);
        orientationImage = view.findViewById(R.id.orientationImageViewMedia);
        // fullScreen = view.findViewById(R.id.fullScreenImageViewMedia);
        videoPlayer = view.findViewById(R.id.videoPlayerMediaMedia);

        setClickListeners();
        prepareMainVideoPlayer(AppConstants.getHall1BackgroundVideo(), false, "hallBack");
        setRecylcerHeight();
        callApiForCompanyDetail();
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerLayout.getVisibility() == View.VISIBLE) {

                } else {
                    recyclerLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    stageButtonsList(poll, goodies, companyDetailResponse);
                    floatingButton.setVisibility(View.GONE);
                    slideUp(recyclerLayout);
                }
            }
        });
        floatingToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerLayout.getVisibility() == View.VISIBLE) {

                } else {
                    recyclerLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    stageButtonsList(poll, goodies, companyDetailResponse);
                    floatingButton.setVisibility(View.GONE);
                    slideUp(recyclerLayout);
                }
            }
        });
        mainLayout = view.findViewById(R.id.mainLayout);
        tollBar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        //  standImageHeight();
    }

    private void callApiForCompanyDetail() {
        Helper.showProgressBar(progressBar);
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
                        Helper.showToast("No Data Found");

                    } else if (response.body().getStatus()) {
                        Glide.with(requireActivity()).load(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + response.body().getData().getCompanyList().get(0).getCompanyStandBanner()).into(bannerImageView);
                        Helper.loadRectangleImageFromUrlWithRounded(standCompanyLogoImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + response.body().getData().getCompanyList().get(0).getCompanyStandImage(), 0);
                        setStandDimensions(response);
                        titleTxt.setText(response.body().getData().getCompanyList().get(0).getCompanyName());
                        Singleton.getInstance().setCompanyMedia(response.body().getData());
                        setMainVideo();
                        poll = response.body().getData().getCompanyList().get(0).getEnablePoll();
                        goodies = response.body().getData().getCompanyList().get(0).getEnableGoodies();
                        companyDetailResponse = response;
                        stageButtonsList(poll, goodies, companyDetailResponse);

                    } else {
                        Helper.showToast(response.body().getMsg());
                    }
                } catch (Exception e) {
                    Helper.hideProgressBar(progressBar);
                    ResponseBody response1 = response.errorBody();
                    Helper.getErrorMessage(response1);
                }

            }

            @Override
            public void onFailure(Call<CompanyDetails> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something went wrong.Please try later.");
            }

        });

    }

    private void setStandDimensions(Response<CompanyDetails> response) {
        ConstraintLayout.LayoutParams lpp =
                (ConstraintLayout.LayoutParams) standCompanyLogoImageView.getLayoutParams();
        ConstraintLayout.LayoutParams cardViewLayoutParams =
                (ConstraintLayout.LayoutParams) sliderCardView.getLayoutParams();
        String url = response.body().getData().getStandBackground();
        if (url != null) {
            String[] separated = url.split("\\.");
            String recruiter = separated[0];
            Glide.with(Singleton.getInstance().getContext())
                    .load(AppConstants.STANDS_BASE_URL + recruiter + ".png")
                    .placeholder(R.drawable.stand)
                    .centerCrop()
                    .into(standImageView);
        } else {
            Glide.with(Singleton.getInstance().getContext())
                    .load(R.drawable.stand)
                    .centerCrop()
                    .into(standImageView);
//            Helper.showToast("Stand background is null");
        }
        if (response.body().getData().getCompanyList().get(0).getCompanyStandType().equals("Bronze")) {
            lpp.bottomMargin = -210;
            standCompanyLogoImageView.setLayoutParams(lpp);
//            standImageView.setBackground(getResources().getDrawable(R.drawable.bronze_stand));
            cardViewLayoutParams.bottomMargin = 525;
            sliderCardView.setLayoutParams(cardViewLayoutParams);
        } else if (response.body().getData().getCompanyList().get(0).getCompanyStandType().equals("Gold")) {
            lpp.bottomMargin = -60;
            standCompanyLogoImageView.setLayoutParams(lpp);
            cardViewLayoutParams.bottomMargin = 470;
            sliderCardView.setLayoutParams(cardViewLayoutParams);
        } else {
            lpp.bottomMargin = -75;
            standCompanyLogoImageView.setLayoutParams(lpp);
            cardViewLayoutParams.bottomMargin = 570;
            sliderCardView.setLayoutParams(cardViewLayoutParams);
        }
    }

    private void setImageSlider(ArrayList<SliderItems> images) {

        sliderCardView.setVisibility(View.VISIBLE);
        sliderView.setVisibility(View.VISIBLE);
        sliderView.setSliderAdapter(new SliderAdapter(Singleton.getInstance().getContext(), images, null, progressBar));
        sliderView.setScrollTimeInSec(4);//set scroll delay in seconds :
        sliderView.startAutoCycle();  //  sliderView.setIndicatorEnabled(false);
        //sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        //  sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        //sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        //    sliderView.setIndicatorSelectedColor(Color.BLACK);
        //   sliderView.setIndicatorUnselectedColor(Color.GRAY);

    }

    private void setMainVideo() {

        ArrayList<SliderItems> images = new ArrayList<>();
        for (int i = 0; i < Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().size(); i++) {
            int j = i;
            if (Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(i).getIsShowFront().equals("1")) {
                if (Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaType().equals("video")) {
                    videoViewLayoutVisibility(false);
                    youTubePlayerView = view.findViewById(R.id.youtubePlayer);
                    standYoutubeLayout.setVisibility(View.VISIBLE);
                    standFullScreen.setVisibility(View.VISIBLE);
                    youTubePlayerView.setVisibility(View.VISIBLE);
                    Helper.setYoutubePlayerSettings(youTubePlayerView);
                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                            super.onReady(youTubePlayer);

                            youTubePlayerCopy = youTubePlayer;
                            if (CompnayStand.this != null && CompnayStand.this.isVisible())
                                youTubePlayer.loadVideo(Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(j).getCompanyMediaVideo(), 0);
                            else {
                                youTubePlayer.pause();
                            }
                            //youTubePlayer.pause();

                        }
                    });
                    youTubePlayerView.enableBackgroundPlayback(false);
                    standFullScreen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (youTubePlayerCopy != null) {
                                youTubePlayerCopy.pause();
                                youTubePlayerCopy.pause();
                            }
                            Fragment fragment = new FullScreenMedia();
                            //replaceFragment(new VideoFragment(), "video");

                            Bundle args = new Bundle();
                            args.putString("type", "youtube");
                            args.putString("video", Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(j).getCompanyMediaVideo());
                            fragment.setArguments(args);
                            getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("video")
                                    .commit();
                        }
                    });
                    break;
                } else if (Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaType().equals("embed")) {
                    videoViewLayoutVisibility(true);
                    youTubePlayerView.setVisibility(View.GONE);
                    startVideoPlayer(Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(j).getCompanyMediaVideo());
                    break;
                }
            }
        }

        for (int i = 0; i < Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().size(); i++) {
            if (Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaType().equals("image")) {
                {
                    SliderItems sliderItems = new SliderItems(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() +
                            Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaImage(), "Waqas");
                    images.add(sliderItems);
                }
            }
        }

        if (images.size() > 0)
            setImageSlider(images);

    }

    private void videoViewLayoutVisibility(boolean b) {
        if (b) {
            videoViewLayout.setVisibility(View.VISIBLE);
            pause.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
            //fullScreen.setVisibility(View.VISIBLE);
            videoPlayer.setVisibility(View.VISIBLE);
            videoViewBackgroundImageView.setVisibility(View.VISIBLE);
        } else {
            videoViewLayout.setVisibility(View.GONE);
            pause.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
            //fullScreen.setVisibility(View.GONE);
            videoPlayer.setVisibility(View.GONE);
            videoViewBackgroundImageView.setVisibility(View.GONE);
        }
    }

    private void setVideoProgress() {
        //get the video duration
        playVideo();
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

    @Override
    public void onStop() {
        sliderView = null;
        sliderCardView = null;
        super.onStop();
    }

    private void startVideoPlayer(String companyMediaVideo) {
        fullScreenImageViewStandMp4.setVisibility(View.VISIBLE);
        proxy = MainApp.getProxy(Singleton.getInstance().getActivity());
        String proxyUrl = proxy.getProxyUrl(companyMediaVideo);

        videoPlayer.setVideoPath(proxyUrl);
        videoPlayer.seekTo(100);
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(companyMediaVideo,
                MediaStore.Images.Thumbnails.MINI_KIND);

        videoPlayer.setOnPreparedListener(mp -> {
            videoViewBackgroundImageView.setVisibility(View.GONE);
            setVideoProgress();

        });

        videoPlayer.setOnErrorListener(mOnErrorListener);

        videoPlayer.setOnCompletionListener(mp -> {
            videoPlayer.pause();
            pause.setImageResource(R.drawable.play);
        });

        fullScreenImageViewStandMp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), ExoPlayerActivity.class).putExtra("video", companyMediaVideo).putExtra("seekTo", videoPlayer.getCurrentPosition() + 5000));
                videoPlayer.pause();
//                Fragment fragment = new FullScreenMedia();
//                //replaceFragment(new VideoFragment(), "video");
//                if (videoMediaPlay!=null){
//                    videoMediaPlay.setVolume(0,0);
//                }
//                Bundle args = new Bundle();
//                args.putString("type", "mp4");
//                args.putInt("seekTo", videoPlayer.getCurrentPosition()+4000);
//                args.putString("video", companyMediaVideo);
//                fragment.setArguments(args);
//                getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
//                        .setReorderingAllowed(true)
//                        .addToBackStack("video")
//                        .commit();

            }
        });
        //fullScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                long now = System.currentTimeMillis();
//                if (now - mLastClickTime < MIN_CLICK_INTERVAL) {
//                    return;
//                }
//                mLastClickTime = now;
//                Singleton.getInstance().setVideoUrl(companyMediaVideo);
//                Singleton.getInstance().setPlaying(false);
//                Singleton.getInstance().setSeekTo(videoPlayer.getCurrentPosition());
//
//                if (videoPlayer.isPlaying()) {
//                    Singleton.getInstance().setPlaying(true);
//                    videoPlayer.pause();
//                    pause.setImageResource(R.drawable.play);
//                }
//                fullScreenClick(companyMediaVideo);
//
//            }
//        });

        setPause();
    }

    public void fullScreenClick(String companyMediaVideo) {
        Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
        videoViewLayout.setVisibility(View.VISIBLE);
        setVideoView(companyMediaVideo);
        recyclerView.setVisibility(View.GONE);

    }

    private void setVideoView(String companyMediaVideo) {
        //  seekBar.seek(100);
        String proxyUrl = proxy.getProxyUrl(companyMediaVideo);
        videoPlayer.setVideoPath(proxyUrl);
        videoPlayer.seekTo(Singleton.getInstance().getSeekTo());
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(proxyUrl,
                MediaStore.Images.Thumbnails.MINI_KIND);

        // fullScreenVideoView.requestFocus();
        videoPlayer.setOnPreparedListener(mp -> {
            videoMediaPlay = mp;
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
        setPause();


    }

    private void exitFullScreen() {
//        fullScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                videoPlayer.pause();
//                videoViewLayout.setVisibility(View.GONE);
//                Singleton.getInstance().getBottomNavigationView().setVisibility(View.VISIBLE);
//                recyclerView.setVisibility(View.VISIBLE);
//
//            }
//        });

    }

    //pause video
    protected void setPause() {
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
            }
        });
    }

    private void playVideo() {
        if (videoPlayer.isPlaying()) {
            videoPlayer.pause();
            pause.setImageResource(R.drawable.play);
        } else {
            videoPlayer.start();
            pause.setImageResource(R.drawable.pause);
        }
    }

    private void prepareMainVideoPlayer(String path, boolean b, String video) {
        Helper.showProgressBar(progressBar);
        String proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(path);
        mainPlayer.setVideoPath(proxyUrl);
        mainPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(b);
                mainPlayer.setSaveEnabled(true);
                mainPlayer.seekTo(1);

                mainPlayer.start();
                thumbnailImageView.setVisibility(View.GONE);
                mainPlayer.setBackground(null);
                Helper.hideProgressBar(progressBar);


            }
        });


        mainPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Singleton.getInstance().setIsHome(false);
            }
        });


    }

    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        floatingButton.setOnTouchListener(this);
        crossButtonsLayoutImageView.setOnClickListener(this);


    }

    private void onBackPress() {
        youTubePlayerView.release();
        getActivity().getSupportFragmentManager().popBackStack();

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
                onBackPress();
                break;
            case R.id.crossButtonsLayoutImageView:
                floatingButton.setVisibility(View.VISIBLE);
                slideDown(recyclerLayout);
                break;

        }
    }

    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        recyclerView.setVisibility(View.GONE);
        recyclerLayout.setVisibility(View.GONE);
        view.clearAnimation();
    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private void manageButtonRecylcerView() {
        try {
            adapter = new ButtonsAdapter(getParentFragmentManager(), Singleton.getInstance().getButtonsList());
            recyclerView.setHasFixedSize(true);
            adapter.setOnShareClickedListener(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(Singleton.getInstance().getContext(), "manageRecyclerView", Toast.LENGTH_SHORT).show();
        }
    }

    private void standImageHeight() {
        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        ViewGroup.LayoutParams params = standImageView.getLayoutParams();
        params.height = (height / 2) - 100;
        standImageView.setLayoutParams(params);
    }

    private void setRecylcerHeight() {
        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = (height / 2);
        recyclerView.setLayoutParams(params);


    }

    private void stageButtonsList(String poll, String goodies, Response<CompanyDetails> response) {

        Singleton.getInstance().getButtonsList().clear();
        if (response != null) {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideRecruitersButtonCompanyStand() == 0 && response.body().getData().getCompanyList().get(0).getEnableRecruiters().equals("1"))
                Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getRecruiters());
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideJobsButtonCompanyStand() == 0 && response.body().getData().getCompanyList().get(0).getEnableJobs().equals("1"))
                Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJobs());
            if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableCourses() == 1 && Singleton.getInstance().getFairData().getFair().getOptions().getHideCoursesButtonCompanyStand() == 0 && response.body().getData().getCompanyList().get(0).getEnableCourses().equals("1"))
                Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourses());
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideWebinarsButtonCompanyStand() == 0 && response.body().getData().getCompanyList().get(0).getEnableWebinars().equals("1"))
                Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaWebinars());
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideMediaButtonCompanyStand() == 0 && response.body().getData().getCompanyList().get(0).getEnableMedia().equals("1"))
                Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMedia());
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideDocumentsButtonCompanyStand() == 0 && response.body().getData().getCompanyList().get(0).getEnableDocuments().equals("1"))
                Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDocuments());
            if (goodies.equals("1"))
                Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoodies());
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideAboutButtonCompanyStand() == 0 && response.body().getData().getCompanyList().get(0).getEnableAbout().equals("1"))
                Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAbout());
            if (poll.equals("1"))
                Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStandPolling());
            if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableSlotMatching() == 1 && response.body().getData().getCompanyList().get(0).getEnableMatchingSlots().equals("1"))
                Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMatchingSlots());

        }
        Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMainHall());
        Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getReception());

        manageButtonRecylcerView();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                lastDown = System.currentTimeMillis();

                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                lastDuration = System.currentTimeMillis() - lastDown;

//                if (lastAction == MotionEvent.ACTION_DOWN) {
//                    hallButtonsList();
//                    floatingButton.setVisibility(View.GONE);
//                    slideUp(recyclerLayout);
//
//                }
                if (lastDuration < 150) {
//                    stageButtonsList(poll, goodies, companyDetailResponse);
//                    floatingButton.setVisibility(View.GONE);
//                    slideUp(recyclerLayout);
                    recyclerLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    stageButtonsList(poll, goodies, companyDetailResponse);
                    floatingButton.setVisibility(View.GONE);
                    slideUp(recyclerLayout);

                }
                break;


            default:
                return false;
        }
        return true;
    }

    private void replaceFragment(Fragment fragment, String tag) {

        Bundle bundle = new Bundle();
        bundle.putInt("index", 1);
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();

    }


    @Override
    public void ShareClicked(String url, int videoIndex) {
        try {
            if (youTubePlayerView != null)
                youTubePlayerCopy.pause();
            youTubePlayerCopy.pause();
        } catch (Exception e) {

        }
        try {
            if (videoPlayer.isPlaying())
                videoPlayer.pause();
        } catch (Exception e) {

        }


        Singleton.getInstance().setHomeState("stands");
        if (url.equals("recruiter")) {
            try {
                youTubePlayerCopy.pause();
            } catch (Exception e) {

            }
            slideDown(recyclerLayout);
            Singleton.getInstance().setHomeState("stands");
            floatingButton.setVisibility(View.VISIBLE);
            replaceFragment(new Recruiters(), "media");


        } else if (url.equals("jobs")) {
            try {
                youTubePlayerCopy.pause();
            } catch (Exception e) {

            }
            slideDown(recyclerLayout);
            Singleton.getInstance().setHomeState("stands");
            floatingButton.setVisibility(View.VISIBLE);
            if (Jobs.getInstance() != null) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove(Jobs.getInstance());
                trans.commit();
            }
            replaceFragment(new Jobs(), "jobs");
//            Singleton.getInstance().setRemoveJobFrag(true);
//            Jobs.removeFragment.setValue(false);


        } else if (url.equals("webinars")) {
            try {
                youTubePlayerCopy.pause();
            } catch (Exception e) {

            }
            Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getCompaniesData().get(index).getCompanyId());

            Singleton.getInstance().setFromStandToWebinar(true);
            Singleton.getInstance().setFromSpeakersToWebinar(false);

            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            Singleton.getInstance().setHomeState("stands");
            Singleton.getInstance().setWebinarVideo(false);

            replaceFragment(new ConferenceAgenda(), "webinars");


        } else if (url.equals("media") && Singleton.getInstance().getHomeState().equals("stands")) {
            try {
                youTubePlayerCopy.pause();
            } catch (Exception e) {

            }
            Singleton.getInstance().setIsHome(false);
            floatingButton.setVisibility(View.VISIBLE);
            //  videoViewLayout.setVisibility(View.GONE);
            Singleton.getInstance().setHomeView(view);
            replaceFragment(new Media(), "media");
            slideDown(recyclerLayout);

        } else if (url.equals("about") && Singleton.getInstance().getHomeState().equals("stands")) {
            try {
                youTubePlayerCopy.pause();
            } catch (Exception e) {

            }
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            replaceFragment(new CompanyDetail(), null);


        } else if (url.equals("documents")) {
            try {
                youTubePlayerCopy.pause();
            } catch (Exception e) {

            }
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            replaceFragment(new Documents(), null);


        } else if (url.equals("reception") && Singleton.getInstance().getHomeState().equals("stands")) {
            try {
                youTubePlayerCopy.pause();
            } catch (Exception e) {

            }
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            Singleton.getInstance().setHomeState("hallBack");
            replaceFragment(new Home(), "home");

        } else if (url.equals("courses") && Singleton.getInstance().getHomeState().equals("stands")) {
            try {
                youTubePlayerCopy.pause();
            } catch (Exception e) {

            }
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            Singleton.getInstance().setIsDashboard(true);
            // Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());
            replaceFragment(new Courses(), null);


        } else if (url.equals("hall") && Singleton.getInstance().getHomeState().equals("stands")) {
            try {
                youTubePlayerCopy.pause();
            } catch (Exception e) {

            }
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            Singleton.getInstance().setHomeState("hall");
            replaceFragment(new Home(), "home");

        } else if (url.equals("standPoll") && Singleton.getInstance().getHomeState().equals("stands")) {
            try {
                youTubePlayerCopy.pause();
            } catch (Exception e) {

            }
            slideDown(recyclerLayout);
            // Singleton.getInstance().setHomeState("stands");
            callApiForStandPoll();

        } else if (url.equals("goodie") && Singleton.getInstance().getHomeState().equals("stands")) {
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            //  Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());
            replaceFragment(new GoodiesList(), null);


        } else if (url.equalsIgnoreCase("matching slots") && Singleton.getInstance().getHomeState().equals("stands")) {
            Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getCompaniesData().get(index).getCompanyId());

            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            Singleton.getInstance().setFromStandToMatchingSlots(true);
            //  Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());
            replaceFragment(new MatchingFragment(), null);


        }

    }

    private void callApiForStandPoll() {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());


        Call<StandPoll> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getStandPolling(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<StandPoll>() {
            @Override
            public void onResponse(Call<StandPoll> call, Response<StandPoll> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {
                        Helper.showToast("No Data Found");
                        floatingButton.setVisibility(View.VISIBLE);

                    } else if (response.body().getStatus()) {
                        if (response.body().getData().getPollList().size() > 0) {
                            Singleton.getInstance().setStandPollData(response.body().getData());
                            floatingButton.setVisibility(View.GONE);
                            if (!response.body().getData().getAttended()) {
                                showPollDialog();
                            } else {
                                showPollResultDialog();
                            }
                        } else {
                            Helper.showToast("No Data Found");
                            floatingButton.setVisibility(View.VISIBLE);
                        }

                    }
                } catch (Exception e) {
                    Helper.showToast("Something Went Wrong");
                    floatingButton.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<StandPoll> call, Throwable t) {
                Helper.showToast("Something Went Wrong");
                floatingButton.setVisibility(View.VISIBLE);
                Helper.hideProgressBar(progressBar);
            }

        });

    }

    private void showPollResultDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_poll_result, null);
        RecyclerView recyclerView;
        PollResultAdapter adapter;
        dialogBuilder.setView(dialogView);
        dialogView.requestLayout();
        AlertDialog alertDialog = dialogBuilder.create();
        //    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textView = dialogView.findViewById(R.id.titleTxt);
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStandPolling() + " "
                + Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSurveyResult());
        //textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAre());

        adapter = new PollResultAdapter(Singleton.getInstance().getStandPollData().getPollList());
        recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ImageView crossImageView = dialogView.findViewById(R.id.crossButtonsLayoutImageView);

        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                long now = System.currentTimeMillis();
//                if (now - mLastClickTime < MIN_CLICK_INTERVAL) {
//                    return;
//                }
//                mLastClickTime = now;

                alertDialog.cancel();

                floatingButton.setVisibility(View.VISIBLE);

            }
        });

        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                floatingButton.setVisibility(View.VISIBLE);
            }
        });
//
    }


    private void showPollDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_poll_questions, null);
        RecyclerView recyclerView;
        PollAdapter adapter;
        dialogBuilder.setView(dialogView);
        dialogView.requestLayout();
        AlertDialog alertDialog = dialogBuilder.create();
        //    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textView = dialogView.findViewById(R.id.titleTxt);
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStandPolling());
        //textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAre());


        Button saveSurvey = dialogView.findViewById(R.id.saveBtn);
        saveSurvey.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSavePoll());
        Helper.setButtonColorWithDrawable(saveSurvey, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(), Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());

        adapter = new PollAdapter(alertDialog, Singleton.getInstance().getStandPollData().getPollList());
        recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        saveSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < MIN_CLICK_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                floatingButton.setVisibility(View.VISIBLE);
                //JSONArray jsonArray = adapter.getPollAnswers();
                // Log.d("json123", jsonArray.toString());
                callApiToSaveStandPOll(adapter.getPollAnswers());
                alertDialog.cancel();


            }
        });

        ImageView crossImageView = dialogView.findViewById(R.id.crossButtonsLayoutImageView);

        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < MIN_CLICK_INTERVAL) {
                    return;
                }
                mLastClickTime = now;

                alertDialog.cancel();

                floatingButton.setVisibility(View.VISIBLE);

            }
        });


//
//        if (alertDialog.getWindow() != null){
//            alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        }
        //alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        //  alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                floatingButton.setVisibility(View.VISIBLE);
            }
        });
//


//        InputMethodManager imm = (InputMethodManager) Singleton.getInstance().getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);


    }

    private void callApiToSaveStandPOll(JSONArray pollAnswers) {

        Helper.showProgressBar(progressBar);
        JsonObject jsonObject = new JsonObject();
        JsonArray images = new JsonArray();

        try {

            jsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());

            jsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
            jsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());


            for (int i = 0; i < pollAnswers.length(); i++) {
                JsonObject test = new JsonObject();
                String pollId = pollAnswers.getJSONObject(i).getString("poll_id");
                String optionId = pollAnswers.getJSONObject(i).getString("option_id");
                String pollType = pollAnswers.getJSONObject(i).getString("poll_type");
                test.addProperty("poll_id", pollId);
                test.addProperty("poll_type", pollType);
                test.addProperty("option_id", optionId);
                images.add(test);
            }
            jsonObject.add("poll_data", images);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<StandPoll> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).saveStandPolling(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, jsonObject);
        call.enqueue(new Callback<StandPoll>() {
            @Override
            public void onResponse(Call<StandPoll> call, Response<StandPoll> response) {
                Helper.hideProgressBar(progressBar);
                floatingButton.setVisibility(View.VISIBLE);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {
                        Helper.showToast("No Data Found");
                    } else if (response.body().getStatus()) {
                        // Singleton.getInstance().setExitPOllData(response.body().getData());
                        Helper.showToast("Poll saved Successfully");
                        pollResultSavedDialog();
                    }
                } catch (Exception e) {
                    Helper.showToast("Something Went Wrong");
                }
            }

            @Override
            public void onFailure(Call<StandPoll> call, Throwable t) {
                Helper.showToast("Something Went Wrong");
                floatingButton.setVisibility(View.VISIBLE);
                Helper.hideProgressBar(progressBar);
            }
        });

    }


    private void pollResultSavedDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_stand_poll_saved, null);
        TextView thankyouTxt = dialogView.findViewById(R.id.thankyouTxt);
        TextView descriptionTxt = dialogView.findViewById(R.id.descriptionTxt);
        thankyouTxt.setText("Thank You");
        descriptionTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTransStringThankForTakingTimeComplete());
        TextView schedulesText = dialogView.findViewById(R.id.recruiterNmae4Txt);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


    }

    @Override
    public void onPause() {
        super.onPause();
        // youTubePlayerView.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        // prepareMainVideoPlayer(AppConstants.HALL_BACKGROUND_VIDEO, false, "hallBack");

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

}