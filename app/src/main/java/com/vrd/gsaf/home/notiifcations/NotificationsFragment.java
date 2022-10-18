package com.vrd.gsaf.home.notiifcations;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;


public class NotificationsFragment extends Fragment implements View.OnClickListener {

    RelativeLayout toolbar;
    ConstraintLayout mainLayout;
    private View view;
    private ProgressBar progressBar;
    private ImageView menuImageView, backImageView;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private TextView titleTxt;

    @Override
    public void onResume() {
        super.onResume();
        // Helper.setActiveItemBottomNav(1);

        try {

        } catch (Exception e) {
            Helper.showToast("waqas");
        }

    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onDestroy() {
        Helper.setActiveItemBottomNav(0);

        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        //  Helper.setActiveItemBottomNav(0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();
        view = inflater.inflate(R.layout.fragment_notiifactions, container, false);
        Singleton.getInstance().setHomeState("notifications");
        Helper.hideKeyboard();
        initializeViews();
        Singleton.setNotiifactions(this);
        // Helper.setActiveItemBottomNav(1);
        Helper.noDataFound(view);

        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        titleTxt = view.findViewById(R.id.titleTxt);
        backImageView = view.findViewById(R.id.backImageView);
        titleTxt.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getNotifications());
        progressBar = view.findViewById(R.id.progressBar);
        menuImageView = view.findViewById(R.id.menuImageView);
        recyclerView = view.findViewById(R.id.recyclerView);
        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.mainLayout);
        toolbar.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getTopnavBackgroundColor()));
        mainLayout.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getSidebarBackgroundColor()));

        setClickListeners();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        menuImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);
//        manageRecyclerView();
    }

    private void manageRecyclerView() {

        NotificationModel model = new NotificationModel("Lorem Ipsum Dollar", "Nam et ante quis dolor elementum \n" +
                "semper. Nullam condtum feu.", ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.round_icon3));
        Singleton.getInstance().setNotificationDataData(model);


        model = new NotificationModel("Lorem Ipsum Dollar", "Nam et ante quis dolor elementum \n" +
                "semper. Nullam condtum feu.", ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.round_icon3));
        Singleton.getInstance().setNotificationDataData(model);

        model = new NotificationModel("Lorem Ipsum Dollar", "Nam et ante quis dolor elementum \n" +
                "semper. Nullam condtum feu.", ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.round_icon3));
        Singleton.getInstance().setNotificationDataData(model);

        model = new NotificationModel("Lorem Ipsum Dollar", "Nam et ante quis dolor elementum \n" +
                "semper. Nullam condtum feu.", ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.round_icon3));
        Singleton.getInstance().setNotificationDataData(model);

        model = new NotificationModel("Lorem Ipsum Dollar", "Nam et ante quis dolor elementum \n" +
                "semper. Nullam condtum feu.", ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.round_icon3));
        Singleton.getInstance().setNotificationDataData(model);

        model = new NotificationModel("Lorem Ipsum Dollar", "Nam et ante quis dolor elementum \n" +
                "semper. Nullam condtum feu.", ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.round_icon3));
        Singleton.getInstance().setNotificationDataData(model);

        model = new NotificationModel("Lorem Ipsum Dollar", "Nam et ante quis dolor elementum \n" +
                "semper. Nullam condtum feu.", ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.round_icon3));
        Singleton.getInstance().setNotificationDataData(model);

        model = new NotificationModel("Lorem Ipsum Dollar", "Nam et ante quis dolor elementum \n" +
                "semper. Nullam condtum feu.", ContextCompat.getDrawable(Singleton.getInstance().getContext(), R.drawable.round_icon3));
        Singleton.getInstance().setNotificationDataData(model);


        adapter = new NotificationAdapter(getParentFragmentManager());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.getInstance().getContext()));

        recyclerView.setAdapter(adapter);
    }

    private void replaceFragment(Fragment fragment, String tag) {

        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack();
        fm.popBackStack();
        fm.popBackStack();
        fm.beginTransaction().replace(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuImageView:
                Helper.menuClick();
                break;

        }
    }

}