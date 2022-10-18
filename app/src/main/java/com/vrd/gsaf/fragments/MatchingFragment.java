package com.vrd.gsaf.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.SlotsResponse;
import com.vrd.gsaf.api.responses.interviewDates.Data;
import com.vrd.gsaf.api.responses.interviewDates.InterviewDates;
import com.vrd.gsaf.api.responses.interviewSlots.Slot;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.dashboard.recruiters.interviewRequest.InterviewRequestAdapter;
import com.vrd.gsaf.home.dashboard.schedules.Schedules;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.DateTime;
import com.vrd.gsaf.utility.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;


public class MatchingFragment extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    DateData previousDate;
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    private View thisView;
    private ProgressBar progressBar;
    private ImageView backImageView;
    private RecyclerView recyclerView;
    private InterviewRequestAdapter adapter;
    //private CalendarView calendarView;
    private long mLastClickTime;
    private int index;
    private LinearLayout linearLayout;
    private TextView availableSlotsValueTxt;
    private MCalendarView mCalenderView;
    private Data list = new Data();

    public static long getTimeInMillis(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        Helper.lockOrientation();

        thisView = inflater.inflate(R.layout.fragment_matching, container, false);

        index = this.getArguments().getInt("index");


        Helper.hideKeyboard();
        initializeViews();
        callApiForInterviewSlots(DateTime.getSimpleCurrentDate());
        return thisView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("CutPasteId")
    private void initializeViews() {
        progressBar = thisView.findViewById(R.id.progressBar);
        availableSlotsValueTxt = thisView.findViewById(R.id.availableSlotsValueTxt);
        linearLayout = thisView.findViewById(R.id.linear);
        backImageView = thisView.findViewById(R.id.backImageView);
        backImageView.setOnClickListener(this);
        recyclerView = thisView.findViewById(R.id.recyclerView);
        // calendarView = thisView.findViewById(R.id.simpleCalendarView);
        mCalenderView = thisView.findViewById(R.id.mCalenderView);
        availableSlotsValueTxt.setText(DateTime.getSimpleCurrentDate());
        // mCalenderView.setMarkedStyle(MarkStyle.BACKGROUND);


//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month,
//                                            int dayOfMonth) {
//                callApiForInterviewSlots(month + "/" + dayOfMonth + "/" + year % 100);
//                availableSlotsValueTxt.setText(dayOfMonth + "-" + month + "-" + year % 100);
//            }
//        });

        mCalenderView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                String dtStart = "" + date.getDay() + "-" + date.getMonth() + "-" + date.getYear();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date date1 = format.parse(dtStart);
                    Date currentDate = format.parse(DateTime.getSimpleCurrentDate());
                    assert date1 != null;
                    if (date1.equals(currentDate) || date1.after(currentDate)) {
                        List<String> dates = new ArrayList<>();
                        if (list.getHighlitedDates() != null) list.getHighlitedDates().clear();
                        list.setHighlitedDates(dates);
                        highlightDates(list, date);
                    } else Helper.showToast("You cannot select date before today");
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
        toolbar = thisView.findViewById(R.id.toolbar);
        mainLayout = thisView.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));


//        setClickListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        callApiForInterviewDates();
    }


    private void highlightDates(Data data, DateData selecetedDate) {
        if (previousDate != null) {
            mCalenderView.unMarkDate(previousDate.getYear(), previousDate.getMonth(), previousDate.getDay());
        }
        mCalenderView.markDate(selecetedDate.getYear(), selecetedDate.getMonth(), selecetedDate.getDay());
        previousDate = selecetedDate;
//        for(int i=0;i<data.getHighlitedDates().size();i++)
//        {
//            mCalenderView.markDate(Integer.parseInt(data.getHighlitedDates().get(i).split("/")[2])
//                    , Integer.parseInt(data.getHighlitedDates().get(i).split("/")[1])
//                    , Integer.parseInt(data.getHighlitedDates().get(i).split("/")[0]));
//
////            mCalenderView.markDate(
////                    new DateData(Integer.parseInt(data.getHighlitedDates().get(i).split("/")[2])
////                            , Integer.parseInt(data.getHighlitedDates().get(i).split("/")[1])
////                            , Integer.parseInt(data.getHighlitedDates().get(i).split("/")[0])).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.BLUE)));
//
//        }


        if (selecetedDate == null) {
//            selecetedDate = new DateData( Integer.parseInt(data.getHighlitedDates().get(0).split("/")[2]),
//                    Integer.parseInt(data.getHighlitedDates().get(0).split("/")[1]),
//                    Integer.parseInt(data.getHighlitedDates().get(0).split("/")[0]));
////            mCalenderView.unMarkDate(selecetedDate);
////            mCalenderView.markDate(selecetedDate.setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.GREEN)));

            callApiForInterviewSlots(data.getHighlitedDates().get(0));
            availableSlotsValueTxt.setText(data.getHighlitedDates().get(0));
        } else {

            String d = selecetedDate.getDayString() + "-" + selecetedDate.getMonthString() + "-" + selecetedDate.getYear();

//            mCalenderView.unMarkDate(selecetedDate);
//            mCalenderView.markDate(selecetedDate.setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.GREEN)));
            callApiForInterviewSlots(d);

//            callApiForInterviewSlots(d);
            availableSlotsValueTxt.setText(d);

        }


    }

    private void manageCalender2(Data list) {


        int s = list.getHighlitedDates().size() - 1;
        int days = Integer.parseInt(list.getHighlitedDates().get(s).split("/")[1]);
        int months = Integer.parseInt(list.getHighlitedDates().get(s).split("/")[0]);
        int years = Integer.parseInt("20" + Integer.parseInt(list.getHighlitedDates().get(s).split("/")[2]));
        //   mCalenderView.setHig(getTimeInMillis(days, months, years));

        int day = Integer.parseInt(list.getHighlitedDates().get(0).split("/")[1]);
        int month = Integer.parseInt(list.getHighlitedDates().get(0).split("/")[0]);
        int year = Integer.parseInt("20" + Integer.parseInt(list.getHighlitedDates().get(0).split("/")[2]));
        //  mCalenderView.setMinDate(getTimeInMillis(day, month, year));


    }

    private void callApiForInterviewDates() {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        //JsonObject.addProperty("candidate_id", "219978");
        JsonObject.addProperty("recruiter_id", Singleton.getInstance().getRecruiterArrayList().get(index).getRecruiterId());
        //JsonObject.addProperty("recruiter_id", "70829");
        Call<InterviewDates> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).interviewDates(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<InterviewDates>() {
            @Override
            public void onResponse(Call<InterviewDates> call, Response<InterviewDates> response) {
                Helper.hideProgressBar(progressBar);
                Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 409) {
                        openAlreadyBookedDialog(getParentFragmentManager());
                    } else if (response.raw().code() == 204) {
                        Helper.noDataFound(thisView);
                        linearLayout.setVisibility(View.GONE);
                    } else if (response.body().getMsg().equals("You have already submitted meeting request to this host.")) {
                        Helper.noDataFound(thisView);

                    } else if (response.body().getStatus()) {
                        // manageCalender(response.body().getData());
                        //   manageCalender2(response.body().getData());
                        list = response.body().getData();

                        highlightDates(response.body().getData(), null);
                        mCalenderView.setBackgroundColor(Color.WHITE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            mCalenderView.setOutlineAmbientShadowColor(Color.RED);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            mCalenderView.setForceDarkAllowed(true);
                        }


                        mCalenderView.travelTo(new DateData(
                                Integer.parseInt(response.body().getData().getHighlitedDates().get(0).split("/")[2]),
                                Integer.parseInt(response.body().getData().getHighlitedDates().get(0).split("/")[1]),
                                Integer.parseInt(response.body().getData().getHighlitedDates().get(0).split("/")[0])));

                    } else {
                        Helper.somethingWentWrong(getView());
                        linearLayout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                    linearLayout.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<InterviewDates> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
                linearLayout.setVisibility(View.GONE);

            }


        });

    }


    private void callApiForInterviewSlots(String s) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("date", s);
        JsonObject.addProperty("timezone", TimeZone.getDefault().getID());
        if (Singleton.getInstance().getFromStandToMatchingSlots()) {
            JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());
        }
        Call<SlotsResponse> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getSlots(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<SlotsResponse>() {
            @Override
            public void onResponse(Call<SlotsResponse> call, Response<SlotsResponse> response) {
                Helper.hideProgressBar(progressBar);
                Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {
                        Helper.showToast("No slots available against " + s);
                        //com.vrd.android.api.responses.interviewSlots.Data dataList=new com.vrd.android.api.responses.interviewSlots.Data();

                        try {
                            adapter.clear();
                        } catch (Exception e) {

                        }
                        //  manageRecyclerView(dataList);
                    } else if (response.body().getStatus()) {
                        manageRecyclerView(response.body());

                    }
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                    linearLayout.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<SlotsResponse> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
                linearLayout.setVisibility(View.GONE);

            }


        });

    }

    private void manageRecyclerView(SlotsResponse data) {
        List<Slot> slotList = data.getData().getSlotList();
        com.vrd.gsaf.api.responses.interviewSlots.Data data1 = new com.vrd.gsaf.api.responses.interviewSlots.Data();
        data1.setSlotList(slotList);
        adapter = new InterviewRequestAdapter(getParentFragmentManager(), data1, index, progressBar, true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        recyclerView.setAdapter(adapter);
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
                requireActivity().onBackPressed();
                break;

        }
    }


    private void openAlreadyBookedDialog(FragmentManager gview) {
        Boolean check = false;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_interview_already_booked, null);

//        TextView emailTxt = dialogView.findViewById(R.id.emailTxt);
//        emailTxt.setText("An Email was sent to " + Singleton.getInstance().getLoginData().getUser().getEmail());
        TextView schedulesText = dialogView.findViewById(R.id.recruiterNmae4Txt);
        SpannableString content = new SpannableString("Please click here to see your schedules");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        schedulesText.setText(content);

        schedulesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Singleton.getInstance().getMainActivity().replaceFragment(new Schedules(), null);

            }
        });
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        FragmentManager fm = Singleton.getInstance().getMainActivity().getSupportFragmentManager();

        schedulesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.popBackStackImmediate();
                fm.popBackStackImmediate();
                FragmentManager fm = gview;
                fm.beginTransaction().add(R.id.frameLayout, new Schedules())
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                alertDialog.cancel();


            }
        });

        ImageView cross = dialogView.findViewById(R.id.crossImageView);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                fm.popBackStackImmediate();


            }
        });

        alertDialog.setCanceledOnTouchOutside(false);


//        alertDialog.setOnCancelListener(dialogInterface -> {
//            onBackPress();
//
//        });
//        alertDialog.setOnDismissListener(dialogInterface -> {
//                if(!flag) {
//                    onBackPress();
//                }
//        });

    }

    @Override
    public void onDestroyView() {
        mCalenderView.unMarkDate(previousDate);
        super.onDestroyView();
    }
}