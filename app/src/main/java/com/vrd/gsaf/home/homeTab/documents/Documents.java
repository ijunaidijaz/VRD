package com.vrd.gsaf.home.homeTab.documents;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.compnayDetail.CompanyDetails;
import com.vrd.gsaf.api.responses.compnayDetail.Data;
import com.vrd.gsaf.api.responses.fairMedia.FairMedia;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Documents extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView;
    private RecyclerView recyclerView;
    private DocumentsAdapter adapter;
    private WebView webView;
    private long mLastClickTime;
    private TextView titleTxt;
    boolean isFAQs = false;

    @Override
    public void onResume() {
        //playVideo();
        super.onResume();
        try {
            Helper.setActiveItemBottomNav(0);

        } catch (Exception e) {
            Helper.showToast("asds");
        }
        // playVideo();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_documents, container, false);

        Helper.hideKeyboard();
        initializeViews();
        return view;
    }


    @SuppressLint("CutPasteId")
    private void initializeViews() {
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        backImageView = view.findViewById(R.id.backImageView);
        webView = view.findViewById(R.id.webView1);
        titleTxt = view.findViewById(R.id.titleTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDocuments());
        setClickListeners();
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
        if (getArguments() != null && getArguments().containsKey("isFaq")) {
            isFAQs = true;
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        if (Helper.isInternetConnected()) {
            if (Singleton.getInstance().getHomeState().equals("stands")) {
                Helper.showProgressBar(progressBar);
                callApiForCompanyDetail(false);
            } else if (Singleton.getInstance().getHomeState().equals("reception")) {
                callApiForFairMedia();
            }
        }
        retryLayout();
        // manageRecyclerView();
    }

    private void callApiForFairMedia() {

        Helper.showProgressBar(progressBar);
        String url = "api/fair/media/list/" + Singleton.getInstance().getFairData().getFair().getId().toString();
        Call<FairMedia> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getFairMedia(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<FairMedia>() {
            @Override
            public void onResponse(Call<FairMedia> call, Response<FairMedia> response) {
                Helper.hideProgressBar(progressBar);
                //Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        manageRecyclerViewForReception(response.body().getData());
                        // prepareListData(response.body().getData().getFaqList());
                    } else {
                        Helper.somethingWentWrong(getView());
                    }
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }
            }

            @Override
            public void onFailure(Call<FairMedia> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
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
                    if (Singleton.getInstance().getHomeState().equals("stands")) {
                        Helper.showProgressBar(progressBar);
                        callApiForCompanyDetail(false);
                    } else if (Singleton.getInstance().getHomeState().equals("reception")) {
                        callApiForFairMedia();
                    }
                }
            }
        });
    }

    private void callApiForCompanyDetail(boolean pagination) {

        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());

        Call<CompanyDetails> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getCompanyDetail(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<CompanyDetails>() {
            @Override
            public void onResponse(Call<CompanyDetails> call, Response<CompanyDetails> response) {
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
                        manageRecyclerView(response.body().getData());
                        //  setData(response.body().getData());
                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<CompanyDetails> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
            }

        });

    }


    private void manageRecyclerViewForReception(com.vrd.gsaf.api.responses.fairMedia.Data data) {

        List<DocumentModel> documentModels = new ArrayList<>();
        DocumentModel model = null;
        for (int i = 0; i < data.getMediaList().size(); i++) {
            if (isFAQs) {
                if (data.getMediaList().get(i).getFairMediaType().equals("faqs")) {
                    model = new DocumentModel(data.getMediaList().get(i).getFairMediaDocument(), data.getMediaList().get(i).getFairMediaName());
                    documentModels.add(model);
                    model = null;
                }
            } else {
                if (data.getMediaList().get(i).getFairMediaType().equals("document")) {
                    model = new DocumentModel(data.getMediaList().get(i).getFairMediaDocument(), data.getMediaList().get(i).getFairMediaName());
                    documentModels.add(model);
                    model = null;
                }
            }
        }

        if (documentModels.size() > 0) {
            adapter = new DocumentsAdapter(getParentFragmentManager(), documentModels, webView, progressBar);

            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        } else
            Helper.noDataFound(view);

    }


    private void manageRecyclerView(Data data) {
        List<DocumentModel> documentModels = new ArrayList<>();
        DocumentModel model = null;
        for (int i = 0; i < data.getCompanyList().get(0).getCompanyMedia().size(); i++) {
            if (data.getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaType().equals("document")) {
                model = new DocumentModel(data.getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaDocument(), data.getCompanyList().get(0).getCompanyMedia().get(i).getCompanyMediaName());
                documentModels.add(model);
                model = null;
            }
        }

        if (documentModels.size() > 0) {
            adapter = new DocumentsAdapter(getParentFragmentManager(), documentModels, webView, progressBar);

            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        } else
            Helper.noDataFound(view);

    }

    private void onBackPress() {
        FragmentManager fm = getParentFragmentManager();
        fm.popBackStackImmediate();
        //fm.popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
            case R.id.menuImageView:
                Helper.menuClick();
                break;
        }
    }


}