package com.vrd.gsaf.home.speakers.webinar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.vrd.gsaf.R;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;


public class Webinar extends Fragment implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    public static boolean isViewClicked = false;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView;
    private int index;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private long mLastClickTime;
    private ShimmerFrameLayout shimmerFrameLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_webinar, container, false);
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();
        progressBar = view.findViewById(R.id.progressBar);
        backImageView = view.findViewById(R.id.backImageView);
        recyclerView = view.findViewById(R.id.recyclerView);
        index = getArguments().getInt("index");
        setClickListeners();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        backImageView.setOnClickListener(this);
        manageRecyclerView();
    }

    private void manageRecyclerView() {

        adapter = new Adapter(Singleton.getInstance().getContext(), getParentFragmentManager(), index);

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


    private void startTimer() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                isViewClicked = false;
            }
        }, MIN_CLICK_INTERVAL);

    }

    @Override
    public void onClick(View view) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;

        mLastClickTime = currentClickTime;

        if (elapsedTime <= MIN_CLICK_INTERVAL)
            return;
        if (!isViewClicked) {
            isViewClicked = true;
            startTimer();
        } else {
            return;
        }

        switch (view.getId()) {
            case R.id.backImageView:
                onBackPress();
                break;
        }
    }

    private void onBackPress() {
        Helper.hideKeyboard();
        FragmentManager fm = getParentFragmentManager();
        getActivity().getSupportFragmentManager().popBackStack();
    }

}