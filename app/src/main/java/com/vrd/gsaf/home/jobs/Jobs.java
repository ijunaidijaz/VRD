package com.vrd.gsaf.home.jobs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.appliedJobs.AppliedJobs;
import com.vrd.gsaf.api.responses.jobPercentage.JobsMatchPercentage;
import com.vrd.gsaf.api.responses.jobPercentage.Match;
import com.vrd.gsaf.api.responses.jobs.Job;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Jobs extends Fragment implements View.OnClickListener, JobAdapter.onApplyClick {

    public static MutableLiveData<Boolean> removeFragment = new MutableLiveData<>(false);
    static Jobs instance;
    private final int limit = 10;
    private final long mLastClickTime = 0;
    public EditText searchJobLocationEdt, searchJobTitleEdt;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    private View view;
    private ProgressBar progressBar;
    private ImageView menuImageView, backImageView, filterImageView, searchImageView2, searchImageView, searchJobTitleImageView;
    private RecyclerView recyclerView;
    private JobAdapter adapter;
    private TextView titleTxt, selectedTxt;
    private boolean isLoading = false;
    private int offset = 0;
    private ShimmerFrameLayout shimmerFrameLayout;
    private boolean isSerching = false;

    public static Jobs getInstance() {
        return instance;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Helper.setActiveItemBottomNav(0);
            if (Singleton.getInstance().getRemoveJobFrag()) {
                Singleton.getInstance().setHomeState("jobs");
            }
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
        Helper.lockOrientation();
        instance = this;

        view = inflater.inflate(R.layout.fragment_jobs, container, false);
        Helper.hideKeyboard();
        offset = 0;
        initializeViews();
        Singleton.getInstance().getJobsData().clear();
        getParentFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager() != null) {
                    if (Singleton.getInstance().getApplyFilter()) {
                        Singleton.getInstance().setApplyFilter(false);
                        Singleton.getInstance().getJobsData().clear();
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
        if (Singleton.getInstance().getLoginData() == null) {
            filterImageView.setVisibility(View.GONE);
        }

        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        titleTxt = view.findViewById(R.id.titleTxt);
        selectedTxt = view.findViewById(R.id.selectedTxt);

        SpannableString content = new SpannableString("Clear Filter");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        selectedTxt.setText(content);

        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJobs());
        progressBar = view.findViewById(R.id.progressBar);
        Helper.hideKeyboard();
        menuImageView = view.findViewById(R.id.menuImageView);
        backImageView = view.findViewById(R.id.backImageView);
        searchJobTitleEdt = view.findViewById(R.id.searchJobTitleEdt);
        searchJobLocationEdt = view.findViewById(R.id.searchJobLocationEdt);
        filterImageView = view.findViewById(R.id.filterImageView);
        if (Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("0") || Singleton.getInstance().getLoginData() == null) {
            filterImageView.setVisibility(View.GONE);
        }
        recyclerView = view.findViewById(R.id.recyclerView);
        searchJobTitleEdt = view.findViewById(R.id.searchJobTitleEdt);
        searchJobLocationEdt = view.findViewById(R.id.searchJobLocationEdt);
        searchImageView2 = view.findViewById(R.id.searchImageView2);
        searchImageView = view.findViewById(R.id.searchImageView);
        searchJobTitleImageView = view.findViewById(R.id.searchJobImageView);
        searchJobLocationEdt.setHint(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSearchBy() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJob() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLocation());

        searchJobTitleEdt.setHint(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSearchBy() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJob() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTitle());
        if (Singleton.getInstance().getIsDashboard()) {
            menuImageView.setVisibility(View.GONE);
            menuImageView.setEnabled(false);
            backImageView.setVisibility(View.VISIBLE);
            filterImageView.setVisibility(View.GONE);

            searchJobTitleEdt.setVisibility(View.GONE);
            searchJobLocationEdt.setVisibility(View.GONE);
            filterImageView.setVisibility(View.GONE);
            searchImageView2.setVisibility(View.GONE);
            searchImageView.setVisibility(View.GONE);
        }

        if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableAdvancedFilterJobsListingFront() != 1)
            filterImageView.setVisibility(View.GONE);

//        if(Singleton.getInstance().getUserLogin())
//            filterImageView.setVisibility(View.GONE);
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
        setClickListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        menuImageView.setOnClickListener(this);
        searchJobTitleImageView.setOnClickListener(this);
        filterImageView.setOnClickListener(this);
        selectedTxt.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        searchJobTitleEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (charSequence.equals("") || charSequence.toString().isEmpty()) {
                    offset = 0;
                    isSerching = true;
                    callApi();
                    searchJobTitleImageView.setVisibility(View.GONE);
                    filterImageView.setVisibility(View.VISIBLE);
                } else {
                    searchJobTitleImageView.setVisibility(View.VISIBLE);
                    filterImageView.setVisibility(View.GONE);
                }
//                EditText editText = view.findViewById(R.id.searchJobLocationEdt);
//                if (charSequence.toString().equals("") && (editText == null || editText.getText().toString().equals(""))) {
//                    adapter.getFilter().filter(" " + "," + " ");
//                } else if (editText == null || editText.getText().toString().equals("")) {
//                    adapter.getFilter().filter(charSequence + "," + " ");
//                } else if (charSequence.toString().equals("")) {
//                    adapter.getFilter().filter(" " + "," + editText.getText());
//                } else
//                    adapter.getFilter().filter(charSequence + "," + editText.getText().toString());

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
                EditText editText = view.findViewById(R.id.searchJobTitleEdt);


                if (charSequence.equals("") || charSequence.toString().isEmpty()) {
                    offset = 0;


                    searchJobTitleImageView.setVisibility(View.GONE);
                    filterImageView.setVisibility(View.VISIBLE);
                } else {
                    searchJobTitleImageView.setVisibility(View.VISIBLE);
                    filterImageView.setVisibility(View.GONE);
                }
//                if (charSequence.toString().equals("") && (editText == null || editText.getText().toString().equals(""))) {
//                    adapter.getFilter().filter(" " + "," + " ");
//                } else if (editText == null || editText.getText().toString().equals("")) {
//                    adapter.getFilter().filter(" " + "," + charSequence);
//                } else if (charSequence.toString().equals("")) {
//                    adapter.getFilter().filter(editText.getText() + "," + " ");
//                } else
//                    adapter.getFilter().filter(editText.getText() + "," + charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        callApi();

        retryLayout();
    }

    private void callApi() {
        if (Helper.isInternetConnected()) {
            if (Singleton.getInstance().getIsLoggedIn()) {
                if (Singleton.getInstance().getHomeState() != null && Singleton.getInstance().getHomeState().equals("stands")) {
                    callApiForStandJobsList(false);
                } else
                    callApiForMatchingJobs(false);
            } else if (!Singleton.getInstance().getIsLoggedIn()) {
                callAPiForJobsList(false);
            }
            isLoading = true;
            shimmerFrameLayout.startShimmer();
        }
    }

    private void callApiForStandJobsList(boolean pagination) {

        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("list_type", "job");
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());
        if (Singleton.getInstance().getFilter().size() > 0) {
            String json = new Gson().toJson(Singleton.getInstance().getFilter());
            JsonObject.addProperty("adv_search", json);
            JsonObject.add("adv_search", Singleton.getInstance().getFilter());


            Set<String> keys = Singleton.getInstance().getFilter().keySet();
            Iterator<String> itr = keys.iterator(); // traversing over HashSet System.out.println("Traversing over Set using Iterator"); while(itr.hasNext()){ System.out.println(itr.next()); }

//            while (itr.hasNext()) {
//                Singleton.getInstance().getFilter().remove(itr.next());
//            }
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
                //parseResponse(pagination, response);
                Helper.endShimmer(shimmerFrameLayout);
                Helper.hideProgressBar(progressBar);
                Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        if (Singleton.getInstance().getJobsData().size() == 0) {
                            Helper.noDataFound(view);
                        }
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

    private void callApiForMatchingJobs(boolean pagination) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("list_type", "job");
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("limit", limit);
        JsonObject.addProperty("offset", offset);
        if (!searchJobTitleEdt.getText().toString().equals(""))
            JsonObject.addProperty("search_title", searchJobTitleEdt.getText().toString());
        else if (!searchJobLocationEdt.getText().toString().equals(""))
            JsonObject.addProperty("search_location", searchJobLocationEdt.getText().toString());


        if (Singleton.getInstance().getFilter().size() > 0) {
            JsonObject.add("adv_search", Singleton.getInstance().getFilter());
            Singleton.getInstance().setFilter(new JsonObject());
            selectedTxt.setVisibility(View.VISIBLE);
        } else
            selectedTxt.setVisibility(View.GONE);

        Call<com.vrd.gsaf.api.responses.jobs.Jobs> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getMatchingJobs(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.jobs.Jobs>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.jobs.Jobs> call, Response<com.vrd.gsaf.api.responses.jobs.Jobs> response) {
                //  parseResponse(pagination, response);
                Helper.endShimmer(shimmerFrameLayout);
                try {
                    recyclerView.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                }
                Helper.hideProgressBar(progressBar);
                Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        if (Singleton.getInstance().getJobsData().size() == 0) {
                            Helper.noDataFound(view);
                            try {
                                recyclerView.setVisibility(View.GONE);
                            } catch (Exception e) {

                            }
                        }
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

    public boolean containsJob(List<Job> list, final Integer id) {
        return list.stream().anyMatch(o -> o.getJobId().equals(id));
    }

    private void parseResponse(boolean pagination, Response<
            com.vrd.gsaf.api.responses.jobs.Jobs> response) {
        Helper.hideProgressBar(progressBar);
        try {
            if (response.body().getStatus()) {
                int size = Singleton.getInstance().getJobsData().size() - 1;

                if (Singleton.getInstance().getJobsData() != null && !Singleton.getInstance().getJobsData().isEmpty()) {
                    for (Job job : response.body().getData().getJobList()) {
                        if (!containsJob(Singleton.getInstance().getJobsData(), job.getJobId())) {
                            Singleton.getInstance().getJobsData().add(job);
                        }
                    }
                } else
                    Singleton.getInstance().getJobsData().addAll(response.body().getData().getJobList());

                if (pagination && adapter != null) {
                    if (size >= 0) adapter.notifyItemInserted(size);
//                    recyclerView.scrollToPosition(offset-1);
                } else {
                    manageRecyclerView(size);
                }
                offset = limit + offset;
                Helper.hideProgressBar(progressBar);
                if (response.body().getData().getJobList().size() > 9)
                    isLoading = false;
                //  Helper.showToast(String.valueOf(Singleton.getInstance().getJobsData().size()));


            } else
                Helper.showToast("Something went wrong...Please try later!");
        } catch (Exception e) {
            Helper.showToast("Something Went Wrong");
        }
    }

    private void callAPiForJobsList(boolean pagination) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("list_type", "job");
        JsonObject.addProperty("limit", limit);
        JsonObject.addProperty("offset", offset);
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
        Call<com.vrd.gsaf.api.responses.jobs.Jobs> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getJobs(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
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
                        Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        parseResponse(pagination, response);
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

    private void manageRecyclerView(int position) {
        if (adapter == null && !isSerching) {
            adapter = new JobAdapter(getParentFragmentManager(), this, this);
            Singleton.getInstance().setJobAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
            recyclerView.setAdapter(adapter);
            initScrollListener();
        } else if (isSerching) {
            adapter = new JobAdapter(getParentFragmentManager(), this, this);
            Singleton.getInstance().setJobAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
            recyclerView.setAdapter(adapter);
            initScrollListener();
            isSerching = false;
        } else {
            if (position > 0) adapter.notifyItemInserted(position);
        }

    }

    private void replaceFragment(Fragment fragment, String tag) {


        getParentFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("jobs")
                .commit();
    }

    private void onBackPress() {
        getActivity().getSupportFragmentManager().popBackStack();
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == Singleton.getInstance().getJobsData().size() - 1) {
                        //bottom of list!
//                        if (Helper.isInternetConnected()) {
//                            if (Singleton.getInstance().getIsDashboard()) {
//                                if (Singleton.getInstance().getHomeState() != null && Singleton.getInstance().getHomeState().equals("stands")) {
//                                    //callApiForStandJobsList(false);
//                                } else
//                                    callApiForMatchingJobs(true);
//                            } else {
//                                //  if(Singleton.getInstance().getFilter().size()==0) {
//                                callAPiForJobsList(true);
//                                //   }
//                            }
//
//                        }
                        if (Helper.isInternetConnected()) {
                            //   if (view.isActivated()) {
                            callApi();
                            //   }

                            //   }

                            //    }
//                        else
//                            callAPiForJobsList(true);
                            // isLoading = true;
                        }
                    }
                }
            }
        });
    }

    private void callApiToApplyJob(int position, CustomViewHolder holder) {

        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        //   if (Singleton.getInstance().getIsDashboard())
        JsonObject.addProperty("job_id", Singleton.getInstance().getJobsData().get(position).getJobId());
        //  else
        //    JsonObject.addProperty("job_id", Singleton.getInstance().getJobsData().get(position).getJobId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getJobsData().get(position).getCompanyInfo().getCompanyId());
        Call<AppliedJobs> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).applyJob(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<AppliedJobs>() {
            @Override
            public void onResponse(Call<AppliedJobs> call, Response<AppliedJobs> response) {
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.body().getStatus()) {
                        //holder.applyBtn.setVisibility(View.GONE);
                        holder.applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApplied());
                        // Singleton.getInstance().getRecruiterArrayList().addAll(response.body().getData().getRecruiterList());
                        Helper.showToast("Applied");

                    } else {
                        //  Helper.showToast(response.body().getMsg());
                    }
                } catch (Exception e) {
                    Helper.showToast("Something Went Wrong");
                }

            }

            @Override
            public void onFailure(Call<AppliedJobs> call, Throwable t) {
                Helper.showToast("Something Went Wrong");
                Helper.hideProgressBar(progressBar);
                //Helper.failureResponse(progressBar, getView());
            }

        });

    }

    @Override
    public void onClick(View view) {
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
                Singleton.getInstance().getJobsData().clear();
                searchJobTitleImageView.setVisibility(View.GONE);
                filterImageView.setVisibility(View.VISIBLE);
                isSerching = true;
                callApi();
                break;
            case R.id.selectedTxt:
                offset = 0;
                callApi();
                break;
        }
    }

    @Override
    public void applyToJob(int index, CustomViewHolder holder) {
        if (Helper.isInternetConnected())
            callApiToApplyJob(index, holder);
    }

    @Override
    public void matchPercentage(int JobId) {
        callApiForMatchPercentage(JobId);
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


}