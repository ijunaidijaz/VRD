package com.vrd.gsaf.registration;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.app.AppSession;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.dashboard.careeTest.CareerTest;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.Objects;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends Fragment implements View.OnClickListener, View.OnTouchListener {


    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    protected float dX;
    protected float dY;
    protected int lastAction;
    long lastDown;
    long lastDuration;
    int languageIndex = Singleton.getInstance().getLanguageIndex();
    private View view;
    private ProgressBar progressBar;
    private ConstraintLayout registerLayout, loginWithOtherOptionsLayout;
    private Button loginBtn;
    private ImageView backImageView, passwordVisibleImageView, draggable_view, backgroundImage;
    private TextView forgetPasswordTxt, titleTxt;
    private TextView userNameTxt, txt;
    private TextView passwordTxt, registerTxt;
    private EditText userNameEdt;
    private EditText passwordEdt;
    private long mLastClickTime;

    public static void loadHome() {
        Singleton.getInstance().getActivity().finish();
        Singleton.getInstance().getActivity().overridePendingTransition(0, 0);
        MainApp.getAppContext().startActivity(Singleton.getInstance().getActivity().getIntent());
        Singleton.getInstance().getActivity().overridePendingTransition(0, 0);
//        FragmentManager fm = getParentFragmentManager();
//        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//            fm.popBackStack();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        Helper.hideKeyboard();
        initializeViews();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.hideKeyboard();
            }
        });
        new Handler().postDelayed(() -> {
            if (Singleton.getInstance().getBottomNavigationView() != null) {
                Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
            }
            Helper.enableTouch();
        }, 400);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            requireActivity().getWindow().setNavigationBarColor(parseColor("#ffffff"));
//        }
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            Window window =  requireActivity().getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(parseColor("#000000"));
//
//        }
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        loginBtn = view.findViewById(R.id.logInBtn);
        txt = view.findViewById(R.id.txt);
        titleTxt = view.findViewById(R.id.titleTxt);
        backgroundImage = view.findViewById(R.id.background_image_view);

        registerLayout = view.findViewById(R.id.registerLayout);
        loginWithOtherOptionsLayout = view.findViewById(R.id.loginWithOtherOptionsLayout);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableRegistrationFront() == 1)
            registerLayout.setVisibility(View.GONE);
        backImageView = view.findViewById(R.id.backImageView);
        passwordVisibleImageView = view.findViewById(R.id.passwordVisibleImageView);
        forgetPasswordTxt = view.findViewById(R.id.forgetPasswordTxt);
        userNameEdt = view.findViewById(R.id.userNameEdt);
        passwordEdt = view.findViewById(R.id.passwordEdt);
        userNameTxt = view.findViewById(R.id.userNameTxt);

        passwordTxt = view.findViewById(R.id.passwordTxt);
        passwordTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getPassword());

        progressBar = view.findViewById(R.id.progressBar);
        draggable_view = view.findViewById(R.id.draggable_view);
        registerTxt = view.findViewById(R.id.registerTxt);
        setClickListeners();
        Helper.setButtonColorWithDrawable(loginBtn, Singleton.getOptions().getTopnavBackgroundColor(), Singleton.getOptions().getTopnavInnerTextColor());
        Helper.loadImageWithGlideNoPlaceHolder(backgroundImage, AppConstants.getBackgroundImage());

        setKeyWords();
        ImageView appLogo = view.findViewById(R.id.logo);
        Glide.with(Singleton.getInstance().getContext()).
                load(AppConstants.APP_LOGO)
                .placeholder(R.drawable.rectangluar_placeholder)
                .into(appLogo);
        // Singleton.getInstance().setLogout(false);

    }

    private void setKeyWords() {
        loginBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getLogin());
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getLogin());

        userNameTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getEmail());
        passwordTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getPassword());
        loginBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getLogin());
        forgetPasswordTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getForgetPassword());
        txt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getDontHaveAnAccount());
        registerTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords().getRegister());

    }

    private void setClickListeners() {
        registerLayout.setOnClickListener(this);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getEnablePublicSsoAttendeeSide() != null) {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getEnablePublicSsoAttendeeSide().equals(1)) {
                loginWithOtherOptionsLayout.setVisibility(View.VISIBLE);
                loginWithOtherOptionsLayout.setOnClickListener(this);
            }
        }
        loginBtn.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        forgetPasswordTxt.setOnClickListener(this);
        passwordVisibleImageView.setOnClickListener(this);
        draggable_view.setOnTouchListener(this);
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
            case R.id.registerLayout:
                onBackPress();
                break;
            case R.id.logInBtn:
                createLoginJsonObject();
                break;
            case R.id.backImageView:
                onBackPress();
                break;
            case R.id.forgetPasswordTxt:
                replaceFragment(new ForgetPassword(), "login");
                break;
            case R.id.passwordVisibleImageView:
                visibilityCheck();
                break;
            case R.id.loginWithOtherOptionsLayout:
                replaceFragment(new RegistrationOptions(), "login");
                break;
        }
    }

    @SuppressLint("ResourceType")
    private void visibilityCheck() {
        if (passwordEdt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            passwordVisibleImageView.setImageResource(R.drawable.visibility);
            //Show Password
            passwordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            passwordVisibleImageView.setImageResource(R.drawable.no_visibility);
            //Hide Password
            passwordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        passwordEdt.setSelection(passwordEdt.getText().length());
    }

    private void replaceFragment(Fragment fragment, String tag) {

        Bundle args = new Bundle();
        args.putString("from", "login");
        fragment.setArguments(args);
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();
    }

    private void onBackPress() {
        FragmentManager fm = getParentFragmentManager();
        if (!Singleton.getInstance().getFlag()) {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableRegistrationFront() == 0)
                fm.popBackStack("register", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            else {
                fm.popBackStackImmediate();
            }
        } else {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableRegistrationFront() != 0) {
                fm.beginTransaction().replace(R.id.frameLayout, new Events())
                        .setReorderingAllowed(false)
                        .commit();
            } else {
                fm.beginTransaction().replace(R.id.frameLayout, new Registration())
                        .setReorderingAllowed(true)
                        .commit();
            }
        }


    }

    private void createLoginJsonObject() {
        Helper.hideKeyboard();

//        if (checkRequired(userNameEdt, userNameTxt)) {

        //  if (passwordEdt.getText().toString().matches(AppConstants.PASSWORD_REGEX)) {
        if (!userNameEdt.getText().toString().isEmpty()) {
            if (Helper.isEmailValid(userNameEdt.getText().toString())) {
                if (checkRequired(passwordEdt, passwordTxt)) {
                    if (isValidDomain(userNameEdt)) {
                        if (Helper.isInternetConnected()) {
                            Helper.showProgressBar(progressBar);
                            JsonObject JsonObject = new JsonObject();
                            JsonObject.addProperty("fair_id", Singleton.getInstance().getFairId());
                            JsonObject.addProperty("timezone", TimeZone.getDefault().getID());
                            JsonObject.addProperty("email", userNameEdt.getText().toString());
                            //      JsonObject.addProperty("email", "alfi@testads.com");
                            JsonObject.addProperty("password", passwordEdt.getText().toString());
                            //    JsonObject.addProperty("password", "qwdaeqwe");
                            JsonObject.addProperty("device_type", "android");
                            //    JsonObject.addProperty("password", "qwdaeqwe");
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            JsonObject.addProperty("device_id", task.getResult());
                                            loginUser(JsonObject);
                                        } else Helper.showToast("Firebase token error");
                                    }

                            );
                        } else
                            Helper.noInternetDialog();
                    } else
                        Helper.showToast(Singleton.getKeywords().getTransPleaseEnterValidEmail());
                } else Helper.showToast(Singleton.getKeywords().getTransPleaseEnterPassword());
            } else
                userNameEdt.setError(Singleton.getKeywords().getTransPleaseEnterValidEmail());
        } else Helper.showToast(Singleton.getKeywords().getTransPleaseEnterEmail());
//                } else {
//                    passwordEdt.setTextColor(getResources().getColor(R.color.required_fileds));
//                    Toast.makeText(Singleton.getInstance().getContext(), "Password must contains a special character, a lower case and an upper case character, an integer and length must be greater than 7", Toast.LENGTH_LONG).show();
//                    userNameEdt.requestFocus();
//                }


//        } else {
//            userNameEdt.setTextColor(getResources().getColor(R.color.required_fileds));
//            Helper.showToast("Email Pattern is not valid");
//            userNameEdt.requestFocus();
//        }
    }

    private void loginUser(JsonObject JsonObject) {
        Call<com.vrd.gsaf.api.responses.login.Login> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).loginUser(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.login.Login>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.login.Login> call, Response<com.vrd.gsaf.api.responses.login.Login> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.body().getStatus()) {
                        updateSp(response);
                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
                    ResponseBody response1 = response.errorBody();
                    Helper.getErrorMessage(response1);
                }

            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.login.Login> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something Went Wrong. Please try again in a while");

            }

        });
    }

    private boolean checkRequired(EditText editText, TextView textView) {

        if (editText.getText().toString().equals("")) {
            textView.setTextColor(getResources().getColor(R.color.required_fileds));
            editText.requestFocus();
            return false;
        } else {
            textView.setTextColor(getResources().getColor(R.color.white));
        }


        return true;
    }

    private void updateSp(Response<com.vrd.gsaf.api.responses.login.Login> response) {
        Singleton.getInstance().setLoginData(response.body().getData());
        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
        editor.putBoolean("login", true);
        Gson gson = new Gson();
        String json = gson.toJson(Singleton.getInstance().getLoginData());
        editor.putString("loginData", json);
        editor.apply();
        if (Singleton.getInstance().getLoginData().getUser().getTakeTest() == 0 && (Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("1") || Singleton.getInstance().getFairData().getFair().getOptions().getEnable_force_questionnaire_after_registration_front() == 1)) {
            replaceFragment(new CareerTest(), "login");
        } else {
            SharedPreferences.Editor editor2 = Singleton.getInstance().getSharedPreferences().edit();
            editor2.putBoolean("careerTest", true);
            editor2.apply();
            loadHome();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                lastDown = System.currentTimeMillis();

                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                lastDuration = System.currentTimeMillis() - lastDown;
                if (lastDuration < 150) {
                    //  if (lastAction == MotionEvent.ACTION_DOWN) {
                    Singleton.getInstance().setIsLoggedIn(false);
                    loadHome();

                }
                break;

            default:
                return false;
        }
        return true;
    }

    public boolean isValidDomain(EditText emailEdt) {
        if (Singleton.getInstance().getFairData().getFair().getOptions().getEmailDomain() != null && !Singleton.getInstance().getFairData().getFair().getOptions().getEmailDomain().isEmpty()) {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getEmailDomainOperator().equalsIgnoreCase("0")) {
                if (!Singleton.getInstance().getFairData().getFair().getOptions().getEmailDomain().equalsIgnoreCase(getEmailDomain(emailEdt.getText().toString()))) {
                    emailEdt.requestFocus();
                    emailEdt.setTextColor(getResources().getColor(R.color.required_fileds));
                    Helper.showToast(Singleton.getKeywords().getEmail_domain_not_allowed());
                    return false;
                }
            } else if (Singleton.getInstance().getFairData().getFair().getOptions().getEmailDomainOperator().equalsIgnoreCase("1")) {
                if (Singleton.getInstance().getFairData().getFair().getOptions().getEmailDomain().equalsIgnoreCase(getEmailDomain(emailEdt.getText().toString()))) {
                    emailEdt.requestFocus();
                    emailEdt.setTextColor(getResources().getColor(R.color.required_fileds));
                    Helper.showToast(Objects.requireNonNull(Singleton.getKeywords()).getEmail_domain_not_allowed());
                    return false;
                }
            }
        }
        return true;
    }

    public String getEmailDomain(String someEmail) {
        return "@" + someEmail.substring(someEmail.indexOf("@") + 1);

    }
}