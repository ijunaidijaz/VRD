package com.vrd.gsaf.home.courses;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.jobs.Job;
import com.vrd.gsaf.api.responses.jobs.Jobs;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.jobs.Filter;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Courses extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    private final int limit = 10;
    public EditText searchJobLocationEdt;
    //private ConstraintLayout retryLayout;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    private View view;
    private ProgressBar progressBar;
    private ImageView menuImageView, searchImageView2, searchImageView, searchJobTitleImageView;
    private ImageView backImageView;
    private RecyclerView recyclerView;
    private CoursesAdapter adapter;
    private ImageView filterImageView;
    private TextView titleTxt, selectedTxt;
    private EditText searchJobTitleEdt;
    private long mLastClickTime;
    private ShimmerFrameLayout shimmerFrameLayout;
    private int offset = 0;
    private boolean isLoading = false;

    @Override
    public void onResume() {
        super.onResume();
        try {
            Helper.setActiveItemBottomNav(0);
            Singleton.getInstance().getCoursesData().clear();
            offset = 0;
            if (adapter != null) {
                adapter.clearData();
            }

        } catch (Exception e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        Helper.lockOrientation();

        view = inflater.inflate(R.layout.fragment_courses, container, false);
        Helper.hideKeyboard();
        offset = 0;

        initializeViews();
        Singleton.getInstance().getCoursesData().clear();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getParentFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager() != null) {
                    if (Singleton.getInstance().getApplyFilter()) {
                        Singleton.getInstance().setApplyFilter(false);
                        Singleton.getInstance().getCoursesData().clear();
                        if (adapter != null) {
                            adapter.clearData();
                        }

                        if (Helper.isInternetConnected()) {
                            offset = 0;
                            callApi();
                            shimmerFrameLayout.startShimmer();
                        }
                    }
                }
            }
        });
        return view;
    }

    private void callApiForStandJobsList(boolean pagination) {

        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("list_type", "course");
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());
        if (Singleton.getInstance().getFilter().size() > 0) {
            String json = new Gson().toJson(Singleton.getInstance().getFilter());
            JsonObject.addProperty("adv_search", json);
            JsonObject.add("adv_search", Singleton.getInstance().getFilter());
            Set<String> keys = Singleton.getInstance().getFilter().keySet();
            Iterator<String> itr = keys.iterator(); // traversing over HashSet System.out.println("Traversing over Set using Iterator"); while(itr.hasNext()){ System.out.println(itr.next()); }
            Singleton.getInstance().setFilter(new JsonObject());

            selectedTxt.setVisibility(View.VISIBLE);
        } else
            selectedTxt.setVisibility(View.GONE);
        if (!searchJobTitleEdt.getText().toString().equals(""))
            JsonObject.addProperty("search_title", searchJobTitleEdt.getText().toString());
        else if (!searchJobLocationEdt.getText().toString().equals(""))
            JsonObject.addProperty("search_location", searchJobLocationEdt.getText().toString());
        Call<com.vrd.gsaf.api.responses.jobs.Jobs> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getStandJobs(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.jobs.Jobs>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.jobs.Jobs> call, Response<com.vrd.gsaf.api.responses.jobs.Jobs> response) {
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
                        if (!pagination && response.body().getData().getJobList().size() < 1) {
                            Helper.noDataFound(view);
                        } else {
                            parseResponse(pagination, response);
                        }
                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }
            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.jobs.Jobs> call, Throwable t) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.failureResponse(progressBar, getView());
            }

        });
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        searchJobTitleImageView = view.findViewById(R.id.searchJobImageView);

        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        titleTxt = view.findViewById(R.id.titleTxt);
        selectedTxt = view.findViewById(R.id.selectedTxt);
        SpannableString content = new SpannableString("Clear Filter");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        selectedTxt.setText(content);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourses());
        progressBar = view.findViewById(R.id.progressBar);
        menuImageView = view.findViewById(R.id.menuImageView);
        backImageView = view.findViewById(R.id.backImageView);
        Helper.hideKeyboard();
        filterImageView = view.findViewById(R.id.filterImageView);
        try {
            if (Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("0") || Singleton.getInstance().getLoginData() == null) {
                filterImageView.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
        recyclerView = view.findViewById(R.id.recyclerView);
        searchJobTitleEdt = view.findViewById(R.id.searchJobTitleEdt);
        searchJobLocationEdt = view.findViewById(R.id.searchJobLocationEdt);
        searchImageView2 = view.findViewById(R.id.searchImageView2);
        searchImageView = view.findViewById(R.id.searchImageView);
        searchJobLocationEdt.setHint(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSearchBy() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourse() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLocation());

        searchJobTitleEdt.setHint(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSearchBy() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourse() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTitle());
        if (Singleton.getInstance().getIsDashboard()) {
            menuImageView.setVisibility(View.GONE);
            menuImageView.setEnabled(false);
            backImageView.setVisibility(View.VISIBLE);
            searchJobLocationEdt.setVisibility(View.GONE);
            searchJobTitleEdt.setVisibility(View.GONE);
            filterImageView.setVisibility(View.GONE);
            searchImageView2.setVisibility(View.GONE);
            searchImageView.setVisibility(View.GONE);
        }

        if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableAdvancedFilterJobsListingFront() != 1)
            filterImageView.setVisibility(View.GONE);
        setClickListeners();

        if ((Singleton.getInstance().getHomeState() != null && Singleton.getInstance().getHomeState().equals("stands")) || Singleton.getInstance().getFairData().getFair().getOptions().getEnableAdvancedFilterJobsListingFront() != 1) {
            filterImageView.setVisibility(View.GONE);

        }
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
        if (getArguments() != null) {
            Boolean fromDashboard = getArguments().getBoolean("fromDashboard");
            if (fromDashboard != null && fromDashboard.equals(true)) {
                filterImageView.setVisibility(View.GONE);
                searchJobTitleEdt.setVisibility(View.GONE);
                searchJobLocationEdt.setVisibility(View.GONE);
            }
        } else if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableAdvancedFilterJobsListingFront().equals(1)) {
            filterImageView.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        searchJobTitleImageView.setOnClickListener(this);
        selectedTxt.setOnClickListener(this);

        menuImageView.setOnClickListener(this);
        filterImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        searchJobTitleEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.equals("") || charSequence.toString().isEmpty()) {
                    offset = 0;
                    callApi();
                    searchJobTitleImageView.setVisibility(View.GONE);
                    filterImageView.setVisibility(View.VISIBLE);
                } else {
                    searchJobTitleImageView.setVisibility(View.VISIBLE);
                    filterImageView.setVisibility(View.GONE);
                }

//                if (view.isActivated()) {
//                    EditText editText = view.findViewById(R.id.searchJobLocationEdt);
//                    if (charSequence.toString().equals("") && (editText == null || editText.getText().toString().equals(""))) {
//                        adapter.getFilter().filter(" " + "," + " ");
//                    } else if (editText == null || editText.getText().toString().equals("")) {
//                        adapter.getFilter().filter(charSequence + "," + " ");
//                    } else if (charSequence.toString().equals("")) {
//                        adapter.getFilter().filter(" " + "," + editText.getText());
//                    } else
//                        adapter.getFilter().filter(charSequence + "," + editText.getText().toString());
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchJobLocationEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                EditText editText = view.findViewById(R.id.searchJobTitleEdt);
//                if (view.isActivated()) {
//
//                    if (charSequence.toString().equals("") && (editText == null || editText.getText().toString().equals(""))) {
//                        adapter.getFilter().filter(" " + "," + " ");
//                    } else if (editText == null || editText.getText().toString().equals("")) {
//                        adapter.getFilter().filter(" " + "," + charSequence);
//                    } else if (charSequence.toString().equals("")) {
//                        adapter.getFilter().filter(editText.getText() + "," + " ");
//                    } else
//                        adapter.getFilter().filter(editText.getText() + "," + charSequence);
//                }

                if (charSequence.equals("") || charSequence.toString().isEmpty()) {
                    callApi();
                    offset = 0;
                    searchJobTitleImageView.setVisibility(View.GONE);
                    filterImageView.setVisibility(View.VISIBLE);
                } else {
                    searchJobTitleImageView.setVisibility(View.VISIBLE);
                    filterImageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
//
//        if (Helper.isInternetConnected()) {
//            if (Singleton.getInstance().getIsDashboard())
//                callApiForMatchingCourses(false);
//            else
//                callAPiForCoursesList(false);
//            shimmerFrameLayout.startShimmer();
//        }

        callApi();


        retryLayout();
    }

    private void callApi() {
        if (Helper.isInternetConnected()) {
            if (Singleton.getInstance().getIsLoggedIn()) {
                if (Singleton.getInstance().getHomeState() != null && Singleton.getInstance().getHomeState().equals("stands")) {
                    callApiForStandJobsList(false);
                } else
                    callApiForMatchingCourses(false);
            } else
                callAPiForCoursesList(false);
            isLoading = true;

            shimmerFrameLayout.startShimmer();
        }
    }

    private void retryLayout() {
        View retryLayout;
        retryLayout = view.findViewById(R.id.somethingWentWrongLayout);
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset = 0;
                callApi();
            }
        });
    }

    private void callAPiForCoursesList(boolean pagination) {
        Helper.showProgressBar(progressBar);

        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("list_type", "course");
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        if (Singleton.getInstance().getFilter().size() > 0) {
            String json = new Gson().toJson(Singleton.getInstance().getFilter());
            JsonObject.addProperty("adv_search", json);
            JsonObject.add("adv_search", Singleton.getInstance().getFilter());
            Set<String> keys = Singleton.getInstance().getFilter().keySet();
            Iterator<String> itr = keys.iterator(); // traversing over HashSet System.out.println("Traversing over Set using Iterator"); while(itr.hasNext()){ System.out.println(itr.next()); }
            Singleton.getInstance().setFilter(new JsonObject());

            selectedTxt.setVisibility(View.VISIBLE);
        } else
            selectedTxt.setVisibility(View.GONE);

        Call<Jobs> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getJobs(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<Jobs>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.jobs.Jobs> call, Response<Jobs> response) {
                Helper.hideProgressBar(progressBar);
                Helper.endShimmer(shimmerFrameLayout);
                Helper.hideLayouts(getView());

                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        if (Singleton.getInstance().getCoursesData().size() == 0)
                            Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        if (!pagination && response.body().getData().getJobList().size() < 1) {
                            Helper.noDataFound(view);
                        } else {
                            parseResponse(pagination, response);
                        }
                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure
                    (Call<com.vrd.gsaf.api.responses.jobs.Jobs> call, Throwable t) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.failureResponse(progressBar, getView());
            }
        });
    }

    private void callApiForMatchingCourses(boolean pagination) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("list_type", "course");
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("limit", limit);
        JsonObject.addProperty("offset", offset);
        if (Singleton.getInstance().getFilter().size() > 0) {
            JsonObject.add("adv_search", Singleton.getInstance().getFilter());
            Singleton.getInstance().setFilter(new JsonObject());
            selectedTxt.setVisibility(View.VISIBLE);
        } else
            selectedTxt.setVisibility(View.GONE);
        if (!searchJobTitleEdt.getText().toString().equals("")) {
            JsonObject.addProperty("search_title", searchJobTitleEdt.getText().toString());
        } else if (!searchJobLocationEdt.getText().toString().equals(""))
            JsonObject.addProperty("search_location", searchJobLocationEdt.getText().toString());


        Call<com.vrd.gsaf.api.responses.jobs.Jobs> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getMatchingJobs(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.jobs.Jobs>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.jobs.Jobs> call, Response<com.vrd.gsaf.api.responses.jobs.Jobs> response) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.hideProgressBar(progressBar);
                Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        if (Singleton.getInstance().getCoursesData().size() == 0) {
                            Helper.noDataFound(view);
                            adapter.clearData();
                        }
                    } else if (response.body().getStatus()) {
                        if (!pagination && response.body().getData().getJobList().size() < 1) {
                            Helper.noDataFound(view);
                            adapter.clearData();
                        } else {
                            parseResponse(pagination, response);
                        }
                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }
            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.jobs.Jobs> call, Throwable t) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.failureResponse(progressBar, getView());

            }

        });

    }

    private void parseResponse(boolean pagination, Response<
            com.vrd.gsaf.api.responses.jobs.Jobs> response) {
        Helper.hideProgressBar(progressBar);
        try {

            if (response.body().getStatus()) {
                if (Singleton.getInstance().getCoursesData() != null && !Singleton.getInstance().getCoursesData().isEmpty()) {
                    for (Job job : response.body().getData().getJobList()) {
                        if (!containsJob(Singleton.getInstance().getCoursesData(), job.getJobId())) {
                            Singleton.getInstance().getCoursesData().add(job);
                        }
                    }
                } else
                    Singleton.getInstance().getCoursesData().addAll(response.body().getData().getJobList());

                if (pagination) {
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(offset - 1);
                } else {
                    manageRecyclerView();
                }
                offset = limit + offset;
                Helper.hideProgressBar(progressBar);
                if (response.body().getData().getJobList().size() > 9)
                    isLoading = false;
//                Helper.showToast(String.valueOf(Singleton.getInstance().getCoursesData().size()));


            } else
                Helper.showToast("Something went wrong...Please try later!");
        } catch (Exception e) {
            Helper.showToast("Something Went Wrong");
        }
    }

    public boolean containsJob(List<Job> list, final Integer id) {
        return list.stream().anyMatch(o -> o.getJobId().equals(id));
    }

    private void manageRecyclerView() {

        adapter = new CoursesAdapter(getParentFragmentManager(), progressBar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));

        recyclerView.setAdapter(adapter);
        initScrollListener();
        Singleton.getInstance().setCoursesAdapter(adapter);

    }

    private void replaceFragment(Fragment fragment, String tag) {


        getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == Singleton.getInstance().getCoursesData().size() - 1) {
                        //bottom of list!
//                        if (Helper.isInternetConnected()) {
//                            if (Singleton.getInstance().getIsDashboard())
//                                callApiForMatchingCourses(true);
//                            else
//                                callAPiForCoursesList(true);
//                        }
                        if (Helper.isInternetConnected()) {
                            callApi();
                        }
//                        else
//                            callAPiForJobsList(true);
                        isLoading = true;
                    }
                }
            }
        });
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
            case R.id.menuImageView:
                Helper.menuClick();
                break;
            case R.id.backImageView:
                onBackPress();
                break;
            case R.id.filterImageView:
                replaceFragment(new Filter(), null);
                break;
            case R.id.searchJobImageView:
                offset = 0;
                Singleton.getInstance().getCoursesData().clear();
                searchJobTitleImageView.setVisibility(View.GONE);
                filterImageView.setVisibility(View.VISIBLE);
                callApi();
            case R.id.selectedTxt:
                callApi();
                offset = 0;
                break;
        }
    }
}