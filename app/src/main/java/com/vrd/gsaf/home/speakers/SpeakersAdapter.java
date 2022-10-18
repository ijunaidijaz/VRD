package com.vrd.gsaf.home.speakers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.api.responses.speakers.Speaker;
import com.vrd.gsaf.home.conferenceAgenda.ConferenceAgenda;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;

public class SpeakersAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final ArrayList<Speaker> dataList;
    private final Context context;
    public FragmentManager view;
    private long mLastClickTime = System.currentTimeMillis();


    public SpeakersAdapter(Context context, FragmentManager view) {
        this.context = context;
        this.dataList = Singleton.getInstance().getSpeakerData();
        this.view = view;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_speaker, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.speakerName.setText(dataList.get(position).getName());
        holder.titleTxt.setText(dataList.get(position).getTitle());


        Helper.loadCircularImageFromUrl(holder.speakerImage, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getSpeakerData().get(position).getImage());

        int index = position;
//        if(!Singleton.getInstance().getUserLogin())
//            holder.webinarBtn.setVisibility(View.GONE);
        if (Singleton.getInstance().getIsLoggedIn() && Singleton.getInstance().getFairData().getFair().getOptions().getDisableWebinarsButtonSpeakersListFront().equals(1)) {
            holder.webinarBtn.setVisibility(View.GONE);
        }
        holder.webinarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (Singleton.getInstance().getIsLoggedIn()) {
                    Singleton.getInstance().setIsDashboard(true);
                    Singleton.getInstance().setSpeakerId(dataList.get(index).getId());
                    Singleton.getInstance().setFromStandToWebinar(false);
                    Singleton.getInstance().setFromSpeakersToWebinar(true);
                    Singleton.getInstance().setWebinarVideo(false);

                    replaceFragment(new ConferenceAgenda(), index);
                } else
                    Helper.showToast("Please Login");

            }
        });
        holder.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                Singleton.getInstance().setSpeakerTitle(dataList.get(index).getTitle());
                replaceFragment(new Profile(), index);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private void replaceFragment(Fragment fragment, int index) {

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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    TextView speakerName;
    ImageView speakerImage;
    TextView titleTxt;
    Button profileBtn;
    Button webinarBtn;

    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        speakerName = mView.findViewById(R.id.websiteTxt);
        speakerImage = mView.findViewById(R.id.userImageView);
        profileBtn = mView.findViewById(R.id.profileBtn);
        webinarBtn = mView.findViewById(R.id.webinarBtn);
        titleTxt = mView.findViewById(R.id.titleTextView);
        ConstraintLayout mainLayout = mView.findViewById(R.id.parentLayout);
        mainLayout.setBackgroundColor(Singleton.getCardBGColor());
        Helper.setCardTextColor(titleTxt);
        Helper.setCardTextColor(speakerName);
//        Helper.setButtonColorWithDrawable(webinarBtn,Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor());
//        Helper.setButtonColorWithDrawable(profileBtn,Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor());
//        Helper.setButtonBackgroundAndTextColor(webinarBtn,
//                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
//                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
//
//        Helper.setButtonBackgroundAndTextColor(profileBtn,
//                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
//                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
        Helper.setButtonColorWithDrawable(webinarBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
        Helper.setButtonColorWithDrawable(profileBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());


//        webinarBtn.setTextColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor()));
//        webinarBtn.setHint(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEnter());
//        GradientDrawable drawable = (GradientDrawable) webinarBtn.getBackground();
//        drawable.setColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterBackground()));
    }
}
