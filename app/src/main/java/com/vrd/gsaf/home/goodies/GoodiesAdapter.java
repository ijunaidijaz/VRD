package com.vrd.gsaf.home.goodies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.goodies.GoodieBag;
import com.vrd.gsaf.api.responses.goodies.Goodies;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodiesAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final List<GoodieBag> dataList;
    private final Context context;
    private final ProgressBar progressBar;
    public FragmentManager view;
    private long mLastClickTime = System.currentTimeMillis();


    public GoodiesAdapter(FragmentManager view, List<GoodieBag> goodieBagList, ProgressBar progressBar) {
        this.context = Singleton.getInstance().getContext();
        this.dataList = goodieBagList;
        Singleton.getInstance().setGoodiesList(dataList);
        this.view = view;
        this.progressBar = progressBar;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_goodie, parent, false);
        return new CustomViewHolder(view);
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
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        if (dataList.get(position).getIsAdded() == 0)
            holder.removeGoodieBtn.setVisibility(View.GONE);
        else if (dataList.get(position).getIsAdded() == 1)
            holder.removeGoodieBtn.setVisibility(View.VISIBLE);

        holder.goodieName.setText(dataList.get(position).getGoodiebagName());
        holder.companyName.setText(dataList.get(position).getCompanyName());
        Helper.loadRectangleImageFromUrlWithRounded(holder.companyImage, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + dataList.get(position).getCompanyLogo(), 25);

        int index = position;
        holder.viewGoodie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                replaceFragment(new GoodieDetail(), index);
            }
        });
        holder.removeGoodieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                callApiToRemoveGoodie(index, holder);
            }
        });


    }

    private void callApiToRemoveGoodie(int index, CustomViewHolder holder) {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("company_id", Singleton.getInstance().getStandCompanyId());
        JsonObject.addProperty("goodie_id", Singleton.getInstance().getGoodiesList().get(index).getGoodiebagId());
        Call<Goodies> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).addGoodieBag(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<Goodies>() {
            @Override
            public void onResponse(Call<com.vrd.gsaf.api.responses.goodies.Goodies> call, Response<Goodies> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    }
                    if (response.raw().code() == 204) {
                        Helper.showToast(response.body().getMsg());
                    } else if (response.body().getStatus()) {
//                        Helper.showToast(response.body().getMsg());
                        Singleton.getInstance().getGoodiesList().get(index).setIsAdded(0);
                        notifyDataSetChanged();

                        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
                        editor.putInt("goodieCount", response.body().getData().getCandidateTotalGoodieBag());
                        Singleton.getInstance().setGoodieCount(response.body().getData().getCandidateTotalGoodieBag());
                        editor.apply();
                        Singleton.getInstance().getMainActivity().goodieBagBadge();
                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
                    Helper.showToast("Something went wrong.Please try again.");
                }
            }

            @Override
            public void onFailure(Call<com.vrd.gsaf.api.responses.goodies.Goodies> call, Throwable t) {
                Helper.showToast("Something went wrong.Please try again.");
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


}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    TextView goodieName;

    ImageView companyImage;
    TextView companyName;
    Button viewGoodie, removeGoodieBtn;


    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        goodieName = mView.findViewById(R.id.goodieTitleTxt);
        removeGoodieBtn = mView.findViewById(R.id.removeGoodieBtn);
        companyName = mView.findViewById(R.id.companyNameTxt);
        companyImage = mView.findViewById(R.id.userImageView);
        viewGoodie = mView.findViewById(R.id.viewGoodieBtn);
        viewGoodie = mView.findViewById(R.id.viewGoodieBtn);
        ConstraintLayout parent = mView.findViewById(R.id.parentLayout);
        viewGoodie.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getView() + " " +
                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoodie());

        // Helper.setButtonColorWithDrawable(viewGoodie,Singleton.getInstance().getFairData().getFair().getOptions().getBU());
        parent.setBackgroundColor(Singleton.getCardBGColor());
        Helper.setButtonColorWithDrawable(viewGoodie, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
        Helper.setButtonColorWithDrawable(removeGoodieBtn, "#b42626",
                "#FFFFFF");

    }

}
