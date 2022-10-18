package com.vrd.gsaf.home.homeTab;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.smarteist.autoimageslider.SliderView;
import com.vrd.gsaf.R;
import com.vrd.gsaf.activity.ExoPlayerActivity;
import com.vrd.gsaf.activity.MainActivity;
import com.vrd.gsaf.adapters.HallModel;
import com.vrd.gsaf.adapters.HallRecyclerAdapter;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.compnayDetail.CompanyDetails;
import com.vrd.gsaf.api.responses.fairDetail.Data;
import com.vrd.gsaf.api.responses.fairDetail.Keywords;
import com.vrd.gsaf.api.responses.fairDetail.Options;
import com.vrd.gsaf.api.responses.standPoll.StandPoll;
import com.vrd.gsaf.api.responses.stands.Company;
import com.vrd.gsaf.api.responses.stands.StandsData;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.fragments.MatchingFragment;
import com.vrd.gsaf.home.AboutUs;
import com.vrd.gsaf.home.companies.companyStand.imageSlider.SliderAdapter;
import com.vrd.gsaf.home.companies.companyStand.imageSlider.SliderItems;
import com.vrd.gsaf.home.companies.companyStand.standPoll.PollAdapter;
import com.vrd.gsaf.home.companies.companyStand.standPollResult.PollResultAdapter;
import com.vrd.gsaf.home.conferenceAgenda.ConferenceAgenda;
import com.vrd.gsaf.home.courses.Courses;
import com.vrd.gsaf.home.dashboard.recruiters.Recruiters;
import com.vrd.gsaf.home.dashboard.schedules.Schedules;
import com.vrd.gsaf.home.faq.FAQ;
import com.vrd.gsaf.home.fullScreenMedia.FullScreenMedia;
import com.vrd.gsaf.home.goodies.GoodiesList;
import com.vrd.gsaf.home.homeTab.chat.Chat;
import com.vrd.gsaf.home.homeTab.documents.Documents;
import com.vrd.gsaf.home.homeTab.media.Media;
import com.vrd.gsaf.home.jobs.CompanyDetail;
import com.vrd.gsaf.home.jobs.Jobs;
import com.vrd.gsaf.home.stageConferenceAgenda.StageConferenceAgenda;
import com.vrd.gsaf.model.ContentModel;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.CenterZoomLayoutManager;
import com.vrd.gsaf.utility.Helper;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment implements View.OnClickListener, View.OnTouchListener, ButtonsAdapter.OnShareClickedListener {

    public static RecyclerView recyclerView, hallRecyclerView;
    public static ConstraintLayout recyclerLayout, timerLayout;
    public static Home instance;
    private final int index = Singleton.getInstance().getLanguageIndex();
    private final ArrayList<SliderItems> hallStandImages = new ArrayList<>();
    //  public SliderView hallViewPager;
    public ImageView receptionFullScreen, standFullScreen, fullScreenImageViewStandMp4, bannerImageView, standCompanyLogoImageView, videoViewBackgroundImageView, menuImageView, floatingToggle, thumbnailImageView, crossButtonsLayoutImageView, standImageView, pause;
    public ButtonsAdapter adapter;
    public VideoView videoView, mainPlayer, billBoardVideoView;
    public SliderView sliderView;
    public CardView sliderCardView;
    public ProgressBar progressBar;
    public HallRecyclerAdapter hallRecyclerAdapter;
    protected TextView daysTxt, hoursTxt, minutesTxt, secondsTxt;
    protected View view;
    protected Button enterBtn;
    protected long mLastClickTime = 0;
    protected YouTubePlayerView youTubePlayerView, receptionYoutubePlayer;
    protected float dX;
    protected float dY;
    protected int lastAction;
    protected SeekBar seekBar;
    protected WebView webView;
    protected double current_pos, total_duration;
    protected int video_index = 0;
    protected ConstraintLayout videoViewLayoutStand, standYoutubeLayout, receptionYoutubeLayout;
    protected DisplayMetrics displayMetrics = new DisplayMetrics();
    protected Handler mHandler, handler;
    RelativeLayout floatingButton;
    SliderAdapter hallImageAdapter;
    ConstraintLayout.LayoutParams lp;
    long lastDown;
    long lastDuration;
    YouTubePlayer player;
    private Date startDate;
    private VideoView videoPlayer;
    private ConstraintLayout toolBar;
    private TextView titleTxt;
    private Home home;
    private String poll = "0";
    private String goodies = "0";
    private Response<CompanyDetails> companyDetailResponse = null;
    private Boolean flag;
    private boolean isVideoPlaying = false;
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

    // slide the view from its current position to below itself
    public static void slideDown(View view) {
        if (view != null) {
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,                 // fromYDelta
                    view.getHeight()); // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);
            view.clearAnimation();
            recyclerView.setVisibility(View.GONE);
            recyclerLayout.setVisibility(View.GONE);
        }
    }

    public static Home getInstance() {
        if (instance == null) {
            return new Home();
        } else return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();
        Singleton.getInstance().setHome(this);
        instance = this;
//        Singleton.getInstance().setHomeState("home");
        if (Singleton.getInstance().getIsLoggedIn() != null && Singleton.getInstance().getIsLoggedIn())
            MainActivity.getInstance().getFairHallsApi();
        if (Singleton.getInstance().getFairData() != null) {
            if (Singleton.getInstance().getFairData().getFair().getLayout().equals("layout48")) {
                view = inflater.inflate(R.layout.fragment_home48, container, false);
            } else if (Singleton.getInstance().getFairData().getFair().getLayout().equals("layout53")) {
                view = inflater.inflate(R.layout.fragment_home_53, container, false);
            } else if (Singleton.getInstance().getFairData().getFair().getLayout().equals("layout11")) {
                view = inflater.inflate(R.layout.fragment_home_11, container, false);
            } else if (Singleton.getInstance().getFairData().getFair().getLayout().equals(AppConstants.LAYOUT66)) {
                view = inflater.inflate(R.layout.fragment_home_66, container, false);
            } else if (Singleton.getInstance().getFairData().getFair().getLayout().equals(AppConstants.LAYOUT60)) {
                view = inflater.inflate(R.layout.fragment_home_60, container, false);
            } else if (Singleton.getInstance().getFairData().getFair().getLayout().equals(AppConstants.LAYOUT15)) {
                view = inflater.inflate(R.layout.fragment_home_15, container, false);
            } else {
                view = inflater.inflate(R.layout.fragment_home_60, container, false);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_home_66, container, false);
        }
//        binding = DataBindingUtil.inflate(
//                inflater, R.layout.fragment_home, container, false);
        MainApp.getAppContext().comitChat();
        toolBar = view.findViewById(R.id.tollBar);
        titleTxt = view.findViewById(R.id.titleTxt);
        for (int i = 0; i < getChildFragmentManager().getBackStackEntryCount(); i++) {
            getChildFragmentManager().popBackStackImmediate();
        }
        try {
            if (Singleton.getInstance().getFairData() != null)
                startDate = Singleton.getInstance().getFormat().parse(Singleton.getInstance().getFairData().getFair().getStartTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Singleton.getInstance().setIsHome(true);
        initializeViews();
        Singleton.getInstance().setViewPager(false);
        this.setRetainInstance(true);

        long diff = startDate.getTime() - new Date().getTime();
        Singleton.getInstance().setLive(diff < 0);
        if (!Singleton.getInstance().getLive()) {
            Menu menu = Singleton.getInstance().getNavigationView().getMenu();
            MenuItem target = menu.findItem(R.id.nav_goodie_bags);
            target.setVisible(false);
        }
//        HomeActivity.getInstance().showByShortName();
//        getLifecycle().addObserver(receptionYoutubePlayer);
//        new Thread() {
//            public void run() {
//                receptionYoutubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//                    @Override
//                    public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
//                        super.onReady(youTubePlayer);
//                        youTubePlayer.loadVideo(Singleton.getInstance().getFairData().getFair().getReceptionVidLink(), 0);
//
//                        youTubePlayer.mute();
//                    }
//                });
////
//            }
//        }.start();

        if (Singleton.getInstance().getLoginData() != null) {
            Singleton.getInstance().setFlag(false);
        }
        if (Singleton.getInstance().getFairData() != null) {
            MainActivity.getInstance().setLanguage();
            MainActivity.getInstance().setColors();
        }
//        checkForAppUpdate();
        return view;
    }


    private void playBillBoardVideo() {
//        if (Singleton.getInstance().getLoginData() != null) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Singleton.getInstance().getHomeState().equals("home")) {
                    String proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getFairData().getFair().getFairVideo());
//        mediaController.setAnchorView(mainPlayer);
//        mainPlayer.setMediaController(mediaController);
                    billBoardVideoView.setVisibility(View.VISIBLE);
                    billBoardVideoView.setVideoPath(proxyUrl);

                    billBoardVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.setLooping(true);
                            billBoardVideoView.setSaveEnabled(true);
                            billBoardVideoView.start();
                            mediaPlayer.setVolume(0, 0);

                        }
                    });

                    billBoardVideoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), ExoPlayerActivity.class)
                                    .putExtra("seekTo", billBoardVideoView.getCurrentPosition())
                                    .putExtra("video", Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getFairData().getFair().getFairVideo()));
//                replaceFragment(new FullScreenMedia(), "video");
                        }
                    });
                }
            }
        }, 500);
    }

    public void prepareMainVideoPlayer(String path, boolean b, String video) {
//        MediaController mediaController = new MediaController(Singleton.getInstance().getContext());
        Helper.showProgressBar(progressBar);

        // Helper.showToast(String.valueOf(Singleton.getInstance().getProxy().isCached(path)));
        String proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(path);
//        mediaController.setAnchorView(mainPlayer);
//        mainPlayer.setMediaController(mediaController);
        thumbnailImageView.setVisibility(View.VISIBLE);
        mainPlayer.setVideoPath(proxyUrl);
        mainPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isVideoPlaying = true;
                Helper.hideProgressBar(progressBar);
                mp.setLooping(b);
                mainPlayer.setSaveEnabled(true);
                mainPlayer.start();
                if (video.equals("home")) {
                    Singleton.getInstance().setHomeState("home");
                    toolBar(true, Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getHome());
                    if (Singleton.getInstance().getLoginData() != null) {
                        enterBtn.setVisibility(View.VISIBLE);
                    } else if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableLoginFront() == 0) {
                        enterBtn.setVisibility(View.VISIBLE);
                    }
                    thumbnailImageView.setVisibility(View.GONE);
                    if (Singleton.getInstance().getFairData().getFair().getFairVideo() != "") {
                        new Handler().postDelayed(() -> playBillBoardVideo(), 300);
                    }
                } else if (video.equals("reception")) {
                    Helper.disableTouch();
                    enterBtn.setVisibility(View.GONE);
                    billBoardVideoView.setVisibility(View.GONE);
                } else if (video.equals("hallBackground")) {
                    hallButtonsList();
                    thumbnailImageView.setVisibility(View.GONE);
                } else if (video.equals("hall")) {
                    Helper.disableTouch();
                    toolBar(false, null);
                    try {
                        receptionYoutubePlayer.release();
                        receptionYoutubePlayer.setVisibility(View.GONE);
                        receptionYoutubeLayout.setVisibility(View.GONE);
                        receptionFullScreen.setVisibility(View.GONE);
                        videoLayoutVisibility(false);
                        recyclerLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    } catch (Exception e) {

                    }
                    thumbnailImageView.setVisibility(View.GONE);
                } else if (video.equals("hallBack")) {
                    Helper.disableTouch();
                    videoViewLayoutVisibility(false);

                    toolBar(false, null);
                    floatingButton.setVisibility(View.GONE);
                    try {
                        thumbnailImageView.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Helper.showToast("Something went wrong.Please try later.");
                    }
                } else if (video.equals("receptionBack")) {
                    enterBtn.setVisibility(View.GONE);
                    toolBar(true, Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getReception());

                    Singleton.getInstance().setHomeState("reception");
                    enterBtn.setVisibility(View.GONE);
                    thumbnailImageView.setVisibility(View.GONE);
                    setMainVideoOnReception();
                    if (recyclerLayout.getVisibility() == View.VISIBLE)
                        floatingButton.setVisibility(View.GONE);
                    else
                        floatingButton.setVisibility(View.VISIBLE);

                } else if (video.equals("standBackground")) {
                    thumbnailImageView.setVisibility(View.GONE);
                    //   HallViewPagerClick(Singleton.getInstance().getStandIndex());
                    HallViewPagerClick2(Singleton.getInstance().getStandIndex());

                }


            }

        });
        mainPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Error loading video");
                return false;
            }
        });

        mainPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                isVideoPlaying = false;
                Singleton.getInstance().setIsHome(false);
                if (video.equals("reception")) {
                    Helper.enableTouch();

                    enterBtn.setVisibility(View.GONE);
                    Singleton.getInstance().setHomeState("reception");
                    prepareMainVideoPlayer(AppConstants.getReceptionBackVideo(), true, "receptionBack");
                    toolBar(true, Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getReception());

                } else if (video.equals("hall")) {
                    Helper.enableTouch();
                    enterBtn.setVisibility(View.GONE);
                    toolBar(true, Singleton.getInstance().getHallName());
                    Singleton.getInstance().setHomeState("hall");

                    prepareMainVideoPlayer(AppConstants.getHall1BackgroundVideo(), true, "hallBackground");
                    // setHallViewPager(Singleton.getInstance().getStandsDataAgainstHall());
                    setHallViewPager2(Singleton.getInstance().getStandsDataAgainstHall());

                } else if (video.equals("hallBack")) {
                    Singleton.getInstance().setHomeState("reception");

                    Helper.enableTouch();
                    toolBar(true, Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getReception());
                    videoViewLayoutVisibility(false);
                    prepareMainVideoPlayer(AppConstants.getReceptionBackVideo(), true, "receptionBack");

                }
            }
        });


    }


    @SuppressLint("CutPasteId")
    private void initializeViews() {
        try {
            home = this;
            videoPlayer = view.findViewById(R.id.videoPlayer);
            standCompanyLogoImageView = view.findViewById(R.id.standCompanyLogoImageView);
            progressBar = view.findViewById(R.id.progressBar);
            menuImageView = view.findViewById(R.id.menuImageView);
            videoViewBackgroundImageView = view.findViewById(R.id.videoViewBackgroundImageView);
            thumbnailImageView = view.findViewById(R.id.thumbnailImageView);
            standImageView = view.findViewById(R.id.standImageView);
            enterBtn = view.findViewById(R.id.enterBtn);
            if (Singleton.getInstance().getFairData() != null) {
                enterBtn.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor()));
                GradientDrawable drawable = (GradientDrawable) enterBtn.getBackground();
                drawable.setStroke(20, Color.parseColor("#33ffffff"));
                drawable.setColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterBackground()));
            }
            if (Singleton.getInstance().getLoginData() == null && Singleton.getInstance().getFairData() != null) {
                if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableLoginFront() != 0 || Singleton.getInstance().getFairData().getFair().getOptions().getDisable_login_button_landing_page_front() != 0) {
                    enterBtn.setVisibility(View.GONE);
                }
                enterBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLogin());
            } else if (Singleton.getInstance().getFairData() != null)
                enterBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEnter());
            sliderView = view.findViewById(R.id.imageSlider);
            sliderCardView = view.findViewById(R.id.sliderCardView);
            sliderView.setVisibility(View.GONE);
            sliderCardView.setVisibility(View.GONE);
            youTubePlayerView = view.findViewById(R.id.youtubePlayer);


            receptionYoutubePlayer = view.findViewById(R.id.receptionYoutubePlayer);
            Singleton.setEnterButton(enterBtn);
            Gson gson = new Gson();
            String json = Singleton.getInstance().getSharedPreferences().getString("fairDetails", null);
            if (json != null) {
                Singleton.getInstance().setFairData(gson.fromJson(json, Data.class));
//                Singleton.getInstance().setLanguageIndex(Singleton.getInstance().getSharedPreferences().getInt("languageIndex", 0));
            }

            recyclerView = view.findViewById(R.id.recyclerView);
            hallRecyclerView = view.findViewById(R.id.hallRecyclerView);
            floatingButton = view.findViewById(R.id.floating_button_layout);
            floatingToggle = view.findViewById(R.id.draggable_view);
            floatingToggle.setOnClickListener(this);
            Singleton.getInstance().setFloatingButton(floatingButton);
            if (Singleton.getInstance().getFairData() != null) {
                floatingButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor())));
                floatingToggle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavInnerTextColor())));
            }
            recyclerLayout = view.findViewById(R.id.recyclerLayout);

            crossButtonsLayoutImageView = view.findViewById(R.id.crossButtonsLayoutImageView);
            recyclerLayout.setVisibility(View.GONE);
            //hallViewPager = new HallViewPager(view, this);
            videoView = view.findViewById(R.id.videoPlayer);
            billBoardVideoView = view.findViewById(R.id.billBoardVideoView);
            seekBar = view.findViewById(R.id.seekBar);
            videoViewLayoutStand = view.findViewById(R.id.videoViewLayoutStand);
            bannerImageView = view.findViewById(R.id.bannerImageView);
            lp = (ConstraintLayout.LayoutParams) videoViewLayoutStand.getLayoutParams();
            //fullScreen = view.findViewById(R.id.fullScreenImageView);
            // screenRotation = view.findViewById(R.id.orientationImageView);
            pause = view.findViewById(R.id.pause);
            mainPlayer = view.findViewById(R.id.idExoPlayerVIew);
            mainPlayer.setVisibility(View.VISIBLE);
            mHandler = new Handler();
            handler = new Handler();

            if (Singleton.getInstance().getHomeState().equals("reception")) {
                prepareMainVideoPlayer(AppConstants.getReceptionBackVideo(), true, "receptionBack");

            } else if (Singleton.getInstance().getHomeState().equals("hallBack")) {
                prepareMainVideoPlayer(AppConstants.getHall1BackVideo(), false, "hallBack");
            } else if (Singleton.getInstance().getHomeState().equals("hall")) {
                try {
                    Singleton.getInstance().setHallId(Singleton.getInstance().getFairHalls().get(0).getHallId());
                    Singleton.getInstance().setActiveHall(Singleton.getInstance().getFairHalls().get(0).getHallName());
                    Singleton.getInstance().setHallName(Singleton.getInstance().getFairHalls().get(0).getHallName());
                    Singleton.getInstance().setBackToReception(true);

                    callApiForHallData(false, true);
                } catch (Exception e) {

                }
            } else if (Singleton.getInstance().getHomeState().equals("stands")) {
                try {
                    flag = true;
                    prepareMainVideoPlayer(AppConstants.getHall1BackgroundVideo(), true, "standBackground");

                } catch (Exception e) {
                    Helper.showToast("Something went wrong.Please try later.");
                }
                //  prepareMainVideoPlayer(AppConstants.HALL_VIDEO, false, "hall");

            } else {
                if (Singleton.getInstance().getFairData() != null)
                    prepareMainVideoPlayer(AppConstants.getHomeVideo(), true, "home");

            }
            setRecylcerHeight();
            setClickListeners();
            // standImageHeight();

            // try {
            //   if(Singleton.getInstance().getUserLogin()!=null && Singleton.getInstance().getUserLogin()) {
//            if (Singleton.getInstance().getIsLoggedIn() != null && Singleton.getInstance().getIsLoggedIn()) {

            setTimer();
//            }
            if (Singleton.getInstance().getFairData() != null && Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatFront().equals(1)) {
                MainActivity.getInstance().chipNavigationBar.setItemEnabled(R.id.nav_chat, false, Singleton.getMessageTitle());
            }
            //   }

            //Helper.startWhereBy();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //initializeHallViewPager();
        manageHallRecyclerView();
        // countDownStart();

    }


    public void setTimer() throws ParseException {
        if (Singleton.getInstance().getFairData() != null) {
            startDate = Singleton.getInstance().getFormat().parse(Singleton.getInstance().getFairData().getFair().getStartTime());

            // Date startDate = format.parse(Singleton.getInstance().getFairData().getFair().getStartTime());
            long diff = startDate.getTime() - new Date().getTime();
            timerLayout = view.findViewById(R.id.timerLayout);
            // diff =300;
            if (diff > 0) {
//                billBoardVideoView.setVisibility(View.GONE);
                timerLayout.setVisibility(View.VISIBLE);
                setTimerLayoutColor();
//                enterBtn.setVisibility(View.GONE);
                countDownStart();
            } else {
                if (Singleton.getInstance().getTouchEnabled()) {
                    if (Singleton.getInstance().getLoginData() != null) {
                        enterBtn.setVisibility(View.VISIBLE);
                    } else if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableLoginFront() == 0) {
                        enterBtn.setVisibility(View.VISIBLE);
                    }
                }
                timerLayout.setVisibility(View.GONE);
            }
        }
    }

    private void setTimerLayoutColor() {
        Button daysImageView = view.findViewById(R.id.daysImageView);
        Button hoursImageView = view.findViewById(R.id.hoursImageView);
        Button minutesImageView = view.findViewById(R.id.minutesImageView);
        Button secondsImageView = view.findViewById(R.id.secondsImageView);

        Helper.setButtonColorWithDrawableAndStroke(daysImageView, Singleton.getInstance().getFairData().getFair().getOptions().getCountdownBackgroundColor(), Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor(), 10);
        Helper.setButtonColorWithDrawableAndStroke(hoursImageView, Singleton.getInstance().getFairData().getFair().getOptions().getCountdownBackgroundColor(), Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor(), 10);
        Helper.setButtonColorWithDrawableAndStroke(minutesImageView, Singleton.getInstance().getFairData().getFair().getOptions().getCountdownBackgroundColor(), Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor(), 10);
        Helper.setButtonColorWithDrawableAndStroke(secondsImageView, Singleton.getInstance().getFairData().getFair().getOptions().getCountdownBackgroundColor(), Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor(), 10);

//        Drawable myDrawable = ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.days);
//        myDrawable.setColorFilter(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()), PorterDuff.Mode.SRC_IN);


    }

    public void countDownStart() {
//        Drawable myDrawable = ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.days);
//        myDrawable.setColorFilter(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()), PorterDuff.Mode.SRC_IN);
//        myDrawable.setColorFilter(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()), PorterDuff.Mode.DST);

        daysTxt = view.findViewById(R.id.daysTxt);
        hoursTxt = view.findViewById(R.id.hoursTxt);
        minutesTxt = view.findViewById(R.id.minutesTxt);
        secondsTxt = view.findViewById(R.id.secondsTxt);
        daysTxt.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()));
        hoursTxt.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()));
        minutesTxt.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()));
        secondsTxt.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()));

        TextView textView = view.findViewById(R.id.daysHeadingTxt);
        textView.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()));
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDays());


        textView = view.findViewById(R.id.secondsHeadingTxt);
        textView.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()));
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSeconds());


        textView = view.findViewById(R.id.hoursHeadingTxt);
        textView.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()));
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getHour());


        textView = view.findViewById(R.id.minutesHeadingTxt);
        textView.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()));
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMinutes());


        Handler handler = new Handler();
        Runnable runnable;
        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    } else {
                        //Set the optional Date format here for Devices Running ANDROID VERSION BELOW N
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    }
// Please here set your event date//YYYY-MM-DD
                    Date currentDate = new Date();
                    if (!currentDate.after(startDate)) {
                        long diff = startDate.getTime() - currentDate.getTime();

                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;

                        //  time.setText(String.valueOf(days) + " days \n" + String.valueOf(hours) + " hours \n " + String.valueOf(minutes) + " minutes \n" + String.valueOf(seconds) + " seconds");
                        daysTxt.setText(String.valueOf(days));
                        hoursTxt.setText(String.valueOf(hours));
                        minutesTxt.setText(String.valueOf(minutes));
                        secondsTxt.setText(String.valueOf(seconds));
                    } else {
                        handler.removeCallbacks(this::run);
                        timerLayout.setVisibility(View.GONE);
                        if (Singleton.getInstance().getLoginData() != null) {
                            enterBtn.setVisibility(View.VISIBLE);
                        } else if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableLoginFront() == 0) {
                            enterBtn.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

    }

    private String formatMilliSecondsToTime(long milliseconds) {

        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : "
                + twoDigitString(seconds);
    }

    private String twoDigitString(long number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        menuImageView.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        floatingButton.setOnTouchListener(this);
//        floatingToggle.setOnTouchListener(this);
        crossButtonsLayoutImageView.setOnClickListener(this);
        floatingButton.setVisibility(View.GONE);
        if (Singleton.getInstance().getLoginData() != null)
            MainActivity.getInstance().goodieBagBadge();
    }

    private void standImageHeight() {
        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        ViewGroup.LayoutParams params = standImageView.getLayoutParams();
        params.height = (height / 2);
        standImageView.setLayoutParams(params);
    }

    private void setRecylcerHeight() {
        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = (height / 2);
        recyclerView.setLayoutParams(params);


    }

    private void receptionButtonsList() {
        Singleton.getInstance().getButtonsList().clear();
        if (Singleton.getInstance().getFairHalls().size() == 0) {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideExhibitorButtonReception() == 0)
                Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getExhibitorsHall());
        }
        for (int i = 0; i < Singleton.getInstance().getFairHalls().size(); i++) {
//            if (Singleton.getInstance().getFairHalls().get(i).getShowHall().equals("Y"))
            Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairHalls().get(i).getHallName());
        }
        Options options = Singleton.getInstance().getFairData().getFair().getOptions();
        Keywords keywords = Singleton.getKeywords();
        List<String> buttons = Singleton.getInstance().getButtonsList();
        assert keywords != null;


        if (options.getHideWebinarsButtonReception() == 0)
            buttons.add(keywords.getFairMetaWebinars());
        if (options.getHideChatButtonReception() == 0)
            buttons.add(keywords.getChat());
        if (options.getHideMediaButtonReception() == 0)
            buttons.add(keywords.getMedia());
        if (options.getHideDocumentsButtonReception() == 0)
            buttons.add(keywords.getDocuments());
        if (options.getHideInterviewsButtonReception() == 0)
            buttons.add(keywords.getFairMetaInterviews());
        if (options.getHideSupportButtonReception() == 0)
            buttons.add(keywords.getSupport());
        if (options.getEnable_conference_agenda_button() == 1)
            buttons.add(keywords.getConferenceAgenda());
        if (options.getEnable_stage_conference_agenda_button() == 1)
            buttons.add(keywords.getStage() + "\n" + keywords.getConferenceAgenda());
        if (options.getEnable_speaker_agenda_button() == 1)
            buttons.add(keywords.getSpeakers_agenda());


        manageButtonRecylcerView();
    }

    private void hallButtonsList() {
        Singleton.getInstance().getButtonsList().clear();
        int size = Singleton.getInstance().getFairHalls().size();
        if (size > 1) {
            for (int i = 0; i < Singleton.getInstance().getFairHalls().size(); i++) {
//            if (Singleton.getInstance().getFairHalls().get(i).getShowHall().equals("Y"))
                Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairHalls().get(i).getHallName());
            }
        }
        Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getReception());


        floatingButton.setVisibility(View.VISIBLE);
        manageButtonRecylcerView();
    }

    private void stageButtonsList(String poll, String goodies, Response<CompanyDetails> response) {
        Singleton.getInstance().getButtonsList().clear();
        if (companyDetailResponse != null) {

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
        }
        Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getHall());
        Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getReception());

        manageButtonRecylcerView();
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

    private void manageHallRecyclerView() {
        if (Singleton.getInstance().getHomeState().equals("hall")) {
            try {
                hallRecyclerAdapter = new HallRecyclerAdapter(getParentFragmentManager());
                hallRecyclerView.setHasFixedSize(true);
                //  hallRecyclerAdapter.setOnShareClickedListener(this);
                // hallRecyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
                hallRecyclerView.setLayoutManager(new CenterZoomLayoutManager(Singleton.getInstance().getContext(), LinearLayoutManager.HORIZONTAL, false));
                hallRecyclerView.setAdapter(hallRecyclerAdapter);
                // CenterZoomLayoutManager centerZoomLayoutManager=new CenterZoomLayoutManager(Singleton.getInstance().getContext(),1,true);
            } catch (Exception e) {
                Toast.makeText(Singleton.getInstance().getContext(), "manageRecyclerView", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void onBackPress() {
        try {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getParentFragmentManager().getBackStackEntryCount() > 1) {
                        FragmentManager fm = getParentFragmentManager();
                        List<Fragment> frags = fm.getFragments();
                        boolean hasHome = false;
                        for (Fragment f : frags) {
                            if (f instanceof Home) {
                                hasHome = true;
                                break;
                            }
                        }
                        if (hasHome)
                            fm.popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    } else {
                        if (Singleton.getInstance().getHomeState().equals("stands")) {
                            onBackFromStandToHall();


                        } else if (Singleton.getInstance().getHomeState().equals("hall")) {
                            if (Singleton.getInstance().getBackToReception())
                                backToReceptionFromHall();
                            else {
                                backToHomeFromHall();
                                Singleton.getInstance().setHomeState("home");
                                // replaceFragment(new Home(), "home");
                            }

                        }

                    }
                    Helper.enableTouch();
                }
            }, 1000);
        } catch (Exception e) {
            Log.d("onBackPress", e.toString());
        }
    }

    public void backToWebinar() {
        slideDown(recyclerLayout);
        Singleton.getInstance().setWebinarVideo(true);
        floatingButton.setVisibility(View.VISIBLE);
        if (Singleton.getInstance().getWebinarToHall()) {

        } else if (Singleton.getInstance().getWebinarToReception()) {

        }


        if (Singleton.getInstance().getHomeState().equals("stands")) {

            Singleton.getInstance().setFromSpeakersToWebinar(false);
            Singleton.getInstance().setHomeState("stands");
        } else if (Singleton.getInstance().getHomeState().equals("reception")) {

            Singleton.getInstance().setFromStandToWebinar(false);
            Singleton.getInstance().setFromSpeakersToWebinar(false);
            Singleton.getInstance().setHomeState("reception");
        }
        //  replaceFragment(new ConferenceAgenda(), "webinars");
        Singleton.getInstance().getMainActivity().replaceFragment(new ConferenceAgenda(), null);

    }


    private void backToHomeFromHall() {
        videoViewLayoutVisibility(false);
        hallRecyclerView.setVisibility(View.GONE);

        toolBar(false, null);
        floatingButton.setVisibility(View.GONE);
        try {
            thumbnailImageView.setVisibility(View.GONE);
            //    Singleton.getInstance().getFloatingButton().setVisibility(View.GONE);
            //floatingButton.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Helper.showToast("Something went wrong.Please try later.");
        }
        prepareMainVideoPlayer(AppConstants.getHomeVideo(), true, "home");


    }

    private void onBackFromStandToHall() {
        if (recyclerLayout.getVisibility() == View.VISIBLE)
            slideDown(recyclerLayout);
        //hideLayout();
        toolBar(true, Singleton.getInstance().getHallName());
        youTubePlayerView.release();
        youTubePlayerView.setVisibility(View.GONE);
        if (standYoutubeLayout != null) standYoutubeLayout.setVisibility(View.GONE);
        if (standFullScreen != null) standFullScreen.setVisibility(View.GONE);
        if (fullScreenImageViewStandMp4 != null)
            fullScreenImageViewStandMp4.setVisibility(View.GONE);
        standImageView.setVisibility(View.GONE);
        bannerImageView.setVisibility(View.GONE);
        standCompanyLogoImageView.setVisibility(View.GONE);

        // hallViewPager.setVisibility(View.VISIBLE);
        hallRecyclerView.setVisibility(View.VISIBLE);

        videoLayoutVisibility(false);
        Singleton.getInstance().setHomeState("hall");
        hallRecyclerView.setVisibility(View.VISIBLE);
        manageHallRecyclerView();

        floatingButton.setVisibility(View.VISIBLE);
        sliderCardView.setVisibility(View.GONE);
        sliderView.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        youTubePlayerView.release();
        youTubePlayerView.setVisibility(View.GONE);
        if (standYoutubeLayout != null) standYoutubeLayout.setVisibility(View.GONE);
        if (standFullScreen != null) standFullScreen.setVisibility(View.GONE);
        hallButtonsList();
    }

    private void backToReceptionFromHall() {

        videoLayoutVisibility(false);
        //  hallViewPager.setVisibility(View.GONE);
        hallRecyclerView.setVisibility(View.GONE);

        Singleton.getInstance().setHomeState("reception");

        prepareMainVideoPlayer(AppConstants.getHall1BackVideo(), false, "hallBack");
    }

    @Override
    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        switch (view.getId()) {
            case R.id.backImageView:
                onBackPress();
                break;
            case R.id.menuImageView:
                Helper.menuClick();
                break;
            case R.id.draggable_view:
                recyclerLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                if (Singleton.getInstance().getHomeState().equals("reception"))
                    receptionButtonsList();
                else if (Singleton.getInstance().getHomeState().equals("stands"))
                    stageButtonsList(poll, goodies, companyDetailResponse);
                floatingButton.setVisibility(View.GONE);
                slideUp(recyclerLayout);
                break;
            case R.id.crossButtonsLayoutImageView:
                floatingButton.setVisibility(View.VISIBLE);
                recyclerLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                slideDown(recyclerLayout);
                break;
            case R.id.enterBtn:
                long diff = startDate.getTime() - new Date().getTime();
                if (Singleton.getInstance().getLoginData() != null) {
                    if (diff < 0) {
                        Singleton.getInstance().setLive(true);
                        //if (enterBtn.getText().equals( Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEnter())) {
                        Singleton.getInstance().setIsHome(false);
                        // absPlayerInternal.seekTo(1, 0);
                        enterBtn.setVisibility(View.GONE);
                        prepareMainVideoPlayer(AppConstants.getHomeToReceptionVideo(), false, "reception");
                        //  }
                    } else {
                        Singleton.getInstance().setLive(false);
                        showPreLiveDialog(true);
                    }
                } else {
                    // showPreLiveDialog(false);
                    Singleton.getInstance().getMainActivity().goToLogin();
//                    Singleton.getInstance().setLoginFragmentFlag(true);
//                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//                    FragmentManager fm = getChildFragmentManager();
//                    for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
//                        getChildFragmentManager().popBackStackImmediate();
//                    }
//                    Singleton.getInstance().setUserLogin(null);
//                    fm.popBackStackImmediate();
//                    Singleton.getInstance().setFlag(true);
//                    fragmentTransaction.replace(R.id.frameLayout, new Login());
//                    fragmentTransaction.commit();
//                    Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
//                    Singleton.getInstance().getNav_bottom_home().setVisibility(View.GONE);

                }

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
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
                if (lastDuration < 150) {
                    recyclerLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (Singleton.getInstance().getHomeState().equals("reception"))
                        receptionButtonsList();
                    else if (Singleton.getInstance().getHomeState().equals("stands"))
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

    public void slideUp(View view) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerLayout.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private void replaceFragment(Fragment fragment, String tag) {
        Bundle args = new Bundle();
        if (tag != null) {
            if (tag.equals("video")) {
                args.putString("type", "mp4");
                args.putInt("seekTo", billBoardVideoView.getCurrentPosition());
                args.putString("video", Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getFairData().getFair().getFairVideo());
            } else {
                args.putInt("index", 1);
            }
        }
        fragment.setArguments(args);
        this.getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();

    }

    @Override
    public void ShareClicked(String url, int videoIndex) {
        if (youTubePlayerView != null)
            youTubePlayerView.release();
        if (receptionYoutubePlayer != null)
            receptionYoutubePlayer.release();
        try {
            if (videoView.isPlaying())
                videoView.pause();
        } catch (Exception e) {

        }


        if (url.equals("hall") && !Singleton.getInstance().getHomeState().equals("stands")) {
            slideDown(recyclerLayout);

            if (Helper.isInternetConnected()) {
                Singleton.getInstance().setBackToReception(true);
                callApiForHallData(!Singleton.getInstance().getHomeState().equals("hall"), false);

            } else
                floatingButton.setVisibility(View.VISIBLE);


        } else if (url.equals("hall") && Singleton.getInstance().getHomeState().equals("stands")) {
            hallButtonsList();
            onBackFromStandToHall();
        } else if (url.equals("interview") && Singleton.getInstance().getHomeState().equals("reception")) {
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            //
            //  Fragment(new Schedules(), null);
            Singleton.getInstance().getMainActivity().replaceFragment(new Schedules(), null);


        } else if (url.equalsIgnoreCase("matching slots") && Singleton.getInstance().getHomeState().equals("stands")) {
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            //  Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());
            replaceFragment(new MatchingFragment(), null);


        } else if (url.equals("recruiter") && Singleton.getInstance().getHomeState().equals("stands")) {
            slideDown(recyclerLayout);
            Singleton.getInstance().setHomeState("stands");
            floatingButton.setVisibility(View.VISIBLE);
            // replaceFragment(new Recruiters(), "media");
            Singleton.getInstance().getMainActivity().replaceFragment(new Recruiters(), null);


        } else if (url.equals("jobs") && Singleton.getInstance().getHomeState().equals("stands")) {
            slideDown(recyclerLayout);
            Singleton.getInstance().setHomeState("stands");
            floatingButton.setVisibility(View.VISIBLE);
            //replaceFragment(new Jobs(), "jobs");
            Singleton.getInstance().getMainActivity().replaceFragment(new Jobs(), null);


        } else if (url.equals("webinars")) {
            Bundle bundle = null;
            slideDown(recyclerLayout);
            Singleton.getInstance().setWebinarVideo(true);

            floatingButton.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().getHomeState().equals("stands")) {
                Singleton.getInstance().setFromSpeakersToWebinar(false);
                Singleton.getInstance().setHomeState("stands");
                Singleton.getInstance().setFromStandToWebinar(true);
                Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());
                bundle = new Bundle();
                bundle.putInt("index", 1);
            } else if (Singleton.getInstance().getHomeState().equals("reception")) {

                Singleton.getInstance().setFromStandToWebinar(false);
                Singleton.getInstance().setFromSpeakersToWebinar(false);
                Singleton.getInstance().setHomeState("reception");
            }
            //  replaceFragment(new ConferenceAgenda(), "webinars");
            Fragment conferenceAgenda = new ConferenceAgenda();
            conferenceAgenda.setArguments(bundle);
            Singleton.getInstance().getMainActivity().replaceFragment(conferenceAgenda, null);


        }else if (url.equals("conferenceAgenda")) {
            Bundle bundle = null;
            bundle = new Bundle();
            slideDown(recyclerLayout);
            Singleton.getInstance().setWebinarVideo(true);

            floatingButton.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().getHomeState().equals("stands")) {
                Singleton.getInstance().setFromSpeakersToWebinar(false);
                Singleton.getInstance().setHomeState("stands");
                Singleton.getInstance().setFromStandToWebinar(true);
                Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());
                bundle.putInt("index", 1);
            } else if (Singleton.getInstance().getHomeState().equals("reception")) {

                Singleton.getInstance().setFromStandToWebinar(false);
                Singleton.getInstance().setFromSpeakersToWebinar(false);
                Singleton.getInstance().setHomeState("reception");
            }
            //  replaceFragment(new ConferenceAgenda(), "webinars");
            Fragment conferenceAgenda = new ConferenceAgenda();
            bundle.putBoolean("isFromSideBar", true);
            bundle.putBoolean("isFromButtons", true);
            conferenceAgenda.setArguments(bundle);
            Singleton.getInstance().getMainActivity().replaceFragment(conferenceAgenda, null);


        }else if (url.equals("stageConferenceAgenda")) {
            Bundle bundle = null;
            bundle = new Bundle();
            slideDown(recyclerLayout);
            Singleton.getInstance().setWebinarVideo(true);

            floatingButton.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().getHomeState().equals("stands")) {
                Singleton.getInstance().setFromSpeakersToWebinar(false);
                Singleton.getInstance().setHomeState("stands");
                Singleton.getInstance().setFromStandToWebinar(true);
                Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());

                bundle.putInt("index", 1);
            } else if (Singleton.getInstance().getHomeState().equals("reception")) {

                Singleton.getInstance().setFromStandToWebinar(false);
                Singleton.getInstance().setFromSpeakersToWebinar(false);
                Singleton.getInstance().setHomeState("reception");
            }
            //  replaceFragment(new ConferenceAgenda(), "webinars");
            Fragment stageConferenceAgenda = new StageConferenceAgenda();
            bundle.putBoolean("isFromSideBar", true);
            bundle.putBoolean("isFromButtons", true);
            stageConferenceAgenda.setArguments(bundle);
            Singleton.getInstance().getMainActivity().replaceFragment(stageConferenceAgenda, null);


        }else if (url.equals("speakersAgenda")) {
            Bundle bundle = null;
            bundle = new Bundle();
            slideDown(recyclerLayout);
            Singleton.getInstance().setWebinarVideo(true);

            floatingButton.setVisibility(View.VISIBLE);
            if (Singleton.getInstance().getHomeState().equals("stands")) {
                Singleton.getInstance().setFromSpeakersToWebinar(false);
                Singleton.getInstance().setHomeState("stands");
                Singleton.getInstance().setFromStandToWebinar(true);
                Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());

                bundle.putInt("index", 1);
            } else if (Singleton.getInstance().getHomeState().equals("reception")) {

                Singleton.getInstance().setFromStandToWebinar(false);
                Singleton.getInstance().setFromSpeakersToWebinar(false);
                Singleton.getInstance().setHomeState("reception");
            }
            //  replaceFragment(new ConferenceAgenda(), "webinars");
            Fragment conferenceAgenda = new ConferenceAgenda();
            bundle.putBoolean("speakersAgenda",true);
            bundle.putBoolean("isFromButtons", true);
            conferenceAgenda.setArguments(bundle);
            Singleton.getInstance().getMainActivity().replaceFragment(conferenceAgenda, null);


        } else if (url.equals("media") && Singleton.getInstance().getHomeState().equals("reception")) {
            Singleton.getInstance().setIsHome(false);
            floatingButton.setVisibility(View.VISIBLE);
            // videoViewLayout.setVisibility(View.GONE);
            Singleton.getInstance().setHomeView(view);
            //  replaceFragment(new Media(), "media");
            slideDown(recyclerLayout);
            Singleton.getInstance().getMainActivity().replaceFragment(new Media(), null);


        } else if (url.equals("media") && Singleton.getInstance().getHomeState().equals("stands")) {
            Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());
            Singleton.getInstance().setIsHome(false);
            floatingButton.setVisibility(View.VISIBLE);
            //  videoViewLayout.setVisibility(View.GONE);
            Singleton.getInstance().setHomeView(view);
            //    replaceFragment(new Media(), "media");
            slideDown(recyclerLayout);
            Singleton.getInstance().getMainActivity().replaceFragment(new Media(), null);


        } else if (url.equals("about") && Singleton.getInstance().getHomeState().equals("stands")) {
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());
            //  replaceFragment(new CompanyDetail(), null);
            Singleton.getInstance().getMainActivity().replaceFragment(new CompanyDetail(), null);


        } else if (url.equals("about") && Singleton.getInstance().getHomeState().equals("reception")) {
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());
            //replaceFragment(new CompanyDetail(), null);
            Singleton.getInstance().getMainActivity().replaceFragment(new AboutUs(), null);

        } else if (url.equals("documents")) {
            if (Singleton.getInstance().getHomeState().equals("reception")) {
                slideDown(recyclerLayout);
                floatingButton.setVisibility(View.VISIBLE);
                //  replaceFragment(new Documents(), "documents");
                Singleton.getInstance().getMainActivity().replaceFragment(new Documents(), null);
            } else if (Singleton.getInstance().getHomeState().equals("stands")) {
                slideDown(recyclerLayout);
                floatingButton.setVisibility(View.VISIBLE);
                Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());
                Singleton.getInstance().getMainActivity().replaceFragment(new Documents(), null);

            }
        } else if (url.equals("support") && Singleton.getInstance().getHomeState().equals("reception")) {
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            // replaceFragment(new FAQ(), null);
            Documents documents=new Documents();
            Bundle bundle =new Bundle();
            bundle.putBoolean("isFaq",true);
            documents.setArguments(bundle);
            Singleton.getInstance().getMainActivity().replaceFragment(documents, null);

        } else if (url.equals("reception") && Singleton.getInstance().getHomeState().equals("stands")) {
            slideDown(recyclerLayout);
            setMainVideoOnReception();
            floatingButton.setVisibility(View.GONE);
            standImageView.setVisibility(View.GONE);
            bannerImageView.setVisibility(View.GONE);
            standCompanyLogoImageView.setVisibility(View.GONE);
            receptionYoutubePlayer.release();
            receptionYoutubePlayer.setVisibility(View.GONE);
            youTubePlayerView.release();
            youTubePlayerView.setVisibility(View.GONE);
            standYoutubeLayout.setVisibility(View.GONE);
            standFullScreen.setVisibility(View.GONE);
            fullScreenImageViewStandMp4.setVisibility(View.GONE);
            receptionYoutubeLayout.setVisibility(View.GONE);
            receptionFullScreen.setVisibility(View.GONE);
            Singleton.getInstance().setHomeState("reception");
            sliderView.setVisibility(View.GONE);
            sliderCardView.setVisibility(View.GONE);

            prepareMainVideoPlayer(AppConstants.getHall1BackVideo(), false, "hallBack");
        } else if (url.equals("reception") && Singleton.getInstance().getHomeState().equals("hall")) {
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.GONE);
            backToReceptionFromHall();
        } else if (url.equals("chat") && Singleton.getInstance().getHomeState().equals("reception")) {
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            //    replaceFragment(new Chat(), null);
            Singleton.getInstance().getMainActivity().replaceFragment(new Chat(), null);

        } else if (url.equals("courses") && Singleton.getInstance().getHomeState().equals("stands")) {
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            Singleton.getInstance().setIsDashboard(true);

            Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());
            // replaceFragment(new Courses(), null);
            Singleton.getInstance().getMainActivity().replaceFragment(new Courses(), null);

        } else if (url.equals("standPoll") && Singleton.getInstance().getHomeState().equals("stands")) {
            slideDown(recyclerLayout);
            // Singleton.getInstance().setHomeState("stands");
            callApiForStandPoll();

        } else if (url.equals("goodie") && Singleton.getInstance().getHomeState().equals("stands")) {
            slideDown(recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
            Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(Singleton.getInstance().getStandIndex()).getCompanyId());
            //  replaceFragment(new GoodiesList(), null);
            Singleton.getInstance().getMainActivity().replaceFragment(new GoodiesList(), null);


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
//                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
//                    return;
//                }
//                mLastClickTime = SystemClock.elapsedRealtime();
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
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
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
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
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


    public void callApiForHallData(boolean playVideo, Boolean hallState) {
        if (!Singleton.getInstance().getBackToReception()) {
            if (Singleton.getInstance().getMainActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
                for (int i = 0; i < Singleton.getInstance().getMainActivity().getSupportFragmentManager().getBackStackEntryCount() - 1; ++i) {
                    Singleton.getInstance().getMainActivity().getSupportFragmentManager().popBackStack();
                }
            }
        }


        Helper.showProgressBar(progressBar);
        String url = "api/companies/list/" + Singleton.getInstance().getFairData().getFair().getId().toString()
                + "/" + Singleton.getInstance().getHallId() + "/" +
                Singleton.getInstance().getLoginData().getUser().getId();

        Call<StandsData> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getStandsAgainstHall(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<StandsData>() {
            @Override
            public void onResponse(Call<StandsData> call, Response<StandsData> response) {
                Helper.hideProgressBar(progressBar);
                //Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        try {
                            floatingButton.setVisibility(View.VISIBLE);
                        } catch (Exception e) {

                        }
                        Helper.showToast("No Booths Found");
                        //  Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        try {
                            billBoardVideoView.setVisibility(View.GONE);
                            if (Singleton.getInstance().getHomeState().equals("reception")) {
                                receptionFullScreen.setVisibility(View.GONE);
                                receptionYoutubePlayer.release();
                                receptionYoutubeLayout.setVisibility(View.GONE);
                                receptionYoutubePlayer.setVisibility(View.GONE);
                            }
                            floatingButton.setVisibility(View.GONE);
                            try {
                                hideStand();
                            } catch (Exception e) {
                                Log.e("hideStand", e.toString());

                            }
                            try {
                                hideReception();
                            } catch (Exception e) {
                                Log.e("hideReception", e.toString());

                            }
                            //   floatingButton.setVisibility(View.VISIBLE);


                        } catch (Exception e) {
                            Helper.showToast("Something went wrong. Please try again");
                        }
                        Singleton.getInstance().getStandsDataAgainstHall().clear();
                        Singleton.getInstance().setStandsDataAgainstHall(response.body().getData().getCompanyList());
                        if (Singleton.getInstance().getHomeState().equals("hall") || hallState) {
                            if (playVideo) {
                                prepareMainVideoPlayer(AppConstants.getReceptionToHall1(), false, "hall");

                            } else {
                                toolBar(true, Singleton.getInstance().getHallName());
                                Singleton.getInstance().setHomeState("hall");
                                prepareMainVideoPlayer(AppConstants.getHall1BackgroundVideo(), true, "hallBackground");
                                Singleton.getInstance().setViewPager(false);
                                //setHallViewPager(Singleton.getInstance().getStandsDataAgainstHall());
                                setHallViewPager2(Singleton.getInstance().getStandsDataAgainstHall());
                                if (hallState) {
                                    enterBtn.setVisibility(View.GONE);
                                }
                            }
                        }
                        if (!Singleton.getInstance().getHomeState().equals("hall") && playVideo)
                            prepareMainVideoPlayer(AppConstants.getReceptionToHall1(), false, "hall");
                        Singleton.getInstance().setHomeState("hall");

                        if (Singleton.getInstance().getExhibitorsClicked()) {
                            Singleton.getInstance().setBackToReception(false);
                            Singleton.getInstance().setExhibitorsClicked(false);

                        }

                    } else {
                        Helper.showToast("Something went wrong. Please try again");

                    }
                } catch (Exception e) {
                    floatingButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<StandsData> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                floatingButton.setVisibility(View.VISIBLE);
            }

        });

    }

    private void hideReception() {
        try {
            receptionYoutubePlayer.release();
            receptionYoutubePlayer.setVisibility(View.GONE);
            receptionYoutubeLayout.setVisibility(View.GONE);
            receptionFullScreen.setVisibility(View.GONE);
            recyclerLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            videoLayoutVisibility(false);

        } catch (Exception e) {
            Log.e("hideReception", e.toString());
//            Helper.showToast(e.getMessage());

        }

    }

    private void hideStand() {
        try {
            //   toolBar(true, Singleton.getInstance().getHallName());
            youTubePlayerView.release();
            youTubePlayerView.setVisibility(View.GONE);
            standYoutubeLayout.setVisibility(View.GONE);
            standFullScreen.setVisibility(View.GONE);
            fullScreenImageViewStandMp4.setVisibility(View.GONE);
            standImageView.setVisibility(View.GONE);
            bannerImageView.setVisibility(View.GONE);
            standCompanyLogoImageView.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e("hideStand", e.toString());
        }

        // hallRecyclerView.setVisibility(View.VISIBLE);

        videoLayoutVisibility(false);
        Singleton.getInstance().setHomeState("hall");
        // hallRecyclerView.setVisibility(View.VISIBLE);
        //  manageHallRecyclerView();

        //  floatingButton.setVisibility(View.VISIBLE);
        sliderCardView.setVisibility(View.GONE);
        sliderView.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        youTubePlayerView.release();
        youTubePlayerView.setVisibility(View.GONE);
        standYoutubeLayout.setVisibility(View.GONE);
        standFullScreen.setVisibility(View.GONE);
    }

    public void callApiForCompanyDetail(int standIndex) {
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
                //   Helper.hideLayouts(getView());
                if (Singleton.getInstance().getHomeState().equals("stands")) {
                    try {
                        if (response.raw().code() == 401) {
                            Helper.showToast("Session Expired");
                            Helper.clearUserData();
                            Singleton.getInstance().setHomeState("hall");

                        }
                        if (response.raw().code() == 204) {
                            Helper.showToast("No Data Found");
                            Singleton.getInstance().setHomeState("hall");
                        } else if (response.body().getStatus()) {
                            // hallViewPager.setVisibility(View.GONE);
                            hallRecyclerView.setVisibility(View.GONE);
                            Singleton.getInstance().setHomeState("stands");
                            floatingButton.setVisibility(View.VISIBLE);
                            Singleton.getInstance().setStandIndex(standIndex);
                            home.toolBar(true, Singleton.getInstance().getStandsDataAgainstHall().get(standIndex).getCompanyName());
                            slideDown(Home.recyclerLayout);
                            standImageView.setVisibility(View.VISIBLE);
                            standCompanyLogoImageView.setVisibility(View.VISIBLE);
                            bannerImageView.setVisibility(View.VISIBLE);

                            standCompanyLogoImageView.setVisibility(View.VISIBLE);

                            standCompanyLogoImageView.setVisibility(View.VISIBLE);
                            setStandDimensions(response);
                            Glide.with(requireActivity()).load(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + response.body().getData().getCompanyList().get(0).getCompanyStandBanner()).into(bannerImageView);
                            Helper.loadRectangleImageFromUrlWithRounded(standCompanyLogoImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + response.body().getData().getCompanyList().get(0).getCompanyStandImage(), 0);
                            Singleton.getInstance().setCompanyMedia(response.body().getData());
                            setMainVideoOnStand();
                            poll = response.body().getData().getCompanyList().get(0).getEnablePoll();
                            goodies = response.body().getData().getCompanyList().get(0).getEnableGoodies();
                            companyDetailResponse = response;
                            stageButtonsList(poll, goodies, response);
                        } else {
                            Helper.showToast("Something went wrong. Please try later");
                            Singleton.getInstance().setHomeState("hall");

                        }

                    } catch (Exception e) {
                        Singleton.getInstance().setHomeState("hall");
                        Helper.hideProgressBar(progressBar);
                        Helper.showToast("Something went wrong. Please try later");

                    }
                }

            }

            @Override
            public void onFailure(Call<CompanyDetails> call, Throwable t) {
                Singleton.getInstance().setHomeState("hall");

                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something went wrong. Please try later");

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
//            standImageView.setBackground(getResources().getDrawable(R.drawable.gold_stand));
            cardViewLayoutParams.bottomMargin = 470;
            sliderCardView.setLayoutParams(cardViewLayoutParams);
        } else {
            lpp.bottomMargin = -75;
            standCompanyLogoImageView.setLayoutParams(lpp);
//            standImageView.setBackground(getResources().getDrawable(R.drawable.silver_stand));
            cardViewLayoutParams.bottomMargin = 570;
            sliderCardView.setLayoutParams(cardViewLayoutParams);
        }
    }

    private void setMainVideoOnStand() {

        standFullScreen = view.findViewById(R.id.standFullScreen);
        fullScreenImageViewStandMp4 = view.findViewById(R.id.fullScreenImageViewStandMp4);
        standYoutubeLayout = view.findViewById(R.id.standYoutubeLayout);
        ArrayList<SliderItems> images = new ArrayList<>();
        for (int i = 0; i < Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().size(); i++) {
            int j = i;
            if (Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(i).getIsShowFront().equals("1")) {
                if (Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaType().equals("video")) {
                    videoViewLayoutVisibility(false);
                    standYoutubeLayout.setVisibility(View.VISIBLE);
//                    youTubePlayerView.setVisibility(View.VISIBLE);
                    standFullScreen.setVisibility(View.VISIBLE);

                    YouTubePlayerView youTubePlayerView = new YouTubePlayerView(requireActivity());
                    Helper.setYoutubePlayerSettings(youTubePlayerView);
                    this.youTubePlayerView = youTubePlayerView;
                    getLifecycle().addObserver(youTubePlayerView);
                    youTubePlayerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    standYoutubeLayout.addView(youTubePlayerView);
                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

                        @Override
                        public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                            super.onReady(youTubePlayer);
                            player = youTubePlayer;
                            Home.this.youTubePlayerView = youTubePlayerView;
                            if (Home.this.isVisible()) {
                                String videoId = Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(j).getCompanyMediaVideo();
                                youTubePlayer.loadVideo(videoId, 0);
                                youTubePlayer.pause();

                            }
                            if (!Singleton.getInstance().getHomeState().equals("stands")) {
                                youTubePlayerView.setVisibility(View.GONE);
                                standYoutubeLayout.setVisibility(View.GONE);
                                standFullScreen.setVisibility(View.GONE);
                                youTubePlayer.pause();
                            }

                        }

                        @Override
                        public void onVideoLoadedFraction(@NonNull YouTubePlayer youTubePlayer, float loadedFraction) {
                            super.onVideoLoadedFraction(youTubePlayer, loadedFraction);
                        }
                    });


                    youTubePlayerView.enableBackgroundPlayback(false);


//                    Thread thread = new Thread(runnable);
//                    thread.start();

                    standFullScreen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (player != null) {
                                player.pause();
                                player.pause();
//                                player.pause();
//                                player.pause();
//                                player.pause();
//                                new Handler().postDelayed(() -> player.pause(),1000);
                            }
                            Fragment fragment = new FullScreenMedia();
                            Bundle args = new Bundle();
                            args.putString("type", "youtube");
                            args.putString("video", Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(j).getCompanyMediaVideo());
                            fragment.setArguments(args);
                            getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("video")
                                    .commit();

//                            startActivity(new Intent(requireContext(), ExoPlayerActivity.class).putExtra("video", Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(j).getCompanyMediaVideo()).putExtra("seekTo", 0));

                        }
                    });


                    break;
                } else if (Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaType().equals("embed")) {
                    youTubePlayerView.release();
                    youTubePlayerView.setVisibility(View.GONE);
                    libraryPlayer(Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(j).getCompanyMediaVideo(), true);
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

//    private void callApiForFairMedia() {
//        Helper.showProgressBar(progressBar);
//        String url = "fair/media/list/" + Singleton.getInstance().getFairData().getFair().getId().toString();
//        Call<FairMedia> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getFairMedia("Bearer " + Singleton.getInstance().getLoginDate().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, url);
//        call.enqueue(new Callback<FairMedia>() {
//            @Override
//            public void onResponse(Call<FairMedia> call, Response<FairMedia> response) {
//                Helper.hideProgressBar(progressBar);
//                //Helper.hideLayouts(getView());
//                try {
//                    if (response.raw().code() == 204) {
//                    } else if (response.body().getStatus()) {
//                        Singleton.getInstance().setFairMedia(response.body().getData());
//
//                        setMainVideoOnReception();
//                        // prepareListData(response.body().getData().getFaqList());
//                    } else {
//                    }
//                } catch (Exception e) {
//                    Helper.showToast(e.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FairMedia> call, Throwable t) {
//                Helper.hideProgressBar(progressBar);
//            }
//
//        });
//
//    }


    private void setMainVideoOnReception() {
        if (Singleton.getInstance().getFairData().getFair().getReceptionVidLink() != null && !Singleton.getInstance().getFairData().getFair().getReceptionVidLink().equals("")) {
            videoViewLayoutVisibility(false);
            receptionYoutubePlayer = null;
            receptionYoutubePlayer = view.findViewById(R.id.receptionYoutubePlayer);
            receptionYoutubeLayout = view.findViewById(R.id.receptionYoutubeLayout);
            receptionFullScreen = view.findViewById(R.id.receptionFullScreen);
            receptionYoutubeLayout.setVisibility(View.VISIBLE);
//            receptionYoutubePlayer.setVisibility(View.VISIBLE);
            receptionFullScreen.setVisibility(View.VISIBLE);
            YouTubePlayerView youTubePlayerView = new YouTubePlayerView(requireActivity());
            Helper.receptionYoutubePlayerSettings(youTubePlayerView);
            youTubePlayerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            receptionYoutubeLayout.addView(youTubePlayerView);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                                           @Override
                                                           public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                                               super.onReady(youTubePlayer);

                                                               youTubePlayer.loadVideo(Singleton.getInstance().getFairData().getFair().getReceptionVidLink(), 0);
                                                               youTubePlayer.mute();
                                                           }

                                                           @Override
                                                           public void onVideoLoadedFraction(@NonNull YouTubePlayer youTubePlayer, float loadedFraction) {
                                                               super.onVideoLoadedFraction(youTubePlayer, loadedFraction);
                                                               youTubePlayerView.enterFullScreen();
                                                           }
                                                       }
            );
            receptionFullScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new FullScreenMedia();
                    //replaceFragment(new VideoFragment(), "video");

                    Bundle args = new Bundle();
                    args.putString("type", "youtube");
                    args.putString("video", Singleton.getInstance().getFairData().getFair().getReceptionVidLink());
                    fragment.setArguments(args);
                    getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
                            .setReorderingAllowed(true)
                            .addToBackStack("video")
                            .commit();
                }
            });


            receptionYoutubeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new FullScreenMedia();
                    //replaceFragment(new VideoFragment(), "video");

                    Bundle args = new Bundle();
                    args.putString("type", "youtube");
                    args.putString("video", Singleton.getInstance().getFairData().getFair().getReceptionVidLink());
                    fragment.setArguments(args);
                    getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
                            .setReorderingAllowed(true)
                            .addToBackStack("video")
                            .commit();
                }
            });


            receptionYoutubePlayer.enableBackgroundPlayback(false);
        }
    }


    private void setImageSlider(ArrayList<SliderItems> images) {
        sliderCardView.setVisibility(View.VISIBLE);
        sliderView.setVisibility(View.VISIBLE);
        // sliderView.setSliderAdapter(new SliderAdapter(Singleton.getInstance().getContext(), images, home, progressBar));
        sliderView.setSliderAdapter(new SliderAdapter(Singleton.getInstance().getContext(), images, home, progressBar));
        sliderView.setScrollTimeInSec(4);//set scroll delay in seconds :
        sliderView.startAutoCycle();
    }


    public void libraryPlayer(String path, Boolean looping) {
        videoLayoutVisibility(true);
        videoViewLayoutVisibility(true);
        fullScreenImageViewStandMp4.setVisibility(View.VISIBLE);
//        new Handler().postDelayed(this::hideControls, 3000);
        fullScreenImageViewStandMp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), ExoPlayerActivity.class).putExtra("video", path).putExtra("seekTo", videoPlayer.getCurrentPosition() + 5000));
                videoPlayer.pause();
//                Fragment fragment = new FullScreenMedia();
//                //replaceFragment(new VideoFragment(), "video");
//                Bundle args = new Bundle();
//                args.putString("type", "mp4");
//                args.putInt("seekTo", videoPlayer.getCurrentPosition()+5000);
//                args.putString("video", path);
//                fragment.setArguments(args);
//                getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
//                        .setReorderingAllowed(true)
//                        .addToBackStack("video")
//                        .commit();
            }
        });
        String proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(path);
        videoView.setVideoPath(proxyUrl);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void
            onPrepared(MediaPlayer mp) {
                mp.setLooping(looping);
                videoViewBackgroundImageView.setVisibility(View.GONE);
                videoView.setSaveEnabled(true);
                videoView.start();

                setVideoProgress();
            }
        });

        setPause();
        setFullScreen();
        hideLayout();
        // orientationChange();

    }


    private void videoViewLayoutVisibility(boolean b) {
        if (b) {
            videoViewLayoutStand.setVisibility(View.VISIBLE);
            pause.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
            //fullScreen.setVisibility(View.VISIBLE);
            videoPlayer.setVisibility(View.VISIBLE);
            videoViewBackgroundImageView.setVisibility(View.VISIBLE);

        } else {
            videoPlayer.pause();
            videoViewLayoutStand.setVisibility(View.GONE);
            pause.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
            //fullScreen.setVisibility(View.GONE);
            videoPlayer = view.findViewById(R.id.videoPlayer);
            videoPlayer.setVisibility(View.GONE);
            videoViewBackgroundImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
//        checkNewAppVersionState();
        super.onResume();

        try {
            slideDown(recyclerLayout);
        } catch (Exception e) {
            Helper.showToast("waqas");

        }
        int count = Singleton.getInstance().getMainActivity().getSupportFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            Helper.setActiveItemBottomNav(0);
        }
        //  try {
        //  if (Singleton.getInstance().getSuperBackPressed()) {
        //     Singleton.getInstance().setSuperBackPressed(false);
        //  Singleton.getInstance().getBottomNavigationView().setCurrentActiveItem(0);
        //   }
//        } catch (Exception e) {
//            Helper.showToast("waqas");
//        }

        try {
            if (Singleton.getInstance().getHomeState().equals("reception")) {
                if (!isVideoPlaying) setMainVideoOnReception();
            }
            if (Singleton.getInstance().getHomeState().equalsIgnoreCase("home")) {
                MainActivity.getInstance().replaceFragment(new Home(), "home");
            }
        } catch (Exception e) {
            Helper.showToast("waqas");

        }
//        if (isVideoPlaying){
//            Helper.showToast("playing");
//            hideReception();
//        }

//        if (Singleton.getInstance().getHomeState().equals("reception"))
//            prepareMainVideoPlayer(AppConstants.RECEPTION_BACK_VIDEO, true, "receptionBack");


    }


    // display video progress
    private void setVideoProgress() {
        //get the video duration
        current_pos = videoView.getCurrentPosition();
        total_duration = videoView.getDuration();

        //display video duration
        //total.setText(timeConversion((long) total_duration));
        //current.setText(timeConversion((long) current_pos));
        seekBar.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = videoView.getCurrentPosition();
                    //current.setText(timeConversion((long) current_pos));
                    seekBar.setProgress((int) current_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        //seekbar change listner
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                videoView.seekTo((int) current_pos);
            }
        });
    }

    //pause video
    protected void setPause() {
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    pause.setImageResource(R.drawable.play);
                } else {
                    videoView.start();
                    pause.setImageResource(R.drawable.pause);
                }
            }
        });
    }

    @Override
    public void onStop() {
        try {
            youTubePlayerView.release();

        } catch (Exception e) {

        }
        try {
            receptionYoutubePlayer.release();

        } catch (Exception e) {

        }
        try {
            videoView.pause();
        } catch (Exception e) {

        }
        try {

            billBoardVideoView.pause();
        } catch (Exception e) {

        }
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setFullScreen() {
//        fullScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isFullScreen) {
//                    Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                    lp.height = displayMetrics.heightPixels;
//                    lp.width = displayMetrics.widthPixels;
//                    isFullScreen = true;
//                    //fullScreen.setImageResource(R.drawable.fullscreen_exit);
//
//                    if (Singleton.getInstance().getHomeState().equals("home"))
//                        enterBtn.setVisibility(View.GONE);
//                    else if (Singleton.getInstance().getHomeState().equals("reception"))
//                        floatingButton.setVisibility(View.GONE);
//                    menuImageView.setVisibility(View.GONE);
//                    Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
//                    lp.setMargins(Helper.getSizeInSp(0),
//                            Helper.getSizeInSp(0), Helper.getSizeInSp(0), Helper.getSizeInSp(0));
//
//                } else {
//                    //fullScreen.setImageResource(R.drawable.full_screen);
//                    isFullScreen = false;
//                    lp.height = Helper.getSizeInSp(175);
//                    lp.width = displayMetrics.widthPixels - 200;
//                    Singleton.getInstance().getBottomNavigationView().setVisibility(View.VISIBLE);
//                    lp.setMargins(Helper.getSizeInSp(55), Helper.getSizeInSp(55), Helper.getSizeInSp(55), Helper.getSizeInSp(55));
//                    if (Singleton.getInstance().getHomeState().equals("home"))
//                        enterBtn.setVisibility(View.VISIBLE);
//                    else if (Singleton.getInstance().getHomeState().equals("reception"))
//                        floatingButton.setVisibility(View.VISIBLE);
//                    menuImageView.setVisibility(View.VISIBLE);
//                }
//                videoViewLayout.setLayoutParams(lp);
//
//
//            }
//        });
    }


    protected void hideLayout() {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                pause.setVisibility(View.GONE);
                seekBar.setVisibility(View.GONE);
                //    screenRotation.setVisibility(View.GONE);
                //fullScreen.setVisibility(View.GONE);
                if (Singleton.getInstance().getActivity().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    pause.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                    //   screenRotation.setVisibility(View.VISIBLE);
                    //fullScreen.setVisibility(View.VISIBLE);
                }

                // showProgress.setVisibility(View.GONE);
                // isVisible = false;
            }
        };
        handler.postDelayed(runnable, 5000);

        videoViewLayoutStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showControls();
//                mHandler.removeCallbacks(runnable);
//                seekBar.setVisibility(View.VISIBLE);
//                pause.setVisibility(View.VISIBLE);
//                //  screenRotation.setVisibility(View.VISIBLE);
//                if (Singleton.getInstance().getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
//                    //fullScreen.setVisibility(View.VISIBLE);
//                    mHandler.postDelayed(runnable, 5000);

            }
        });

    }


    public void videoLayoutVisibility(Boolean flag) {
        if (flag) {
            videoViewLayoutStand.setVisibility(View.VISIBLE);

        } else {
            if (videoView.isPlaying())
                videoView.stopPlayback();
            videoViewLayoutStand.setVisibility(View.GONE);
        }
    }


    public void toolBar(Boolean flag, String title) {

        if (flag) {
            toolBar.setVisibility(View.VISIBLE);
            titleTxt.setVisibility(View.VISIBLE);
            titleTxt.setText(title);
            toolBar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        } else {
            toolBar.setVisibility(View.GONE);
            titleTxt.setVisibility(View.GONE);
        }

    }


    public void showPreLiveDialog(boolean login) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_welcome, null);

        dialogView.requestLayout();
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView descriptionTxt = dialogView.findViewById(R.id.descriptionTxt);
        TextView title = dialogView.findViewById(R.id.title);
        title.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getWelcome_to() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFair_name());
//        Helper.setTextColor(title);
//        Helper.setTextColor(descriptionTxt);
        getPreLiveData(descriptionTxt);
        if (login) {
            if (Singleton.getInstance().getFairData().getFair().getFairSetting().getWelcomeContentAfterRegistration() != null) {
                Helper.loadHtml(descriptionTxt, Singleton.getInstance().getFairData().getFair().getFairSetting().getPreFairEnterContent());
            }
        } else {
            if (Singleton.getInstance().getFairData().getFair().getFairSetting().getWelcomeContentAfterRegistration() != null) {
                Helper.loadHtml(descriptionTxt, Singleton.getInstance().getFairData().getFair().getFairSetting().getPreFairEnterContent());
            }
        }
        descriptionTxt.setMovementMethod(new ScrollingMovementMethod());
//        descriptionTxt.setText(Singleton.getInstance().getFairData().getFair().getFairSetting().getWelcomeContentAfterRegistration());


        Button okBtn = dialogView.findViewById(R.id.okBtn);
//        okBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getO());
        Helper.setButtonColorWithDrawable(okBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(), Singleton.getInstance().getFairData().getFair().getOptions().getReceptionButtonsInnerTextColor());

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                alertDialog.cancel();
            }
        });
        Singleton.getInstance().getDrawerLayout().close();

        //   okBtn.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor()));
        //  enterBtn.setBackgroundColor(Color.RED);

//        GradientDrawable drawable = (GradientDrawable) okBtn.getBackground();
//        drawable.setColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterBackground()));


        alertDialog.show();
    }


    public void HallViewPagerClick(int standIndex) {
        if (recyclerLayout.getVisibility() != View.VISIBLE) {

            Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(standIndex).getCompanyId());

            callApiForCompanyDetail(standIndex);
            Singleton.getInstance().setHomeState("stands");
            Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(standIndex).getCompanyId());

            callApiForCompanyDetail(standIndex);
            Singleton.getInstance().setHomeState("stands");
            companyDetailResponse = null;


        } else {
            slideDown(Home.recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
        }

    }

    public void HallViewPagerClick2(int standIndex) {
        if (recyclerLayout.getVisibility() != View.VISIBLE) {
            if (Singleton.getInstance().getStandsDataAgainstHall() != null && !Singleton.getInstance().getStandsDataAgainstHall().isEmpty()) {
                // if (Singleton.getInstance().getHomeState().equals("hall")) {
                Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getStandsDataAgainstHall().get(standIndex).getCompanyId());

                callApiForCompanyDetail(standIndex);
                Singleton.getInstance().setHomeState("stands");
                // }
                companyDetailResponse = null;
            }

        } else {
            slideDown(Home.recyclerLayout);
            floatingButton.setVisibility(View.VISIBLE);
        }

    }

    private void initializeHallViewPager() {
//        hallViewPager = view.findViewById(R.id.hallViewPager);
//        hallImageAdapter = new SliderAdapter(Singleton.getInstance().getContext(), hallStandImages, home, progressBar);
//        hallViewPager.setSliderAdapter(hallImageAdapter);
//        hallViewPager.setAutoCycle(false);
//        hallViewPager.setIndicatorAnimation(IndicatorAnimationType.WORM);
//        hallViewPager.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);


    }


    private void setHallViewPager(ArrayList<Company> standData) {
//        hallViewPager = view.findViewById(R.id.hallViewPager);
//        hallStandImages.clear();
//        for (int i = 0; i < standData.size(); i++) {
//            SliderItems sliderItems = null;
//            if (standData.get(i).getCompanyStandType().equals("Gold"))
//                sliderItems = new SliderItems(R.drawable.gold_stand, "Gold");
//            else if (standData.get(i).getCompanyStandType().equals("Bronze"))
//                sliderItems = new SliderItems(R.drawable.bronze_stand, "Bronze");
//            else
//                sliderItems = new SliderItems(R.drawable.silver_stand, "Silver");
//
//            //sliderItems = new SliderItems(Singleton.getInstance().getStandsDataAgainstHall().get(i).getStandBackgroundUrl(), "");
//
//            hallStandImages.add(sliderItems);
//        }
//        hallViewPager.setVisibility(View.VISIBLE);
//
//        hallImageAdapter.notifyDataSetChanged();


    }

    private void showControls() {
        if (seekBar.getVisibility() == View.VISIBLE) {
            hideControls();
        } else {
            seekBar.setVisibility(View.VISIBLE);
            pause.setVisibility(View.VISIBLE);
            fullScreenImageViewStandMp4.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideControls();
                }
            }, 3000);
        }

    }

    private void hideControls() {
        seekBar.setVisibility(View.GONE);
        pause.setVisibility(View.GONE);
        fullScreenImageViewStandMp4.setVisibility(View.GONE);
    }

    private void setHallViewPager2(ArrayList<Company> standData) {
        Singleton.getInstance().getHallModelArrayList().clear();
        for (int i = 0; i < standData.size(); i++) {
            HallModel hallModel = null;
            if (standData.get(i).getCompanyStandType().equals("Gold"))
                hallModel = new HallModel("Gold", standData.get(i).getCompanyLogo());
            else if (standData.get(i).getCompanyStandType().equals("Bronze"))
                hallModel = new HallModel("Bronze", standData.get(i).getCompanyLogo());
            else
                hallModel = new HallModel("Silver", standData.get(i).getCompanyLogo());

            //sliderItems = new SliderItems(Singleton.getInstance().getStandsDataAgainstHall().get(i).getStandBackgroundUrl(), "");

            Singleton.getInstance().getHallModelArrayList().add(hallModel);
        }

        // Collections.reverse(Singleton.getInstance().getHallModelArrayList());

        hallRecyclerView.setVisibility(View.VISIBLE);
        manageHallRecyclerView();
        hallRecyclerAdapter.notifyDataSetChanged();
        //  hallRecyclerView.smoothScrollToPosition(Singleton.getInstance().getHallModelArrayList().size() / 2);

    }

    public void onDrawerClosed() {
        if (Singleton.getInstance().getHomeState().equals("home")) {
            Helper.disableTouch();
            billBoardVideoView.setVisibility(View.GONE);
            Helper.disableTouch();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    billBoardVideoView.setVisibility(View.VISIBLE);
                    Helper.enableTouch();
                }
            }, 200);
        }
    }

    private void getPreLiveData(TextView textView) {
        Helper.showProgressBar(progressBar);
        String url = "api/auth/fair/prefairentercontent/" + Singleton.getMyFairId();
        Call<ContentModel> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getNeedHelp(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<ContentModel>() {
            @Override
            public void onResponse(Call<ContentModel> call, Response<ContentModel> response) {
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
                        if (response.body().getData() != null && response.body().getData().getPre_fair_enter_content() != null) {
                            Helper.loadHtml(textView, response.body().getData().getPre_fair_enter_content());
                        }

                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<ContentModel> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {

            case REQ_CODE_VERSION_UPDATE:
                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                    Log.d("Update flow failed! Result code: " + resultCode, "");
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                    unregisterInstallStateUpdListener();
                }

                break;
        }
    }

    private void checkForAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(MainApp.getAppContext());

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED)
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister();
            }
        };

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                    // Before starting an update, register a listener for updates.
//                    appUpdateManager.registerListener(installStateUpdatedListener);
//                    // Start an update.
                    startAppUpdateFlexible(appUpdateInfo);

//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+"com.vrd.gsaf")));
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Start an update.
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+"com.vrd.gsaf")));

                    startAppUpdateImmediate(appUpdateInfo);
                }
            }
        });
    }

    private void showUpdateDialog() {
        Dialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.vrd.gsaf")));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                background.start();
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        dialog = builder.show();
    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    requireActivity(),
                    // Include a request code to later monitor this update request.
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    requireActivity(),
                    // Include a request code to later monitor this update request.
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    private void popupSnackbarForCompleteUpdateAndUnregister() {
        Snackbar snackbar =
                Snackbar.make(MainActivity.getInstance().drawerLayout, getString(R.string.update_downloaded), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.restart, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.primaryTextColor));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            //FLEXIBLE:
                            // If the update is downloaded but not installed,
                            // notify the user to complete the update.
                            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                popupSnackbarForCompleteUpdateAndUnregister();
                            }

                            //IMMEDIATE:
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
//                                 If an in-app update is already running, resume the update.
                                startAppUpdateImmediate(appUpdateInfo);
                            }
                        });

    }

    /**
     * Needed only for FLEXIBLE update
     */
    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    @Override
    public void onDestroy() {
        unregisterInstallStateUpdListener();
        if (mainPlayer!=null&& mainPlayer.isPlaying()){
            mainPlayer.pause();
            mainPlayer.stopPlayback();
        }
        super.onDestroy();
    }
}


