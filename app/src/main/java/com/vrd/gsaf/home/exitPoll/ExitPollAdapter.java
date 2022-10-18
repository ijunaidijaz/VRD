package com.vrd.gsaf.home.exitPoll;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.api.responses.exitPoll.Survey;
import com.vrd.gsaf.api.responses.exitPoll.SurveyOption;
import com.vrd.gsaf.singleton.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ExitPollAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final List<Survey> dataList;
    private final Context context;
    private final AlertDialog alertDialog;
    private final long mLastClickTime = System.currentTimeMillis();
    private final JSONArray jsonArray = new JSONArray();
    private final JSONArray selectedRadio = new JSONArray();
    boolean isEditMode = false;
    private CustomViewHolder editTextHolder;
    private CustomViewHolder ratingHolder;

    public ExitPollAdapter(AlertDialog alertDialog, List<Survey> pollList, Boolean edit) {
        this.context = Singleton.getInstance().getContext();
        this.dataList = pollList;
        this.alertDialog = alertDialog;
        this.isEditMode = edit;
    }

    public JSONArray getPollAnswers() {
        return getRatingAndEditTextAnswers();
    }

    public JSONArray getRadioAnswers() {
        JSONArray jsonArray = new JSONArray();
        for (Survey survey : dataList) {
            for (SurveyOption surveyOption : survey.getSurveyOptions()) {
                if (survey.getSurveyType().equalsIgnoreCase("radio") && surveyOption.getIsChecked()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("poll_id", survey.getSurveyId());
                        jsonObject.put("poll_type", survey.getSurveyType());
                        jsonObject.put("option_id", surveyOption.getOptionId());

                    } catch (JSONException e) {
                        // TODO Auto- catch block
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
            }
        }
        return jsonArray;
    }

    private JSONArray getRatingAndEditTextAnswers() {
        if (editTextHolder != null) {
            int i = editTextHolder.getAbsoluteAdapterPosition();
            addInJsonArray(dataList.get(i).getSurveyId(), "text", editTextHolder.editText.getText().toString());
        }
        if (ratingHolder != null) {
            try {
                int ratinf = Math.round(ratingHolder.ratingBar.getRating()) - 1;
                int i = ratingHolder.getAbsoluteAdapterPosition();
                addInJsonArray(dataList.get(i).getSurveyId(), "rating", String.valueOf(dataList.get(i).getSurveyOptions().get(ratinf).getOptionId()));
            } catch (Exception e) {

            }
//            int i = ratingHolder.getAbsoluteAdapterPosition();

//            addInJsonArray(dataList.get(i).getSurveyId(), "rating", String.valueOf(ratingHolder.ratingBar.getRating()));
        }
        return jsonArray;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_stand_poll, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        holder.questionTxt.setText(dataList.get(position).getSurveyQuestion());
        if (dataList.get(position).getSurveyType().equals("radio")) {
            radioButtons(holder, position);

        } else if (dataList.get(position).getSurveyType().equals("checkbox")) {
            checkBox(holder, position);

        } else if (dataList.get(position).getSurveyType().equals("text")) {
            editTextHolder = holder;
            editText(holder, position);

        } else if (dataList.get(position).getSurveyType().equals("rating")) {
            ratingHolder = holder;
            ratingBar(holder, position);


        }


        holder.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                int ratinf = Math.round(v)-1;
//                int i = ratingHolder.getAbsoluteAdapterPosition();
//                addInJsonArray(dataList.get(i).getSurveyId(), "rating", String.valueOf(dataList.get(i).getSurveyOptions().get(ratinf).getOptionId()));
            }
        });

    }

    private void ratingBar(CustomViewHolder holder, int position) {
        List<SurveyOption> surveyOptions = dataList.get(position).getSurveyOptions();
        holder.ratingBar.setVisibility(View.VISIBLE);
        if (isEditMode && surveyOptions != null && !surveyOptions.isEmpty()) {
            for (SurveyOption surveyOption : surveyOptions) {
                if (surveyOption.getIsChecked()) {
                    ratingHolder.ratingBar.setRating(Float.parseFloat(surveyOption.getOptionValue()));
                }
            }
        }
        holder.radioButtonLayout.setVisibility(View.GONE);
        holder.editTextLayout.setVisibility(View.GONE);
        holder.checkBoxLayout.setVisibility(View.GONE);

    }

    private void editText(CustomViewHolder holder, int position) {
        holder.editTextLayout.setVisibility(View.VISIBLE);
//        editTextHolder.editText = holder.mView.findViewById(R.id.editText);
        if (isEditMode && dataList.get(position).getUserAnswer() != null)
            editTextHolder.editText.setText(dataList.get(position).getUserAnswer());
        holder.radioButtonLayout.setVisibility(View.GONE);
        holder.ratingBar.setVisibility(View.GONE);
        holder.checkBoxLayout.setVisibility(View.GONE);

    }

    private void checkBox(CustomViewHolder holder, int position) {
        if (dataList.get(position).getSurveyOptions().size() > 1) {
            List<SurveyOption> surveyOptions = dataList.get(position).getSurveyOptions();
            holder.checkBoxLayout.setVisibility(View.VISIBLE);
            holder.radioButtonLayout.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.GONE);
            holder.editTextLayout.setVisibility(View.GONE);

            holder.checkBoxLayout1.setVisibility(View.VISIBLE);
            holder.checkBoxLayout2.setVisibility(View.VISIBLE);
            holder.checkBoxOptionTxt1.setText(dataList.get(position).getSurveyOptions().get(0).getOptionValue());
            holder.checkBoxOptionTxt2.setText(dataList.get(position).getSurveyOptions().get(1).getOptionValue());

            if (isEditMode && surveyOptions.get(0).getIsChecked()) {
                if (holder.selectorCheckBoxImageView1.getTag().equals("select")) {
                    holder.selectorCheckBoxImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                    holder.selectorCheckBoxImageView1.setTag("selected");
                    addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(0).getOptionId()), true);

                } else {
                    holder.selectorCheckBoxImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                    holder.selectorCheckBoxImageView1.setTag("select");
                    removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(0).getOptionId());
                    addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(0).getOptionId()), false);


                }
            }
            if (isEditMode && surveyOptions.get(1).getIsChecked()) {
                if (holder.selectorCheckBoxImageView2.getTag().equals("select")) {
                    holder.selectorCheckBoxImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                    holder.selectorCheckBoxImageView2.setTag("selected");
                    addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(1).getOptionId()), true);


                } else {
                    holder.selectorCheckBoxImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                    holder.selectorCheckBoxImageView2.setTag("select");
                    removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(1).getOptionId());
                    addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(1).getOptionId()), false);


                }
            }

            if (dataList.get(position).getSurveyTotalOption() > 2) {
                holder.checkBoxLayout3.setVisibility(View.VISIBLE);
                holder.checkBoxOptionTxt3.setText(dataList.get(position).getSurveyOptions().get(2).getOptionValue());
                if (isEditMode && surveyOptions.get(2).getIsChecked()) {
                    if (holder.selectorCheckBoxImageView3.getTag().equals("select")) {
                        holder.selectorCheckBoxImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                        holder.selectorCheckBoxImageView3.setTag("selected");
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(2).getOptionId()), true);


                    } else {
                        holder.selectorCheckBoxImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                        holder.selectorCheckBoxImageView3.setTag("select");
                        removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(2).getOptionId());
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(2).getOptionId()), false);

                    }
                }

            }
            if (dataList.get(position).getSurveyTotalOption() > 3) {
                holder.checkBoxLayout4.setVisibility(View.VISIBLE);
                holder.checkBoxOptionTxt4.setText(dataList.get(position).getSurveyOptions().get(3).getOptionValue());
                if (isEditMode && surveyOptions.get(3).getIsChecked()) {
                    if (holder.selectorCheckBoxImageView4.getTag().equals("select")) {
                        holder.selectorCheckBoxImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                        holder.selectorCheckBoxImageView4.setTag("selected");
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(3).getOptionId()), true);


                    } else {
                        holder.selectorCheckBoxImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                        holder.selectorCheckBoxImageView4.setTag("select");
                        removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(3).getOptionId());
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(3).getOptionId()), false);

                    }
                }
            }
            if (dataList.get(position).getSurveyTotalOption() > 4) {
                holder.checkBoxLayout5.setVisibility(View.VISIBLE);
                holder.checkBoxOptionTxt5.setText(dataList.get(position).getSurveyOptions().get(4).getOptionValue());
                if (isEditMode && surveyOptions.get(4).getIsChecked()) {
                    if (holder.selectorCheckBoxImageView5.getTag().equals("select")) {
                        holder.selectorCheckBoxImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                        holder.selectorCheckBoxImageView5.setTag("selected");
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(4).getOptionId()), true);


                    } else {
                        holder.selectorCheckBoxImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                        holder.selectorCheckBoxImageView5.setTag("select");
                        removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(4).getOptionId());
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(4).getOptionId()), false);

                    }
                }
            }
            if (dataList.get(position).getSurveyTotalOption() > 5) {
                holder.checkBoxLayout6.setVisibility(View.VISIBLE);
                holder.checkBoxOptionTxt6.setText(dataList.get(position).getSurveyOptions().get(5).getOptionValue());
                if (isEditMode && surveyOptions.get(5).getIsChecked()) {
                    if (holder.selectorCheckBoxImageView6.getTag().equals("select")) {
                        holder.selectorCheckBoxImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                        holder.selectorCheckBoxImageView6.setTag("selected");
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(5).getOptionId()), true);

                    } else {
                        holder.selectorCheckBoxImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                        holder.selectorCheckBoxImageView6.setTag("select");
                        removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(5).getOptionId());
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(5).getOptionId()), false);

                    }

                }
            }
            if (dataList.get(position).getSurveyTotalOption() > 6) {
                holder.checkBoxLayout7.setVisibility(View.VISIBLE);
                holder.checkBoxOptionTxt7.setText(dataList.get(position).getSurveyOptions().get(6).getOptionValue());
                if (isEditMode && surveyOptions.get(6).getIsChecked()) {
                    if (holder.selectorCheckBoxImageView7.getTag().equals("select")) {
                        holder.selectorCheckBoxImageView7.setTag("selected");
                        holder.selectorCheckBoxImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(6).getOptionId()), true);

                    } else {
                        holder.selectorCheckBoxImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                        holder.selectorCheckBoxImageView7.setTag("select");
                        removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(6).getOptionId());
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(6).getOptionId()), false);

                    }
                }
            }

            holder.selectorCheckBoxImageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.selectorCheckBoxImageView1.getTag().equals("select")) {
                        holder.selectorCheckBoxImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                        holder.selectorCheckBoxImageView1.setTag("selected");
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(0).getOptionId()), true);

                    } else {
                        holder.selectorCheckBoxImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                        holder.selectorCheckBoxImageView1.setTag("select");
                        removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(0).getOptionId());
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(0).getOptionId()), false);
                    }

                }
            });

            holder.selectorCheckBoxImageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.selectorCheckBoxImageView2.getTag().equals("select")) {
                        holder.selectorCheckBoxImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                        holder.selectorCheckBoxImageView2.setTag("selected");
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(1).getOptionId()), true);


                    } else {
                        holder.selectorCheckBoxImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                        holder.selectorCheckBoxImageView2.setTag("select");
                        removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(1).getOptionId());
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(1).getOptionId()), false);

                    }
                }
            });

            holder.selectorCheckBoxImageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.selectorCheckBoxImageView3.getTag().equals("select")) {
                        holder.selectorCheckBoxImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                        holder.selectorCheckBoxImageView3.setTag("selected");
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(2).getOptionId()), true);


                    } else {
                        holder.selectorCheckBoxImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                        holder.selectorCheckBoxImageView3.setTag("select");
                        removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(2).getOptionId());
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(2).getOptionId()), false);


                    }
                }
            });

            holder.selectorCheckBoxImageView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.selectorCheckBoxImageView4.getTag().equals("select")) {
                        holder.selectorCheckBoxImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                        holder.selectorCheckBoxImageView4.setTag("selected");
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(3).getOptionId()), true);


                    } else {
                        holder.selectorCheckBoxImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                        holder.selectorCheckBoxImageView4.setTag("select");
                        removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(3).getOptionId());
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(3).getOptionId()), false);

                    }
                }
            });

            holder.selectorCheckBoxImageView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.selectorCheckBoxImageView5.getTag().equals("select")) {
                        holder.selectorCheckBoxImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                        holder.selectorCheckBoxImageView5.setTag("selected");
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(4).getOptionId()), true);


                    } else {
                        holder.selectorCheckBoxImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                        holder.selectorCheckBoxImageView5.setTag("select");
                        removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(4).getOptionId());
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(4).getOptionId()), false);

                    }
                }
            });

            holder.selectorCheckBoxImageView6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.selectorCheckBoxImageView6.getTag().equals("select")) {
                        holder.selectorCheckBoxImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                        holder.selectorCheckBoxImageView6.setTag("selected");
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(5).getOptionId()), true);

                    } else {
                        holder.selectorCheckBoxImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                        holder.selectorCheckBoxImageView6.setTag("select");
                        removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(5).getOptionId());
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(5).getOptionId()), false);

                    }

                }
            });
            holder.selectorCheckBoxImageView7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.selectorCheckBoxImageView7.getTag().equals("select")) {
                        holder.selectorCheckBoxImageView7.setTag("selected");
                        holder.selectorCheckBoxImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector_selected));
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(6).getOptionId()), true);

                    } else {
                        holder.selectorCheckBoxImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.rectangle_selector));
                        holder.selectorCheckBoxImageView7.setTag("select");
                        removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(6).getOptionId());
                        addInJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(6).getOptionId()), false);

                    }
                }
            });
        }

    }

    private void radioButtons(CustomViewHolder holder, int position) {
        List<SurveyOption> surveyOptions = dataList.get(position).getSurveyOptions();
        if (dataList.get(position).getSurveyOptions().size() > 1) {
            dataList.get(position).getSurveyOptions().add(dataList.get(position).getSurveyOptions().get(0));
            holder.radioButtonLayout.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.GONE);
            holder.checkBoxLayout.setVisibility(View.GONE);
            holder.editTextLayout.setVisibility(View.GONE);
            holder.radioButtonLayout1.setVisibility(View.VISIBLE);
            holder.radioButtonLayout2.setVisibility(View.VISIBLE);
            holder.radioOptionTxt1.setText(dataList.get(position).getSurveyOptions().get(0).getOptionValue());
            holder.radioOptionTxt2.setText(dataList.get(position).getSurveyOptions().get(1).getOptionValue());
            if  ( surveyOptions.get(0).getIsChecked()) {
                holder.selectorRadioImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

            }
            if (surveyOptions.get(1).getIsChecked()) {
                holder.selectorRadioImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

            }
            if (isEditMode && surveyOptions.get(0).getIsChecked()) {
                holder.selectorRadioImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

            }
            if (isEditMode && surveyOptions.get(1).getIsChecked()) {
                holder.selectorRadioImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

            }


            if ( dataList.get(position).getSurveyTotalOption() > 2) {
                holder.radioButtonLayout3.setVisibility(View.VISIBLE);
                holder.radioOptionTxt3.setText(dataList.get(position).getSurveyOptions().get(2).getOptionValue());

                if (dataList.get(position).getSurveyOptions().get(2).getIsChecked()){
                    holder.selectorRadioImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

                }

                if (isEditMode && dataList.get(position).getSurveyOptions().get(2).getIsChecked()) {
                    holder.selectorRadioImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

                }
            }
            if ( dataList.get(position).getSurveyTotalOption() > 3) {
                holder.radioButtonLayout4.setVisibility(View.VISIBLE);
                holder.radioOptionTxt4.setText(surveyOptions.get(3).getOptionValue());

                if (dataList.get(position).getSurveyOptions().get(3).getIsChecked()){
                    holder.selectorRadioImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

                }
                if (isEditMode && dataList.get(position).getSurveyOptions().get(3).getIsChecked()) {
                    holder.selectorRadioImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

                }
            }
            if ( dataList.get(position).getSurveyTotalOption() > 4) {
                holder.radioButtonLayout5.setVisibility(View.VISIBLE);
                holder.radioOptionTxt5.setText(surveyOptions.get(4).getOptionValue());

                if (isEditMode && surveyOptions.get(4).getIsChecked()) {
                    holder.selectorRadioImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

                }
                if (dataList.get(position).getSurveyOptions().get(4).getIsChecked()){
                    holder.selectorRadioImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

                }
            }
            if (dataList.get(position).getSurveyTotalOption() > 5) {
                holder.radioButtonLayout6.setVisibility(View.VISIBLE);
                holder.radioOptionTxt6.setText(surveyOptions.get(5).getOptionValue());

                if (surveyOptions.get(5).getIsChecked()){
                    holder.selectorRadioImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

                }
                if (isEditMode&& surveyOptions.get(5).getIsChecked()) {
                    holder.selectorRadioImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

                }
            }
            if ( dataList.get(position).getSurveyTotalOption() > 6) {
                holder.radioButtonLayout7.setVisibility(View.VISIBLE);
                holder.radioOptionTxt7.setText(surveyOptions.get(6).getOptionValue());

                if (surveyOptions.get(6).getIsChecked()) {
                    holder.selectorRadioImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

                }
                if (isEditMode && surveyOptions.get(6).getIsChecked()) {
                    holder.selectorRadioImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

                }
            }
        }

        holder.selectorRadioImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.selectorRadioImageView1.getTag().equals("select")) {
                    checkAnotherSelecetd(dataList.get(position).getSurveyId(), holder, 0, dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(0).getOptionId()));
                    holder.selectorRadioImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));
                    holder.selectorRadioImageView1.setTag("selected");
                    uncheckOthers(1, holder, position);
                } else {
                    holder.selectorRadioImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                    holder.selectorRadioImageView1.setTag("select");
                    removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(0).getOptionId());
                    checkAnotherSelecetdOnDeselect(dataList.get(position).getSurveyId(), holder, 0);
                }
            }
        });

        holder.selectorRadioImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.selectorRadioImageView2.getTag().equals("select")) {
                    checkAnotherSelecetd(dataList.get(position).getSurveyId(), holder, 1, dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(1).getOptionId()));
                    holder.selectorRadioImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));
                    holder.selectorRadioImageView2.setTag("selected");
                    uncheckOthers(2, holder, position);
                } else {
                    holder.selectorRadioImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                    holder.selectorRadioImageView2.setTag("select");
                    removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(1).getOptionId());
                    checkAnotherSelecetdOnDeselect(dataList.get(position).getSurveyId(), holder, 1);
                }
            }
        });

        holder.selectorRadioImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.selectorRadioImageView3.getTag().equals("select")) {
                    checkAnotherSelecetd(dataList.get(position).getSurveyId(), holder, 2, dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(2).getOptionId()));
                    holder.selectorRadioImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));
                    holder.selectorRadioImageView3.setTag("selected");
                    uncheckOthers(3, holder, position);
                } else {
                    holder.selectorRadioImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                    holder.selectorRadioImageView3.setTag("select");
                    removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(2).getOptionId());
                    checkAnotherSelecetdOnDeselect(dataList.get(position).getSurveyId(), holder, 2);
                }
            }
        });

        holder.selectorRadioImageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.selectorRadioImageView4.getTag().equals("select")) {
                    checkAnotherSelecetd(dataList.get(position).getSurveyId(), holder, 3, dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(3).getOptionId()));
                    holder.selectorRadioImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));
                    holder.selectorRadioImageView4.setTag("selected");
                    uncheckOthers(4, holder, position);
                } else {
                    holder.selectorRadioImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                    holder.selectorRadioImageView4.setTag("select");
                    removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(3).getOptionId());
                    checkAnotherSelecetdOnDeselect(dataList.get(position).getSurveyId(), holder, 3);
                }
            }
        });

        holder.selectorRadioImageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.selectorRadioImageView5.getTag().equals("select")) {
                    checkAnotherSelecetd(dataList.get(position).getSurveyId(), holder, 4, dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(4).getOptionId()));
                    holder.selectorRadioImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));
                    holder.selectorRadioImageView5.setTag("selected");
                    uncheckOthers(5, holder, position);
                } else {
                    holder.selectorRadioImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                    holder.selectorRadioImageView5.setTag("select");
                    removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(4).getOptionId());
                    checkAnotherSelecetdOnDeselect(dataList.get(position).getSurveyId(), holder, 4);
                }
            }
        });

        holder.selectorRadioImageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.selectorRadioImageView6.getTag().equals("select")) {
                    checkAnotherSelecetd(dataList.get(position).getSurveyId(), holder, 5, dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(5).getOptionId()));
                    holder.selectorRadioImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));
                    holder.selectorRadioImageView6.setTag("selected");
                    uncheckOthers(6, holder, position);
                } else {
                    holder.selectorRadioImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                    holder.selectorRadioImageView6.setTag("select");
                    removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(5).getOptionId());
                    checkAnotherSelecetdOnDeselect(dataList.get(position).getSurveyId(), holder, 5);
                }
            }
        });
        holder.selectorRadioImageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.selectorRadioImageView7.getTag().equals("select")) {
                    checkAnotherSelecetd(dataList.get(position).getSurveyId(), holder, 6, dataList.get(position).getSurveyType(), String.valueOf(dataList.get(position).getSurveyOptions().get(6).getOptionId()));

                    holder.selectorRadioImageView7.setTag("selected");

                    holder.selectorRadioImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));
                    uncheckOthers(7, holder, position);
                } else {
                    holder.selectorRadioImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                    holder.selectorRadioImageView7.setTag("select");
                    removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(6).getOptionId());
                    checkAnotherSelecetdOnDeselect(dataList.get(position).getSurveyId(), holder, 6);
                }
            }
        });


    }

    private void checkAnotherSelecetdOnDeselect(Integer pollId, CustomViewHolder holder, int pos) {
        for (int i = 0; i < selectedRadio.length(); i++) {
            try {
                JSONObject json_obj = selectedRadio.getJSONObject(i);   //get the 3rd item
                String poll_id = json_obj.getString("poll_id");
                Integer position = Integer.parseInt(json_obj.getString("position"));
                if (poll_id.equals(String.valueOf(pollId))) {
                    selectedRadio.remove(i);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jso = jsonArray.getJSONObject(j);   //get the 3rd item
                        String poll = jso.getString("poll_id");
                        if (poll.equals(String.valueOf(pollId)))
                            jsonArray.remove(j);
                    }
                    if (position == 0) {
                        holder.selectorRadioImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView1.setTag("select");
                    } else if (position == 1) {
                        holder.selectorRadioImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView2.setTag("select");
                    } else if (position == 2) {
                        holder.selectorRadioImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView3.setTag("select");
                    } else if (position == 3) {
                        holder.selectorRadioImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView4.setTag("select");
                    } else if (position == 4) {
                        holder.selectorRadioImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView5.setTag("select");
                    } else if (position == 5) {
                        holder.selectorRadioImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView6.setTag("select");
                    } else if (position == 6) {
                        holder.selectorRadioImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView7.setTag("select");
                    }
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkAnotherSelecetd(Integer pollId, CustomViewHolder holder, int pos, String pollType, String optionId) {
        for (int i = 0; i < selectedRadio.length(); i++) {
            try {
                JSONObject json_obj = selectedRadio.getJSONObject(i);   //get the 3rd item
                String poll_id = json_obj.getString("poll_id");
                Integer position = Integer.parseInt(json_obj.getString("position"));
                if (poll_id.equals(String.valueOf(pollId))) {
                    selectedRadio.remove(i);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jso = jsonArray.getJSONObject(j);   //get the 3rd item
                        String poll = jso.getString("poll_id");
                        if (poll.equals(String.valueOf(pollId)))
                            jsonArray.remove(j);
                    }
                    if (position == 0) {
                        holder.selectorRadioImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView1.setTag("select");
                    } else if (position == 1) {
                        holder.selectorRadioImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView2.setTag("select");
                    } else if (position == 2) {
                        holder.selectorRadioImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView3.setTag("select");
                    } else if (position == 3) {
                        holder.selectorRadioImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView4.setTag("select");
                    } else if (position == 4) {
                        holder.selectorRadioImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView5.setTag("select");
                    } else if (position == 5) {
                        holder.selectorRadioImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView6.setTag("select");
                    } else if (position == 6) {
                        holder.selectorRadioImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                        holder.selectorRadioImageView7.setTag("select");
                    }
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        addInRadioSelected(pollId, pos);

//        addInJsonArray(pollId, pollType, optionId);

    }

    private void uncheckOthers(int checkedPosition, CustomViewHolder holder, int position) {
        switch (checkedPosition) {
            case 1:
                holder.selectorRadioImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView2.setTag("select");
                holder.selectorRadioImageView3.setTag("select");
                holder.selectorRadioImageView4.setTag("select");
                holder.selectorRadioImageView5.setTag("select");
                holder.selectorRadioImageView6.setTag("select");
                holder.selectorRadioImageView7.setTag("select");
                removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(0).getOptionId());
                dataList.get(position).getSurveyOptions().get(0).setIsChecked(true);
                break;
            case 2:
                holder.selectorRadioImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView1.setTag("select");
                holder.selectorRadioImageView3.setTag("select");
                holder.selectorRadioImageView4.setTag("select");
                holder.selectorRadioImageView5.setTag("select");
                holder.selectorRadioImageView6.setTag("select");
                holder.selectorRadioImageView7.setTag("select");
                removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(1).getOptionId());
                dataList.get(position).getSurveyOptions().get(1).setIsChecked(true);
                break;

            case 3:
                holder.selectorRadioImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView1.setTag("select");
                holder.selectorRadioImageView2.setTag("select");
                holder.selectorRadioImageView4.setTag("select");
                holder.selectorRadioImageView5.setTag("select");
                holder.selectorRadioImageView6.setTag("select");
                holder.selectorRadioImageView7.setTag("select");
                dataList.get(position).getSurveyOptions().get(2).setIsChecked(true);
                removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(2).getOptionId());
                break;
            case 4:
                holder.selectorRadioImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView1.setTag("select");
                holder.selectorRadioImageView3.setTag("select");
                holder.selectorRadioImageView2.setTag("select");
                holder.selectorRadioImageView5.setTag("select");
                holder.selectorRadioImageView6.setTag("select");
                holder.selectorRadioImageView7.setTag("select");
                dataList.get(position).getSurveyOptions().get(3).setIsChecked(true);
                removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(3).getOptionId());
                break;
            case 5:
                holder.selectorRadioImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView1.setTag("select");
                holder.selectorRadioImageView3.setTag("select");
                holder.selectorRadioImageView4.setTag("select");
                holder.selectorRadioImageView2.setTag("select");
                holder.selectorRadioImageView6.setTag("select");
                holder.selectorRadioImageView7.setTag("select");
                dataList.get(position).getSurveyOptions().get(4).setIsChecked(true);
                removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(4).getOptionId());
                break;
            case 6:
                holder.selectorRadioImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView7.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView1.setTag("select");
                holder.selectorRadioImageView3.setTag("select");
                holder.selectorRadioImageView4.setTag("select");
                holder.selectorRadioImageView5.setTag("select");
                holder.selectorRadioImageView2.setTag("select");
                holder.selectorRadioImageView7.setTag("select");
                dataList.get(position).getSurveyOptions().get(5).setIsChecked(true);
                removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(5).getOptionId());
                break;
            case 7:
                holder.selectorRadioImageView1.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView3.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView4.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView5.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView6.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView2.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorRadioImageView1.setTag("select");
                holder.selectorRadioImageView3.setTag("select");
                holder.selectorRadioImageView4.setTag("select");
                holder.selectorRadioImageView5.setTag("select");
                holder.selectorRadioImageView6.setTag("select");
                holder.selectorRadioImageView2.setTag("select");
                dataList.get(position).getSurveyOptions().get(6).setIsChecked(true);
                removeFromJsonArray(dataList.get(position).getSurveyId(), dataList.get(position).getSurveyType(), dataList.get(position).getSurveyOptions().get(6).getOptionId());
                break;
        }
        for (SurveyOption surveyOption : dataList.get(position).getSurveyOptions()) {
            if (!surveyOption.getOptionId().equals(dataList.get(position).getSurveyOptions().get(checkedPosition - 1).getOptionId())) {
                surveyOption.setIsChecked(false);
            }
        }

    }

    private void addInRadioSelected(Integer pollId, int position) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("poll_id", pollId);
            jsonObject.put("position", position);
        } catch (JSONException e) {
            // TODO Auto- catch block
            e.printStackTrace();
        }
        selectedRadio.put(jsonObject);
    }

    private void addInJsonArray(Integer pollId, String pollType, String optionId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("poll_id", pollId);
            jsonObject.put("poll_type", pollType);
            jsonObject.put("option_id", optionId);

        } catch (JSONException e) {
            // TODO Auto- catch block
            e.printStackTrace();
        }

        jsonArray.put(jsonObject);
    }

    private void addInJsonArray(Integer pollId, String pollType, String optionId, boolean isChecked) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("poll_id", pollId);
            jsonObject.put("poll_type", pollType);
            jsonObject.put("option_id", optionId);
            jsonObject.put("is_checked", isChecked);

            boolean hasValue = false;
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getJSONObject(i).getString("poll_id").equalsIgnoreCase(String.valueOf(pollId))) {
                        hasValue = true;
                        break;
                    }
                }
            }
            if (!hasValue) jsonArray.put(jsonObject);
            else {
                removeFromJsonArray(pollId, pollType, Integer.valueOf(optionId));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            // TODO Auto- catch block
            e.printStackTrace();
        }
    }

    private void removeFromJsonArray(Integer pollId, String pollType, Integer optionId) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject json_obj = jsonArray.getJSONObject(i);   //get the 3rd item
                String poll_id = json_obj.getString("poll_id");
                String poll_type = json_obj.getString("poll_type");
                String option_id = json_obj.getString("option_id");
                if (poll_id.equals(String.valueOf(pollId)) && poll_type.equals(pollType) && option_id.equals(String.valueOf(optionId))) {
                    jsonArray.remove(i);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private void replaceFragment(Fragment fragment, int index) {

//        FragmentManager fm = view;
//        Bundle args = new Bundle();
//        args.putInt("index", index);
//        fragment.setArguments(args);
//        fm.beginTransaction().add(R.id.frameLayout, fragment)
//                .setReorderingAllowed(true)
//                .addToBackStack(null)
//                .commit();
    }

}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    public TextView questionTxt, radioOptionTxt1, radioOptionTxt2, radioOptionTxt3, radioOptionTxt4, radioOptionTxt5, radioOptionTxt6, radioOptionTxt7;
    public RatingBar ratingBar;
    public ConstraintLayout editTextLayout, radioButtonLayout, checkBoxLayout;
    public ConstraintLayout radioButtonLayout1, radioButtonLayout2, radioButtonLayout3, radioButtonLayout4, radioButtonLayout5, radioButtonLayout6, radioButtonLayout7;

    public ConstraintLayout checkBoxLayout1, checkBoxLayout2, checkBoxLayout3, checkBoxLayout4, checkBoxLayout5,
            checkBoxLayout6, checkBoxLayout7;


    public EditText editText;

    public ImageView selectorRadioImageView1, selectorRadioImageView2, selectorRadioImageView3, selectorRadioImageView4,
            selectorRadioImageView5, selectorRadioImageView6, selectorRadioImageView7;

    public ImageView selectorCheckBoxImageView1, selectorCheckBoxImageView2, selectorCheckBoxImageView3, selectorCheckBoxImageView4,
            selectorCheckBoxImageView5, selectorCheckBoxImageView6, selectorCheckBoxImageView7;

    public TextView checkBoxOptionTxt1, checkBoxOptionTxt2, checkBoxOptionTxt3, checkBoxOptionTxt4, checkBoxOptionTxt5,
            checkBoxOptionTxt6, checkBoxOptionTxt7;


    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        questionTxt = mView.findViewById(R.id.questionTxt);

        ratingBar = mView.findViewById(R.id.ratingBar);

        editTextLayout = mView.findViewById(R.id.editTextLayout);
        editText = mView.findViewById(R.id.editText);


        radioButtonLayout = mView.findViewById(R.id.radioButtonLayout);

        radioButtonLayout1 = mView.findViewById(R.id.radioButtonLayout1);
        radioButtonLayout2 = mView.findViewById(R.id.radioButtonLayout2);
        radioButtonLayout3 = mView.findViewById(R.id.radioButtonLayout3);
        radioButtonLayout4 = mView.findViewById(R.id.radioButtonLayout4);
        radioButtonLayout5 = mView.findViewById(R.id.radioButtonLayout5);
        radioButtonLayout6 = mView.findViewById(R.id.radioButtonLayout6);
        radioButtonLayout7 = mView.findViewById(R.id.radioButtonLayout7);

        radioOptionTxt1 = mView.findViewById(R.id.radioOptionTxt1);
        radioOptionTxt2 = mView.findViewById(R.id.radioOptionTxt2);
        radioOptionTxt3 = mView.findViewById(R.id.radioOptionTxt3);
        radioOptionTxt4 = mView.findViewById(R.id.radioOptionTxt4);
        radioOptionTxt5 = mView.findViewById(R.id.radioOptionTxt5);
        radioOptionTxt6 = mView.findViewById(R.id.radioOptionTxt6);
        radioOptionTxt7 = mView.findViewById(R.id.radioOptionTxt7);

        selectorRadioImageView1 = mView.findViewById(R.id.selectorRadioImageView1);
        selectorRadioImageView2 = mView.findViewById(R.id.selectorRadioImageView2);
        selectorRadioImageView3 = mView.findViewById(R.id.selectorRadioImageView3);
        selectorRadioImageView4 = mView.findViewById(R.id.selectorRadioImageView4);
        selectorRadioImageView5 = mView.findViewById(R.id.selectorRadioImageView5);
        selectorRadioImageView6 = mView.findViewById(R.id.selectorRadioImageView6);
        selectorRadioImageView7 = mView.findViewById(R.id.selectorRadioImageView7);


        checkBoxLayout = mView.findViewById(R.id.checkBoxLayout);

        checkBoxLayout1 = mView.findViewById(R.id.checkBoxLayout1);
        checkBoxLayout2 = mView.findViewById(R.id.checkBoxLayout2);
        checkBoxLayout3 = mView.findViewById(R.id.checkBoxLayout3);
        checkBoxLayout4 = mView.findViewById(R.id.checkBoxLayout4);
        checkBoxLayout5 = mView.findViewById(R.id.checkBoxLayout5);
        checkBoxLayout6 = mView.findViewById(R.id.checkBoxLayout6);
        checkBoxLayout7 = mView.findViewById(R.id.checkBoxLayout7);

        selectorCheckBoxImageView1 = mView.findViewById(R.id.selectorCheckBoxImageView1);
        selectorCheckBoxImageView2 = mView.findViewById(R.id.selectorCheckBoxImageView2);
        selectorCheckBoxImageView3 = mView.findViewById(R.id.selectorCheckBoxImageView3);
        selectorCheckBoxImageView4 = mView.findViewById(R.id.selectorCheckBoxImageView4);
        selectorCheckBoxImageView5 = mView.findViewById(R.id.selectorCheckBoxImageView5);
        selectorCheckBoxImageView6 = mView.findViewById(R.id.selectorCheckBoxImageView6);
        selectorCheckBoxImageView7 = mView.findViewById(R.id.selectorCheckBoxImageView7);

        checkBoxOptionTxt1 = mView.findViewById(R.id.checkBoxOptionTxt1);
        checkBoxOptionTxt2 = mView.findViewById(R.id.checkBoxOptionTxt2);
        checkBoxOptionTxt3 = mView.findViewById(R.id.checkBoxOptionTxt3);
        checkBoxOptionTxt4 = mView.findViewById(R.id.checkBoxOptionTxt4);
        checkBoxOptionTxt5 = mView.findViewById(R.id.checkBoxOptionTxt5);
        checkBoxOptionTxt6 = mView.findViewById(R.id.checkBoxOptionTxt6);
        checkBoxOptionTxt7 = mView.findViewById(R.id.checkBoxOptionTxt7);


//        viewGoodie.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getView() + " " +
//                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoodie());
//
//        Helper.setButtonColorWithDrawable(viewGoodie,Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor());
    }

}
