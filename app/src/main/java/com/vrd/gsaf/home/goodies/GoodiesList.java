package com.vrd.gsaf.home.goodies;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.goodies.Goodies;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodiesList extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    public static GoodiesAdapter goodieAdapter;
    private final boolean isLoading = false;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView, menuImageView;
    private TextView titleTxt;
    private RecyclerView recyclerView;
    private long mLastClickTime;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ConstraintLayout noDataLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_goodie_bag, container, false);
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        progressBar = view.findViewById(R.id.progressBar);
        backImageView = view.findViewById(R.id.backImageView);
        menuImageView = view.findViewById(R.id.menuImageView);
        recyclerView = view.findViewById(R.id.recyclerView);
        titleTxt = view.findViewById(R.id.titleTxt);
        if (Singleton.getInstance().getGoodies()) {
            backImageView.setVisibility(View.VISIBLE);
            menuImageView.setVisibility(View.GONE);
            titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoodies());
        } else {
            backImageView.setVisibility(View.GONE);
            menuImageView.setVisibility(View.VISIBLE);
            titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoodie() + " " +
                    Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getBag());
        }
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        setClickListeners();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        menuImageView.setOnClickListener(this);
        if (Helper.isInternetConnected()) {
            if (Singleton.getInstance().getGoodies())
                callAPiForGoodieBagList();
            else
                callAPiForCandidateGoodieBag();
            shimmerFrameLayout.startShimmer();

        }
        retryLayout();
    }

    private void callAPiForCandidateGoodieBag() {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        Call<Goodies> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getGoodieBags(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<Goodies>() {
            @Override
            public void onResponse(Call<Goodies> call, Response<Goodies> response) {
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
                        manageRecyclerView(response.body().getData().getGoodieBagList());
                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }
            }

            @Override
            public void onFailure(Call<Goodies> call, Throwable t) {
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
                    callAPiForGoodieBagList();
                }
            }
        });
    }

    private void callAPiForGoodieBagList() {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());
        Call<com.vrd.gsaf.api.responses.goodies.Goodies> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getGoodieBagList(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.goodies.Goodies>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.goodies.Goodies> call, Response<com.vrd.gsaf.api.responses.goodies.Goodies> response) {
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
                        manageRecyclerView(response.body().getData().getGoodieBagList());

                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }
            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.goodies.Goodies> call, Throwable t) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.failureResponse(progressBar, getView());
            }

        });

    }

    private void manageRecyclerView(List<com.vrd.gsaf.api.responses.goodies.GoodieBag> goodieBagList) {

        goodieAdapter = new GoodiesAdapter(getParentFragmentManager(), goodieBagList, progressBar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));

        recyclerView.setAdapter(goodieAdapter);
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
            case R.id.backImageView:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.menuImageView:
                Helper.menuClick();
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Helper.setActiveItemBottomNav(0);

        } catch (Exception e) {

        }
    }
}