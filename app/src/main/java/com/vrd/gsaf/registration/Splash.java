package com.vrd.gsaf.registration;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.vrd.gsaf.BuildConfig;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.events.Fairs;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Splash extends Fragment {

    private View view;
    private Handler handler;
    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();
        view = inflater.inflate(R.layout.fragment_splash, container, false);

        getEvents();

        View retryLayout;
        retryLayout = view.findViewById(R.id.noDataLayout);
//        ConstraintLayout mainLayout=retryLayout.findViewById(R.id.mainLayout);
//        String appLogo=BuildConfig.APP_LOGO;
//        if (BuildConfig.APP_LOGO.contains("vcd")){
//            mainLayout.setBackground(getActivity().getDrawable(R.drawable.gsaf_splash));
//        }
        return view;
    }

    private void retryLayout() {
        View retryLayout;
        retryLayout = view.findViewById(R.id.somethingWentWrongLayout);
        ConstraintLayout mainLayout=retryLayout.findViewById(R.id.mainLayout);
//        String appLogo=BuildConfig.APP_LOGO;
//        if (appLogo.contains("vcd")){
//            mainLayout.setBackground(getActivity().getDrawable(R.drawable.vcd_splash));
//        }
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEvents();
            }
        });
    }


    private void replaceFrament(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void getEvents() {
        if (Helper.isInternetConnected()) {
            getEventsApi();
        }
        try {
            Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
        } catch (Exception e) {

        }
    }

    private void getEventsApi() {
        Call<Fairs> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getEvents(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY);
        call.enqueue(new Callback<Fairs>() {
            @Override
            public void onResponse(Call<Fairs> call, Response<Fairs> response) {

                Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 204) {
                        //Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        Singleton.getInstance().setFairs(response.body());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                replaceFrament(new Events());
                            }
                        }, AppConstants.SPLASH_SCREEN_DISPLAY_TIME);
                    } else {
//                        Log.e("http",response.toString());
//                        Helper.somethingWentWrong(getView());
                        Helper.showToast(response.body().toString() + "1");
                        // Log.e("Something","asd");


                    }
                } catch (Exception e) {
//                    Log.d("minifyresponse", response.body().toString());
                    ResponseBody response1 = response.errorBody();
                    Helper.getErrorMessage(response1);
                    //Helper.showToast("Something Went Wrong.2" +e.getMessage()+" , "+e.getCause() + " , " +e.getStackTrace()+ " , " + e.getLocalizedMessage());
                    //Log.e("http",e.toString());
                    // Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<Fairs> call, Throwable t) {
                //Helper.showToast("Something went wrong...Please try later!");
                Helper.failureResponse(null, getView());

                // Toast.makeText(Singleton.getInstance().getContext(), "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void noInternetDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_no_internet, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        Button tryAgainBtn = dialogView.findViewById(R.id.tryAgainBtn);
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                getEvents();
            }
        });

        alertDialog.show();
    }


}