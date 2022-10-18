package com.vrd.gsaf.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.vrd.gsaf.R;
import com.vrd.gsaf.utility.Helper;


public class ExitSurvey extends Fragment implements View.OnClickListener {

    private View view;
    private ProgressBar progressBar;
    private ImageView menuImageView;

    @Override
    public void onResume() {
        super.onResume();
        try {
            Helper.setActiveItemBottomNav(0);

        } catch (Exception e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        Helper.lockOrientation();

        view = inflater.inflate(R.layout.fragment_exit_survey, container, false);
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initializeViews() {
        progressBar = view.findViewById(R.id.progressBar);
        menuImageView = view.findViewById(R.id.menuImageView);
        setClickListeners();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners() {
        menuImageView.setOnClickListener(this);
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

    private void onBackPress() {
        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                onBackPress();
                break;
            case R.id.menuImageView:
                Helper.menuClick();
                break;
        }
    }
}