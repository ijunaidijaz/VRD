package com.vrd.gsaf.registration;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OTP extends Fragment implements View.OnKeyListener, View.OnClickListener {


    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    private View view;
    private ProgressBar progressBar;
    private Button verifyBtn;
    private ImageView backImageView;
    private EditText otp1Edt;
    private EditText otp2Edt;
    private EditText otp3Edt;
    private EditText otp4Edt;
    private long mLastClickTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_o_t_p, container, false);
        Helper.hideKeyboard();
        initializeViews();
        verifyBtn = view.findViewById(R.id.verifyBtn);
        verifyBtn.setText(Singleton.getKeywords().getVerify());
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
        otp1Edt = view.findViewById(R.id.otpEdt1);
        otp2Edt = view.findViewById(R.id.otpEdt2);
        otp3Edt = view.findViewById(R.id.otpEdt3);
        otp4Edt = view.findViewById(R.id.otpEdt4);
        verifyBtn = view.findViewById(R.id.verifyBtn);
        backImageView = view.findViewById(R.id.backImageView);
        TextView txt1 = view.findViewById(R.id.txt1);
        TextView txt2 = view.findViewById(R.id.txt2);
        TextView txt3 = view.findViewById(R.id.txt3);

        Helper.setButtonColorWithDrawable(verifyBtn, Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterBackground(), Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor());
        ImageView icon = view.findViewById(R.id.logo);
        RelativeLayout toolbar = view.findViewById(R.id.toolbar);
        Singleton.changeIconColor(R.drawable.forget, icon);
        Helper.setTextColor(txt1);
        Helper.setTextColor(txt2);
        Helper.setTextColor(txt3);
        Helper.setTextColor(otp1Edt);
        Helper.setTextColor(otp2Edt);
        Helper.setTextColor(otp3Edt);
        Helper.setTextColor(otp4Edt);
        Singleton.setBackgroundColor(view);
        toolbar.setBackgroundColor(Singleton.getTopNavColor());
        setClickListeners();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        otp1Edt.setOnKeyListener(this::onKey);
        otp2Edt.setOnKeyListener(this::onKey);
        otp3Edt.setOnKeyListener(this::onKey);
        otp4Edt.setOnKeyListener(this::onKey);
        verifyBtn.setOnClickListener(this);
        backImageView.setOnClickListener(this);

        otp1Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!otp1Edt.getText().toString().matches(""))
                    otp2Edt.requestFocus();
            }
        });

        otp2Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!otp2Edt.getText().toString().matches(""))
                    otp3Edt.requestFocus();
            }
        });

        otp3Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!otp3Edt.getText().toString().matches(""))
                    otp4Edt.requestFocus();
            }
        });

        otp4Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!otp4Edt.getText().toString().matches(""))
                    Helper.hideKeyboard();
            }
        });

        otp4Edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!otp4Edt.getText().toString().matches(""))
                    otp4Edt.setText("");
                return false;
            }
        });

        otp3Edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!otp3Edt.getText().toString().matches(""))
                    otp3Edt.setText("");
                return false;
            }
        });

        otp2Edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!otp2Edt.getText().toString().matches(""))
                    otp2Edt.setText("");
                return false;
            }
        });

        otp1Edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!otp1Edt.getText().toString().matches(""))
                    otp1Edt.setText("");
                return false;
            }
        });


    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        EditText editText = view.findViewById(view.getId());
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL && view.getId() != R.id.otpEdt1) {
            //If current is empty then previous EditText's number will also be deleted

            if (editText.getText().toString().matches("")) {
                if (editText != otp1Edt) {
                    if (otp2Edt.getId() == editText.getId()) {
                        otp1Edt.requestFocus();
                        otp1Edt.setText("");
                    } else if (otp3Edt.getId() == editText.getId()) {
                        otp2Edt.requestFocus();
                        otp2Edt.setText("");

                    } else if (otp4Edt.getId() == editText.getId()) {
                        otp3Edt.requestFocus();
                        otp3Edt.setText("");
                    }
                    return true;
                }
                return false;
            } else
                editText.setText("");
            return false;
        }


        return false;
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
        fm.popBackStack("forgetPassword", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
            case R.id.verifyBtn:
                //  replaceFragment(new ResetPassword(), "otp");
                if (checkOtp())
                    verifyOtpApi();
                break;
            case R.id.backImageView:
                onBackPress();
                break;
        }
    }

    private boolean checkOtp() {
        if (otp1Edt.getText().toString().equals("")) {
            otp1Edt.setBackgroundColor(getResources().getColor(R.color.required_fileds));
            return false;
        }
        if (otp2Edt.getText().toString().equals("")) {
            otp2Edt.setBackgroundColor(getResources().getColor(R.color.required_fileds));
            return false;
        }
        if (otp3Edt.getText().toString().equals("")) {
            otp3Edt.setBackgroundColor(getResources().getColor(R.color.required_fileds));
            return false;
        }
        if (otp4Edt.getText().toString().equals("")) {
            otp4Edt.setBackgroundColor(getResources().getColor(R.color.required_fileds));
            return false;
        }
        return true;
    }


    private void verifyOtpApi() {
        String otp = otp1Edt.getText().toString() + otp2Edt.getText().toString() + otp3Edt.getText().toString() + otp4Edt.getText().toString();
        Singleton.getInstance().setOtp(otp);
        String url = "api/password/find/" + otp;

        Call<com.vrd.gsaf.api.responses.login.Login> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).verifyOtp(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.login.Login>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.login.Login> call, Response<com.vrd.gsaf.api.responses.login.Login> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.body().getStatus()) {
                        replaceFragment(new ResetPassword(), "otp");
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

    private void verifyOtpJson() {
        String otp = otp1Edt.getText().toString() + otp2Edt.getText().toString() + otp3Edt.getText().toString() + otp4Edt.getText().toString();
        if (otp.equals(Singleton.getInstance().getOtp()))
            replaceFragment(new ResetPassword(), "otp");
        else
            Helper.showToast("Invalid OTP");

    }


}

