package com.vrd.gsaf.home.homeTab;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.util.ArrayList;
import java.util.List;


public class ButtonsAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    public FragmentManager view;
    OnShareClickedListener mCallback;
    private List<String> dataList;
    private long mLastClickTime = System.currentTimeMillis();

    public ButtonsAdapter(FragmentManager view, ArrayList<String> buttonsList) {
        if (Singleton.getInstance().getFairData().getFair().getOptions().getWebinarEnable() == 0) {
            buttonsList.remove("Webinars");
        }
        if (Singleton.getInstance().getFairData().getFair().getOptions().getEnableCourses() == 0) {
            buttonsList.remove("Courses");
        }
//        if (Singleton.getInstance().getHomeState().equalsIgnoreCase("stands")&&Singleton.getInstance().getFairData().getFair().getOptions().getHideMediaButtonCompanyStand() == 1) {
//            buttonsList.remove("Media");
//        }
        if (Singleton.getInstance().getFairData().getFair().getGoodies().equalsIgnoreCase("0")) {
            buttonsList.remove("Goodies");
        }
        if (Singleton.getInstance().getHomeState().equalsIgnoreCase("reception") && Singleton.getInstance().getFairData().getFair().getOptions().getHideExhibitorButtonReception() == 1) {
            List<String> list = new ArrayList<>();
            list.addAll(buttonsList);
            list.removeIf(it -> it.indexOf("Hall") == 0);
            buttonsList.clear();
            buttonsList.addAll(list);
        }
        this.dataList = buttonsList;
        this.view = view;

    }

    public void setItems(ArrayList<String> buttonsList) {
        this.dataList = buttonsList;
        notifyDataSetChanged();
    }

    public void setOnShareClickedListener(OnShareClickedListener mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_reception, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        if (Singleton.getInstance().getHomeState().equals("reception")) {

            Helper.setButtonColorWithDrawableAndStroke(holder.button, Singleton.getInstance().getFairData().getFair().getOptions().getReceptionButtonsBackgroundColor(), Singleton.getInstance().getFairData().getFair().getOptions().getReceptionButtonsInnerTextColor(), 5);
        } else if (Singleton.getInstance().getHomeState().equals("stands")) {
            Helper.setButtonColorWithDrawableAndStroke(holder.button, Singleton.getInstance().getFairData().getFair().getOptions().getStandButtonsBackgroundColor(), Singleton.getInstance().getFairData().getFair().getOptions().getStandButtonsInnerTextColor(), 5);

        } else if (Singleton.getInstance().getHomeState().equals("hall")) {
            Helper.setButtonColorWithDrawableAndStroke(holder.button, Singleton.getInstance().getFairData().getFair().getOptions().getMultiHallBackgroundColor(), Singleton.getInstance().getFairData().getFair().getOptions().getMultiHallFontColor(), 5);

            if (Singleton.getInstance().getActiveHall() != null && Singleton.getInstance().getActiveHall().equals(dataList.get(position))) {
                Helper.setButtonColorWithDrawableAndStroke(holder.button, Singleton.getInstance().getFairData().getFair().getOptions().getMultiHallActiveBackgroundColor(), Singleton.getInstance().getFairData().getFair().getOptions().getMultiHallActiveFontColor(), 5);

            }
        } else {
            Helper.setButtonColorWithDrawableAndStroke(holder.button, Singleton.getInstance().getFairData().getFair().getOptions().getStandButtonsBackgroundColor(), Singleton.getInstance().getFairData().getFair().getOptions().getStandButtonsInnerTextColor(), 5);


        }


        int index = position;
        holder.button.setText(dataList.get(position));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                // if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(index).getKeywords().getExhibitorsHall())) {
                //mCallback.ShareClicked("hall", 1);
                //  }
                if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMedia())) {
                    mCallback.ShareClicked("media", 1);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getAbout())) {
                    mCallback.ShareClicked("about", 1);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDocuments())) {
                    mCallback.ShareClicked("documents", 1);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getRecruiters())) {
                    mCallback.ShareClicked("recruiter", 1);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaJobs())) {
                    mCallback.ShareClicked("jobs", 1);
                    Singleton.getInstance().setIsDashboard(true);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaWebinars())) {
                    mCallback.ShareClicked("webinars", 1);
                    Singleton.getInstance().setIsDashboard(true);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getFairMetaInterviews())) {
                    mCallback.ShareClicked("interview", 1);
                    Singleton.getInstance().setIsDashboard(true);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getSupport())) {
                    mCallback.ShareClicked("support", 1);
                    Singleton.getInstance().setIsDashboard(true);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getReception())) {
                    mCallback.ShareClicked("reception", 1);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getChat())) {
                    mCallback.ShareClicked("chat", 1);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getCourses())) {
                    mCallback.ShareClicked("courses", 1);
                } else if (holder.button.getText().equals(Singleton.getKeywords().getConferenceAgenda())) {
                    mCallback.ShareClicked("conferenceAgenda", 1);
                } else if (holder.button.getText().equals(Singleton.getKeywords().getStage() + "\n" + Singleton.getKeywords().getConferenceAgenda())) {
                    mCallback.ShareClicked("stageConferenceAgenda", 1);
                } else if (holder.button.getText().equals(Singleton.getKeywords().getSpeakers_agenda())) {
                    mCallback.ShareClicked("speakersAgenda", 1);
                }else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getHall()) || holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMainHall())) {
                    mCallback.ShareClicked("hall", 1);

                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getStandPolling())) {
                    mCallback.ShareClicked("standPoll", 1);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoodies())) {
                    mCallback.ShareClicked("goodie", 1);
                    Singleton.getInstance().setGoodies(true);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getMatchingSlots())) {
                    mCallback.ShareClicked("matching slots", 1);
                } else if (holder.button.getText().equals(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getHome())) {
                    mCallback.ShareClicked("home", 1);
                    //  Singleton.getInstance().setGoodies(true);
                } else if (Singleton.getInstance().getFairHalls().size() > 0) {
                    for (int i = 0; i < Singleton.getInstance().getFairHalls().size(); i++) {
                        if (holder.button.getText().equals(Singleton.getInstance().getFairHalls().get(i).getHallName())) {
                            Singleton.getInstance().setHallName(Singleton.getInstance().getFairHalls().get(i).getHallName());
                            Singleton.getInstance().setHallId(Singleton.getInstance().getFairHalls().get(i).getHallId());
                            //  Singleton.getInstance().setHallId("1");
                            mCallback.ShareClicked("hall", 1);
                            Singleton.getInstance().setActiveHall(holder.button.getText().toString());
                            break;
                        }
                    }
                }

            }
        });
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
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnShareClickedListener {
        void ShareClicked(String url, int videoIndex);
    }


}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    Button button;

    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        button = mView.findViewById(R.id.btn);

    }
}


