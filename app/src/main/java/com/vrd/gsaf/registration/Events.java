package com.vrd.gsaf.registration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vrd.gsaf.BuildConfig;
import com.vrd.gsaf.R;
import com.vrd.gsaf.activity.MainActivity;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.PrivacyAndConditionsReponse;
import com.vrd.gsaf.api.responses.fairDetail.Data;
import com.vrd.gsaf.api.responses.fairDetail.FairDetail;
import com.vrd.gsaf.api.responses.fairDetail.FairLanguage;
import com.vrd.gsaf.api.responses.fairDetail.FairTranslation;
import com.vrd.gsaf.app.AppSession;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.DateTime;
import com.vrd.gsaf.utility.Helper;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Events extends Fragment implements View.OnClickListener, LocationListener {

    private static final long MIN_CLICK_INTERVAL = 400;
    public static boolean isViewClicked = false;
    static boolean clicked = false;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    boolean forLanguage = false;
    List<Address> addresses;
    boolean locationChanged = false;
    ArrayList<String> languages = new ArrayList<>();
    private View view;
    private TextView textView, languageText;
    private ArrayList<String> eventsList;
    private Button continueBtn;
    private ProgressBar progressBar;
    private LocationManager locationManager;
    private Location userLocation;
    private long mLastClickTime;
    private ImageView bgImageView;

    public static Data setLanguageOrder(Response<FairDetail> response) {
        Data fairData = response.body().getData();
        List<FairLanguage> languages = fairData.getFair().getFairLanguages();
        List<FairTranslation> translations = fairData.getFair().getFairTranslations();
        List<FairTranslation> newTranslations = new ArrayList<>();
        List<String> lanCodes = new ArrayList<>();
        for (FairLanguage fairLanguage : languages) {
            lanCodes.add(fairLanguage.getAutoId());
        }
        for (String code : lanCodes) {
            for (FairTranslation translation : translations) {
                if (translation.getCode().equals(code)) {
                    newTranslations.add(translation);
                }
            }
        }

        fairData.getFair().setFairTranslations(newTranslations);
        Singleton.getInstance().setFairData(fairData);
        return fairData;
    }

    public static void getFairCloseContent(ProgressBar progressBar, String fairId) {
        clicked = false;
        if (progressBar != null) Helper.showProgressBar(progressBar);
        String url = "api/auth/fair/fairclosecontent/" + fairId;

        Call<PrivacyAndConditionsReponse> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getPrivacyAndConditions(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<PrivacyAndConditionsReponse>() {
            @Override
            public void onResponse(Call<PrivacyAndConditionsReponse> call, Response<PrivacyAndConditionsReponse> response) {
                if (progressBar != null) Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.body().getStatus()) {
//                        parseResponse(response);
                        openTermAndConditionsDialog(response.body().getPrivacyCondition().getFairCloseContent());
                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
                    ResponseBody response1 = response.errorBody();
                    Helper.getErrorMessage(response1);
//                    Helper.showToast("Something went wrong...Please try later!");
                }
            }

            @Override
            public void onFailure(Call<PrivacyAndConditionsReponse> call, Throwable t) {
                if (progressBar != null) Helper.hideProgressBar(progressBar);
                Helper.showToast("Something went wrong...Please try later!");
                // Toast.makeText(Singleton.getInstance().getContext(), "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void openTermAndConditionsDialog(String data) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_term_and_conditions, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        ImageView cancel = dialogView.findViewById(R.id.cancelImageView);
        TextView webinarTitleTxt = dialogView.findViewById(R.id.webinarTitleTxt);
        TextView descriptionTxt = dialogView.findViewById(R.id.descriptionTxt);
        webinarTitleTxt.setText("Fair Closed");
        Helper.loadHtml(descriptionTxt, data);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
//        ViewGroup.LayoutParams params = dialogView.getLayoutParams();
//        params.height = height-200;
//        params.width = width-200;
//        dialogView.setLayoutParams(params);

//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
//        layoutParams.width = height-200;
//        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT-200;
//        alertDialog.getWindow().setAttributes(layoutParams);
        ViewGroup.LayoutParams window = dialogView.getLayoutParams();

        // window.setLayout(width-100, height/2);
//        window.height=height/2;

        alertDialog.getWindow().setLayout(200, 400);
        alertDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();


        view = inflater.inflate(R.layout.fragment_event, container, false);
        bgImageView = view.findViewById(R.id.bgImageView);
        // Helper.loadRectangleImageFromUrlWithRounded(bgImageView,"data:image/jpeg;base64,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",0);


        initializeViews();
        ConstraintLayout mainLayout = view.findViewById(R.id.mainLayout);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.parseColor(BuildConfig.APP_COLOR),
                        Color.parseColor(BuildConfig.APP_MID_COLOR),
                        Color.parseColor(BuildConfig.END_COLOR)});
        gd.setCornerRadius(0f);
        mainLayout.setBackground(gd);
        return view;
    }

    private void initializeViews() {
        textView = view.findViewById(R.id.dropDownTxt);
        languageText = view.findViewById(R.id.languageTxt);
        continueBtn = view.findViewById(R.id.continueBtn);
        progressBar = view.findViewById(R.id.progressBar);
        ImageView appLogo = view.findViewById(R.id.logo);
        Glide.with(Singleton.getInstance().getContext()).
                load(AppConstants.APP_LOGO)
                .placeholder(R.drawable.rectangluar_placeholder)
                .into(appLogo);
        loadSpinnerValues();
        //getEvents();
        setClickListeners();
    }

    private void setClickListeners() {
        continueBtn.setOnClickListener(this);
        textView.setOnClickListener(this);
        languageText.setOnClickListener(this);
        //getLocation();
    }

    private void replaceFragment(Fragment fragment, String tag) {
        try {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().replace(R.id.frameLayout, fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(tag)
                    .commitAllowingStateLoss();
        } catch (Exception e) {

        }
    }

    private void openDropDown() {
        Dialog dialog = new Dialog(getView().getContext());
        dialog.setContentView(R.layout.dialog_searchable_spineer);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        dialog.getWindow().setLayout(width - 100, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        EditText editText = dialog.findViewById(R.id.edtText);
        ListView listView = dialog.findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Singleton.getInstance().getActivity(),
                R.layout.view_events_spinner, eventsList);
        listView.setAdapter(arrayAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                textView.setText(arrayAdapter.getItem(i));
                Singleton.getInstance().setFairId(Singleton.getInstance().getFairs().getData().get(i).getId());
                SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
                editor.commit();
                editor.apply();
                Singleton.getInstance().setFairId(Singleton.getInstance().getFairs().getData().get(i).getId());
                Singleton.getInstance().setShortname(Singleton.getInstance().getFairs().getData().get(i).getShortName());
                dialog.cancel();
                getLocation();

            }

        });
    }

    private void getFairDetails() {
        locationChanged = false;
        String location;
        try {
            location = addresses.get(0).getCountryName() + "," + addresses.get(0).getLocality();
        } catch (Exception e) {
            location = userLocation.getLatitude() + "," + userLocation.getLongitude();

        }
        if (Helper.isInternetConnected()) {
            JsonObject JsonObject = new JsonObject();
            //JsonObject.addProperty("fair_id", Singleton.getInstance().getFairId());
            JsonObject.addProperty("fair_id", Singleton.getInstance().getFairId());
            //  JsonObject.addProperty("fair_id","53");
            JsonObject.addProperty("timezone", TimeZone.getDefault().getID());
            JsonObject.addProperty("ip", Helper.getIPAddress(true));
            JsonObject.addProperty("location", location);
            JsonObject.addProperty("device", "android");
            JsonObject.addProperty("browser", "chrome");
            getFairDetailsApi(JsonObject);
        } else {
            Helper.noInternetDialog();
            Helper.hideProgressBar(progressBar);
        }
    }

    private void getFairDetailsApi(JsonObject JsonObject) {
        Helper.showProgressBar(progressBar);
        Call<FairDetail> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getFairDetails(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<FairDetail>() {
            @Override
            public void onResponse(Call<FairDetail> call, Response<FairDetail> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.body().getStatus()) {
                        parseResponse(response);
                    } else
                        Helper.showToast("Something went wrong. Please try again");

                } catch (Exception e) {
//                    Helper.showToast("Something went wrong...Please try later!");
                    ResponseBody response1 = response.errorBody();
                    Helper.getErrorMessage(response1);
                }
            }

            @Override
            public void onFailure(Call<FairDetail> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something went wrong...Please try later!");
                //Toast.makeText(Singleton.getInstance().getContext(), "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void parseResponse(Response<FairDetail> response) {
        Helper.hideProgressBar(progressBar);
        assert response.body() != null;
        Data fairData = setLanguageOrder(response);
        Singleton.getInstance().setFairData(fairData);
        languages.clear();
        if (fairData.getFair().getFairLanguages().size() > 2) {
            for (FairLanguage fairLanguage : fairData.getFair().getFairLanguages()) {
                if (!fairLanguage.getLangName().equalsIgnoreCase("Default")) {
                    languages.add(fairLanguage.getLangName());
                }
            }
            languageText.setVisibility(View.VISIBLE);
        } else {
            languageText.setText(fairData.getFair().getFairLanguages().get(0).getLangName());
            Singleton.getInstance().setLanguage(fairData.getFair().getFairLanguages().get(0).getAutoId());
            AppSession.put("languageIndex", 0);
            Singleton.getInstance().setLanguageIndex(0);
            languageText.setVisibility(View.GONE);
        }
        if (forLanguage) {
            forLanguage = false;
            return;
        }
//        response.body().getData().getFair().setFairTranslations(newTranslations);


    }

    private void goToRegistration() {
        Data fairData = Singleton.getInstance().getFairData();
        String fairEnd = Singleton.getFair().getFairTiming().getFairEnd();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date1 = format.parse(fairEnd);
            Date currentDate = format.parse(DateTime.getCurrentDateTime());
            assert date1 != null;
            if (!date1.equals(currentDate) && date1.after(currentDate)) {

                SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
                editor.clear();
                Gson gson = new Gson();
                String json = gson.toJson(fairData);
                editor.putInt("fairId", fairData.getFair().getId());
                editor.putString("fairDetails", json);
                editor.putInt("goodieCount", 0);
//                Singleton.getInstance().setLanguageIndex(0);

//                for (int i = 0; i < Singleton.getInstance().getFairData().getFair().getFairTranslations().size(); i++) {
//                    if (Singleton.getInstance().getFairData().getFair().getFairTranslations().get(i).getCode().equals(Singleton.getInstance().getFairData().getFair().getDefaultLanguage().getAutoId())) {
//                        Singleton.getInstance().setLanguageIndex(i);
//                        editor.putInt("languageIndex", i);
//                        break;
//                    }
//                }

//        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableRegistrationFront() == 0)
//            replaceFragment(new Registration());
//        else
//            replaceFragment(new Login());
                editor.apply();
                Singleton.saveChatDetails(fairData.getChatApiDetails());
                MainActivity.getInstance().bufferVideos();
                if (clicked) getExtraFields();


            } else {
                if (clicked)
                    getFairCloseContent(progressBar, String.valueOf(Singleton.getInstance().getFairData().getFair().getId()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getExtraFields() {
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableRegistrationFront() == 0) {
            replaceFragment(new Registration(), "registration");
        } else if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableLoginFront() != 0) {
            Singleton.getInstance().setIsLoggedIn(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Singleton.getInstance().getActivity().finish();
                    Singleton.getInstance().getActivity().overridePendingTransition(0, 0);
                    MainApp.getAppContext().startActivity(Singleton.getInstance().getActivity().getIntent());
                    Singleton.getInstance().getActivity().overridePendingTransition(0, 0);
                }
            }, 200);

        } else
            replaceFragment(new Login(), "login");
        clicked = false;
//        if (Helper.isInternetConnected()) {
//            JsonObject JsonObject = new JsonObject();
//            JsonObject.addProperty("fair_id", Singleton.getInstance().getSharedPreferences().getInt("fairId", 0));
//            // JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginDate().getUser().getId());
//            getFairExtraFieldsApi(JsonObject);
//        }
    }


    private void loadSpinnerValues() {
        eventsList = new ArrayList<>();
        if (!Singleton.getInstance().getFairs().getData().isEmpty() && Singleton.getInstance().getFairs().getData().size() == 1) {
            textView.setText(Singleton.getInstance().getFairs().getData().get(0).getName());
            Singleton.getInstance().setFairId(Singleton.getInstance().getFairs().getData().get(0).getId());
            SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
            editor.commit();
            editor.apply();
            Singleton.getInstance().setFairId(Singleton.getInstance().getFairs().getData().get(0).getId());
            Singleton.getInstance().setShortname(Singleton.getInstance().getFairs().getData().get(0).getShortName());
            getLocation();
        } else {
            for (int i = 0; i < Singleton.getInstance().getFairs().getData().size(); i++) {
                eventsList.add(Singleton.getInstance().getFairs().getData().get(i).getName());
            }
        }
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
            case R.id.dropDownTxt:
                if (!Singleton.getInstance().getFairs().getData().isEmpty() && Singleton.getInstance().getFairs().getData().size() > 1) {
                    openDropDown();
                }
                break;
            case R.id.languageTxt:
                forLanguage = true;
                if (languages != null && !languages.isEmpty()) openLanguageDropDown();
                else Helper.showToast("Please select fair first");
                break;
            case R.id.continueBtn:
                clicked = true;
                if (!textView.getText().toString().equals(getResources().getString(R.string.select_your_event))) {
                    locationChanged = false;
                    if (!languageText.getText().toString().equals(getResources().getString(R.string.select_language))) {
                        if (userLocation != null) goToRegistration();
                    } else Helper.showToast("Please select language");
                } else
                    Helper.showToast("Please select an event");
                break;
        }
    }

    private void getLocation() {
        if (Helper.isInternetConnected()) {
            if (Helper.isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(Singleton.getInstance().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Singleton.getInstance().getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                } else {
                    //location = getLastKnownLocation();

                    getAddress();
                    // Write you code here if permission already given.
                }

            } else {
                Helper.buildAlertMessageNoGps();
            }
        } else {
            Helper.showToast("No Internet Connection");

        }
    }

    @SuppressLint("MissingPermission")
    private void getAddress() {
        locationManager = (LocationManager) Singleton.getInstance().getActivity().getApplicationContext().getSystemService(Singleton.getInstance().getContext().LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        userLocation = getLastKnownLocation();
        if (userLocation == null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        //Request update as location manager can return null otherwise
        else {
            geoCodeLocation();
        }

    }

    @SuppressLint("MissingPermission")
    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l != null) {
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
        }
        return bestLocation;
    }

    private void geoCodeLocation() {
        Geocoder geocoder = new Geocoder(Singleton.getInstance().getContext(), Locale.getDefault());
        try {
            Singleton.getInstance().setLocation(userLocation);
            addresses = geocoder.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), 1);
            Singleton.getInstance().setAddresses(addresses);
            // JSONObject jsonObject=Helper.getLocationInfo(userLocation.getLatitude(),userLocation.getLongitude());

            getFairDetails();

            String postalCode;
            try {
                postalCode = Singleton.getInstance().getAddresses().get(0).getPostalCode();
            } catch (Exception e) {
                postalCode = " ";
            }
            SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
            editor.putString("postalCode", postalCode);
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
            Helper.hideProgressBar(progressBar);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null) {
            this.userLocation = location;
            locationChanged = true;
            geoCodeLocation();
            locationManager.removeUpdates(this);
        }

        // }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ActivityCompat.checkSelfPermission(Singleton.getInstance().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Helper.showToast("App requires location permission in order to display data near to your location");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission accepted.
                    getAddress();
                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", Singleton.getInstance().getContext().getPackageName(), String.valueOf(this)));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, 1);
                    //permission denied.
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void openLanguageDropDown() {
        Dialog dialog = new Dialog(getView().getContext());
        dialog.setContentView(R.layout.view_drop_down_list);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        dialog.getWindow().setLayout(width - 100, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        ListView listView = dialog.findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Singleton.getInstance().getActivity(),
                R.layout.view_events_spinner, languages);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                languageText.setText(arrayAdapter.getItem(i));
                for (FairLanguage fairLanguage : Singleton.getInstance().getFairData().getFair().getFairLanguages()) {
                    if (fairLanguage.getLangName().equals(languageText.getText().toString())) {
                        Singleton.getInstance().setLanguage(fairLanguage.getAutoId());
                        break;
                    }
                }
                for (int j = 0; j < Singleton.getInstance().getFairData().getFair().getFairTranslations().size(); j++) {
                    if (Singleton.getInstance().getFairData().getFair().getFairTranslations().get(j).getCode().equals(Singleton.getInstance().getLanguage())) {
                        Singleton.getInstance().setLanguageIndex(j);
                        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
                        editor.clear();
                        editor.putInt("languageIndex", j);
                        AppSession.put("languageIndex", j);
                        Singleton.getInstance().setLanguageIndex(j);
                        editor.apply();
                        break;
                    }
                }

                forLanguage = false;
                dialog.cancel();
            }

        });
    }

}

