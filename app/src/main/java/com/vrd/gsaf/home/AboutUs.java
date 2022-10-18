package com.vrd.gsaf.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AboutUs extends Fragment implements View.OnClickListener {


    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    private View view;
    private ProgressBar progressBar;
    private ImageView menuImageView;
    private TextView aboutUs, titleTxt;
    private long mLastClickTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Helper.lockOrientation();
        view = inflater.inflate(R.layout.fragment_about_us, container, false);
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        progressBar = view.findViewById(R.id.progressBar);
        Helper.showProgressBar(progressBar);
        menuImageView = view.findViewById(R.id.menuImageView);
        aboutUs = view.findViewById(R.id.aboutUsTxt);
        mainLayout = view.findViewById(R.id.mainLayout);
        titleTxt = view.findViewById(R.id.titleTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAbout());
        Helper.setTextColor(aboutUs);
        if (Helper.isInternetConnected())
            callApiForAboutUs();

        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        setClickListeners();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        menuImageView.setOnClickListener(this);
        retryLayout();
    }

    private void retryLayout() {
        View retryLayout;
        retryLayout = view.findViewById(R.id.somethingWentWrongLayout);
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.isInternetConnected()) {
                    callApiForAboutUs();
                }
            }
        });
    }


    private void onBackPress() {
        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
            case R.id.menuImageView:
                Helper.menuClick();
                break;
        }
    }


    private void callApiForAboutUs() {
        String url = "api/auth/about/fair/" + Singleton.getInstance().getFairData().getFair().getId().toString();
        Call<com.vrd.gsaf.api.responses.aboutUs.AboutUs> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getAboutUs(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.aboutUs.AboutUs>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.aboutUs.AboutUs> call, Response<com.vrd.gsaf.api.responses.aboutUs.AboutUs> response) {
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
                        loadData(response.body().getData().getFairNews());
//                        Helper.loadHtml(aboutUs, response.body().getData().getFairNews());
                        Helper.hideProgressBar(progressBar);
                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.aboutUs.AboutUs> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
            }

        });
    }

    private void loadData(String html) {
        WebView webView = view.findViewById(R.id.webView);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
            }
        });
//        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        try {
            webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
        } catch (Exception e) {

        }
    }

}