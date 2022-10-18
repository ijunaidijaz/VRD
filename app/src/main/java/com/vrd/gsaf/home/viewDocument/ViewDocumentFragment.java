package com.vrd.gsaf.home.viewDocument;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.vrd.gsaf.R;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;


public class ViewDocumentFragment extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    private View view;
    private ProgressBar progressBar;
    private String link;
    private ImageView backImageView;
    private TextView titleTxt;
    private long mLastClickTime;

    @Override
    public void onStart() {
        super.onStart();
        Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_document, container, false);
        Bundle bundle = this.getArguments();
        link = bundle.getString("link");
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }

    private void initializeViews() {
        ProgressDialog progDailog = ProgressDialog.show(Singleton.getInstance().getContext(), "Loading", "Please wait...", true);
        progDailog.setCancelable(true);

        progressBar = view.findViewById(R.id.progressBar);

        android.webkit.WebView urlWebView = view.findViewById(R.id.webView);
        backImageView = view.findViewById(R.id.backImageView);
        backImageView.setOnClickListener(this);
        titleTxt = view.findViewById(R.id.titleTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDocuments());


        urlWebView.getSettings().setJavaScriptEnabled(true);
        urlWebView.getSettings().setLoadWithOverviewMode(true);
        urlWebView.getSettings().setUseWideViewPort(true);

        // Configure the client to use when opening URLs
        urlWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();

                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();

            }
        });
        // urlWebView.loadUrl("https://www.tutorialspoint.com");
        urlWebView.loadUrl(String.valueOf(Uri.parse(link)));
        // urlWebView.loadUrl("https://www.javatpoint.com/android-webview-example");

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