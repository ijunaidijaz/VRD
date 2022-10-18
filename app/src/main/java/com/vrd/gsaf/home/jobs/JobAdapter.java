package com.vrd.gsaf.home.jobs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.api.responses.jobs.Job;
import com.vrd.gsaf.home.companies.companyStand.CompnayStand;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class JobAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final List<Job> dataList;
    private final Jobs jobs;
    public FragmentManager view;
    onApplyClick mCallback;
    private List<Job> filteredDataList;
    private Context context;
    private long mLastClickTime = System.currentTimeMillis();

    public JobAdapter(FragmentManager view, Jobs jobs, onApplyClick onApplyClick) {
        this.dataList = Singleton.getInstance().getJobsData();
        this.filteredDataList = Singleton.getInstance().getJobsData();
        this.jobs = jobs;
        this.view = view;
        this.mCallback = onApplyClick;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_jobs, parent, false);
        return new CustomViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {


        if (filteredDataList.get(position).getJobType() != "")
            holder.jobTypeBanner.setText(filteredDataList.get(position).getJobType());
        if (Singleton.getInstance().getIsLoggedIn() && Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("1")) {
            holder.matchingLayout.setVisibility(View.VISIBLE);
            holder.matchingJobsValueTxt.setText(filteredDataList.get(position).getMatchPercentage() + "%");
//            if (!filteredDataList.get(position).getUserApplied()) {
//                holder.applyBtn.setVisibility(View.VISIBLE);
//            } else {
//                holder.applyBtn.setVisibility(View.VISIBLE);
//                holder.applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApplied());
//            }
        }

        if (Singleton.getInstance().getIsLoggedIn()) {
            holder.applyBtn.setVisibility(View.VISIBLE);
            if (filteredDataList.get(position).getUserApplied())
                holder.applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApplied());
            else
                holder.applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApply());
        } else
            holder.applyBtn.setVisibility(View.GONE);

        if (Singleton.getInstance().getHomeState() != null && Singleton.getInstance().getHomeState().equals("stands")) {
            //holder.companyTxt.setVisibility(View.GONE);
            holder.companyNameLayout.setVisibility(View.GONE);

        }

        if (!Singleton.getInstance().getIsLoggedIn() || Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("0"))
            holder.matchingLayout.setVisibility(View.GONE);


        holder.jobTitle.setText(filteredDataList.get(position).getJobTitle());
        holder.salary.setText(filteredDataList.get(position).getJobSalary());
        holder.location.setText(filteredDataList.get(position).getJobLocation());


//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions = requestOptions.transforms(new CenterInside(), new RoundedCorners(15));

        Helper.loadRectangleImageFromUrlWithRounded(holder.companyImage, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getJobsData().get(position).getCompanyInfo().getCompanyLogo(), 25);
//        Glide.with(Singleton.getInstance().getContext()).
//                load(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getJobsData().get(position).getCompanyInfo().getCompanyLogo())
//                .placeholder(R.drawable.rectangluar_placeholder)
//                .apply(requestOptions)
//                .into(holder.companyImage);


        // holder.companyImage.setImageDrawable(dataList.get(position).getImage());
        holder.companyTxt.setPaintFlags(holder.companyTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.companyTxt.setText(filteredDataList.get(position).getCompanyInfo().getCompanyName());
        int index = position;
        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideVisitSiteFront() == 0) {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideJobWebsite() == 0) {
                if (filteredDataList.get(position).getJobUrl() != null && !filteredDataList.get(position).getJobUrl().equals("")) {
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
        holder.companyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableJobDetailPage().equals(0)) {
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    mLastClickTime = now;


                    if (Singleton.getInstance().getLive() && Singleton.getInstance().getIsLoggedIn())
                        replaceFragment(new CompnayStand(), null, index);

                    else {
                        replaceFragment(new CompanyDetail(), null, index);
                    }
                    //  replaceFragment(new CompanyDetail(), null, index);
                }
            }
        });


        holder.applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (!holder.applyBtn.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApplied()))
                    mCallback.applyToJob(index, holder);
                else
                    Helper.showToast("You have already applied for this job");
            }
        });
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableJobDetailPage() == 0) {
            // holder.jobTitle.setTextColor(R.color.link_color);
            // holder.courseTitle.setText(Html.fromHtml("<u>Text to underline</u>"));
            // holder.courseTitle.setPaintFlags(0);

            // SpannableString content = new SpannableString(holder.jobTitle.getText());
            //   content.setSpan(new UnderlineSpan(), 0, holder.jobTitle.getText().length(), 0);
            //   holder.jobTitle.setText(content);
            holder.jobTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    mLastClickTime = now;
                    Singleton.getInstance().setFromCoursesToJobDetails(false);

                    replaceFragment(new JobDetail(), null, index);

                }
            });
        }

        holder.matchingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.matchPercentage(dataList.get(index).getJobId());

            }
        });
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_match_percentage_job_list_front().equals(1)) {
            holder.matchingLayout.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_hyperlink_on_company_name_courses_list().equals(1)) {
            holder.webisteNameLayout.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_company_name_list_front().equals(1)) {
            holder.companyTxt.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_applybtn_jobdetail_page().equals(1)) {
            holder.applyBtn.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_hyperlink_on_company_name_courses_list().equals(1)) {
            holder.companyTxt.setPaintFlags(0);
            holder.companyTxt.setTextColor(R.color.black);
            holder.companyTxt.setClickable(false);
        }
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


    public void updateData(int i) {
        filteredDataList.get(i).setUserApplied(true);
        notifyDataSetChanged();
    }

    public android.widget.Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String[] list = charSequence.toString().split(",");
                String title = list[0];
                String location = list[1];
                //  String charString2 = jobs.searchJobLocationEdt.getText().toString();
                if (title.equals(" ") && location.equals(" ")) {
                    filteredDataList = dataList;
                } else {
                    List<Job> filteredList = new ArrayList<>();
                    for (Job row : dataList) {
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface onApplyClick {
        void applyToJob(int videoIndex, @Nullable CustomViewHolder holder);

        void matchPercentage(int companyId);
    }


}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;
    TextView jobTypeBanner, salaryTxt, locationTxt, matchingJobsTxt, matchingJobsValueTxt, jobTitle, salary, location, companyTxt, websiteTxt;
    ImageView companyImage, locationIcon, matchingIcon, salaryIcon, webIcon;
    LinearLayoutCompat matchingLayout, companyNameLayout, locationLayout, salaryLayout, webisteNameLayout;
    Button applyBtn;

    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        jobTypeBanner = mView.findViewById(R.id.jobTypeBanner);
        jobTitle = mView.findViewById(R.id.jobTitleTxt);
        webisteNameLayout = mView.findViewById(R.id.webisteNameLayout);
        applyBtn = mView.findViewById(R.id.applyBtn);
        companyNameLayout = mView.findViewById(R.id.companyNameLayout);
        locationLayout = mView.findViewById(R.id.locationLayout);
        salaryLayout = mView.findViewById(R.id.salaryLayout);
        websiteTxt = mView.findViewById(R.id.websiteTxt);

        matchingLayout = mView.findViewById(R.id.matchingLayout);
        matchingJobsTxt = mView.findViewById(R.id.matchingJobsTxt);
        matchingJobsTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMatch() + " :");
        websiteTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getVisitOurSite());
        matchingJobsValueTxt = mView.findViewById(R.id.matchingJobsValueTxt);

        salary = mView.findViewById(R.id.salaryValueTxt);
        salaryTxt = mView.findViewById(R.id.salaryTxt);
        salaryTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSalary() + " :");

        location = mView.findViewById(R.id.locationValueTxt);
        locationTxt = mView.findViewById(R.id.locationTxt);
        locationTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLocation() + " :");
        applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApply());

        companyTxt = mView.findViewById(R.id.companyTxt);
        companyImage = mView.findViewById(R.id.companyImageView);

        webIcon = mView.findViewById(R.id.webIcon);
        locationIcon = mView.findViewById(R.id.locationIcon);
        salaryIcon = mView.findViewById(R.id.salaryIcon);
        matchingIcon = mView.findViewById(R.id.matchingIcon);
        ConstraintLayout mainLayout = mView.findViewById(R.id.mainLayout);
        mainLayout.setBackgroundColor(Singleton.getCardBGColor());

        Helper.setCardTextColor(locationTxt);
        Helper.setCardTextColor(salaryTxt);
        Helper.setCardTextColor(matchingJobsTxt);
        Helper.setCardTextColor(matchingJobsValueTxt);

        Singleton.changeVectorIconColor(R.drawable.salary, salaryIcon, Singleton.getSidebarInnerTextColor());
        Singleton.changeVectorIconColor(R.drawable.location, locationIcon, Singleton.getSidebarInnerTextColor());
        Singleton.changeVectorIconColor(R.drawable.match, matchingIcon, Singleton.getSidebarInnerTextColor());
        Singleton.changeVectorIconColor(R.drawable.web, webIcon, Singleton.getSidebarInnerTextColor());


        Helper.setButtonColorWithDrawable(applyBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());

        // Helper.setButtonBackgroundAndTextColor(
        // ,
//                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
//                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());


        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideJobLocation() == 1)
            locationLayout.setVisibility(View.GONE);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getHideJobSalary() == 1)
            salaryLayout.setVisibility(View.GONE);


    }
}


