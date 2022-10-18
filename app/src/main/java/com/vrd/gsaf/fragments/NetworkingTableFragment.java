package com.vrd.gsaf.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.vrd.gsaf.R;
import com.vrd.gsaf.adapters.NetworkingTablesAdapter;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.callbacks.NetworkingTablesCallback;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.model.GetTablesResponse;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkingTableFragment extends Fragment implements NetworkingTablesCallback {
    RecyclerView tablesRV;
    TextView title;
    ProgressBar progressBar;
    NetworkingTablesAdapter adapter;
    RelativeLayout toolbar;
    RelativeLayout mainLayout;
    ImageView menuImageView;
    View view;

    public static NetworkingTableFragment newInstance() {
        return new NetworkingTableFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_networking_table, container, false);
        initViews(view);
        getTables();
        return view;
    }

    private void initViews(View view) {
        title = view.findViewById(R.id.titleTxt);
        tablesRV = view.findViewById(R.id.tablesRV);
        progressBar = view.findViewById(R.id.progressBar);
        toolbar = view.findViewById(R.id.toolbar);
        menuImageView = view.findViewById(R.id.menuImageView);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));
        title.setTextColor(Singleton.getTopNavInnerTextColor());
        title.setText(Singleton.getKeywords().getNetworking_tables());

        menuImageView.setOnClickListener(view1 -> {
            Helper.menuClick();
        });
    }

    private void getTables() {
        Helper.showProgressBar(progressBar);
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairData().getFair().getId());
        JsonObject.addProperty("candidate_id", Singleton.getInstance().getLoginData().getUser().getId());
        JsonObject.addProperty("timezone", TimeZone.getDefault().getID());
        Call<GetTablesResponse> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).getTables(Singleton.getInstance().getLanguage(), "application/json", "Bearer " + Singleton.getInstance().getLoginData().getAccessToken(), AppConstants.APP_ID, AppConstants.APP_KEY, JsonObject);
        call.enqueue(new Callback<GetTablesResponse>() {
            @Override
            public void onResponse(Call<GetTablesResponse> call, Response<GetTablesResponse> response) {
                Helper.hideProgressBar(progressBar);
                Helper.hideLayouts(getView());
                try {
                    if (response.raw().code() == 401) {
                        Helper.showToast("Session Expired");
                        Helper.clearUserData();
                    } else if (response.raw().code() == 204) {
                        Helper.noDataFound(view);
                    } else if (response.body().getStatus()) {
                        setAdapter(response.body());

                    }
                } catch (Exception e) {
                    Helper.somethingWentWrong(getView());

                }
            }

            @Override
            public void onFailure(Call<GetTablesResponse> call, Throwable t) {
                Helper.failureResponse(progressBar, getView());

            }


        });


    }

    private void setAdapter(GetTablesResponse body) {
        if (body.getData() != null && body.getData().getNetworkingTables() != null && !body.getData().getNetworkingTables().isEmpty()) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            tablesRV.setLayoutManager(linearLayoutManager);
            adapter = new NetworkingTablesAdapter(getContext(), body.getData().getNetworkingTables(), this);
            tablesRV.setAdapter(adapter);
        } else Helper.noDataFound(view);
    }


    @Override
    public void onTableClick(int companyId) {

    }
}