package com.vrd.gsaf.home.dashboard.recruiters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.api.responses.dashboardRecruiters.Recruiter;
import com.vrd.gsaf.home.companies.companyStand.CompnayStand;
import com.vrd.gsaf.home.dashboard.recruiters.interviewRequest.InterviewRequest;
import com.vrd.gsaf.home.jobs.CompanyDetail;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.List;

public class RecruiterAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final List<Recruiter> dataList;
    public FragmentManager view;
    onMatch mCallback;
    private Context context;
    private long mLastClickTime = System.currentTimeMillis();


    public RecruiterAdapter(FragmentManager view, onMatch onApplyClick) {
        this.dataList = Singleton.getInstance().getRecruiterArrayList();
        this.view = view;
        this.mCallback = onApplyClick;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_recruiters, parent, false);
        return new CustomViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        if (Singleton.getInstance().getLive()) {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatFront() == 0 && Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatButton() == 0 && dataList.get(position).getIsChatEnable().equals("0")) {
                if (dataList.get(position).getRecruiterStatus().equals("online")) {
                    holder.chatBtn.setVisibility(View.VISIBLE);
                }
            }
        }
        if (Singleton.getInstance().getHomeState() != null && Singleton.getInstance().getHomeState().equals("stands")) {
            holder.companyValue.setVisibility(View.GONE);
        }

        holder.companyValue.setPaintFlags(holder.companyValue.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        if (!dataList.get(position).getRecruiterStatus().equals("online")) {
            if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableBreakRecruiters() != 0) {
                holder.emailBtn.setVisibility(View.VISIBLE);
                holder.chatBtn.setVisibility(View.GONE);
            }
        }

        GradientDrawable drawable = (GradientDrawable) holder.statusImageView.getBackground();

        if (dataList.get(position).getRecruiterStatus().equals("online")) {
            drawable.setColor(Color.parseColor("#8cc835"));
            holder.chatBtn.setVisibility(View.VISIBLE);
            holder.emailBtn.setVisibility(View.GONE);
        }
        if (dataList.get(position).getRecruiterStatus().equalsIgnoreCase("busy")) {
            drawable.setColor(Color.parseColor("#f5a027"));
            holder.busyTxt.setVisibility(View.VISIBLE);
            holder.busyTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getBusy());
            holder.chatBtn.setVisibility(View.GONE);
            holder.emailBtn.setVisibility(View.VISIBLE);
        }
        if (dataList.get(position).getRecruiterStatus().equals("offline")) {
            drawable.setColor(Color.parseColor("#cc3333"));

        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableBreakRecruiters() != 0) {
            drawable.setColor(Color.parseColor("#f5a027"));
            holder.busyTxt.setVisibility(View.VISIBLE);
            holder.busyTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getRecruiterBreakTrans());
        }

        holder.locationValue.setText(dataList.get(position).getLocation());
        holder.recruiterTitleTxt.setText(dataList.get(position).getTitle());
        holder.companyValue.setText(dataList.get(position).getCompany().getCompanyName());
        holder.companyValue.setPaintFlags(holder.companyValue.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.recruiterName.setText(dataList.get(position).getName());
        holder.emailText.setText(dataList.get(position).getPublicEmail());
        if (dataList.get(position).getPublicEmail() != null) {
            holder.emailLayout.setVisibility(View.GONE);
        }
        holder.matchValue.setText(dataList.get(position).getPercentage() + "%");
        if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableBreakRecruiters() != 0) {
            holder.emailBtn.setVisibility(View.VISIBLE);
            holder.chatBtn.setVisibility(View.GONE);
        }
        if (!dataList.get(position).getRecruiterStatus().equals("online")) {
            holder.emailBtn.setVisibility(View.VISIBLE);
        }
        int index = position;

        if (!dataList.get(position).getLinkedin().equals("")) {
            holder.linkedInLayout.setVisibility(View.VISIBLE);
            holder.viewLinkedInProfileTxt.setPaintFlags(holder.viewLinkedInProfileTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            holder.linkedInLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Helper.viewDocument(dataList.get(index).getLinkedin());
//                    Helper.goToUrl(dataList.get(index).getLinkedin());
                    //   goToUrl(dataList.get(index).getLinkedin());
                }
            });
        }
        holder.interviewRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                replaceFragment(new InterviewRequest(), index);
            }
        });

        holder.companyValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;


                if (Singleton.getInstance().getLive() && Singleton.getInstance().getIsLoggedIn())
                    replaceFragment(new CompnayStand(), index);

                else {
                    replaceFragment(new CompanyDetail(), index);
                }

                // replaceFragment(new CompanyDetail(), 1);
            }
        });

        holder.viewProfileTxt.setPaintFlags(holder.viewProfileTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        holder.viewProfileTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                openProfileDialog(index);
            }
        });

        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                Helper.startUserChat(dataList.get(index).getName(), Singleton.getInstance().getFairData().getFair().getId() + "f" + dataList.get(index).getRecruiterId(), "user");


            }
        });


        holder.emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;

                if (dataList.get(index).getPublicEmail() != "")
                    Helper.openEmail(dataList.get(index).getPublicEmail());
                else
                    Helper.showToast("Email Not available");


            }
        });
        Helper.loadCircularImageFromUrl(holder.companyImage, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + dataList.get(position).getRecruiterImg());
        //  Log.e(String.valueOf(index),Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl()+dataList.get(position).getRecruiterImg());

        if (Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("0")
                || Singleton.getInstance().getFairData().getFair().getOptions().getDisableMatchPercentageRecListFront().equals(1)) {
            holder.matchingLayout.setVisibility(View.GONE);
        }
        holder.matchingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.matchPercentage(Integer.parseInt(dataList.get(index).getRecruiterId()));

            }
        });

        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableEmailButton().equals(1)) {
            holder.emailBtn.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableLocationRecListFront().equals(1)) {
            holder.locationLayout.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableRecruiterViewProfileFront() != 1) {
            holder.profileLayout.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisable_interview_request_button_recruiters_list_front() == 1) {
            holder.interviewRequestBtn.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatFront().equals(1)) {
            holder.chatBtn.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatButton().equals(1)) {
            holder.chatBtn.setVisibility(View.GONE);
        }
    }

    private void goToUrl(String url) {
        if (!url.equals("")) {
            try {
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                Singleton.getInstance().getContext().startActivity(launchBrowser);
            } catch (Exception e) {

            }
        } else
            Helper.showToast("Not Available");
    }

    private void openProfileDialog(int index) {
        Recruiter recruiter = Singleton.getInstance().getRecruiterArrayList().get(index);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_speaker_profile, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        ImageView cancel = dialogView.findViewById(R.id.cancelImageView);
        ConstraintLayout mainlayout = dialogView.findViewById(R.id.parentLayout);
        ImageView userImageView = dialogView.findViewById(R.id.userImageView);
        Helper.loadCircularImageFromUrl(userImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getRecruiterArrayList().get(index).getRecruiterImg());
        TextView description = dialogView.findViewById(R.id.descriptionTxt);
        if (recruiter.getRecruiter_info() != null)
            Helper.loadHtml(description, Singleton.getInstance().getRecruiterArrayList().get(index).getRecruiter_info());
        TextView userDesignationTxt = dialogView.findViewById(R.id.userDesignationTxt);
        userDesignationTxt.setText(Singleton.getInstance().getRecruiterArrayList().get(index).getPublicEmail());
        TextView userNameTxt = dialogView.findViewById(R.id.userNameTxt);
        userNameTxt.setText(Singleton.getInstance().getRecruiterArrayList().get(index).getName());
        mainlayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void replaceFragment(Fragment fragment, int index) {

        Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getRecruiterArrayList().get(index).getCompany().getCompanyId());

        FragmentManager fm = view;
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fm.beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface onMatch {

        void matchPercentage(int companyId);
    }
}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    TextView recruiterName, busyTxt, emailText;
    TextView viewLinkedInProfileTxt, recruiterTitleTxt, matchValue, matchTxt, locationTxt, locationValue, companyValue, companyTxt, viewProfileTxt;

    ImageView companyImage, statusImageView;
    Button interviewRequestBtn, chatBtn, emailBtn;
    LinearLayoutCompat linkedInLayout, matchingLayout, locationLayout, profileLayout, emailLayout;

    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        matchingLayout = mView.findViewById(R.id.matchingLayout);
        busyTxt = mView.findViewById(R.id.busyTxt);
        matchTxt = mView.findViewById(R.id.matchTxt);
        emailText = mView.findViewById(R.id.emailTxt);
        statusImageView = mView.findViewById(R.id.statusImageView);
        linkedInLayout = mView.findViewById(R.id.linkedInLayout);
        locationLayout = mView.findViewById(R.id.locationLayout);
        profileLayout = mView.findViewById(R.id.profileLayout);
        viewLinkedInProfileTxt = mView.findViewById(R.id.viewLinkedInProfileTxt);
        recruiterTitleTxt = mView.findViewById(R.id.recruiterTitleTxt);
        viewProfileTxt = mView.findViewById(R.id.viewProfileTxt);
        emailLayout = mView.findViewById(R.id.emailLayout);
        locationTxt = mView.findViewById(R.id.locationTxt);
        matchTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMatch() + " :");
        viewProfileTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getView() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getProfile());
        locationTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getLocation() + " :");

        locationValue = mView.findViewById(R.id.locationValueTxt);
        matchValue = mView.findViewById(R.id.matchValueTxt);
        companyValue = mView.findViewById(R.id.companyNameTxt);
        recruiterName = mView.findViewById(R.id.recruiterNameTxt);
        companyImage = mView.findViewById(R.id.companyImageView);

        interviewRequestBtn = mView.findViewById(R.id.requestInterviewBtn);
        chatBtn = mView.findViewById(R.id.chatBtn);
        emailBtn = mView.findViewById(R.id.emailBtn);
        LinearLayoutCompat mainLayout = mView.findViewById(R.id.parentLayout);
        mainLayout.setBackgroundColor(Singleton.getCardBGColor());

        chatBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getChat());
        emailBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEmail());
        interviewRequestBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getInterviewRequest());
        viewLinkedInProfileTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getViewLinkedinProfile());
        busyTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTheHiringManagerBusy());


//        Helper.setButtonColorWithDrawable(interviewRequestBtn, Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor());
//        Helper.setButtonColorWithDrawable(chatBtn, Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor());
//        Helper.setButtonColorWithDrawable(emailBtn, Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor());


        Helper.setButtonColorWithDrawable(interviewRequestBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());

        Helper.setButtonColorWithDrawable(chatBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());

        Helper.setButtonColorWithDrawable(emailBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());


    }
}


