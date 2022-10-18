package com.vrd.gsaf.home.dashboard.recruiters.interviewRequest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.interviewDates.InterviewDates;
import com.vrd.gsaf.api.responses.interviewSlots.Data;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.dashboard.schedules.Schedules;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterviewRequestAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final com.vrd.gsaf.api.responses.interviewSlots.Data dataList;
    private final int recruiterid;
    private final ProgressBar progressBar;
    public FragmentManager view;
    CustomViewHolder selectedHolder;
    boolean forAllRecruiters = false;
    private long mLastClickTime = System.currentTimeMillis();

    public InterviewRequestAdapter(FragmentManager view, Data slots, int index, ProgressBar progressBar) {
        this.dataList = slots;
        this.view = view;
        this.recruiterid = index;
        this.progressBar = progressBar;
    }

    public InterviewRequestAdapter(FragmentManager view, Data slots, int index, ProgressBar progressBar, boolean forAllRecruiters) {
        this.dataList = slots;
        this.view = view;
        this.recruiterid = index;
        this.progressBar = progressBar;
        this.forAllRecruiters = forAllRecruiters;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_interview_slots, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        //            this.data.add(list.getMatchDate().get(i).getDate().split(" ")[1]);

        // holder.slotsTxt.setText(dataList.get(position));
        if (forAllRecruiters) {
            holder.slotsTxt.setText(dataList.getSlotList().get(position).getStart_time() + " " + dataList.getSlotList().get(position).getEnd_time());
            dataList.getSlotList().get(position).setSlot(dataList.getSlotList().get(position).getStart_time() + " " + dataList.getSlotList().get(position).getEnd_time());
        } else {
            holder.slotsTxt.setText(dataList.getSlotList().get(position).getSlot());
        }
        int index = position;
        holder.slotsTxt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                // holder.slotsTxt.setBackground(Drawable.createFromPath(Singleton.getInstance().getContext().getResources().getString(R.color.appColor)));
                openSelectedDateDialof(index);
                selectedHolder = holder;
            }
        });

    }


    private void openSelectedDateDialof(int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_interview_date, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        ImageView cancel = dialogView.findViewById(R.id.crossImageView);
        TextView recruiterNmae3Txt = dialogView.findViewById(R.id.recruiterNmae3Txt);
        recruiterNmae3Txt.setText(dataList.getSlotList().get(position).getSlot());
        cancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                alertDialog.cancel();
                //  selectedHolder.slotsTxt.setBackground(Drawable.createFromPath(Singleton.getInstance().getContext().getResources().getString(R.color.interview_slots_color)));

            }
        });

        Button continueBtn = dialogView.findViewById(R.id.continueBtn);
        Helper.setButtonColorWithDrawable(continueBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(), Singleton.getInstance().getFairData().getFair().getOptions().getReceptionButtonsInnerTextColor());

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                alertDialog.cancel();
                callApiForInterviewDates(position);
            }
        });

        alertDialog.show();
    }

    private void callApiForInterviewDates(int i) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("invited_by", "candidate");
        JsonObject.addProperty("slot_id", dataList.getSlotList().get(i).getId());
        if (!forAllRecruiters) {
            JsonObject.addProperty("recruiter_id", Singleton.getInstance().getRecruiterArrayList().get(recruiterid).getRecruiterId());
        } else {
            JsonObject.addProperty("recruiter_id", dataList.getSlotList().get(i).getRecruiterId());
        }
        Call<InterviewDates> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).bookInterview(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<InterviewDates>() {
            @Override
            public void onResponse(Call<InterviewDates> call, Response<InterviewDates> response) {
                Helper.hideProgressBar(progressBar);

                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {
                    } else if (response.body().getStatus()) {
                        openDetailDialog(i);

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<InterviewDates> call, Throwable t) {
                Helper.hideProgressBar(progressBar);

            }


        });

    }

    private void openDetailDialog(int i) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_interview_booked, null);
        TextView emailTxt = dialogView.findViewById(R.id.emailTxt);
        emailTxt.setText("An Email was sent to " + Singleton.getInstance().getLoginData().getUser().getEmail());
        TextView schedulesText = dialogView.findViewById(R.id.recruiterNmae4Txt);
        SpannableString content = new SpannableString("Please click here to see your schedules");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        schedulesText.setText(content);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        FragmentManager fm = view;

        schedulesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.popBackStackImmediate();
                fm.popBackStackImmediate();
                replaceFragment(new Schedules(), 1);
                alertDialog.cancel();
            }
        });


    }

    private void replaceFragment(Fragment fragment, int index) {


        FragmentManager fm = view;
        fm.beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public int getItemCount() {
        return dataList.getSlotList().size();
    }

    public void clear() {
        int size = dataList.getSlotList().size();
        dataList.getSlotList().clear();
        notifyItemRangeRemoved(0, size);
    }
}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    TextView slotsTxt;

    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        slotsTxt = mView.findViewById(R.id.slotTxt);

    }
}

