package com.vrd.gsaf.registration;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.otp.Otp;
import com.vrd.gsaf.app.AppSession;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgetPassword extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    private View view;
    private EditText emailEdt;
    private TextView emailTextView;
    private ImageView backImageView;
    private Button continueBtn;
    private ProgressBar progressBar;
    private long mLastClickTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        Helper.hideKeyboard();
        Singleton.getInstance().setLanguageIndex(AppSession.getInt("languageIndex"));
        initializeViews();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.hideKeyboard();
            }
        });
        return view;
    }


    private void initializeViews() {

        backImageView = view.findViewById(R.id.backImageView);
        continueBtn = view.findViewById(R.id.continueBtn);
        continueBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getContinue());
        Helper.setButtonColorWithDrawable(continueBtn, Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterBackground(), Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor());

        emailEdt = view.findViewById(R.id.emailEdt);

        emailTextView = view.findViewById(R.id.emailTextView);
        emailTextView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEnter() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEmail());
        continueBtn.setText(Singleton.getKeywords().get_continue());
        progressBar = view.findViewById(R.id.progressBar);
        ImageView icon = view.findViewById(R.id.forgetIcon);
        RelativeLayout toolbar = view.findViewById(R.id.toolbar);
        Singleton.changeIconColor(R.drawable.forget, icon);
        Helper.setTextColor(emailEdt);
        Singleton.setBackgroundColor(view);
        toolbar.setBackgroundColor(Singleton.getTopNavColor());
        Helper.setTextColor(emailTextView);
        setClickListeners();
    }

    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        continueBtn.setOnClickListener(this);

    }

    private void replaceFragment(Fragment fragment, String tag) {

        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();
    }


    private void onBackPress() {
        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack("login", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
            case R.id.continueBtn:
                if (!emailEdt.getText().toString().equals(""))
                    sendOtpToEmail();
                else
                    emailTextView.setTextColor(getResources().getColor(R.color.required_fileds));
                break;
            case R.id.emailEdt:
                break;
        }
    }


    private void loginUser(JsonObject JsonObject) {
        Call<Otp> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).sendOTPToEmail(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<Otp>() {
            @Override
            public void onResponse(Call<Otp> call, Response<Otp> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.body().getStatus()) {
                        // Helper.showToast(response.body().getData().getOtp());
//                        Log.d("otp", response.body().getData().getOtp().toString());
                        Singleton.getInstance().setOtp(response.body().getData().getOtp());
                        replaceFragment(new OTP(), "forgetPassword");
                    } else {
                        ResponseBody response1 = response.errorBody();
                        Helper.getErrorMessage(response1);
                    }
                } catch (Exception e) {
                    ResponseBody response1 = response.errorBody();
                    Helper.getErrorMessage(response1);
                }
            }

            @Override
            public void onFailure(Call<Otp> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something Went Wrong. Please try again in a while");

            }

        });
    }

    private void sendOtpToEmail() {
        if (Helper.isInternetConnected()) {
            Helper.showProgressBar(progressBar);
            JsonObject JsonObject = new JsonObject();
            JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
            JsonObject.addProperty("email", emailEdt.getText().toString());
            JsonObject.addProperty("findUrl", "vrd_virtualrecruitmentdays.com");
            Singleton.getInstance().setEmail(emailEdt.getText().toString());
            loginUser(JsonObject);
        } else
            Helper.noInternetDialog();
    }


}
