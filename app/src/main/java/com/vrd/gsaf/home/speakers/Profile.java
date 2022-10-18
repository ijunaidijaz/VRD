package com.vrd.gsaf.home.speakers;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.speakerDetail.DetailList;
import com.vrd.gsaf.api.responses.speakerDetail.SpeakerDetail;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.databinding.FragmentProfileBinding;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile extends Fragment implements View.OnClickListener {

    FragmentProfileBinding binding;
    DetailList detailList;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView;
    private ImageView speakerImageView;
    private TextView descriptionTxt;
    private TextView nameTxt;
    private TextView companyName;
    private int index;

    @Override
    public void onResume() {
        //playVideo();
        super.onResume();
        try {
            Helper.setActiveItemBottomNav(0);

        } catch (Exception e) {

        }
        // playVideo();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_profile, container, false);
        view = binding.getRoot();
        Helper.hideKeyboard();
        index = getArguments().getInt("index");
        initializeViews();


        return view;
    }


    @SuppressLint("CutPasteId")
    private void initializeViews() {
        progressBar = view.findViewById(R.id.progressBar);
        backImageView = view.findViewById(R.id.backImageView);
        descriptionTxt = view.findViewById(R.id.descriptionTxt);
        nameTxt = view.findViewById(R.id.websiteTxt);
        companyName = view.findViewById(R.id.emailTextView);
        speakerImageView = view.findViewById(R.id.userImageView);

        descriptionTxt.setMovementMethod(new ScrollingMovementMethod());
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
        setClickListeners();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        binding.twitter.setOnClickListener(this);
        binding.insta.setOnClickListener(this);
        binding.linkedIn.setOnClickListener(this);
        binding.facebook.setOnClickListener(this);
        if (Helper.isInternetConnected()) {
            callApiForSpeakerDetailApi(index);
            Helper.showProgressBar(progressBar);
        }
    }

    private void callApiForSpeakerDetailApi(int index) {
        Helper.showProgressBar(progressBar);
        String url = "api/auth/get/speaker/detail/" + Singleton.getInstance().getSpeakerData().get(index).getId();
        Call<SpeakerDetail> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getSpeakerDetail(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<SpeakerDetail>() {
            @Override
            public void onResponse(Call<SpeakerDetail> call, Response<SpeakerDetail> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.body().getStatus()) {
                        if (response.body().getData().getDetailList().size() > 0)
                            setData(response.body().getData().getDetailList().get(0));
                        else
                            Helper.noDataFound(getView());
                    }
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }
            }

            @Override
            public void onFailure(Call<SpeakerDetail> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
            }

        });

    }

    private void setData(DetailList detailList) {
        this.detailList = detailList;
        nameTxt.setText(detailList.getUserName());
        if (!detailList.getTitle().equals("")) {
            companyName.setVisibility(View.VISIBLE);
            companyName.setText(detailList.getTitle());
        }

        companyName.setVisibility(View.VISIBLE);
        companyName.setText(Singleton.getInstance().getSpeakerTitle());
        descriptionTxt.setMovementMethod(new ScrollingMovementMethod());

        descriptionTxt.setMovementMethod(LinkMovementMethod.getInstance());
        Helper.loadHtml(descriptionTxt, detailList.getUserDetails());
        Glide.with(Singleton.getInstance().getContext()).load(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + detailList.getUserImage())
                .placeholder(R.drawable.add_emoji)
                .apply(RequestOptions.circleCropTransform()).
                error(R.drawable.add_emoji).into(speakerImageView);
        if (detailList.getUserFacebook() != null && !detailList.getUserFacebook().isEmpty()) {
            binding.facebook.setVisibility(View.VISIBLE);
        }
        if (detailList.getUserInstagram() != null && !detailList.getUserInstagram().isEmpty()) {
            binding.insta.setVisibility(View.VISIBLE);
        }
        if (detailList.getUserLinkedin() != null && !detailList.getUserLinkedin().isEmpty()) {
            binding.linkedIn.setVisibility(View.VISIBLE);
        }
        if (detailList.getUserTwitter() != null && !detailList.getUserTwitter().isEmpty()) {
            binding.twitter.setVisibility(View.VISIBLE);
        }
        if (detailList.getUserTwitter() != null && !detailList.getUserTwitter().isEmpty()) {
            binding.youtube.setVisibility(View.VISIBLE);
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


    private void onBackPress() {

        FragmentManager fm = getParentFragmentManager();
        getActivity().getSupportFragmentManager().popBackStack();
        // fm.popBackStack("speakers", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                onBackPress();
                break;
            case R.id.facebook:
                Helper.viewDocument(detailList.getUserFacebook());
                break;
            case R.id.insta:
                Helper.viewDocument(detailList.getUserInstagram());
                break;
            case R.id.linkedIn:
                Helper.viewDocument(detailList.getUserLinkedin());
                break;
            case R.id.twitter:
                Helper.viewDocument(detailList.getUserTwitter());
                break;

        }
    }


}