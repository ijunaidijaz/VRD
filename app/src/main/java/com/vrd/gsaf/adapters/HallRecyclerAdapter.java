package com.vrd.gsaf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vrd.gsaf.R;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

public class HallRecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private final FragmentManager view;

    public HallRecyclerAdapter(FragmentManager view) {
        this.view = view;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Helper.lockOrientation();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_hall_recycler, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        if (Singleton.getInstance().getHallModelArrayList().get(position).getType().equals("Silver")) {
            Glide.with(Singleton.getInstance().getContext()).
                    load(R.drawable.silver_stand2)
                    .placeholder(R.drawable.rectangluar_placeholder)
                    .into(holder.hallImageView);
        } else if (Singleton.getInstance().getHallModelArrayList().get(position).getType().equals("Gold")) {
            Glide.with(Singleton.getInstance().getContext()).
                    load(R.drawable.gold_stand2)
                    .placeholder(R.drawable.rectangluar_placeholder)
                    .into(holder.hallImageView);
        } else if (Singleton.getInstance().getHallModelArrayList().get(position).getType().equals("Bronze")) {
            Glide.with(Singleton.getInstance().getContext()).
                    load(R.drawable.bronze_stand2)
                    .placeholder(R.drawable.rectangluar_placeholder)
                    .into(holder.hallImageView);
        }

        Helper.loadImageWithGlide(holder.companyImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getHallModelArrayList().get(position).getCompanyLogo());


        int index = position;

        holder.companyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Singleton.getInstance().getHome().HallViewPagerClick2(index);
                Singleton.getInstance().getHome().prepareMainVideoPlayer(AppConstants.getHall1BackgroundVideo(), true, "standBackground");
                Singleton.getInstance().setStandIndex(index);
            }
        });
    }

    public void clearData() {
//        dataList.clear();
//        dataList=Singleton.getInstance().getHallModelArrayList();
//        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return Singleton.getInstance().getHallModelArrayList().size();
    }
}

class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;
    ImageView hallImageView, companyImageView;

    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        hallImageView = mView.findViewById(R.id.hallImageView);
        companyImageView = mView.findViewById(R.id.companyImageView);


    }
}


