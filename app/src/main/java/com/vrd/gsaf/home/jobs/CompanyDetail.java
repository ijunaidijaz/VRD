package com.vrd.gsaf.home.jobs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
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

import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.compnayDetail.CompanyDetails;
import com.vrd.gsaf.api.responses.compnayDetail.Data;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyDetail extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    static String url;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    int socialLinksCounter = 0;
//    RelativeLayout mWebView;
    WebView webView;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView, companyImageView, insta, linkedInImageView, facebookImageView, twitterImageView, youtubeImageView;
    private TextView websiteTxt, descriptionTxt, titleTxt, followUs;
    private int index;
    private long mLastClickTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        view = inflater.inflate(R.layout.fragment_company_detail, container, false);

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
        progressBar = view.findViewById(R.id.progressBar);
        backImageView = view.findViewById(R.id.backImageView);
        youtubeImageView = view.findViewById(R.id.youtube);
        insta = view.findViewById(R.id.insta);
        facebookImageView = view.findViewById(R.id.facebook);
        twitterImageView = view.findViewById(R.id.twitter);
        linkedInImageView = view.findViewById(R.id.linkedIn);
        companyImageView = view.findViewById(R.id.companyImageView);
        titleTxt = view.findViewById(R.id.titleTxt);
        followUs = view.findViewById(R.id.followUs);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCompany());
        descriptionTxt = view.findViewById(R.id.descriptionTxt);
        websiteTxt = view.findViewById(R.id.websiteTxt);
        setClickListeners();
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        Helper.setTextColor(descriptionTxt);

        try {
            Helper.loadRectangleImageFromUrlWithRounded(companyImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getCompaniesData().get(index).getCompanyLogo(), 25);
        } catch (Exception e) {

        }

        if (Helper.isInternetConnected()) {
            if (Singleton.getInstance().getIsLoggedIn())
                callApiForCompanyDetail(false);
            else
                callApiForCompanyDetailWithNoAuth(false);
        }

        retryLayout();
    }

    private void retryLayout() {
        View retryLayout;
        retryLayout = view.findViewById(R.id.somethingWentWrongLayout);
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.isInternetConnected()) {
                    if (Singleton.getInstance().getIsLoggedIn())
                        callApiForCompanyDetail(false);
                    else
                        callApiForCompanyDetailWithNoAuth(false);
                }
            }
        });
    }

    private void setData(Data companyList) {

        if (!companyList.getCompanyList().get(0).getCompanyInstagramUrl().equals("")) {
            insta.setVisibility(View.VISIBLE);
            socialLinksCounter = socialLinksCounter + 1;
            insta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToUrl(companyList.getCompanyList().get(0).getCompanyInstagramUrl());
                }
            });
        }
        if (!companyList.getCompanyList().get(0).getCompanyInUrl().equals("")) {
            linkedInImageView.setVisibility(View.VISIBLE);
            socialLinksCounter = socialLinksCounter + 1;
            linkedInImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToUrl(companyList.getCompanyList().get(0).getCompanyInUrl());
                }
            });
        }
        if (!companyList.getCompanyList().get(0).getCompanyFacebookUrl().equals("")) {
            facebookImageView.setVisibility(View.VISIBLE);
            socialLinksCounter = socialLinksCounter + 1;
            facebookImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToUrl(companyList.getCompanyList().get(0).getCompanyFacebookUrl());
                }
            });
        }
        if (!companyList.getCompanyList().get(0).getCompanyTwitterUrl().equals("")) {
            twitterImageView.setVisibility(View.VISIBLE);
            socialLinksCounter = socialLinksCounter + 1;
            twitterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToUrl(companyList.getCompanyList().get(0).getCompanyTwitterUrl());
                }
            });
        }
        if (!companyList.getCompanyList().get(0).getCompanyYoutubeUrl().equals("")) {
            youtubeImageView.setVisibility(View.VISIBLE);
            socialLinksCounter = socialLinksCounter + 1;
            youtubeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToUrl(companyList.getCompanyList().get(0).getCompanyYoutubeUrl());
                }
            });
        }
        if (socialLinksCounter > 0) {
            followUs.setVisibility(View.VISIBLE);
        } else followUs.setVisibility(View.GONE);

        descriptionTxt.setMovementMethod(new ScrollingMovementMethod());

//        descriptionTxt.setMovementMethod(LinkMovementMethod.getInstance());
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAbout() + " "
                + companyList.getCompanyList().get(0).getCompanyName());
        websiteTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoToOurWebsite());
        followUs.setText(Singleton.getKeywords().getFollowUsOn());
        Helper.setTextColor(followUs);
        websiteTxt.setPaintFlags(websiteTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (companyList.getCompanyList().get(0).getCompanyWebUrl() == null || companyList.getCompanyList().get(0).getCompanyWebUrl().equalsIgnoreCase("")) {
            websiteTxt.setVisibility(View.GONE);
        }
        websiteTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.goToUrl(companyList.getCompanyList().get(0).getCompanyWebUrl());
            }
        });
        Helper.loadRectangleImageFromUrlWithRounded(companyImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + companyList.getCompanyList().get(0).getCompanyLogo(), 25);
//        Glide.with(Singleton.getInstance().getContext()).load(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + companyList.getCompanyList().getCompanyLogo())
//                .placeholder(R.drawable.add_emoji)
//                .error(R.drawable.add_emoji)
//                .apply(RequestOptions.circleCropTransform())
//                .into(companyImageView);
//        Helper.loadHtml(descriptionTxt, companyList.getCompanyList().get(0).getDescription());
        webView = view.findViewById(R.id.webView);
//        webView = new WebView(getActivity());

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = companyList.getCompanyList().get(0).getDescription();
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
                return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            }
        });
//        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        try {
//            mWebView.addView(webView);
            webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
        } catch (Exception e) {

        }


    }

    private void goToUrl(String url) {
        if (!url.equals("")) {
            try {
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            } catch (Exception e) {

            }
        } else
            Helper.showToast("Not Available");
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {

        backImageView.setOnClickListener(this);
        websiteTxt.setOnClickListener(this);
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

    private void callApiForCompanyDetail(boolean pagination) {
        Helper.showProgressBar(progressBar);

        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        try {
            JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        } catch (Exception e) {

        }
        //  if (Singleton.getInstance().getHomeState() != null && Singleton.getInstance().getHomeState().equals("stands")) {
        JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());
//        } else
//            JsonObject.addProperty("company_id", Singleton.getInstance().());


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
                        companyImageView.setVisibility(View.GONE);
                        websiteTxt.setVisibility(View.GONE);
                    } else if (response.body().getStatus()) {
                        setData(response.body().getData());
                    } else {
                        Helper.somethingWentWrong(getView());
                        companyImageView.setVisibility(View.GONE);
                        websiteTxt.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    companyImageView.setVisibility(View.GONE);
                    websiteTxt.setVisibility(View.GONE);
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<CompanyDetails> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
                companyImageView.setVisibility(View.GONE);
                websiteTxt.setVisibility(View.GONE);
            }

        });

    }

    private void callApiForCompanyDetailWithNoAuth(boolean pagination) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());
        String url = "api/front/exibitor/detail";


        Call<CompanyDetails> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getCompanyDetailWithNoAuth(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, url, JsonObject);
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
                        companyImageView.setVisibility(View.GONE);
                        websiteTxt.setVisibility(View.GONE);
                        Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        setData(response.body().getData());
                    } else {
                        Helper.somethingWentWrong(getView());
                        companyImageView.setVisibility(View.GONE);
                        websiteTxt.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    companyImageView.setVisibility(View.GONE);
                    websiteTxt.setVisibility(View.GONE);
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<CompanyDetails> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
                companyImageView.setVisibility(View.GONE);
                websiteTxt.setVisibility(View.GONE);
            }

        });

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

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.destroy();
//            destroyWebView();
        }
        super.onDestroy();
    }

    public void destroyWebView() {
        // Make sure you remove the WebView from its parent view before doing anything.
//        mWebView.removeAllViews();
        webView.clearHistory();

        // NOTE: clears RAM cache, if you pass true, it will also clear the disk cache.
        // Probably not a great idea to pass true if you have other WebViews still alive.
        webView.clearCache(true);

        // Loading a blank page is optional, but will ensure that the WebView isn't doing anything when you destroy it.
        webView.loadUrl("about:blank");

        webView.onPause();
        webView.removeAllViews();
        webView.destroyDrawingCache();

        // NOTE: This pauses JavaScript execution for ALL WebViews,
        // do not use if you have other WebViews still alive.
        // If you create another WebView after calling this,
        // make sure to call mWebView.resumeTimers().
        webView.pauseTimers();

        // NOTE: This can occasionally cause a segfault below API 17 (4.2)
        webView.destroy();

        // Null out the reference so that you don't end up re-using it.
        webView = null;


    }
}