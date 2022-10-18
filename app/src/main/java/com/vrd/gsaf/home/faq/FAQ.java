package com.vrd.gsaf.home.faq;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.faq.Faq;
import com.vrd.gsaf.api.responses.faq.Faqs;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FAQ extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    FaqAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    private View view;
    private ProgressBar progressBar;
    private ImageView menuImageView, backImageView;
    private TextView titleTxt, noDataText;
    private long mLastClickTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_f_a_q, container, false);
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        titleTxt = view.findViewById(R.id.titleTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFaq());
        progressBar = view.findViewById(R.id.progressBar);
        menuImageView = view.findViewById(R.id.menuImageView);
        backImageView = view.findViewById(R.id.backImageView);
        expListView = view.findViewById(R.id.lvExp);
        noDataText = view.findViewById(R.id.noDataTxt);

        setClickListeners();
        if (Singleton.getInstance().getIsDashboard()) {
            backImageView.setVisibility(View.VISIBLE);
            menuImageView.setVisibility(View.GONE);
        } else {
            backImageView.setVisibility(View.GONE);
            menuImageView.setVisibility(View.VISIBLE);
        }

        if (Helper.isInternetConnected()) {
            callApiForFaq();
        }
        retryLayout();
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

    }

    private void retryLayout() {
        View retryLayout;
        retryLayout = view.findViewById(R.id.somethingWentWrongLayout);
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.isInternetConnected()) {
                    callApiForFaq();
                }
            }
        });
    }

    private void prepareListData(List<Faq> faqList) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        for (int i = 0; i < faqList.size(); i++) {
            listDataHeader.add(faqList.get(i).getQuestion());
            List<String> top250 = new ArrayList<String>();
            top250.add(faqList.get(i).getAnswer());
            listDataChild.put(listDataHeader.get(i), top250);
        }
        if (listDataHeader == null || listDataHeader.isEmpty()) {
            noDataText.setVisibility(View.VISIBLE);
            noDataText.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTrans_string_oops_no_data_found());
        }
        listAdapter = new FaqAdapter(listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.setGroupIndicator(null);
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                int len = listAdapter.getGroupCount();

                for (int i = 0; i < len; i++) {
                    if (i != groupPosition) {
                        expListView.collapseGroup(i);
                    }
                }
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        menuImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);
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
            case R.id.backImageView:
                onBackPress();
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

    private void callApiForFaq() {
        Helper.showProgressBar(progressBar);
        String url = "api/auth/fair/faqs/" + Singleton.getInstance().getFairData().getFair().getId().toString();
        Call<Faqs> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getFaq(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<Faqs>() {
            @Override
            public void onResponse(Call<Faqs> call, Response<Faqs> response) {
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
                        prepareListData(response.body().getData().getFaqList());
                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<Faqs> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
            }

        });
    }
}