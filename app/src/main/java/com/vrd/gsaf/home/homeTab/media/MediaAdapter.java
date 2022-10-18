package com.vrd.gsaf.home.homeTab.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.vrd.gsaf.R;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.home.fullScreenMedia.FullScreenMedia;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    public static final String VIDEO_ID = "6kx7kghRnvk";
    private static final long CLICK_TIME_INTERVAL = 500;
    private final List<MediaModel> dataList;
    public FragmentManager views;
    protected HttpProxyCacheServer proxy;
    protected MainApp app = new MainApp();
    protected double current_pos, total_duration;
    protected Handler mHandler, handler;
    OnFullScreenClickListener mCallback;
    private Context context;
    private long mLastClickTime = System.currentTimeMillis();

    private com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayerPlaying;
    private VideoView videoPlayerPlaying;


    public MediaAdapter(FragmentManager view) {
        this.dataList = Singleton.getInstance().getMediaList();
        this.views = view;
        mHandler = new Handler();
        handler = new Handler();

    }

    public void setOnFullScreenClickListener(OnFullScreenClickListener mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_video_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        int index = position;
        if (dataList.get(index).getVideoUrl() != null) {
            setVideoView(holder, index);
            holder.imageLayout.setVisibility(View.GONE);
            holder.youTubePlayerView2.setVisibility(View.GONE);
            holder.videoViewLayout.setVisibility(View.VISIBLE);
            holder.youtubeLayout.setVisibility(View.GONE);

        } else if (dataList.get(index).getYoutubeId() != null) {
            holder.youTubePlayerView2.setVisibility(View.VISIBLE);
            holder.videoViewLayout.setVisibility(View.GONE);
            holder.imageLayout.setVisibility(View.GONE);
            holder.youtubeLayout.setVisibility(View.VISIBLE);

            //this.getLifecycle().addObserver(holder.youTubePlayerView2);

            holder.youTubePlayerView2.enableBackgroundPlayback(false);
            Helper.setYoutubePlayerSettings(holder.youTubePlayerView2);
            holder.youTubePlayerView2.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                    super.onReady(youTubePlayer);
                    youTubePlayer.loadVideo(dataList.get(index).getYoutubeId(), 0);
                    youTubePlayer.pause();
                    Singleton.getInstance().setYoutubePLayers(youTubePlayer);
                    youTubePlayer.addListener(new YouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            youTubePlayerPlaying = youTubePlayer;
                        }

                        @Override
                        public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState playerState) {
                            if (playerState.name().equals("PLAYING"))
                                youTubePlayerPlaying = youTubePlayer;

                        }

                        @Override
                        public void onPlaybackQualityChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackQuality playbackQuality) {

                        }

                        @Override
                        public void onPlaybackRateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackRate playbackRate) {
//                            Helper.showToast("on ready");

                        }

                        @Override
                        public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError playerError) {
//                            Helper.showToast("on ready");

                        }

                        @Override
                        public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float v) {
//                            Helper.showToast("on ready");

                        }

                        @Override
                        public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float v) {
//                            Helper.showToast("on ready");

                        }

                        @Override
                        public void onVideoLoadedFraction(@NonNull YouTubePlayer youTubePlayer, float v) {
//                            Helper.showToast("on ready");

                        }

                        @Override
                        public void onVideoId(@NonNull YouTubePlayer youTubePlayer, @NonNull String s) {

                        }

                        @Override
                        public void onApiChange(@NonNull YouTubePlayer youTubePlayer) {

                        }
                    });

                }
            });


            holder.youtubeFullScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment fragment = new FullScreenMedia();
                    FragmentManager fm = views;
                    Bundle args = new Bundle();
                    args.putString("type", "youtube");
                    args.putString("video", dataList.get(index).getYoutubeId());
                    fragment.setArguments(args);
                    fm.beginTransaction().add(R.id.frameLayout, fragment)
                            .setReorderingAllowed(true)
                            .addToBackStack("video")
                            .commit();
                    if (youTubePlayerPlaying != null) youTubePlayerPlaying.pause();
//                    holder.youTubePlayerView2.release();
                }
            });

        } else if (dataList.get(index).getImages() != null) {
            holder.youTubePlayerView2.setVisibility(View.GONE);
            holder.videoViewLayout.setVisibility(View.GONE);
            holder.imageLayout.setVisibility(View.VISIBLE);
            holder.youtubeLayout.setVisibility(View.GONE);


            //this.getLifecycle().addObserver(holder.youTubePlayerView2);

            Helper.loadImageWithGlide(holder.imageLayout, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + dataList.get(index).getImages());

            holder.imageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        youTubePlayerPlaying.pause();
                    } catch (Exception e) {

                    }
                    try {
                        videoPlayerPlaying.pause();
                    } catch (Exception e) {

                    }
                    Fragment fragment = new FullScreenMedia();
                    //replaceFragment(new VideoFragment(), "video");
                    FragmentManager fm = views;
                    Bundle args = new Bundle();
                    args.putString("type", "image");
                    args.putString("video", dataList.get(index).getImages());
                    fragment.setArguments(args);
                    fm.beginTransaction().add(R.id.frameLayout, fragment)
                            .setReorderingAllowed(true)
                            .addToBackStack("video")
                            .commit();
                }
            });

        }


    }

    private void setVideoView(CustomViewHolder holder, int index) {

        proxy = MainApp.getProxy(Singleton.getInstance().getActivity());
        //   String proxyUrl = proxy.getProxyUrl(Singleton.getInstance().getMediaList().get(holder.getPosition()).getVideoUrl());
        String proxyUrl = proxy.getProxyUrl(dataList.get(holder.getPosition()).getVideoUrl());
        holder.fullScreen.setVisibility(View.VISIBLE);
        holder.seekBar.setVisibility(View.VISIBLE);
        holder.videoPlayer.setVideoPath(proxyUrl);
        holder.videoPlayer.seekTo(100);
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(proxyUrl,
                MediaStore.Images.Thumbnails.MINI_KIND);

        holder.videoPlayer.setOnPreparedListener(mp -> {
            setVideoProgress(holder);


        });


        holder.videoPlayer.setOnCompletionListener(mp -> {
            holder.videoPlayer.pause();
            holder.pause.setImageResource(R.drawable.play);
        });

        holder.fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                holder.videoPlayer.pause();
                holder.pause.setImageResource(R.drawable.play);
                Fragment fragment = new FullScreenMedia();
                //replaceFragment(new VideoFragment(), "video");
                FragmentManager fm = views;
                Bundle args = new Bundle();
                args.putString("type", "mp4");
                args.putString("video", dataList.get(index).getVideoUrl());
                fragment.setArguments(args);
                fm.beginTransaction().add(R.id.frameLayout, fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("video")
                        .commit();


            }
        });
//        holder.orientationImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                long now = System.currentTimeMillis();
//                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
//                    return;
//                }
//                mLastClickTime = now;
//            }
//        });

        setPause(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CustomViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.pause.setImageResource(R.drawable.play);
        holder.videoPlayer.pause();

    }

    private void setVideoProgress(CustomViewHolder holder) {
        //get the video duration


        current_pos = holder.videoPlayer.getCurrentPosition();
        total_duration = holder.videoPlayer.getDuration();

        //display video duration
        //total.setText(timeConversion((long) total_duration));
        //current.setText(timeConversion((long) current_pos));
        holder.seekBar.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = holder.videoPlayer.getCurrentPosition();
                    //current.setText(timeConversion((long) current_pos));
                    holder.seekBar.setProgress((int) current_pos);
                    handler.postDelayed(this, 100);
                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 100);

        //seekbar change listner
        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                holder.videoPlayer.seekTo((int) current_pos);
            }
        });


    }

    //pause video
    protected void setPause(CustomViewHolder holder) {
        holder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.videoPlayer.isPlaying()) {
                    holder.videoPlayer.pause();
                    holder.pause.setImageResource(R.drawable.play);
                } else {
                    videoPlayerPlaying = holder.videoPlayer;
                    holder.videoPlayer.start();
                    holder.pause.setImageResource(R.drawable.pause);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnFullScreenClickListener {
        void fullScreenClick();

        void orientationChange();
    }
}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    SeekBar seekBar;
    ImageView pause, youtubeFullScreen, fullScreen, imageLayout;
    //  ImageView orientationImage;
    VideoView videoPlayer;
    ConstraintLayout videoViewLayout, youtubeLayout;

    com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView youTubePlayerView2;


    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        seekBar = mView.findViewById(R.id.seekBarMedia);
        youtubeFullScreen = mView.findViewById(R.id.youtubeFullScreen);
        youtubeLayout = mView.findViewById(R.id.youtubeLayout);
        imageLayout = mView.findViewById(R.id.imageLayout);
        pause = mView.findViewById(R.id.pauseMedia);
        //  orientationImage = mView.findViewById(R.id.orientationImageViewMedia);
        fullScreen = mView.findViewById(R.id.fullScreenImageViewMedia);
        videoPlayer = mView.findViewById(R.id.videoPlayerMediaMedia);
        youTubePlayerView2 = mView.findViewById(R.id.youtube_player_view);
        videoViewLayout = mView.findViewById(R.id.videoViewLayoutStand);

    }


}



