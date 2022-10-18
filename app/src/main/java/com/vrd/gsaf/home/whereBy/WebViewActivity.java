package com.vrd.gsaf.home.whereBy;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.vrd.gsaf.R;
import com.vrd.gsaf.singleton.Singleton;

import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1234;
    private final String[] requiredDangerousPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO
    };
    public String roomUrlString = null; // Replace by your own
    ProgressDialog progressBar;
    ConstraintLayout mainLayout;
    private String roomParameters = "?skipMediaPermissionPrompt";
    private ImageView backImageView;
    private TextView titleTxt;
    private WebView webView;
    int SECONDS=10*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        titleTxt = findViewById(R.id.titleTxt);
        backImageView = findViewById(R.id.backImageView);
        this.webView = findViewById(R.id.webView);
        mainLayout = findViewById(R.id.mainLayout);
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        new Handler().postDelayed(() -> {
            if (progressBar!=null&& progressBar.isShowing()){
                progressBar.dismiss();
            }
        },SECONDS);
        if (getIntent().hasExtra("isCv")) {
            roomUrlString = getIntent().getExtras().getString("url");
//            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            progressBar = ProgressDialog.show(this, "", "Loading...");

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public Bitmap getDefaultVideoPoster() {
                    return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                }
            });
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    Log.i(TAG, "Processing webview url click...");
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
//                    Log.i(TAG, "Finished loading URL: " + url);
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                    Log.e(TAG, "Error: " + description);
                    Toast.makeText(WebViewActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage(description);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    alertDialog.show();
                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + roomUrlString);

                }
            }, 1000);
            titleTxt.setText("");

        } else {
            roomUrlString = getIntent().getExtras().getString("url");
            WebViewUtils.configureWebView(this.webView);
            this.webView.setWebChromeClient(new CustomWebChromeClient(this));
            this.webView.setWebViewClient(new WebViewClient());

            titleTxt = findViewById(R.id.titleTxt);
            try {
                titleTxt.setText(Singleton.getInstance().getWebinarDetail().getWebinarTitle());

            } catch (Exception e) {
                titleTxt.setText("Title");
            }

        }
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.webView.getUrl() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.isPendingPermissions()) {
                // This explicitly requests the camera and audio permissions.
                // It's fine for a demo app but should probably be called earlier in the flow,
                // on a user interaction instead of onResume.
                this.requestCameraAndAudioPermissions();
            } else {
                this.loadEmbeddedRoomUrl();
            }
        }
    }

    private void loadEmbeddedRoomUrl() {
        if (roomUrlString != null) {
            if (roomUrlString.contains("?")) {
                roomParameters = "&skipMediaPermissionPrompt";
            }
            this.webView.loadUrl(roomUrlString + roomParameters);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (this.grantResultsContainsDenials(grantResults)) {
                    // Show some permissions required dialog.
                } else {
                    // All necessary permissions granted, continue loading.
                    this.loadEmbeddedRoomUrl();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraAndAudioPermissions() {
        this.requestPermissions(this.getPendingPermissions(), PERMISSION_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private String[] getPendingPermissions() {
        List<String> pendingPermissions = new ArrayList<>();
        for (String permission : this.requiredDangerousPermissions) {
            if (this.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                pendingPermissions.add(permission);
            }
        }
        return pendingPermissions.toArray(new String[pendingPermissions.size()]);
    }

    private boolean isPendingPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        return this.getPendingPermissions().length > 0;
    }

    private boolean grantResultsContainsDenials(int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        if (webView!=null){
            webView.destroy();
//            destroyWebView();
        }
        super.onStop();
    }

    public void destroyWebView() {
        // Make sure you remove the WebView from its parent view before doing anything.
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
//        webView.pauseTimers();

        // NOTE: This can occasionally cause a segfault below API 17 (4.2)
        webView.destroy();

        // Null out the reference so that you don't end up re-using it.
        webView = null;


    }


    public class AppWebViewClients extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }
    }
}