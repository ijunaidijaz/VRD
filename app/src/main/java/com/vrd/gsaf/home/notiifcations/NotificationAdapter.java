package com.vrd.gsaf.home.notiifcations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private final List<NotificationModel> dataList;
    public FragmentManager view;

    public NotificationAdapter(FragmentManager view) {
        this.dataList = Singleton.getInstance().getNotificationData();
        this.view = view;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_notificaitons, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.heading.setText(dataList.get(position).getHeading());
        holder.description.setText(dataList.get(position).getDescription());
        holder.imageView.setImageDrawable(dataList.get(position).getImage());


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
}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    TextView heading;
    TextView description, joinButton;
    ImageView imageView;


    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        heading = mView.findViewById(R.id.headingTxt);
        RelativeLayout cardView = mView.findViewById(R.id.mainLayout);
        description = mView.findViewById(R.id.descriptionTxt);
        imageView = mView.findViewById(R.id.imageView);
        joinButton = mView.findViewById(R.id.joinBtn);
        cardView.setBackgroundColor(Singleton.getCardBGColor());
        Helper.setTextColor(heading);
        Helper.setTextColor(description);
        Helper.setTextColor(joinButton);

    }
}

