package com.vrd.gsaf.home.jobs;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.api.responses.jobPercentage.Match;

import java.util.List;

public class JobMatchAdapter extends RecyclerView.Adapter<CustomViewHolderMatch> {
    private final List<Match> dataList;

    public JobMatchAdapter(List<Match> matchList) {
        this.dataList = matchList;

    }

    @Override
    public CustomViewHolderMatch onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_job_matchpercentsge, parent, false);
        return new CustomViewHolderMatch(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(CustomViewHolderMatch holder, int position) {

        holder.questionText.setText(HtmlCompat.fromHtml(dataList.get(position).getQuestion(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
        holder.percentageValueTxt.setText(dataList.get(position).getScore() + "%");
        holder.progressBar.setProgress(Integer.parseInt(dataList.get(position).getScore()));
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

    public interface onApplyClick {
        void applyToJob(int videoIndex, CustomViewHolder holder);

        void matchPercentage(int companyId);
    }


}


class CustomViewHolderMatch extends RecyclerView.ViewHolder {

    public final View mView;
    TextView questionText, percentageValueTxt;
    ProgressBar progressBar;


    CustomViewHolderMatch(View itemView) {
        super(itemView);
        mView = itemView;

        percentageValueTxt = mView.findViewById(R.id.percentageValueTxt);
        questionText = mView.findViewById(R.id.questionTxt);
        progressBar = mView.findViewById(R.id.progressBar);


    }
}


