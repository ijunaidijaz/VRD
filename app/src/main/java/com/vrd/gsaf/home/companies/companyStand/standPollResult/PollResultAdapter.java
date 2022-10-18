package com.vrd.gsaf.home.companies.companyStand.standPollResult;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.responses.standPoll.Poll;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;
import com.vrd.gsaf.utility.MyPercentFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PollResultAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private static final long CLICK_TIME_INTERVAL = 500;
    private final List<Poll> dataList;
    private final Context context;
    private final long mLastClickTime = System.currentTimeMillis();


    public PollResultAdapter(List<Poll> pollList) {
        this.context = Singleton.getInstance().getContext();
        this.dataList = pollList;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_stand_poll_result, parent, false);
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
        try {
            if (!dataList.get(position).getPollType().equals("text")) {
                holder.questionTxt.setVisibility(View.VISIBLE);
                holder.questionTxt.setText(dataList.get(position).getPollQuestion());
                if (dataList.get(position).getPollGraphType().equals("pie") || dataList.get(position).getPollGraphType().equals("donut"))
                    pieGraph(holder, position);
                else if (dataList.get(position).getPollGraphType().equals("basicbar"))
                    barGraph(holder, position);
                else if (dataList.get(position).getPollType().equals("rating"))
                    rating(holder, position);
            }
        } catch (Exception e) {
            Helper.showToast("Something went wrong");
        }

        // donutGraph(holder);


    }

    private void rating(CustomViewHolder holder, int position) {

        holder.pieChart.setVisibility(View.GONE);
        holder.barChart.setVisibility(View.GONE);
        holder.ratingBarLayout.setVisibility(View.VISIBLE);

        try {
            holder.ratingTxt1.setText(dataList.get(position).getPollResult().get(0).getOptionsResult().get(0).getTotalPercentage().toString() + "%");
            holder.progress_1.setProgress(Float.floatToIntBits(dataList.get(position).getPollResult().get(0).getOptionsResult().get(0).getTotalPercentage()));

            holder.ratingTxt2.setText(dataList.get(position).getPollResult().get(0).getOptionsResult().get(1).getTotalPercentage().toString() + "%");
            holder.progress_2.setProgress(Float.floatToIntBits(dataList.get(position).getPollResult().get(0).getOptionsResult().get(1).getTotalPercentage()));

            holder.ratingTxt3.setText(dataList.get(position).getPollResult().get(0).getOptionsResult().get(2).getTotalPercentage().toString() + "%");
            holder.progress_3.setProgress(Float.floatToIntBits(dataList.get(position).getPollResult().get(0).getOptionsResult().get(2).getTotalPercentage()));

            holder.ratingTxt4.setText(dataList.get(position).getPollResult().get(0).getOptionsResult().get(3).getTotalPercentage().toString() + "%");
            holder.progress_4.setProgress(Float.floatToIntBits(dataList.get(position).getPollResult().get(0).getOptionsResult().get(3).getTotalPercentage()));

            holder.ratingTxt5.setText(dataList.get(position).getPollResult().get(0).getOptionsResult().get(4).getTotalPercentage().toString() + "%");
            holder.progress_5.setProgress(Float.floatToIntBits(dataList.get(position).getPollResult().get(0).getOptionsResult().get(4).getTotalPercentage()));
        } catch (Exception e) {
            Helper.showToast("das");
        }

    }

    private void donutGraph(CustomViewHolder holder) {
    }

    private void pieGraph(CustomViewHolder holder, int position) {
        holder.pieChart.setVisibility(View.VISIBLE);
        holder.barChart.setVisibility(View.GONE);
        holder.ratingBarLayout.setVisibility(View.GONE);
        PieData barData;

        ArrayList barEntriesArrayList;
        barEntriesArrayList = new ArrayList<>();
        barEntriesArrayList.clear();
        //  ArrayList<String> labelNames = new ArrayList<>();


        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.

        for (int i = 0; i < dataList.get(position).getPollResult().get(0).getOptionsResult().size(); i++) {
            // if (Float.compare(dataList.get(position).getPollResult().get(0).getOptionsResult().get(i).getTotalPercentage(), 0) != 0) {
            barEntriesArrayList.add(new PieEntry(dataList.get(position).getPollResult().get(0).getOptionsResult().get(i).getTotalPercentage()
                    , dataList.get(position).getPollResult().get(0).getOptionsResult().get(i).getOptionValue()));
            // labelNames.add(dataList.get(position).getPollResult().get(0).getOptionsResult().get(i).getOptionValue());
            // }
        }

//        for (int i = 0; i < dataList.get(position).getPollResult().get(0).getOptionsResult().size(); i++) {
//            labelNames.add(dataList.get(position).getPollResult().get(0).getOptionsResult().get(i).getOptionValue());
//        }


        // variable for our bar data set.
        PieDataSet barDataSet;

        // array list for storing entries.
        barDataSet = new PieDataSet(barEntriesArrayList, "");
        // holder.pieChart.setUsePercentValues(true);

        barDataSet.setSliceSpace(5f);

        barData = new PieData(barDataSet);

        Description description = new Description();
        description.setText("Poll Result");
        holder.pieChart.setDrawEntryLabels(false);
        holder.pieChart.setDescription(description);
        barDataSet.setValueFormatter(new MyPercentFormatter(holder.pieChart));
        //  barDataSet.setValueFormatter(new NonZeroChartValueFormatter(0));

//        holder.pieChart.setUsePercentValues(true);

        holder.pieChart.setNoDataText("No Data Found");
        // adding color to our bar data set.
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // setting text color.
        barDataSet.setValueTextColor(Color.WHITE);

        // setting text size
        barDataSet.setValueTextSize(13f);
        holder.pieChart.getDescription().setEnabled(true);
        Legend legend = holder.pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setWordWrapEnabled(true);
        legend.setXEntrySpace(4f);
        legend.setYOffset(10f);
        legend.setDrawInside(false);
        holder.pieChart.setData(barData);
        int j = 0;
        for (int i = 0; i < barEntriesArrayList.size(); i++) {
            PieEntry pieEntry = (PieEntry) barEntriesArrayList.get(i);
            if (pieEntry.getValue() == 0.0) {
                j++;
            }
        }

        if (j == barEntriesArrayList.size())
            holder.pieChart.setVisibility(View.GONE);
    }

    private void barGraph(CustomViewHolder holder, int position) {
        holder.pieChart.setVisibility(View.GONE);
        holder.barChart.setVisibility(View.VISIBLE);
        holder.ratingBarLayout.setVisibility(View.GONE);

        BarData barData;

        ArrayList<BarEntry> barEntriesArrayList = new ArrayList<>();
        barEntriesArrayList.clear();
        ArrayList<String> labelNames = new ArrayList<>();


        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.

        for (int i = 0; i < dataList.get(position).getPollResult().get(0).getOptionsResult().size(); i++) {

            barEntriesArrayList.add(new BarEntry(i, dataList.get(position).getPollResult().get(0).getOptionsResult().get(i).getTotalPercentage()));
            if (i != dataList.get(position).getPollResult().get(0).getOptionsResult().size() - 1)
                labelNames.add(dataList.get(position).getPollResult().get(0).getOptionsResult().get(i).getOptionValue());
            else {
                String l = dataList.get(position).getPollResult().get(0).getOptionsResult().get(i).getOptionValue();

                labelNames.add(l.substring(0, Math.min(l.length(), 7)));

            }

        }


        // variable for our bar data set.
        BarDataSet barDataSet = new BarDataSet(barEntriesArrayList, "Poll Result");
        // adding color to our bar data set.
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(8f);


        barData = new BarData(barDataSet);

        //holder.barChart.setFitBars(true);
        holder.barChart.getDescription().setEnabled(false);
        //  holder.barChart.getDescription().setText("Poll Result");
//        Description description = new Description();
//        description.setText("Poll Result");
//        holder.pieChart.setDescription(description);
        holder.barChart.animateY(2000);
        holder.barChart.animateX(2000);

        XAxis xAxis = holder.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
//        xAxis.setTextSize(8);
//        xAxis.setSpaceMin(30);
        // xAxis.labelCount = labels.size // important

        //  xAxis.setSpaceMax(0.5f); // optional
        xAxis.setSpaceMin(0.5f);// optional
        xAxis.setGranularityEnabled(true);
        //holder.barChart.zoomIn();
//        holder.barChart.setVisibleXRangeMaximum(8);
//        holder.barChart.setVisibleXRangeMinimum(4);
        xAxis.setCenterAxisLabels(false);


        // xAxis.setGranularity(1f);
        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(holder.barChart, labelNames);
        xAxis.setValueFormatter(xAxisFormatter);

        xAxis.setLabelRotationAngle(45f);
        xAxis.setAvoidFirstLastClipping(true);
        // barData.setBarWidth(1f);

        // xAxis.setMaxLabelChars(maxLabelLength);


        // holder.barChart.setVisibleXRangeMaximum(5);//no more than 5 values on the x-axis can be viewed at once without scrolling
        //  holder.barChart.setVisibleXRangeMinimum(5);
        holder.barChart.setData(barData);
        holder.barChart.invalidate();


//        xAxis.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return labelNames.get((int) value);
//
//            }
//        });

        //     holder.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labelNames));


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void replaceFragment(Fragment fragment, int index) {

//        FragmentManager fm = view;
//        Bundle args = new Bundle();
//        args.putInt("index", index);
//        fragment.setArguments(args);
//        fm.beginTransaction().add(R.id.frameLayout, fragment)
//                .setReorderingAllowed(true)
//                .addToBackStack(null)
//                .commit();
    }

    public class NonZeroChartValueFormatter extends DefaultValueFormatter {

        public NonZeroChartValueFormatter(int digits) {
            super(digits);
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                        ViewPortHandler viewPortHandler) {
            if (value > 0) {
                return mFormat.format(value);
            } else {
                return "";
            }
        }
    }

    public class DayAxisValueFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;
        ArrayList<String> labelNames = new ArrayList<>();
        int i = 0;

        public DayAxisValueFormatter(BarLineChartBase<?> chart, ArrayList<String> labelNames) {
            this.chart = chart;
            this.labelNames = labelNames;
        }

        @Override
        public String getFormattedValue(float value) {

            return labelNames.get((int) value);
        }
    }


}


class CustomViewHolder extends RecyclerView.ViewHolder {


    public final View mView;

    public TextView questionTxt;

    BarChart barChart;
    PieChart pieChart;
    LinearLayout ratingBarLayout;
    ProgressBar progress_5, progress_4, progress_3, progress_2, progress_1;
    TextView ratingTxt5, ratingTxt4, ratingTxt3, ratingTxt2, ratingTxt1;


    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        questionTxt = mView.findViewById(R.id.questionTxt);
        barChart = mView.findViewById(R.id.barChart);
        pieChart = mView.findViewById(R.id.pieChart);
        ratingBarLayout = mView.findViewById(R.id.ratingBarLayout);

        progress_5 = mView.findViewById(R.id.progress_5);
        progress_4 = mView.findViewById(R.id.progress_4);
        progress_3 = mView.findViewById(R.id.progress_3);
        progress_2 = mView.findViewById(R.id.progress_2);
        progress_1 = mView.findViewById(R.id.progress_1);


        ratingTxt5 = mView.findViewById(R.id.ratingTxt5);
        ratingTxt4 = mView.findViewById(R.id.ratingTxt4);
        ratingTxt3 = mView.findViewById(R.id.ratingTxt3);
        ratingTxt2 = mView.findViewById(R.id.ratingTxt2);
        ratingTxt1 = mView.findViewById(R.id.ratingTxt1);

//        viewGoodie.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getView() + " " +
//                Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getGoodie());
//
//        Helper.setButtonColorWithDrawable(viewGoodie,Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterInnerTextColor());
    }

    public class CustomPercentFormatter implements IValueFormatter {

        private final DecimalFormat mFormat;

        public CustomPercentFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }

        public CustomPercentFormatter(DecimalFormat format) {
            this.mFormat = format;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (value == 0.0f)
                return "";
            return mFormat.format(value) + " %";
        }
    }
}
