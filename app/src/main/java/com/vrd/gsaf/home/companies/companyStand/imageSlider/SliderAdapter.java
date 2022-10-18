package com.vrd.gsaf.home.companies.companyStand.imageSlider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.vrd.gsaf.R;
import com.vrd.gsaf.home.homeTab.Home;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    static final int MIN_DISTANCE = 150;
    private final Home home;
    public ProgressBar progressBar;
    private Context context;
    private List<SliderItems> mSliderItems = new ArrayList<>();
    private float x1, x2;


    public SliderAdapter(Context contextm, ArrayList<SliderItems> data, Home home, ProgressBar progressBar) {
        this.context = context;
        this.mSliderItems = data;
        this.home = home;
        this.progressBar = progressBar;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate;
        if (Singleton.getInstance().getHomeState().equals("hall")) {
            inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_hall_image_slider, null);
        } else
            inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_stand_image_slider, null);

        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        try {
            SliderItems sliderItem = mSliderItems.get(position);

            if (Singleton.getInstance().getHomeState().equals("hall")) {
//                DisplayMetrics displaymetrics = new DisplayMetrics();
//                Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//                Singleton.getInstance().getHome().hallViewPager.getLayoutParams().height = displaymetrics.heightPixels / 2;
                viewHolder.imageViewBackground.setImageResource(sliderItem.getStaticImage());


                if (sliderItem.getDescription().equals("Bronze")) {
                    viewHolder.companyImageView2.setVisibility(View.VISIBLE);
                    viewHolder.companyImageView.setVisibility(View.GONE);
                    Helper.loadRectangleImageFromUrlWithRounded(viewHolder.companyImageView2, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getStandsDataAgainstHall().get(position).getCompanyStandImage(), 0);
                } else {
                    viewHolder.companyImageView.setVisibility(View.VISIBLE);
                    viewHolder.companyImageView2.setVisibility(View.GONE);
                    Helper.loadRectangleImageFromUrlWithRounded(viewHolder.companyImageView, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + Singleton.getInstance().getStandsDataAgainstHall().get(position).getCompanyStandImage(), 0);
                }

            } else if (Singleton.getInstance().getHomeState().equals("stands")) {
                Helper.loadRectangleImageFromUrlWithRounded(viewHolder.imageViewBackground, sliderItem.getImageUrl(), 6);


            } else {
                Helper.loadRectangleImageFromUrlWithRounded(viewHolder.imageViewBackground, sliderItem.getImageUrl(), 6);


            }


            //viewHolder.itemView.visi

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //    Toast.makeText(Singleton.getInstance().getContext(), "This is item in position " + position, Toast.LENGTH_SHORT).show();
                    // if(sliderItem.getImageUrl()==null) {
                    // home.HallViewPagerClick(position);
                    //  home.prepareMainVideoPlayer(AppConstants.HALL_BACKGROUND_VIDEO, true, "standBackground");

                    // Singleton.getInstance().setStandIndex(position);
                    //  }

                }
            });


        } catch (Exception e) {
//                Helper.showToast("Slider Adpater");
        }
    }


    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground, companyImageView, companyImageView2;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            if (Singleton.getInstance().getHomeState().equals("hall")) {
                companyImageView = itemView.findViewById(R.id.companyImageView);
                companyImageView2 = itemView.findViewById(R.id.companyImageView2);
            }
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}