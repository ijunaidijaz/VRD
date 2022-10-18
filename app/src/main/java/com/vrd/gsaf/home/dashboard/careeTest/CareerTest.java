package com.vrd.gsaf.home.dashboard.careeTest;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.careerTest.Answer;
import com.vrd.gsaf.api.responses.careerTest.Question;
import com.vrd.gsaf.api.responses.fairDetail.Data;
import com.vrd.gsaf.api.responses.login.Login;
import com.vrd.gsaf.app.AppSession;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CareerTest extends Fragment implements View.OnClickListener {


    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    ArrayList<Answer> arrayList = new ArrayList();
    List<Question> questions = new ArrayList<>();
    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    TextView currentText;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView;
    private RecyclerView recyclerView;
    private OptionsAdapter adapter;
    private TextView questionCountTxt, questionTxt, noteTxt, stepsCountTxt, titleTxt, messageTxt, qTxt;
    private Button previousBtn, nextBtn, doneBtn;
    private int index = 0;
    private Boolean dashBoard = false;
    private long mLastClickTime;
    private ImageView circular;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();
        Singleton.getInstance().setLanguageIndex(AppSession.getInt("languageIndex"));
        Singleton.getInstance().setLanguage(Singleton.getInstance().getFairData().getFair().getFairLanguages().get(Singleton.getInstance().getLanguageIndex()).getAutoId());
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            dashBoard = bundle.getBoolean("dashBoard");
        }
        view = inflater.inflate(R.layout.fragment_career_test, container, false);
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        progressBar = view.findViewById(R.id.progressBar);
        titleTxt = view.findViewById(R.id.titleTxt);
        circular = view.findViewById(R.id.circular);
        currentText = view.findViewById(R.id.currentText);
        TextView totalText = view.findViewById(R.id.currentText);
        ImageView circularCurrent = view.findViewById(R.id.circularCurrent);
        Singleton.changeIconColor(R.drawable.circle, circularCurrent);
        currentText.setTextColor(Singleton.getTopNavInnerTextColor());
        totalText.setTextColor(Singleton.getTopNavInnerTextColor());
        currentText.setText(Singleton.getKeywords().getCurrent());
        totalText.setText(Singleton.getKeywords().getTotal());
        qTxt = view.findViewById(R.id.qTxt);
        Helper.setTextColor(qTxt);
        messageTxt = view.findViewById(R.id.messageTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaCareertest());
        messageTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTransPleaseTakeCareerTest());
        backImageView = view.findViewById(R.id.backImageView);
        if (!dashBoard)
            backImageView.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.recyclerView);
        questionCountTxt = view.findViewById(R.id.questionCountTxt);
        questionTxt = view.findViewById(R.id.questionTxt);
        stepsCountTxt = view.findViewById(R.id.currentquestionCountTxt);
        previousBtn = view.findViewById(R.id.previousBtn);
        nextBtn = view.findViewById(R.id.nextBtn);
        doneBtn = view.findViewById(R.id.doneBtn);
        doneBtn.setText(Singleton.getKeywords().getDone());
        previousBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getPrevious());
        nextBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getNext());
        // doneBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDone());
        noteTxt = view.findViewById(R.id.noteTxt);


        Helper.setButtonColorWithDrawable(previousBtn,
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
        Helper.setButtonColorWithDrawable(nextBtn,
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
        Helper.setButtonColorWithDrawable(doneBtn,
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());

        Gson gson = new Gson();
        String json = Singleton.getInstance().getSharedPreferences().getString("fairDetails", null);
        if (json != null) {
            Singleton.getInstance().setFairData(gson.fromJson(json, Data.class));

        }
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        setClickListeners();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        if (Helper.isInternetConnected()) {
            callApiForQuestions();
            Helper.showProgressBar(progressBar);
        }
        adapter = new OptionsAdapter(getParentFragmentManager(), arrayList, index, 0, 0, 0, Singleton.getInstance().getIsDashboard());
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        recyclerView.setAdapter(adapter);
        doneBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        previousBtn.setOnClickListener(this);
    }

    private void manageRecyclerView(int position) {
        questionCountTxt.setTextColor(getResources().getColor(R.color.fade_black));
        questionCountTxt.setTextColor(getResources().getColor(R.color.fade_black));
        Helper.setTextColor(questionTxt);
        stepsCountTxt.setTextColor(Singleton.getTopNavInnerTextColor());
        questionTxt.setText(
                HtmlCompat.fromHtml(Singleton.getInstance().getCareerTest().get(position).getQuestion(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
        stepsCountTxt.setText("" + (position + 1));


        noteTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getNote() + " : " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getChooseUpTo() + " " +
                Singleton.getInstance().getCareerTest().get(position).getMaxSelection() + " " + Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getOptions());
        questionCountTxt.setText(String.valueOf(Singleton.getInstance().getCareerTest().size()));
        arrayList.clear();
        for (int i = 0; i < Singleton.getInstance().getCareerTest().get(position).getAnswers().size(); i++) {
            arrayList.add(Singleton.getInstance().getCareerTest().get(position).getAnswers().get(i));
        }
        adapter.updateReceiptsList(index, Singleton.getInstance().getCareerTest().get(position).getId(), Integer.parseInt(Singleton.getInstance().getCareerTest().get(position).getMaxSelection()), Integer.parseInt(Singleton.getInstance().getCareerTest().get(position).getMinSelection()));
        buttonsVisibility();

    }

    private void onBackPress() {
        getActivity().getSupportFragmentManager().popBackStack();

    }


    private void callApiForQuestions() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        jsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        Call<com.vrd.gsaf.api.responses.careerTest.CareerTest> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getCareerTest(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, jsonObject);
        call.enqueue(new Callback<com.vrd.gsaf.api.responses.careerTest.CareerTest>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.careerTest.CareerTest> call, Response<com.vrd.gsaf.api.responses.careerTest.CareerTest> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        Singleton.getInstance().setCareerTest((ArrayList<Question>) response.body().getData().getQuestionList());
                        questions.clear();
                        questions.addAll(response.body().getData().getQuestionList());
                        manageRecyclerView(index);
//                        LinearLayout countersLayout=view.findViewById(R.id.countersLayout);
                        view.findViewById(R.id.countersLayout).setVisibility(View.VISIBLE);
                        noteTxt.setVisibility(View.VISIBLE);
                        qTxt.setVisibility(View.VISIBLE);
                        circular.setVisibility(View.VISIBLE);
                    } else
                        Helper.somethingWentWrong(getView());
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.careerTest.CareerTest> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
            }

        });
    }

    private void callApiToSubmitTest(List<String> list) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject jsonObject = new JsonObject();
        for (String item : list) {
            jsonObject.addProperty(item, item);
        }
        JsonObject.add("selectedAnswers", jsonObject);

        Call<Login> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).submitCareerTest(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.body().getStatus()) {
                        Helper.showToast(Singleton.getKeywords().getTransCareerTestUpdated());
                        if (!dashBoard)
                            loadHome();
                        else
                            onBackPress();
                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
                    ResponseBody response1 = response.errorBody();
                    Helper.getErrorMessage(response1);
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something Went Wrong. Please try again in a while");

            }

        });
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
                onBackPress();
                break;
            case R.id.nextBtn:
                nextQuestion();
                break;
            case R.id.previousBtn:
                previousBtn();
                break;
            case R.id.doneBtn:
                submitTEet();
                break;
        }
    }

    private void submitTEet() {
        List<String> list = new ArrayList<>();
        for (Question question : questions) {
            for (Answer answer : question.getAnswers()) {
                if (answer.getIsChecked()) {
                    list.add(answer.getId().toString());
                }
            }
        }
        Log.d("questionsList", list.toString());
        if (Singleton.getInstance().getSelectedCareerOptions() > 0) {
            if (Helper.isInternetConnected())
                callApiToSubmitTest(list);
        } else
            Helper.showToast(Singleton.getKeywords().getTransTakeCareerTest());
    }


    private void previousBtn() {
        index--;
        Singleton.getInstance().setSelectedCareerOptions(0);
        manageRecyclerView(index);


    }

    private void nextQuestion() {
        if (Singleton.getInstance().getSelectedCareerOptions() > 0) {
            index++;
            Singleton.getInstance().setSelectedCareerOptions(0);
            manageRecyclerView(index);

        } else
            Helper.showToast("Please select at least one option");

    }

    private void buttonsVisibility() {
        if (index == 0 && Singleton.getInstance().getCareerTest().size() > 1) {
            doneBtn.setVisibility(View.GONE);
            previousBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        } else if (index == Singleton.getInstance().getCareerTest().size() - 1) {
            nextBtn.setVisibility(View.GONE);
            doneBtn.setVisibility(View.VISIBLE);
            if (index > 0) {
                previousBtn.setVisibility(View.VISIBLE);
            }
        } else {
            doneBtn.setVisibility(View.GONE);
            previousBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }

    private void loadHome() {
        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
        editor.putBoolean("careerTest", false);
        editor.commit();
        editor.apply();
        Singleton.getInstance().getActivity().finish();
        Singleton.getInstance().getActivity().overridePendingTransition(0, 0);
        startActivity(Singleton.getInstance().getActivity().getIntent());
        Singleton.getInstance().getActivity().overridePendingTransition(0, 0);
//        FragmentManager fm = getParentFragmentManager();
//        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//            fm.popBackStack();
//        }
    }
}