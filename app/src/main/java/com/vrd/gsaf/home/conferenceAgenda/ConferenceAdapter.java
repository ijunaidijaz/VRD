package com.vrd.gsaf.home.conferenceAgenda;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.speakerDetail.DetailList;
import com.vrd.gsaf.api.responses.speakerDetail.SpeakerDetail;
import com.vrd.gsaf.api.responses.webinarDetail.Detail;
import com.vrd.gsaf.api.responses.webinarDetail.WebinarDetail;
import com.vrd.gsaf.api.responses.webinars.Webinar;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.callbacks.ConferenceAgendaCallback;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.companies.companyStand.CompnayStand;
import com.vrd.gsaf.home.jobs.CompanyDetail;
import com.vrd.gsaf.home.liveWebinar.LiveWebinar;
import com.vrd.gsaf.home.whereBy.WebViewActivity;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConferenceAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final ArrayList<Webinar> dataList;
    private final Context context;
    private final ProgressBar progressBar;
    public FragmentManager view;
    ConferenceAgendaCallback mCallback;
    private List<Webinar> filteredDataList;
    private long mLastClickTime = System.currentTimeMillis();


    public ConferenceAdapter(ProgressBar progressBar, Context context, FragmentManager view, ArrayList<Webinar> dataList, ConferenceAgendaCallback mCallback) {
        this.context = context;
        this.dataList = dataList;
        this.filteredDataList = dataList;
        this.view = view;
        this.progressBar = progressBar;
        this.mCallback = mCallback;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Helper.lockOrientation();

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_conference, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        if (!Singleton.getInstance().getLive())
            holder.joinBtn.setVisibility(View.GONE);
        holder.title.setText(filteredDataList.get(position).getWebinarTitle());
        if (filteredDataList.get(position).getCompanyInfo() != null && filteredDataList.get(position).getCompanyInfo().getCompanyName() != null && filteredDataList.get(position).getCompanyInfo().getCompanyName() != "") {
            holder.companyNameTxt.setVisibility(View.VISIBLE);
            holder.companyNameTxt.setText(filteredDataList.get(position).getCompanyInfo().getCompanyName());
            holder.companyNameTxt.setPaintFlags(holder.companyNameTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        holder.typeValue.setText(filteredDataList.get(position).getWebinarType());
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_stage_name_webinars_listing_front().equals(0) && !filteredDataList.get(position).getStageName().equals("")) {
            holder.stageLayout.setVisibility(View.VISIBLE);
            holder.stageValue.setVisibility(View.VISIBLE);
            holder.stageValue.setText(filteredDataList.get(position).getStageName());

        }
        holder.startAtValue.setText(filteredDataList.get(position).getWebinarStartTime());
        holder.endAtValue.setText(filteredDataList.get(position).getWebinarEndTime());
        // if (filteredDataList.get(position).getWebinarSpeaker() == 1) {
        holder.roundImageCardView.setVisibility(View.VISIBLE);
        Helper.loadCircularImageFromUrl(holder.speakerImage, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + filteredDataList.get(position).getUserInfo().getImage());
        if (Singleton.getInstance().getIsLoggedIn() && Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("1")) {
            holder.matchingLayout.setVisibility(View.VISIBLE);
            holder.matchingJobsValueTxt.setText(filteredDataList.get(position).getWebinarPercentage() + "%");
        }

        if (!Singleton.getInstance().getIsLoggedIn() || Singleton.getInstance().getFairData().getFair().getOptions().getEnabledMatchingWebinar().equals(0))
            holder.matchingLayout.setVisibility(View.GONE);
        if (!Singleton.getInstance().getIsLoggedIn() || Singleton.getInstance().getFairData().getFair().getOptions().getDisableViewSpeakerProfile().equals(1))
            holder.profileLayout.setVisibility(View.GONE);
        if (!Singleton.getInstance().getIsLoggedIn() || Singleton.getInstance().getFairData().getFair().getOptions().getDisableWebinarTypeWebinarsListingFront().equals(1))
            holder.webinarTypeLayout.setVisibility(View.GONE);

        //  } else {


//            holder.rectangleImageCardView.setVisibility(View.VISIBLE);
//            Helper.loadRectangleImageFromUrl(holder.companyImage, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + filteredDataList.get(position).getCompanyInfo().getCompanyLogo());

        //    }

        int index = position;


        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                Helper.showProgressBar(progressBar);
                callApiForSpeakerDetailApi(index);
            }
        });
        holder.companyNameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                if (Singleton.getInstance().getLive() && Singleton.getInstance().getIsLoggedIn())
                    replaceFragment(new CompnayStand(), null, index);

                else {
                    replaceFragment(new CompanyDetail(), null, index);
                }
            }
        });

        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (Helper.isInternetConnected()) {
                    Helper.showProgressBar(progressBar);
                    callApiForWebinarDetailApi(index, false);
                }
            }
        });

        holder.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (Singleton.getInstance().getLoginData() == null)
                    Helper.showToast("Please login");
                else {
                    Webinar webinar = dataList.get(index);
                    if (webinar.getMainStage().equals(7)) {
                        callApiForWebinarDetailApi(index, true);
                    } else if (webinar.getMainStage().equals(6)) {
                        Helper.viewDocument(webinar.getFinalJoinLink());
                    } else if (Singleton.getInstance().getFairData().getFair().getWebinarPlugin().equalsIgnoreCase("zoom") && (webinar.getMainStage().equals(3) || webinar.getMainStage().equals(5))) {
                        Helper.launchZoomUrl(webinar.getFinalJoinLink());
                    } else if (Singleton.getInstance().getFairData().getFair().getWebinarPlugin().equalsIgnoreCase("live") && webinar.getMainStage().equals(4)) {
                        callApiForWebinarDetailApi(index, true);
                    } else if (webinar.getWebinarType().equalsIgnoreCase("link")) {
                        Helper.viewDocument(webinar.getFinalJoinLink());
                    } else if (Singleton.getInstance().getFairData().getFair().getWebinarPlugin().equalsIgnoreCase("default_plugin")) {
                        callApiForWebinarDetailApi(index, true);
                    } else {
                        callApiForWebinarDetailApi(index, true);
                    }
//


                }

            }
        });
        holder.matchingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.matchPercentage(dataList.get(index).getWebinarId());

            }
        });

        if (!Singleton.getInstance().getIsLoggedIn() || Singleton.getInstance().getFairData().getFair().getOptions().getDisableCompanyNameWebinarsListingFront().equals(1)) {
            holder.companyNameTxt.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_start_end_date_time_webinars_front().equals(1)) {
            holder.datesLayout.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_view_detail_webinars_listing_front().equals(1)) {
            holder.webinarDetailLayout.setVisibility(View.GONE);
        }
        if (dataList.get(position).getWebinar_status() != null) {
            if (dataList.get(position).getWebinar_status().equalsIgnoreCase("live")) {
                holder.joinBtn.setEnabled(true);
            } else if (dataList.get(position).getWebinar_status().equalsIgnoreCase("coming soon")) {
                holder.joinBtn.setEnabled(false);
                Helper.setButtonColorWithDrawable(holder.joinBtn, "#C4C4C4",
                        Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
                holder.joinBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTrans_starting_soon());
            } else {
                holder.joinBtn.setEnabled(false);
                Helper.setButtonColorWithDrawable(holder.joinBtn, "#C4C4C4",
                        Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
                if (Singleton.getKeywords().getTrans_webinar_has_been_closed() != null && !Singleton.getKeywords().getTrans_webinar_has_been_closed().isEmpty()) {
                    holder.joinBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTrans_webinar_has_been_closed());
                } else if (Singleton.getKeywords().getWebinar_closed() != null && !Singleton.getKeywords().getWebinar_closed().isEmpty()) {
                    holder.joinBtn.setText(Singleton.getKeywords().getWebinar_closed());
                } else {
                    holder.joinBtn.setText("Webinar has been closed");
                }
            }
        } else {
            holder.joinBtn.setEnabled(false);
            Helper.setButtonColorWithDrawable(holder.joinBtn, "#C4C4C4",
                    Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
            holder.joinBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTrans_string_oops_no_data_found());

        }

//        holder.startTimeIcon.setColorFilter(ContextCompat.getColor(context, R.color.green_600),android.graphics.PorterDuff.Mode.SRC_IN);
    }


    private void callApiForSpeakerDetailApi(int index) {
        Helper.showProgressBar(progressBar);
        String url = "api/auth/get/speaker/detail/" + dataList.get(index).getRecruiterId();
        Call<SpeakerDetail> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getSpeakerDetail(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, url);
        call.enqueue(new Callback<SpeakerDetail>() {
            @Override
            public void onResponse(Call<SpeakerDetail> call, Response<SpeakerDetail> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.body().getStatus()) {
                        if (response.body().getData().getDetailList().size() > 0)
                            openProfileDialog(response.body().getData().getDetailList().get(0), index);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<SpeakerDetail> call, Throwable t) {
                Helper.hideProgressBar(progressBar);

            }

        });

    }

    private void callApiForWebinarDetailApi(int index, Boolean start) {
        Helper.showProgressBar(progressBar);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fair_id", String.valueOf(Singleton.getMyFairId()))
                .addFormDataPart("webinar_id", String.valueOf(dataList.get(index).getWebinarId()))
                .build();
        Call<WebinarDetail> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getWebinarDetail(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, requestBody);
        call.enqueue(new Callback<WebinarDetail>() {
            @Override
            public void onResponse(Call<WebinarDetail> call, Response<WebinarDetail> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.body().getStatus()) {
                        if (response.body().getData().getDetailList().size() > 0) {
                            if (start) {
                                Singleton.getInstance().setWebinarDetail(response.body().getData().getDetailList().get(0));

                                Webinar webinar = dataList.get(index);
                                String plugin = Singleton.getInstance().getFairData().getFair().getWebinarPlugin();
                                Singleton.getInstance().setCurrentWebinar(webinar);
//                                 if (webinar.getWebinarType().equalsIgnoreCase("link")) {
//                                    Helper.viewDocument(webinar.getFinalJoinLink());
//                                }else
                                if (webinar.getMainStage() == 7) {
                                    replaceFragment(new LiveWebinar(), "liveWebinar");
                                } else if (webinar.getMainStage() == 6) {
                                    Helper.startMsTeams(webinar.getFinalJoinLink());
                                } else if (webinar.getWebinarType().equalsIgnoreCase("live_100")) {
                                    MainApp.getAppContext().startActivity(new Intent(MainApp.getAppContext(), WebViewActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .putExtra("url", webinar.getFinalJoinLink()));

                                } else if (webinar.getWebinarType().equalsIgnoreCase("ms_team") || webinar.getWebinarType().equalsIgnoreCase("ms_team_direct")) {
                                    Helper.viewDocument(webinar.getFinalJoinLink());
                                } else if (webinar.getMainStage().equals(0)) {
                                    replaceFragment(new LiveWebinar(), "liveWebinar");
                                } else if (webinar.getWebinarType().equalsIgnoreCase("link")) {
                                    Helper.viewDocument(webinar.getFinalJoinLink());
                                } else {
                                    Helper.viewDocument(webinar.getFinalJoinLink());
                                }

                            } else
                                openDetailDialog(response.body().getData().getDetailList().get(0));

                        }
                    }

                } catch (Exception e) {
                    Helper.showToast("Something Went Wrong.PLease try Later!");
                }
            }

            @Override
            public void onFailure(Call<WebinarDetail> call, Throwable t) {
                Helper.hideProgressBar(progressBar);

            }

        });

    }

    private void openProfileDialog(DetailList detail, int index) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_speaker_profile, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        ImageView cancel = dialogView.findViewById(R.id.cancelImageView);
        ConstraintLayout parent = dialogView.findViewById(R.id.parentLayout);
        ImageView userImageView = dialogView.findViewById(R.id.userImageView);
        parent.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        Helper.loadCircularImageFromUrl(userImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + detail.getUserImage());
        if (!detail.getUserDetails().equals("")) {
            TextView description = dialogView.findViewById(R.id.descriptionTxt);
            description.setVisibility(View.VISIBLE);
            Helper.loadHtml(description, detail.getUserDetails());
        }
        TextView userDesignationTxt = dialogView.findViewById(R.id.userDesignationTxt);
        userDesignationTxt.setText(detail.getTitle());
        TextView userNameTxt = dialogView.findViewById(R.id.userNameTxt);
        userNameTxt.setText(detail.getUserName());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void openDetailDialog(Detail detail) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_webinar_details, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        ImageView cancel = dialogView.findViewById(R.id.cancelImageView);
        TextView webinarTitleTxt = dialogView.findViewById(R.id.webinarTitleTxt);
        TextView descriptionTxt = dialogView.findViewById(R.id.descriptionTxt);
        Helper.loadHtml(webinarTitleTxt, detail.getWebinarTitle());
        Helper.loadHtml(descriptionTxt, detail.getWebinarDetail());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }


    private void replaceFragment(Fragment fragment, String tag) {

        FragmentManager fm = view;
        Bundle args = new Bundle();
        // args.putInt("index", index);
        fragment.setArguments(args);
        fm.beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();
    }

    private void replaceFragment(Fragment fragment, String tag, int index) {

        Singleton.getInstance().setStandCompanyId(filteredDataList.get(index).getCompanyInfo().getCompanyId());
        FragmentManager fm = view;
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fm.beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();

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
                // if(date.equals())
                if (title.equals(" ") && time.equals(" ") && date.equals(" ")) {
                    filteredDataList = dataList;
                } else {
                    List<Webinar> filteredList = new ArrayList<>();
                    if (time.equalsIgnoreCase("Start Date")) {
                        time = " ";
                    }
                    for (Webinar row : dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
//                        if (row.getTitle().toLowerCase().contains(title.toLowerCase()) || row.getStartTime().contains(time) ) {
//                            filteredList.add(row);
//                        }

                        if (!title.trim().equalsIgnoreCase("") && time.trim().equalsIgnoreCase("")) {
                            if (row.getWebinarTitle().toLowerCase().contains(title.toLowerCase()))
                                filteredList.add(row);
                        } else if (!title.equals(" ") && !time.equals(" ")) {
                            if (row.getWebinarTitle().toLowerCase().contains(title.toLowerCase()) && row.getWebinarStartTime().toLowerCase().contains(time.toLowerCase()))
                                filteredList.add(row);

                        } else if (title.equals(" ")) {
                            if (row.getWebinarStartTime().toLowerCase().contains(time.toLowerCase()))
                                filteredList.add(row);
                        } else if (time.equals(" ")) {
                            if (row.getWebinarTitle().toLowerCase().contains(title.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }


                    }

                    filteredDataList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataList = (List<Webinar>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
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

    TextView title;
    TextView typeValue, typeTxt;
    TextView stageValue, stageTxt;
    TextView startAtValue, startAtTxt, endAtTxt;
    TextView endAtValue;
    TextView profile, matchingJobsValueTxt;
    TextView detail, companyNameTxt;
    ImageView speakerImage, companyImage;
    Button joinBtn;
    LinearLayout datesLayout;
    LinearLayoutCompat matchingLayout, profileLayout, webinarTypeLayout, webinarDetailLayout, stageLayout;
    CardView roundImageCardView, rectangleImageCardView;
    ImageView startTimeIcon, endTimeIcon, profileIcon, matchingIcon, detailIcon;

    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        title = mView.findViewById(R.id.webinarTitleTxt);
        companyNameTxt = mView.findViewById(R.id.companyNameTxt);
        typeValue = mView.findViewById(R.id.typeValueTxt);
        stageValue = mView.findViewById(R.id.stageValueTxt);
        stageLayout = mView.findViewById(R.id.stageLayout);
        startAtValue = mView.findViewById(R.id.startAtTxt);
        startAtTxt = mView.findViewById(R.id.startAt);
        endAtValue = mView.findViewById(R.id.entAtTxt);
        endAtTxt = mView.findViewById(R.id.endAt);
        stageTxt = mView.findViewById(R.id.stageTxt);
        typeTxt = mView.findViewById(R.id.typeTxt);
        profile = mView.findViewById(R.id.profileTxt);
        profileLayout = mView.findViewById(R.id.profileLayout);
        webinarTypeLayout = mView.findViewById(R.id.webinarTypeLayout);
        detail = mView.findViewById(R.id.detailTxt);
        matchingLayout = mView.findViewById(R.id.matchingLayout);
        webinarDetailLayout = mView.findViewById(R.id.webinarDetailLayout);
        datesLayout = mView.findViewById(R.id.datesLayout);
        matchingJobsValueTxt = mView.findViewById(R.id.matchingJobsValueTxt);
        speakerImage = mView.findViewById(R.id.userImageViewRound);
        companyImage = mView.findViewById(R.id.userImageViewRectangle);
        startTimeIcon = mView.findViewById(R.id.startTimeIcon);
        endTimeIcon = mView.findViewById(R.id.endTimeIcon);
        profileIcon = mView.findViewById(R.id.profileIcon);
        matchingIcon = mView.findViewById(R.id.matchingIcon);
        detailIcon = mView.findViewById(R.id.detailIcon);
        roundImageCardView = mView.findViewById(R.id.cardViewRound);
        roundImageCardView = mView.findViewById(R.id.cardViewRound);
        roundImageCardView = mView.findViewById(R.id.cardViewRound);
        rectangleImageCardView = mView.findViewById(R.id.cardViewRectangle);
        ConstraintLayout parentLayout = mView.findViewById(R.id.parentLayout);

        parentLayout.setBackgroundColor(Singleton.getCardBGColor());
        joinBtn = mView.findViewById(R.id.joinBtn);
        joinBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getClickToJoin() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaWebinar());

        startAtTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStartAt() + " :");
        endAtTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEndAt() + " :");
        stageTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStage() + " :");
        typeTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getType() + " :");

        Helper.setCardTextColor(startAtTxt);
        Helper.setCardTextColor(endAtTxt);
        Helper.setCardTextColor(stageTxt);
        Helper.setCardTextColor(typeTxt);
        Helper.setCardTextColor(matchingJobsValueTxt);
        Helper.setCardTextColor(title);
        Helper.setCardTextColor(typeValue);
        Helper.setCardTextColor(stageTxt);
        Helper.setCardTextColor(startAtValue);
        Helper.setCardTextColor(endAtValue);
        Helper.setCardTextColor(matchingJobsValueTxt);


        Singleton.changeVectorIconColor(R.drawable.clock, endTimeIcon, Singleton.getSidebarInnerTextColor());
        Singleton.changeVectorIconColor(R.drawable.clock, startTimeIcon, Singleton.getSidebarInnerTextColor());
        Singleton.changeVectorIconColor(R.drawable.user, profileIcon, Singleton.getSidebarInnerTextColor());
        Singleton.changeVectorIconColor(R.drawable.file, detailIcon, Singleton.getSidebarInnerTextColor());
        Singleton.changeVectorIconColor(R.drawable.match, matchingIcon, Singleton.getSidebarInnerTextColor());
        Helper.underLineText(profile, Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getView() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getProfile());

        Helper.underLineText(detail, Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getView() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaWebinar() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDetail());
        Helper.setButtonColorWithDrawable(joinBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());


    }

}




