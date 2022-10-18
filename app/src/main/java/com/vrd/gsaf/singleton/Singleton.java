package com.vrd.gsaf.singleton;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.vrd.gsaf.activity.MainActivity;
import com.vrd.gsaf.adapters.HallModel;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.careerTest.Question;
import com.vrd.gsaf.api.responses.companies.Company;
import com.vrd.gsaf.api.responses.compnayDetail.CompanyDetails;
import com.vrd.gsaf.api.responses.dashboardRecruiters.Recruiter;
import com.vrd.gsaf.api.responses.events.Fairs;
import com.vrd.gsaf.api.responses.fairDetail.ChatApiDetails;
import com.vrd.gsaf.api.responses.fairDetail.Data;
import com.vrd.gsaf.api.responses.fairDetail.Fair;
import com.vrd.gsaf.api.responses.fairDetail.Keywords;
import com.vrd.gsaf.api.responses.fairDetail.Options;
import com.vrd.gsaf.api.responses.fairExtraFields.FairData;
import com.vrd.gsaf.api.responses.goodieBag.GoodieBag;
import com.vrd.gsaf.api.responses.halls.HallsDatum;
import com.vrd.gsaf.api.responses.jobs.Job;
import com.vrd.gsaf.api.responses.schedules.InterviewSlot;
import com.vrd.gsaf.api.responses.speakers.Speaker;
import com.vrd.gsaf.api.responses.webinarDetail.Detail;
import com.vrd.gsaf.api.responses.webinars.Webinar;
import com.vrd.gsaf.app.AppSession;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.home.courses.CoursesAdapter;
import com.vrd.gsaf.home.dashboard.DashBoardModel;
import com.vrd.gsaf.home.homeTab.Home;
import com.vrd.gsaf.home.homeTab.media.MediaModel;
import com.vrd.gsaf.home.jobs.JobAdapter;
import com.vrd.gsaf.home.liveWebinar.LiveWebinar;
import com.vrd.gsaf.home.notiifcations.NotificationModel;
import com.vrd.gsaf.home.notiifcations.NotificationsFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Singleton {


    public static SharedPreferences sharedPreferences = null;
    public static Context context;
    private static Button enterButton = null;
    private static NotificationsFragment notificationsFragment = null;
    private static String language = "109";
    private static Singleton singleInstance = null;
    private final ArrayList<NotificationModel> notificationData = new ArrayList<>();
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final ArrayList<DashBoardModel> dashboardData = new ArrayList<>();
    private final ArrayList<MediaModel> mediaList = new ArrayList();
    private final ArrayList<YouTubePlayer> youtubePLayers = new ArrayList();
    public Boolean logout = false;
    public Boolean stageWebinars = false;
    public Boolean liveWebinar = false;
    public Boolean backToReception = false;
    public Boolean exhibitorsClicked = false;
    public Boolean fromCoursesToJobDetails = false;
    public String activeHall;
    Webinar webinar;
    boolean fromSideStands = false;
    private int languageIndex = AppSession.getInt("languageIndex");
    private final ApiInterface service = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
    private Detail webinarDetail = null;
    private int goodieCount = 0;
    private MainActivity mainActivity;
    private Activity activity;
    private DrawerLayout drawerLayout;
    private Boolean isLoginRequired = true;
    private Boolean isWebinarToHall = false;
    private Boolean isWebinarToReception = false;
    private Boolean isTouchEnabled = true;
    private Boolean webinarVideo = false;
    private Boolean fromCompaniesToStand = true;
    private Boolean isSuperBackPressed = false;
    private Boolean isDashboard = false;
    private Boolean isHome = true;
    private Boolean isLive = false;
    private Boolean goodies = false;
    private Boolean flag = false;
    private Boolean newRegistration = false;
    private Boolean activityRunning = true;
    private Boolean removeJobFrag = false;
    private boolean isLiveWebinar = false;
    private Integer hallId;
    private String speakerTitle;
    private String lastPlayedVideo = "";
    private String hallName;
    private int standIndex;
    private JobAdapter adapter;
    //    private BubbleToggleView nav_bottom_home;
    private CoursesAdapter coursesAdapter;
    private Boolean isExit = false;
    private String homeState = "home";
    private String previousHomeState = "";
    private String firebaseToken;
    private ChipNavigationBar bottomNavigationView;
    private int fairId;
    private String shortname;
    private Boolean loginFragmentFlag = false;
    private Boolean splashShown = false;
    private String otp;
    private Location location;
    private RelativeLayout floatingButton;
    private Boolean isLoggedIn;
    private View homeView;
    private ArrayList<Speaker> speakerData = new ArrayList<>();
    private com.vrd.gsaf.api.responses.receptionist.Data receptionistDetails;
    private ArrayList<Company> companiesData = new ArrayList<>();
    private List<Job> jobsData = new ArrayList<>();
    private ArrayList<Job> coursesData = new ArrayList<>();
    private ArrayList<String> buttonsList = new ArrayList();
    private ArrayList<Webinar> webinarList = new ArrayList();
    private ArrayList<Webinar> stageWebinarList = new ArrayList();
    private ArrayList<Question> careerTest = new ArrayList();
    private ArrayList<Recruiter> recruiterArrayList = new ArrayList();
    private ArrayList<GoodieBag> candidateGoodieBagList = new ArrayList();
    private ArrayList<InterviewSlot> schedulesList = new ArrayList();
    private ArrayList<com.vrd.gsaf.api.responses.stands.Company> standsDataAgainstHall = new ArrayList();
    private ArrayList<HallModel> hallModelArrayList = new ArrayList();
    private ArrayList<HallsDatum> fairHalls = new ArrayList();
    private List<com.vrd.gsaf.api.responses.goodies.GoodieBag> goodiesList = new ArrayList<>();
    private com.vrd.gsaf.api.responses.standPoll.Data standPollData;
    private com.vrd.gsaf.api.responses.exitPoll.Data exitPollData;
    private com.vrd.gsaf.api.responses.compnayDetail.Data companyMedia = null;
    private CompanyDetails companyDetails = null;
    private com.vrd.gsaf.api.responses.fairMedia.Data fairMedia = null;
    private JsonObject filter = new JsonObject();
    private Boolean applyFilter = false;
    private NavigationView navigationView;
    private String postalCode;
    private boolean pdfProfile = false;
    private boolean pdfResume = false;
    private boolean pdfTasks = false;
    private boolean isRegistration = false;
    private int standCompanyId;
    private Boolean fromStandToWebinar = false;
    private Boolean fromStandToMatchingSlots = false;
    private Boolean fromSpeakersToWebinar = false;
    private Boolean careerTestGiven = false;
    private JsonObject careerTestOptions = new JsonObject();
    private Fairs fairs;
    private Home home;
    private LiveWebinar liveWebinarFragment;
    private Boolean isViewPager = false;
    private Data fairData;
    private FairData extraFieldsFairData;
    private com.vrd.gsaf.api.responses.login.Data loginData;
    private List<Address> addresses;
    private String Email;
    //variables on orientation change of video view
    private String videoUrl;
    private Boolean isPlaying;
    private int seekTo;
    private int selectedCareerOptions = 0;
    private HttpProxyCacheServer proxy;
    private MainApp app = new MainApp();
    private int speakerId;

    public static Integer getMyFairId() {
        int fairId = Singleton.getInstance().getSharedPreferences().getInt("fairId", 0);
        return fairId;
    }

    public static ChatApiDetails getChatDetails() {
        sharedPreferences = MainApp.getAppContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String chatString = sharedPreferences.getString("chatDetails", null);
        ChatApiDetails chatApiDetails = new Gson().fromJson(chatString, ChatApiDetails.class);
        return chatApiDetails;
    }

    public static void saveChatDetails(ChatApiDetails chatApiDetails) {
        SharedPreferences.Editor editor = MainApp.getAppContext().getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
        editor.putString("chatDetails", new Gson().toJson(chatApiDetails));
        editor.apply();
    }

    public static Singleton getInstance() {
        if (singleInstance == null)
            singleInstance = new Singleton();
        return singleInstance;
    }

    public static int getTopNavColor() {
        if (Singleton.getInstance().getFairData() != null)
            return Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor());
        else return Color.parseColor("#ffffff");
    }

    public static int getCardBGColor() {
        if (Singleton.getInstance().getFairData() != null && Singleton.getInstance().getFairData().getFair().getOptions().getMobile_card_background_color() != null)
            return Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getMobile_card_background_color());
        else return Color.parseColor("#ffffff");
    }

    public static String getHomeTitle() {
        if (Singleton.getInstance().getFairData() != null && Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getHome() != null)
            return Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getHome();
        else return "Home";
    }

    public static String getNotificationTitle() {
        if (Singleton.getInstance().getFairData() != null && Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getNotifications() != null)
            return Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getNotifications();
        else return "Notifications";
    }

    public static String getMessageTitle() {
        if (Singleton.getInstance().getFairData() != null && Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMessages() != null)
            return Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMessages();
        else return "Message";
    }

    public static int getCardTextColor() {
        if (Singleton.getInstance().getFairData() != null && Singleton.getInstance().getFairData().getFair().getOptions().getMobile_card_text_color() != null)
            return Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getMobile_card_text_color());
        else return Color.GRAY;
    }

    public static int getSidebarInnerTextColor() {
        if (Singleton.getInstance().getFairData() != null)
            return Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarInnerTextColor());
        else return Color.GRAY;
    }

    public static int getTopNavInnerTextColor() {
        if (Singleton.getInstance().getFairData() != null)
            return Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavInnerTextColor());
        else return Color.GRAY;
    }
    public static void changeIconColor(ImageView imageView) {
        try {
            Drawable drawable = imageView.getDrawable();
            drawable.setColorFilter(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()), PorterDuff.Mode.MULTIPLY);
            imageView.setImageDrawable(drawable);
        } catch (Exception e) {
            Log.d("Color Exception", e.toString());
        }
    }

    public static void changeIconColorToTextColor(ImageView imageView) {
        try {
            Drawable drawable = imageView.getDrawable();
            drawable.setColorFilter(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarInnerTextColor()), PorterDuff.Mode.MULTIPLY);
            imageView.setImageDrawable(drawable);
        } catch (Exception e) {
            Log.d("Color Exception", e.toString());
        }
    }

    public static void changeIconColor(int drawableId, ImageView imageView) {
        try {
            Drawable drawable = MainApp.getAppContext().getResources().getDrawable(drawableId);
            drawable.setColorFilter(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()), PorterDuff.Mode.SRC_IN);
            imageView.setImageDrawable(drawable);
        } catch (Exception e) {
            Log.d("Color Exception", e.toString());
        }
    }

    public static void changeIconColor(int drawableId, ImageView imageView, int color) {
        try {
            Drawable drawable = MainApp.getAppContext().getResources().getDrawable(drawableId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            imageView.setImageDrawable(drawable);
        } catch (Exception e) {
            Log.d("Color Exception", e.toString());
        }
    }

    public static void changeVectorIconColor(int drawableId, ImageView imageView, int color) {
        try {
            Drawable drawable = MainApp.getAppContext().getResources().getDrawable(drawableId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            imageView.setImageDrawable(drawable);
        } catch (Exception e) {
            Log.d("Color Exception", e.toString());
        }
    }

    public static void changeBGColor(int drawableId, View view) {
        try {
            Drawable drawable = MainApp.getAppContext().getResources().getDrawable(drawableId);
            drawable.setColorFilter(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()), PorterDuff.Mode.MULTIPLY);
            view.setBackground(drawable);
        } catch (Exception e) {
            Log.d("BgColor Exception", e.toString());
        }
    }

    public static int getSideBarBgColor() {
        if (Singleton.getInstance().getFairData() != null)
            return Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor());
        else return Color.parseColor("#ffffff");
    }

    public static Keywords getKeywords() {
        if (Singleton.getInstance().getFairData() != null) {
            return Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords();
        } else return null;
    }

    public static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            if (context != null)
                sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void setBackgroundColor(View view) {
        try {
            view.setBackgroundColor(Singleton.getSideBarBgColor());
        } catch (Exception e) {
        }
    }

    public static Button getEnterButton() {
        return enterButton;
    }

    public static void setEnterButton(Button enterButton) {
        Singleton.enterButton = enterButton;
    }

    public static NotificationsFragment getNotiifactions() {
        return notificationsFragment;
    }

    public static void setNotiifactions(NotificationsFragment notificationsFragment) {
        Singleton.notificationsFragment = notificationsFragment;
    }

    public static Fair getFair() {
        if (Singleton.getInstance().getFairData() != null) {
            return Singleton.getInstance().getFairData().getFair();
        } else return null;
    }

    public static Options getOptions() {
      return Singleton.getInstance().getFairData().getFair().getOptions();
    }


    public Boolean getStageWebinars() {
        return stageWebinars;
    }

    public void setStageWebinars(Boolean stageWebinars) {
        this.stageWebinars = stageWebinars;
    }

    public com.vrd.gsaf.api.responses.exitPoll.Data getExitPollData() {
        return exitPollData;
    }

    public void setExitPollData(com.vrd.gsaf.api.responses.exitPoll.Data exitPollData) {
        this.exitPollData = exitPollData;
    }

    public com.vrd.gsaf.api.responses.standPoll.Data getStandPollData() {
        return standPollData;
    }

    public void setStandPollData(com.vrd.gsaf.api.responses.standPoll.Data standPollData) {
        this.standPollData = standPollData;
    }

    public ArrayList<YouTubePlayer> getYoutubePLayers() {
        return youtubePLayers;
    }

    public void setYoutubePLayers(YouTubePlayer youtubePLayers) {
        this.youtubePLayers.add(youtubePLayers);
    }

    public boolean isPdfTasks() {
        return pdfTasks;
    }

    public void setPdfTasks(boolean pdfTasks) {
        this.pdfTasks = pdfTasks;
    }

    public boolean isPdfResume() {
        return pdfResume;
    }

    public void setPdfResume(boolean pdfResume) {
        this.pdfResume = pdfResume;
    }

    public boolean isPdfProfile() {
        return pdfProfile;
    }

    public void setPdfProfile(boolean pdfProfile) {
        this.pdfProfile = pdfProfile;
    }

    public boolean isRegistration() {
        return isRegistration;
    }

    public void setRegistration(boolean registration) {
        isRegistration = registration;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public HttpProxyCacheServer getProxy() {
        return proxy;
    }

    public void setProxy(HttpProxyCacheServer proxy) {
        this.proxy = proxy;
    }

    public MainApp getApp() {
        return app;
    }

    public void setApp(MainApp app) {
        this.app = app;
    }

    public int getSelectedCareerOptions() {
        return selectedCareerOptions;
    }

    public void setSelectedCareerOptions(int selectedCareerOptions) {
        this.selectedCareerOptions = selectedCareerOptions;
    }

    public boolean isFromSideStands() {
        return fromSideStands;
    }

    public void setFromSideStands(boolean fromSideStands) {
        this.fromSideStands = fromSideStands;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Boolean getPlaying() {
        return isPlaying;
    }

    public void setPlaying(Boolean playing) {
        isPlaying = playing;
    }

    public int getSeekTo() {
        return seekTo;
    }

    public void setSeekTo(int seekTo) {
        this.seekTo = seekTo;
    }

    public ArrayList<MediaModel> getMediaList() {
        return mediaList;
    }

    public void setMediaList(MediaModel mediaList) {
        this.mediaList.add(mediaList);
    }

    public Boolean getLoginFragmentFlag() {
        return loginFragmentFlag;
    }

    public void setLoginFragmentFlag(Boolean loginFragmentFlag) {
        this.loginFragmentFlag = loginFragmentFlag;
    }

    public FairData getExtraFieldsFairData() {
        return extraFieldsFairData;
    }

    public void setExtraFieldsFairData(FairData extraFieldsFairData) {
        this.extraFieldsFairData = extraFieldsFairData;
    }

    public ArrayList<HallsDatum> getFairHalls() {
        return fairHalls;
    }

    public void setFairHalls(ArrayList<HallsDatum> fairHalls) {
        this.fairHalls = fairHalls;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public com.vrd.gsaf.api.responses.login.Data getLoginData() {
        return loginData;
    }

    public void setLoginData(com.vrd.gsaf.api.responses.login.Data loginData) {
        this.loginData = loginData;
    }

    public Data getFairData() {
        return fairData;
    }

    public void setFairData(Data fairData) {
        this.fairData = fairData;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public ChipNavigationBar getBottomNavigationView() {
        return bottomNavigationView;
    }

    public void setBottomNavigationView(ChipNavigationBar bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    public Boolean getViewPager() {
        return isViewPager;
    }

    public void setViewPager(Boolean viewPager) {
        isViewPager = viewPager;
    }

    public Boolean getExit() {
        return isExit;
    }

    public void setExit(Boolean exit) {
        isExit = exit;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public String getHomeState() {
        return homeState;
    }

    public void setHomeState(String homeState) {
        this.homeState = homeState;
    }

    public int getFairId() {
        return fairId;
    }

    public void setFairId(int fairId) {
        this.fairId = fairId;
    }

    public Fairs getFairs() {
        return fairs;
    }

    public void setFairs(Fairs fairs) {
        this.fairs = fairs;
    }

    public Boolean getIsHome() {
        return isHome;
    }

    public void setIsHome(Boolean isHome) {
        this.isHome = isHome;
    }

    public Boolean getIsDashboard() {
        return isDashboard;
    }

    public void setIsDashboard(Boolean isDashboard) {
        this.isDashboard = isDashboard;
    }

    public ArrayList<NotificationModel> getNotificationData() {
        return notificationData;
    }

    public void setNotificationDataData(NotificationModel notificationModel) {
        this.notificationData.add(notificationModel);
    }

    public ArrayList<DashBoardModel> getDashboardData() {
        return dashboardData;
    }

    public void setDashboardData(DashBoardModel dashboardData) {
        this.dashboardData.add(dashboardData);
    }

    public ArrayList<Speaker> getSpeakerData() {
        return speakerData;
    }

    public void setSpeakerData(List<Speaker> speakerData) {
        this.speakerData = (ArrayList<Speaker>) speakerData;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        Singleton.context = context;
    }

    public ApiInterface getService() {
        return service;
    }

    public Boolean getIsLoginRequired() {
        return isLoginRequired;
    }

    public void setIsLoginRequired(Boolean isLoginRequired) {
        this.isLoginRequired = isLoginRequired;
    }

    public Boolean getSplashShown() {
        return splashShown;
    }

    public void setSplashShown(Boolean splashShown) {
        this.splashShown = splashShown;
    }

    public ArrayList<String> getButtonsList() {
        return buttonsList;
    }

    public void setButtonsList(ArrayList<String> buttonsList) {
        this.buttonsList = buttonsList;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public ArrayList<Company> getCompaniesData() {
        return companiesData;
    }

    public void setCompaniesData(ArrayList<Company> companiesData) {
        this.companiesData = companiesData;
    }

    public List<Job> getJobsData() {
        return jobsData;
    }

    public void setJobsData(List<Job> jobsData) {
        this.jobsData = jobsData;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public View getHomeView() {
        return homeView;
    }

    public void setHomeView(View homeView) {
        this.homeView = homeView;
    }

    public int getStandCompanyId() {
        return standCompanyId;
    }

    public void setStandCompanyId(int standCompanyId) {
        this.standCompanyId = standCompanyId;
    }

    public ArrayList<Job> getCoursesData() {
        return coursesData;
    }

    public void setCoursesData(ArrayList<Job> coursesData) {
        this.coursesData = coursesData;
    }

    public ArrayList<Webinar> getWebinarList() {
        return webinarList;
    }

    public void setWebinarList(List<Webinar> webinarList) {
        this.webinarList = (ArrayList<Webinar>) webinarList;
    }

    public RelativeLayout getFloatingButton() {
        return floatingButton;
    }

    public void setFloatingButton(RelativeLayout floatingButton) {
        this.floatingButton = floatingButton;
    }

    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public ArrayList<Question> getCareerTest() {
        return careerTest;
    }

    public void setCareerTest(ArrayList<Question> careerTest) {
        this.careerTest = careerTest;
    }

    public JsonObject getCareerTestOptions() {
        return careerTestOptions;
    }

    public void setCareerTestOptions(JsonObject careerTestOptions) {
        this.careerTestOptions = careerTestOptions;
    }

    public Boolean getCareerTestGiven() {
        return careerTestGiven;
    }

    public void setCareerTestGiven(Boolean careerTestGiven) {
        this.careerTestGiven = careerTestGiven;
    }

    public int getLanguageIndex() {
        return languageIndex;
    }

    public void setLanguageIndex(int languageIndex) {
        this.languageIndex = languageIndex;
    }

    public ArrayList<Recruiter> getRecruiterArrayList() {
        return recruiterArrayList;
    }

    public void setRecruiterArrayList(ArrayList<Recruiter> recruiterArrayList) {
        this.recruiterArrayList = recruiterArrayList;
    }

    public ArrayList<GoodieBag> getCandidateGoodieBagList() {
        return candidateGoodieBagList;
    }

    public void setCandidateGoodieBagList(ArrayList<GoodieBag> candidateGoodieBagList) {
        this.candidateGoodieBagList = candidateGoodieBagList;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public void setNavigationView(NavigationView navigationView) {
        this.navigationView = navigationView;
    }

    public ArrayList<InterviewSlot> getSchedulesList() {
        return schedulesList;
    }

    public void setSchedulesList(ArrayList<InterviewSlot> schedulesList) {
        this.schedulesList = schedulesList;
    }

    public JsonObject getFilter() {
        return filter;
    }

    public void setFilter(JsonObject filter) {
        this.filter = filter;
    }

    public Boolean getApplyFilter() {
        return applyFilter;
    }

    public void setApplyFilter(Boolean applyFilter) {
        this.applyFilter = applyFilter;
    }

    public Boolean getLive() {
        return isLive;
    }

    public void setLive(Boolean live) {
        isLive = live;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public Boolean getActivityRunning() {
        return activityRunning;
    }

    public void setActivityRunning(Boolean activityRunning) {
        this.activityRunning = activityRunning;
    }

    public Integer getHallId() {
        return hallId;
    }

    public void setHallId(Integer hallId) {
        this.hallId = hallId;
    }

    public ArrayList<com.vrd.gsaf.api.responses.stands.Company> getStandsDataAgainstHall() {
        return standsDataAgainstHall;
    }

    public void setStandsDataAgainstHall(ArrayList<com.vrd.gsaf.api.responses.stands.Company> standsDataAgainstHall) {
        this.standsDataAgainstHall = standsDataAgainstHall;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public int getStandIndex() {
        return standIndex;
    }

    public void setStandIndex(int standIndex) {
        this.standIndex = standIndex;
    }

    public CompanyDetails getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(CompanyDetails companyDetails) {
        this.companyDetails = companyDetails;
    }

    public com.vrd.gsaf.api.responses.compnayDetail.Data getCompanyMedia() {
        return companyMedia;
    }

    public void setCompanyMedia(com.vrd.gsaf.api.responses.compnayDetail.Data companyMedia) {
        this.companyMedia = companyMedia;
    }

    public com.vrd.gsaf.api.responses.fairMedia.Data getFairMedia() {
        return fairMedia;
    }

    public void setFairMedia(com.vrd.gsaf.api.responses.fairMedia.Data fairMedia) {
        this.fairMedia = fairMedia;
    }

    public int getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(int speakerId) {
        this.speakerId = speakerId;
    }

    public com.vrd.gsaf.api.responses.receptionist.Data getReceptionistDetails() {
        return receptionistDetails;
    }

    public void setReceptionistDetails(com.vrd.gsaf.api.responses.receptionist.Data receptionistDetails) {
        this.receptionistDetails = receptionistDetails;
    }

    public Boolean getFromStandToWebinar() {
        return fromStandToWebinar;
    }

    public void setFromStandToWebinar(Boolean fromStandToWebinar) {
        this.fromStandToWebinar = fromStandToWebinar;
    }

    public Boolean getFromStandToMatchingSlots() {
        return fromStandToMatchingSlots;
    }

    public void setFromStandToMatchingSlots(Boolean fromStandToMatchingSlots) {
        this.fromStandToMatchingSlots = fromStandToMatchingSlots;
    }

    public Boolean getNewRegistration() {
        return newRegistration;
    }

    public void setNewRegistration(Boolean newRegistration) {
        this.newRegistration = newRegistration;
    }

    public JobAdapter getJobAdapter() {
        return adapter;
    }

    public void setJobAdapter(JobAdapter adapter) {
        this.adapter = adapter;
    }

    public Boolean getFromSpeakersToWebinar() {
        return fromSpeakersToWebinar;
    }

    public void setFromSpeakersToWebinar(Boolean fromSpeakersToWebinar) {
        this.fromSpeakersToWebinar = fromSpeakersToWebinar;
    }

    public ArrayList<Webinar> getStageWebinarList() {
        return stageWebinarList;
    }

    public void setStageWebinarList(ArrayList<Webinar> stageWebinarList) {
        this.stageWebinarList = stageWebinarList;
    }

    public Boolean getLogout() {
        return logout;
    }

    public void setLogout(Boolean logout) {
        this.logout = logout;
    }

    public Boolean getFromCoursesToJobDetails() {
        return fromCoursesToJobDetails;
    }

    public void setFromCoursesToJobDetails(Boolean fromCoursesToJobDetails) {
        this.fromCoursesToJobDetails = fromCoursesToJobDetails;
    }

    public String getActiveHall() {
        return activeHall;
    }

    public void setActiveHall(String activeHall) {
        this.activeHall = activeHall;
    }

    public String getPreviousHomeState() {
        return previousHomeState;
    }

    public void setPreviousHomeState(String previousHomeState) {
        this.previousHomeState = previousHomeState;
    }

    public CoursesAdapter getCoursesAdapter() {
        return coursesAdapter;
    }

    public void setCoursesAdapter(CoursesAdapter coursesAdapter) {
        this.coursesAdapter = coursesAdapter;
    }

    public Boolean getFromCompaniesToStand() {
        return fromCompaniesToStand;
    }

    public void setFromCompaniesToStand(Boolean fromCompaniesToStand) {
        this.fromCompaniesToStand = fromCompaniesToStand;
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public String getLanguage() {
        if (Singleton.getFair() != null && language == "109") {
            language = Singleton.getFair().getFairLanguages().get(languageIndex).getAutoId();
            return language;
        } else return language;
    }

    public void setLanguage(String language) {
        Singleton.language = language;
    }

    public Boolean getWebinarVideo() {
        return webinarVideo;
    }

    public void setWebinarVideo(Boolean webinarVideo) {
        this.webinarVideo = webinarVideo;
    }

    public Detail getWebinarDetail() {
        return webinarDetail;
    }

    public void setWebinarDetail(Detail webinarDetail) {
        this.webinarDetail = webinarDetail;
    }

    public List<com.vrd.gsaf.api.responses.goodies.GoodieBag> getGoodiesList() {
        return goodiesList;
    }

    public void setGoodiesList(List<com.vrd.gsaf.api.responses.goodies.GoodieBag> goodiesList) {
        this.goodiesList = goodiesList;
    }

    public Boolean getGoodies() {
        return goodies;
    }

    public void setGoodies(Boolean goodies) {
        this.goodies = goodies;
    }

    public Boolean getLiveWebinar() {
        return liveWebinar;
    }

    public void setLiveWebinar(Boolean liveWebinar) {
        this.liveWebinar = liveWebinar;
    }

    public LiveWebinar getLiveWebinarFragment() {
        return liveWebinarFragment;
    }

    public void setLiveWebinarFragment(LiveWebinar liveWebinarFragment) {
        this.liveWebinarFragment = liveWebinarFragment;
    }

    public int getGoodieCount() {
        return goodieCount;
    }

    public void setGoodieCount(int goodieCount) {
        this.goodieCount = goodieCount;
    }

    public String getLastPlayedVideo() {
        return lastPlayedVideo;
    }

    public void setLastPlayedVideo(String lastPlayedVideo) {
        this.lastPlayedVideo = lastPlayedVideo;
    }

    public String getSpeakerTitle() {
        return speakerTitle;
    }

    public void setSpeakerTitle(String speakerTitle) {
        this.speakerTitle = speakerTitle;
    }

//    public BubbleToggleView getNav_bottom_home() {
//        return nav_bottom_home;
//    }
//
//    public void setNav_bottom_home(BubbleToggleView nav_bottom_home) {
//        this.nav_bottom_home = nav_bottom_home;
//    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public ArrayList<HallModel> getHallModelArrayList() {
        return hallModelArrayList;
    }

    public void setHallModelArrayList(ArrayList<HallModel> hallModelArrayList) {
        this.hallModelArrayList = hallModelArrayList;
    }

    public Boolean getTouchEnabled() {
        return isTouchEnabled;
    }

    public void setTouchEnabled(Boolean touchEnabled) {
        isTouchEnabled = touchEnabled;
    }

    public Boolean getSuperBackPressed() {
        return isSuperBackPressed;
    }

    public void setSuperBackPressed(Boolean superBackPressed) {
        isSuperBackPressed = superBackPressed;
    }

    public Boolean getBackToReception() {
        return backToReception;
    }

    public void setBackToReception(Boolean backToReception) {
        this.backToReception = backToReception;
    }

    public Boolean getExhibitorsClicked() {
        return exhibitorsClicked;
    }

    public void setExhibitorsClicked(Boolean exhibitorsClicked) {
        this.exhibitorsClicked = exhibitorsClicked;
    }

    public Boolean getWebinarToHall() {
        return isWebinarToHall;
    }

    public void setWebinarToHall(Boolean webinarToHall) {
        isWebinarToHall = webinarToHall;
    }

    public Boolean getWebinarToReception() {
        return isWebinarToReception;
    }

    public void setWebinarToReception(Boolean webinarToReception) {
        isWebinarToReception = webinarToReception;
    }

    public boolean getRemoveJobFrag() {
        return this.removeJobFrag;
    }

    public void setRemoveJobFrag(boolean b) {
        this.removeJobFrag = true;
    }

    public boolean getIsLiveWebinar() {
        return this.isLiveWebinar;
    }

    public void setIsLiveWebinar(boolean b) {
        isLiveWebinar = b;
    }

    public Webinar getCurrentWebinar() {
        return this.webinar;
    }

    public void setCurrentWebinar(Webinar webinar) {
        this.webinar = webinar;
    }
}
