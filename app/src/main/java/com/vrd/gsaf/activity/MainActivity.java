package com.vrd.gsaf.activity;

import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.parseColor;
import static com.vrd.gsaf.home.dashboard.profile.UserProfile.onBackPressProfile;
import static com.vrd.gsaf.home.dashboard.resume.Resume.onBackPressResume;
import static com.vrd.gsaf.home.dashboard.uploadTask.UploadTask.onBackPressTasks;
import static com.vrd.gsaf.home.homeTab.Home.recyclerLayout;
import static com.vrd.gsaf.home.homeTab.Home.recyclerView;
import static com.vrd.gsaf.home.homeTab.Home.slideDown;
import static com.vrd.gsaf.registration.Events.setLanguageOrder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.vrd.gsaf.BuildConfig;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.FairsHalls.FairHallsResponse;
import com.vrd.gsaf.api.responses.PrivacyAndConditionsReponse;
import com.vrd.gsaf.api.responses.exitPoll.ExitPoll;
import com.vrd.gsaf.api.responses.fairDetail.Data;
import com.vrd.gsaf.api.responses.fairDetail.FairDetail;
import com.vrd.gsaf.api.responses.fairDetail.FairLanguage;
import com.vrd.gsaf.api.responses.fairExtraFields.FairExtraFieldsResponse;
import com.vrd.gsaf.api.responses.halls.Halls;
import com.vrd.gsaf.api.responses.halls.HallsDatum;
import com.vrd.gsaf.app.AppSession;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.callbacks.MyBottomNavListener;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.fragments.NetworkingTableFragment;
import com.vrd.gsaf.home.AboutUs;
import com.vrd.gsaf.home.NeedHelpFragment;
import com.vrd.gsaf.home.companies.Companies;
import com.vrd.gsaf.home.conferenceAgenda.ConferenceAgenda;
import com.vrd.gsaf.home.courses.Courses;
import com.vrd.gsaf.home.dashboard.DashBoard;
import com.vrd.gsaf.home.dashboard.careeTest.CareerTest;
import com.vrd.gsaf.home.dashboard.profile.UserProfile;
import com.vrd.gsaf.home.exitPoll.ExitPollAdapter;
import com.vrd.gsaf.home.exitPoll.ExitPollResultAdapter;
import com.vrd.gsaf.home.faq.FAQ;
import com.vrd.gsaf.home.goodies.GoodiesList;
import com.vrd.gsaf.home.homeTab.Home;
import com.vrd.gsaf.home.jobs.Jobs;
import com.vrd.gsaf.home.notiifcations.NotificationsFragment;
import com.vrd.gsaf.home.speakers.Speakers;
import com.vrd.gsaf.home.stageConferenceAgenda.StageConferenceAgenda;
import com.vrd.gsaf.registration.Events;
import com.vrd.gsaf.registration.Login;
import com.vrd.gsaf.registration.Registration;
import com.vrd.gsaf.registration.Splash;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.DateTime;
import com.vrd.gsaf.utility.GpsLocationReceiver;
import com.vrd.gsaf.utility.Helper;
import com.vrd.gsaf.utility.Res;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import us.zoom.sdk.ZoomSDK;
//import us.zoom.sdk.ZoomSDKInitParams;
//import us.zoom.sdk.ZoomSDKInitializeListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, LifecycleObserver, MyBottomNavListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    private static final long CLICK_TIME_INTERVAL = 500;
    public static boolean isViewClicked = false;
    static MainActivity instance;
    static String fairId;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private final GpsLocationReceiver gpsBroadcastReceiver = new GpsLocationReceiver();
    public TextView goodieBagCount;
    //    public BubbleNavigationConstraintView bottomNavigationView;
    public ChipNavigationBar chipNavigationBar;
    public DrawerLayout drawerLayout;
    //    public BubbleToggleView nav_bottom_home;
    BottomNavigationView bottomNavigationView;
    View contentView;
    boolean allValuesFilled = true;
    private NavigationView navigationView, navigationViewFooter;
    //    private BubbleToggleView nav_bottom_notificaiton;
//    private BubbleToggleView nav_bottom_chat;
    private TextView daysTxt, minutesTxt, hourstxt, secondsTxt;
    private ImageView fairImage;
    private Menu menu;
    private long mLastClickTime = 0;
    private TextView aboutUs, languageBtn, logoutBtn;
    private LocationManager locationManager;
    private List<Address> addresses;
    private Fragment fragment;
    private Res res;
    private VideoView mainPlayer;
    private LinearLayout timerLayout;
    private ConstraintLayout mainLayout;
    private View splash_image;
    private UiModeManager uiModeManager;
    private Date startDate;

    public MainActivity() {
    }

    public static MainActivity getInstance() {
        if (instance == null) {
            instance = new MainActivity();
        }
        return instance;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        Log.d("MyApp", "App in background");
        //      recyclerView.setVisibility(View.GONE);
        //    slideDown(recyclerLayout);
        //  Singleton.getInstance().getFloatingButton().setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            //  recyclerView.setVisibility(View.GONE);
            //  slideDown(recyclerLayout);
            //   Singleton.getInstance().getFloatingButton().setVisibility(View.VISIBLE);
        } catch (Exception e) {
            // Helper.showToast("asd");
        }
    }

    @Override
    protected void onResume() {
        try {

            super.onResume();
            if (Singleton.getInstance().getHomeState() != null) {
                if (Singleton.getInstance().getHomeState().equalsIgnoreCase("notifications")) {
                    Helper.setActiveItemBottomNav(1);
                } else {
                    Helper.setActiveItemBottomNav(0);

                }
            }

        } catch (Exception e) {
            // Helper.showToast("onResume");
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        Log.d("MyApp", "App in foreground");

    }

    @Override
    public Resources getResources() {
        if (res == null) {
            res = new Res(super.getResources());
        }
        return res;
    }

    private void computePakageHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.vrd.android.Applications",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
        computePakageHash();

        Singleton.getInstance().setMainActivity(this);
        // Helper.adjustFontSize(Singleton.getInstance().getContext());
        Singleton.getInstance().setActivity(this);
        Singleton.getInstance().setContext(this);
        Singleton.getInstance().setApp(MainApp.getAppContext());
        Singleton.getInstance().setProxy(Singleton.getInstance().getApp().getProxy(Singleton.getInstance().getActivity()));
        registerReceiver(gpsBroadcastReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));

//        bufferVideos();
//        int index = AppSession.getInt("languageIndex", 0);
//        Singleton.getInstance().setLanguageIndex(index);

        if (Singleton.getInstance().getSharedPreferences().getBoolean("login", false)) {
            String loginData = Singleton.getInstance().getSharedPreferences().getString("loginData", null);
            Gson gson = new Gson();
            Singleton.getInstance().setLoginData(gson.fromJson(loginData, com.vrd.gsaf.api.responses.login.Data.class));
            String Fairjson = Singleton.getInstance().getSharedPreferences().getString("fairDetails", null);
            if (Fairjson != null) {
                Singleton.getInstance().setFairData(gson.fromJson(Fairjson, Data.class));
            }
            if (AppSession.getBoolean("careerTest", false) && Singleton.getInstance().getFairData() != null && Singleton.getInstance().getLoginData().getUser().getTakeTest() == 0 && (Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("1") || Singleton.getInstance().getFairData().getFair().getOptions().getEnable_force_questionnaire_after_registration_front() == 1)) {
                gson = new Gson();
                String json = Singleton.getInstance().getSharedPreferences().getString("fairDetails", null);
                Singleton.getInstance().setFairData(gson.fromJson(json, Data.class));
                Singleton.getInstance().setNewRegistration(true);
                replaceFragment(new CareerTest(), null);
            } else {
                Singleton.getInstance().setHomeState("home");
                replaceFragment(new Home(), "home");
                try {
                    Singleton.getInstance().getFloatingButton().setVisibility(View.GONE);
                } catch (Exception e) {
                    //  Helper.showToast("onCreate1");
                }
                initializeViews();
                Singleton.getInstance().setIsLoggedIn(true);

            }

        } else if (Singleton.getInstance().getIsLoggedIn() != null && !Singleton.getInstance().getIsLoggedIn()) {
            Singleton.getInstance().setHomeState("home");
            replaceFragment(new Home(), "home");
            Singleton.getInstance().setIsLoggedIn(false);
            initializeViews();
        } else {
            //go to registration module
            Singleton.getInstance().setLoginFragmentFlag(true);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, new Splash());
            fragmentTransaction.commit();
            // replaceFrament(new Splash(), "splash");
        }


    }

    private void loginUser(String token) {
        int fairId = Singleton.getMyFairId();
        String UID = "";
        if (Singleton.getInstance().getIsLoggedIn()) {
            String userId = Singleton.getInstance().getLoginData().getUser().getId();
            //Spiderman
            //Wolverine
            //   String UID = "53f123"; // Replace with the UID of the user to login
            UID = fairId + "f" + userId;
        } // Replace with the UID of the user to login
//        String authKey = AppConstants.AUTH_KEY_CHAT; // Replace with your App Auth Key

        if (Singleton.getInstance().getIsLoggedIn() && CometChat.isInitialized() && CometChat.getLoggedInUser() == null) {
            CometChat.login(UID, Singleton.getChatDetails().getApiAuthKey(), new CometChat.CallbackListener<User>() {

                @Override
                public void onSuccess(User user) {
                    Log.d("waqas", "Login Successful : " + user.toString());
                    CometChat.registerTokenForPushNotification(token, new CometChat.CallbackListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Log.e("onSuccessPN: ", s);
                        }

                        @Override
                        public void onError(CometChatException e) {
                            Helper.showToast("loggedIn" + e.getMessage());
                            Log.e("onErrorPN: ", e.getMessage());
                        }
                    });
                }

                @Override
                public void onError(CometChatException e) {
                    Helper.showToast("" + e.getMessage());
                    Log.d("waqas", "Login failed with exception: " + e.getMessage());
                }
            });
        } else {
            // Helper.showToast("Already Logged In");
            // User already logged in
        }


    }

    private void setClickListeners() {
        navigationView.setItemIconTintList(null);
        aboutUs.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        languageBtn.setOnClickListener(this);
        Singleton.getInstance().setDrawerLayout(drawerLayout);
    }

    private void initializeViews() {

        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        contentView = findViewById(R.id.frameLayout);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigationView = findViewById(R.id.navigationView);


        Singleton.getInstance().setNavigationView(navigationView);
        navigationViewFooter = findViewById(R.id.nav_view_footer);
        chipNavigationBar = findViewById(R.id.chipNavBar);
//        bottomNavigationView = findViewById(R.id.bottomNavigation);
//        nav_bottom_home = findViewById(R.id.nav_home);

//        nav_bottom_notificaiton = findViewById(R.id.nav_notification);
//        nav_bottom_chat = findViewById(R.id.nav_chat);
        mainLayout = findViewById(R.id.mainLayout);
//        if (BuildConfig.APP_LOGO.contains("gsaf")){
//            mainLayout.setBackground(this.getDrawable(R.drawable.gsaf_splash));
//        }else if(BuildConfig.APP_LOGO.contains("fm.png")){
//            mainLayout.setBackground(this.getDrawable(R.drawable.fv_splash));
//        }
        Singleton.getInstance().setBottomNavigationView(chipNavigationBar);
        aboutUs = findViewById(R.id.aboutUsTab);
        languageBtn = findViewById(R.id.languageTab);
        logoutBtn = findViewById(R.id.logOut);
        logoutBtn.setVisibility(View.VISIBLE);

//        bottomNavigationView.setVisibility(View.VISIBLE);
        chipNavigationBar.setVisibility(View.VISIBLE);
//        chipNavigationBar.setBackgroundColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        chipNavigationBar.setItemSelected(R.id.nav_home, true, Singleton.getHomeTitle());

        getFairDetails();
//        bottomNavigationView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onNavigationChanged(View view, int position) {
//
//                onItemClickBubble(position);
//
//            }
//        });
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.nav_home:
                        onItemClickBubble(0);
                        break;
                    case R.id.nav_notification:
                        onItemClickBubble(1);
                        break;
                    case R.id.nav_chat:
                        onItemClickBubble(2);
                        break;
                }
//                onItemClickBubble(i);
            }
        });

        bottomNavigationOptions();

        setBadge(R.id.nav_notification);

        setNavigation();
        setClickListeners();
        getFireBaseToken();
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        zoomInitialization();
        getLocation();
        goodieBagBadge();
        // if(Singleton.getInstance().getUserLogin()==null || Singleton.getInstance().getUserLogin()) {
        initializeTimer();
        //  }


    }

    private void initializeTimer() {
        try {
            startDate = Singleton.getInstance().getFormat().parse(Singleton.getInstance().getFairData().getFair().getStartTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = startDate.getTime() - new Date().getTime();
        View headerView = navigationView.getHeaderView(0);
        timerLayout = headerView.findViewById(R.id.timerLayout);
        //  if (Singleton.getInstance().getUserLogin() != null && Singleton.getInstance().getUserLogin()) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Singleton.getInstance().getHome().setTimer();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
        //  }

        // diff =300;
        if (diff > 0) {


            chipNavigationBar.setItemEnabled(R.id.nav_chat, false, Singleton.getMessageTitle());
            chipNavigationBar.setItemEnabled(R.id.nav_notification, false, Singleton.getNotificationTitle());
            timerLayout.setVisibility(View.VISIBLE);
            daysTxt = headerView.findViewById(R.id.daysTxt);
            minutesTxt = headerView.findViewById(R.id.minutesTxt);
            hourstxt = headerView.findViewById(R.id.hoursTxt);
            secondsTxt = headerView.findViewById(R.id.secondsTxt);
            countDownStart(headerView);

            // countDownStart();
        } else {
            if (Singleton.getInstance().getIsLoggedIn() == null || Singleton.getInstance().getIsLoggedIn()) {
                timerLayout.setVisibility(View.GONE);
//                nav_bottom_chat.setVisibility(View.VISIBLE);
//                nav_bottom_notificaiton.setVisibility(View.VISIBLE);
            }


        }
    }

    public void countDownStart(View view) {
//        Drawable myDrawable = ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.days);
//        myDrawable.setColorFilter(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()), PorterDuff.Mode.SRC_IN);
//        myDrawable.setColorFilter(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()), PorterDuff.Mode.DST);
        ImageView daysImageView = view.findViewById(R.id.daysImageView);
        ImageView hoursImageView = view.findViewById(R.id.hoursImageView);
        ImageView minutesImageView = view.findViewById(R.id.minutesImageView);
        ImageView secondsImageView = view.findViewById(R.id.secondsImageView);

        Singleton.changeIconColor(R.drawable.white_circle, daysImageView, Singleton.getSidebarInnerTextColor());
        Singleton.changeIconColor(R.drawable.white_circle, hoursImageView, Singleton.getSidebarInnerTextColor());
        Singleton.changeIconColor(R.drawable.white_circle, minutesImageView, Singleton.getSidebarInnerTextColor());
        Singleton.changeIconColor(R.drawable.white_circle, secondsImageView, Singleton.getSidebarInnerTextColor());

        Helper.setTextColor(daysTxt);
        Helper.setTextColor(hourstxt);
        Helper.setTextColor(minutesTxt);
        Helper.setTextColor(secondsTxt);

        TextView textView = view.findViewById(R.id.daysHeadingTxt);
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDays());
        Helper.setTextColor(textView);

        textView = view.findViewById(R.id.secondsHeadingTxt);
        textView.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getCountdownInnerColor()));
        Helper.setTextColor(textView);

        textView = view.findViewById(R.id.hoursHeadingTxt);
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getHour());
        Helper.setTextColor(textView);

        textView = view.findViewById(R.id.minutesHeadingTxt);
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMinutes());
        Helper.setTextColor(textView);

        Handler handler = new Handler();
        Runnable runnable;
        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    } else {
                        //Set the optional Date format here for Devices Running ANDROID VERSION BELOW N
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    }
// Please here set your event date//YYYY-MM-DD
                    Date currentDate = new Date();
                    if (!currentDate.after(startDate)) {
                        long diff = startDate.getTime() - currentDate.getTime();

                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;

                        //  time.setText(String.valueOf(days) + " days \n" + String.valueOf(hours) + " hours \n " + String.valueOf(minutes) + " minutes \n" + String.valueOf(seconds) + " seconds");
                        daysTxt.setText(String.valueOf(days));
                        hourstxt.setText(String.valueOf(hours));
                        minutesTxt.setText(String.valueOf(minutes));
                        secondsTxt.setText(String.valueOf(seconds));
                    } else {
                        handler.removeCallbacks(this::run);
                        timerLayout.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

    }

    public void goodieBagBadge() {
        goodieBagCount = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_goodie_bags));

        goodieBagCount.setBackground(getResources().getDrawable(R.drawable.goodie_bag_badge));
        GradientDrawable drawable = (GradientDrawable) goodieBagCount.getBackground();
        // drawable.setColor(R.color.appColor);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getBadgeBackgroundColorFront() != null) {
            drawable.setColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getBadgeBackgroundColorFront()));
        }
        goodieBagCount.setGravity(Gravity.CENTER);
        goodieBagCount.setTypeface(null, Typeface.BOLD);
        // if(Singleton.getInstance().getGoodieCount()!=0)
        goodieBagCount.setText(String.valueOf(Singleton.getInstance().getSharedPreferences().getInt("goodieCount", 0)));
        if (Singleton.getInstance().getFairData().getFair().getOptions().getBadgeFontTextColorFront() != null)
            goodieBagCount.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getBadgeFontTextColorFront()));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 20, 10, 0);
        goodieBagCount.setLayoutParams(params);
        goodieBagCount.setGravity(Gravity.CENTER);
    }

    private void zoomInitialization() {
//        ZoomSDK zoomSDK = ZoomSDK.getInstance();
//        ZoomSDKInitParams params = new ZoomSDKInitParams();
//        params.appKey = AppConstants.SDK_KEY_ZOOM;
//        params.appSecret = AppConstants.SDK_SECRET_ZOOM;
//        params.domain = "https://zoom.us"; // Required
//        params.enableLog = true; // Optional for debugging
//
//        ZoomSDKInitializeListener listener = new ZoomSDKInitializeListener() {
//            @Override
//            public void onZoomSDKInitializeResult(int i, int i1) {
//                // Helper.showToast("zoom initialization success");
//
//            }
//
//            @Override
//            public void onZoomAuthIdentityExpired() {
//                // Helper.showToast("zoom initialization failure");
//            }
//        };
//        zoomSDK.initialize(getApplicationContext(), listener, params);
    }

    private void welcomeNote() {
        if (Singleton.getInstance().getNewRegistration()) {
//            showWelcomeDialog();
            Singleton.getInstance().setNewRegistration(false);
            getWelcomeContent(null, String.valueOf(Singleton.getMyFairId()));
        }
    }

//    public void showWelcomeDialog() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
//// ...Irrelevant code for customizing the buttons and title
//        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_welcome, null);
//
//        dialogView.requestLayout();
//        dialogBuilder.setView(dialogView);
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
//        TextView descriptionTxt = dialogView.findViewById(R.id.descriptionTxt);
//        if (Singleton.getInstance().getFairData().getFair().getFairSetting().getWelcomeContentAfterRegistration() != null) {
//            Helper.loadHtml(descriptionTxt, Singleton.getInstance().getFairData().getFair().getFairSetting().getWelcomeContentAfterRegistration());
//        }
////        descriptionTxt.setText(Singleton.getInstance().getFairData().getFair().getFairSetting().getWelcomeContentAfterRegistration());
//        descriptionTxt.setMovementMethod(new ScrollingMovementMethod());
//
//
//        Button okBtn = dialogView.findViewById(R.id.okBtn);
////        okBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getO());
//        Helper.setButtonColorWithDrawable(okBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(), Singleton.getInstance().getFairData().getFair().getOptions().getReceptionButtonsInnerTextColor());
//
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                long now = System.currentTimeMillis();
//                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
//                    return;
//                }
//                alertDialog.cancel();
//            }
//        });
//        drawerLayout.close();
//
//        alertDialog.show();
//    }

//    private void bottomNavigationOptions() {
//        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatFront() == 1)
//            bottomNavigationView.getMenu().removeItem(R.id.nav_chat);
//
//        if (Singleton.getInstance().getLoginData() == null) {
//            bottomNavigationView.getMenu().removeItem(R.id.nav_chat);
//            bottomNavigationView.getMenu().removeItem(R.id.nav_notification);
//        }
//    }

    private void bottomNavigationOptions() {
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatFront() == 1)
//            nav_bottom_chat.setVisibility(View.GONE);
            chipNavigationBar.setItemEnabled(R.id.nav_chat, false, Singleton.getMessageTitle());
        if (Singleton.getInstance().getLoginData() == null && !Singleton.getInstance().getIsLoggedIn()) {
//            nav_bottom_chat.setVisibility(View.GONE);
            chipNavigationBar.setItemEnabled(R.id.nav_chat, false, Singleton.getMessageTitle());
            chipNavigationBar.setItemEnabled(R.id.nav_notification, false, Singleton.getNotificationTitle());
//            nav_bottom_notificaiton.setVisibility(View.GONE);


        }
    }

    private void getFireBaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        //  Helper.showToast("FireBase Token");
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Singleton.getInstance().setFirebaseToken(token);
                        //  comitChat();z
                        loginUser(token);
                        // Log and toast
                    }
                });
        FirebaseMessaging.getInstance().getToken().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Helper.showToast("FireBase Token Failure");

            }
        });
    }


    @SuppressLint({"ResourceAsColor", "ResourceType"})
    public void setColors() {

        if (Singleton.getEnterButton() != null) {
            if (Singleton.getInstance().getLoginData() == null) {
                Singleton.getEnterButton().setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLogin());
            } else {
                Singleton.getEnterButton().setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEnter());
            }
            Singleton.getEnterButton().setTextColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor()));
            GradientDrawable drawable = (GradientDrawable) Singleton.getEnterButton().getBackground();
            drawable.setStroke(25, parseColor("#33ffffff"));
            drawable.setColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterBackground()));

        }
        navigationView.setBackgroundColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
        navigationViewFooter.setBackgroundColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
        mainLayout.setBackgroundColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));


        String color = Singleton.getInstance().getFairData().getFair().getOptions().getSidebarInnerTextColor();
        navigationView.setItemTextColor(ColorStateList.valueOf(parseColor(color)));
        navigationViewFooter.setItemTextColor(ColorStateList.valueOf(parseColor(color)));

        TextView aboutUsTab = navigationViewFooter.findViewById(R.id.aboutUsTab);
        TextView logOut = navigationViewFooter.findViewById(R.id.logOut);
        aboutUsTab.setTextColor(parseColor(color));
        languageBtn.setTextColor(parseColor(color));
        logOut.setTextColor(parseColor(color));

        navigationView.setItemIconTintList(ColorStateList.valueOf(parseColor(color)));
        float radius = 40;


        color = Singleton.getInstance().getFairData().getFair().getOptions().getTopnavInnerTextColor();

//        nav_bottom_notificaiton.setBackgroundColor(parseColor(color));
//        nav_bottom_chat.setBackgroundColor(parseColor(color));
//        nav_bottom_home.setBackgroundColor(parseColor(color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // ColorFilter colorFilter= new ColorFilter(R.color.black);

            //nav_bottom_home.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
            //  nav_bottom_home.setForegroundTintList(ColorStateList.valueOf(parseColor(color)));
            //  PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
//            nav_bottom_home.setForegroundTintList(ColorStateList.valueOf(parseColor(color)));


        }

        // bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(parseColor(color)));
        // bottomNavigationView.setItemTextColor(ColorStateList.valueOf(parseColor(color)));

        color = Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(parseColor(color));
        }

//        bottomNavigationView.setBackgroundColor(parseColor(color));
        chipNavigationBar.setBackgroundColor(parseColor(color));

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(parseColor(color));

        }

        drawerLayout.setScrimColor(TRANSPARENT);

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                           @Override
                                           public void onDrawerSlide(View drawer, float slideOffset) {
                                               contentView.setX(navigationView.getWidth() * slideOffset);
                                               contentView.getLayoutParams();
                                               ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
                                               params.height = drawer.getHeight() - (int) (drawer.getHeight() * slideOffset * 0.4f);
                                               contentView.setLayoutParams(params);
                                               Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);

                                               GradientDrawable shape = new GradientDrawable();
//                                              if (Singleton.getInstance().getHomeState().equals("home")){
//                                               Home.getInstance().billBoardVideoView.setVisibility(View.GONE);}
//

//                                               Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
//                                               navigationView.startAnimation(animFadeIn);
                                           }

                                           @Override
                                           public void onDrawerClosed(View drawerView) {
                                               super.onDrawerClosed(drawerView);
                                               Singleton.getInstance().getBottomNavigationView().setVisibility(View.VISIBLE);
//                                             Home.getInstance().onDrawerClosed();
//                                               Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
//                                               navigationView.startAnimation(animFadeIn);
                                           }
                                       }
        );


    }

    public void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

//    private void setBadge(int itemId) {
//        // bottomNavigationView.getMenu().getItem(2).setTitle("wasdad");
//        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(itemId);
//        badge.setVisible(true);
//        badge.setBackgroundColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterBackground()));
//        badge.setNumber(100);
//        String color = Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor();
//        //  bottomNavigationView.set(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
//    }

    private void setBadge(int itemId) {
//        nav_bottom_notificaiton.setBadgeText("100");
    }

    private void setHeader() {
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.userNameTxt);
        TextView userEmail = headerView.findViewById(R.id.userEmailTxt);
        ImageView imageView = headerView.findViewById(R.id.imageView);
        fairImage = headerView.findViewById(R.id.fairImage);

        if (Singleton.getInstance().getLoginData() != null) {
            fairImage.setVisibility(View.VISIBLE);
            navUsername.setVisibility(View.VISIBLE);
            userEmail.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);

            if (Singleton.getInstance().getLoginData() != null) {
                navUsername.setText(Singleton.getInstance().getLoginData().getUser().getName());
                userEmail.setText(Singleton.getInstance().getLoginData().getUser().getEmail());
                imageView.setBackground(null);
                if (Singleton.getInstance().getLoginData().getUser().getProfileImage() != null && Singleton.getInstance().getLoginData().getUser().getProfileImage() != "") {
                    Glide.with(Singleton.getInstance().getContext())
                            .load(Singleton.getInstance().getFairData().getSystemUrl().getAvatarUrl() + Singleton.getInstance().getLoginData().getUser().getProfileImage())
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(R.drawable.ic_users)
                            .error(R.drawable.ic_users)
                            .into(imageView);
                }
            }
            imageView.setOnClickListener(v -> {
                Singleton.getInstance().setIsDashboard(false);
                replaceFragment(new UserProfile(), null);
            });
            headerView.setBackgroundColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
            userEmail.setTextColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarInnerTextColor()));
            navUsername.setTextColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarInnerTextColor()));
            Helper.loadRectangleImageFromUrlWithRounded(fairImage, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() +
                    Singleton.getInstance().getFairData().getFair().getFairImage(), 0);

        } else {
            fairImage.setVisibility(View.VISIBLE);
            navUsername.setVisibility(View.GONE);
            userEmail.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            headerView.setBackgroundColor(parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));


            Helper.loadRectangleImageFromUrlWithRounded(fairImage, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() +
                    Singleton.getInstance().getFairData().getFair().getFairImage(), 0);

            Glide.with(Singleton.getInstance().getContext()).
                    load(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() +
                            Singleton.getInstance().getFairData().getFair().getFairImage())
                    .placeholder(R.drawable.rectangluar_placeholder)
                    .fitCenter()
                    .into(fairImage);

        }
    }


    private void hideDrawerOptions() {
        menu = navigationView.getMenu();
        MenuItem target = menu.findItem(R.id.nav_exhibitions);
        target.setVisible(true);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getExhibitors());


        if (Singleton.getInstance().getFairData() != null) {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideJobsFront() != 0) {
                target = menu.findItem(R.id.nav_jobs);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJobs());
                target.setVisible(false);
            }
            if (!Singleton.getInstance().getIsLoggedIn() || !Singleton.getInstance().getLive() || Singleton.getInstance().getFairData().getFair().getOptions().getEnable_virtual_networking_tables() != 1) {
                target = menu.findItem(R.id.nav_networking);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getNetworking_tables());
                target.setVisible(false);
            }
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideConferenceAgednaFront() != 0) {
                target = menu.findItem(R.id.nav_conference_agenda);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getConferenceAgenda());
                target.setVisible(false);
            }
            if (Singleton.getOptions().getWebinarEnable() != 1 || Singleton.getInstance().getFairData().getFair().getOptions().getEnable_speakers_agenda() != 1) {
                target = menu.findItem(R.id.nav_speakers_agenda);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSpeakers_agenda());
                target.setVisible(false);
            }
            if (!Singleton.getInstance().getIsLoggedIn() || !Singleton.getInstance().getLive() || Singleton.getInstance().getFairData().getFair().getOptions().getEnable_reception_tab_front() != 1) {
                target = menu.findItem(R.id.nav_reception);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getReception());
                target.setVisible(false);
            }

            if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableStageConferenceAgenda() != 1) {
                target = menu.findItem(R.id.nav_stage_conference_agenda);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStageConferenceAgenda());
                target.setVisible(false);
            }
            if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableCourses() == 0 || Singleton.getInstance().getFairData().getFair().getOptions().getDisableCoursesFront() == 1) {
                target = menu.findItem(R.id.nav_courses);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourses());
                target.setVisible(false);

            }
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideSpeakersFront() != 0) {
                target = menu.findItem(R.id.nav_speaskers);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSpeakers());
                target.setVisible(false);
            }
            if (Singleton.getInstance().getFairData().getFair().getOptions().getHideCompanyMenuFront() != 0) {
                target = menu.findItem(R.id.nav_companies);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCompanies());
                target.setVisible(false);
            }


            if (Singleton.getInstance().getIsLoggedIn() != null && !Singleton.getInstance().getIsLoggedIn()) {
                target = menu.findItem(R.id.nav_exhibitions);
                target.setVisible(false);
            }


            if (!Singleton.getInstance().getLive() || Singleton.getInstance().getFairData() == null || Singleton.getInstance().getFairData().getFair().getOptions().getDisableExhibitorsHall() == 1) {
                target = menu.findItem(R.id.nav_exhibitions);
                target.setVisible(false);
            }

            if (!Singleton.getInstance().getLive() && Singleton.getInstance().getFairData().getFair().getOptions().getEnableDashboardFront() == 0) {
                target = menu.findItem(R.id.nav_dashboard);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDashboard());
                target.setVisible(false);
            }

            if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableDashboard() != 0) {
                target = menu.findItem(R.id.nav_dashboard);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDashboard());
                target.setVisible(false);
            }


            if (Singleton.getInstance().getIsLoggedIn() != null && !Singleton.getInstance().getIsLoggedIn()) {
                target = menu.findItem(R.id.nav_dashboard);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDashboard());
                target.setVisible(false);
            }

            if (!Singleton.getInstance().getLive() || !Singleton.getInstance().getIsLoggedIn() || Singleton.getInstance().getFairData().getFair().getOptions().getSurveyEnable() == 0
                    || Singleton.getInstance().getFairData().getFair().getOptions().getDisable_sidebar_exit_survey() == 1) {
                target = menu.findItem(R.id.nav_exit_survey);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getExitSurvey());
                target.setVisible(false);
            }

            if (Singleton.getInstance().getIsLoggedIn() != null && !Singleton.getInstance().getIsLoggedIn()) {
                target = menu.findItem(R.id.nav_goodie_bags);
//                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoodie() + " " +
//                        Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getBag());
                target.setVisible(false);
            }
            if (Singleton.getInstance().getFairData().getFair().getGoodies().equalsIgnoreCase("0")) {
                target = menu.findItem(R.id.nav_goodie_bags);
//                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoodie() + " " +
//                        Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getBag());
                target.setVisible(false);
            }
            if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableFaqs() != 0) {
                target = menu.findItem(R.id.nav_faq);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFaq());
                target.setVisible(false);
            }
            if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableNeedHelp() != 0) {
                target = menu.findItem(R.id.nav_need_help);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getNeedHelp());
                target.setVisible(false);
            }
            if (Singleton.getInstance().getFairData().getFair().getOptions().getWebinarEnable() != 1) {
                target = menu.findItem(R.id.nav_conference_agenda);
                target.setVisible(false);
                target = menu.findItem(R.id.nav_stage_conference_agenda);
                target.setVisible(false);
            }
            if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableAboutSidebarMenuFront() != 0) {

                target = menu.findItem(R.id.nav_about_us);
                target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAbout() + " Us");
                target.setVisible(false);
            }
//            if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableAboutSidebarMenuFront() != 0) {
//
//                TextView textView = findViewById(R.id.aboutUsTab);
//                textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAbout() + " " + "Us");
//                textView.setVisibility(View.GONE);
//            }
            if (Singleton.getInstance().getFairData().getFair().getOptions().getSurveyEnable() == 0) {

                TextView textView = findViewById(R.id.nav_exit_survey);
                textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getExitSurvey());
                textView.setVisibility(View.GONE);
            }
            if (!Singleton.getInstance().getIsLoggedIn() && Singleton.getInstance().getFairData().getFair().getOptions().getDisable_login_button_landing_page_front() != 0) {
                TextView textView = findViewById(R.id.logOut);
                textView.setVisibility(View.GONE);
            }
            TextView textView = findViewById(R.id.languageTab);
            int index = Singleton.getInstance().getLanguageIndex();

            String languageCode = Singleton.getInstance().getFairData().getFair().getFairLanguages().get(index).getLangCode();
            Singleton.getInstance().setLanguage(Singleton.getInstance().getFairData().getFair().getFairLanguages().get(index).getAutoId());
            if (languageCode != null) {
                textView.setText("Language" + " (" + languageCode + ")");
            } else textView.setText("Language");
            //textView.setVisibility(View.GONE);

        }
//        else {
//            Singleton.getInstance().setLoginFragmentFlag(true);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                getLocation();
//            }
//        }


    }


    private void getFairDetails() {
        Gson gson = new Gson();
        String json = Singleton.getInstance().getSharedPreferences().getString("fairDetails", null);
        if (json != null) {
            Singleton.getInstance().setFairData(gson.fromJson(json, Data.class));
            UIKitSettings.sendVoiceNotes(false);
            UIKitSettings.shareLocation(false);
            UIKitSettings.sendStickers(false);
            UIKitSettings.shareLocation(false);
            UIKitSettings.setColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor());
            UIKitSettings.users(false);
            UIKitSettings.groups(false);
            UIKitSettings.groupCreate(false);
            UIKitSettings.startConversation(false);
            if (Singleton.getInstance().getFairData().getFair().getFairSetting().getVideoChat().equals("1")) {
                UIKitSettings.calls(false);

            }
            if (Singleton.getInstance().getLoginData() != null && Singleton.getInstance().getFairData() != null && Singleton.getInstance().getFairData().getFair().getOptions().getEnable_audio_video_call_candidate_front() != null && Singleton.getInstance().getFairData().getFair().getOptions().getEnable_audio_video_call_candidate_front().equals(0)) {
                UIKitSettings.calls(false);
                UIKitSettings.userVideoCall(false);
                UIKitSettings.userAudioCall(false);
                UIKitSettings.groupVideoCall(false);
                UIKitSettings.groupAudioCall(false);
                UIKitSettings.groupAudioCall(false);
            }
            if (Singleton.getInstance().getLoginData() != null && Singleton.getInstance().getFairData() != null && Singleton.getInstance().getFairData().getFair().getOptions().getDisable_groups_tab_front_chat() != null && Singleton.getInstance().getFairData().getFair().getOptions().getDisable_groups_tab_front_chat().equals(1)) {
                UIKitSettings.groups(false);
            }
            if (Singleton.getInstance().getLoginData() != null)
                logoutBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLogout());
            else {
                if (Singleton.getInstance().getLoginData() == null && Singleton.getInstance().getFairData().getFair().getOptions().getDisableLoginFront() != 0) {
                    logoutBtn.setVisibility(View.GONE);
                }
                logoutBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLogin());
            }
        }
        try {
            welcomeNote();
            setHeader();
            setColors();
            hideDrawerOptions();
            setLanguage();
        } catch (Exception e) {
            // Helper.showToast("getFairDetails");
        }

    }

    public void setLanguage() {
        menu = navigationView.getMenu();
        MenuItem target = menu.findItem(R.id.nav_jobs);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJobs());

        target = menu.findItem(R.id.nav_networking);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getNetworking_tables());

        target = menu.findItem(R.id.nav_exhibitions);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getExhibitors());

        target = menu.findItem(R.id.nav_reception);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getReception());

        target = menu.findItem(R.id.nav_home);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getHome());

        target = menu.findItem(R.id.nav_conference_agenda);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getConferenceAgenda());

        target = menu.findItem(R.id.nav_speakers_agenda);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSpeakers_agenda());

        target = menu.findItem(R.id.nav_stage_conference_agenda);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStageConferenceAgenda());

        target = menu.findItem(R.id.nav_courses);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourses());

        target = menu.findItem(R.id.nav_speaskers);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSpeakers());

        target = menu.findItem(R.id.nav_companies);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCompanies());


        target = menu.findItem(R.id.nav_dashboard);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDashboard());

        target = menu.findItem(R.id.nav_exit_survey);
        if (Singleton.getInstance().getFairData().getFair().getOptions().getEnable_edit_candidate_exit_survey_front().equals(1)) {
            target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEdit() + " " + Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getExitSurvey());

        } else {
            target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getExitSurvey());
        }

        target = menu.findItem(R.id.nav_faq);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFaq());

        target = menu.findItem(R.id.nav_about_us);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAbout());

        target = menu.findItem(R.id.nav_need_help);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getNeedHelp());

        target = menu.findItem(R.id.nav_goodie_bags);
        target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoodie() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getBag());


        TextView textView = findViewById(R.id.aboutUsTab);
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAbout());
        textView = findViewById(R.id.logOut);
        // textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLogout());

        TextView textView1 = findViewById(R.id.languageTab);
        int index = Singleton.getInstance().getLanguageIndex();

        String languageCode = Singleton.getInstance().getFairData().getFair().getFairLanguages().get(index).getLangCode();
        Singleton.getInstance().setLanguage(Singleton.getInstance().getFairData().getFair().getFairLanguages().get(index).getAutoId());
        if (languageCode != null) {
            textView1.setText("Language" + " (" + languageCode + ")");
        } else textView1.setText("Language");

        if (Objects.requireNonNull(Singleton.getFair()).getFairTranslations().size() <= 2) {
            textView1.setVisibility(View.GONE);
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
//        long currentClickTime = SystemClock.uptimeMillis();
//        long elapsedTime = currentClickTime - mLastClickTime;
//
//        mLastClickTime = currentClickTime;
//
//        if (elapsedTime <= MIN_CLICK_INTERVAL)
//            return;
//        if (!isViewClicked) {
//            isViewClicked = true;
//            startTimer();
//        } else {
//            return;
//        }

        long now = System.currentTimeMillis();
        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;

        switch (view.getId()) {
            case R.id.aboutUsTab:
//                replaceFragment(new AboutUs(), null);
                break;
            case R.id.logOut:
                //   Helper.showToast("asd");
                Singleton.getInstance().getDrawerLayout().close();
                if (Singleton.getInstance().getLoginData() != null && Singleton.getInstance().getFairData().getFair().getOptions().getDisable_force_exit_survey() == 0 && Singleton.getInstance().getLive() && Singleton.getInstance().getFairData().getFair().getOptions().getSurveyEnable() == 1)
                    callApiForExitPOll(true);
                else if (logoutBtn.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLogin())) {
                    goToLogin();
                } else {
                    openLogoutDialog();
                }

                break;
            case R.id.languageTab:
                languageDialogBox();
                break;
        }
    }

    public void goToLogin() {
        Singleton.getInstance().setLoginFragmentFlag(true);
        clearAllFragmentStack();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        FragmentManager fm = getSupportFragmentManager();
//        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
//            getSupportFragmentManager().popBackStackImmediate();
//        }
        Singleton.getInstance().setIsLoggedIn(null);
//        fm.popBackStackImmediate();
        Singleton.getInstance().setFlag(true);
        fragmentTransaction.replace(R.id.frameLayout, new Login());
        fragmentTransaction.commitAllowingStateLoss();
//        fragmentTrx(new Login(),null,null);
        Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
//        nav_bottom_home.setVisibility(View.GONE);
    }

    @SuppressLint("ResourceType")
    private void openLogoutDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_logout, null);

        dialogView.requestLayout();
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        TextView textView = dialogView.findViewById(R.id.titleTxt);
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLogout());
        textView = dialogView.findViewById(R.id.logoutTxt);
        //textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAre());


        Button cancel = dialogView.findViewById(R.id.cancelBtn);
        ImageView logOutIcon = dialogView.findViewById(R.id.logOutIcon);
        Singleton.changeIconColor(R.drawable.ic_logout, logOutIcon);
        cancel.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCancel());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                alertDialog.cancel();
            }
        });

        Button logoutBtn = dialogView.findViewById(R.id.logoutBtn);
        TextView titleTxt = dialogView.findViewById(R.id.titleTxt);
        TextView logoutTxt = dialogView.findViewById(R.id.logoutTxt);
        ImageView icon = dialogView.findViewById(R.id.logOutIcon);

        LinearLayoutCompat view = dialogView.findViewById(R.id.parentLayout);
        logoutBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLogout());
        view.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
        titleTxt.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarInnerTextColor()));
        logoutTxt.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarInnerTextColor()));
//     icon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                logout();
                alertDialog.cancel();


            }
        });
        drawerLayout.close();

        Helper.setButtonColorWithDrawable(cancel, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(), Singleton.getInstance().getFairData().getFair().getOptions().getReceptionButtonsInnerTextColor());
        Helper.setButtonColorWithDrawable(logoutBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(), Singleton.getInstance().getFairData().getFair().getOptions().getReceptionButtonsInnerTextColor());


        alertDialog.show();

    }

    private void logout() {
        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
        Singleton.getInstance().setLoginData(null);
        Singleton.getInstance().setFairData(null);
        editor.clear();
        editor.apply();
        editor.commit();
        AppSession.clearSharedPref();
//        bottomNavigationView.setVisibility(View.GONE);
        chipNavigationBar.setVisibility(View.GONE);
        drawerLayout.close();
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
//                for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
//                    getSupportFragmentManager().popBackStack();
//                }

        logoutCometChat();
        Singleton.getInstance().setLogout(true);

        Singleton.getInstance().setIsLoggedIn(null);
//                getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new Splash())
//                        .setReorderingAllowed(true)
//                        .addToBackStack(null)
//                        .commit();
        Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
//        Singleton.getInstance().getNav_bottom_home().setVisibility(View.GONE);
//                nav_bottom_home.setVisibility(View.GONE);
//                nav_bottom_chat.setVisibility(View.GONE);
//                nav_bottom_notificaiton.setVisibility(View.GONE);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void languageDialogBox() {
        drawerLayout.close();
        ArrayList<String> languages = new ArrayList<>();
        for (FairLanguage fairLanguage : Singleton.getInstance().getFairData().getFair().getFairLanguages()) {
            if (!fairLanguage.getLangName().equalsIgnoreCase("Default")) {
                languages.add(fairLanguage.getLangName());
            }
        }


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);

        dialogView.requestLayout();
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

        TextView titleTxt = dialogView.findViewById(R.id.titleTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSelect() + " Language");
        alertDialog.show();


        //  EditText editText = dialog.findViewById(R.id.edtText);
        ListView listView = dialogView.findViewById(R.id.listView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Singleton.getInstance().getActivity(),
                android.R.layout.simple_list_item_1, languages);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Singleton.getInstance().setLanguageIndex(i);
                SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
                editor.putInt("languageIndex", i);
                AppSession.put("languageIndex", i);
                editor.commit();
                editor.apply();
                alertDialog.dismiss();
                Singleton.getInstance().getActivity().finish();
                Singleton.getInstance().getActivity().overridePendingTransition(0, 0);
                startActivity(Singleton.getInstance().getActivity().getIntent());
                Singleton.getInstance().getActivity().overridePendingTransition(0, 0);
                FragmentManager fm = getSupportFragmentManager();
                for (int j = 0; j < fm.getBackStackEntryCount(); j++) {
                    fm.popBackStack();
                }

            }

        });
    }

    private void callAPiForHalls() {
        drawerLayout.close();

        Helper.showProgressBar(Singleton.getInstance().getHome().progressBar);

        String url = "api/fair/halls/list/" + Singleton.getInstance().getFairData().getFair().getId();

        Call<Halls> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getTotalHalls(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<Halls>() {
            @Override
            public void onResponse(Call<Halls> call, Response<Halls> response) {
                Helper.hideProgressBar(Singleton.getInstance().getHome().progressBar);

                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        //Helper.clearUserData();
                    } else if (response.body().getStatus()) {
                        hallsDialogBox(response.body().getData().getHallsData());
                    } else
                        Helper.showToast("Something Went Wrong please try later");

                } catch (Exception e) {
                    Helper.showToast("Something Went Wrong please try later");

                }

            }

            @Override
            public void onFailure(Call<Halls> call, Throwable t) {
                Helper.hideProgressBar(Singleton.getInstance().getHome().progressBar);

                Helper.showToast("Something Went Wrong please try later");
            }

        });

    }

    private void hallsDialogBox(List<HallsDatum> hallsData) {

//            if (Singleton.getInstance().getHomeActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
//                for (int i = 0; i < Singleton.getInstance().getHomeActivity().getSupportFragmentManager().getBackStackEntryCount() - 1; ++i) {
//                    Singleton.getInstance().getHomeActivity().getSupportFragmentManager().popBackStack();
//                }
//            }


        ArrayList<String> halls = new ArrayList<>();
        for (int i = 0; i < hallsData.size(); i++) {
            halls.add(hallsData.get(i).getHallName());
        }


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);

        dialogView.requestLayout();
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

        TextView titleTxt = dialogView.findViewById(R.id.titleTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getHalls());
        alertDialog.show();


        //  EditText editText = dialog.findViewById(R.id.edtText);
        ListView listView = dialogView.findViewById(R.id.listView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Singleton.getInstance().getActivity(),
                android.R.layout.simple_list_item_1, halls);

        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Singleton.getInstance().setBackToReception(false);
                Singleton.getInstance().setHallId(hallsData.get(i).getHallId());
                //   Singleton.getInstance().setHomeState("hall");
                Singleton.getInstance().setHallName(hallsData.get(i).getHallName());
                Singleton.getInstance().getHome().callApiForHallData(false, true);
                alertDialog.dismiss();
                Singleton.getInstance().setActiveHall(hallsData.get(i).getHallName());

                Singleton.getInstance().setExhibitorsClicked(true);

                try {
                    Singleton.getInstance().getHome().slideDown(recyclerLayout);
                } catch (Exception e) {
                    Log.e("nav_exhibitions", e.toString());
                }

            }

        });
    }


//    public boolean onItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_home:
//                helper();
//                Singleton.getInstance().setHomeState("home");
//                replaceFragment(new Home(), "home");
//                break;
//            case R.id.nav_notification:
//                helper();
//
//                replaceFragment(new Notiifactions(), null);
//                break;
//            case R.id.nav_chat:
//                Singleton.getInstance().setIsHome(true);
//                try {
//                    // Helper.startUserChat();
//                    launchChatActivity();
//                } catch (Exception e) {
//                    Log.e("chat", e.toString());
//                }
//                break;
//        }
//        return false;
//    }

    public void launchChatActivity() {
        helper();
        Intent intent = new Intent(this, CometChatUI.class);
        intent.putExtra("fairId", Singleton.getInstance().getFairData().getFair().getId());
        intent.putExtra("intent", new Intent(Singleton.getInstance().getContext(), MainActivity.class));
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean onItemClickBubble(int postion) {
        switch (postion) {
            case 0:
                // if (!Singleton.getInstance().getSuperBackPressed()) {
//                    if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
//                        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount() - 1; i++) {
//                            getSupportFragmentManager().popBackStack();
//                            //  fragment.getChildFragmentManager().popBackStack();
//                        }
//                        getSupportFragmentManager().popBackStack();
//                        Fragment fragment = new Home();
//                        String backStateName = fragment.getClass().getName();
//
//                        FragmentManager manager = getSupportFragmentManager();
//                        boolean fragmentPopped = false;
//                        try {
//                            fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
//                        } catch (Exception e) {
//
//                        }
//
//                        if (!fragmentPopped) { //fragment not in back stack, create it.
//                            FragmentTransaction ft = manager.beginTransaction();
//                            ft.replace(R.id.frameLayout, fragment);
//                            ft.addToBackStack(backStateName);
//                            ft.commit();
//                        }
//                    }

                if (!Singleton.getInstance().getSuperBackPressed()) {
                    helper();
                    Singleton.getInstance().setHomeState("home");

                    replaceFragment(new Home(), "home");

                } else
                    Singleton.getInstance().setSuperBackPressed(false);

                //  }


                break;
            case 1:
                //  if (!Singleton.getInstance().getSuperBackPressed()) {

                helper();

                replaceFragment(new NotificationsFragment(), null);
                //  }
                //   else

                break;
            case 2:
                try {

//                    if( Singleton.getNotiifactions()!=null &&Singleton.getNotiifactions().isVisible())
//                    {
//                        bottomNavigationView.setCurrentActiveItem(1);
//                    }
//                    else
//                        bottomNavigationView.setCurrentActiveItem(0);
                    helper();
                    Intent intent = new Intent(this, CometChatUI.class);
                    intent.putExtra("fairId", Singleton.getInstance().getFairData().getFair().getId());
                    intent.putExtra("intent", new Intent(Singleton.getInstance().getContext(), MainActivity.class));
                    startActivity(intent);


                } catch (Exception e) {
                    Log.e("chat", e.toString());
                }
                break;
        }
        return false;
    }

    private void helper() {
        if (Singleton.getInstance().getYoutubePLayers() != null) {
            for (int i = 0; i < Singleton.getInstance().getYoutubePLayers().size(); i++)
                Singleton.getInstance().getYoutubePLayers().get(i).pause();
        }
        recyclerView.setVisibility(View.GONE);
        slideDown(recyclerLayout);
        if (Singleton.getInstance().getHomeState().equals("reception") || Singleton.getInstance().getHomeState().equals("stands"))
            Singleton.getInstance().getFloatingButton().setVisibility(View.VISIBLE);
    }

    public void replaceFragment(Fragment fragment, String tag) {
        try {
            boolean clearingFrags = false;
            if (getSupportFragmentManager().getFragments().size() > 0) {
                if (!Singleton.getInstance().getIsHome() || tag == null) {
                    Singleton.getInstance().setIsHome(false);

                    if (tag == "home") {
                        clearAllFragmentStack();
                        clearingFrags = true;

//                    for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
//                        getSupportFragmentManager().popBackStack();
//                        //  fragment.getChildFragmentManager().popBackStack();
//                    }
                    }
                    String backStateName = fragment.getClass().getName();
                    if (clearingFrags) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                FragmentManager manager = getSupportFragmentManager();
                                boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

                                if (!fragmentPopped) { //fragment not in back stack, create it.
                                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.frameLayout, fragment);
                                    ft.addToBackStack(tag);
                                    ft.commitAllowingStateLoss();
//                                    ft.commit();
                                }
                            }
                        }, 100);
                    } else {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frameLayout, fragment);
                        ft.addToBackStack(tag);
                        ft.commitAllowingStateLoss();
//                        ft.commit();
                    }
                }

            } else {

                getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(tag)
                        .commitAllowingStateLoss();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (drawerLayout != null)
                        drawerLayout.close();
                }
            }, 250);
        } catch (Exception e) {

        }
    }

    public void fragmentTrx(Fragment fragment, Bundle bundle, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment, tag);
        fragment.setArguments(bundle);
        transaction.addToBackStack(fragment.getTag());
        transaction.commitAllowingStateLoss();
    }

    public void clearAllFragmentStack() {
        AsyncTask.execute(() -> {
            Log.d("junaidthreds", "run: ");
            FragmentManager fm = getLastFragmentManagerWithBack(getSupportFragmentManager());
            if (fm.getBackStackEntryCount() > 0) {
                for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack();
                }
//            bottomNavigationView.getMenu().getItem(0).setChecked(true);

            }
        });

    }

    private FragmentManager getLastFragmentManagerWithBack(FragmentManager fm) {
        FragmentManager fmLast = fm;
        List<Fragment> fragments = fm.getFragments();
        for (Fragment f : fragments) {
            if (f.isAdded()) {
                if ((f.getChildFragmentManager() != null) && (f.getChildFragmentManager().getBackStackEntryCount() > 0)) {
                    fmLast = f.getFragmentManager();
                    FragmentManager fmChild = getLastFragmentManagerWithBack(f.getChildFragmentManager());
                    if (fmChild != fmLast)
                        fmLast = fmChild;
                }
            }
        }
        return fmLast;
    }

    private void setNavigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (System.currentTimeMillis() - mLastClickTime < 100) {
                    return false;
                }
                Bundle bundle = new Bundle();
                mLastClickTime = SystemClock.elapsedRealtime();
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        //      Singleton.getInstance().setSuperBackPressed(true);
                        //Singleton.getInstance().getBottomNavigationView().setCurrentActiveItem(0);
                        Singleton.getInstance().setIsHome(false);
                        Singleton.getInstance().setHomeState("home");
                        replaceFragment(new Home(), "home");
                        break;
                    case R.id.nav_reception:
                        Singleton.getInstance().setIsHome(false);
                        Singleton.getInstance().setHomeState("reception");
                        replaceFragment(new Home(), "home");
                        break;
                    case R.id.nav_exhibitions:
                        if (Helper.isInternetConnected()) {
                            callAPiForHalls();
                        }
                        break;
                    case R.id.nav_speaskers:
                        replaceFragment(new Speakers(), null);
                        Singleton.getInstance().setIsDashboard(false);
                        break;
                    case R.id.nav_about_us:
                        replaceFragment(new AboutUs(), null);
                        Singleton.getInstance().setIsDashboard(false);
                        break;
                    case R.id.nav_companies:
                        bundle.putBoolean("isFromSideBar", true);
                        Fragment fragment = new Companies();
                        fragment.setArguments(bundle);
                        replaceFragment(fragment, null);
                        Singleton.getInstance().setIsDashboard(false);
                        break;
                    case R.id.nav_networking:
                        replaceFragment(NetworkingTableFragment.newInstance(), null);
                        Singleton.getInstance().setIsDashboard(false);
                        break;
                    case R.id.nav_conference_agenda:
                        bundle.putBoolean("isFromSideBar", true);
                        Fragment conferenceAgenda = new ConferenceAgenda();
                        conferenceAgenda.setArguments(bundle);
                        Singleton.getInstance().setFromStandToWebinar(false);
                        Singleton.getInstance().setFromSpeakersToWebinar(false);
                        Singleton.getInstance().setIsDashboard(false);
                        Singleton.getInstance().setWebinarVideo(false);

                        replaceFragment(conferenceAgenda, null);

                        break;
                    case R.id.nav_speakers_agenda:
                        bundle.putBoolean("isFromSideBar", true);
                        bundle.putBoolean("speakersAgenda", true);
                        Fragment speakersAgenda = new ConferenceAgenda();
                        speakersAgenda.setArguments(bundle);
                        Singleton.getInstance().setFromStandToWebinar(false);
                        Singleton.getInstance().setFromSpeakersToWebinar(false);
                        Singleton.getInstance().setIsDashboard(false);
                        Singleton.getInstance().setWebinarVideo(false);

                        replaceFragment(speakersAgenda, null);

                        break;
                    case R.id.nav_stage_conference_agenda:
                        bundle.putBoolean("isFromSideBar", true);
                        Fragment frag = new StageConferenceAgenda();
                        frag.setArguments(bundle);
                        Singleton.getInstance().setWebinarVideo(false);

                        Singleton.getInstance().setFromStandToWebinar(false);
                        Singleton.getInstance().setFromSpeakersToWebinar(false);
                        Singleton.getInstance().setIsDashboard(false);
                        replaceFragment(frag, null);
                        break;
                    case R.id.nav_dashboard:
                        Singleton.getInstance().setIsDashboard(true);
                        replaceFragment(new DashBoard(), null);
                        break;
                    case R.id.nav_exit_survey:
                        Singleton.getInstance().setIsDashboard(false);
                        callApiForExitPOll(false);
                        drawerLayout.close();

                        break;
                    case R.id.nav_faq:
                        Singleton.getInstance().setIsDashboard(false);
                        replaceFragment(new FAQ(), null);
                        break;
                    case R.id.nav_jobs:
                        Singleton.getInstance().setIsDashboard(false);
                        Singleton.getInstance().setHomeState("jobs");
                        replaceFragment(new Jobs(), null);
                        break;
                    case R.id.nav_need_help:
                        replaceFragment(new NeedHelpFragment(), null);
                        break;
                    case R.id.nav_goodie_bags:

                        //throw new RuntimeException("Test Crash"); // Force a crash
                        Singleton.getInstance().setIsDashboard(false);
                        Singleton.getInstance().setGoodies(false);

                        replaceFragment(new GoodiesList(), null);

                        break;
                    case R.id.nav_courses:
                        Singleton.getInstance().setIsDashboard(false);
                        replaceFragment(new Courses(), null);
                        break;
                }
                return false;
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (Singleton.getInstance().getTouchEnabled()) {

            if (drawerLayout != null)
                drawerLayout.close();
            slideDown(recyclerLayout);
//            if (Singleton.getInstance().getWebinarToHall() || Singleton.getInstance().getWebinarToReception()) {
//                Singleton.getInstance().getHome().backToWebinar();
//            } else {
            Singleton.getInstance().getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            try {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                if (f instanceof Registration && !Singleton.getInstance().getIsLoggedIn()) {
                    clearAllFragmentStack();
                    fragmentTrx(new Events(), null, "events");
                    return;
                }
                if (f instanceof Events) {
                    killApp();
                    return;
                }
            } catch (Exception e) {

            }
            if (Singleton.getInstance().isPdfProfile())
                onBackPressProfile();
            else if (Singleton.getInstance().isPdfResume())
                onBackPressResume();
            else if (Singleton.getInstance().isPdfTasks())
                onBackPressTasks();
            else if (!Singleton.getInstance().getFlag()) {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (Singleton.getInstance().getLiveWebinar()) {
                    Singleton.getInstance().getLiveWebinarFragment().onBackPress();
                    Singleton.getInstance().setLiveWebinar(false);
                } else if (count == 2) {

                    Singleton.getInstance().setIsHome(!Singleton.getInstance().getHomeState().equals("home") && !Singleton.getInstance().getHomeState().equals("reception") && !Singleton.getInstance().getHomeState().equals("stands"));
                    super.onBackPressed();
                } else if (count == 1) {
                    if (!Singleton.getInstance().getSharedPreferences().getBoolean("login", false) && !Singleton.getInstance().getIsHome()) {
                        super.onBackPressed();
                    } else if (Singleton.getInstance().getHome() != null && Singleton.getInstance().getHome().isVisible() && (Singleton.getInstance().getHomeState().equals("home") || Singleton.getInstance().getHomeState().equals("reception"))) {
                        killApp();
                    } else {
                        if (Singleton.getInstance().getHome() != null)
                            Singleton.getInstance().getHome().onBackPress();
                        else super.onBackPressed();
                    }

                } else if (count == 0) {
                    if (Singleton.getInstance().getHomeState().equals("home") || Singleton.getInstance().getHomeState().equals("reception")) {
                        killApp();
                    } else {
                        Singleton.getInstance().getHome().onBackPress();
                    }
                } else {
                    if (!Singleton.getInstance().getLastPlayedVideo().equals("") && Singleton.getInstance().getLastPlayedVideo().equals("webinarToReception")) {
//                    Singleton.getInstance().getHome().ShareClicked("webinars",0);
//
//                    FragmentManager fm = getSupportFragmentManager();
//
//                    // args.putInt("index", index);
//                    fm.beginTransaction().add(R.id.frameLayout, fragment)
//                            .setReorderingAllowed(true)
//                            .addToBackStack("liveWebinar")
//                            .commit();
                        super.onBackPressed();

                        // getSupportFragmentManager().popBackStack();

                    } else {
                        super.onBackPressed();

                        //  getSupportFragmentManager().popBackStack();
                        //getActivity().getSupportFragmentManager().popBackStack();
                        if (!Singleton.getInstance().getPreviousHomeState().equals("")) {
                            Singleton.getInstance().setHomeState(Singleton.getInstance().getPreviousHomeState());
                            Singleton.getInstance().setPreviousHomeState("");
                        }
                    }

                }

//            if (Singleton.getInstance().getHomeState().equals("stands") || Singleton.getInstance().getHomeState().equals("reception_back"))
//                Singleton.getInstance().getFloatingButton().setVisibility(View.VISIBLE);
            } else {
                Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
                try {
//                    nav_bottom_home.setVisibility(View.GONE);
                } catch (Exception e) {

                }
                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count == 0) {
                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                    if (f instanceof Login) {
                        clearAllFragmentStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Registration())
                                .setReorderingAllowed(true)
                                .addToBackStack("events")
                                .commit();
                    } else super.onBackPressed();
                } else if (count == 1) {
                    clearAllFragmentStack();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Events())
                            .setReorderingAllowed(true)
                            .addToBackStack("events")
                            .commit();
                } else if (count == 3 || count == 2) {
                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                    if (f instanceof Login) {
                        clearAllFragmentStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Registration())
                                .setReorderingAllowed(true)
                                .addToBackStack("events")
                                .commit();
                    } else super.onBackPressed();
                } else {
                    killApp();
                    //super.onBackPressed();
                }
            }
        }

    }

    private void killApp() {
        //  Singleton.getInstance().setSuperBackPressed(false);
        if (Singleton.getInstance().getExit()) {
            finishAffinity();
            System.exit(0);
            return;
        }
        if (Singleton.getInstance().getHomeState() != "home")
            Singleton.getInstance().getFloatingButton().setVisibility(View.VISIBLE);
        Singleton.getInstance().setExit(true);
        Helper.showToast("Press again to exit");

        //  Toast.makeText(this, , Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Singleton.getInstance().setExit(false);
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 10)
                getLocation();
            else if (requestCode == 2404) {
                android.app.Fragment fragment = getFragmentManager().findFragmentById(R.id.edit_user_profile);
                fragment.onActivityResult(requestCode, resultCode, data);
            }

        } catch (Exception e) {
            //  Helper.showToast("sd");

        }
//        if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            Uri picUri = result.getUri();
//            Bitmap bitmapp = null;
//            try {
//                bitmapp = Helper.getBitmapFromUri(picUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            UserProfile.croppedImage(bitmapp, data);
//
//
//        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//            Toast.makeText(Singleton.getInstance().getContext(), "nfdzk", Toast.LENGTH_SHORT).show();
//        }

        // if(resultCode==RESULT_OK) {

        // }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Helper.showToast("Wadlkfmlwkmflk");

    }


    private void logoutCometChat() {
        if (CometChat.isInitialized())
            CometChat.logout(new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Helper.showToast("User Logout Successfully");
                }

                @Override
                public void onError(CometChatException e) {
                    Helper.showToast("User Logout Failure");


                }
            });
    }

    private void geoCodeLocation() {
        Geocoder geocoder = new Geocoder(Singleton.getInstance().getContext(), Locale.getDefault());
        try {
            String postalCode = " ";
            try {
                postalCode = Singleton.getInstance().getAddresses().get(0).getPostalCode();
            } catch (Exception e) {
//                Helper.showToast(e.toString());

            }
            addresses = geocoder.getFromLocation(Singleton.getInstance().getLocation().getLatitude(), Singleton.getInstance().getLocation().getLongitude(), 1);
            Singleton.getInstance().setAddresses(addresses);
            new Thread(new Runnable() {
                public void run() {
                    // a potentially time consuming task
                    showByShortName();
//                    getExtraField();
                }
            }).start();
            SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
            editor.putString("postalCode", postalCode);
            editor.apply();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLocation() {
        if (Helper.isInternetConnected()) {
            if (Helper.isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(Singleton.getInstance().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Singleton.getInstance().getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                } else {
                    getAddress();
                    // Write you code here if permission already given.
                }

            } else {
                try {
                    Singleton.getInstance().getFloatingButton().setVisibility(View.GONE);
                } catch (Exception e) {
                    Helper.showToast("");

                }
                Helper.buildAlertMessageNoGps();
            }
        } else
            Helper.showToast("No Internet Connection");
    }

    @SuppressLint("MissingPermission")
    public void getAddress() {
        locationManager = (LocationManager) Singleton.getInstance().getActivity().getApplicationContext().getSystemService(Singleton.getInstance().getContext().LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        Singleton.getInstance().setLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        if (Singleton.getInstance().getLocation() == null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        //Request update as location manager can return null otherwise
        else {
            geoCodeLocation();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location == null) {
            Singleton.getInstance().setLocation(location);
            geoCodeLocation();
        }
        locationManager.removeUpdates(this);
        // }

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    public void showByShortName() {
        if (Helper.isInternetConnected()) {
            String location = " ";
            try {
                location = addresses.get(0).getCountryName() + "," + addresses.get(0).getLocality();
            } catch (Exception e) {
                // Helper.showToast("");
                location = Singleton.getInstance().getLocation().getLatitude() + "," + Singleton.getInstance().getLocation().getLongitude();


            }
            JsonObject JsonObject = new JsonObject();
            JsonObject.addProperty("fair_id", Singleton.getInstance().getSharedPreferences().getInt("fairId", 0));
            JsonObject.addProperty("timezone", TimeZone.getDefault().getID());
            JsonObject.addProperty("ip", Helper.getIPAddress(true));
            JsonObject.addProperty("location", location);
            JsonObject.addProperty("device", "android");
            try {
                JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
            } catch (Exception e) {

            }

            // JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginDate().getUser().getId());
            getFairDetailsApi(JsonObject);
        }
    }

    public void getExtraField() {
        if (Helper.isInternetConnected()) {
            String location = " ";
            try {
                location = addresses.get(0).getCountryName() + "," + addresses.get(0).getLocality();
            } catch (Exception e) {
                // Helper.showToast("");
                location = Singleton.getInstance().getLocation().getLatitude() + "," + Singleton.getInstance().getLocation().getLongitude();


            }
            JsonObject JsonObject = new JsonObject();
            JsonObject.addProperty("fair_id", Singleton.getInstance().getSharedPreferences().getInt("fairId", 0));
            JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
            getFairExtraFieldsApi(JsonObject);
        }
    }

    private void getFairDetailsApi(JsonObject JsonObject) {


        Call<FairDetail> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getFairDetails(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<FairDetail>() {
            @Override
            public void onResponse(Call<FairDetail> call, Response<FairDetail> response) {
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.body().getStatus()) {
                        if (Singleton.getInstance().getIsLoggedIn()) getFairHallsApi();
                        if (Singleton.getInstance().getIsLoggedIn()) getExtraField();
                        parseResponse(response);

                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
//                    Helper.showToast("Something went wrong...Please try later!");
                }
            }

            @Override
            public void onFailure(Call<FairDetail> call, Throwable t) {
                Helper.showToast("Something went wrong...Please try later!");
                // Toast.makeText(Singleton.getInstance().getContext(), "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getFairExtraFieldsApi(JsonObject JsonObject) {
        Call<FairExtraFieldsResponse> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getFairExtraFields(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<FairExtraFieldsResponse>() {
            @Override
            public void onResponse(Call<FairExtraFieldsResponse> call, Response<FairExtraFieldsResponse> response) {
                try {
                    if (response.code() == 204) {
                        parseExtraFieldsResponse(response);
                    } else if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.body().getStatus()) {
                        parseExtraFieldsResponse(response);

                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
                    Helper.showToast("Something went wrong...Please try later!");
                }
            }

            @Override
            public void onFailure(Call<FairExtraFieldsResponse> call, Throwable t) {
                Helper.showToast("Something went wrong...Please try later!");
                // Toast.makeText(Singleton.getInstance().getContext(), "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void parseResponse(Response<FairDetail> response) {
        //  response.body().getData().getFair().setStartTime("2022-02-24 15:03:00");
        assert response.body() != null;
        Data fairData = setLanguageOrder(response);
//        response.body().getData().getFair().setFairTranslations(newTranslations);
        String fairEnd = Singleton.getInstance().getFairData().getFair().getFairTiming().getFairEnd();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date1 = format.parse(fairEnd);
            Date currentDate = format.parse(DateTime.getCurrentDateTime());
            assert date1 != null;
            if (!date1.equals(currentDate) && date1.after(currentDate)) {
                Singleton.getInstance().setFairData(fairData);
                SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
                Gson gson = new Gson();
                String json = gson.toJson(fairData);
                editor.putString("fairDetails", json);
                editor.putInt("goodieCount", response.body().getData().getFairCandidateGoodiesCount());
                Singleton.getInstance().setGoodieCount(response.body().getData().getFairCandidateGoodiesCount());

                editor.apply();
                getFairDetails();

                goodieBagBadge();

                initializeTimer();
                Singleton.saveChatDetails(fairData.getChatApiDetails());
                setColors();
            } else {
                fairId = String.valueOf(Singleton.getMyFairId());
                SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
                Singleton.getInstance().setLoginData(null);
                Singleton.getInstance().setFairData(null);
                editor.clear();
                editor.apply();
                editor.commit();
//                bottomNavigationView.setVisibility(View.GONE);
                chipNavigationBar.setVisibility(View.GONE);
                drawerLayout.close();
                FragmentManager fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                logoutCometChat();
                Singleton.getInstance().setLogout(true);

                Singleton.getInstance().setIsLoggedIn(null);
                Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
                chipNavigationBar.setItemEnabled(R.id.nav_home, false, Singleton.getHomeTitle());
                Singleton.getInstance().getActivity().finish();
                Singleton.getInstance().getActivity().overridePendingTransition(0, 0);
                MainApp.getAppContext().startActivity(Singleton.getInstance().getActivity().getIntent());
                Singleton.getInstance().getActivity().overridePendingTransition(0, 0);
                Helper.showToast("Your event has been ended!");
            }
        } catch (ParseException exception) {

        }


    }

    private void parseExtraFieldsResponse(Response<FairExtraFieldsResponse> response) {
        if (response.body() != null) {
            //  response.body().getData().getFair().setStartTime("2022-02-24 15:03:00");
            Singleton.getInstance().setExtraFieldsFairData(response.body().getFairData());
            Singleton.getInstance().getLoginData().getUser().setExtraFields(response.body().getFairData().getExtraFields());
            SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
            Gson gson = new Gson();
            String json = gson.toJson(response.body().getFairData());
            editor.putString("fairFeildsData", json);

            editor.apply();
        }
    }

    public void getFairHallsApi() {
        Call<FairHallsResponse> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getFairHalls(Singleton.getInstance().getSharedPreferences().getInt("fairId", 0)
                , "Bearer " + Singleton.getInstance().getLoginData().getAccessToken()
                , Singleton.getInstance().getLanguage(),
                "application/json", AppConstants.APP_ID,
                AppConstants.APP_KEY);
        call.enqueue(new Callback<FairHallsResponse>() {
            @Override
            public void onResponse(Call<FairHallsResponse> call, Response<FairHallsResponse> response) {
                try {
                    if (response.body().getStatus()) {
                        FairHallsResponse fairHallsResponse = response.body();
                        if (fairHallsResponse != null) {
                            Singleton.getInstance().setFairHalls((ArrayList<HallsDatum>) fairHallsResponse.getData().getHallsData());
                            SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(fairHallsResponse.getData().getHallsData());
                            editor.putString("fair_halls", json);
                            editor.apply();
                        }
                    } else
                        Helper.showToast("Something went wrong. Please try again");

                } catch (Exception e) {
                    Helper.showToast("Something went wrong...Please try later!");

                }
            }

            @Override
            public void onFailure(Call<FairHallsResponse> call, Throwable t) {
                Helper.showToast("Something went wrong...Please try later!");
                //Toast.makeText(Singleton.getInstance().getContext(), "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void bufferVideos() {
        mainPlayer = this.findViewById(R.id.videoPreBuffer);
        String proxyUrl;
        if (!Singleton.getInstance().getProxy().isCached(AppConstants.getHomeVideo())) {
            proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(AppConstants.getHomeVideo());
            videoPreBuffer(proxyUrl, false, "home");
        } else if (!Singleton.getInstance().getProxy().isCached(AppConstants.getHomeToReceptionVideo())) {
            proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(AppConstants.getHomeToReceptionVideo());
            videoPreBuffer(proxyUrl, false, "reception");
        } else if (!Singleton.getInstance().getProxy().isCached(AppConstants.getReceptionBackVideo())) {
            proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(AppConstants.getReceptionBackVideo());
            videoPreBuffer(proxyUrl, false, "receptionBackground");
        } else if (!Singleton.getInstance().getProxy().isCached(AppConstants.getHall1BackgroundVideo())) {
            proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(AppConstants.getHall1BackgroundVideo());
            videoPreBuffer(proxyUrl, false, "hallBackGround");
        } else if (!Singleton.getInstance().getProxy().isCached(AppConstants.getReceptionToHall1())) {
            proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(AppConstants.getReceptionToHall1());
            videoPreBuffer(proxyUrl, false, "hall");
        } else if (!Singleton.getInstance().getProxy().isCached(AppConstants.getHall1BackgroundVideo())) {
            proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(AppConstants.getHall1BackgroundVideo());
            videoPreBuffer(proxyUrl, false, "hallBack");
        } else if (!Singleton.getInstance().getProxy().isCached(AppConstants.getReceptionToWebinar())) {
            proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(AppConstants.getReceptionToWebinar());
            videoPreBuffer(proxyUrl, false, "reception_to_webinar");
        } else if (!Singleton.getInstance().getProxy().isCached(AppConstants.getWebinar_BACKGROUND())) {
            proxyUrl = Singleton.getInstance().getProxy().getProxyUrl(AppConstants.getWebinar_BACKGROUND());
            videoPreBuffer(proxyUrl, false, "webinarBackground");
        }
    }

    private void videoPreBuffer(String path, boolean b, String video) {

        // new VideoPreloadTask(this,AppConstants.HOME_VIDEO).execute();

        mainPlayer.setVideoPath(path);
        mainPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(false);
                mainPlayer.setSaveEnabled(true);
                mainPlayer.start();

                // mainPlayer.seekTo(mainPlayer.getDuration());
            }
        });


        mainPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                bufferVideos();
            }
        });
    }

    @Override
    protected void onStart() {
        try {
            super.onStart();
        } catch (Exception e) {
            Helper.showToast("On Start");

        }
    }

    @Override
    protected void onRestart() {
        try {
            super.onRestart();

        } catch (Exception e) {
            Helper.showToast("onRestart");

        }
    }

    @Override
    protected void onStop() {
        try {
            super.onStop();
        } catch (Exception e) {
            Helper.showToast("onStop");
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
        } catch (Exception e) {
            Helper.showToast("onDestroy");
        }
    }

    private void callApiForExitPOll(Boolean logout) {
        try {
            ProgressDialog proDialog = ProgressDialog.show(this, "Loading", "Please Wait");
            proDialog.show();

            JsonObject JsonObject = new JsonObject();
            JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
            JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());


            Call<ExitPoll> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getExitPolling(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
            call.enqueue(new Callback<ExitPoll>() {
                @Override
                public void onResponse(Call<ExitPoll> call, Response<ExitPoll> response) {
                    //      Helper.hideProgressBar(progressBar);
                    proDialog.dismiss();
                    try {
                        if (response.raw().code() == 401) {
                            Helper.showToast("Session Expired");
                            Helper.clearUserData();
                        } else if (response.raw().code() == 204) {
                            Helper.showToast("No Data Found");
                            if (logout)
                                openLogoutDialog();

                        } else if (response.body().getStatus()) {
                            if (response.body().getData().getSurveyList().size() > 0) {
                                Singleton.getInstance().setExitPollData(response.body().getData());
                                if (response.body().getData().getAttended() && Singleton.getInstance().getFairData().getFair().getOptions().getEnable_edit_candidate_exit_survey_front() == 1) {
                                    showPollDialog(logout, true);
                                } else if (!response.body().getData().getAttended() && Singleton.getInstance().getFairData().getFair().getOptions().getDisable_exit_survey_logout() == 0) {
                                    showPollDialog(logout, false);
                                } else if (!response.body().getData().getAttended() && Singleton.getInstance().getFairData().getFair().getOptions().getDisable_exit_survey_logout() == 0) {
                                    showPollDialog(logout, false);
                                } else if (!response.body().getData().getAttended() && Singleton.getInstance().getFairData().getFair().getOptions().getDisable_exit_survey_logout().equals(1)) {
                                    showPollDialog(false, false);
                                } else if (!logout && Singleton.getInstance().getFairData().getFair().getSurveyResultPopUp().equals("1")) {
                                    showPollResultDialog();
                                } else if (logout)
                                    openLogoutDialog();
                                else {
                                    // Helper.showToast("Disabled");
                                    menu = navigationView.getMenu();
                                    MenuItem target = menu.findItem(R.id.nav_exit_survey);
                                    target.setTitle(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getExitSurvey());
//                                    target.setVisible(false);
                                    showPollResultDialog();
                                }
                            } else {
                                openLogoutDialog();

                                Helper.showToast("No Data Found");

                            }

                        }
                    } catch (Exception e) {
                        Helper.showToast("Something Went Wrong");
                        openLogoutDialog();

                    }

                }

                @Override
                public void onFailure(Call<ExitPoll> call, Throwable t) {
                    proDialog.dismiss();
                    if (logout)
                        openLogoutDialog();

                    Helper.showToast("Something Went Wrong");
                    //   Helper.hideProgressBar(progressBar);
                }

            });
        } catch (Exception e) {
            openLogoutDialog();
        }

    }

    private void showPollResultDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_poll_result, null);
        RecyclerView recyclerView;
        ExitPollResultAdapter adapter;
        dialogBuilder.setView(dialogView);
        dialogView.requestLayout();
        AlertDialog alertDialog = dialogBuilder.create();
        //    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        TextView textView = dialogView.findViewById(R.id.titleTxt);
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getExitSurveyResult());

        adapter = new ExitPollResultAdapter(Singleton.getInstance().getExitPollData().getSurveyList());
        recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ImageView crossImageView = dialogView.findViewById(R.id.crossButtonsLayoutImageView);
        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                long now = System.currentTimeMillis();
//                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
//                    return;
//                }
//                mLastClickTime = now;

                alertDialog.cancel();

            }
        });

        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });

    }

    private void showPollDialog(Boolean logout, Boolean edit) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_poll_questions, null);
        RecyclerView recyclerView;
        ExitPollAdapter adapter;
        dialogBuilder.setView(dialogView);
        dialogView.requestLayout();
        AlertDialog alertDialog = dialogBuilder.create();
        //    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        TextView textView = dialogView.findViewById(R.id.titleTxt);
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getExitSurvey());
        //textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAre());


        Button saveSurvey = dialogView.findViewById(R.id.saveBtn);
        saveSurvey.setText(Singleton.getKeywords().getSave() + " " + Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSurvey());
        Helper.setButtonColorWithDrawable(saveSurvey, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(), Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());

        adapter = new ExitPollAdapter(alertDialog, Singleton.getInstance().getExitPollData().getSurveyList(), edit);
        recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        saveSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                alertDialog.cancel();
                JSONArray jsonArray = (adapter.getPollAnswers());
                JSONArray radioArray = new JSONArray();
                if (adapter.getRadioAnswers() != null) {
                    radioArray = adapter.getRadioAnswers();
                    for (int i = 0; i < radioArray.length(); i++) {
                        try {
                            jsonArray.put(radioArray.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!jsonArray.isNull(0))
                    callApiToSaveStandPOll(jsonArray);
                else Helper.showToast("Please fill survey to continue");
                if (logout)
                    openLogoutDialog();
            }
        });

        ImageView crossImageView = dialogView.findViewById(R.id.crossButtonsLayoutImageView);

        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;

                alertDialog.cancel();

                if (logout){
                    openLogoutDialog();
                }


            }
        });


//
//        if (alertDialog.getWindow() != null){
//            alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        }
        //alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        //  alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
//


//        InputMethodManager imm = (InputMethodManager) Singleton.getInstance().getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);


    }

    private void callApiToSaveStandPOll(JSONArray pollAnswers) {
        JsonObject jsonObject = new JsonObject();
        JsonArray images = new JsonArray();

        try {
//            for (int i = 0; i < pollAnswers.length(); i++) {
//                if (pollAnswers.getJSONObject(i).getString("poll_id") == null || pollAnswers.getJSONObject(i).getString("poll_id").equalsIgnoreCase("")) {
//                    allValuesFilled = false;
//                }
//                if (pollAnswers.getJSONObject(i).getString("option_id") == null || pollAnswers.getJSONObject(i).getString("option_id").equalsIgnoreCase("")) {
//                    allValuesFilled = false;
//                }
//                if (pollAnswers.getJSONObject(i).getString("poll_type") == null || pollAnswers.getJSONObject(i).getString("poll_type").equalsIgnoreCase("")) {
//                    allValuesFilled = false;
//                }
//            }
//            if (allValuesFilled) {

            jsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
            jsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());


            for (int i = 0; i < pollAnswers.length(); i++) {
                JsonObject test = new JsonObject();
                String pollId = pollAnswers.getJSONObject(i).getString("poll_id");
                String optionId = pollAnswers.getJSONObject(i).getString("option_id");
                String pollType = pollAnswers.getJSONObject(i).getString("poll_type");
                test.addProperty("survey_id", pollId);
                test.addProperty("survey_type", pollType);
                test.addProperty("option_id", optionId);
                if (pollType.equalsIgnoreCase("checkbox")) {
                    boolean isChecked = pollAnswers.getJSONObject(i).getBoolean("is_checked");
                    test.addProperty("uncheck", !isChecked);
                }

                images.add(test);
            }
            jsonObject.add("survey_data", images);


            Call<ExitPoll> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).saveExitPoll(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, jsonObject);

            call.enqueue(new Callback<ExitPoll>() {
                @Override
                public void onResponse(Call<ExitPoll> call, Response<ExitPoll> response) {
                    //   Helper.hideProgressBar(progressBar);
                    try {
                        if (response.raw().code() == 401) {
                            Helper.showToast("Session Expired");
                            Helper.clearUserData();
                        } else if (response.raw().code() == 204) {
                            Helper.showToast("No Data Found");
                        } else if (response.body().getStatus()) {
                            // Singleton.getInstance().setExitPOllData(response.body().getData());
                            Helper.showToast("Poll saved Successfully");
                        }
                    } catch (Exception e) {
                        Helper.showToast("Something Went Wrong");
                    }
                }

                @Override
                public void onFailure(Call<ExitPoll> call, Throwable t) {
                    Helper.showToast("Something Went Wrong");
                    // Helper.hideProgressBar(progressBar);
                }
            });
//            } else {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Helper.showToast("Please fill all fields to save survey");
//                    }
//                });
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getWelcomeContent(ProgressBar progressBar, String fairId) {
        if (progressBar != null) Helper.showProgressBar(progressBar);
        String url = "api/auth/fair/welcomecontentafterregistration/" + fairId;

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
                        openWelcomeDialog(response.body().getPrivacyCondition().getWelcomeNote());
                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
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

    public void openWelcomeDialog(String data) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_term_and_conditions, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        ImageView cancel = dialogView.findViewById(R.id.cancelImageView);
        TextView webinarTitleTxt = dialogView.findViewById(R.id.webinarTitleTxt);
        TextView descriptionTxt = dialogView.findViewById(R.id.descriptionTxt);
        webinarTitleTxt.setText(Singleton.getKeywords().getWelcome_to() + " " + Singleton.getKeywords().getFair_name());
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(int id) {
        onItemClickBubble(id);
//        switch (id) {
//            case R.id.nav_home:
//
//                break;
//            case R.id.nav_notification:
//                onItemClickBubble(1);
//                break;
//            case R.id.nav_chat:
//                onItemClickBubble(2);
//                break;
//        }
    }

    class VideoPreloadTask extends AsyncTask<String, Void, Void> {

        private final Context mContext;
        private final String videUrl;

        public VideoPreloadTask(Context context, String homeVideo) {
            mContext = context;
            videUrl = homeVideo;
        }

        @Override
        public void onPreExecute() {

        }


        @Override
        protected Void doInBackground(String... params) {


            return null;
        }
    }


}

