package com.vrd.gsaf.home.dashboard.profile;

import static android.graphics.Color.parseColor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.activity.MainActivity;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.BaseResponse;
import com.vrd.gsaf.api.responses.fairExtraFields.ExtraField;
import com.vrd.gsaf.api.responses.fairExtraFields.FairExtraFieldsResponse;
import com.vrd.gsaf.api.responses.fairExtraFields.FairOption;
import com.vrd.gsaf.api.responses.login.Data;
import com.vrd.gsaf.api.responses.login.Login;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.model.SelectedOptions;
import com.vrd.gsaf.model.ViewModel;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserProfile extends Fragment implements View.OnClickListener {

    private static final int RESULT_OK = -1;
    private static ConstraintLayout mainLayout;
    //  private static ConstraintLayout pdfLayout;
    private final long MIN_CLICK_INTERVAL = 600;
    private final int PICKFILE_RESULT_CODE = 100;
    private final int PERMISSION_REQUEST_CODE = 1;
    private final ArrayList<ViewModel> dynamicViews = new ArrayList<>();
    private final int languageIndex = Singleton.getInstance().getLanguageIndex();
    private final ArrayList<SelectedOptions> selectedOptions = new ArrayList<>();
    private final JsonObject JsonObject = new JsonObject();
    private final JsonObject extraFields = new JsonObject();
    private final UserProfile activity = this;
    public boolean isViewClicked = false;
    WebView urlWebView;
    RelativeLayout toolbar;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView;
    private ImageView editPicImageView;
    private ImageView userImageView;
    private Button updateBtn;
    private TextView titleTxt, txt, companyName, jobTitle, numberTxt, loginTxt, nameTxt, emailTxt, cvTxtView, textView1, termsAndConditionsLinkTxt, privacyLinkTxt, cityTExt, postaltext, countrytext;
    private EditText nameEdt, emailEdt, numberEdt, companyNameEdt, jobTitleEdt, cityEdit, postalEdit, countryEdit;
    private ConstraintLayout nameLayout;
    private ConstraintLayout emailLayout;
    private ConstraintLayout passwordLayout;
    private ConstraintLayout numberLayout;
    private ConstraintLayout companyNameLayout;
    private ConstraintLayout jobTitleLayout;
    private ConstraintLayout loginLayout;
    private ConstraintLayout uploadCvLayout;
    private ConstraintLayout cvUploadedLayout;
    private long mLastClickTime;
    private View cvLayout;
    private File file;
    private ScrollView scrollView;
    private String fileName;
    //  private PDFView pdfView;
    private ByteArrayOutputStream stream;
    private String company = "";
    private String phone = "";
    private String job = "";
    private boolean camera = false;
    private Uri picUri;
    private Intent savedData;
    private UserProfile userProfile;

    public static void onBackPressProfile() {

        // FragmentManager fm = getParentFragmentManager();
        if (Singleton.getInstance().isPdfProfile()) {
            Singleton.getInstance().setPdfProfile(false);
            //  pdfLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        } else
            Singleton.getInstance().getActivity().onBackPressed();
        // fm.popBackStack("speakers", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();
        userProfile = this;

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Helper.hideKeyboard();
        initializeViews();
        getFairExtraFieldsApi();
        return view;
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
                numberEdt.setText(Singleton.getInstance().getLoginData().getUser().getPhone());
                setTextColor(numberTxt);
                setTextColor(numberEdt);

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
                companyNameEdt.setText(Singleton.getInstance().getLoginData().getUser().getUserCompany());
                setTextColor(companyName);
                setTextColor(companyNameEdt);

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
                jobTitleEdt.setText(Singleton.getInstance().getLoginData().getUser().getUserJobTitle());

                setTextColor(jobTitleEdt);
                setTextColor(jobTitle);
            }
            if (Singleton.getInstance().getExtraFieldsFairData() != null) {
                for (int i = 0; i < Singleton.getInstance().getExtraFieldsFairData().getExtraFields().size(); i++) {
                    if (Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getFieldType().equals("dropdown") && Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getHideField().equals("N"))
                        dropDownView(Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i), i, Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getAutoId(),
                                Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getFieldTitle(),
                                Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getOptions(),
                                Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getIsRequired());
                    else if (Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getFieldType().equals("text") && Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getHideField().equals("N"))
                    //  setTextView(1, "Name","text");
                    {
                        setTextView(Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i), i, Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getAutoId(), Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getFieldTitle(), "text", Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getIsRequired());
                    }


                }
            }
            if (Singleton.getInstance().getFairData().getFair().getFairSetting().getCvRequired().equals("1") || Singleton.getInstance().getFairData().getFair().getFairSetting().getCvRequired().equals("2")) {
                uploadCvView();
            }
//            setTextColor(numberEdt);
//            setTextColor(companyNameEdt);
//            setTextColor(companyName);
//            setTextColor(numberEdt);
//            setTextColor(numberTxt);
            setKeyWords();
            setClickListeners();

        } catch (Exception e) {
            Log.d("Error", "updateView registration");
        }

    }

    public void setTextColor(EditText editText) {
        editText.setTextColor(Singleton.getSidebarInnerTextColor());
        editText.setHintTextColor(Color.GRAY);
        editText.getBackground().setColorFilter(Singleton.getSidebarInnerTextColor(),
                PorterDuff.Mode.SRC_ATOP);
    }

    public void setTextColor(TextView textView) {
        textView.setTextColor(Singleton.getSidebarInnerTextColor());
    }

    private void uploadCvView() {
        LinearLayout ll = view.findViewById(R.id.parentLinearLayout);
        cvLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.upload_cv, ll, false);
        ll.addView(cvLayout);
        uploadCvLayout = view.findViewById(R.id.uploadCvLayout);
        uploadCvLayout.setVisibility(View.VISIBLE);
        cvUploadedLayout = view.findViewById(R.id.cvUploaded);
        ImageView chooseFileImageView = view.findViewById(R.id.chooseFileImageView);
        Singleton.changeIconColor(R.drawable.attachment, chooseFileImageView);
        cvUploadedLayout.setVisibility(View.GONE);

        TextView fileDescriptionTxt = view.findViewById(R.id.fileDescriptionTxt);
        TextView chooseFileTxt = view.findViewById(R.id.chooseFileTxt);
        fileDescriptionTxt.setText(Singleton.getInstance().getLoginData().getUser().getCv());
        uploadCvLayout.setOnClickListener(this);
        cvTxtView = cvLayout.findViewById(R.id.uploadCvTxt);
        setTextColor(cvTxtView);
        setTextColor(fileDescriptionTxt);
        setTextColor(chooseFileTxt);
        fileDescriptionTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getUpload() + " " + Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getTransOnlyUploadCvFormat());
        cvTxtView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getUpload() + " " + Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getCv());


        TextView fileNameTxt = cvLayout.findViewById(R.id.fileNameTxt);
        ImageView imageView = cvLayout.findViewById(R.id.documentImageView);
        fileNameTxt.setText(Singleton.getInstance().getLoginData().getUser().getCv());
        ImageView cross = cvLayout.findViewById(R.id.cancel);
        setTextColor(fileNameTxt);
        Singleton.changeIconColor(cross);
        if (!Singleton.getInstance().getLoginData().getUser().getCv().equals("")) {
            uploadCvLayout.setVisibility(View.GONE);
            cvUploadedLayout.setVisibility(View.VISIBLE);
        }

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadCvLayout.setVisibility(View.VISIBLE);
                cvUploadedLayout.setVisibility(View.GONE);
                file = null;
                fileName = null;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                urlWebView.getSettings().setJavaScriptEnabled(true);
//                urlWebView.getSettings().setUseWideViewPort(true);
//                urlWebView.loadUrl("http://docs.google.com/gview?embedded=true&url="
//                        + Singleton.getInstance().getFairData().getSystemUrl().getResumeUrl() +
//                        Singleton.getInstance().getFairData().getSystemUrl().getResumeRoute() +
//                        Singleton.getInstance().getLoginData().getUser().getCv());

            }
        });

    }

    private void dropDownView(ExtraField extraField, int j, String id, String title, List<FairOption> options, String isRequired) {

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
        String optionId;
        setTextColor(textView);
        setTextColor(headingTxt);
        for (int l = 0; l < extraField.getOptions().size(); l++) {
            if (extraField.getOptions().get(l).getIsSelected()) {
                textView.setText(extraField.getOptions().get(l).getOptionText());
                model.setValue(extraField.getAutoId() + "," + l);

                SelectedOptions selectedOptions1 = new SelectedOptions(extraField.getAutoId() + "," + l, "dropDown");
                selectedOptions1.setAutoId(String.valueOf(extraField.getAutoId()));
                selectedOptions.add(selectedOptions1);
            }
        }
//        for (int i = 0; i < Singleton.getInstance().getLoginData().getUser().getExtraFields().size(); i++) {
//            if (Singleton.getInstance().getLoginData().getUser().getExtraFields().get(i).getFieldId().equals(id)) {
//                optionId = Singleton.getInstance().getLoginData().getUser().getExtraFields().get(i).getOptionId();
//                for (int k = 0; k < Singleton.getInstance().getExtraFieldsFairData().getExtraFields().size(); k++) {
//                    if (Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(i).getAutoId().equals(id)) {
//                        for (int l = 0; l < Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(k).getOptions().size(); l++) {
//                            if (Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(k).getOptions().get(l).getAutoId().toString()
//                                    .equals(optionId)) {
//                                textView.setText(Singleton.getInstance().getLoginData().getUser().getExtraFields().get(i).getFieldText());
//                                model.setValue(textView.getText().toString());
//
//                                SelectedOptions selectedOptions1 = new SelectedOptions(Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(k).getOptions().get(l).getFieldId() + "," + l, "dropDown");
//                                selectedOptions1.setAutoId(Singleton.getInstance().getExtraFieldsFairData().getExtraFields().get(k).getOptions().get(l).getFieldId());
//                                selectedOptions.add(selectedOptions1);
//                                break;
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
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
        dialog.getWindow().setLayout(width / 2, ViewGroup.LayoutParams.WRAP_CONTENT);

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

    private void setKeyWords() {
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getProfile());
        nameTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getName());
        emailTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getEmail());
        updateBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getUpdate());
        cityTExt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getCity());
        countrytext.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getCountry());
        postaltext.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getPostCode());
        nameEdt.setText(Singleton.getInstance().getLoginData().getUser().getName());
        emailEdt.setText(Singleton.getInstance().getLoginData().getUser().getEmail());
        postalEdit.setText(Singleton.getInstance().getLoginData().getUser().getPostalCode());
        cityEdit.setText(Singleton.getInstance().getLoginData().getUser().getCityName());
        countryEdit.setText(Singleton.getInstance().getLoginData().getUser().getCountryName());
        Helper.setTextColor(nameTxt);
        Helper.setTextColor(emailTxt);
        Helper.setTextColor(emailEdt);
        Helper.setTextColor(cityTExt);
        Helper.setTextColor(cityEdit);
        Helper.setTextColor(countrytext);
        Helper.setTextColor(countryEdit);
        Helper.setTextColor(postaltext);
        Helper.setTextColor(postalEdit);
    }

    @SuppressLint("ResourceType")
    private void setTextView(ExtraField extraField, int i, String id, String title, String input, String isRequired) {

        LinearLayout ll = view.findViewById(R.id.parentLinearLayout);
        View layout2 = LayoutInflater.from(view.getContext()).inflate(R.layout.text_view, ll, false);
        // layout2.setId(id);

        TextView textView = layout2.findViewById(R.id.nameTxt);

        textView.setText(title);
        setTextColor(textView);
        if (isRequired.equals("N")) {
            TextView textView2 = layout2.findViewById(R.id.requiredStar);
            textView2.setVisibility(View.GONE);
        }
        EditText editText = layout2.findViewById(R.id.nameEdt);
        setTextColor(editText);
        if (input.equals("number"))
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setText(extraField.getUserAnswer());
//        for (int i = 0; i < Singleton.getInstance().getLoginData().getUser().getExtraFields().size(); i++) {
//            if (Singleton.getInstance().getLoginData().getUser().getExtraFields().get(i).getFieldId().equals(id)) {
//                editText.setText(Singleton.getInstance().getLoginData().getUser().getExtraFields().get(i).getFieldText());
//                break;
//            }
//        }
        ll.addView(layout2);
        ViewModel model = new ViewModel(23, "text", isRequired, textView);
        model.setEditText(editText);
        model.setAutoId(id);
        model.setExtra(true);
        model.setValue(extraField.getAutoId() + "," + i);
        dynamicViews.add(model);

    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        scrollView = view.findViewById(R.id.scrollView);
        //  pdfLayout = view.findViewById(R.id.pdfLayout);
        mainLayout = view.findViewById(R.id.mainLayout);
        progressBar = view.findViewById(R.id.progressBar);
        cityTExt = view.findViewById(R.id.cityTextView);
        cityEdit = view.findViewById(R.id.cityEdt);
        countrytext = view.findViewById(R.id.countryTextView);
        countryEdit = view.findViewById(R.id.countryEdt);
        postaltext = view.findViewById(R.id.postalTextView);
        postalEdit = view.findViewById(R.id.postalEdt);
        backImageView = view.findViewById(R.id.backImageView);
        editPicImageView = view.findViewById(R.id.editPicImageView);
        userImageView = view.findViewById(R.id.userImageView);
        urlWebView = view.findViewById(R.id.webView);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        // pdfView = view.findViewById(R.id.idPDFView);

        try {
            if (Singleton.getInstance().getLoginData().getUser().getProfileImage() != null && Singleton.getInstance().getLoginData().getUser().getProfileImage() != "") {
                Helper.loadCircularImageFromUrl(userImageView, Singleton.getInstance().getFairData().getSystemUrl().getAvatarUrl() + Singleton.getInstance().getLoginData().getUser().getProfileImage());
            }
        } catch (Exception e) {

        }
//            Glide.with(Singleton.getInstance().getContext())
//                    .load(Singleton.getInstance().getFairData().getSystemUrl().getAvatarUrl() + Singleton.getInstance().getLoginData().getUser().getProfileImage())
//                    .placeholder(R.drawable.add_emoji)
//                    .circleCrop()
//                    .into(userImageView);


        titleTxt = view.findViewById(R.id.tittle);
        updateBtn = view.findViewById(R.id.updateBtn);

        Helper.setButtonColorWithDrawable(updateBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());


        nameEdt = view.findViewById(R.id.nameEdt);
        nameTxt = view.findViewById(R.id.nameTxt);
        emailEdt = view.findViewById(R.id.emailEdt);
        emailEdt.setEnabled(false);
        emailTxt = view.findViewById(R.id.emailTextView);
        setTextColor(nameEdt);
        setTextColor(emailEdt);
        setTextColor(nameTxt);
        setTextColor(emailTxt);
        Singleton.changeIconColor(R.drawable.edit_image, editPicImageView);
        ViewModel model = new ViewModel(1, "text", "Y", nameTxt);
        model.setEditText(nameEdt);
        dynamicViews.add(model);
        ViewModel model1 = new ViewModel(1, "text", "Y", emailTxt);
        model1.setEditText(emailEdt);
        dynamicViews.add(model1);
        updateView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        editPicImageView.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        scrollView.setOnTouchListener((v, event) -> {
            if (event != null && event.getAction() == MotionEvent.ACTION_MOVE) {
                InputMethodManager imm = ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
                boolean isKeyboardUp = imm.isAcceptingText();

                if (isKeyboardUp) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return false;
        });

    }

    private void replaceFragment(Fragment fragment, String tag) {

        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack();
        fm.popBackStack();
        fm.popBackStack();
        fm.beginTransaction().replace(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .commit();
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
                onBackPressProfile();
                break;
            case R.id.editPicImageView:

                if (ActivityCompat.checkSelfPermission(Singleton.getInstance().getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1000);
                } else {
                    openEditPictureDialog();
                }
                break;
            case R.id.uploadCvLayout:
                uploadCv();
                break;
            case R.id.updateBtn:
                if (getValues() && nameValidation()) {
                    if (checkCv()) {
                        reisterJsonObject(false);
                    }
                }
                break;

        }
    }


    private boolean nameValidation() {
        if (nameEdt.getText().toString().equalsIgnoreCase("")) {
            nameEdt.setTextColor(getResources().getColor(R.color.required_fileds));
            nameEdt.requestFocus();
            focusOnView(nameEdt);
            Helper.showToast("csac");
            Toast.makeText(Singleton.getInstance().getContext(), "Name can only contains alphabets and space", Toast.LENGTH_LONG).show();
            return false;
        } else
            Helper.setTextColor(nameEdt);
        if (countryEdit.getText().toString().equalsIgnoreCase("")) {
            countryEdit.setTextColor(getResources().getColor(R.color.required_fileds));
            countryEdit.requestFocus();
            focusOnView(countryEdit);
            Toast.makeText(Singleton.getInstance().getContext(), Singleton.getKeywords().getPlease_enter_country(), Toast.LENGTH_LONG).show();
            setTextColor(nameEdt);
            setTextColor(emailEdt);
            setTextColor(nameTxt);
            setTextColor(emailTxt);
            return false;
        } else Helper.setTextColor(countryEdit);
        if (cityEdit.getText().toString().equalsIgnoreCase("")) {
            cityEdit.setTextColor(getResources().getColor(R.color.required_fileds));
            cityEdit.requestFocus();
            focusOnView(cityEdit);
            Toast.makeText(Singleton.getInstance().getContext(), Singleton.getKeywords().getPlease_enter_city(), Toast.LENGTH_LONG).show();
            setTextColor(nameEdt);
            setTextColor(emailEdt);
            setTextColor(nameTxt);
            setTextColor(emailTxt);
            return false;
        } else Helper.setTextColor(cityEdit);
        if (postalEdit.getText().toString().equalsIgnoreCase("")) {
            postalEdit.setTextColor(getResources().getColor(R.color.required_fileds));
            postalEdit.requestFocus();
            focusOnView(postalEdit);
            Toast.makeText(Singleton.getInstance().getContext(), Singleton.getKeywords().getPlease_enter_post_code(), Toast.LENGTH_LONG).show();
            setTextColor(nameEdt);
            setTextColor(emailEdt);
            setTextColor(nameTxt);
            setTextColor(emailTxt);
            return false;
        } else Helper.setTextColor(postalEdit);

        return true;
    }

    private void selectPicture() {

        ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
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
                            int index = Integer.valueOf(array[1]);
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


        JsonObject.addProperty("fair_id", String.valueOf(Singleton.getInstance().getFairData().getFair().getId()));
        JsonObject.addProperty("name", nameEdt.getText().toString());
        JsonObject.addProperty("email", emailEdt.getText().toString());
//        if (Singleton.getInstance().getAddresses() != null && !Singleton.getInstance().getAddresses().isEmpty()) {
//            JsonObject.addProperty("user_country", Singleton.getInstance().getAddresses().get(0).getCountryName());
//        } else JsonObject.addProperty("user_country", "unknown");

        if (Singleton.getInstance().getFairData().getFair().getOptions().getFieldPhoneNumber().equals(0))
            JsonObject.addProperty("phone", numberEdt.getText().toString());
        if (Singleton.getInstance().getFairData().getFair().getOptions().getFieldJobTitle().equals(0))
            if (jobTitleEdt != null)
                JsonObject.addProperty("user_job_title", jobTitleEdt.getText().toString());
        if (Singleton.getInstance().getFairData().getFair().getOptions().getFieldCompanyName().equals(0))
            JsonObject.addProperty("user_company", companyNameEdt.getText().toString());

        if (Singleton.getInstance().getExtraFieldsFairData() != null) {
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


        JsonObject.addProperty("city_name", cityEdit.getText().toString());
        JsonObject.addProperty("country_name", countryEdit.getText().toString());
        JsonObject.addProperty("postal_code", postalEdit.getText().toString());
        JsonObject.addProperty("timezone", TimeZone.getDefault().getID());
        JsonObject.add("extra_fields", extraFields);
        getView().clearFocus();
        InputMethodManager imm = (InputMethodManager) Singleton.getInstance().getContext().
                getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        try {
            phone = numberEdt.getText().toString();
        } catch (Exception e) {
            // Helper.showToast("sd");
        }
        try {
            company = companyNameEdt.getText().toString();
        } catch (Exception e) {
            //  Helper.showToast("sd");

        }
        try {
            job = jobTitleEdt.getText().toString();
        } catch (Exception e) {
            //Helper.showToast("sd");

        }
        Helper.setTextColor(nameTxt);
        Helper.setTextColor(emailTxt);
        Helper.setTextColor(emailEdt);
        Helper.setTextColor(nameEdt);
        if (Helper.isInternetConnected()) {
            if (file != null) {
                try {
                    new RetrieveFeedTask().execute();
                } catch (Exception e) {
                    Helper.hideProgressBar(progressBar);
                }
            } else if (file == null)
                updateUserApiWithoutCV(JsonObject);
        }


    }

    private void updateUserApiWithoutCV(JsonObject JsonObject) {
        Call<Login> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).updateProfile(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.login.Login> call, Response<Login> response) {
                Helper.hideProgressBar(progressBar);
                try {

                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.body().getStatus()) {
                        updateSp(response.body().getData());
                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
//                    Helper.showToast("Something Went Wrong.");
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

    private void updateSp(Data response) {
        response.setAccessToken(Singleton.getInstance().getLoginData().getAccessToken());
        Singleton.getInstance().setLoginData(response);
        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
        editor.putBoolean("login", true);
        Gson gson = new Gson();
        String json = gson.toJson(Singleton.getInstance().getLoginData());
        editor.putString("loginData", json);
        editor.apply();
        updateUser();
        MainActivity.getInstance().getExtraField();
    }

    private void updateUser() {
        try {
            Helper.showProgressBar(progressBar);
            View headerView = Singleton.getInstance().getNavigationView().getHeaderView(0);
            TextView navUsername = headerView.findViewById(R.id.userNameTxt);
            ImageView headerImageView = headerView.findViewById(R.id.imageView);
            if (Singleton.getInstance().getLoginData() != null) {
                navUsername.setText(Singleton.getInstance().getLoginData().getUser().getName());
            }
            Helper.showToast(Singleton.getKeywords().getTrans_string_successfully_updated_profile());
            headerView.setBackgroundColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
            navUsername.setTextColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarInnerTextColor()));
            headerImageView.setImageBitmap(null);

            Helper.loadCircularImageFromUrl(userImageView, Singleton.getInstance().getFairData().getSystemUrl().getAvatarUrl() + Singleton.getInstance().getLoginData().getUser().getProfileImage());
            Helper.loadCircularImageFromUrl(headerImageView, Singleton.getInstance().getFairData().getSystemUrl().getAvatarUrl() + Singleton.getInstance().getLoginData().getUser().getProfileImage());
            Helper.hideProgressBar(progressBar);
            requireActivity().onBackPressed();

        } catch (Exception e) {
            Helper.hideProgressBar(progressBar);
        }

    }

    private void updateUserImage(String url) {
        Helper.showToast("Profile picture updated");
        View headerView = Singleton.getInstance().getNavigationView().getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.imageView);
        Glide.with(Singleton.getInstance().getContext())
                .load(AppConstants.USER_PICTURE + url)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

        Glide.with(Singleton.getInstance().getContext())
                .load(Singleton.getInstance().getFairData().getSystemUrl().getAvatarUrl() + Singleton.getInstance().getLoginData().getUser().getProfileImage())
                .apply(RequestOptions.circleCropTransform())
                .into(userImageView);


    }

    private final void focusOnView(EditText edt) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, edt.getBottom());
            }
        });

    }

    private boolean checkRequired(ViewModel viewModel, int i) {
        if (viewModel.getRequired().equals("Y")) {
            if (viewModel.getType().equals("text")) {
                if (viewModel.getEditText().getText().toString().equals("")) {
                    viewModel.getTitle().setTextColor(getResources().getColor(R.color.required_fileds));
                    viewModel.getEditText().requestFocus();
                    Helper.showToast("Please enter " + viewModel.getTitle().getText().toString());
                    focusOnView(viewModel.getEditText());
                    return false;
                } else {
                    viewModel.getTitle().setTextColor(getResources().getColor(R.color.white));

                }

            } else if (viewModel.getType().equals("dropDown")) {

                if (viewModel.getValue() == null || viewModel.getValue().equals("")) {
                    viewModel.getTitle().requestFocus();
                    viewModel.getTitle().setTextColor(getResources().getColor(R.color.required_fileds));
                    // viewModel.getEditText().requestFocus();
                    return false;
                } else
                    viewModel.getTitle().setTextColor(getResources().getColor(R.color.white));

            }
        }
        if (viewModel.getAutoId() != null && viewModel.getExtra()) {
            if (viewModel.getType().equals("text"))
                if (!selectedOptions.contains(viewModel.getAutoId() + "," + viewModel.getEditText().getText())) {
                    if (viewModel.getEditText().toString().equalsIgnoreCase("")) {
                        SelectedOptions selectedOptions1 = new SelectedOptions(viewModel.getAutoId() + "," + " ", "text");
                        selectedOptions.add(selectedOptions1);
                    } else {
                        SelectedOptions selectedOptions1 = new SelectedOptions(viewModel.getAutoId() + "," + viewModel.getEditText().getText(), "text");
                        selectedOptions.add(selectedOptions1);
                    }
                }
        }

        return true;
    }

    private boolean getValues() {
        for (int i = 0; i < selectedOptions.size(); i++) {
            if (selectedOptions.get(i).getType().equals("text")) {
                selectedOptions.remove(i);
                i--;
            }
        }
        for (int i = 0; i < dynamicViews.size(); i++) {
            if (!checkRequired(dynamicViews.get(i), i)) {
                return false;
            } else {
                if (i == (dynamicViews.size() - 1)) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }

    private void uploadCv() {
        camera = false;
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


        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");

        startActivityForResult(intent, PICKFILE_RESULT_CODE);


    }

    private void openEditPictureDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_picture, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        Button cameraBtn = dialogView.findViewById(R.id.cameraBtn);
        Button galleryBtn = dialogView.findViewById(R.id.galleryBtn);

        Helper.setButtonColorWithDrawable(cameraBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(), Singleton.getInstance().getFairData().getFair().getOptions().getReceptionButtonsInnerTextColor());
        Helper.setButtonColorWithDrawable(galleryBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(), Singleton.getInstance().getFairData().getFair().getOptions().getReceptionButtonsInnerTextColor());

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera = false;
                alertDialog.cancel();

                ImagePicker.with(userProfile)
                        .cropSquare()
                        .galleryOnly()//Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(720, 720)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera = true;
                alertDialog.cancel();

                ImagePicker.with(userProfile)
                        .cropSquare()
                        .cameraOnly()
                        .compress(1024)
                        .maxResultSize(720, 720)
                        .start();
                //Final image resolution will be less than 1080 x 1080(Optional)
//Final image size will be less than 1 MB(Optional)
                //   .start();

//
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                try {
//                    startActivityForResult(takePictureIntent, 1111);
//                } catch (ActivityNotFoundException e) {
//                    // display error state to the user
//                }


            }
        });
        alertDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2404:
                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    Uri uri = data.getData();
                    // camera = false;
                    Bitmap bitmap2 = null;
                    try {
                        bitmap2 = MediaStore.Images.Media.getBitmap(Singleton.getInstance().getContext().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updateImage(bitmap2, data);
                }
                break;
            case 0:
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    savedData = data;

                    Uri selectedImage = data.getData();
                    Bitmap bitmap2 = null;
                    try {
                        bitmap2 = MediaStore.Images.Media.getBitmap(Singleton.getInstance().getContext().getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updateImage(bitmap2, data);
                }
                break;
            case PICKFILE_RESULT_CODE:
                if (camera) {
                    savedData = data;
                    Bitmap bitmap3 = (Bitmap) data.getExtras().get("data");
                    picUri = data.getData();
                    updateImage(bitmap3, data);
                } else if (resultCode == resultCode) {
                    if (data != null) {
                        String filePath = getDriveFilePath(data.getData(), Singleton.getInstance().getActivity());
                        if (filePath == null)
                            break;
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
            case 111:
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                break;
        }
        Singleton.getInstance().setIsDashboard(true);

    }

    private String getDriveFilePath(Uri uri, Context context) {
        File file = null;
        try {
            Uri returnUri = uri;
            Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
            /*
             * Get the column indexes of the data in the Cursor,
             *     * move to the first row in the Cursor, get the data,
             *     * and display it.
             * */
            String name = "pic.jpg";
            try {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                name = (returnCursor.getString(nameIndex));
                String size = (Long.toString(returnCursor.getLong(sizeIndex)));
            } catch (Exception e) {
//                Helper.showToast("sd");

            }
            file = new File(context.getCacheDir(), name);

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
        if (file == null)
            return null;
        return file.getPath();
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
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission accepted.
                    openEditPictureDialog();
                } else {
                    Helper.showToast("Please give camera permission");
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", Singleton.getInstance().getContext().getPackageName(), String.valueOf(this)));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, 1);
                    //permission denied.
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void updateImage(Bitmap bitmap, Intent data) {

        stream = new ByteArrayOutputStream();
        String filePath;
        // if (camera) {
        //    filePath = getDriveFilePath(getImageUri(Singleton.getInstance().getContext(), bitmap), Singleton.getInstance().getActivity());
        //  } else
        filePath = getDriveFilePath(data.getData(), Singleton.getInstance().getActivity());

        String[] parsedpath = filePath.split("/");
        //now we will upload the file
        //lets import okhttp first
        fileName = parsedpath[parsedpath.length - 1];
        file = null;
        file = new File(filePath);

        // file=bitmapToFile(bitmap);
        Helper.showProgressBar(progressBar);

        new updateProfilePic().execute();

    }

    private void getFairExtraFieldsApi() {
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getSharedPreferences().getInt("fairId", 0));
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        Helper.showProgressBar(progressBar);
        Call<FairExtraFieldsResponse> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getFairExtraFields(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<FairExtraFieldsResponse>() {
            @Override
            public void onResponse(Call<FairExtraFieldsResponse> call, Response<FairExtraFieldsResponse> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {

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
        //  response.body().getData().getFair().setStartTime("2022-02-24 15:03:00");
        Singleton.getInstance().setExtraFieldsFairData(response.body().getFairData());
        Singleton.getInstance().getLoginData().getUser().setExtraFields(response.body().getFairData().getExtraFields());
        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
        Gson gson = new Gson();
        String json = gson.toJson(response.body().getFairData());
        editor.putString("fairFeildsData", json);

        editor.apply();
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
        } catch (Exception e) {
            Helper.showToast(":");
        }
    }

    @Override
    public void onStop() {
        try {
            super.onStop();
        } catch (Exception e) {
            Helper.showToast(":");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        try {
            super.onAttach(context);
        } catch (Exception e) {
            Helper.showToast(":");

        }
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
        } catch (Exception e) {
            Helper.showToast(":");

        }
    }

    @Override
    public void onDetach() {
        try {
            super.onDetach();
        } catch (Exception e) {
            Helper.showToast(":");

        }
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
            // pdfView.fromStream(inputStream).load();
        }
    }

    class updateProfilePic extends AsyncTask<String, Void, Login> {

        private Exception exception;

        protected com.vrd.gsaf.api.responses.login.Login doInBackground(String... urls) {


            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("candidate_id", Singleton.getInstance().getLoginData().getUser().getId())
                    .addFormDataPart("fair_id", String.valueOf(Singleton.getInstance().getFairData().getFair().getId()))
                    .addFormDataPart("profile_image", fileName,
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    new File(file.getPath())))
                    .build();

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            Request request = new Request.Builder()
                    .url(AppConstants.BASE_URL + "api/save/candidate/profile-image")
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
            try {
                if (response.isSuccessful()) {

                    com.vrd.gsaf.api.responses.login.Login responseResult = null;
                    Gson gson = new Gson();
                    com.vrd.gsaf.api.responses.login.Login login = null;
                    // Do something here
                    file = null;
                    login = gson.fromJson(response.body().string(), com.vrd.gsaf.api.responses.login.Login.class);
                    return login;
                } else {
                    String jsonString = response.body().string();
                    BaseResponse baseResponse = new Gson().fromJson(jsonString, BaseResponse.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Helper.showToast(baseResponse.getMsg());
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(com.vrd.gsaf.api.responses.login.Login feed) {
            // updateSp(feed.getData());
            Helper.hideProgressBar(progressBar);

            if (feed != null) {
                updateSp(feed.getData());
            }

            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Login> {

        JsonArray ex = new JsonArray();
        JsonObject js = new JsonObject();
        private Exception exception;

        protected com.vrd.gsaf.api.responses.login.Login doInBackground(String... urls) {

            if (Singleton.getInstance().getPostalCode() == null) {
                Singleton.getInstance().setPostalCode(Singleton.getInstance().getSharedPreferences().getString("postalCode", null));
            }

            if (Singleton.getInstance().getPostalCode() == null) {
                Singleton.getInstance().setPostalCode(" ");
            }

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("name", nameEdt.getText().toString())
                    .addFormDataPart("email", emailEdt.getText().toString())
                    .addFormDataPart("phone", phone)
                    .addFormDataPart("user_job_title", job)
                    .addFormDataPart("user_company", company)
                    .addFormDataPart("fair_id", String.valueOf(Singleton.getInstance().getFairData().getFair().getId()))
                    .addFormDataPart("city_name", cityEdit.getText().toString())
                    .addFormDataPart("country_name", countryEdit.getText().toString())
                    .addFormDataPart("postal_code", postalEdit.getText().toString())
                    .addFormDataPart("timezone", TimeZone.getDefault().getID())
                    .addFormDataPart("extra_fields", extraFields.toString())
                    .addFormDataPart("document", fileName,
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    file))
                    .build();


            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            Request request = new Request.Builder()
                    .url(AppConstants.BASE_URL + "api/save/candidate/profile")
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
                if (response != null && response.isSuccessful()) {
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
                    String jsonString = response.body().string();
                    BaseResponse baseResponse = new Gson().fromJson(jsonString, BaseResponse.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Helper.showToast(baseResponse.getMsg());
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }


        protected void onPostExecute(com.vrd.gsaf.api.responses.login.Login feed) {
            if (feed != null) {
                updateSp(feed.getData());
            }
            Helper.hideProgressBar(progressBar);
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }
}