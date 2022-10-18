package com.vrd.gsaf.registration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.activity.MainActivity;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.BaseResponse;
import com.vrd.gsaf.api.responses.PrivacyAndConditionsReponse;
import com.vrd.gsaf.api.responses.fairDetail.Data;
import com.vrd.gsaf.api.responses.fairExtraFields.FairExtraFieldsResponse;
import com.vrd.gsaf.api.responses.fairExtraFields.FairOption;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.dashboard.careeTest.CareerTest;
import com.vrd.gsaf.model.SelectedOptions;
import com.vrd.gsaf.model.ViewModel;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnTouchListener {

    private static final int PICKFILE_RESULT_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_GALLERY = 200;
    private static final long MIN_CLICK_INTERVAL = 600;
    private static final long CLICK_TIME_INTERVAL = 500;
    public static boolean isViewClicked = false;
    static Registration instance;
    private static Uri fileUri;
    private final ArrayList<ViewModel> dynamicViews = new ArrayList<>();
    private final ArrayList<SelectedOptions> selectedOptions = new ArrayList<>();
    private final JsonObject JsonObject = new JsonObject();
    private final JsonObject extraFields = new JsonObject();
    private final int languageIndex = Singleton.getInstance().getLanguageIndex();
    protected float dX;
    protected float dY;
    protected int lastAction;
    String company = "";
    String phone = "";
    String job = "";
    long lastDown;
    long lastDuration;
    ConstraintLayout termsLayout;
    String token;
    private View view;
    private Button registerBtn;
    private ProgressBar progressBar;
    private EditText nameEdt, emailEdt, passwordEdt, numberEdt, companyNameEdt, jobTitleEdt;
    private TextView titleTxt, txt, registerOptionstxt, companyName, jobTitle, numberTxt, loginTxt, registerTxt, nameTxt, emailTxt, passwordTxt, cvTxtView, textView1, termsAndConditionsLinkTxt, privacyLinkTxt;
    private ConstraintLayout nameLayout, emailLayout, passwordLayout, numberLayout, companyNameLayout, jobTitleLayout, loginLayout, registerWithOtherOptionsLayout, uploadCvLayout, cvUploadedLayout;
    private ImageView backImageView, passwordVisibleImageView, draggable_view, backgroundImage;
    private Data fairData;
    private CheckBox termsAndConditionsCheckBox;
    private File file;
    private ScrollView scrollView;
    private String fileName;
    private long mLastClickTime;
    private View cvLayout;
    private ProgressDialog progressDialog;

    public static Registration getInstance() {
        if (instance == null) {
            instance = new Registration();
        }
        return instance;
    }

    private static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    @SuppressLint("Range")
    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static File makeEmptyFileIntoExternalStorageWithTitle(String title) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        return new File(root, title);
    }

    public static boolean isGoogleDrive(Uri uri) {
        return "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }

    public static void onBackPressRegister() {

        // FragmentManager fm = getParentFragmentManager();
        if (Singleton.getInstance().isRegistration()) {
            Singleton.getInstance().setRegistration(false);
            Registration.getInstance().onBackPress();
            //  pdfLayout.setVisibility(View.GONE);
        } else
            Singleton.getInstance().getActivity().onBackPressed();
        // fm.popBackStack("speakers", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_registration, container, false);
        instance = this;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.hideKeyboard();
            }
        });
        Gson gson = new Gson();
        String json = Singleton.getInstance().getSharedPreferences().getString("fairDetails", null);

        if (json != null) {
            Singleton.getInstance().setFairData(gson.fromJson(json, Data.class));
        }

        initializeViews();
        if (Singleton.getInstance().getBottomNavigationView() != null)
            Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        token = task.getResult();
                    } else Helper.showToast("Firebase token error");
                }

        );
        if (Helper.isInternetConnected()) {
            JsonObject JsonObject = new JsonObject();
            JsonObject.addProperty("fair_id", Singleton.getInstance().getSharedPreferences().getInt("fairId", 0));
            getFairExtraFieldsApi(JsonObject);
        }
        return view;

    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        try {
            scrollView = view.findViewById(R.id.scrollView);
            progressBar = view.findViewById(R.id.progressBar);
            registerBtn = view.findViewById(R.id.registerBtn);
            txt = view.findViewById(R.id.txt);
            registerOptionstxt = view.findViewById(R.id.registerOptionstxt);
            nameEdt = view.findViewById(R.id.nameEdt);
            nameTxt = view.findViewById(R.id.nameTxt);
            loginTxt = view.findViewById(R.id.loginTxt);
            registerTxt = view.findViewById(R.id.registerTxt);
            backgroundImage = view.findViewById(R.id.background_image_view);
            emailEdt = view.findViewById(R.id.emailEdt);
            emailTxt = view.findViewById(R.id.emailTextView);
            titleTxt = view.findViewById(R.id.titleTxt);
            passwordEdt = view.findViewById(R.id.passwordEdt);
            passwordTxt = view.findViewById(R.id.passwordTxt);
            loginLayout = view.findViewById(R.id.loginLayout);
            registerWithOtherOptionsLayout = view.findViewById(R.id.registerWithOtherOptionsLayout);
            backImageView = view.findViewById(R.id.backImageView);
            draggable_view = view.findViewById(R.id.draggable_view);
            passwordVisibleImageView = view.findViewById(R.id.passwordVisibleImageView);
            ImageView appLogo = view.findViewById(R.id.logo);
            Glide.with(Singleton.getInstance().getContext()).
                    load(AppConstants.APP_LOGO)
                    .placeholder(R.drawable.rectangluar_placeholder)
                    .into(appLogo);
            ViewModel model = new ViewModel(1, "text", "Y", nameTxt);
            model.setEditText(nameEdt);
            dynamicViews.add(model);
            ViewModel model1 = new ViewModel(1, "text", "Y", emailTxt);
            model1.setEditText(emailEdt);
            dynamicViews.add(model1);
            ViewModel model2 = new ViewModel(1, "text", "Y", passwordTxt);
            model2.setEditText(passwordEdt);
            dynamicViews.add(model2);
            updateView();
            //registerBtnColor();
            Helper.setButtonColorWithDrawable(registerBtn, Singleton.getOptions().getTopnavBackgroundColor(), Singleton.getOptions().getTopnavInnerTextColor());

            if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableLoginFront() != 0) {
                loginLayout.setVisibility(View.GONE);
            }
            Helper.loadImageWithGlideNoPlaceHolder(backgroundImage, AppConstants.getBackgroundImage());
        } catch (Exception e) {
            Log.d("Error", "initializeViews registration");

        }
    }

    private void setKeyWords() {
        registerBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getRegister());
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getRegister());
        nameTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getName());
        emailTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getEmail());
        passwordTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getPassword());
        loginTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getLogin());
        registerTxt.setText(" with other options");
        txt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getAlreadyHaveAnAccount());
        registerOptionstxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getRegister());
        Helper.showProgressBar(progressBar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Helper.hideProgressBar(progressBar);
            }
        }, 2000);

    }

    private void setClickListeners() {
        registerBtn.setOnClickListener(this);
        loginLayout.setOnClickListener(this);
        registerWithOtherOptionsLayout.setOnClickListener(this);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getEnablePublicSsoAttendeeSide() != null) {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getEnablePublicSsoAttendeeSide().equals(1)) {
                registerWithOtherOptionsLayout.setVisibility(View.VISIBLE);
                registerWithOtherOptionsLayout.setOnClickListener(this);
            }
        }
        backImageView.setOnClickListener(this);
        passwordVisibleImageView.setOnClickListener(this);
        draggable_view.setOnTouchListener(this);

    }

    private void updateView() {
        try {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getFieldPhoneNumber().equals(0)) {
                numberLayout = view.findViewById(R.id.numberLayout);
                numberTxt = view.findViewById(R.id.numberTxt);

                numberTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getPhoneNumber());

                numberLayout.setVisibility(View.VISIBLE);
                numberEdt = view.findViewById(R.id.numberEdt);
                ViewModel model = new ViewModel(1, "text", "Y", numberTxt);
                model.setEditText(numberEdt);
                dynamicViews.add(model);

            }
            if (Singleton.getInstance().getFairData().getFair().getOptions().getFieldCompanyName().equals(0)) {
                companyNameLayout = view.findViewById(R.id.companyNameLayout);
                companyNameLayout.setVisibility(View.VISIBLE);
                companyName = view.findViewById(R.id.companyNameView);
                companyName.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getCompanyName());

                companyNameEdt = view.findViewById(R.id.companyNameEdt);
                ViewModel model = new ViewModel(1, "text", "Y", companyName);
                model.setEditText(companyNameEdt);
                dynamicViews.add(model);

            }

            if (Singleton.getInstance().getFairData().getFair().getOptions().getFieldJobTitle().equals(0)) {
                jobTitleLayout = view.findViewById(R.id.jobTitleLayout);
                jobTitleEdt = view.findViewById(R.id.jobTitleEdt);
                jobTitle = view.findViewById(R.id.jobTitleTxt);
                jobTitle.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getJobTitle());
                jobTitleLayout.setVisibility(View.VISIBLE);
                ViewModel model = new ViewModel(1, "text", "Y", jobTitle);
                model.setEditText(jobTitleEdt);
                dynamicViews.add(model);

            }

            setClickListeners();
            setKeyWords();
//            setTermAndConditionView();
            // setPrivacyPolicyView();
        } catch (Exception e) {
            Helper.showToast("exception");
            Log.d("Error", "updateView registration");
        }

    }

    private void getFairExtraFieldsApi(JsonObject JsonObject) {
        Helper.showProgressBar(progressBar);
        Call<FairExtraFieldsResponse> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getFairExtraFields(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<FairExtraFieldsResponse>() {
            @Override
            public void onResponse(Call<FairExtraFieldsResponse> call, Response<FairExtraFieldsResponse> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.code() == 204) {
                        parseExtraFieldsResponse(response);
                    } else if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.body().getStatus()) {
                        parseExtraFieldsResponse(response);

                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
                    Helper.showToast("Something went wrong...Please try later!");
                }
            }

            @Override
            public void onFailure(Call<FairExtraFieldsResponse> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something went wrong...Please try later!");
                // Toast.makeText(Singleton.getInstance().getContext(), "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void parseExtraFieldsResponse(Response<FairExtraFieldsResponse> response) {
        if (response.body() != null) {
            //  response.body().getData().getFair().setStartTime("2022-02-24 15:03:00");
            Singleton.getInstance().setExtraFieldsFairData(response.body().getFairData());
            SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
            Gson gson = new Gson();
            String json = gson.toJson(response.body().getFairData());
            editor.putString("fairFeildsData", json);

            editor.apply();
            if (Singleton.getInstance().getExtraFieldsFairData() != null) {
                for (int i = 0; i < Singleton.getInstance().getExtraFieldsFairData().getExtraFields().size(); i++) {
                    if (Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getFieldType().equals("dropdown") && Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getHideField().equals("N"))
                        dropDownView(i, Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getAutoId(),
                                Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getFieldTitle(),
                                Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getOptions(),
                                Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getIsRequired());
                    else if (Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getFieldType().equals("text") && Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getHideField().equals("N"))
                        //  setTextView(1, "Name","text");
                        setTextView(Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getAutoId(), Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getFieldTitle(), "text", Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getIsRequired());


                }
            }

        }
        if (Singleton.getInstance().getFairData().getFair().getFairSetting().getCvRequired().equals("1") || Singleton.getInstance().getFairData().getFair().getFairSetting().getCvRequired().equals("2")) {
            uploadCvView();
        }
        setTermAndConditionView();

    }

    private void setPrivacyPolicyView() {
        LinearLayout ll = view.findViewById(R.id.parentLinearLayout);
        View termAndConditions = LayoutInflater.from(view.getContext()).inflate(R.layout.term_conditiond, ll, false);
        ll.addView(termAndConditions);
        //  privacyCheckBox = termAndConditions.findViewById(R.id.checkBox);
        TextView agreeTxt = termAndConditions.findViewById(R.id.agreeTxt);
        privacyLinkTxt = termAndConditions.findViewById(R.id.termsAndConditionsLinkTxt);
        privacyLinkTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getPrivacyPolicyKey());
        agreeTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getiAgreeWith());

        privacyLinkTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openTermAndConditionsDialog("privacyPolicy");
                getPrivacyAndConditions("privacy");
            }
        });
//        privacyCheckBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                long now = System.currentTimeMillis();
//                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
//                    return;
//                }
//                mLastClickTime = now;
//                if (privacyCheckBox.isChecked()) {
//                    privacyCheckBox.setChecked(true);
//                    privacyLinkTxt.setTextColor(getResources().getColor(R.color.white));
//                } else
//                    privacyCheckBox.setChecked(false);
//            }
//        });
    }

    private void dropDownView(int j, String id, String title, List<FairOption> options, String isRequired) {

        ArrayList<String> eventsList = new ArrayList<>();
        for (int i = 0; i < options.size(); i++)
            eventsList.add(options.get(i).getOptionText());
        LinearLayout ll = view.findViewById(R.id.parentLinearLayout);
        View dropDown = LayoutInflater.from(view.getContext()).inflate(R.layout.drop_down, ll, false);
        TextView headingTxt = dropDown.findViewById(R.id.headingTxt);
        headingTxt.setText(title);
        TextView textView = dropDown.findViewById(R.id.dropDownClick);
        TextView requiredStar = dropDown.findViewById(R.id.requiredStar);
        if (isRequired.equals("N")) {
            TextView textView2 = dropDown.findViewById(R.id.requiredStar);
            textView2.setVisibility(View.GONE);
        }
        ViewModel model = new ViewModel(j, "dropDown", isRequired, headingTxt);
        dynamicViews.add(model);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDropDown(eventsList, model);
                textView1 = textView;
            }
        });
        model.setAutoId(id);
        ll.addView(dropDown);


        //  loadSpinnerValues();
    }

    private void openDropDown(ArrayList<String> eventsList, ViewModel model) {
        Dialog dialog = new Dialog(Singleton.getInstance().getContext());
        dialog.setContentView(R.layout.view_drop_down_list);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();

        ListView listView = dialog.findViewById(R.id.listView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Singleton.getInstance().getActivity(),
                R.layout.view_events_spinner, eventsList);

        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textView1.setText(arrayAdapter.getItem(i));
                model.setValue(arrayAdapter.getItem(i));
                dialog.dismiss();
                for (int k = 0; k < selectedOptions.size(); k++) {
                    if (selectedOptions.get(k).getType().equals("dropDown") && selectedOptions.get(k).getAutoId() != null) {
                        if (selectedOptions.get(k).getAutoId().equals(model.getAutoId()))
                            selectedOptions.remove(k);
                    }
                }
                SelectedOptions selectedOptions1 = new SelectedOptions(model.getAutoId() + "," + i, "dropDown");
                selectedOptions1.setAutoId(model.getAutoId());
                selectedOptions.add(selectedOptions1);

            }

        });
    }

    private void uploadCvView() {
        LinearLayout ll = view.findViewById(R.id.parentLinearLayout);
        cvLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.upload_cv, ll, false);
        ll.addView(cvLayout);
        uploadCvLayout = view.findViewById(R.id.uploadCvLayout);
        uploadCvLayout.setVisibility(View.VISIBLE);
        cvUploadedLayout = view.findViewById(R.id.cvUploaded);
        cvUploadedLayout.setVisibility(View.GONE);

        TextView fileDescriptionTxt = view.findViewById(R.id.fileDescriptionTxt);
        uploadCvLayout.setOnClickListener(this);
        cvTxtView = cvLayout.findViewById(R.id.uploadCvTxt);
        //  fileDescriptionTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getUpload() + " " + Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getTransOnlyUploadCvFormat());
        fileDescriptionTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getTransOnlyUploadCvFormat());
        if (Singleton.getInstance().getFairData().getFair().getFairSetting().getCvRequired().equals("1"))
            cvTxtView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getUpload() + " " + Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getCv() + " *");
        else
            cvTxtView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getUpload() + " " + Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getCv());


    }

    private void setTermAndConditionView() {
        LinearLayout ll = view.findViewById(R.id.parentLinearLayout);
        View termAndConditions = LayoutInflater.from(view.getContext()).inflate(R.layout.term_conditiond, ll, false);
        termsLayout = termAndConditions.findViewById(R.id.termAndConditionsLayout);
//        ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) constraintLayout.getLayoutParams();
//        newLayoutParams.topMargin = 28;
//        newLayoutParams.leftMargin = 30;
//        newLayoutParams.rightMargin = 30;
//        constraintLayout.setLayoutParams(newLayoutParams);
        ll.addView(termAndConditions);
        termsAndConditionsCheckBox = termAndConditions.findViewById(R.id.checkBox);
        TextView agreeTxt = termAndConditions.findViewById(R.id.agreeTxt);
        //  TextView andTxt = termAndConditions.findViewById(R.id.andTxt);
        privacyLinkTxt = termAndConditions.findViewById(R.id.privacyLinkTxt);
        termsAndConditionsLinkTxt = termAndConditions.findViewById(R.id.termsAndConditionsLinkTxt);
        termsAndConditionsLinkTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getTermsConditionsKey() + " & ");
        agreeTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getiAgreeWith());
        //  andTxt.setText(" & ");
        privacyLinkTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getPrivacyPolicyKey());

        termsAndConditionsLinkTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrivacyAndConditions("terms");
//                openTermAndConditionsDialog("termsAndConditions");
            }
        });
        privacyLinkTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrivacyAndConditions("privacy");
//                openTermAndConditionsDialog("privacyPolicy");
            }
        });
        termsAndConditionsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (termsAndConditionsCheckBox.isChecked()) {
                    termsAndConditionsCheckBox.setChecked(true);
                    termsAndConditionsLinkTxt.setTextColor(getResources().getColor(R.color.white));
                    privacyLinkTxt.setTextColor(getResources().getColor(R.color.white));
                } else
                    termsAndConditionsCheckBox.setChecked(false);
            }
        });
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_term_conditions_registration_front().equals(1)) {
            termsLayout.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_privacy_policy_registration_front().equals(1)) {
            termsLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ResourceType")
    private void setTextView(String id, String title, String input, String isRequired) {

        LinearLayout ll = view.findViewById(R.id.parentLinearLayout);
        View layout2 = LayoutInflater.from(view.getContext()).inflate(R.layout.text_view, ll, false);
        // layout2.setId(id);

        TextView textView = layout2.findViewById(R.id.nameTxt);
        textView.setText(title);
        if (isRequired.equals("N")) {
            TextView textView2 = layout2.findViewById(R.id.requiredStar);
            textView2.setVisibility(View.GONE);
        }
        EditText editText = layout2.findViewById(R.id.nameEdt);
        editText.setSingleLine(true);
        if (input.equals("number"))
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        ll.addView(layout2);
        ViewModel model = new ViewModel(23, "text", isRequired, textView);
        model.setEditText(editText);
        model.setAutoId(id);
        model.setExtra(true);
        dynamicViews.add(model);

    }

    private void replaceFragment(Fragment fragment) {

        Bundle args = new Bundle();
        args.putString("from", "registeration");
        fragment.setArguments(args);
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("register")
                .commit();
    }

    @SuppressLint("ResourceType")
    private void visibilityCheck() {
        if (passwordEdt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            passwordVisibleImageView.setImageResource(R.drawable.visibility);

            //Show Password
            passwordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        } else {
            passwordVisibleImageView.setImageResource(R.drawable.no_visibility);
            // passwordEdt.setSelection(passwordEdt.getText().length());

            //Hide Password
            passwordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        passwordEdt.setSelection(passwordEdt.getText().length());

    }

    private void termsAndConditions() {
        termsAndConditionsCheckBox.setChecked(!termsAndConditionsCheckBox.isChecked());

    }

    private void changeToDefaultColor(EditText editText) {
        nameEdt.setTextColor(getResources().getColor(R.color.white));
    }

    private boolean getValues() {
        for (int i = 0; i < selectedOptions.size(); i++) {
            if (selectedOptions.get(i).getType().equals("text")) {
                selectedOptions.remove(i);
                i--;
            }
        }

        for (int i = 0; i < dynamicViews.size(); i++) {
            if (nameEdt.getText().toString().isEmpty()) {
                nameEdt.setTextColor(getResources().getColor(R.color.required_fileds));
                nameEdt.requestFocus();
                focusOnView(nameEdt);
                Helper.showToast(Singleton.getKeywords().getTransPleaseEnterName());
//                Toast.makeText(Singleton.getInstance().getContext(), "Name can only contains alphabets and space", Toast.LENGTH_LONG).show();
                return false;
            } else changeToDefaultColor(nameEdt);

            if (!checkRequired(dynamicViews.get(i), i)) {
                return false;
            } else {
                if (i == (dynamicViews.size() - 1)) {
//                    if (!passwordEdt.getText().toString().matches(AppConstants.PASSWORD_REGEX)) {
                    if (passwordEdt.getText().toString().isEmpty()) {
                        passwordEdt.setTextColor(getResources().getColor(R.color.required_fileds));
                        passwordEdt.requestFocus();
                        focusOnView(passwordEdt);
                        Toast.makeText(Singleton.getInstance().getContext(), Singleton.getKeywords().getTransPleaseEnterPassword(), Toast.LENGTH_LONG).show();
                        return false;
                    } else changeToDefaultColor(passwordEdt);
                    if (!(passwordEdt.getText().toString().length() >= 8)) {
                        passwordEdt.setTextColor(getResources().getColor(R.color.required_fileds));
                        passwordEdt.requestFocus();
                        focusOnView(passwordEdt);
                        Toast.makeText(Singleton.getInstance().getContext(), "Password must contains 8 characters", Toast.LENGTH_LONG).show();
                        return false;
                    } else changeToDefaultColor(passwordEdt);
                    return true;
                }
                continue;
            }
        }

        return false;
    }

    private boolean checkRequired(ViewModel viewModel, int i) {
        if (emailEdt.getText().toString().isEmpty()) {
            emailEdt.requestFocus();
            emailEdt.setTextColor(getResources().getColor(R.color.required_fileds));
            focusOnView(emailEdt);
            Helper.showToast(Singleton.getKeywords().getTransPleaseEnterEmail());
            return false;
        } else changeToDefaultColor(emailEdt);
        if (!Helper.isEmailValid(emailEdt.getText().toString())) {
            emailEdt.requestFocus();
            emailEdt.setTextColor(getResources().getColor(R.color.required_fileds));
            focusOnView(emailEdt);
            Helper.showToast(Singleton.getKeywords().getTransPleaseEnterValidEmail());
            return false;
        } else changeToDefaultColor(emailEdt);
        if (Helper.isEmailValid(emailEdt.getText().toString())) {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getEmailDomain() != null && !Singleton.getInstance().getFairData().getFair().getOptions().getEmailDomain().isEmpty()) {
                if (Singleton.getInstance().getFairData().getFair().getOptions().getEmailDomainOperator().equalsIgnoreCase("0")) {
                    if (!Singleton.getInstance().getFairData().getFair().getOptions().getEmailDomain().equalsIgnoreCase(getEmailDomain(emailEdt.getText().toString()))) {
                        emailEdt.requestFocus();
                        emailEdt.setTextColor(getResources().getColor(R.color.required_fileds));
                        focusOnView(emailEdt);
                        Helper.showToast(Singleton.getKeywords().getEmail_domain_not_allowed());
                        return false;
                    } else changeToDefaultColor(emailEdt);
                } else if (Singleton.getInstance().getFairData().getFair().getOptions().getEmailDomainOperator().equalsIgnoreCase("1")) {
                    if (Singleton.getInstance().getFairData().getFair().getOptions().getEmailDomain().equalsIgnoreCase(getEmailDomain(emailEdt.getText().toString()))) {
                        emailEdt.requestFocus();
                        emailEdt.setTextColor(getResources().getColor(R.color.required_fileds));
                        focusOnView(emailEdt);
                        Helper.showToast(Objects.requireNonNull(Singleton.getKeywords()).getEmail_domain_not_allowed());
                        return false;
                    } else changeToDefaultColor(emailEdt);
                }
            }
        }
        if (passwordEdt.getText().toString().isEmpty()) {
            passwordEdt.requestFocus();
            passwordEdt.setTextColor(getResources().getColor(R.color.required_fileds));
            focusOnView(passwordEdt);
            Helper.showToast(Singleton.getKeywords().getTransPleaseEnterPassword());
            return false;
        } else changeToDefaultColor(passwordEdt);
        if (viewModel.getRequired().equals("Y")) {
            if (viewModel.getType().equals("text")) {
                if (viewModel.getEditText().getText().toString().equals("")) {
                    viewModel.getTitle().setTextColor(getResources().getColor(R.color.required_fileds));
                    viewModel.getEditText().requestFocus();
                    focusOnView(viewModel.getEditText());
                    Helper.showToast(viewModel.getTitle().getText().toString() + " is empty");
                    return false;
                } else {
                    viewModel.getTitle().setTextColor(getResources().getColor(R.color.white));
                }

            } else if (viewModel.getType().equals("dropDown")) {

                if (viewModel.getValue() == null || viewModel.getValue().equals("")) {
                    viewModel.getTitle().requestFocus();
                    viewModel.getTitle().setTextColor(getResources().getColor(R.color.required_fileds));
                    Helper.showToast(viewModel.getTitle().getText().toString() + " is empty");
                    // viewModel.getEditText().requestFocus();
                    return false;
                } else
                    viewModel.getTitle().setTextColor(getResources().getColor(R.color.white));

            }
        }
        if (viewModel.getAutoId() != null && viewModel.getExtra()) {
            if (viewModel.getType().equals("text"))
                if (!selectedOptions.contains(viewModel.getAutoId() + "," + viewModel.getEditText().getText())) {
                    SelectedOptions selectedOptions1 = new SelectedOptions(viewModel.getAutoId() + "," + viewModel.getEditText().getText(), "text");
                    selectedOptions.add(selectedOptions1);
                }
        }

        return true;
    }

    public String getEmailDomain(String someEmail) {
        return "@" + someEmail.substring(someEmail.indexOf("@") + 1);

    }

    //URL = "https://docs.google.com/uc?id=" + IDFile + "&export=download";

    private void uploadCv() {

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

    private void requestPermission() {
        ActivityCompat.requestPermissions(Singleton.getInstance().getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Singleton.getInstance().getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        result = ContextCompat.checkSelfPermission(Singleton.getInstance().getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void filePicker() {


//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        // Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//
//        intent.setType("application/pdf");

        //  intent.setType("file/*");

        //  startActivityForResult(intent, PICKFILE_RESULT_CODE);

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        //  intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, PICKFILE_RESULT_CODE);


    }

    public void onBackPress() {
        Helper.showToast("regist back");

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Events())
                .setReorderingAllowed(true)
                .addToBackStack("events")
                .commit();
        MainActivity.getInstance().clearAllFragmentStack();
    }

    private void loadHome() {
        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
        editor.putBoolean("careerTest", true);
        editor.putInt("languageIndex", Singleton.getInstance().getLanguageIndex());
        editor.commit();
        editor.apply();
        Intent intent = new Intent(Singleton.getInstance().getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
//        FragmentManager fm = getParentFragmentManager();
//        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//            fm.popBackStack();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    filePicker();
                } else {
                    Helper.showToast("Permission denies");
                    //  Toast.makeText(Singleton.getInstance().getActivity(), "Permission denies", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private String getItemId(String filedId, String type) {
        String id = null;

        if (type.equals("text")) {
            for (int i = 0; i < selectedOptions.size(); i++) {
                String[] array = selectedOptions.get(i).getValue().split(",");
                if (filedId.equals(array[0]) && array.length > 1) {
                    return array[1];
                } else if (filedId.equals(array[0]) && array.length == 1) {
                    return "";
                }
            }

        } else if (type.equals("dropDown")) {
            for (int i = 0; i < selectedOptions.size(); i++) {
                String[] array = selectedOptions.get(i).getValue().split(",");
                if (filedId.equals(array[0]) && array.length > 1) {
                    for (int j = 0; j < Singleton.getInstance().getExtraFieldsFairData().getExtraFields().size(); j++) {
                        if (Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(j).getAutoId().equals(array[0])) {
                            int index = Integer.valueOf(array[array.length - 1]);
                            return String.valueOf(Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(j).getOptions().get(index).getAutoId());
                        }
                    }
                }
            }

        }

        return " ";
    }

    private void reisterJsonObject(Boolean cv) {
        Helper.showProgressBar(progressBar);
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairId());
        JsonObject.addProperty("name", nameEdt.getText().toString());
        JsonObject.addProperty("email", emailEdt.getText().toString());
        JsonObject.addProperty("password", passwordEdt.getText().toString());
        JsonObject.addProperty("user_country", Singleton.getInstance().getAddresses().get(0).getCountryName());
        if (Singleton.getInstance().getFairData().getFair().getOptions().getFieldPhoneNumber().equals(0))
            JsonObject.addProperty("phone", numberEdt.getText().toString());
        if (Singleton.getInstance().getFairData().getFair().getOptions().getFieldJobTitle().equals(0))
            JsonObject.addProperty("user_job_title", jobTitleEdt.getText().toString());
        if (Singleton.getInstance().getFairData().getFair().getOptions().getFieldCompanyName().equals(0))
            JsonObject.addProperty("user_company", companyNameEdt.getText().toString());

        if (Singleton.getInstance().getExtraFieldsFairData() != null && Singleton.getInstance().getExtraFieldsFairData().getExtraFields() != null) {
            for (int i = 0; i < Singleton.getInstance().getExtraFieldsFairData().getExtraFields().size(); i++) {
                if (Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getHideField().equals("N")) {
                    JsonObject jb = new JsonObject();
                    String id;
                    if (Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getFieldType().equals("text")) {
                        id = getItemId(Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getAutoId(), "text");
                        jb.addProperty(Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getAutoId(), id);

                    } else {
                        id = getItemId(Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getAutoId(), "dropDown");
                        jb.addProperty(Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getAutoId(), id);

                    }
                    if (!id.equals(" ")) {
                        extraFields.addProperty(Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getAutoId(), id);
                    }
                    //if (!id.equals(" "))
                }

            }

        }
        JsonObject.addProperty("user_city", Singleton.getInstance().getAddresses().get(0).getLocality());
        if (Singleton.getInstance().getAddresses().get(0).getPostalCode() == null) {
            JsonObject.addProperty("user_postal_code", "980096");
        } else {
            JsonObject.addProperty("user_postal_code", Singleton.getInstance().getAddresses().get(0).getPostalCode());
        }
        JsonObject.addProperty("timezone", TimeZone.getDefault().getID());
        JsonObject.add("extra_fields", extraFields);
        JsonObject.addProperty("device_type", "android");

        try {
            phone = numberEdt.getText().toString();
        } catch (Exception e) {

        }
        try {
            company = companyNameEdt.getText().toString();
        } catch (Exception e) {

        }
        try {
            job = jobTitleEdt.getText().toString();
        } catch (Exception e) {

        }

        if (Helper.isInternetConnected()) {
            if (file != null) {
                new RetrieveFeedTask().execute();
            } else if (file == null) {
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                JsonObject.addProperty("device_id", task.getResult());
                                Log.d("reisterJsonObject", JsonObject.toString());
                                registerUserApiWithoutCV(JsonObject);
                            } else Helper.showToast("Firebase token error");
                        }

                );
            }
        }


    }

    private void registerUserApi(JsonObject JsonObject) {

        String json = new Gson().toJson(JsonObject);


        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("document", fileName, requestFile);

        RequestBody jsonBody =
                RequestBody.create(
                        MediaType.parse("application/json"), String.valueOf(json));

        Call<com.vrd.gsaf.api.responses.login.Login> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).registerUser(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, body, jsonBody);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.login.Login>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.login.Login> call, Response<com.vrd.gsaf.api.responses.login.Login> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.body().getStatus()) {
                        Singleton.getInstance().setLoginData(response.body().getData());
                        updateLanguage(response, false, null);
                    } else {
                        Helper.showToast("Something went wrong...Please try later!");
                        Log.e("something1", response.toString());
                    }
                } catch (Exception e) {
                    Helper.showToast("Something Went Wrong.6");
                    Log.e("something2", e.toString());

                }

            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.login.Login> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something Went Wrong. Please try again in a while");
                Log.e("something3", t.toString());


            }

        });
    }

    private void registerUserApiWithoutCV(JsonObject jsonObject) {

//        jsonObject.remove("extra_fields");
//        JsonArray jsonArray=new JsonArray();
//        com.google.gson.JsonObject j=new JsonObject();
//        j.addProperty("18","547");
//        jsonArray.add(j);

        // jsonObject.add("extra_fields",jsonArray);

        Call<com.vrd.gsaf.api.responses.login.Login> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).registerUser(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, jsonObject);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.login.Login>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.login.Login> call, Response<com.vrd.gsaf.api.responses.login.Login> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.body().getStatus()) {
                        Singleton.getInstance().setLoginData(response.body().getData());
                        updateLanguage(response, false, null);
                    } else {
                        //  Helper.showToast(response.body().getMsg());
                        Helper.showToast(response.body().getMsg());
                    }
                } catch (Exception e) {
                    ResponseBody response1 = response.errorBody();
                    Helper.getErrorMessage(response1);


                }

            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.login.Login> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something Went Wrong. Please try again in a while");


            }

        });
    }

    private void updateLanguage(Response<com.vrd.gsaf.api.responses.login.Login> loginResponse, boolean withoutCV, com.vrd.gsaf.api.responses.login.Login feed) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fair_id", Singleton.getMyFairId());
        jsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        Call<BaseResponse> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).updateLanguage("Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, jsonObject);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.body().getStatus()) {
                        if (!withoutCV && loginResponse != null) {
                            updateSp(loginResponse);
                        } else if (feed != null) update(feed);
                    } else {
                        //  Helper.showToast(response.body().getMsg());
                        Helper.showToast(response.body().getMsg());
                    }
                } catch (Exception e) {
                    ResponseBody response1 = response.errorBody();
                    Helper.getErrorMessage(response1);


                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something Went Wrong. Please try again in a while");


            }

        });
    }

    private Boolean checkCv() {
        if (Singleton.getInstance().getFairData().getFair().getFairSetting().getCvRequired().equals("1") && file == null) {
            cvTxtView.setTextColor(getResources().getColor(R.color.required_fileds));
            return false;
        } else if (Singleton.getInstance().getFairData().getFair().getFairSetting().getCvRequired().equals("2"))
            return true;
        else if (Singleton.getInstance().getFairData().getFair().getFairSetting().getCvRequired().equals("0"))
            return true;
        else
            return true;
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
            case R.id.registerBtn:
                if (getValues()) {
                    if (checkCv()) {
                        if (!termsAndConditionsCheckBox.isChecked()) {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                            termsAndConditionsLinkTxt.setTextColor(getResources().getColor(R.color.required_fileds));
                            privacyLinkTxt.setTextColor(getResources().getColor(R.color.required_fileds));
                        } else {
                            Helper.hideKeyboard();
                            // if (file == null)
                            reisterJsonObject(false);
//                            else
//                                reisterJsonObject(true);
                        }
                    }
                }
                // loadHome();
                break;
            case R.id.loginLayout:
                replaceFragment(new Login());
                break;
            case R.id.registerWithOtherOptionsLayout:
                replaceFragment(new RegistrationOptions());
                break;
            case R.id.backImageView:
                MainActivity.getInstance().clearAllFragmentStack();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Events())
                        .setReorderingAllowed(true)
                        .addToBackStack("events")
                        .commit();
                break;
            case R.id.uploadCvLayout:
                uploadCv();
                break;
            case R.id.passwordVisibleImageView:
                visibilityCheck();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == resultCode) {
                    if (data != null) {
                        String filePath = getRealPathFromUri(data.getData(), Singleton.getInstance().getActivity());
                        String[] parsedpath = filePath.split("/");
                        //now we will upload the file
                        //lets import okhttp first
                        fileName = parsedpath[parsedpath.length - 1];
                        file = new File(filePath);
                        uploadCvLayout.setVisibility(View.GONE);
                        cvUploadedLayout.setVisibility(View.VISIBLE);
                        TextView fileNameTxt = cvLayout.findViewById(R.id.fileNameTxt);
                        fileNameTxt.setText(fileName);
                        ImageView cross = cvLayout.findViewById(R.id.cancel);
                        cross.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                uploadCvLayout.setVisibility(View.VISIBLE);
                                cvUploadedLayout.setVisibility(View.GONE);
                                file = null;
                                fileName = null;
                            }
                        });
                    }
                }
                break;

        }


    }

    public String getRealPathFromUri(Uri uri, Activity activity) {
        return getDriveFilePath(uri, getContext());
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
//        if (cursor == null) {
//            return uri.getPath();
//        } else {
//            cursor.moveToFirst();
//            int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            String data = getDriveFilePath(uri,getContext());
//           // return cursor.getString(id);
//            return data;
//        }


        //more info here: https://developers.google.com/drive/web/m
    }

    private void updateSp(Response<com.vrd.gsaf.api.responses.login.Login> response) {
        Singleton.getInstance().setLoginData(response.body().getData());
        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
        editor.putBoolean("login", true);
        Gson gson = new Gson();
        String json = gson.toJson(response.body().getData());
        editor.putString("loginData", json);
        editor.commit();
        editor.apply();

        if (Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("1")) {
            Singleton.getInstance().setNewRegistration(true);
            replaceFragment(new CareerTest());
        } else {
            Singleton.getInstance().setNewRegistration(true);
            loadHome();
        }
    }

    private final void focusOnView(EditText edt) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, edt.getBottom());
            }
        });
    }

    private void userRegistered(com.vrd.gsaf.api.responses.login.Login feed) {
        Helper.hideProgressBar(progressBar);
        try {
            if (feed.getStatus()) {
                updateLanguage(null, true, feed);
//                update(feed);
            } else
                Helper.showToast(feed.getMsg());
        } catch (Exception e) {
            Helper.showToast("Something went wrong...Please try later!");
            Log.e("something7", e.toString());


        }
//


    }

    private void update(com.vrd.gsaf.api.responses.login.Login feed) {

        Singleton.getInstance().setLoginData(feed.getData());
        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
        editor.putBoolean("login", true);
        Gson gson = new Gson();
        String json = gson.toJson(feed.getData());
        editor.putString("loginData", json);
        editor.commit();
        editor.apply();

        if (Singleton.getInstance().getLoginData().getUser().getTakeTest() == 0 && (Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("1") || Singleton.getInstance().getFairData().getFair().getOptions().getEnable_force_questionnaire_after_registration_front() == 1)) {
            Singleton.getInstance().setNewRegistration(true);
            replaceFragment(new CareerTest());
        } else {
            SharedPreferences.Editor editor2 = Singleton.getInstance().getSharedPreferences().edit();
            editor2.putBoolean("careerTest", false);
            editor.commit();
            editor.apply();
            Singleton.getInstance().setNewRegistration(true);
            loadHome();
        }

    }

    private void openTermAndConditionsDialog(String key, String data) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_term_and_conditions, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        ImageView cancel = dialogView.findViewById(R.id.cancelImageView);
        TextView webinarTitleTxt = dialogView.findViewById(R.id.webinarTitleTxt);
        TextView descriptionTxt = dialogView.findViewById(R.id.descriptionTxt);
        if (key.equalsIgnoreCase("privacy")) {
            webinarTitleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getPrivacyPolicyKey());
            Helper.loadHtml(descriptionTxt, data);
        } else {
            webinarTitleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getTermsConditionsKey());
            Helper.loadHtml(descriptionTxt, data);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
//        ViewGroup.LayoutParams params = dialogView.getLayoutParams();
//        params.height = height-200;
//        params.width = width-200;
//        dialogView.setLayoutParams(params);

//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
//        layoutParams.width = height-200;
//        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT-200;
//        alertDialog.getWindow().setAttributes(layoutParams);
        ViewGroup.LayoutParams window = dialogView.getLayoutParams();

        // window.setLayout(width-100, height/2);
//        window.height=height/2;

        alertDialog.getWindow().setLayout(200, 400);
        alertDialog.show();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                lastDown = System.currentTimeMillis();

                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                lastDuration = System.currentTimeMillis() - lastDown;
                if (lastDuration < 150) {
                    //  if (lastAction == MotionEvent.ACTION_DOWN) {
                    Singleton.getInstance().setIsLoggedIn(false);
                    loadHome();
                    // }
                }
                break;

            default:
                return false;
        }
        return true;
    }

    private void getPrivacyAndConditions(String type) {
        Helper.showProgressBar(progressBar);
        String url = "";
        if (type.equalsIgnoreCase("privacy")) {
            url = "api/auth/fair/privacy/" + Singleton.getInstance().getFairData().getFair().getId();
        } else if (type.equalsIgnoreCase("terms")) {
            url = "api/auth/fair/terms/" + Singleton.getInstance().getFairData().getFair().getId();
        }

        Call<PrivacyAndConditionsReponse> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getPrivacyAndConditions(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<PrivacyAndConditionsReponse>() {
            @Override
            public void onResponse(Call<PrivacyAndConditionsReponse> call, Response<PrivacyAndConditionsReponse> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.body().getStatus()) {
//                        parseResponse(response);
                        if (type.equalsIgnoreCase("privacy")) {
                            openTermAndConditionsDialog(type, response.body().getPrivacyCondition().getPrivacyPolicy());
                        } else if (type.equalsIgnoreCase("terms")) {
                            openTermAndConditionsDialog(type, response.body().getPrivacyCondition().getTermsConditions());
                        }
                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
//                    Helper.showToast("Something went wrong...Please try later!");
                }
            }

            @Override
            public void onFailure(Call<PrivacyAndConditionsReponse> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something went wrong...Please try later!");
                // Toast.makeText(Singleton.getInstance().getContext(), "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, com.vrd.gsaf.api.responses.login.Login> {

        private Exception exception;

        protected com.vrd.gsaf.api.responses.login.Login doInBackground(String... urls) {
            String postalCode = Singleton.getInstance().getAddresses().get(0).getPostalCode();
            if (postalCode == null)
                postalCode = "null";
            String lang = "";
            if (Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getCode() == null) {
                lang = "109";
            } else
                lang = Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getCode();

            if (token != null) {


                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("name", nameEdt.getText().toString())
                        .addFormDataPart("email", emailEdt.getText().toString())
                        .addFormDataPart("phone", phone)
                        .addFormDataPart("user_job_title", job)
                        .addFormDataPart("user_company", company)
                        .addFormDataPart("password", passwordEdt.getText().toString())
                        .addFormDataPart("fair_id", String.valueOf(Singleton.getInstance().getFairId()))
                        .addFormDataPart("user_country", Singleton.getInstance().getAddresses().get(0).getCountryName())
                        .addFormDataPart("user_city", Singleton.getInstance().getAddresses().get(0).getLocality())
                        .addFormDataPart("user_postal_code", postalCode)
                        .addFormDataPart("timezone", TimeZone.getDefault().getID())
                        .addFormDataPart("extra_fields", extraFields.toString())
                        .addFormDataPart("device_type", "android")
                        .addFormDataPart("device_id", token)
                        .addFormDataPart("document", fileName,
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        new File(file.getPath())))
                        .build();


//                                .addFormDataPart("document", "user-id-12457_Vol.5,-No.1.-May-2015.-pp.-51--54.pdf",
//                    RequestBody.create(MediaType.parse("application/octet-stream"),
//                            new File("/storage/emulated/0/Download/user-id-12457_Vol.5,-No.1.-May-2015.-pp.-51--54.pdf")))

                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url(AppConstants.BASE_URL + "api/auth/candidates")
                        .post(body)
                        .addHeader("app-id", AppConstants.APP_ID)
                        .addHeader("requested-lang", lang)
                        .addHeader("app-key", AppConstants.APP_KEY)
                        .addHeader("Accept", "application/json")
                        .build();


                okhttp3.Response response = null;
                try {
                    response = client.newCall(request).execute();


                    if (response.isSuccessful()) {
                        try {
                            com.vrd.gsaf.api.responses.login.Login responseResult = null;
                            Gson gson = new Gson();
                            com.vrd.gsaf.api.responses.login.Login login = null;
                            // Do something here
                            login = gson.fromJson(response.body().string(), com.vrd.gsaf.api.responses.login.Login.class);
                            return login;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    } else {
                        Helper.getErrorMessage(response.body());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                requireActivity().runOnUiThread(() -> Helper.showToast("Firebase token error"));
            }
            return null;

        }


        protected void onPostExecute(com.vrd.gsaf.api.responses.login.Login feed) {
            userRegistered(feed);
            Singleton.getInstance().setLoginData(feed.getData());
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }
}


