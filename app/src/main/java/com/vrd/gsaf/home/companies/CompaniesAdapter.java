package com.vrd.gsaf.home.companies;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.responses.companies.Company;
import com.vrd.gsaf.home.companies.companyStand.CompnayStand;
import com.vrd.gsaf.home.jobs.CompanyDetail;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;
import java.util.List;

public class CompaniesAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final Context context;
    public FragmentManager view;
    private ArrayList<Company> dataList = new ArrayList<>();
    private long mLastClickTime = System.currentTimeMillis();

    public CompaniesAdapter(FragmentManager view) {
        this.context = Singleton.getInstance().getContext();
        this.dataList = Singleton.getInstance().getCompaniesData();
        this.view = view;

    }

    public CompaniesAdapter(FragmentManager view, ArrayList<Company> dataList, Boolean addMode) {
        this.context = Singleton.getInstance().getContext();
        if (addMode) {
            this.dataList.addAll(dataList);
        }

        this.view = view;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_companies, parent, false);
        return new CustomViewHolder(view);


    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.companyName.setText(dataList.get(position).getCompanyName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterInside(), new RoundedCorners(25));

        Glide.with(Singleton.getInstance().getContext()).
                load(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + dataList.get(position).getCompanyLogo())
                .placeholder(R.drawable.rectangluar_placeholder)
                .apply(requestOptions)
                .into(holder.companyImage);
//            Glide.with(Singleton.getInstance().getContext()).
//                    load(R.drawable.demo7)
//                    .placeholder(R.drawable.company)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(holder.companyImage);


        int index = position;
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (Singleton.getInstance().getLive() && Singleton.getInstance().getIsLoggedIn()) {
                    replaceFragment(new CompnayStand(), null, index);
                    //  replaceFragment(new Home(), null, index);
                    //  Singleton.getInstance().setFromCompaniesToStand(true);
                } else {
                    replaceFragment(new CompanyDetail(), null, index);
                }


            }
        });


    }

    private void replaceFragment(Fragment fragment, String tag, int index) {

        Singleton.getInstance().setStandCompanyId(Singleton.getInstance().getCompaniesData().get(index).getCompanyId());
        FragmentManager fm = view;
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
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


    public void clearData() {
        if (dataList != null) {
            dataList.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<Company> companyList) {
        this.dataList.addAll(companyList);
        notifyItemInserted(companyList.size() - 1);
    }
}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    TextView companyName;
    ImageView companyImage;

    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        companyName = mView.findViewById(R.id.emailTextView);
        companyName.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCompanyName());
        companyImage = mView.findViewById(R.id.companyImageView);
        Helper.setTextColor(companyName);
    }
}



