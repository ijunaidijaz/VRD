package com.vrd.gsaf.home.dashboard.careeTest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.api.responses.careerTest.Answer;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;
import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 300;
    private final Boolean dashBoard;
    public FragmentManager view;
    public CustomViewHolder selectedHolder;
    private ArrayList<Answer> dataList = new ArrayList();
    private int index;
    private int questionId;
    private int max;
    private int min;
    private String selectedOptionId;
    private long mLastClickTime = System.currentTimeMillis();


    public OptionsAdapter(FragmentManager view, ArrayList<Answer> options, int index, int questionId, int max, int min, Boolean dashBoard) {
        this.dataList = options;
        this.view = view;
        this.index = index;
        this.questionId = questionId;
        this.max = max;
        this.min = min;
        this.dashBoard = dashBoard;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_options, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setIsRecyclable(false);
        holder.selectorImageView.setTag("unSelected");


        if (dashBoard) {
            if (dataList.get(position).getIsChecked()) {
//                Singleton.getInstance().setSelectedCareerOptions(Singleton.getInstance().getSelectedCareerOptions() + 1);
                holder.selectorImageView.setTag("selected");
                holder.selectorImageView.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));

//                Singleton.getInstance().getCareerTestOptions().addProperty(String.valueOf(dataList.get(position).getId()), String.valueOf(dataList.get(position).getId()));
                selectedOptionId = String.valueOf(dataList.get(position).getId());
                dataList.get(position).setTag(true);
                dataList.get(position).setIsChecked(true);
                selectedHolder = holder;
            }
        } else {
            if (dataList.get(position).getIsChecked()) {
                holder.selectorImageView.setTag("selected");
//            Singleton.getInstance().setSelectedCareerOptions(Singleton.getInstance().getSelectedCareerOptions() + 1);
                holder.selectorImageView.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));
            } else {
                holder.selectorImageView.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                holder.selectorImageView.setTag("unSelected");
            }
        }

        int p = position;
        holder.optionTxt.setText(dataList.get(position).getAnswer());
        holder.optionTxt.setTextColor(Singleton.getSidebarInnerTextColor());
        holder.selectorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;

                if (holder.selectorImageView.getTag().equals("selected")) {
                    holder.selectorImageView.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                    //selcetedCount--;
                    Singleton.getInstance().setSelectedCareerOptions(Singleton.getInstance().getSelectedCareerOptions() - 1);
//                    Singleton.getInstance().getCareerTestOptions().remove(String.valueOf(dataList.get(position).getId()));

                    holder.selectorImageView.setTag("unSelected");
                    dataList.get(position).setIsChecked(false);
                    dataList.get(position).setTag(false);

                } else if (Singleton.getInstance().getSelectedCareerOptions() < max) {
                    if (!dataList.get(position).getIsChecked()) {
                        holder.selectorImageView.setTag("selected");
//                        Singleton.getInstance().getCareerTestOptions().addProperty(String.valueOf(dataList.get(p).getId()), String.valueOf(dataList.get(p).getId()));
                        selectedOptionId = String.valueOf(dataList.get(p).getId());
                        holder.selectorImageView.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));
                        dataList.get(position).setIsChecked(true);
                        dataList.get(position).setTag(true);
                        selectedHolder = holder;
                        //  selcetedCount++;
                        Singleton.getInstance().setSelectedCareerOptions(Singleton.getInstance().getSelectedCareerOptions() + 1);
                    }
                } else if (max == 1) {
//                    Singleton.getInstance().getCareerTestOptions().remove(selectedOptionId);
                    holder.selectorImageView.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle));
                    holder.selectorImageView.setTag("unSelected");
                    dataList.get(holder.getLayoutPosition()).setIsChecked(false);
                    dataList.get(holder.getLayoutPosition()).setTag(false);


                    holder.selectorImageView.setBackground(ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.option_circle_selected));
                    dataList.get(position).setIsChecked(true);
                    dataList.get(position).setTag(true);

                    holder.selectorImageView.setTag("selected");
                    dataList.get(position).setTag(true);
                    dataList.get(position).setIsChecked(true);

//                    Singleton.getInstance().getCareerTestOptions().addProperty(String.valueOf(dataList.get(p).getId()), String.valueOf(dataList.get(p).getId()));
                    selectedOptionId = String.valueOf(dataList.get(p).getId());
                    selectedHolder = holder;
                    unselectOthers(position, holder);
                } else if (Singleton.getInstance().getSelectedCareerOptions() == max) {
                    Helper.showToast("You have selected the maximum options ");
                }
//                Helper.showToast(""+Singleton.getInstance().getSelectedCareerOptions());
            }


        });


    }

    private void unselectOthers(int position, CustomViewHolder holder) {
        for (int i = 0; i < dataList.size(); i++) {
            if (position != i) {
                dataList.get(i).setIsChecked(false);
                dataList.get(i).setTag(false);
//                Singleton.getInstance().getCareerTestOptions().remove(String.valueOf(dataList.get(i).getId()));
            }
            notifyDataSetChanged();
        }


    }

    public void updateReceiptsList(int index, int questionId, int max, int min) {
        selectedHolder = null;
        this.index = index;
        this.questionId = questionId;
        this.max = max;
        this.min = min;
        Singleton.getInstance().setSelectedCareerOptions(0);
        for (Answer answer : dataList) {
            if (answer.getIsChecked()) {
                Singleton.getInstance().setSelectedCareerOptions(Singleton.getInstance().getSelectedCareerOptions() + 1);
            }
        }

        //  this.selcetedCount = 0;
// notify adapter
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private void replaceFragment(Fragment fragment, String tag, int index) {

        FragmentManager fm = view;
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fm.beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public List<Answer> getAllItems() {
        return dataList;
    }
}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    TextView optionTxt;
    ImageView selectorImageView;


    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        optionTxt = mView.findViewById(R.id.optionTxt);
        selectorImageView = mView.findViewById(R.id.selectorImageView);
//        selectorImageView.setTag("unSelected");

    }
}

