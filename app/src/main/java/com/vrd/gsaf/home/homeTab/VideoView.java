package com.vrd.gsaf.home.homeTab;

import android.os.Handler;
import android.util.DisplayMetrics;

public class VideoView extends Home {

    private final DisplayMetrics displayMetrics = new DisplayMetrics();
    private final Home home;
    protected Handler mHandler, handler;


    public VideoView(Home home) {
        this.home = home;

//        home.mainPlayer.stopPlayback();

    }

    protected void changeLayout(String options) {
        switch (options) {
            case "orientation":
                //  landscapeMode();
                break;
        }

    }

//    private void landscapeMode()
//    {
//        //videoViewLayout.setVisibility(View.VISIBLE);
//
//        lp = (ConstraintLayout.LayoutParams) videoViewLayout.getLayoutParams();
//        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        lp.height = displayMetrics.heightPixels - 300;
//        lp.width = displayMetrics.widthPixels;
//        isFullScreen = true;
//        if(Singleton.getInstance().getHomeState().equals("home"))
//            enterBtn.setVisibility(View.GONE);
//        fullScreen.setVisibility(View.GONE);
//        menuImageView.setVisibility(View.GONE);
//        Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
//        lp.setMargins(Helper.getSizeInSp(0),Helper.getSizeInSp(0),Helper.getSizeInSp(0),Helper.getSizeInSp(0));
//
//        videoViewLayout.setLayoutParams(lp);
//    }
//
//    protected void libraryPlayer(String path, Boolean looping) {
//        if (home.videoViewLayout.getVisibility() == View.GONE)
//            home.videoViewLayout.setVisibility(View.VISIBLE);
//
//        home.proxy = home.app.getProxy(Singleton.getInstance().getActivity());
//        String proxyUrl = home.proxy.getProxyUrl(path);
//        mHandler = new Handler();
//        handler = new Handler();
//        home.videoView.setVideoPath(proxyUrl);
//        home.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(looping);
//                home.videoView.setSaveEnabled(true);
//                home.videoView.start();
//                setVideoProgress();
//            }
//        });
//
//        setPause();
//        setFullScreen();
//        hideLayout();
//        orientationChange();
//
//    }
//
//
//    private void orientationChange() {
//        home.screenRotation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Singleton.getInstance().getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                    Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    hideLayout();
//                } else
//                    Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            }
//        });
//    }
//
//
//    // display video progress
//    private void setVideoProgress() {
//        //get the video duration
//        home.current_pos = home.videoView.getCurrentPosition();
//        home.total_duration = home.videoView.getDuration();
//
//        //display video duration
//        //total.setText(timeConversion((long) total_duration));
//        //current.setText(timeConversion((long) current_pos));
//        home.seekBar.setMax((int) home.total_duration);
//        final Handler handler = new Handler();
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    home.current_pos = home.videoView.getCurrentPosition();
//                    //current.setText(timeConversion((long) current_pos));
//                    home.seekBar.setProgress((int) current_pos);
//                    handler.postDelayed(this, 1000);
//                } catch (IllegalStateException ed) {
//                    ed.printStackTrace();
//                }
//            }
//        };
//        handler.postDelayed(runnable, 1000);
//
//        //seekbar change listner
//        home.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                home.current_pos = seekBar.getProgress();
//                home.videoView.seekTo((int) current_pos);
//            }
//        });
//    }
//
//    //pause video
//    protected void setPause() {
//        home.pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (home.videoView.isPlaying()) {
//                    home.videoView.pause();
//                    home.pause.setImageResource(R.drawable.play);
//                } else {
//                    home.videoView.start();
//                    home.pause.setImageResource(R.drawable.pause);
//                }
//            }
//        });
//    }
//
//
//    private void setFullScreen() {
//        home.fullScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!home.isFullScreen) {
//                    Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                    home.lp.height = displayMetrics.heightPixels;
//                    home.lp.width = displayMetrics.widthPixels;
//                    home.isFullScreen = true;
//                    home.fullScreen.setImageResource(R.drawable.fullscreen_exit);
//
//                    if (Singleton.getInstance().getHomeState().equals("home"))
//                        home.enterBtn.setVisibility(View.GONE);
//                    else if (Singleton.getInstance().getHomeState().equals("reception"))
//                        home.floatingButton.setVisibility(View.GONE);
//                    home.menuImageView.setVisibility(View.GONE);
//                    Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
//                    home.lp.setMargins(Helper.getSizeInSp(0),
//                            Helper.getSizeInSp(0), Helper.getSizeInSp(0), Helper.getSizeInSp(0));
//
//                } else {
//                    home.fullScreen.setImageResource(R.drawable.full_screen);
//                    home.isFullScreen = false;
//                    home.lp.height = Helper.getSizeInSp(175);
//                    home.lp.width = displayMetrics.widthPixels - 200;
//                    Singleton.getInstance().getBottomNavigationView().setVisibility(View.VISIBLE);
//                    home.lp.setMargins(Helper.getSizeInSp(55), Helper.getSizeInSp(55), Helper.getSizeInSp(55), Helper.getSizeInSp(55));
//                    if (Singleton.getInstance().getHomeState().equals("home"))
//                        home.enterBtn.setVisibility(View.VISIBLE);
//                    else if (Singleton.getInstance().getHomeState().equals("reception"))
//                        home.floatingButton.setVisibility(View.VISIBLE);
//                    home.menuImageView.setVisibility(View.VISIBLE);
//                }
//                home.videoViewLayout.setLayoutParams(home.lp);
//
//
//            }
//        });
//    }
//
//
//    protected void hideLayout() {
//
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
////                if (home.videoViewLayout.getVisibility() == View.GONE)
////                    home.videoViewLayout.setVisibility(View.VISIBLE);
//                home.pause.setVisibility(View.GONE);
//                home.seekBar.setVisibility(View.GONE);
//                home.screenRotation.setVisibility(View.GONE);
//                home.fullScreen.setVisibility(View.GONE);
//                // showProgress.setVisibility(View.GONE);
//                // isVisible = false;
//            }
//        };
//        handler.postDelayed(runnable, 5000);
//        home.videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mHandler.removeCallbacks(runnable);
//                home.seekBar.setVisibility(View.VISIBLE);
//                home.pause.setVisibility(View.VISIBLE);
//                home.screenRotation.setVisibility(View.VISIBLE);
//                if (Singleton.getInstance().getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
//                    home.fullScreen.setVisibility(View.VISIBLE);
//                mHandler.postDelayed(runnable, 5000);
//
//            }
//        });
//
//    }

}
