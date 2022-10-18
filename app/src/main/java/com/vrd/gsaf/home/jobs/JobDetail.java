package com.vrd.gsaf.home.jobs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.appliedJobs.AppliedJobs;
import com.vrd.gsaf.api.responses.jobDetail.Data;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class JobDetail extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    LinearLayoutCompat emailLayout, contactNameLayout, salaryLayout, jobTypeLayout, locationLayout, languageLayout;
    CardView imageCardView;
    LinearLayout jobDetailsLayout;
    RelativeLayout toolbar;
    ConstraintLayout mainLayoutBack;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView, companyImageView;
    private TextView descriptionTxt, descriptionValueTxt, titleTxt, jobTitleTxt, salaryTxt, salaryValueTxt, jobTypeTxt,
            jobTypeValueTxt, locationTxt, locationValueTxt, emailTxt, emailValueTxt,
            contactNameTxt, contactNameValueTxt, languageTxt, languageValueTxt;
    private int index;
    private long mLastClickTime;
    private ConstraintLayout mainLayout;
    private Button applyBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        view = inflater.inflate(R.layout.fragment_job_detail, container, false);
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
        jobDetailsLayout = view.findViewById(R.id.jobsDetailsLayout);
        applyBtn = view.findViewById(R.id.applyBtn);
        Helper.setButtonColorWithDrawable(applyBtn,
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());

        mainLayout = view.findViewById(R.id.mainLayout);
        titleTxt = view.findViewById(R.id.titleTxt);

        companyImageView = view.findViewById(R.id.companyImageView);


        descriptionTxt = view.findViewById(R.id.descriptionTxt);
        descriptionValueTxt = view.findViewById(R.id.descriptionValueTxt);
        descriptionTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDescription2());
        descriptionTxt.setMovementMethod(LinkMovementMethod.getInstance());

        jobTitleTxt = view.findViewById(R.id.jobTitleTxt);
        salaryTxt = view.findViewById(R.id.salaryTxt);
        salaryValueTxt = view.findViewById(R.id.salaryValueTxt);
        jobTypeTxt = view.findViewById(R.id.jobTypeTxt);
        jobTypeValueTxt = view.findViewById(R.id.jobTypeValueTxt);
        locationTxt = view.findViewById(R.id.locationTxt);
        locationValueTxt = view.findViewById(R.id.locationValueTxt);
        emailTxt = view.findViewById(R.id.emailTxt);
        emailValueTxt = view.findViewById(R.id.emailValueTxt);
        contactNameTxt = view.findViewById(R.id.contactNameTxt);
        contactNameValueTxt = view.findViewById(R.id.contactNameValueTxt);
        setClickListeners();
        mainLayout.setVisibility(View.GONE);
        applyBtn.setVisibility(View.GONE);

        emailLayout = view.findViewById(R.id.emailLayout);
        contactNameLayout = view.findViewById(R.id.contactNameLayout);
        salaryLayout = view.findViewById(R.id.salaryLayout);
        languageValueTxt = view.findViewById(R.id.languageValueTxt);
        languageTxt = view.findViewById(R.id.languageTxt);
        jobTypeLayout = view.findViewById(R.id.jobTypeLayout);
        locationLayout = view.findViewById(R.id.locationLayout);
        languageLayout = view.findViewById(R.id.languageLayout);
        imageCardView = view.findViewById(R.id.cardView);
        toolbar = view.findViewById(R.id.toolbar);
        mainLayoutBack = view.findViewById(R.id.mainLayoutBack);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayoutBack.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        if (Helper.isInternetConnected()) {
            callApiForCompanyDetailWithNoAuth(false);
        }
        if (!Singleton.getInstance().getFromCoursesToJobDetails()) {
            titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJob() + " " +
                    Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDetail());
            Helper.loadRectangleImageFromUrlWithRounded(companyImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getJobsData().get(index).getCompanyInfo().getCompanyLogo(), 25);
            languageTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJob() + " " + "Languages :");

        } else {
            titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourse() + " " +
                    Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDetail());
            Helper.loadRectangleImageFromUrlWithRounded(companyImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getCoursesData().get(index).getCompanyInfo().getCompanyLogo(), 25);
            languageTxt.setText("Course Languages :");

        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_jobinformation_section_jobdetail_page() == 1) {
            jobDetailsLayout.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getEnable_company_logo_job_detail_page() != 1) {
            imageCardView.setVisibility(View.GONE);
        }

        retryLayout();

    }

    private void setData(Data data) {
        mainLayout.setVisibility(View.VISIBLE);
        salaryTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSalary() + " : ");
        jobTypeTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJob() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getType() + " : ");
        locationTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLocation() + " : ");
        emailTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEmail() + " : ");
        contactNameTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getName() + " : ");
        if (!Singleton.getInstance().getFromCoursesToJobDetails()) {
            if (data.getJobList().get(0).getUserApplied())
                applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApplied());
            else
                applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApply());

        } else {
            if (data.getJobList().get(0).getUserApplied())
                applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getInterested());
            else
                applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getShowInterest());

        }
        if (Singleton.getInstance().getIsLoggedIn()) {
            applyBtn.setVisibility(View.VISIBLE);
            if (data.getJobList().get(0).getUserApplied())
                applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApplied());
            else
                applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApply());
        } else
            applyBtn.setVisibility(View.GONE);

        jobTitleTxt.setText(data.getJobList().get(0).getJobTitle());
        salaryValueTxt.setText(data.getJobList().get(0).getJobSalary());
        jobTypeValueTxt.setText(data.getJobList().get(0).getJobType());
        locationValueTxt.setText(data.getJobList().get(0).getJobLocation());
        emailValueTxt.setText(data.getJobList().get(0).getJobEmail());
        contactNameValueTxt.setText(data.getJobList().get(0).getJobContactName());
        languageValueTxt.setText(data.getJobList().get(0).getJobLanguage());
        setTextViewHTML(descriptionValueTxt, data.getJobList().get(0).getJobDescription());
//        Helper.loadHtml(descriptionValueTxt, data.getJobList().get(0).getJobDescription());
//        descriptionValueTxt.setMovementMethod(LinkMovementMethod.getInstance());
        // descriptionTxt.setText(data.getJobList().get(0).getJobDescription());
        Helper.loadRectangleImageFromUrlWithRounded(companyImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + data.getJobList().get(0).getCompanyInfo().getCompanyLogo(), 25);


        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideJobLocation() == 1 ||
                Singleton.getInstance().getFairData().getFair().getOptions().getDisable_company_location_job_detail_front() == 1)
            locationLayout.setVisibility(View.GONE);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideJobSalary() == 1)
            salaryLayout.setVisibility(View.GONE);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideJobType() == 1)
            jobTypeLayout.setVisibility(View.GONE);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideJobEmail() == 1)
            emailLayout.setVisibility(View.GONE);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideJobContactName() == 1)
            contactNameLayout.setVisibility(View.GONE);

        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideJobLanguage() == 1 || data.getJobList().get(0).getJobLanguage().equals(""))
            languageLayout.setVisibility(View.GONE);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_applybtn_jobdetail_page().equals(1)) {
            applyBtn.setVisibility(View.GONE);
        }
//        descriptionValueTxt.setMovementMethod(new ScrollingMovementMethod());

    }

    private void retryLayout() {
        View retryLayout;
        retryLayout = view.findViewById(R.id.somethingWentWrongLayout);
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.isInternetConnected()) {
                    callApiForCompanyDetailWithNoAuth(false);
                }
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        applyBtn.setOnClickListener(this);
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
            case R.id.applyBtn:
                if (applyBtn.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApply()) ||
                        applyBtn.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getShowInterest()))
                    applyOnJob();
                else
                    Helper.showToast("Already Applied");
                break;

        }
    }

    private void applyOnJob() {

        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        if (!Singleton.getInstance().getFromCoursesToJobDetails()) {
            JsonObject.addProperty("job_id", Singleton.getInstance().getJobsData().get(index).getJobId());
            JsonObject.addProperty("company_id", Singleton.getInstance().getJobsData().get(index).getCompanyInfo().getCompanyId());

        } else {
            JsonObject.addProperty("job_id", Singleton.getInstance().getCoursesData().get(index).getJobId());
            JsonObject.addProperty("company_id", Singleton.getInstance().getCoursesData().get(index).getCompanyInfo().getCompanyId());

        }

        Call<AppliedJobs> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).applyJob(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<AppliedJobs>() {
            @Override
            public void onResponse(Call<AppliedJobs> call, Response<AppliedJobs> response) {
                Helper.hideProgressBar(progressBar);

                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.body().getStatus()) {
                        if (!Singleton.getInstance().getFromCoursesToJobDetails()) {
                            applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApplied());
                            Singleton.getInstance().getJobsData().get(index).setUserApplied(true);
                            Singleton.getInstance().getJobAdapter().updateData(index);

                        } else {
                            applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getInterested());
                            Singleton.getInstance().getCoursesData().get(index).setUserApplied(true);
                            Singleton.getInstance().getCoursesAdapter().updateData(index);


                        }
                        Helper.showToast("Applied");
                    } else if (response.body().getCode() == 406)
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
                    Helper.showToast("Something Went Wrong");
                }

            }

            @Override
            public void onFailure(Call<AppliedJobs> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something Went Wrong");
                //Helper.failureResponse(progressBar, getView());
            }

        });


    }


    @Override
    public void onResume() {
        //playVideo();
        super.onResume();
        try {
            Helper.setActiveItemBottomNav(0);

        } catch (Exception e) {

        }
        // playVideo();

    }

    private void callApiForCompanyDetailWithNoAuth(boolean pagination) {
        Helper.showProgressBar(progressBar);
        String url;
        if (!Singleton.getInstance().getFromCoursesToJobDetails()) {
            if (Singleton.getInstance().getIsLoggedIn())
                url = "api/front/job/detail/" + Singleton.getInstance().getJobsData().get(index).getJobId() + "/" + Singleton.getInstance().getLoginData().getUser().getId();
            else
                url = "api/front/job/detail/" + Singleton.getInstance().getJobsData().get(index).getJobId();
        } else {
            if (Singleton.getInstance().getIsLoggedIn())
                url = "api/front/job/detail/" + Singleton.getInstance().getCoursesData().get(index).getJobId() + "/" + Singleton.getInstance().getLoginData().getUser().getId();
            else
                url = "api/front/job/detail/" + Singleton.getInstance().getCoursesData().get(index).getJobId();
        }


        Call<com.vrd.gsaf.api.responses.jobDetail.JobDetail> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getJobDetailWithNoAuth(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.jobDetail.JobDetail>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.jobDetail.JobDetail> call, Response<com.vrd.gsaf.api.responses.jobDetail.JobDetail> response) {
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
                        if (!Singleton.getInstance().getFromCoursesToJobDetails())
                            setData(response.body().getData());
                        else
                            setCoursesDetail(response.body().getData());
                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.jobDetail.JobDetail> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
            }

        });

    }

    private void setCoursesDetail(Data data) {

        mainLayout.setVisibility(View.VISIBLE);

        salaryTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourseFee() + " : ");
        jobTypeTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourse() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getType() + " : ");
        locationTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLocation() + " : ");
        emailTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEmail() + " : ");
        contactNameTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getName() + " : ");

        if (data.getJobList().get(0).getUserApplied())
            applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getInterested());
        else
            applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getShowInterest());


        if (Singleton.getInstance().getIsLoggedIn()) {
            applyBtn.setVisibility(View.VISIBLE);
            if (data.getJobList().get(0).getUserApplied())
                applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getInterested());
            else
                applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getShowInterest());
        } else
            applyBtn.setVisibility(View.GONE);

        jobTitleTxt.setText(data.getJobList().get(0).getJobTitle());
        salaryValueTxt.setText(data.getJobList().get(0).getJobSalary());
        jobTypeValueTxt.setText(data.getJobList().get(0).getJobType());
        locationValueTxt.setText(data.getJobList().get(0).getJobLocation());
        emailValueTxt.setText(data.getJobList().get(0).getJobEmail());
        contactNameValueTxt.setText(data.getJobList().get(0).getJobContactName());
        languageValueTxt.setText(data.getJobList().get(0).getJobLanguage());
//        descriptionValueTxt.setText(result);
        setTextViewHTML(descriptionValueTxt, data.getJobList().get(0).getJobDescription());
//        Helper.loadHtml(descriptionValueTxt, data.getJobList().get(0).getJobDescription());
//        descriptionValueTxt.setMovementMethod(LinkMovementMethod.getInstance());
        // descriptionTxt.setText(data.getJobList().get(0).getJobDescription());
        Helper.loadRectangleImageFromUrlWithRounded(companyImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + data.getJobList().get(0).getCompanyInfo().getCompanyLogo(), 25);


        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideCourseLocation() == 1)
            locationLayout.setVisibility(View.GONE);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideCourseSalary() == 1)
            salaryLayout.setVisibility(View.GONE);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideCourseType() == 1)
            jobTypeLayout.setVisibility(View.GONE);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideCourseEmail() == 1)
            emailLayout.setVisibility(View.GONE);

        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideCourseContactName() == 1)
            contactNameLayout.setVisibility(View.GONE);

        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideCourseLanguage() == 1 || data.getJobList().get(0).getJobLanguage().equals(""))
            languageLayout.setVisibility(View.GONE);

//        descriptionValueTxt.setMovementMethod(new ScrollingMovementMethod());
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_applybtn_jobdetail_page().equals(1)) {
            applyBtn.setVisibility(View.GONE);
        }
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(span.getURL()));
                requireActivity().startActivity(intent);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    protected void setTextViewHTML(TextView text, String html) {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());


    }
}