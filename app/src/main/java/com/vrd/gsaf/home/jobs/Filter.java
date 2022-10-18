package com.vrd.gsaf.home.jobs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.careerTest.Answer;
import com.vrd.gsaf.api.responses.careerTest.CareerTest;
import com.vrd.gsaf.api.responses.careerTest.Question;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Filter extends Fragment implements View.OnClickListener {

    private final ArrayList<Answer> eventsList = new ArrayList<>();
    private final ArrayList<TextView> textViewArrayList = new ArrayList<>();
    private final JsonObject filterOptions = new JsonObject();
    private View view;
    private ProgressBar progressBar;
    private ImageView crossImageView;
    private ArrayList<Question> questionArrayList = new ArrayList<>();
    private int selectedIndex;
    private TextView titleTxt;
    private LinearLayout ll;
    private Button applyBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_filter, container, false);
        Helper.hideKeyboard();
        initializeViews();

        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        progressBar = view.findViewById(R.id.progressBar);
        applyBtn = view.findViewById(R.id.applyBtn);
        applyBtn.setVisibility(View.GONE);
//        Helper.setButtonColorWithDrawable(applyBtn,
//                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
//                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());

        Helper.setButtonColorWithDrawable(applyBtn,
                "#16233e",
                "#FFFFFF");

        titleTxt = view.findViewById(R.id.titleTxt);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFilter());
        applyBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApply());
        progressBar.setVisibility(View.VISIBLE);
        crossImageView = view.findViewById(R.id.crossImageView);
        setClickListeners();
        if (Helper.isInternetConnected()) {
            if (Singleton.getInstance().getLoginData() != null && Singleton.getInstance().getLoginData().getAccessToken() != null)
                callApiForQuestions();
            else
                callApiForQuestionsWithAuth();
        }

    }


    private void setClickListeners() {
        crossImageView.setOnClickListener(this);
        applyBtn.setOnClickListener(this);
    }

    @SuppressLint("ResourceType")
    private void createButton() {
        LinearLayout ll = view.findViewById(R.id.linearLayout);
        Button button = new Button(Singleton.getInstance().getContext());
        button.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaApply());
        // int textSize =getResources().getInteger(R.dimen._16sdp);
        button.setTextSize(18);
        button.setAllCaps(true);
        button.setId(Integer.parseInt("123"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(80,
                100,
                80, 50);
        button.setLayoutParams(params);
        button.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rounded_cornor_btn_white));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Helper.showToast("Clicked");
                Singleton.getInstance().setApplyFilter(true);
                onBackPress();
            }
        });
        ll.addView(button);
    }


    private void applyFilter() {
    }


    private void dropDownView(int i, String heading, ArrayList<Answer> answers) {


        ll = view.findViewById(R.id.linearLayout);
        View dropDown = LayoutInflater.from(view.getContext()).inflate(R.layout.drop_down, ll, false);

        TextView required = dropDown.findViewById(R.id.requiredStar);
        required.setVisibility(View.GONE);
        TextView textView = dropDown.findViewById(R.id.dropDownClick);
        textView.setHint(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSelect());
        textView.setId(i);
        textViewArrayList.add(textView);
        ArrayList<String> arrayList = new ArrayList<>();
        for (int j = 0; j < answers.size(); j++) {
            answers.get(j).setIsChecked(false);
            arrayList.add(answers.get(j).getAnswer());
        }
        TextView headingTxt = dropDown.findViewById(R.id.headingTxt);
        headingTxt.setText(heading);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDropDown(arrayList);
                selectedIndex = i;
            }
        });
        ll.addView(dropDown);
        if (i == 0)
            dropDown.setVisibility(View.GONE);
    }


    private void openDropDown(ArrayList<String> arrayList) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);


        dialogView.requestLayout();
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //  EditText editText = dialog.findViewById(R.id.edtText);
        ListView listView = dialogView.findViewById(R.id.listView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Singleton.getInstance().getActivity(),
                android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!questionArrayList.get(selectedIndex - 1).getAnswers().get(i).getIsChecked()) {
                    if (textViewArrayList.get(selectedIndex).getText().equals(""))
                        textViewArrayList.get(selectedIndex).setText(arrayAdapter.getItem(i));
                    else
                        textViewArrayList.get(selectedIndex).setText(textViewArrayList.get(selectedIndex).getText() + "," + arrayAdapter.getItem(i));
                    questionArrayList.get(selectedIndex - 1).getAnswers().get(i).setIsChecked(true);
                    filterOptions.addProperty(Integer.toString(questionArrayList.get(selectedIndex - 1).getAnswers().get(i).getId()), Integer.toString(questionArrayList.get(selectedIndex - 1).getAnswers().get(i).getId()));
                    Singleton.getInstance().getFilter().addProperty(Integer.toString(questionArrayList.get(selectedIndex - 1).getAnswers().get(i).getId()), Integer.toString(questionArrayList.get(selectedIndex - 1).getAnswers().get(i).getId()));

                }
                alertDialog.dismiss();

            }

        });
        alertDialog.show();

    }

    private void onBackPress() {


        getParentFragmentManager().popBackStackImmediate();
    }


    private void callApiForQuestions() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        jsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        Call<CareerTest> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getCareerTest(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, jsonObject);
        call.enqueue(new Callback<CareerTest>() {
            @Override
            public void onResponse(Call<CareerTest> call, Response<CareerTest> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        ll.setVisibility(View.GONE);
                        Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        Singleton.getInstance().setCareerTest((ArrayList<Question>) response.body().getData().getQuestionList());
                        prepareView(response.body().getData().getQuestionList());
                        applyBtn.setVisibility(View.VISIBLE);

                    } else {
                        Helper.somethingWentWrong(getView());
                        ll.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    ll.setVisibility(View.GONE);
                    Helper.somethingWentWrong(getView());
                    onBackPress();

                }

            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.careerTest.CareerTest> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
                ll.setVisibility(View.GONE);
                onBackPress();

            }

        });
    }

    private void callApiForQuestionsWithAuth() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        Call<CareerTest> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getCareerTestWithOutAuth(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, jsonObject);
        call.enqueue(new Callback<CareerTest>() {
            @Override
            public void onResponse(Call<CareerTest> call, Response<CareerTest> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        ll.setVisibility(View.GONE);
                        Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        Singleton.getInstance().setCareerTest((ArrayList<Question>) response.body().getData().getQuestionList());
                        prepareView(response.body().getData().getQuestionList());
                        applyBtn.setVisibility(View.VISIBLE);

                    } else {
                        Helper.somethingWentWrong(getView());
                        ll.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    onBackPress();

                    ll.setVisibility(View.GONE);
                    Helper.somethingWentWrong(getView());
                }

            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.careerTest.CareerTest> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());
                ll.setVisibility(View.GONE);
                onBackPress();

            }

        });
    }

    private void prepareView(List<Question> questionList) {
        dropDownView(0, "waqas", eventsList);
        for (int i = 0; i < questionList.size(); i++) {
            eventsList.clear();
            eventsList.addAll(questionList.get(i).getAnswers());
            dropDownView(i + 1, questionList.get(i).getShortQuestion(), eventsList);
        }
        questionArrayList = (ArrayList<Question>) questionList;
        //  createButton();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crossImageView:
                onBackPress();
                break;
            case R.id.applyBtn:
                //    Helper.showToast("Clicked");
                Singleton.getInstance().setApplyFilter(true);
                onBackPress();
                break;
        }
    }


}