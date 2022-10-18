package com.vrd.gsaf.registration;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.resetpassword.Resetpassword;
import com.vrd.gsaf.app.AppSession;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResetPassword extends Fragment implements View.OnClickListener {


    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    private final Boolean passwordFlag = true;
    private final Boolean confirmPasswordFlag = true;
    private View view;
    private ProgressBar progressBar;
    private Button resetBtn;
    private ImageView backImageView;
    private ImageView passwordVisibleImageView;
    private ImageView confirmPasswordVisibleImageView;
    private EditText passwordEdt;
    private EditText confirmPasswordEdt;
    private TextView confirmPasswordTxt;
    private TextView passwordTxt;
    private long mLastClickTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_password, container, false);
        // Helper.showKeyboard();
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

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        progressBar = view.findViewById(R.id.progressBar);
        confirmPasswordEdt = view.findViewById(R.id.confirmPasswordEdt);
        passwordEdt = view.findViewById(R.id.passwordEdt);
        confirmPasswordTxt = view.findViewById(R.id.confirmPasswordTxt);
        passwordTxt = view.findViewById(R.id.passwordTxt);
        resetBtn = view.findViewById(R.id.resetBtn);
        resetBtn.setText(Singleton.getKeywords().getReset());
        backImageView = view.findViewById(R.id.backImageView);
        passwordVisibleImageView = view.findViewById(R.id.visibleImgView);
        confirmPasswordVisibleImageView = view.findViewById(R.id.confirmVisibleImgView);
        passwordTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEnter() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getPassword());
        confirmPasswordTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEnter() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getConfirm() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getPassword());

        Helper.setButtonColorWithDrawable(resetBtn, Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterBackground(), Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor());

//        confirmPasswordTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCond() + " " +
        //    Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getPassword());
        ImageView icon = view.findViewById(R.id.logo);
        RelativeLayout toolbar = view.findViewById(R.id.toolbar);
        ConstraintLayout mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Singleton.getTopNavColor());
        mainLayout.setBackgroundColor(Singleton.getTopNavColor());
        setClickListeners();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        resetBtn.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        passwordVisibleImageView.setOnClickListener(this);
        confirmPasswordVisibleImageView.setOnClickListener(this);
    }

    private void replaceFragment(Fragment fragment, String tag) {

        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack();
        fm.popBackStack();
        fm.beginTransaction().replace(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .commit();
    }

    private void onBackPress() {
        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack("otp", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
            case R.id.resetBtn:
                // replaceFragment(new Login(), null);

                if (checkRequired())
                    resetPasswordJson();
                break;
            case R.id.backImageView:
                onBackPress();
                break;
            case R.id.visibleImgView:
//                int drawable = R.drawable.visibility;
//                if (passwordFlag) {
//                    drawable = R.drawable.no_visibility;
//                    passwordFlag = false;
//                }
//                visibilityCheck(view, drawable);
                visibilityCheck(passwordEdt, passwordVisibleImageView);
                break;
            case R.id.confirmVisibleImgView:
//                int drawable1 = R.drawable.visibility;
//                ;
//                if (confirmPasswordFlag) {
//                    drawable1 = R.drawable.no_visibility;
//                    confirmPasswordFlag = false;
//                }
//                visibilityCheck(view, drawable1);
                visibilityCheck(confirmPasswordEdt, confirmPasswordVisibleImageView);

                break;
        }
    }

    private boolean checkRequired() {
        if (passwordEdt.getText().toString().equals("")) {
            passwordTxt.setTextColor(getResources().getColor(R.color.required_fileds));
            return false;
        }
        if (confirmPasswordEdt.getText().toString().equals("")) {
            confirmPasswordTxt.setTextColor(getResources().getColor(R.color.required_fileds));
            return false;
        }

        if (passwordEdt.getText().toString().isEmpty() || passwordEdt.getText().toString().length() < 8) {
            passwordEdt.setTextColor(getResources().getColor(R.color.required_fileds));
            passwordEdt.requestFocus();
            Toast.makeText(Singleton.getInstance().getContext(), "Password must contains  8 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (confirmPasswordEdt.getText().toString().isEmpty() || confirmPasswordEdt.getText().toString().length() < 8) {
            confirmPasswordEdt.setTextColor(getResources().getColor(R.color.required_fileds));
            confirmPasswordEdt.requestFocus();
            Toast.makeText(Singleton.getInstance().getContext(), "Password must contains 8 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!confirmPasswordEdt.getText().toString().equals(passwordEdt.getText().toString())) {
            passwordEdt.setTextColor(getResources().getColor(R.color.required_fileds));
            confirmPasswordEdt.setTextColor(getResources().getColor(R.color.required_fileds));
            Toast.makeText(Singleton.getInstance().getContext(), Singleton.getKeywords().getPassword_mismatch(), Toast.LENGTH_LONG).show();
            return false;
        }
//        if (confirmPasswordEdt.getText().toString() != passwordEdt.getText().toString()) {
//            Helper.showToast("Passwords don't match with each other");
//            return false;
        // }

        return true;

    }

    private void visibilityCheck(View view, int drawable) {

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(ContextCompat.getDrawable(Singleton.getInstance().getContext(), drawable));
        } else {
            view.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), drawable));
        }
    }


    @SuppressLint("ResourceType")
    private void visibilityCheck(EditText editText, ImageView imageView) {
        if (editText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            imageView.setImageResource(R.drawable.visibility);
            //Show Password
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            imageView.setImageResource(R.drawable.no_visibility);
            //Hide Password
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        editText.setSelection(editText.getText().length());
    }

    private void resetPasswordApi(JsonObject JsonObject) {
        Call<Resetpassword> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).resetPassword(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<Resetpassword>() {
            @Override
            public void onResponse(Call<Resetpassword> call, Response<Resetpassword> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.body().getStatus()) {
                        Helper.showToast(Singleton.getKeywords().getPassword_updated());
                        replaceFragment(new Login(), null);
                    } else
                        Helper.showToast("Something went wrong...Please try later!");
                } catch (Exception e) {
                    //  Helper.showToast("Something Went Wrong.4");
                }
            }

            @Override
            public void onFailure(Call<Resetpassword> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something Went Wrong. Please try again in a while");

            }

        });
    }

    private void resetPasswordJson() {
        if (Helper.isInternetConnected()) {
            Helper.showProgressBar(progressBar);
            JsonObject JsonObject = new JsonObject();
            JsonObject.addProperty("email", Singleton.getInstance().getEmail());
            JsonObject.addProperty("password", passwordEdt.getText().toString());
            JsonObject.addProperty("password_confirmation", confirmPasswordEdt.getText().toString());
            JsonObject.addProperty("token", Singleton.getInstance().getOtp());
            JsonObject.addProperty("front", 1);
            resetPasswordApi(JsonObject);

        } else
            Helper.noInternetDialog();
    }


}