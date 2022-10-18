package com.vrd.gsaf.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.vrd.gsaf.R;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;


public class WebView extends Fragment implements View.OnClickListener {
    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView;
    private TextView titleTxt;
    private long mLastClickTime;
    private android.webkit.WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Helper.lockOrientation();

        view = inflater.inflate(R.layout.fragment_web_view, container, false);
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }


    @SuppressLint("CutPasteId")
    private void initializeViews() {
        progressBar = view.findViewById(R.id.progressBar);

        android.webkit.WebView urlWebView = view.findViewById(R.id.webView);
        backImageView = view.findViewById(R.id.backImageView);
        titleTxt = view.findViewById(R.id.titleTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getNeedHelp());
        urlWebView.loadUrl("https://livewebinar.com/338-397-333/p/pKsxEAfSnMukVJBcvj2JuzPlhi3Huhptt5t7NavpuWTXpAKF7HTNpqd2nl7O");
        // urlWebView.loadUrl("https://www.javatpoint.com/android-webview-example");

        setClickListeners();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
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
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
}