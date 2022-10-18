package com.vrd.gsaf.home.fullScreenMedia;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.vrd.gsaf.R;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.databinding.FragmentFullScreenMediaBinding;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

public class FullScreenMedia extends Fragment implements View.OnClickListener, Player.Listener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    private final boolean mute = false;
    protected YouTubePlayerView youTubePlayer;
    protected HttpProxyCacheServer proxy;
    protected MainApp app = new MainApp();
    protected double current_pos, total_duration;
    SeekBar seekBar;
    VideoView videoPlayer;
    FragmentFullScreenMediaBinding binding;
    ConstraintLayout videoViewLayout;
    Player.Listener playbackStateListener;
    ConstraintLayout mainLayout;
    private long mLastClickTime;
    private View view;
    private ProgressBar progressBar;
    private ImageView orientationYoutube, imageView, volumeImageView, backImageView, pause, orientationImageViewMedia, videoViewBackgroundImageView;
    private boolean landscape = false;
    private String video, type;
    private int seekTo;
    private MediaPlayer mediaPlayer;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    public void onStart() {
        super.onStart();
        Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
    }

    @Override
    public void onStop() {

        youTubePlayer.release();
        videoPlayer.pause();
        Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Singleton.getInstance().getBottomNavigationView().setVisibility(View.VISIBLE);
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onStop();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_full_screen_media, container, false);
        view = binding.getRoot();
        //  this.setRetainInstance(true);
        Bundle bundle = this.getArguments();
        seekTo = bundle.getInt("seekTo");
        type = bundle.getString("type");
        video = bundle.getString("video");
        Helper.hideKeyboard();
        initializeViews();
        if (type.equals("mp4")) {
            Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            landscape = true;
        }


        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {

        progressBar = view.findViewById(R.id.progressBar);
        imageView = view.findViewById(R.id.imageView);
        orientationYoutube = view.findViewById(R.id.orientationYoutube);
        videoViewBackgroundImageView = view.findViewById(R.id.videoViewBackgroundImageView);
        backImageView = view.findViewById(R.id.backImageView);
        volumeImageView = view.findViewById(R.id.volumeImageView);
        youTubePlayer = view.findViewById(R.id.youtubePlayer);
        backImageView.setOnClickListener(this);
        volumeImageView.setOnClickListener(this);
        orientationYoutube.setOnClickListener(this);

        videoViewLayout = view.findViewById(R.id.videoViewLayoutStand);
        seekBar = view.findViewById(R.id.seekBarMedia);
        pause = view.findViewById(R.id.pauseMedia);
        orientationImageViewMedia = view.findViewById(R.id.orientationImageViewMedia);

        videoPlayer = view.findViewById(R.id.videoPlayerMediaMedia);
        mainLayout = view.findViewById(R.id.mainLayout);
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

//        videoPlayer.setScaleX(1);
//        videoPlayer.setScaleY(1);
        videoPlayer.requestFocus();
        ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.FILL_PARENT,
                ConstraintLayout.LayoutParams.FILL_PARENT);
        videoPlayer.setLayoutParams(p);

        proxy = MainApp.getProxy(Singleton.getInstance().getActivity());

        if (type.equals("youtube")) {
            youTubePlayer.setVisibility(View.VISIBLE);
            videoViewLayout.setVisibility(View.GONE);
            binding.exoPlayerLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            startYoututbePlayer();
        } else if (type.equals("mp4")) {
            orientationYoutube.setVisibility(View.GONE);


            youTubePlayer.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
//            fitStretchClick();
            videoViewLayout.setVisibility(View.GONE);
//            startVideoPlayer(video);
            binding.exoPlayerLayout.setVisibility(View.VISIBLE);
            setExoPlayer();
        } else if (type.equals("image")) {
            orientationYoutube.setVisibility(View.GONE);

            videoViewLayout.setVisibility(View.GONE);
            youTubePlayer.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Helper.loadImageWithGlide(imageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + video);
        }

        // retryLayout();
    }


    private void startYoututbePlayer() {
        youTubePlayer.enterFullScreen();
        Helper.setYoutubePlayerSettings(youTubePlayer);
        youTubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                // youTubePlayer.loadVideo("8MJY_Xqui8Y", 0);

                //   if (Home.this != null && Home.this.isVisible()) {
                youTubePlayer.loadVideo(video, seekTo);
                // youTubePlayer.mute();
                //  }
            }
        });


    }

    private void startVideoPlayer(String companyMediaVideo) {
        Helper.showProgressBar(progressBar);
        String proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(companyMediaVideo);

        videoPlayer.setVideoPath(proxyUrl);
        videoPlayer.seekTo(seekTo);

        // videoPlayer.setZOrderOnTop(true);
        // videoPlayer.seekTo(100);
        videoPlayer.setOnPreparedListener(mp -> {
            Helper.hideProgressBar(progressBar);
            setVideoProgress();
            pause.setImageResource(R.drawable.pause);
            videoViewBackgroundImageView.setVisibility(View.GONE);
            videoPlayer.start();
            mp.setVolume(0, 0);
            mediaPlayer = mp;

            videoPlayer.setRotation(360f);

        });
        videoPlayer.requestFocusFromTouch();
        videoPlayer.setOnCompletionListener(mp -> {
            videoPlayer.pause();
            pause.setImageResource(R.drawable.play);
        });
        //  videoPlayer.setRotation(90f);
        orientationImageViewMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < MIN_CLICK_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                changeOrientation();
            }
        });

        setPause();
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
                if (landscape)
                    Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                getActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.orientationYoutube:
                changeOrientation();

                break;

        }

    }

    private void changeOrientation() {
        if (!landscape) {
            landscape = true;
            Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            landscape = false;
            Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void setExoPlayer() {
        hideSystemUi();
        if (player == null) {
//            DataSource.Factory dataSourceFactory =
//                    new DefaultHttpDataSourceFactory(Util.getUserAgent(getActivity()
//                            , getActivity().getApplicationInfo().loadLabel(getActivity().getPackageManager()).toString()));
// Passing Load Control
            LoadControl loadControl = new DefaultLoadControl.Builder()
                    .setBufferDurationsMs(25000, 50000, 1500, 2000).createDefaultLoadControl();

            @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER;

            RenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity()).setExtensionRendererMode(extensionRendererMode);
            PlayerControlView controls = view.findViewById(R.id.controls);
            binding.controls.setVisibility(View.VISIBLE);
            binding.controls.setPlayer(binding.playerView.getPlayer());
// Create a progressive media source pointing to a stream uri.
            String proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(video);
//            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
//                    .createMediaSource(Uri.parse(proxyUrl));
// Create a player instance.
            player = new SimpleExoPlayer.Builder(getActivity(), renderersFactory).setLoadControl(loadControl).build();
// Prepare the player with the media source.
            binding.playerView.setPlayer(player);
            player.seekTo(seekTo);
            MediaSource videoSource = buildMediaSource(Uri.parse(proxyUrl));
            player.setPlayWhenReady(true);
            player.setMediaSource(videoSource);
            player.prepare();
            binding.playerView.setKeepScreenOn(true);
//            player.play();
        }
    }

//    private MediaSource newVideoSource(String url) {
//        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        String userAgent = Util.getUserAgent(getActivity(), "AndroidVideoCache sample");
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), userAgent, bandwidthMeter);
//        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//        return new ExtractorMediaSource(Uri.parse(url), dataSourceFactory, extractorsFactory, null, null);
//    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
//            player.removeListener(playbackStateListener);
//            player.removeListener(this);
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getActivity(), "example");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri));
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        binding.playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onIsPlayingChanged(boolean isPlaying) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            imgViewChangeScreenOrientation.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.fullscreen_exit));
//            fullscreen = true;
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            imgViewChangeScreenOrientation.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.full_screen));
//            fullscreen = false;
//        }
    }

    @Override
    public void onDestroyView() {
        releasePlayer();
        super.onDestroyView();
    }

    class PlaybackStateListener implements Player.Listener {

//        @Override
//        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
//            Helper.showToast("onTimelineChanged");
//        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Helper.showToast("onTracksChanged");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Helper.showToast("onLoadingChanged");
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {
            Helper.showToast("onPlayerStateChanged");
            String stateString;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                case ExoPlayer.STATE_READY:
                    player.seekTo(seekTo);
                    stateString = "ExoPlayer.STATE_READY     -";
//                startTime = Constants.getCurrentTime();
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
            Log.i("TAG", "onPlayerStateChanged: " + stateString);
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }
//
//        @Override
//        public void onPlayerError(ExoPlaybackException error) {
//
//        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    }

}