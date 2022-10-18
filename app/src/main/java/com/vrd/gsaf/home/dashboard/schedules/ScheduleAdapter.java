package com.vrd.gsaf.home.dashboard.schedules;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.general.General;
import com.vrd.gsaf.api.responses.interviewDates.InterviewDates;
import com.vrd.gsaf.api.responses.schedules.InterviewSlot;
import com.vrd.gsaf.api.responses.webinarDetail.WebinarDetail;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.conferenceAgenda.ConferenceModel;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private static final long CLICK_TIME_INTERVAL = 500;
    private final Context context;
    private final Fragment view;
    public FragmentManager fragmentManager;
    public ProgressBar progressBar;
    private ProgressDialog dialog;
    private long mLastClickTime = System.currentTimeMillis();

    private List<InterviewSlot> filteredDataList = Singleton.getInstance().getSchedulesList();

    public ScheduleAdapter(FragmentManager fragmentManager, Fragment view, ProgressBar progressBar) {
        this.context = Singleton.getInstance().getContext();
        this.fragmentManager = fragmentManager;
        this.progressBar = progressBar;
        this.view = view;
        filteredDataList = Singleton.getInstance().getSchedulesList();
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_schedules, parent, false);
        return new CustomViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        InterviewSlot interviewSlot = filteredDataList.get(position);
        holder.recruiterName.setText(filteredDataList.get(position).getRecruiterName());
        holder.companyName.setText(filteredDataList.get(position).getCompany());
        // holder.slotTxt.setText(Singleton.getInstance().getSchedulesList().get(position).getSlot());
        holder.slot.setText(filteredDataList.get(position).getSlot());
        holder.cancel.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCancel());
        holder.invitedBy.setText(filteredDataList.get(position).getInvitedBy());

        setStatus(filteredDataList.get(position).getStatus(), holder, filteredDataList.get(position).getInvitedBy());


        String upperString = filteredDataList.get(position).getStatus().substring(0, 1).toUpperCase() + filteredDataList.get(position).getStatus().substring(1).toLowerCase();

        holder.status.setText(upperString);

        int index = position;
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if (holder.cancel.getText().equals("cancel"))
                openCancelReasonDialog(index, holder);

            }
        });

        holder.resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiForResendingEmail(index);
            }
        });
        holder.acceptLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiForInterviewAccept(index, holder);
            }
        });
        holder.startInterview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // replaceFragment(new WebView(),null);
                // Helper.startWhereBy();

                //   Helper.startLiveWebinar("https://livewebinar.com/338-397-333/p/pKsxEAfSnMukVJBcvj2JuzPlhi3Huhptt5t7NavpuWTXpAKF7HTNpqd2nl7O");
                if (interviewSlot.getTeamsMeeting().equalsIgnoreCase("Y")) {
                    Helper.startMsTeams(interviewSlot.getTeamsUrl());

                } else if (interviewSlot.getTeamsMeeting().equalsIgnoreCase("N")) {
                    Helper.startMsTeams("https://embed.archiebot.com/resource/widget/303-613-435/" + interviewSlot.getuId());
                }


            }
        });
    }

    private void replaceFragment(Fragment fragment, String tag) {

        fragmentManager.beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();

    }

    private void callApiForInterviewAccept(int index, CustomViewHolder holder) {

        startProgressBar(true);
        String url = "api/interview/book/schedule/" + filteredDataList.get(index).getuId() + "/candidate";
        Call<General> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).acceptInvitation(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<General>() {
            @Override
            public void onResponse(Call<General> call, Response<General> response) {
                startProgressBar(false);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {
                    } else if (response.body().getStatus()) {
                        Helper.showToast(response.body().getMsg());
                        Singleton.getInstance().getSchedulesList().get(index).setStatus("booked");

                        Helper.showToast(response.body().getMsg());

                        holder.acceptLayout.setVisibility(View.GONE);
                        holder.resendLayout.setVisibility(View.VISIBLE);
                        holder.startInterview.setVisibility(View.VISIBLE);
                        notifyDataSetChanged();

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<General> call, Throwable t) {
                startProgressBar(false);

            }

        });
    }

    private void setStatus(String status, CustomViewHolder holder, String invitedBy) {

        switch (status) {
            case "pending":
                holder.startInterviewLayout.setVisibility(View.GONE);
                holder.resendLayout.setVisibility(View.GONE);
                holder.status.setTextColor(Color.parseColor("#0b8367"));
//                if (invitedBy.equals("candidate")) {
//                    holder.resendLayout.setVisibility(View.VISIBLE);
//                } else
//                    holder.acceptLayout.setVisibility(View.VISIBLE);
                break;
            case "booked":
                holder.status.setTextColor(Color.BLACK);

                break;
            case "canceled":
            case "noshow":
                holder.startInterviewLayout.setVisibility(View.GONE);
                holder.resendLayout.setVisibility(View.GONE);
                holder.cancel.setVisibility(View.GONE);
                holder.cancelImageView.setVisibility(View.GONE);
                holder.status.setTextColor(Color.RED);
                break;

            case "completed":
                holder.startInterviewLayout.setVisibility(View.GONE);
                holder.resendLayout.setVisibility(View.GONE);
                holder.cancel.setVisibility(View.GONE);
                holder.cancelImageView.setVisibility(View.GONE);
                holder.status.setTextColor(Color.BLUE);

                break;

        }
    }

    private void openCancelReasonDialog(int index, CustomViewHolder holder) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_interview_cancel_date, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        ImageView cancel = dialogView.findViewById(R.id.crossImageView);
        EditText reasonEdt = dialogView.findViewById(R.id.reasonEdt);
        TextView reasonTxt = dialogView.findViewById(R.id.reasonTxt);
        //  reasonTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getResume());
        reasonTxt.setText("Reason");
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

        Button submitBtn = dialogView.findViewById(R.id.submitBtn);
        submitBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSubmit());
        Helper.setButtonColorWithDrawable(submitBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(), Singleton.getInstance().getFairData().getFair().getOptions().getReceptionButtonsInnerTextColor());

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (!reasonEdt.getText().toString().equals("")) {
                    callApiForCancellingInterview(index, holder);
                    alertDialog.cancel();
                } else
                    Helper.showToast("Please enter some reason");
            }
        });

        alertDialog.show();

    }


    public void startProgressBar(boolean start) {

        if (start) {
            dialog = new ProgressDialog(Singleton.getInstance().getContext());
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Please Wait");
            dialog.show();
        } else {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }


    private void callApiForResendingEmail(int index) {
        startProgressBar(true);
        String url = "api/resend/confirmation/email/" + Singleton.getInstance().getSchedulesList().get(index).getuId();
        Call<WebinarDetail> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).resendEmail(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<WebinarDetail>() {
            @Override
            public void onResponse(Call<WebinarDetail> call, Response<WebinarDetail> response) {
                startProgressBar(false);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {
                    } else if (response.body().getStatus()) {
                        Helper.showToast(response.body().getMsg());
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<WebinarDetail> call, Throwable t) {
                startProgressBar(false);

            }

        });

    }


    private void callApiForCancellingInterview(int index, CustomViewHolder holder) {
        startProgressBar(true);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("u_id", Singleton.getInstance().getSchedulesList().get(index).getuId());
        JsonObject.addProperty("slot_id", Singleton.getInstance().getSchedulesList().get(index).getSlotId());
        JsonObject.addProperty("notes", "This is excuse note");
        Call<InterviewDates> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).cancelInterView(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<InterviewDates>() {
            @Override
            public void onResponse(Call<InterviewDates> call, Response<InterviewDates> response) {
                startProgressBar(false);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {
                    } else if (response.body().getStatus()) {
                        Singleton.getInstance().getSchedulesList().get(index).setStatus("canceled");
                        // clearData();
                        Helper.hideProgressBar(progressBar);
                        Helper.showToast(response.body().getMsg());
                        holder.acceptLayout.setVisibility(View.GONE);
                        notifyDataSetChanged();

//                        if (fragmentManager != null) {
//                            Bundle bundle = new Bundle();
//                            bundle.putBoolean("dashBoard", true);
//                            view.setArguments(bundle);
//                            fragmentManager.beginTransaction().replace(R.id.frameLayout, new Schedules())
//                                    .setReorderingAllowed(true)
//                                    .addToBackStack(null)
//                                    .commit();
//
//                        }
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<InterviewDates> call, Throwable t) {

                startProgressBar(false);
                Helper.showToast("Something went wrong");

            }

        });

    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String[] list = charSequence.toString().split(",");
                String title = list[0];

                String time = list[1];
                String date = list[2];
                if (time.equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStartTime()))
                    time = " ";
                if (title.equals(" ") && time.equals(" ") && date.equals(" ")) {
                    filteredDataList = Singleton.getInstance().getSchedulesList();
                } else {
                    List<InterviewSlot> filteredList = new ArrayList<>();
                    for (InterviewSlot row : Singleton.getInstance().getSchedulesList()) {

//                        if (row.getStatus().toLowerCase().contains(title.toLowerCase()))
//                            filteredList.add(row);

                        if (!title.equals(" ") && !time.equals(" ")) {
                            if (row.getStatus().toLowerCase().contains(title.toLowerCase()) && row.getSlot().toLowerCase().contains(time.toLowerCase()))
                                filteredList.add(row);

                        } else if (title.equals(" ")) {
                            if (row.getSlot().toLowerCase().contains(time.toLowerCase()))
                                filteredList.add(row);
                        } else if (time.equals(" ")) {
                            if (row.getStatus().toLowerCase().contains(title.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                    }
                    // if (filteredList.size() > 0)
                    filteredDataList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataList = (List<InterviewSlot>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public void clearData() {
        if (Singleton.getInstance().getSchedulesList() != null && filteredDataList != null) {
            filteredDataList.clear();
            Singleton.getInstance().getSchedulesList().clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface ConferenceAdapterListener {
        void onTitleSelected(ConferenceModel contact);
    }
}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    TextView acceptTxt, recruiterName, recruiterTxt, companyName, companyTxt, slot, slotTxt, status, statusTxt, invitedBy, invitedByTxt, startInterview, cancel, resend;

    LinearLayoutCompat startInterviewLayout, resendLayout, acceptLayout;
    ImageView cancelImageView;

    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        cancelImageView = mView.findViewById(R.id.cancelImageView);
        acceptTxt = mView.findViewById(R.id.acceptTxt);
        acceptLayout = mView.findViewById(R.id.acceptLayout);
        recruiterName = mView.findViewById(R.id.recruiterNameTxt);
        resendLayout = mView.findViewById(R.id.resendLayout);
        startInterviewLayout = mView.findViewById(R.id.startInterviewLayout);
        recruiterTxt = mView.findViewById(R.id.recruiterTxt);
        companyName = mView.findViewById(R.id.companyNameTxt);
        companyTxt = mView.findViewById(R.id.companyTxt);
        slot = mView.findViewById(R.id.slotValueTxt);
        slotTxt = mView.findViewById(R.id.slotTxt);
        status = mView.findViewById(R.id.statusValueTxt);
        statusTxt = mView.findViewById(R.id.statusTxt);
        invitedBy = mView.findViewById(R.id.invitedByValueTxt);
        invitedByTxt = mView.findViewById(R.id.invitedByTxt);
        startInterview = mView.findViewById(R.id.startInterveiwTxt);
        cancel = mView.findViewById(R.id.cancelTxt);
        resend = mView.findViewById(R.id.resendTxt);
        recruiterTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getRecruiter() + " :");
        companyTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCompany() + " :");
        slotTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSlot() + " :");
        statusTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStatus() + " :");
        invitedByTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getInvitedBy() + " :");
        startInterview.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStartInterview());
        cancel.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCancel());
        resend.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getResendConfirmationEmail());
        acceptTxt.setText("Accept");

    }
}




