package com.vrd.gsaf.home.liveWebinar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.vrd.gsaf.R;
import com.vrd.gsaf.activity.ExoPlayerActivity;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.homeTab.ButtonsAdapter;
import com.vrd.gsaf.home.homeTab.Home;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import vimeoextractor.OnVimeoExtractionListener;
import vimeoextractor.VimeoExtractor;
import vimeoextractor.VimeoVideo;


public class LiveWebinar extends Fragment implements View.OnClickListener, View.OnTouchListener, ButtonsAdapter.OnShareClickedListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    public static RecyclerView recyclerView;
    private final MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            // Your code goes here

            return true;
        }
    };
    public ButtonsAdapter adapter;
    protected HttpProxyCacheServer proxy;
    protected MainApp app = new MainApp();
    protected double current_pos, total_duration;
    protected float dX;
    protected float dY;
    protected int lastAction;
    ConstraintLayout videoViewLayout, mainLayout, toolBar;
    com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView youTubePlayerView;
    RelativeLayout youtubeLayout;
    YouTubePlayerTracker tracker;
    SeekBar seekBar;
    VideoView videoPlayer, mainPlayer;
    com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayerDefault;
    RelativeLayout toolbar;
    long lastDown;
    long lastDuration;
    RelativeLayout floatingButton;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView, thumbNailImage, pause, fullScreen, orientationImage, videoViewBackgroundImageView, fullScreenExitImageViewMedia, fullScreenExitImageViewMediaPotriat;
    private TextView titleTxt;
    private long mLastClickTime;
    private Button chatBtn;
    private ViewGroup viewGroup;
    private Boolean full = false;
    private ImageView floatingToggle, crossButtonsLayoutImageView, fullScreenImageView;
    private ConstraintLayout recyclerLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
        if (Singleton.getInstance().getFairData().getFair().getLayout().equals(AppConstants.LAYOUT48)) {
            view = inflater.inflate(R.layout.fragment_live_webinar_48, container, false);
        } else if (Singleton.getInstance().getFairData().getFair().getLayout().equals(AppConstants.LAYOUT53)) {
            view = inflater.inflate(R.layout.fragment_live_webinar_53, container, false);
        } else if (Singleton.getInstance().getFairData().getFair().getLayout().equals(AppConstants.LAYOUT11)) {
            view = inflater.inflate(R.layout.fragment_live_webinar_11, container, false);
        } else if (Singleton.getInstance().getFairData().getFair().getLayout().equals(AppConstants.LAYOUT66)) {
            view = inflater.inflate(R.layout.fragment_live_webinar_66, container, false);
        } else if (Singleton.getInstance().getFairData().getFair().getLayout().equals(AppConstants.LAYOUT60)) {
            view = inflater.inflate(R.layout.fragment_live_webinar_60, container, false);
        } else if (Singleton.getInstance().getFairData().getFair().getLayout().equals(AppConstants.LAYOUT15)) {
            view = inflater.inflate(R.layout.fragment_live_webinar_15, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_live_webinar_66, container, false);
        }
        //  this.setRetainInstance(true);
        Bundle bundle = this.getArguments();
        viewGroup = container;
        Helper.hideKeyboard();
        initializeViews();
        Singleton.getInstance().setLiveWebinarFragment(this);
        // Singleton.getInstance().getBottomNavigationView().setCurrentActiveItem(0);
        //Singleton.getInstance().getBottomNavigationView().setCurrentActiveItem(0);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        //playVideo();
        super.onResume();
        try {
            Helper.setActiveItemBottomNav(0);

        } catch (Exception e) {

        }
        // playVideo();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        try {
            youTubePlayerDefault.pause();
        } catch (Exception e) {

        }
        try {
            videoPlayer.pause();
        } catch (Exception e) {

        }

        Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        try {
            youTubePlayerView.release();
        } catch (Exception e) {

        }
        super.onDestroy();
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {

        progressBar = view.findViewById(R.id.progressBar);
        crossButtonsLayoutImageView = view.findViewById(R.id.crossButtonsLayoutImageView);

        floatingToggle = view.findViewById(R.id.draggable_view);
        floatingButton = view.findViewById(R.id.floating_button_layout);
        floatingButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor())));
        floatingToggle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavInnerTextColor())));
        thumbNailImage = view.findViewById(R.id.thumbNailImage);
        mainPlayer = view.findViewById(R.id.idExoPlayerVIew);
        backImageView = view.findViewById(R.id.backImageView);
        recyclerLayout = view.findViewById(R.id.recyclerLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        fullScreenImageView = view.findViewById(R.id.fullScreenImageViewStandMp4);

        backImageView.setVisibility(View.GONE);
        recyclerLayout.setVisibility(View.GONE);

        chatBtn = view.findViewById(R.id.chatBtn);
        chatBtn.setVisibility(View.GONE);

        Helper.setButtonColorWithDrawable(chatBtn,
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());

        titleTxt = view.findViewById(R.id.titleTxt);

        chatBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getChat());
        titleTxt.setVisibility(View.GONE);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolBar = view.findViewById(R.id.tollBar);
//        youTubePlayerView = view.findViewById(R.id.youtubePlayer);
        youtubeLayout = view.findViewById(R.id.youtubePlayer);
        videoViewLayout = view.findViewById(R.id.videoViewLayoutStand);
        seekBar = view.findViewById(R.id.seekBarMedia);
        pause = view.findViewById(R.id.pauseMedia);
        orientationImage = view.findViewById(R.id.orientationImageViewMedia);
        fullScreen = view.findViewById(R.id.fullScreenImageViewMedia);
        fullScreenExitImageViewMedia = view.findViewById(R.id.fullScreenExitImageViewMedia);
        videoPlayer = view.findViewById(R.id.videoPlayerMediaMedia);
        proxy = MainApp.getProxy(Singleton.getInstance().getActivity());
        videoViewBackgroundImageView = view.findViewById(R.id.videoViewBackgroundImageView);
        backImageView.setOnClickListener(this);
        chatBtn.setOnClickListener(this);
        crossButtonsLayoutImageView.setOnClickListener(this);
        floatingButton.setOnTouchListener(this);
        //  floatingButton.setVisibility(View.GONE);

        selectVideo();

        thumbNailImage.setVisibility(View.VISIBLE);
        mainPlayer.setVisibility(View.VISIBLE);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        manageButtonRecylcerView();
        videoViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showControls();
            }
        });
        fullScreenImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (youTubePlayerView.getVisibility() == View.VISIBLE) {
                    startActivity(new Intent(requireContext(), ExoPlayerActivity.class)
                            .putExtra("video", Singleton.getInstance().getWebinarDetail().getWebinarLink())
                            .putExtra("type", "youtube")
                            .putExtra("seekTo", 0));

//                    Fragment fragment = new FullScreenMedia();
//                    //replaceFragment(new VideoFragment(), "video");
//                    Bundle args = new Bundle();
//                    args.putString("type", "youtube");
//                    args.putInt("seekTo", (int) tracker.getCurrentSecond());
//                    args.putString("video", Singleton.getInstance().getWebinarDetail().getWebinarLink());
//                    fragment.setArguments(args);
//                    getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
//                            .setReorderingAllowed(true)
//                            .addToBackStack("video")
//                            .commit();
                } else if (videoViewLayout.getVisibility() == View.VISIBLE) {
                    startActivity(new Intent(requireContext(), ExoPlayerActivity.class)
                            .putExtra("video", Singleton.getInstance().getWebinarDetail().getWebinarLink())
                            .putExtra("type", "mp4")
                            .putExtra("seekTo", videoPlayer.getCurrentPosition() + 5000));
                    videoPlayer.pause();
//                    Fragment fragment = new FullScreenMedia();
//                    //replaceFragment(new VideoFragment(), "video");
//
//                    Bundle args = new Bundle();
//                    args.putString("type", "mp4");
//                    args.putString("video", Singleton.getInstance().getWebinarDetail().getWebinarLink());
//                    fragment.setArguments(args);
//                    getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
//                            .setReorderingAllowed(true)
//                            .addToBackStack("video")
//                            .commit();

                }
            }
        });
        // retryLayout();

        //  Singleton.getInstance().getBottomNavigationView().setCurrentActiveItem(0);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_chats_webinar_room_front().equals(1)) {
            chatBtn.setVisibility(View.GONE);
        }
        tracker = new YouTubePlayerTracker();
        if (youTubePlayerView != null) youTubePlayerView.addYouTubePlayerListener(tracker);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatFront().equals(1)) {
            chatBtn.setVisibility(View.GONE);
        }
    }

    private void selectVideo() {
        if (Singleton.getInstance().getWebinarVideo()) {
            if (Singleton.getInstance().getHomeState().equals("reception")) {
                prepareMainVideoPlayer(AppConstants.getReceptionToWebinar(), false, "reception");
            } else if (Singleton.getInstance().getHomeState().equals("stands")) {
                prepareMainVideoPlayer(AppConstants.getHall1ToWebinar(), false, "reception");
            }
            Singleton.getInstance().setLiveWebinar(true);
        } else {
            toolBar(true, Singleton.getInstance().getWebinarDetail().getWebinarTitle());
            prepareMainVideoPlayer(AppConstants.getWebinar_BACKGROUND(), true, "background");


            playVideo();

            backImageView.setVisibility(View.VISIBLE);
            titleTxt.setVisibility(View.VISIBLE);

        }

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

    public void prepareMainVideoPlayer(String path, boolean b, String video) {
//        MediaController mediaController = new MediaController(Singleton.getInstance().getContext());
        Helper.showProgressBar(progressBar);

        // Helper.showToast(String.valueOf(Singleton.getInstance().getProxy().isCached(path)));
        String proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(path);
//        mediaController.setAnchorView(mainPlayer);
//        mainPlayer.setMediaController(mediaController);

        mainPlayer.setVideoPath(proxyUrl);

        mainPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Helper.hideProgressBar(progressBar);
                thumbNailImage.setVisibility(View.GONE);

                mp.setLooping(b);
                mainPlayer.setSaveEnabled(true);
                mainPlayer.start();

                if (video.equals("reception")) {
                    Helper.disableTouch();

                }
                if (video.equals("background")) {
                    try {
                        if (Singleton.getInstance().getWebinarDetail().getWebinarChat().equals("1")) {
                            chatBtn.setVisibility(View.VISIBLE);
                        }
                        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatFront().equals(1)) {
                            chatBtn.setVisibility(View.GONE);
                        }
                        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatButton().equals(1)) {
                            chatBtn.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {

                    }
                }
                if (video.equals("back")) {

                    try {
                        youTubePlayerView.release();
                    } catch (Exception e) {

                    }
                    try {
                        videoPlayer.pause();
                    } catch (Exception e) {

                    }
                    Helper.disableTouch();

                    titleTxt.setVisibility(View.GONE);
                    fullScreenImageView.setVisibility(View.GONE);
                    backImageView.setVisibility(View.GONE);
                    slideDown(recyclerLayout);
                    //floatingButton.setVisibility(View.VISIBLE);

                    youTubePlayerView.setVisibility(View.GONE);
                    videoViewLayout.setVisibility(View.GONE);
                    chatBtn.setVisibility(View.GONE);

                }

            }
        });


        mainPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Singleton.getInstance().setIsHome(false);
                if (video.equals("reception")) {

                    Helper.enableTouch();

                    playVideo();
                    chatBtn.setVisibility(View.VISIBLE);
                    if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatFront().equals(1)) {
                        chatBtn.setVisibility(View.GONE);
                    }
                    if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatButton().equals(1)) {
                        chatBtn.setVisibility(View.GONE);
                    }
                    backImageView.setVisibility(View.VISIBLE);
                    titleTxt.setVisibility(View.VISIBLE);
                    prepareMainVideoPlayer(AppConstants.getWebinar_BACKGROUND(), true, "background");
                    toolBar(true, Singleton.getInstance().getWebinarDetail().getWebinarTitle());


                } else if (video.equals("back")) {
                    toolBar(false, null);

                    Helper.enableTouch();

                    if (Singleton.getInstance().getWebinarToHall() || Singleton.getInstance().getWebinarToReception()) {
                        Singleton.getInstance().setWebinarToReception(false);
                        Singleton.getInstance().setWebinarToHall(false);

                        loadHome();

                    } else {
                        //Singleton.getInstance().getBottomNavigationView().setVisibility(View.VISIBLE);
                        if (Singleton.getInstance().getWebinarVideo())
                            getActivity().getSupportFragmentManager().popBackStack();
                        else {
                            FragmentManager fm = getParentFragmentManager();

                            fm.beginTransaction().add(R.id.frameLayout, new Home())
                                    .setReorderingAllowed(true)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }

                }
            }
        });

    }

    private void playVideo() {
//        youTubePlayerView = view.findViewById(R.id.youtubePlayer);
        youtubeLayout = view.findViewById(R.id.youtubePlayer);
        videoViewLayout = view.findViewById(R.id.videoViewLayoutStand);
        seekBar = view.findViewById(R.id.seekBarMedia);
        pause = view.findViewById(R.id.pauseMedia);
        orientationImage = view.findViewById(R.id.orientationImageViewMedia);
        orientationImage = view.findViewById(R.id.orientationImageViewMedia);
        fullScreen = view.findViewById(R.id.fullScreenImageViewMedia);
        fullScreenExitImageViewMedia = view.findViewById(R.id.fullScreenExitImageViewMedia);
        videoPlayer = view.findViewById(R.id.videoPlayerMediaMedia);

//        if (Singleton.getInstance().getIsLiveWebinar()&&Singleton.getInstance().getWebinarDetail().getWebinarType().equals("live_100")) {
//            WebView webview = view.findViewById(R.id.webview);
//            startActivity(new Intent(requireActivity(), WebViewActivity.class).putExtra("url", AppSession.get("url")));
//        } else if (Singleton.getInstance().getIsLiveWebinar()&&Singleton.getInstance().getFairData().getFair().getWebinarPlugin().equalsIgnoreCase("live")) {
//            Helper.viewDocument(AppSession.get("url"));
//        }
//        else
        if (!Singleton.getInstance().getStageWebinars()) {
            if (Singleton.getInstance().getWebinarDetail().getWebinarType().equals("recorded") || Singleton.getInstance().getWebinarDetail().getWebinarType().equals("121recorded") || Singleton.getInstance().getWebinarDetail().getWebinarType().equals("youtubelive")) {
                youtubeLayout.setVisibility(View.VISIBLE);
                fullScreenImageView.setVisibility(View.VISIBLE);
                YouTubePlayerView youTubePlayerView = new YouTubePlayerView(requireActivity());
                Helper.setYoutubePlayerSettings(youTubePlayerView);
                this.youTubePlayerView = youTubePlayerView;
                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                        super.onReady(youTubePlayer);
                        youTubePlayerDefault = youTubePlayer;
                        youTubePlayer.loadVideo(Singleton.getInstance().getWebinarDetail().getWebinarLink(), 0);

                        //youTubePlayer.pause();

                    }
                });
                youTubePlayerView.enableBackgroundPlayback(false);
                youTubePlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                youtubeLayout.addView(youTubePlayerView);
            } else if (Singleton.getInstance().getWebinarDetail().getWebinarType().equals("embed") || Singleton.getInstance().getWebinarDetail().getWebinarType().equals("121embed")) {
                startVideoPlayer(Singleton.getInstance().getWebinarDetail().getWebinarLink());
                //  initializePlayer("https://www.appsloveworld.com/wp-content/uploads/2018/10/640.mp4");

            } else if (Singleton.getInstance().getWebinarDetail().getWebinarType().equals("vimeo")) {
                playVimeoVideo(Singleton.getInstance().getWebinarDetail().getWebinarLink());

            }
        } else {
            if (Singleton.getInstance().getWebinarDetail().getWebinarType().equals("recorded") || Singleton.getInstance().getWebinarDetail().getWebinarType().equals("121recorded") || Singleton.getInstance().getWebinarDetail().getWebinarType().equals("youtubelive")) {
                youTubePlayerView.setVisibility(View.VISIBLE);
                fullScreenImageView.setVisibility(View.VISIBLE);
                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                        super.onReady(youTubePlayer);
                        youTubePlayer.loadVideo(Singleton.getInstance().getWebinarDetail().getWebinarLink(), 0);

                        //youTubePlayer.pause();

                    }
                });
                youTubePlayerView.enableBackgroundPlayback(false);
            } else if (Singleton.getInstance().getWebinarDetail().getWebinarType().equals("embed") || Singleton.getInstance().getWebinarDetail().getWebinarType().equals("121embed")) {
                // videoViewLayoutVisibility(true);
                startVideoPlayer(Singleton.getInstance().getWebinarDetail().getWebinarLink());
                //  initializePlayer("https://www.appsloveworld.com/wp-content/uploads/2018/10/640.mp4");

            } else if (Singleton.getInstance().getWebinarDetail().getWebinarType().equals("vimeo")) {
                playVimeoVideo(Singleton.getInstance().getWebinarDetail().getWebinarLink());

            } else if (Singleton.getInstance().getCurrentWebinar() != null && Singleton.getInstance().getCurrentWebinar().getMainStage().equals(7)) {
                youTubePlayerView.setVisibility(View.VISIBLE);
                fullScreenImageView.setVisibility(View.VISIBLE);
                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                        super.onReady(youTubePlayer);
                        youTubePlayer.loadVideo(Singleton.getInstance().getWebinarDetail().getWebinarLink(), 0);

                        //youTubePlayer.pause();

                    }
                });
                youTubePlayerView.enableBackgroundPlayback(false);
            }
        }

    }

    private void replaceFragment(Fragment fragment, String tag) {

        FragmentManager fm = getParentFragmentManager();
        Bundle args = new Bundle();
        // args.putInt("index", index);
        fragment.setArguments(args);
        fm.beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();
    }

    public void playVimeoVideo(String videoURL) {
        VimeoExtractor.getInstance().fetchVideoWithURL(videoURL, null, new OnVimeoExtractionListener() {
            @Override
            public void onSuccess(VimeoVideo video) {
                String vimeoUrl = video.getStreams().get("720p");
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startVideoPlayer(vimeoUrl);
                    }
                });
                //...
            }

            @Override
            public void onFailure(Throwable throwable) {
                //Error handling here
            }
        });
    }

    private void videoViewLayoutVisibility(boolean b) {
        // if (b) {
        videoViewLayout.setVisibility(View.VISIBLE);
        pause.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        fullScreen.setVisibility(View.VISIBLE);
        videoPlayer.setVisibility(View.VISIBLE);
        videoViewBackgroundImageView.setVisibility(View.GONE);
//        } else {
//            videoViewLayout.setVisibility(View.GONE);
//            pause.setVisibility(View.GONE);
//            seekBar.setVisibility(View.GONE);
//            //fullScreen.setVisibility(View.GONE);
//            videoPlayer.setVisibility(View.GONE);
//            videoViewBackgroundImageView.setVisibility(View.GONE);
//        }
    }

    private void setVideoProgress() {

        current_pos = videoPlayer.getCurrentPosition();
        total_duration = videoPlayer.getDuration();

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

    private void startVideoPlayer(String companyMediaVideo) {
        videoViewLayout.setVisibility(View.VISIBLE);
        pause.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        fullScreen.setVisibility(View.VISIBLE);
        videoPlayer.setVisibility(View.VISIBLE);
        new Handler().postDelayed(this::hideControls, 3000);
        videoViewBackgroundImageView.setVisibility(View.GONE);
        Helper.showProgressBar(progressBar);
        proxy = MainApp.getProxy(Singleton.getInstance().getActivity());
        String proxyUrl = proxy.getProxyUrl(companyMediaVideo);
        videoPlayer.setVideoPath(proxyUrl);
        videoPlayer.seekTo(100);

        videoPlayer.setOnPreparedListener(mp -> {
            Helper.hideProgressBar(progressBar);
            pause.setImageResource(R.drawable.pause);
            videoPlayer.start();
            setVideoProgress();

        });
        videoPlayer.requestFocusFromTouch();

        videoPlayer.setOnErrorListener(mOnErrorListener);
        videoPlayer.setOnCompletionListener(mp -> {
            videoPlayer.pause();
            pause.setImageResource(R.drawable.play);
        });
        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < MIN_CLICK_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (videoPlayer.isPlaying()) {
                    videoPlayer.pause();
                    pause.setImageResource(R.drawable.play);
                }

//                Fragment fragment = new FullScreenMedia();
//                Bundle args = new Bundle();
//
//                args.putInt("seekTo", videoPlayer.getCurrentPosition());
//                args.putString("video", companyMediaVideo);
//                args.putString("type", "mp4");
                startActivity(new Intent(requireContext(), ExoPlayerActivity.class).putExtra("video", companyMediaVideo).putExtra("seekTo", videoPlayer.getCurrentPosition() + 2000));

//                fragment.setArguments(args);
//                getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
//                        .setReorderingAllowed(true)
//                        .addToBackStack("video")
//                        .commit();

                //  Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                //  full = true;


            }
        });
        fullScreenExitImageViewMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < MIN_CLICK_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                //   exitFullScreen();
                full = false;

            }
        });

        setPause();
    }

    private void showControls() {
        if (seekBar.getVisibility() == View.VISIBLE) {
            hideControls();
        } else {
            seekBar.setVisibility(View.VISIBLE);
            pause.setVisibility(View.VISIBLE);
            fullScreen.setVisibility(View.VISIBLE);
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
        fullScreen.setVisibility(View.GONE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
//        int currentOrientation = getResources().getConfiguration().orientation;
//
//        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Log.v("TAG", "Landscape !!!");
//            Helper.showToast("1");
//        } else {
//            // fullScreenClick();
//
//            //  setVideoViewPotrait();
//            Helper.showToast("2");
//
//            Log.v("TAG", "Portrait !!!");
//        }
//
//        if (full)
//            fullScreenClick();
//        else
//            exitFullScreen();
    }

    public void fullScreenClick() {
//        int w = viewGroup.getWidth();
//        int h = viewGroup.getHeight();
//        view.setRotation(90);
//        view.setTranslationX((w - h) / 2);
//        view.setTranslationY((h - w) / 2);
//        ViewGroup.LayoutParams lp = view.getLayoutParams();
//        lp.height = w;
//        lp.width = h;
//        view.requestLayout();

        //  Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);

        fullScreenExitImageViewMedia.setVisibility(View.VISIBLE);
        fullScreen.setVisibility(View.GONE);
        DisplayMetrics metrics = new DisplayMetrics();
        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) videoViewLayout.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.topMargin = 140;
        videoViewLayout.setLayoutParams(params);
    }

    private void exitFullScreen() {

//        int w = viewGroup.getWidth();
//        int h = viewGroup.getHeight();
//        view.setRotation(360);
//        view.setTranslationX(1);
//        view.setTranslationY(1);
//        ViewGroup.LayoutParams lp = view.getLayoutParams();
//        lp.height = (h);
//        lp.width = (w);
//        view.requestLayout();

        DisplayMetrics metrics = new DisplayMetrics();
        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) videoViewLayout.getLayoutParams();

        newLayoutParams.width = ConstraintSet.PARENT_ID;
        newLayoutParams.height = (int) (120 * metrics.density);
        newLayoutParams.leftMargin = 220;
//        newLayoutParams.topMargin = 250;
        newLayoutParams.rightMargin = 200;
        newLayoutParams.bottomMargin = 345;
        newLayoutParams.bottomToBottom = ConstraintSet.PARENT_ID;
        newLayoutParams.leftToLeft = ConstraintSet.PARENT_ID;
        newLayoutParams.rightToRight = ConstraintSet.PARENT_ID;
        newLayoutParams.topToTop = ConstraintSet.PARENT_ID;
        videoViewLayout.setLayoutParams(newLayoutParams);

        fullScreenExitImageViewMedia.setVisibility(View.GONE);
        fullScreen.setVisibility(View.VISIBLE);


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
                slideDown(recyclerLayout);
                floatingButton.setVisibility(View.VISIBLE);

                break;
            case R.id.chatBtn:
                Helper.startUserChat(Singleton.getInstance().getWebinarDetail().getUserInfo().getName(), Singleton.getInstance().getFairData().getFair().getId() + "f" + Singleton.getInstance().getWebinarDetail().getRecruiterId(), "user");

//                Intent intent = new Intent(Singleton.getInstance().getActivity(), CometChatUI.class);
//                intent.putExtra("fairId", Singleton.getInstance().getFairData().getFair().getId());
//                intent.putExtra("intent", new Intent(Singleton.getInstance().getContext(), HomeActivity.class));
//                startActivity(intent);
                break;

        }

    }

    public void onBackPress() {

        if (Singleton.getInstance().getWebinarVideo() && Singleton.getInstance().getHomeState().equals("reception")) {
            prepareMainVideoPlayer(AppConstants.getWebinarToRECEPTION(), false, "back");
        } else if (Singleton.getInstance().getWebinarVideo() && Singleton.getInstance().getHomeState().equals("stands")) {
            prepareMainVideoPlayer(AppConstants.getWebinar_TO_HALL1(), false, "back");
        } else if (Singleton.getInstance().getWebinarVideo() && Singleton.getInstance().getHomeState().equals("hall")) {
            prepareMainVideoPlayer(AppConstants.getReceptionToHall1(), false, "back");
        } else
            requireActivity().getSupportFragmentManager().popBackStack();

        Singleton.getInstance().setLiveWebinar(false);
        Helper.enableTouch();

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
                    webinarButtonsList();
                    floatingButton.setVisibility(View.GONE);
                    slideUp(recyclerLayout);

                }

                break;

            default:
                return false;
        }
        return true;
    }

    private void slideUp(View view) {
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

    private void slideDown(View view) {
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

    private void webinarButtonsList() {

        Singleton.getInstance().getButtonsList().clear();
        Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getReception());
        Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getHall());
        Singleton.getInstance().getButtonsList().add(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getHome());

        manageButtonRecylcerView();
    }


    @Override
    public void ShareClicked(String url, int videoIndex) {

        slideDown(recyclerLayout);


        try {
            youTubePlayerView.release();
        } catch (Exception e) {

        }
        try {


            videoPlayer.pause();
        } catch (Exception e) {

        }
        if (url.equals("hall")) {
            prepareMainVideoPlayer(AppConstants.getWebinar_TO_HALL1(), false, "back");
            Singleton.getInstance().setHomeState("hall");
            Singleton.getInstance().setWebinarToHall(true);
            floatingButton.setVisibility(View.GONE);
            toolBar(false, null);

        } else if (url.equals("reception")) {
            prepareMainVideoPlayer(AppConstants.getWebinarToRECEPTION(), false, "back");
            Singleton.getInstance().setHomeState("reception");
            Singleton.getInstance().setLastPlayedVideo("webinarToReception");
            Singleton.getInstance().setWebinarToReception(true);
            floatingButton.setVisibility(View.GONE);
            toolBar(false, null);


        } else if (url.equals("home")) {
            Singleton.getInstance().setHomeState("home");
            floatingButton.setVisibility(View.GONE);
            toolBar(false, null);

            loadHome();


        }

    }

    private void loadHome() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        Fragment fragment = new Home();
        //replaceFragment(new VideoFragment(), "video");
        getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("home")
                .commit();
    }

    public void toolBar(Boolean flag, String title) {

        if (flag) {
            toolBar.setVisibility(View.VISIBLE);
            titleTxt.setVisibility(View.VISIBLE);
            titleTxt.setText(title);
        } else {
            toolBar.setVisibility(View.GONE);
            titleTxt.setVisibility(View.GONE);
        }

    }
}