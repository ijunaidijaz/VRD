package com.vrd.gsaf.home.dashboard.uploadTask;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.vrd.gsaf.R;
import com.vrd.gsaf.activity.PDFViewerActivity;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.login.Data;
import com.vrd.gsaf.api.responses.login.Login;
import com.vrd.gsaf.api.responses.uploads.UploadedFileUrlResponse;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.whereBy.WebViewActivity;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadTask extends Fragment implements View.OnClickListener {


    private static final long MIN_CLICK_INTERVAL = 600;
    //  private PDFView pdfView;
    private static final int PICKFILE_RESULT_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static boolean isViewClicked = false;
    private static ConstraintLayout mainLayout;
    private static ConstraintLayout pdfLayout;
    String url;
    RelativeLayout toolbar;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView;
    private WebView webView;
    private long mLastClickTime;
    private TextView titleTxt;
    private Button uploadBtn, viewBtn, downloadBtn;
    private String fileName;
    private File file;
    private ProgressDialog mProgressDialog;

    public static void onBackPressTasks() {

        // FragmentManager fm = getParentFragmentManager();
        if (Singleton.getInstance().isPdfTasks()) {
            Singleton.getInstance().setPdfTasks(false);
            pdfLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        } else
            Singleton.getInstance().getActivity().onBackPressed();
        // fm.popBackStack("speakers", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_task, container, false);
        Helper.hideKeyboard();
        initializeViews();
        getFileUrl();
        return view;
    }

    @SuppressLint({"CutPasteId", "SetTextI18n"})
    private void initializeViews() {
        //  pdfView = view.findViewById(R.id.idPDFView);
        progressBar = view.findViewById(R.id.progressBar);
        backImageView = view.findViewById(R.id.backImageView);
        pdfLayout = view.findViewById(R.id.pdfLayout);
        mainLayout = view.findViewById(R.id.mainLayout);

        webView = view.findViewById(R.id.webView);
        titleTxt = view.findViewById(R.id.titleTxt);
        downloadBtn = view.findViewById(R.id.downloadBtn);
        viewBtn = view.findViewById(R.id.viewBtn);
        uploadBtn = view.findViewById(R.id.uploadBtn);

        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getUpload() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTask());
        downloadBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDownload() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDocuments());
        viewBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getView() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDocuments());

        if (Singleton.getInstance().getLoginData().getUser().getUserTaskList() == null || Singleton.getInstance().getLoginData().getUser().getUserTaskList() == "") {
            downloadBtn.setVisibility(View.GONE);
            viewBtn.setVisibility(View.GONE);
        }

        uploadBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getUpload() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDocuments());


        Helper.setButtonColorWithDrawable(uploadBtn,
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
        Helper.setButtonColorWithDrawable(viewBtn,
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
        Helper.setButtonColorWithDrawable(downloadBtn,
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());

        setClickListeners();
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {

        backImageView.setOnClickListener(this);
        downloadBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
        viewBtn.setOnClickListener(this);
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

    private void getFileUrl() {
        Helper.showProgressBar(progressBar);
        String url = "api/candidate/download/tasklist/" + Singleton.getInstance().getLoginData().getUser().getId();
        Call<UploadedFileUrlResponse> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getFileUrl(url
                , "Bearer " + Singleton.getInstance().getLoginData().getAccessToken()
                , Singleton.getInstance().getLanguage(),
                "application/json", AppConstants.APP_ID,
                AppConstants.APP_KEY);
        call.enqueue(new Callback<UploadedFileUrlResponse>() {
            @Override
            public void onResponse(Call<UploadedFileUrlResponse> call, Response<UploadedFileUrlResponse> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {
                        Helper.showToast(Singleton.getKeywords().getTrans_string_oops_no_data_found());
                        return;
                    }
                    if (response.body().getStatus()) {
                        UploadTask.this.url = response.body().getData().getFileUrl();
                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
                    Helper.showToast("Something went wrong...Please try later!");
                }
            }

            @Override
            public void onFailure(Call<UploadedFileUrlResponse> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something went wrong...Please try later!");
                // Toast.makeText(Singleton.getInstance().getContext(), "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
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
                onBackPressTasks();
                break;
            case R.id.downloadBtn:
                if (Singleton.getInstance().getLoginData().getUser().getUserTaskList() != "")
                    dowloadCv();
                break;
            case R.id.uploadBtn:
                uploadCV();
                break;
            case R.id.viewBtn:
                if (url != null) {
                    if (Singleton.getInstance().getLoginData().getUser().getUserTaskList() != null && Singleton.getInstance().getLoginData().getUser().getUserTaskList() != "") {
                        if (url.contains(".pdf")){
                            MainApp.getAppContext().startActivity(new Intent(MainApp.getAppContext(), PDFViewerActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("url", url).putExtra("isCv", true));
                        }else
                        MainApp.getAppContext().startActivity(new Intent(MainApp.getAppContext(), WebViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("url", url).putExtra("isCv", true));
                    }
//                    viewCv();
                } else
                    Helper.showToast(Singleton.getKeywords().getTrans_string_oops_no_data_found());
                break;
        }
    }

    private void viewCv() {
        Helper.viewDocument(url, false);
    }

    private void uploadCV() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                filePicker();
            } else {
                requestPermission();
            }
        } else {
            filePicker();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Singleton.getInstance().getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        result = ContextCompat.checkSelfPermission(Singleton.getInstance().getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Singleton.getInstance().getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void filePicker() {
        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
//                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
//                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "application/pdf"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), PICKFILE_RESULT_CODE);

    }

    private void dowloadCv() {

        Helper.viewDocument(url, true);
//        Helper.showProgressBar(progressBar);
//
//        final DownloadFile downloadTask = new DownloadFile(Singleton.getInstance().getContext(), progressBar, Singleton.getInstance().getLoginData().getUser().getUserTaskList());
//        downloadTask.execute(Singleton.getInstance().getFairData().getSystemUrl().getResumeUrl() +
//                Singleton.getInstance().getFairData().getSystemUrl().getResumeRoute() +
//                Singleton.getInstance().getLoginData().getUser().getUserTaskList());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == resultCode) {
                    if (data != null) {
                        String filePath = Helper.getDriveFilePath(data.getData(), Singleton.getInstance().getActivity());
                        String[] parsedpath = filePath.split("/");
                        fileName = parsedpath[parsedpath.length - 1];
                        file = new File(filePath);
                        callApi();
                    }
                }
                break;
        }
    }

    private void callApi() {
        Helper.showProgressBar(progressBar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    new RetrieveFeedTask().execute();
                } catch (Exception e) {
                    Helper.hideProgressBar(progressBar);
                }
            }
        }, 3000);

    }

    private void resumeUpdate(Login feed) {
        try {
            Helper.showToast(feed.getMsg());
            if (feed != null) {
                updateSp(feed.getData());
            }
        } catch (Exception e) {
            Helper.showToast("Something Went Wrong");
        }

        getFileUrl();
        Helper.hideProgressBar(progressBar);

    }

    private void updateSp(Data response) {
        response.setAccessToken(Singleton.getInstance().getLoginData().getAccessToken());
        Singleton.getInstance().setLoginData(response);
        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
        editor.putBoolean("login", true);
        Gson gson = new Gson();
        String json = gson.toJson(Singleton.getInstance().getLoginData());
        editor.putString("loginData", json);
        editor.apply();
        downloadBtn.setVisibility(View.VISIBLE);
        viewBtn.setVisibility(View.VISIBLE);
        url = Singleton.getInstance().getLoginData().getUser().getUserTaskList();
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            //  pdfView.fromStream(inputStream).load();
        }
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Login> {

        private Exception exception;


        protected com.vrd.gsaf.api.responses.login.Login doInBackground(String... urls) {

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("candidate_id", Singleton.getInstance().getLoginData().getUser().getId())
                    .addFormDataPart("tasklist", fileName,
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    new File(file.getPath())))
                    .build();


            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            Request request = new Request.Builder()
                    .url(AppConstants.BASE_URL + "api/save/candidate/tasklist")
                    .post(body)
                    .addHeader("app-id", AppConstants.APP_ID)
                    .addHeader("requested-lang", Singleton.getInstance().getLanguage())
                    .addHeader("app-key", AppConstants.APP_KEY)
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken())
                    .build();


            okhttp3.Response response = null;

            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.isSuccessful()) {

                com.vrd.gsaf.api.responses.login.Login responseResult = null;
                Gson gson = new Gson();
                com.vrd.gsaf.api.responses.login.Login login = null;
                // Do something here
                try {

                    login = gson.fromJson(response.body().string(), Login.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return login;

            }

            return null;
        }


        protected void onPostExecute(com.vrd.gsaf.api.responses.login.Login feed) {
            resumeUpdate(feed);
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

}