package com.vrd.gsaf.home.dashboard;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;


public class DashBoard extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    private View view;
    private ProgressBar progressBar;
    private ImageView menuImageView;
    private RecyclerView recyclerView;
    private DashBoardAdapter adapter;
    private ConstraintLayout colorLayout;
    private TextView titleTxt;
    private RelativeLayout toolbar;
    private ConstraintLayout mainLayout;
    private long mLastClickTime;


    @Override
    public void onResume() {
        super.onResume();
        try {
            Helper.setActiveItemBottomNav(0);
        } catch (Exception e) {

        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Helper.lockOrientation();

        view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        try {
            titleTxt = view.findViewById(R.id.titleTxt);
            titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDashboard());
            progressBar = view.findViewById(R.id.progressBar);
            menuImageView = view.findViewById(R.id.menuImageView);
            recyclerView = view.findViewById(R.id.recyclerView);
            colorLayout = view.findViewById(R.id.colorLayout);
            toolbar = view.findViewById(R.id.toolbar);
            toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
            colorLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
            // Helper.setbackgroundColor(colorLayout);
            setClickListeners();
        } catch (Exception e) {
            // Helper.showToast("initial views");
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        menuImageView.setOnClickListener(this);
        manageRecyclerView();
    }

    private void manageRecyclerView() {
        try {

            Singleton.getInstance().getDashboardData().clear();
            DashBoardModel model;


            if (Singleton.getInstance().getLive()) {
                if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableProfile() == 0) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getProfile(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.man));
                    Singleton.getInstance().setDashboardData(model);
                }

                if (Singleton.getInstance().getFairData().getFair().getOptions().getHideRecruitersTab() == 0) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getRecruiters(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.recruiter));
                    Singleton.getInstance().setDashboardData(model);
                }

                if (Singleton.getInstance().getFairData().getFair().getOptions().getHideJobsTab() == 0) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJobs(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.jobs));
                    Singleton.getInstance().setDashboardData(model);
                }

                if (Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("1")) {
                    if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableCareerTest() == 0) {
                        model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaCareertest(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.career_test));
                        Singleton.getInstance().setDashboardData(model);
                    }
                }
                if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableSchedules() == 0) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSchedules(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.calendar_alt));
                    Singleton.getInstance().setDashboardData(model);

                }
                if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableMatchingSlots() == 0 && Singleton.getInstance().getFairData().getFair().getOptions().getEnableSlotMatching() == 1) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMatchingSlots(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.ic_matching));
                    Singleton.getInstance().setDashboardData(model);

                }

                if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableResume() == 0 && !Singleton.getInstance().getFairData().getFair().getFairSetting().getCvRequired().equalsIgnoreCase("0")) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getResume(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.progile_dashboard));
                    Singleton.getInstance().setDashboardData(model);
                }


                if (Singleton.getInstance().getFairData().getFair().getOptions().getHideTasklistTab() == 0) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getUpload() + " " +
                            Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTask(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.uploadtask_dashboard));
                    Singleton.getInstance().setDashboardData(model);
                }

                if (Singleton.getInstance().getFairData().getFair().getOptions().getWebinarEnable() == 1) {
                    if (Singleton.getInstance().getFairData().getFair().getOptions().getHideWebinarsTab() == 0) {
                        model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaWebinars(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.webinars));
                        Singleton.getInstance().setDashboardData(model);
                    }
                }

                if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableCompanies() == 0 && Singleton.getInstance().getFairData().getFair().getOptions().getHideRecomendedCompaniesFront() == 0) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCompanies(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.companies));
                    Singleton.getInstance().setDashboardData(model);
                }

            }
            if (!Singleton.getInstance().getLive()) {
                if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableProfileDashboard() == 1) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getProfile(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.man));
                    Singleton.getInstance().setDashboardData(model);
                }

                if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableRecruitersTabDashboard() == 1) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getRecruiters(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.recruiter));
                    Singleton.getInstance().setDashboardData(model);
                }

                if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableJobsTabDashboard() == 1) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJobs(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.jobs));
                    Singleton.getInstance().setDashboardData(model);
                }

                if (Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("1")) {
                    if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableCareerTestDashboard() == 1) {
                        model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaCareertest(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.career_test));
                        Singleton.getInstance().setDashboardData(model);
                    }
                }

                if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableSchedulesDashboard() == 1) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSchedules(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.calendar_alt));
                    Singleton.getInstance().setDashboardData(model);
                }

                if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableResumeDashboard() == 1 && !Singleton.getInstance().getFairData().getFair().getFairSetting().getCvRequired().equalsIgnoreCase("0")) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getResume(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.progile_dashboard));
                    Singleton.getInstance().setDashboardData(model);
                }


                if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableTasklistTabDashboard() == 1) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getUpload() + " " +
                            Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTask(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.uploadtask_dashboard));
                    Singleton.getInstance().setDashboardData(model);
                }
                if (Singleton.getInstance().getFairData().getFair().getOptions().getWebinarEnable() == 1) {
                    if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableWebinarsTabDashboard() == 1) {
                        model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaWebinars(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.webinars));
                        Singleton.getInstance().setDashboardData(model);
                    }
                }
                if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableCompaniesDashboard() == 1 && Singleton.getInstance().getFairData().getFair().getOptions().getHideRecomendedCompaniesFront() == 0) {
                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCompanies(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.companies));
                    Singleton.getInstance().setDashboardData(model);
                }
//                if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableMatchingSlots() != 1) {
//                    model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMatchingSlots(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.ic_matching));
//                    Singleton.getInstance().setDashboardData(model);
//
//                }
            }

            if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableCourses() == 1) {
                if (Singleton.getInstance().getLive()) {
                    if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableCourses() == 0) {
                        model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourses(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.courses_dashboard));
                        Singleton.getInstance().setDashboardData(model);
                    }

                } else if (!Singleton.getInstance().getLive()) {
                    if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableCoursesDashboard() == 1) {
                        model = new DashBoardModel(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourses(), ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.courses_dashboard));
                        Singleton.getInstance().setDashboardData(model);
                    }
                }
            }

            adapter = new DashBoardAdapter(Singleton.getInstance().getContext(), getParentFragmentManager());

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(Singleton.getInstance().getContext(), 2));

            recyclerView.setAdapter(adapter);
        } catch (
                Exception e) {
            // Helper.showToast("");
        }

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
            case R.id.menuImageView:
                Helper.menuClick();
                break;
        }
    }
}