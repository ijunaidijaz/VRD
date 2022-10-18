package com.vrd.gsaf.home.stageConferenceAgenda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.jobPercentage.JobsMatchPercentage;
import com.vrd.gsaf.api.responses.jobPercentage.Match;
import com.vrd.gsaf.api.responses.webinars.Webinar;
import com.vrd.gsaf.api.responses.webinars.Webinars;
import com.vrd.gsaf.callbacks.ConferenceAgendaCallback;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.conferenceAgenda.ConferenceAdapter;
import com.vrd.gsaf.home.conferenceAgenda.ConferenceModel;
import com.vrd.gsaf.home.jobs.JobMatchAdapter;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StageConferenceAgenda extends Fragment implements View.OnClickListener, ConferenceAdapter.ConferenceAdapterListener, ConferenceAgendaCallback {

    private final Calendar myCalendar = Calendar.getInstance();
    private final int limit = 10;
    String index;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    boolean isFromSideBar = false;
    private View view;
    private ProgressBar progressBar;
    private ImageView menuImageView, timeReset, dateReset, backImageView;
    private TextView titleTxt;
    private RecyclerView recyclerView;
    private ConferenceAdapter adapter;
    private EditText searchEdt;
    private TextView dateTxt;
    private TextView timeText;
    LinearLayout linearLayout;
    private long mLastClickTime = 0;
    private ShimmerFrameLayout shimmerFrameLayout;
    private int offset = 0;
    ImageView searchImageView;
    private boolean isLoading = false;

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
        Helper.lockOrientation();

        view = inflater.inflate(R.layout.fragment_stage_conference_agends, container, false);
        Helper.hideKeyboard();
        Bundle bundle = this.getArguments();
        try {
            if (bundle != null) {
                index = bundle.get("index").toString();
            }
        } catch (Exception e) {

        }

        isFromSideBar = getArguments() != null && getArguments().containsKey("isFromSideBar");


        offset = 0;
        Singleton.getInstance().setStageWebinars(true);
        Singleton.getInstance().getStageWebinarList().clear();
        initializeViews();


        //   Singleton.getInstance().getCompaniesData().clear();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        backImageView = view.findViewById(R.id.backImageView);
        titleTxt = view.findViewById(R.id.titleTxt);
        linearLayout = view.findViewById(R.id.linearLayout);
        progressBar = view.findViewById(R.id.progressBar);
        menuImageView = view.findViewById(R.id.menuImageView);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchEdt = view.findViewById(R.id.searchJobTitleEdt);
        searchImageView = view.findViewById(R.id.searchImageView);
        //   searchEdt.setHint("required");
        dateTxt = view.findViewById(R.id.endDateEdt);
        dateTxt.setText(Objects.requireNonNull(Singleton.getKeywords()).getEnd_date());
        searchEdt.setHint(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSearchBy() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaWebinar() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTitle());
        timeText = view.findViewById(R.id.startDateEdt);
        timeText.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStartTime());
        timeReset = view.findViewById(R.id.timeReset);
        dateReset = view.findViewById(R.id.dateReset);
        dateReset.setVisibility(View.GONE);
        timeReset.setVisibility(View.GONE);
//        if (Singleton.getInstance().getIsDashboard()) {
//            menuImageView.setVisibility(View.GONE);
//            menuImageView.setEnabled(false);
//            backImageView.setVisibility(View.VISIBLE);
//        }

//        if (Singleton.getInstance().getFromSpeakersToWebinar())
//            titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSpeakers() + " " + Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaWebinars());
//        else if (Singleton.getInstance().getIsDashboard())
//            titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaWebinars());
//        else if (Singleton.getInstance().getStageWebinars())
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStageConferenceAgenda());
//        else
//            titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getConferenceAgenda());

        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
        if (getArguments() != null && getArguments().containsKey("isFromButtons")) {
            hideAllFilters();
        }
        setClickListeners();
    }

    private void hideAllFilters() {
        searchEdt.setVisibility(View.GONE);
        timeText.setVisibility(View.GONE);
        dateTxt.setVisibility(View.GONE);
        searchImageView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

    }
    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        menuImageView.setOnClickListener(this);
        timeText.setOnClickListener(this);
        dateTxt.setOnClickListener(this);
        dateReset.setOnClickListener(this);
        timeReset.setOnClickListener(this);

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // adapter.getFilter().filter(charSequence);
                callFilter(searchEdt.getText().toString(), timeText.getText().toString(), dateTxt.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //adapter.getFilter().filter();
            }
        });

        if (Helper.isInternetConnected()) {
            shimmerFrameLayout.startShimmer();

//            if (Singleton.getInstance().getIsDashboard())
//                callApiForMatchingWebinars(false);
//            else
//                callApiForWebinars(false);
            if (isFromSideBar) {
                callApiForWebinars(false);
            } else if (Singleton.getInstance().getIsLoggedIn()) {
                callApiForMatchingWebinars(false);
            } else callApiForWebinars(false);

//
//            if (Singleton.getInstance().getIsLoggedIn())
//                callApiForMatchingWebinars(false);
//            else
//                callApiForWebinars(false);

        }

        retryLayout();
    }

    private void retryLayout() {
        View retryLayout;
        retryLayout = view.findViewById(R.id.somethingWentWrongLayout);
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.isInternetConnected()) {
//                    if (Singleton.getInstance().getIsDashboard())
//                        callApiForMatchingWebinars(false);
//                    else
//                        callApiForWebinars(false);
                    if (Singleton.getInstance().getIsLoggedIn())
                        callApiForMatchingWebinars(false);
                    else
                        callApiForWebinars(false);
                }
            }
        });
    }

    private void callApiForMatchingWebinars(boolean pagination) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getSharedPreferences().getInt("fairId", 0));
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("agenda_type", 2);
        // JsonObject.addProperty("timezone", TimeZone.getDefault().getID());
        JsonObject.addProperty("limit", limit);
        JsonObject.addProperty("offset", offset);
        if (Singleton.getInstance().getFromStandToWebinar()) {
            //  JsonObject.addProperty("company_id", Singleton.getInstance().getSpeakerData().get(Integer.valueOf(index)).getId());
            JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());
            // Singleton.getInstance().setSpeakerId(null);
        } else if (Singleton.getInstance().getFromSpeakersToWebinar()) {
            //  JsonObject.addProperty("company_id", Singleton.getInstance().getSpeakerData().get(Integer.valueOf(index)).getId());
            JsonObject.addProperty("speaker_id", Singleton.getInstance().getSpeakerId());
            //Singleton.getInstance().setSpeakerId(null);
        }

        Call<Webinars> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getMatchingWebinars(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<Webinars>() {
            @Override
            public void onResponse(Call<Webinars> call, Response<Webinars> response) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.hideProgressBar(progressBar);
                Helper.hideLayouts(getView());

                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        if (Singleton.getInstance().getStageWebinarList().size() == 0)
                            Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        if (!pagination && response.body().getData().getWebinarList().size() < 1) {
                            Helper.noDataFound(view);
                        } else {
                            parseResponse(pagination, response);
                        }

                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }


            }

            @Override
            public void onFailure(Call<Webinars> call, Throwable t) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.failureResponse(progressBar, getView());

            }

        });

    }

    private void callApiForWebinars(boolean pagination) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getSharedPreferences().getInt("fairId", 0));
        JsonObject.addProperty("timezone", TimeZone.getDefault().getID());
        JsonObject.addProperty("agenda_type", 2);


        Call<Webinars> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getWebinars(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<Webinars>() {
            @Override
            public void onResponse(Call<Webinars> call, Response<Webinars> response) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.hideProgressBar(progressBar);
                Helper.hideLayouts(getView());

                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        if (!pagination)
                            Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {

                        if (!pagination && response.body().getData().getWebinarList().size() < 1) {
                            Helper.noDataFound(view);
                        } else {
                            parseResponse(pagination, response);
                        }


                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<Webinars> call, Throwable t) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.failureResponse(progressBar, getView());

            }

        });

    }

    private void parseResponse(boolean pagination, Response<Webinars> response) {

        try {
            if (response.body().getStatus()) {
                for (int i = 0; i < response.body().getData().getWebinarList().size(); i++) {
                    Webinar webinar = response.body().getData().getWebinarList().get(i);
                    if (!Singleton.getInstance().getIsDashboard()) {
                        if ( Singleton.getInstance().getStageWebinarList().stream().noneMatch(o -> o.getWebinarId().equals(webinar.getWebinarId())))
                            Singleton.getInstance().getStageWebinarList().add(response.body().getData().getWebinarList().get(i));
                    } else {
                        if (Singleton.getInstance().getStageWebinarList().stream().noneMatch(o -> o.getWebinarId().equals(webinar.getWebinarId()))) {
                            Singleton.getInstance().getStageWebinarList().add(response.body().getData().getWebinarList().get(i));
                        }
                    }
                }
                if (pagination) {
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(offset - 1);
                } else {
                    manageRecyclerView();
                }
                offset = limit + offset;
                Helper.hideProgressBar(progressBar);
                if (response.body().getData().getWebinarList().size() > 9)
                    isLoading = false;
//                Helper.showToast(String.valueOf(Singleton.getInstance().getStageWebinarList().size()));

            } else
                Helper.showToast("Something went wrong...Please try later!");
        } catch (Exception e) {
            Helper.showToast("Something Went Wrong");
        }


        if (Singleton.getInstance().getStageWebinarList().size() < 1) {
            Helper.noDataFound(view);

        }
    }

    private void manageRecyclerView() {

        adapter = new ConferenceAdapter(progressBar, Singleton.getInstance().getContext(), getParentFragmentManager(), Singleton.getInstance().getStageWebinarList(), this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));

        recyclerView.setAdapter(adapter);
        initScrollListener();

    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == Singleton.getInstance().getJobsData().size() - 1) {
                        //bottom of list!
                        if (Helper.isInternetConnected()) {
//                            if (Singleton.getInstance().getIsDashboard())
//                                callApiForMatchingWebinars(true);
//                            else
//                                callApiForWebinars(true);
                            //    if (view.getRootView().isActivated()) {
                            if (Singleton.getInstance().getIsLoggedIn())
                                callApiForMatchingWebinars(true);
                            else
                                callApiForWebinars(true);
                            //  }
                            //  }
//                        else
//                            callAPiForJobsList(true);
                            isLoading = true;
                        }
                    }
                }
            }
        });
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
        getActivity().getSupportFragmentManager().popBackStack();
    }


    @Override
    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        switch (view.getId()) {
            case R.id.menuImageView:
                Helper.menuClick();
                break;
            case R.id.endDateEdt:
                openCalender();
                break;
            case R.id.startDateEdt:
                openTimePicker();
                break;
            case R.id.timeReset:
                callFilter(searchEdt.getText().toString(), "", dateTxt.getText().toString());
                timeReset.setVisibility(View.GONE);
                timeText.setText("");
                break;
            case R.id.backImageView:
                onBackPress();
                break;
            case R.id.dateReset:
                callFilter(searchEdt.getText().toString(), timeText.getText().toString(), "");
                dateReset.setVisibility(View.GONE);
                dateTxt.setText("");
                break;


        }
    }

    private void openTimePicker() {
        new SingleDateAndTimePickerDialog.Builder(getActivity())
                .bottomSheet()
                .curved()
                .displayHours(true)
                .displayMinutes(true)
                .todayText("Today")
                .title(Objects.requireNonNull(Singleton.getKeywords()).getStartAt())
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        // Retrieve the SingleDateAndTimePicker
                    }

                })
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        updateStartDateTextView(calendar);
                    }
                }).display();
    }

    private void openCalender() {
        new SingleDateAndTimePickerDialog.Builder(getActivity())
                .bottomSheet()
                .curved()
                .displayHours(true)
                .displayMinutes(true)
                .todayText("Today")
                .title(Objects.requireNonNull(Singleton.getKeywords()).getEnd_date())
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        // Retrieve the SingleDateAndTimePicker
                    }

                })
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        myCalendar.setTime(date);
                        updateDateTextView();
                    }
                }).display();
    }

    private void updateDateTextView() {
        // SimpleDateFormat parseFormat = new SimpleDateFormat("E MMMM dd,yyyy hh:mm a");

        String myFormat = "E, MMMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateTxt.setText(sdf.format(myCalendar.getTime()));
        dateReset.setVisibility(View.VISIBLE);
        callFilter(searchEdt.getText().toString(), timeText.getText().toString(), dateTxt.getText().toString());
        //  adapter.getFilter().filter(sdf.format(myCalendar.getTime()) + " " + timeText.getText());
    }
    private void updateStartDateTextView(Calendar calendar) {
        // SimpleDateFormat parseFormat = new SimpleDateFormat("E MMMM dd,yyyy hh:mm a");

        String myFormat = "E, MMMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

//        dateTxt.setText(sdf.format(myCalendar.getTime()));
//        dateReset.setVisibility(View.VISIBLE);
//        callFilter(searchEdt.getText().toString(), timeText.getText().toString(), dateTxt.getText().toString());

        timeText.setText(sdf.format(calendar.getTime()));
        // adapter.getFilter().filter(selectedHour + ":" + selectedMinute);
        callFilter(searchEdt.getText().toString(), timeText.getText().toString(), dateTxt.getText().toString());

        timeReset.setVisibility(View.VISIBLE);
        //  adapter.getFilter().filter(sdf.format(myCalendar.getTime()) + " " + timeText.getText());
    }
    private void callFilter(String title, String time, String date) {

        try {
            if (title.equals(""))
                title = " ";
            if (time.equals(""))
                time = " ";
            if (date.equals(""))
                date = " ";

            String characters = title + "," + time + "," + date;
            adapter.getFilter().filter(characters);
//            Helper.showToast("eads");
        } catch (Exception e) {

        }

    }

    @Override
    public void onTitleSelected(ConferenceModel contact) {

    }

    @Override
    public void matchPercentage(int JobId) {
        callApiForMatchPercentage(JobId);
    }

    public void callApiForMatchPercentage(int jobId) {

        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("match_param", "webinar");
        JsonObject.addProperty("job_id", jobId);


        Call<JobsMatchPercentage> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getJobPercentage(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<JobsMatchPercentage>() {
            @Override
            public void onResponse(Call<JobsMatchPercentage> call, Response<JobsMatchPercentage> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {
                        Helper.showToast("No Data Found");

                    } else if (response.body().getStatus()) {
                        showMatchDialog(response.body().getData().getMatchList());
                    }
                } catch (Exception e) {
                    Helper.showToast("Something Went Wrong");

                }

            }

            @Override
            public void onFailure(Call<JobsMatchPercentage> call, Throwable t) {
                Helper.showToast("Something Went Wrong");
                Helper.hideProgressBar(progressBar);
            }

        });

    }

    private void showMatchDialog(List<Match> matchList) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_job_matchpercentsge, null);
        RecyclerView recyclerView;
        JobMatchAdapter adapter;
        dialogBuilder.setView(dialogView);
        dialogView.requestLayout();
        AlertDialog alertDialog = dialogBuilder.create();
        //    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textView = dialogView.findViewById(R.id.titleTxt);
        textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMatch() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDetail());
        //textView.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAre());


        adapter = new JobMatchAdapter(matchList);
        recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ImageView crossImageView = dialogView.findViewById(R.id.crossButtonsLayoutImageView);

        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                alertDialog.cancel();


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
}