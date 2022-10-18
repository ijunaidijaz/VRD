package com.vrd.gsaf.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.model.ContentModel;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NeedHelpFragment extends Fragment implements View.OnClickListener {


    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    private View view;
    private ProgressBar progressBar;
    private ImageView menuImageView, backImageView;
    private TextView emailTxt, titleTxt;
    private TextView descriptionTxt;
    private Button sendEmailBtn;
    private long mLastClickTime;

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

        view = inflater.inflate(R.layout.fragment_need_help, container, false);
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        progressBar = view.findViewById(R.id.progressBar);
        sendEmailBtn = view.findViewById(R.id.sendEmailBtn);
        Helper.setButtonColorWithDrawable(sendEmailBtn,
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
        menuImageView = view.findViewById(R.id.menuImageView);
        backImageView = view.findViewById(R.id.backImageView);
        emailTxt = view.findViewById(R.id.emailTxt);
        titleTxt = view.findViewById(R.id.titleTxt);
        TextView websiteTxt = view.findViewById(R.id.websiteTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getNeedHelp());
        emailTxt.setPaintFlags(emailTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        emailTxt.setText(Singleton.getInstance().getFairData().getFair().getEmail());


        descriptionTxt = view.findViewById(R.id.descriptionTxt);
        descriptionTxt.setMovementMethod(new ScrollingMovementMethod());

        descriptionTxt.setMovementMethod(LinkMovementMethod.getInstance());
        Helper.setTextColor(descriptionTxt);
        Helper.setTextColor(emailTxt);
        Helper.setTextColor(websiteTxt);

        if (Singleton.getInstance().getIsDashboard()) {
            backImageView.setVisibility(View.VISIBLE);
            menuImageView.setVisibility(View.GONE);
        } else {
            backImageView.setVisibility(View.GONE);
            menuImageView.setVisibility(View.VISIBLE);
        }
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_email_button_help_section().equals(1)) {
            sendEmailBtn.setVisibility(View.GONE);
            emailTxt.setVisibility(View.GONE);
        }
        setClickListeners();
        getNeedHelpData();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        menuImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        sendEmailBtn.setOnClickListener(this);
        emailTxt.setOnClickListener(this);
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
            case R.id.backImageView:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.sendEmailBtn:
                if (emailTxt.getText().toString() != null)
                    Helper.openEmail(emailTxt.getText().toString());
                break;
            case R.id.emailTxt:
                if (emailTxt.getText().toString() != null)
                    Helper.openEmail(emailTxt.getText().toString());
                break;
        }
    }

    private void getNeedHelpData() {
        Helper.showProgressBar(progressBar);
        String url = "api/auth/fair/help/" + Singleton.getMyFairId();
        Call<ContentModel> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getNeedHelp(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<ContentModel>() {
            @Override
            public void onResponse(Call<ContentModel> call, Response<ContentModel> response) {
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
                        if (response.body().getData() != null && response.body().getData().getFairHelp() != null) {
                            Helper.loadHtml(descriptionTxt, response.body().getData().getFairHelp());
                        }

                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<ContentModel> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
            }

        });

    }

//    private void openEmail() {
//        try {
//            Intent email = new Intent(Intent.ACTION_SEND);
//            email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTxt.getText().toString()});
////                email.putExtra(Intent.EXTRA_SUBJECT, "Waqas");
////                email.putExtra(Intent.EXTRA_TEXT, "Afzal");
//
//            email.setType("message/rfc822");
//            startActivity(Intent.createChooser(email, "Choose an Email client :"));
//        } catch (android.content.ActivityNotFoundException e) {
//            Helper.showToast("There is no email client installed.");
//            // Toast.makeText(Singleton.getInstance().getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
//        }
//    }
}