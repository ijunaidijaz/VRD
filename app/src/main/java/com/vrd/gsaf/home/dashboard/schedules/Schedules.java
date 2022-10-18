package com.vrd.gsaf.home.dashboard.schedules;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.schedules.InterviewSlot;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.conferenceAgenda.ConferenceAdapter;
import com.vrd.gsaf.home.conferenceAgenda.ConferenceModel;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Schedules extends Fragment implements View.OnClickListener, ConferenceAdapter.ConferenceAdapterListener {


    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    private final Calendar myCalendar = Calendar.getInstance();
    String status = "all";
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    private int limit = 10;
    private int offset = 0;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private EditText statusSearchEdt;
    private TextView timeText, dateTxt;
    private long mLastClickTime;
    private ShimmerFrameLayout shimmerFrameLayout;
    private boolean isLoading = false;
    private ImageView startDateReset, endDateReset, statusReset, statusDropDown;

    @Override
    public void onResume() {
        //playVideo();
        super.onResume();
        try {
            Helper.setActiveItemBottomNav(0);

        } catch (Exception e) {
            Helper.showToast("asds");
        }
        // playVideo();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_schedules, container, false);
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.progressBar);
        backImageView = view.findViewById(R.id.backImageView);
        recyclerView = view.findViewById(R.id.recyclerView);

        // dateEdt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getResendConfirmationEmail());
        // timeEdt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStartTime());
        // statusSearchEdt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStatus());
        startDateReset = view.findViewById(R.id.timeReset);
        dateTxt = view.findViewById(R.id.endDateEdt);
        statusSearchEdt = view.findViewById(R.id.statusSearchEdt);

        timeText = view.findViewById(R.id.startDateEdt);
        timeText.setText("Start Date");
        dateTxt.setText("End Date");
        startDateReset = view.findViewById(R.id.timeReset);
        endDateReset = view.findViewById(R.id.dateReset);
        statusReset = view.findViewById(R.id.statusReset);
        statusDropDown = view.findViewById(R.id.statusDropDown);

        endDateReset.setVisibility(View.GONE);
        startDateReset.setVisibility(View.GONE);
        statusReset.setVisibility(View.GONE);
        statusDropDown.setVisibility(View.VISIBLE);
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        setClickListeners();

    }

    private void openDropDown() {
        ArrayList<String> array = new ArrayList<>();
        array.add("All");
        array.add("Canceled");
        array.add("Pending");
        array.add("Booked");
        array.add("Completed");
        array.add("No-Show");


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);


        dialogView.requestLayout();
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView titleTxt = dialogView.findViewById(R.id.titleTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSchedules());

        alertDialog.show();

        //  EditText editText = dialog.findViewById(R.id.edtText);
        ListView listView = dialogView.findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Singleton.getInstance().getActivity(),
                android.R.layout.simple_list_item_1, array);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                offset = 0;
                limit = 10;
                callApiForSchedulesList(false, arrayAdapter.getItem(i).toLowerCase(), timeText.getText().toString(), dateTxt.getText().toString());
                //  callFilter(arrayAdapter.getItem(i), timeText.getText().toString(), dateTxt.getText().toString());
                statusSearchEdt.setText(arrayAdapter.getItem(i));
                status = arrayAdapter.getItem(i).toLowerCase();
                statusReset.setVisibility(View.VISIBLE);
                statusDropDown.setVisibility(View.GONE);
                alertDialog.dismiss();
            }

        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {

        backImageView.setOnClickListener(this);

        if (Helper.isInternetConnected()) {
            callApiForSchedulesList(false, status, timeText.getText().toString(), dateTxt.getText().toString());
            shimmerFrameLayout.startShimmer();
        }
        timeText.setOnClickListener(this);
        dateTxt.setOnClickListener(this);
        endDateReset.setOnClickListener(this);
        startDateReset.setOnClickListener(this);
        statusReset.setOnClickListener(this);

        statusSearchEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDropDown();

            }
        });
//        statusSearchEdt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                // adapter.getFilter().filter(charSequence);
//                //  callFilter(statusSearchEdt.getText().toString(), timeText.getText().toString(), dateTxt.getText().toString());
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                //adapter.getFilter().filter();
//            }
//        });

        retryLayout();

    }

    private void retryLayout() {
        View retryLayout;
        retryLayout = view.findViewById(R.id.somethingWentWrongLayout);
        TextView noDataTxt = retryLayout.findViewById(R.id.noDataTxt);
        Helper.setTextColor(noDataTxt);
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.isInternetConnected()) {
                    callApiForSchedulesList(true, status, timeText.getText().toString(), dateTxt.getText().toString());
                }
            }
        });
    }

    private void callApiForSchedulesList(boolean pagination, String stat, String startDate, String endDate) {

        if (startDate.equals("Start Date"))
            startDate = "";
        if (endDate.equals("End Date"))
            endDate = "";
        if (stat.equals(""))
            stat = "all";
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("status", stat);
        JsonObject.addProperty("request_by", "candidate");
        JsonObject.addProperty("limit", limit);
        JsonObject.addProperty("offset", offset);
        JsonObject.addProperty("start_date", startDate);
        JsonObject.addProperty("end_date", endDate);

        Call<com.vrd.gsaf.api.responses.schedules.Schedules> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getSchedules(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.schedules.Schedules>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.schedules.Schedules> call, Response<com.vrd.gsaf.api.responses.schedules.Schedules> response) {
                Helper.hideProgressBar(progressBar);
                Helper.endShimmer(shimmerFrameLayout);
                Helper.hideLayouts(getView());
                if (adapter != null && !pagination) {
                    adapter.clearData();
                }
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {
                        Helper.noDataFound(view);
                    } else if (response.isSuccessful() && response.body().getStatus()) {
                        if (!pagination && response.body().getData().getInterviewSlots().size() < 1) {
                            Helper.noDataFound(view);
                        } else {
                            Singleton.getInstance().setSchedulesList((ArrayList<InterviewSlot>) response.body().getData().getInterviewSlots());
                            manageRecyclerView();
                            Helper.hideProgressBar(progressBar);
                        }
                    } else {
                        Helper.getErrorMessage(response.errorBody());
                        Helper.somethingWentWrong(getView());
                    }
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.schedules.Schedules> call, Throwable t) {
                Helper.endShimmer(shimmerFrameLayout);
                Helper.failureResponse(progressBar, getView());

            }

        });

    }


    private void manageRecyclerView() {
        adapter = new ScheduleAdapter(getParentFragmentManager(), this, progressBar);
        //Singleton.getInstance().getSchedulesList().clear();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        recyclerView.setAdapter(adapter);
        initScrollListener();
        adapter.notifyDataSetChanged();
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
                            callApiForSchedulesList(true, status, timeText.getText().toString(), dateTxt.getText().toString());
                        }

                        isLoading = true;
                    }
                }
            }
        });
    }


    private void onBackPress() {
        getActivity().getSupportFragmentManager().popBackStack();
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
            // Helper.showToast("eads");
        } catch (Exception e) {

        }
    }

    private void openTimePicker() {
        // TODO Auto- method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(Singleton.getInstance().getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeText.setText(selectedHour + ":" + selectedMinute);
                callFilter(statusSearchEdt.getText().toString(), timeText.getText().toString(), dateTxt.getText().toString());

                startDateReset.setVisibility(View.VISIBLE);


            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void openCalender(String type) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto- method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTextView(type);
            }

        };

        Date c = Calendar.getInstance().getTime();

        DatePickerDialog datePicker = new DatePickerDialog(Singleton.getInstance().getContext(), R.style.DialogTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        //  datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePicker.show();
    }

    private void updateDateTextView(String type) {
        // SimpleDateFormat parseFormat = new SimpleDateFormat("E MMMM dd,yyyy hh:mm a");
        //   String myFormat = "E-MMMM-dd-yyyy"; //In which you need put here
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        offset = 0;
        limit = 10;
        if (type.equals("end")) {

            dateTxt.setText(sdf.format(myCalendar.getTime()));
            endDateReset.setVisibility(View.VISIBLE);
            //callFilter(statusSearchEdt.getText().toString(), timeText.getText().toString(), dateTxt.getText().toString());

        } else if (type.equals("start")) {
            timeText.setText(sdf.format(myCalendar.getTime()));
            startDateReset.setVisibility(View.VISIBLE);
            //  callFilter(statusSearchEdt.getText().toString(), timeText.getText().toString(), dateTxt.getText().toString());
        }

        callApiForSchedulesList(false, statusSearchEdt.getText().toString(), timeText.getText().toString(), dateTxt.getText().toString());


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
            case R.id.startDateEdt:
                // openTimePicker();
                openCalender("start");
                break;
            case R.id.timeReset:
                limit = 10;
                offset = 0;
                //  callFilter(statusSearchEdt.getText().toString(), "", dateTxt.getText().toString());
                startDateReset.setVisibility(View.GONE);
                timeText.setText("");
                callApiForSchedulesList(false, statusSearchEdt.getText().toString(), timeText.getText().toString(), dateTxt.getText().toString());

                break;
            case R.id.dateReset:
                limit = 10;
                offset = 0;
                //  callFilter(statusSearchEdt.getText().toString(), timeText.getText().toString(), "");
                endDateReset.setVisibility(View.GONE);
                dateTxt.setText("");
                callApiForSchedulesList(false, statusSearchEdt.getText().toString(), timeText.getText().toString(), dateTxt.getText().toString());
                break;
            case R.id.endDateEdt:
                openCalender("end");
                break;
            case R.id.statusReset:
                //callFilter("", timeText.getText().toString(), dateTxt.getText().toString());
                limit = 10;
                offset = 0;
                callApiForSchedulesList(false, "all", timeText.getText().toString(), dateTxt.getText().toString());
                statusSearchEdt.setText("All");
                statusReset.setVisibility(View.GONE);
                statusReset.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onTitleSelected(ConferenceModel contact) {

    }
}

