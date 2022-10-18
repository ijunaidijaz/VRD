package com.vrd.gsaf.home.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.fragments.MatchingFragment;
import com.vrd.gsaf.home.companies.Companies;
import com.vrd.gsaf.home.conferenceAgenda.ConferenceAgenda;
import com.vrd.gsaf.home.courses.Courses;
import com.vrd.gsaf.home.dashboard.careeTest.CareerTest;
import com.vrd.gsaf.home.dashboard.profile.UserProfile;
import com.vrd.gsaf.home.dashboard.recruiters.Recruiters;
import com.vrd.gsaf.home.dashboard.resume.Resume;
import com.vrd.gsaf.home.dashboard.schedules.Schedules;
import com.vrd.gsaf.home.dashboard.uploadTask.UploadTask;
import com.vrd.gsaf.home.jobs.Jobs;
import com.vrd.gsaf.singleton.Singleton;

import java.util.List;

public class DashBoardAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final List<DashBoardModel> dataList;
    private final Context context;
    public FragmentManager view;
    private long mLastClickTime = System.currentTimeMillis();

    public DashBoardAdapter(Context context, FragmentManager view) {
        this.context = context;
        this.dataList = Singleton.getInstance().getDashboardData();
        this.view = view;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_dashboard, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.name.setText(dataList.get(position).getName());
        holder.icon.setImageDrawable(dataList.get(position).getIcon());
        try {
            Drawable drawable = dataList.get(position).getIcon();
            drawable.setColorFilter(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarInnerTextColor()), PorterDuff.Mode.MULTIPLY);
            holder.icon.setImageDrawable(drawable);
        } catch (Exception e) {
            Log.d("Color Exception", e.toString());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (holder.name.getText().toString().matches(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getProfile()))
                    replaceFragment(new UserProfile(), null);
                else if (holder.name.getText().toString().matches(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getRecruiters()))
                    replaceFragment(new Recruiters(), null);
                else if (holder.name.getText().toString().matches(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaCareertest()))
                    replaceFragment(new CareerTest(), null);
                else if (holder.name.getText().toString().matches(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getResume()))
                    replaceFragment(new Resume(), null);
                else if (holder.name.getText().toString().matches(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getUpload() + " " +
                        Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTask()))
                    replaceFragment(new UploadTask(), null);
                else if (holder.name.getText().toString().matches(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSchedules()))
                    replaceFragment(new Schedules(), null);
                else if (holder.name.getText().toString().matches(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJobs())) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromDashboard", true);
                    Fragment jobFragment = new Jobs();
                    jobFragment.setArguments(bundle);
                    replaceFragment(jobFragment, "jobs");
                } else if (holder.name.getText().toString().matches(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourses())) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromDashboard", true);
                    Fragment courses = new Courses();
                    courses.setArguments(bundle);
                    replaceFragment(courses, "jobs");
                } else if (holder.name.getText().toString().matches(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaWebinars())) {
                    replaceFragment(new ConferenceAgenda(), null);
                    Singleton.getInstance().setWebinarVideo(false);

                    Singleton.getInstance().setFromStandToWebinar(false);
                    Singleton.getInstance().setFromSpeakersToWebinar(false);
                    Singleton.getInstance().setIsDashboard(true);
                } else if (holder.name.getText().toString().matches(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCompanies())) {
                    replaceFragment(new Companies(), null);
                    Singleton.getInstance().setIsDashboard(true);
                } else if (holder.name.getText().toString().matches(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMatchingSlots())) {
                    Singleton.getInstance().setFromStandToMatchingSlots(false);
                    replaceFragment(new MatchingFragment(), 0);
                }
            }
        });


    }

    private void replaceFragment(Fragment fragment, String tag) {
        Singleton.getInstance().getMainActivity().replaceFragment(fragment, tag);
    }

    private void replaceFragment(Fragment fragment, int index) {

//        Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getRecruiterArrayList().get(index).getCompany().getCompanyId());

        FragmentManager fm = view;
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fm.beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}


class CustomViewHolder extends RecyclerView.ViewHolder {

    private final View mView;
    TextView name;
    ImageView icon;
    CardView cardView;
    ConstraintLayout inner_card;

    @SuppressLint("ResourceType")
    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        name = mView.findViewById(R.id.nameTxt);
        cardView = mView.findViewById(R.id.cardViewParent);
        icon = mView.findViewById(R.id.iconImageView);
        cardView = mView.findViewById(R.id.cardViewParent);
        inner_card = mView.findViewById(R.id.inner_card);
        GradientDrawable drawable = (GradientDrawable) inner_card.getBackground();
        drawable.setStroke(3, Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarInnerTextColor()));
        cardView.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
        drawable.setColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
        name.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarInnerTextColor()));
//        icon.setColorFilter(ContextCompat.getColor(Singleton.getInstance().getActivity(), Color.RED), android.graphics.PorterDuff.Mode.MULTIPLY);;
//        inner_card.getBackground().setColorFilter(Color.parseColor(Co), PorterDuff.Mode.SRC_ATOP);
//        Drawable background = inner_card.getBackground();
//        if (background instanceof ShapeDrawable) {
//            ((ShapeDrawable)background).getPaint().setColor(ContextCompat.getColor(MainApp.getAppContext(),Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor());
//        }
//        cardView.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

    }
}
