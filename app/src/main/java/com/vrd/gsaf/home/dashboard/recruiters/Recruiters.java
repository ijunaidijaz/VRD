package com.vrd.gsaf.home.dashboard.recruiters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.dashboardRecruiters.MatchingRecruiters;
import com.vrd.gsaf.api.responses.jobPercentage.JobsMatchPercentage;
import com.vrd.gsaf.api.responses.jobPercentage.Match;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.jobs.JobMatchAdapter;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Recruiters extends Fragment implements View.OnClickListener, RecruiterAdapter.onMatch {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    private final int limit = 10;
    ConstraintLayout mainLayout;
    RelativeLayout toolbar;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView;
    private TextView titleTxt;
    private RecyclerView recyclerView;
    private RecruiterAdapter adapter;
    private long mLastClickTime;
    private boolean isLoading = false;
    private int offset = 0;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recruiters, container, false);
        Singleton.getInstance().getRecruiterArrayList().clear();
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }


    @SuppressLint("CutPasteId")
    private void initializeViews() {
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        progressBar = view.findViewById(R.id.progressBar);
        backImageView = view.findViewById(R.id.backImageView);
        recyclerView = view.findViewById(R.id.recyclerView);
        titleTxt = view.findViewById(R.id.titleTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getRecruiters());
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        setClickListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        if (Helper.isInternetConnected()) {
            if (Singleton.getInstance().getHomeState() != null && Singleton.getInstance().getHomeState().equals("stands")) {
                callApiForStandRecruitersList(false);
            } else {
                callAPiForRecruitersList(false);
            }
            Helper.showProgressBar(progressBar);
            shimmerFrameLayout.startShimmer();
        }
        retryLayout();
    }

    private void callApiForStandRecruitersList(boolean pagination) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());
        JsonObject.addProperty("limit", "10");
        JsonObject.addProperty("offset", offset);
        Call<MatchingRecruiters> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getStandRecruiters(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<MatchingRecruiters>() {
            @Override
            public void onResponse(Call<MatchingRecruiters> call, Response<MatchingRecruiters> response) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.hideProgressBar(progressBar);
                Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        if (!pagination)
                            Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {

                        Singleton.getInstance().getRecruiterArrayList().addAll(response.body().getData().getRecruiterList());
                        if (pagination) {
                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(offset - 1);
                        } else {
                            if (response.body().getData().getRecruiterList().size() > 0) {
                                manageRecyclerView();
                            } else
                                Helper.noDataFound(view);
                        }
                        offset = limit + offset;
                        Helper.hideProgressBar(progressBar);
                        if (response.body().getData().getRecruiterList().size() > 9)
                            isLoading = false;
//                        Helper.showToast(String.valueOf(Singleton.getInstance().getRecruiterArrayList().size()));

                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<MatchingRecruiters> call, Throwable t) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.failureResponse(progressBar, getView());
            }

        });
    }

    private void retryLayout() {
        View retryLayout;
        retryLayout = view.findViewById(R.id.somethingWentWrongLayout);
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.isInternetConnected()) {
                    if (Singleton.getInstance().getHomeState() != null && Singleton.getInstance().getHomeState().equals("stands")) {
                        callApiForStandRecruitersList(false);
                    } else
                        callAPiForRecruitersList(false);
                }
            }
        });
    }


    private void manageRecyclerView() {
        adapter = new RecruiterAdapter(getParentFragmentManager(), this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));

        recyclerView.setAdapter(adapter);
        initScrollListener();
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

    private void callAPiForRecruitersList(boolean pagination) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("limit", "10");
        JsonObject.addProperty("offset", offset);
        Call<MatchingRecruiters> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getMatchingRecruiters(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<MatchingRecruiters>() {
            @Override
            public void onResponse(Call<MatchingRecruiters> call, Response<MatchingRecruiters> response) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.hideProgressBar(progressBar);
                Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        if (!pagination)
                            Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        Singleton.getInstance().getRecruiterArrayList().addAll(response.body().getData().getRecruiterList());
                        if (pagination) {
                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(offset - 1);
                        } else {
                            if (response.body().getData().getRecruiterList().size() > 0) {
                                manageRecyclerView();
                            } else
                                Helper.noDataFound(view);
                        }
                        offset = limit + offset;
                        Helper.hideProgressBar(progressBar);
                        if (response.body().getData().getRecruiterList().size() > 9)
                            isLoading = false;
//                        Helper.showToast(String.valueOf(Singleton.getInstance().getRecruiterArrayList().size()));

                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<MatchingRecruiters> call, Throwable t) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.failureResponse(progressBar, getView());
            }

        });

    }


    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == Singleton.getInstance().getRecruiterArrayList().size() - 1) {
                        //bottom of list!

                        if (Singleton.getInstance().getIsDashboard()) {
                            if (Singleton.getInstance().getHomeState() != null && Singleton.getInstance().getHomeState().equals("stands")) {
                                callApiForStandRecruitersList(true);
                            } else
                                callAPiForRecruitersList(true);
                        }


                        isLoading = true;
                    }
                }
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
                onBackPress();
                break;

        }
    }

    @Override
    public void matchPercentage(int companyId) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("match_param", "recruiter");
        JsonObject.addProperty("job_id", companyId);


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
    }
//
}