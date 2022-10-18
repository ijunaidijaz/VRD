package com.vrd.gsaf.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.vrd.gsaf.R;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

public class ExoPlayerActivity extends AppCompatActivity implements Player.Listener, View.OnClickListener {

    private static final String TAG = "";
    VideoView videoView;
    String video, type;
    boolean fullscreen;
    int seekTo;
    YouTubePlayerView youTubePlayerView;
    private PlaybackStateListener playbackStateListener;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private ImageView imgViewChangeScreenOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_exo_player);
        playerView = findViewById(R.id.playerView);
        youTubePlayerView = findViewById(R.id.youtubePlayer);
        playbackStateListener = new PlaybackStateListener();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        imgViewChangeScreenOrientation.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        hideSystemUi();
        Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
        initializePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
//        hideSystemUi();
        if (getIntent().hasExtra("type") && getIntent().getExtras().getString("type").equalsIgnoreCase("youtube")) {
            playerView.setVisibility(View.GONE);
            youTubePlayerView.setVisibility(View.VISIBLE);
            playYoutubeVideo();
        } else {
            if (player == null) {
                youTubePlayerView.setVisibility(View.GONE);
                initializePlayer();
            }
        }
    }

    private void playYoutubeVideo() {
        seekTo = getIntent().getExtras().getInt("seekTo");
        type = getIntent().getExtras().getString("type");
        video = getIntent().getExtras().getString("video");
        youTubePlayerView.enterFullScreen();
        youTubePlayerView.getPlayerUiController().showYouTubeButton(false);
        youTubePlayerView.getPlayerUiController().showFullscreenButton(false);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
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

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
        if (youTubePlayerView != null) {
            youTubePlayerView.release();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (youTubePlayerView != null) {
            youTubePlayerView.release();
        }
        releasePlayer();
        Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Singleton.getInstance().getBottomNavigationView().setVisibility(View.VISIBLE);
        releasePlayer();
    }


    private void initializePlayer() {
        if (player == null) {
//            DataSource.Factory dataSourceFactory =
//                    new DefaultHttpDataSourceFactory(Util.getUserAgent(this
//                            , this.getApplicationInfo().loadLabel(this.getPackageManager()).toString()));
// Passing Load Control
            LoadControl loadControl = new DefaultLoadControl.Builder()
                    .setBufferDurationsMs(25000, 50000, 1500, 2000).createDefaultLoadControl();

            @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER;

            RenderersFactory renderersFactory = new DefaultRenderersFactory(this).setExtensionRendererMode(extensionRendererMode);
            PlayerControlView controls = new PlayerControlView(this);
            controls.setPlayer(playerView.getPlayer());
// Create a progressive media source pointing to a stream uri.
            video = getIntent().getExtras().getString("video");
            seekTo = getIntent().getExtras().getInt("seekTo");
            String proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(video);
//            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
//                    .createMediaSource(MediaItem.fromUri(Uri.parse(proxyUrl)));
// Create a player instance.
            player = new SimpleExoPlayer.Builder(this, renderersFactory).setLoadControl(loadControl).build();
// Prepare the player with the media source.
            playerView.setPlayer(player);
            player.seekTo(seekTo);
//            player.prepare(mediaSource);

//            player.prepare(mediaSource, true, true);
            MediaSource videoSource = buildMediaSource(Uri.parse(proxyUrl));
            player.setPlayWhenReady(true);
            player.prepare(videoSource);

            playerView.setKeepScreenOn(true);

        }
    }

//    private MediaSource newVideoSource(String url) {
//        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        String userAgent = Util.getUserAgent(this, "AndroidVideoCache sample");
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, userAgent, bandwidthMeter);
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
                new DefaultDataSourceFactory(this, "example");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri));
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    //
//    @Override
//    public void onIsPlayingChanged(boolean isPlaying) {
//
//    }
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onClick(View view) {

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

    class PlaybackStateListener implements Player.Listener {
//
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