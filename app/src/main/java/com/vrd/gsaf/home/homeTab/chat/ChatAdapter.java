package com.vrd.gsaf.home.homeTab.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.responses.receptionist.Data;
import com.vrd.gsaf.api.responses.receptionist.Rec;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

public class ChatAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final Data dataList;
    private final Context context;
    public FragmentManager view;
    private long mLastClickTime = System.currentTimeMillis();


    public ChatAdapter(Context context, FragmentManager view) {
        this.context = context;
        this.dataList = Singleton.getInstance().getReceptionistDetails();
        this.view = view;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_chat, parent, false);
        return new CustomViewHolder(view);
    }


    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Rec rec = dataList.getRecList().get(position);
        holder.name.setText(dataList.getRecList().get(position).getReceptionName());
        holder.titleTxt.setText(dataList.getRecList().get(position).getReceptionName());
//        holder.cardView.setBackgroundTintList(MainApp.getAppContext().getColorStateList(R.color.colorPrimary));
        Glide.with(Singleton.getInstance().getContext()).
                load(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + dataList.getRecList().get(position).getReceptionImage())
                .apply(RequestOptions.circleCropTransform()).
                into(holder.userImageView);

        int index = position;
        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;

                Intent messageIntent = new Intent(Singleton.getInstance().getContext(), CometChatMessageListActivity.class);
                messageIntent.putExtra("name", dataList.getRecList().get(index).getReceptionName());
                messageIntent.putExtra("uid", Singleton.getInstance().getFairData().getFair().getId() + "f" + dataList.getRecList().get(index).getReceptionId());
                messageIntent.putExtra("type", "user");
                Singleton.getInstance().getContext().startActivity(messageIntent);

            }
        });
        holder.emailBTn.setOnClickListener(view1 -> {
            long now = System.currentTimeMillis();
            if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                return;
            }
            mLastClickTime = now;

            if (dataList.getRecList().get(index).getReception_email() != "")
                Helper.openEmail(dataList.getRecList().get(index).getReception_email());
            else
                Helper.showToast("Email Not available");

        });

        GradientDrawable drawable = (GradientDrawable) holder.statusImageView.getBackground();
        if (rec.getReceptionStatus().equalsIgnoreCase("0")) {
            drawable.setColor(Color.parseColor("#cc3333"));
            holder.chatBtn.setVisibility(View.GONE);
        } else if (rec.getReceptionStatus().equalsIgnoreCase("1")) {
            drawable.setColor(Color.parseColor("#8cc835"));
        } else if (rec.getReceptionStatus().equalsIgnoreCase("2")) {
            drawable.setColor(Color.parseColor("#f5a027"));
            holder.busyTxt.setVisibility(View.VISIBLE);
            holder.chatBtn.setVisibility(View.GONE);
            holder.busyTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTrans_break_receptionists());
        } else {
            holder.chatBtn.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getEnable_break_receptionists_front().equals(1)) {
            drawable.setColor(Color.parseColor("#f5a027"));
            holder.busyTxt.setVisibility(View.VISIBLE);
            holder.chatBtn.setVisibility(View.GONE);
            holder.busyTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getTrans_break_receptionists());

        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatFront().equals(1)) {
            holder.chatBtn.setVisibility(View.GONE);
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getDisableChatButton().equals(1)) {
            holder.chatBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.getRecList().size();
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

    TextView name;
    ImageView userImageView, statusImageView;
    TextView titleTxt, busyTxt;
    Button chatBtn, emailBTn;
    CardView cardView;

    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        name = mView.findViewById(R.id.nameTxt);
        statusImageView = mView.findViewById(R.id.statusImageView);
        cardView = mView.findViewById(R.id.cardView);
        userImageView = mView.findViewById(R.id.userImageView);
        chatBtn = mView.findViewById(R.id.chatBtn);
        busyTxt = mView.findViewById(R.id.busyTxt);
        emailBTn = mView.findViewById(R.id.emailBtn);
        titleTxt = mView.findViewById(R.id.titleTextView);
        chatBtn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getChat());
        emailBTn.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getEmail());
        Helper.setButtonColorWithDrawable(chatBtn,
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
        Helper.setButtonColorWithDrawable(emailBTn,
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());

    }
}
