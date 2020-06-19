package id.exorty.monira.ui.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.eclipsesource.json.JsonObject;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import id.exorty.monira.R;

public class SatkerCurrentMonthComponent extends LinearLayout {
    private TextView mTxtCardHeader;
    private BarChart mChart;
    private float mBarWidth;
    private float mBarSpace;
    private float mGroupSpace;

    BarDataSet mDataSetNational, mDataSetSatker;

    private LinearLayout mBackgroundProcessLayout;
    private AVLoadingIndicatorView mAvloadingIndicatorView;
    private LinearLayout mTryAgainView;

    private Dialog mDialogDataInfo;

    protected Context mContext;

    private Callback mCallback;
    public interface Callback {
        void onReload();
    }

    public SatkerCurrentMonthComponent(Context context) {
        super(context);

        this.mContext = context;

        this.addView(createComponent());
    }

    private View createComponent(){
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.component_current_month_satker_layout, null);

        mTxtCardHeader = view.findViewById(R.id.txt_card_header);
        mTxtCardHeader.setText(R.string.dashboard_current_month);

        mBackgroundProcessLayout = view.findViewById(R.id.background_process_layout);
        mAvloadingIndicatorView = view.findViewById(R.id.avloadingIndicatorView);
        mTryAgainView = view.findViewById(R.id.layout_try_again);
        mTryAgainView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onReload();
            }
        });

        mChart = view.findViewById(R.id.chart_current_month);
        mChart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogDataInfo.show();
            }
        });

        createChart();

        return view;
    }

    public void setCallback(Callback callback){
        this.mCallback = callback;
    }

    public void startUpdateData(){
        mChart.setVisibility(GONE);
        mBackgroundProcessLayout.setVisibility(View.VISIBLE);
        mAvloadingIndicatorView.setVisibility(VISIBLE);
        mTryAgainView.setVisibility(GONE);
    }

    private void createChart() {

        Typeface typeface = ResourcesCompat.getFont(mContext, R.font.abel_regular);

        mBarWidth = 0.3f;
        mBarSpace = 0f;
        mGroupSpace = 0.4f;

        mChart.setDescription(null);
        mChart.setPinchZoom(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        mChart.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        mChart.getAxisLeft().setAxisMinimum(0);
        mChart.getAxisLeft().setAxisMaximum(100);

        ArrayList xVals = new ArrayList();

        xVals.add("Realisasi");
        xVals.add("Prognosa");
        xVals.add("Prognosa Akhir Tahun");

        ArrayList yVals1 = new ArrayList();
        ArrayList yVals2 = new ArrayList();

        yVals1.add(new BarEntry(1, (float) 1));
        yVals2.add(new BarEntry(1, (float) 2));
        yVals1.add(new BarEntry(2, (float) 3));
        yVals2.add(new BarEntry(2, (float) 4));
        yVals1.add(new BarEntry(3, (float) 5));
        yVals2.add(new BarEntry(3, (float) 6));

        mDataSetNational = new BarDataSet(yVals1, "Nasional");
        mDataSetNational.setColor(Color.RED);

        mDataSetSatker = new BarDataSet(yVals2, "Satker");
        mDataSetSatker.setColor(Color.BLUE);

        BarData data = new BarData(mDataSetNational, mDataSetSatker);
        data.setValueFormatter(new LargeValueFormatter());

        mChart.setData(data);
        mChart.getBarData().setBarWidth(mBarWidth);
        mChart.getXAxis().setAxisMinimum(0);
        mChart.getXAxis().setAxisMaximum(0 + mChart.getBarData().getGroupWidth(mGroupSpace, mBarSpace) * 3);
        mChart.groupBars(0, mGroupSpace, mBarSpace);
        mChart.getData().setHighlightEnabled(false);
        mChart.getData().setValueTypeface(typeface);
        mChart.getData().setValueTextColor(getResources().getColor(R.color.textColor1));
        mChart.getData().setValueTextSize(12f);
        mChart.invalidate();

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setXEntrySpace(20f);
        l.setTypeface(typeface);
        l.setTextSize(20f);
        l.setTextColor(getResources().getColor(R.color.textColor1));


//X-axis
        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(9f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(3);
        xAxis.mAxisMaximum = 3f;
        xAxis.setCenterAxisLabels(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setSpaceMax(4f);
        xAxis.setSpaceMin(4f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        xAxis.setTypeface(typeface);
        xAxis.setTextColor(getResources().getColor(R.color.textColor1));
        xAxis.setMultiLineLabel(true);
        xAxis.setTextSize(13f);

        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setEnabled(false);
        mChart.setPadding(0,0,0,30);
        mChart.setExtraBottomOffset(75f);
        mChart.setExtraTopOffset(50f);
    }

    public void updateData(JsonObject joNational, JsonObject joSatker){
        ArrayList yVals1 = new ArrayList();
        ArrayList yVals2 = new ArrayList();

        yVals1.add(new BarEntry(1, joNational.get("realization").asFloat()));
        yVals2.add(new BarEntry(1, joSatker.get("realization").asFloat()));
        yVals1.add(new BarEntry(2, joNational.get("prognosis").asFloat()));
        yVals2.add(new BarEntry(2, joSatker.get("prognosis").asFloat()));
        yVals1.add(new BarEntry(3, joNational.get("end_of_year").asFloat()));
        yVals2.add(new BarEntry(3, joSatker.get("end_of_year").asFloat()));

        mDataSetNational.setValues(yVals1);
        mDataSetSatker.setValues(yVals2);
        mChart.getBarData().setBarWidth(mBarWidth);
        mChart.getXAxis().setAxisMinimum(0);
        mChart.getXAxis().setAxisMaximum(0 + mChart.getBarData().getGroupWidth(mGroupSpace, mBarSpace) * 3);
        mChart.groupBars(0, mGroupSpace, mBarSpace);
        mChart.getData().setHighlightEnabled(false);
        mChart.invalidate();

        mChart.setVisibility(VISIBLE);
        mBackgroundProcessLayout.setVisibility(GONE);

        createDialog(joSatker);
    }

    private void createDialog(JsonObject joSatker){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dataInfoLayout = inflater.inflate(R.layout.custom_dialog_satker_data_info, null);

        TextView txtPagu = dataInfoLayout.findViewById(R.id.txt_pagu);
        txtPagu.setText("Rp. " + String.format("%,.0f", joSatker.get("budget_amount").asFloat()));

        TextView txtRealization = dataInfoLayout.findViewById(R.id.txt_realization);
        txtRealization.setText("Rp. " + String.format("%,.0f", joSatker.get("realization_amount").asFloat()));

        TextView txtPrognosis = dataInfoLayout.findViewById(R.id.txt_prognosis);
        txtPrognosis.setText("Rp. " + String.format("%,.0f", joSatker.get("prognosis_amount").asFloat()));

        TextView txtEndOfYearProgonosis = dataInfoLayout.findViewById(R.id.txt_prognosis_end_of_year);
        txtEndOfYearProgonosis.setText("Rp. " + String.format("%,.0f", joSatker.get("end_of_year_amount").asFloat()));

        TextView txtSptjm = dataInfoLayout.findViewById(R.id.txt_sptjm);
        txtSptjm.setText("Rp. " + String.format("%,.0f", joSatker.get("sptjm_amount").asFloat()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        builder.setTitle("Satker - Dalam Rupiah");

        builder.setView(dataInfoLayout);
        builder.setNegativeButton(R.string.material_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mDialogDataInfo = builder.create();
    }

    public void errorUpdateData(){
        mChart.setVisibility(GONE);
        mBackgroundProcessLayout.setVisibility(View.VISIBLE);
        mAvloadingIndicatorView.setVisibility(GONE);
        mTryAgainView.setVisibility(View.VISIBLE);
    }
}
