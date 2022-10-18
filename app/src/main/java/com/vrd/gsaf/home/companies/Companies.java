package com.vrd.gsaf.home.companies;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Companies extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    RelativeLayout toolbar;
    int limit = 10;
    int offset = 0;
    boolean isFromSideBar = false;
    private View view;
    private ProgressBar progressBar;
    private ImageView menuImageView, backImageView;
    private RecyclerView recyclerView;
    private CompaniesAdapter adapter;
    private TextView titleTxt;
    private TextView search_badge;
    private RelativeLayout search_bar;
    private EditText searchJobTitleEdt;
    private long mLastClickTime;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ConstraintLayout mainLayout;
    private boolean isLoading = false;

    @Override
    public void onResume() {
        super.onResume();
        try {
            Helper.setActiveItemBottomNav(0);
        } catch (Exception e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Helper.lockOrientation();

        view = inflater.inflate(R.layout.fragment_companies, container, false);
        Helper.hideKeyboard();
        offset = 0;
        initializeViews();
        if (getArguments() != null && getArguments().containsKey("isFromSideBar")) {
            isFromSideBar = true;
            search_bar.setVisibility(View.VISIBLE);
        } else isFromSideBar = false;

        if (Helper.isInternetConnected()) {
            shimmerFrameLayout.startShimmer();
            if (isFromSideBar) {
                callApiForCompnaniesList(false);
            } else if (Singleton.getInstance().getIsLoggedIn()) {
                callApiForMatchingCompnaniesList(false);
            } else callApiForCompnaniesList(false);
        }
        Singleton.getInstance().getCompaniesData().clear();


        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        mainLayout = view.findViewById(R.id.mainLayout);
        searchJobTitleEdt = view.findViewById(R.id.searchJobTitleEdt);
        searchJobTitleEdt.clearFocus();
        searchJobTitleEdt.setHint(Objects.requireNonNull(Singleton.getKeywords()).getSearch_here()+"...");
        search_badge = view.findViewById(R.id.search_badge);
        search_bar = view.findViewById(R.id.search_bar);
        titleTxt = view.findViewById(R.id.titleTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCompanies());
        progressBar = view.findViewById(R.id.progressBar);
        menuImageView = view.findViewById(R.id.menuImageView);
        backImageView = view.findViewById(R.id.backImageView);
        recyclerView = view.findViewById(R.id.recyclerView);
        if (Singleton.getInstance().getIsDashboard()) {
//            menuImageView.setVisibility(View.GONE);
//            menuImageView.setEnabled(false);
//            backImageView.setVisibility(View.VISIBLE);
        }
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
        searchJobTitleEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString() != null && editable.toString().equalsIgnoreCase("")) {
                    offset = 0;
                    if (isFromSideBar) {
                        callApiForCompnaniesList(false);
                    } else if (Singleton.getInstance().getIsLoggedIn()) {
                        callApiForMatchingCompnaniesList(false);
                    } else callApiForCompnaniesList(false);
                }
            }
        });

        setClickListeners();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        menuImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        search_badge.setOnClickListener(this);

        retryLayout();
    }

    private void retryLayout() {
        View retryLayout;
        retryLayout = view.findViewById(R.id.somethingWentWrongLayout);
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.isInternetConnected()) {
                    if (Singleton.getInstance().getIsLoggedIn())
                        callApiForMatchingCompnaniesList(false);
                    else
                        callApiForCompnaniesList(false);

                }
            }
        });
    }

    private void parseResponse(boolean pagination, Response<com.vrd.gsaf.api.responses.companies.Companies> response) {
        try {
            if (response.body().getStatus()) {
                Singleton.getInstance().getCompaniesData().addAll(response.body().getData().getCompanyList());
                if (pagination) {
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(offset - 1);
                } else {
                    Singleton.getInstance().getCompaniesData().clear();
                    Singleton.getInstance().getCompaniesData().addAll(response.body().getData().getCompanyList());
                    manageRecyclerView();
                }
                offset = offset + limit;
                Helper.hideProgressBar(progressBar);
                if (response.body().getData().getCompanyList().size() > (limit - 1))
                    isLoading = false;

            } else
                Helper.showToast("Something went wrong...Please try later!");
        } catch (Exception e) {
            Helper.showToast("Something Went Wrong");
        }
    }

    private void callApiForMatchingCompnaniesList(boolean pagination) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        // JsonObject.addProperty("latitude",Singleton.getInstance().getLocation().getLatitude());
        //JsonObject.addProperty("longitude",Singleton.getInstance().getLocation().getLongitude());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("limit", limit);
        JsonObject.addProperty("offset", offset);

        Call<com.vrd.gsaf.api.responses.companies.Companies> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getMatchingCompanies(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.companies.Companies>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.companies.Companies> call, Response<com.vrd.gsaf.api.responses.companies.Companies> response) {

                Helper.hideProgressBar(progressBar);
                Helper.endShimmer(shimmerFrameLayout);
                Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        if (!pagination) {
                            new Handler().postDelayed(() -> Helper.noDataFound(view), 1000);
                            if (adapter != null) adapter.clearData();
                        }
                    } else if (response.body().getStatus()) {
                        if (!pagination && response.body().getData().getCompanyList().size() < 1) {
                            new Handler().postDelayed(() -> Helper.noDataFound(view), 1000);
                            if (adapter != null) adapter.clearData();
                        } else {
                            parseResponse(pagination, response);
                        }
                    } else if (!pagination)
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }
            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.companies.Companies> call, Throwable t) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.failureResponse(progressBar, getView());
            }

        });

    }

    private void callApiForCompnaniesList(boolean pagination) {
        Helper.showProgressBar(progressBar);

        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getMyFairId());
        if (Singleton.getInstance().getLocation() != null) {
            JsonObject.addProperty("latitude", Singleton.getInstance().getLocation().getLatitude());
            JsonObject.addProperty("longitude", Singleton.getInstance().getLocation().getLongitude());
        }
        if (!searchJobTitleEdt.getText().toString().equalsIgnoreCase("")) {
            JsonObject.addProperty("search", searchJobTitleEdt.getText().toString());
        }
        JsonObject.addProperty("limit", limit);
        JsonObject.addProperty("offset", offset);

        Call<com.vrd.gsaf.api.responses.companies.Companies> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getCompanies(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.companies.Companies>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.companies.Companies> call, Response<com.vrd.gsaf.api.responses.companies.Companies> response) {
                Helper.hideProgressBar(progressBar);
                Helper.endShimmer(shimmerFrameLayout);
                Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        if (!pagination) {
                            if (adapter != null) adapter.clearData();
                            new Handler().postDelayed(() -> Helper.noDataFound(view), 1000);
                        }
                    } else if (response.body().getStatus()) {
                        if (!pagination && response.body().getData().getCompanyList().size() < 1) {
                            if (adapter != null) adapter.clearData();
                            new Handler().postDelayed(() -> Helper.noDataFound(view), 1000);
                        } else {
                            parseResponse(pagination, response);
                        }
                    } else if (!pagination)
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }
            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.companies.Companies> call, Throwable t) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.failureResponse(progressBar, getView());

            }

        });

    }

    private void manageRecyclerView() {
        adapter = new CompaniesAdapter(getParentFragmentManager());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(Singleton.getInstance().getContext(), 2));
        recyclerView.setAdapter(adapter);
        initScrollListener();
        Helper.hideKeyboard();

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
        Singleton.getInstance().setHomeState("home");
        requireActivity().onBackPressed();
//        getActivity().getSupportFragmentManager().popBackStack();
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == Singleton.getInstance().getCompaniesData().size() - 1) {
                        //bottom of list!
                        if (Helper.isInternetConnected()) {
                            if (isFromSideBar) {
                                callApiForCompnaniesList(true);
                            } else if (Singleton.getInstance().getIsLoggedIn()) {
                                callApiForMatchingCompnaniesList(true);
                            } else callApiForCompnaniesList(true);
                            // }
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
                requireActivity().onBackPressed();
                break;
            case R.id.menuImageView:
                Helper.menuClick();
                break;
            case R.id.search_badge:
                if (!searchJobTitleEdt.getText().toString().equalsIgnoreCase("")) {
                    offset = 0;
                    if (isFromSideBar) {
                        callApiForCompnaniesList(false);
                    } else if (Singleton.getInstance().getIsLoggedIn()) {
                        callApiForMatchingCompnaniesList(false);
                    } else callApiForCompnaniesList(false);
                }else Helper.showToast("Please enter text to continue");
                break;
        }
    }


    @Override
    public void onPause() {
        searchJobTitleEdt.clearFocus();
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}