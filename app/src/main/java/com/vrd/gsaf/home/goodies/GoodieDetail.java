package com.vrd.gsaf.home.goodies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.google.gson.JsonObject;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.goodies.Goodies;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.fullScreenMedia.FullScreenMedia;
import com.vrd.gsaf.home.whereBy.WebViewActivity;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoodieDetail extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    protected YouTubePlayerView youTubePlayerView;
    ConstraintLayout youtubeLayout;
    com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer player;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView, companyImageView, iconImageView, youtubeFullScreen;
    private ImageView goodieImageView;
    private TextView companyName, linkText;
    private TextView goodieTitleTxt;
    private TextView detailValueTxt;
    private TextView titleTxt, detailTxt;
    private Button downlaodGoodieBtn;
    private int index;
    private long mLastClickTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        view = inflater.inflate(R.layout.fragment_goodie_detail, container, false);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            index = bundle.getInt("index");
            // handle your code here.
        }

        Helper.hideKeyboard();
        initializeViews();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        iconImageView = view.findViewById(R.id.iconImageView);
        youtubeLayout = view.findViewById(R.id.youtubeLayout);
        youtubeFullScreen = view.findViewById(R.id.youtubeFullScreen);
        youTubePlayerView = view.findViewById(R.id.youtubePlayer);
        Helper.setYoutubePlayerSettings(youTubePlayerView);
        progressBar = view.findViewById(R.id.progressBar);
        goodieImageView = view.findViewById(R.id.goodieImageView);
        companyImageView = view.findViewById(R.id.companyImageView);
        backImageView = view.findViewById(R.id.backImageView);
        goodieTitleTxt = view.findViewById(R.id.goodieTitleTxt);
        linkText = view.findViewById(R.id.linkText);
        titleTxt = view.findViewById(R.id.titleTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoodie() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getBag());
        companyName = view.findViewById(R.id.companyNameTxt);
        detailValueTxt = view.findViewById(R.id.detailValueTxt);
        detailTxt = view.findViewById(R.id.detailTxt);
        downlaodGoodieBtn = view.findViewById(R.id.downlaodGoodieBtn);
        if (Singleton.getInstance().getGoodies()) {
            if (Singleton.getInstance().getGoodiesList().get(index).getIsAdded() == 0) {
                downlaodGoodieBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAddToBag());
                Helper.setButtonColorWithDrawable(downlaodGoodieBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                        Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
            } else {
                downlaodGoodieBtn.setText("Remove");
                Helper.setButtonColorWithDrawable(downlaodGoodieBtn, "#b42626", "#FFFFFF");
            }
        } else {
            downlaodGoodieBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDownload() + " " + Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoodie());
            Helper.setButtonColorWithDrawable(downlaodGoodieBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                    Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
        }

        detailValueTxt.setMovementMethod(new ScrollingMovementMethod());

        Helper.loadHtml(detailValueTxt, Singleton.getInstance().getGoodiesList().get(index).getGoodiebagDescription());

        if (Singleton.getInstance().getGoodiesList().get(index).getGoodiebagDescription().equals("")) {
            detailTxt.setVisibility(View.GONE);
            detailValueTxt.setVisibility(View.GONE);
            // detailTxt.setVisibility(View.GONE);
        }
        companyName.setText(Singleton.getInstance().getGoodiesList().get(index).getCompanyName());
        goodieTitleTxt.setText(Singleton.getInstance().getGoodiesList().get(index).getGoodiebagName());

        Helper.loadRectangleImageFromUrlWithRounded(companyImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getGoodiesList().get(index).getCompanyLogo(), 25);


        checkType();
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        setClickListeners();
    }

    private void checkType() {
        if (Singleton.getInstance().getGoodiesList().get(index).getGoodiebagType().equals("video")) {
            youtubeLayout.setVisibility(View.VISIBLE);
            Helper.setYoutubePlayerSettings(youTubePlayerView);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                    super.onReady(youTubePlayer);
                    player = youTubePlayer;
                    if (GoodieDetail.this != null && GoodieDetail.this.isVisible()) {
                        youTubePlayer.loadVideo(Singleton.getInstance().getGoodiesList().get(index).getGoodiebagVideo(), 0);
                    }
//                    if (!Singleton.getInstance().getHomeState().equals("stands")) {
//                        youTubePlayerView.setVisibility(View.GONE);
//                        youTubePlayer.pause();
//                    }

                }
            });

            youtubeFullScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();

                    player.pause();
                    player.pause();
                    Fragment fragment = new FullScreenMedia();
                    //replaceFragment(new VideoFragment(), "video");
                    Bundle args = new Bundle();
                    args.putString("type", "youtube");
                    args.putString("video", Singleton.getInstance().getGoodiesList().get(index).getGoodiebagVideo());
                    fragment.setArguments(args);
                    getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
                            .setReorderingAllowed(true)
                            .addToBackStack("video")
                            .commit();


                }
            });
        } else if (Singleton.getInstance().getGoodiesList().get(index).getGoodiebagType().equals("image")) {
            goodieImageView.setVisibility(View.VISIBLE);
            Helper.loadRectangleImageFromUrlWithRounded(goodieImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getGoodiesList().get(index).getGoodiebagImage(), 25);
        } else if (Singleton.getInstance().getGoodiesList().get(index).getGoodiebagType().equals("links")) {
            linkText.setVisibility(View.VISIBLE);
            linkText.setText(Singleton.getInstance().getGoodiesList().get(index).getGoodiebagLink());

//            Helper.loadRectangleImageFromUrlWithRounded(goodieImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getGoodiesList().get(index).getGoodiebagImage(), 25);
            linkText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Helper.goToUrl(Singleton.getInstance().getGoodiesList().get(index).getGoodiebagLink());
                }
            });
        } else if (Singleton.getInstance().getGoodiesList().get(index).getGoodiebagType().equals("document")) {
            iconImageView.setVisibility(View.VISIBLE);
            iconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getGoodiesList().get(index).getGoodiebagDocument();
                    MainApp.getAppContext().startActivity(new Intent(MainApp.getAppContext(), WebViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("url", url).putExtra("isCv", true));

                }
            });
        }


    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        downlaodGoodieBtn.setOnClickListener(this);
    }


    private void onBackPress() {
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
            case R.id.downlaodGoodieBtn:
                if (Singleton.getInstance().getGoodies()) {
                    if (downlaodGoodieBtn.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAddToBag()))
                        callAPiToAddGoodie();
                    else
                        callApiToRemoveGoodie();
                } else
                    downlaodGoodie();
                break;

        }
    }

    private void downlaodGoodie() {
        if (Singleton.getInstance().getGoodiesList().get(index).getGoodiebagType().equals("video")) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + Singleton.getInstance().getGoodiesList().get(index).getGoodiebagVideo())));

        } else if (Singleton.getInstance().getGoodiesList().get(index).getGoodiebagType().equals("image")) {
            String url = Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getGoodiesList().get(index).getGoodiebagImage();
            String extension = url.substring(url.lastIndexOf("."));
//            Helper.downloadImage(url, true);
//            Helper.goToUrl(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getGoodiesList().get(index).getGoodiebagImage());
//            final DownloadFile downloadTask = new DownloadFile(Singleton.getInstance().getContext(), progressBar, Singleton.getInstance().getLoginData().getUser().getUserTaskList());
//         downloadTask.execute(url);
            Helper.showProgressBar(progressBar);
            AndroidNetworking.download(url, String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), System.currentTimeMillis() + extension)
                    .build()
                    .startDownload(new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            Helper.hideProgressBar(progressBar);
                            Toast.makeText(getActivity(), "Download successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(ANError anError) {
                            Helper.hideProgressBar(progressBar);
                            Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        } else if (Singleton.getInstance().getGoodiesList().get(index).getGoodiebagType().equals("links")) {
            Helper.goToUrl(Singleton.getInstance().getGoodiesList().get(index).getGoodiebagLink());

        } else if (Singleton.getInstance().getGoodiesList().get(index).getGoodiebagType().equals("document")) {
            Helper.showProgressBar(progressBar);

//            final DownloadFile downloadTask = new DownloadFile(Singleton.getInstance().getContext(), progressBar, Singleton.getInstance().getLoginData().getUser().getUserTaskList());
            //   downloadTask.execute("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf");
            //  downloadTask.execute("https://www.ucrhealth.org/wp-content/uploads/2020/04/sample.pdf");
            String url = Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getGoodiesList().get(index).getGoodiebagDocument();
            String extension = url.substring(url.lastIndexOf("."));
//            downloadTask.execute(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getGoodiesList().get(index).getGoodiebagDocument());
            AndroidNetworking.download(url, String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), System.currentTimeMillis() + extension)
                    .build()
                    .startDownload(new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            Helper.hideProgressBar(progressBar);
                            Toast.makeText(getActivity(), "Download successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(ANError anError) {
                            Helper.hideProgressBar(progressBar);
                            Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }

    private void callApiToRemoveGoodie() {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());
        JsonObject.addProperty("goodie_id", Singleton.getInstance().getGoodiesList().get(index).getGoodiebagId());
        Call<Goodies> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).addGoodieBag(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<Goodies>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.goodies.Goodies> call, Response<Goodies> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        Helper.showToast(response.body().getMsg());
                    } else if (response.body().getStatus()) {
//                        Helper.showToast(response.body().getMsg());
                        downlaodGoodieBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAddToBag());
                        Singleton.getInstance().getGoodiesList().get(index).setIsAdded(0);
                        Helper.setButtonColorWithDrawable(downlaodGoodieBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
                        GoodiesList.goodieAdapter.notifyDataSetChanged();
                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
                    Helper.showToast("Something went wrong.Please try again.");
                }
            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.goodies.Goodies> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something went wrong.Please try again.");

            }

        });
    }

    private void callAPiToAddGoodie() {

        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());
        JsonObject.addProperty("goodie_id", Singleton.getInstance().getGoodiesList().get(index).getGoodiebagId());
        Call<Goodies> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).addGoodieBag(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<Goodies>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.goodies.Goodies> call, Response<Goodies> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        Helper.showToast(response.body().getMsg());
                    } else if (response.body().getStatus()) {
//                        Helper.showToast(response.body().getMsg());
                        downlaodGoodieBtn.setText("Remove");
                        Helper.setButtonColorWithDrawable(downlaodGoodieBtn, "#b42626", "#FFFFFF");
                        Singleton.getInstance().getGoodiesList().get(index).setIsAdded(1);
                        GoodiesList.goodieAdapter.notifyDataSetChanged();


                        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
                        editor.putInt("goodieCount", response.body().getData().getCandidateTotalGoodieBag());
                        Singleton.getInstance().setGoodieCount(response.body().getData().getCandidateTotalGoodieBag());
                        editor.apply();
                        Singleton.getInstance().getMainActivity().goodieBagBadge();
                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
                    Helper.showToast("Something went wrong.Please try again.");
                }
            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.goodies.Goodies> call, Throwable t) {
                Helper.showToast("Something went wrong.Please try again.");
            }

        });
    }

    @Override
    public void onStop() {
        try {
            if (player != null) {
                player.pause();
            }
//            youTubePlayerView.release();
        } catch (Exception e) {

        }
        super.onStop();
    }
}