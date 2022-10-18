package com.vrd.gsaf.home.courses;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.appliedJobs.AppliedJobs;
import com.vrd.gsaf.api.responses.jobPercentage.JobsMatchPercentage;
import com.vrd.gsaf.api.responses.jobPercentage.Match;
import com.vrd.gsaf.api.responses.jobs.Job;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.companies.companyStand.CompnayStand;
import com.vrd.gsaf.home.jobs.CompanyDetail;
import com.vrd.gsaf.home.jobs.JobDetail;
import com.vrd.gsaf.home.jobs.JobMatchAdapter;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final List<Job> dataList;
    private final ProgressBar progressBar;
    public FragmentManager view;
    private List<Job> filteredDataList;
    private Context context;
    private long mLastClickTime = System.currentTimeMillis();


    public CoursesAdapter(FragmentManager view, ProgressBar progressBar) {
        this.dataList = Singleton.getInstance().getCoursesData();
        this.filteredDataList = Singleton.getInstance().getCoursesData();
        this.view = view;
        this.progressBar = progressBar;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_courses, parent, false);
        return new CustomViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        //  holder.courseInterestBtn.setVisibility(View.VISIBLE);
        if (filteredDataList.get(position).getJobType() != "")
            holder.jobTypeBanner.setText(filteredDataList.get(position).getJobType());
        holder.courseTitle.setText(filteredDataList.get(position).getJobTitle());
        holder.courseFee.setText(filteredDataList.get(position).getJobSalary());
        holder.location.setText(filteredDataList.get(position).getJobLocation());
//        Glide.with(Singleton.getInstance().getContext()).load(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getCoursesData().get(position).getCompanyInfo().getCompanyLogo())
//                .placeholder(R.drawable.add_emoji)
//                .apply(RequestOptions.circleCropTransform())
//                .error(R.drawable.add_emoji)
//                .into(holder.courseImage);

        Helper.loadRectangleImageFromUrlWithRounded(holder.courseImage, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getCoursesData().get(position).getCompanyInfo().getCompanyLogo(), 25);


        if (Singleton.getInstance().getIsLoggedIn()) {
            holder.courseInterestBtn.setVisibility(View.VISIBLE);
            if (filteredDataList.get(position).getUserApplied())
                holder.courseInterestBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getInterested());
            else
                holder.courseInterestBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getShowInterest());
        } else
            holder.courseInterestBtn.setVisibility(View.GONE);

        Helper.loadRectangleImageFromUrlWithRounded(holder.courseImage, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getCoursesData().get(position).getCompanyInfo().getCompanyLogo(), 25);
        if (!Singleton.getInstance().getIsLoggedIn() || Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("0"))
            holder.matchingLayout.setVisibility(View.GONE);
        else {
            holder.matchingLayout.setVisibility(View.VISIBLE);
            holder.matchingJobsValueTxt.setText(filteredDataList.get(position).getMatchPercentage() + "%");
        }

        if (Singleton.getInstance().getHomeState() != null && Singleton.getInstance().getHomeState().equals("stands")) {
            holder.companyName.setVisibility(View.GONE);
        }

        int index = position;

        // holder.courseImage.setImageDrawable(dataList.get(position).getCompany().getCompanyLogo());
        holder.companyName.setPaintFlags(holder.companyName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.companyName.setText(filteredDataList.get(index).getCompanyInfo().getCompanyName());

        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideVisitSiteFront() == 0) {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideCourseWebsite() == 0) {
                if (!filteredDataList.get(position).getJobUrl().equals("")) {
                    holder.webisteNameLayout.setVisibility(View.VISIBLE);
                    holder.websiteTxt.setPaintFlags(holder.websiteTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    holder.webisteNameLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Helper.goToUrl(filteredDataList.get(index).getJobUrl());
                        }
                    });
                }
            }
        }
        holder.companyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableJobDetailPage().equals(0) && Singleton.getInstance().getFairData().getFair().getOptions().getDisable_hyperlink_on_company_name_courses_list().equals(0)) {
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    mLastClickTime = now;
                    // replaceFragment(new CompanyDetail(), null, index);


                    if (Singleton.getInstance().getLive() && Singleton.getInstance().getIsLoggedIn())
                        replaceFragment(new CompnayStand(), null, index);

                    else {
                        replaceFragment(new CompanyDetail(), null, index);
                    }
                }
            }
        });

        holder.courseInterestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (!holder.courseInterestBtn.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getInterested())) {
                    if (Helper.isInternetConnected()) {
                        callApiToApplyJob(index, holder);
                    }
                } else {
                    Helper.showToast("You have already applied for this course");

                }
            }
        });


        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableJobDetailPage() == 0) {
            //  holder.courseTitle.setTextColor(R.color.link_color);
            // holder.courseTitle.setText(Html.fromHtml("<u>Text to underline</u>"));
            // holder.courseTitle.setPaintFlags(0);

//            SpannableString content = new SpannableString( holder.courseTitle.getText());
//            content.setSpan(new UnderlineSpan(), 0, holder.courseTitle.getText().length(), 0);
//            holder.courseTitle.setText(content);

            holder.courseTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    mLastClickTime = now;
                    Singleton.getInstance().setFromCoursesToJobDetails(true);
                    replaceFragment(new JobDetail(), null, index);

                }
            });
        }

        holder.matchingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiForMatchPercentage(dataList.get(index).getJobId());


            }
        });
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_match_percentage_job_list_front().equals(1)) {
            holder.matchingLayout.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_hyperlink_on_company_name_courses_list().equals(1)) {
            holder.companyName.setPaintFlags(0);
            holder.companyName.setTextColor(R.color.black);
            holder.companyName.setClickable(false);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_company_name_list_front().equals(1)) {
            holder.companyName.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_applybtn_jobdetail_page().equals(1)) {
            holder.courseInterestBtn.setVisibility(View.GONE);
        }
    }


    public void callApiForMatchPercentage(int jobId) {

        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("match_param", "job");
        JsonObject.addProperty("job_id", jobId);


        Call<JobsMatchPercentage> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getJobPercentage(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<JobsMatchPercentage>() {
            @Override
            public void onResponse(Call<JobsMatchPercentage> call, Response<JobsMatchPercentage> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {
                        Helper.showToast("No Data Found");

                    } else if (response.body().getStatus()) {
                        showMatchDialog(response.body().getData().getMatchList());
                    }
                } catch (Exception e) {
                    Helper.showToast("Something Went Wrong");

                }

            }

            @Override
            public void onFailure(Call<JobsMatchPercentage> call, Throwable t) {
                Helper.showToast("Something Went Wrong");
                Helper.hideProgressBar(progressBar);
            }

        });

    }


    private void showMatchDialog(List<Match> matchList) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_job_matchpercentsge, null);
        RecyclerView recyclerView;
        JobMatchAdapter adapter;
        dialogBuilder.setView(dialogView);
        dialogView.requestLayout();
        AlertDialog alertDialog = dialogBuilder.create();
        //    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textView = dialogView.findViewById(R.id.titleTxt);
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMatch() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDetail());
        //textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAre());


        adapter = new JobMatchAdapter(matchList);
        recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ImageView crossImageView = dialogView.findViewById(R.id.crossButtonsLayoutImageView);

        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                alertDialog.cancel();


            }
        });


//
//        if (alertDialog.getWindow() != null){
//            alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        }
        //alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        //  alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
//


//        InputMethodManager imm = (InputMethodManager) Singleton.getInstance().getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);


    }

    private void callApiToApplyJob(int position, CustomViewHolder holder) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        //   if (Singleton.getInstance().getIsDashboard())
        JsonObject.addProperty("job_id", Singleton.getInstance().getCoursesData().get(position).getJobId());
        //  else
        //    JsonObject.addProperty("job_id", Singleton.getInstance().getJobsData().get(position).getJobId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getCoursesData().get(position).getCompanyInfo().getCompanyId());
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
                        //holder.applyBtn.setVisibility(View.GONE);
                        holder.courseInterestBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getInterested());
                        // Singleton.getInstance().getRecruiterArrayList().addAll(response.body().getData().getRecruiterList());
                        Helper.showToast("Applied");

                    } else
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


    private void replaceFragment(Fragment fragment, String tag, int index) {

        Singleton.getInstance().setStandCompanyId(filteredDataList.get(index).getCompanyInfo().getCompanyId());


        FragmentManager fm = view;
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fm.beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();

    }

    public android.widget.Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String[] list = charSequence.toString().split(",");

                String title = list[0];
                String location = list[1];
                //  String charString2 = jobs.searchJobLocationEdt.getText().toString();

                //  String charString2 = jobs.searchJobLocationEdt.getText().toString();
                if (title.equals(" ") && location.equals(" ")) {
                    filteredDataList = dataList;
                } else {
                    List<Job> filteredList = new ArrayList<>();
                    for (Job row : dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (!title.equals(" ") && !location.equals(" ")) {
                            if (row.getJobTitle().toLowerCase().contains(title.toLowerCase()) && row.getJobLocation().toLowerCase().contains(location.toLowerCase()))
                                filteredList.add(row);

                        } else if (title.equals(" ")) {
                            if (row.getJobLocation().toLowerCase().contains(location.toLowerCase()))
                                filteredList.add(row);
                        } else if (location.equals(" ")) {
                            if (row.getJobTitle().toLowerCase().contains(title.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }


                    }

                    filteredDataList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataList = (List<Job>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }

    public void clearData() {
        if (dataList != null) {
            dataList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateData(int i) {
        filteredDataList.get(i).setUserApplied(true);
        notifyDataSetChanged();
    }
}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    TextView companyName, matchingJobsTxt, matchingJobsValueTxt, websiteTxt;
    TextView courseTitle, jobTypeBanner;
    TextView courseFee, courseFeeTxt;
    TextView location, locationTxt;
    ImageView courseImage, locationIcon, matchingIcon, salaryIcon, webIcon;
    Button courseInterestBtn;
    LinearLayoutCompat matchingLayout, locationLayout, courseFeeLayout, webisteNameLayout;


    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        jobTypeBanner = mView.findViewById(R.id.jobTypeBanner);
        matchingLayout = mView.findViewById(R.id.matchingLayout);
        locationTxt = mView.findViewById(R.id.locationTxt);
        courseFeeTxt = mView.findViewById(R.id.courseFeeTxt);
        websiteTxt = mView.findViewById(R.id.websiteTxt);
        webisteNameLayout = mView.findViewById(R.id.webisteNameLayout);
        locationLayout = mView.findViewById(R.id.locationLayout);
        courseFeeLayout = mView.findViewById(R.id.courseFeeLayout);

        courseTitle = mView.findViewById(R.id.courseTitleTxt);
        courseFee = mView.findViewById(R.id.courseFeeValueTxt);
        location = mView.findViewById(R.id.locationValueTxt);
        companyName = mView.findViewById(R.id.companyNameTxt);
        courseImage = mView.findViewById(R.id.courseImageView);
        courseInterestBtn = mView.findViewById(R.id.courseInterestBtn);

        matchingLayout = mView.findViewById(R.id.matchingLayout);
        matchingJobsTxt = mView.findViewById(R.id.matchingJobsTxt);
        matchingJobsTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMatch() + " :");
        matchingJobsValueTxt = mView.findViewById(R.id.matchingJobsValueTxt);

        ConstraintLayout mainLayout = mView.findViewById(R.id.parentLayout);
        webIcon = mView.findViewById(R.id.webIcon);
        locationIcon = mView.findViewById(R.id.locationIcon);
        salaryIcon = mView.findViewById(R.id.salaryIcon);
        matchingIcon = mView.findViewById(R.id.matchingIcon);
        mainLayout.setBackgroundColor(Singleton.getCardBGColor());

        Helper.setCardTextColor(locationTxt);
        Helper.setCardTextColor(courseFee);
        Helper.setCardTextColor(courseFeeTxt);
        Helper.setCardTextColor(matchingJobsTxt);
        Helper.setCardTextColor(matchingJobsValueTxt);


        Helper.setButtonColorWithDrawable(courseInterestBtn,
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());


        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideCourseLocation() == 1)
            locationLayout.setVisibility(View.GONE);

        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideCourseSalary() == 1)
            courseFeeLayout.setVisibility(View.GONE);
//        if (Singleton.getInstance().getFairData().getFair().getOptions().getCourseF() == 1)
//            courseFeeLayout.setVisibility(View.GONE);

        websiteTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getVisitOurSite());
        locationTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLocation() + " : ");
        courseFeeTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourseFee() + " : ");

    }
}


