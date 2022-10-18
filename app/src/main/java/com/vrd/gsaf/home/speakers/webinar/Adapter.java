package com.vrd.gsaf.home.speakers.webinar;

import android.content.Context;
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

import com.vrd.gsaf.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final Context context;
    public FragmentManager view;
    private List<WebinarModel> dataList;
    private long mLastClickTime = System.currentTimeMillis();


    public Adapter(Context context, FragmentManager view, int index) {
        this.context = context;
        //  this.dataList = Singleton.getInstance().getWebinarData();
        this.view = view;
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
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_webinar, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getTitle());
        holder.type.setText(dataList.get(position).getType());
        holder.stage.setText(dataList.get(position).getStage());
        holder.startAt.setText(dataList.get(position).getStartAt());
        holder.endAt.setText(dataList.get(position).getEndAt());

        holder.speakerImage.setImageDrawable(dataList.get(position).getSpeakerImage());

        holder.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                // replaceFragment(new Profile(),"speakers",index);
            }
        });


//        Picasso.Builder builder = new Picasso.Builder(context);
//        builder.downloader(new OkHttp3Downloader(context));
//        builder.build().load(dataList.get(position).getImage())
//                .placeholder((R.drawable.ic_launcher_background))
//                .error(R.drawable.ic_launcher_background)
//                .into(holder.coverImage);

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


}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    TextView title;
    TextView type;
    TextView stage;
    TextView startAt;
    TextView endAt;
    Button joinBtn;

    ImageView speakerImage, companyImage;
    CardView roundImageCardView, rectangleImageCardView;

    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        title = mView.findViewById(R.id.webinarTitleTxt);
        type = mView.findViewById(R.id.typeValueTxt);
        stage = mView.findViewById(R.id.stageValueTxt);
        startAt = mView.findViewById(R.id.startAtTxt);
        endAt = mView.findViewById(R.id.entAtTxt);

        speakerImage = mView.findViewById(R.id.userImageViewRound);
        companyImage = mView.findViewById(R.id.userImageViewRectangle);
        roundImageCardView = mView.findViewById(R.id.cardViewRound);
        rectangleImageCardView = mView.findViewById(R.id.cardViewRectangle);

        joinBtn = mView.findViewById(R.id.joinBtn);
    }
}
