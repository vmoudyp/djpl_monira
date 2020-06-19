package id.exorty.monira.ui.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.eclipsesource.json.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.ui.model.CustomDataEntry;

public class SatkerTrendComponent extends LinearLayout {
    private TextView txtCardHeader;

    private Context mContext;
    private LinearLayout mBackgroundProcessLayout;
    private AVLoadingIndicatorView mAvloadingIndicatorView;
    private LinearLayout mTryAgainView;

    private AnyChartView mAnyChartView;
    private Cartesian mCartesian;
    private Line mRealization;
    private Line mPrognosis;
    private Line mLastYear;

    private List<DataEntry> mNationalSeriesData = new ArrayList<>();
    private List<DataEntry> mSatkerSeriesData = new ArrayList<>();

    private SatkerProfileComponent.Callback mCallback;

    public interface Callback {
        void onReload();
    }

    public SatkerTrendComponent(Context context) {
        super(context);

        this.mContext = context;

        this.addView(createComponent());
    }

    private View createComponent(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.component_trend_layout, null);

        txtCardHeader = view.findViewById(R.id.txt_card_header);
        txtCardHeader.setText(R.string.dashboard_trend);

        mBackgroundProcessLayout = view.findViewById(R.id.background_process_layout);
        mAvloadingIndicatorView = view.findViewById(R.id.avloadingIndicatorView);
        mTryAgainView = view.findViewById(R.id.layout_try_again);
        mTryAgainView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onReload();
            }
        });

        Button btnNationalView = view.findViewById(R.id.btn_national_view);
        btnNationalView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData(mNationalSeriesData);
            }
        });

        Button btnSatkerView = view.findViewById(R.id.btn_satker_view);
        btnNationalView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData(mSatkerSeriesData);
            }
        });

        mAnyChartView = view.findViewById(R.id.chart_trend);

        mCartesian = AnyChart.line();
        mCartesian.animation(true);
        mCartesian.background().fill("#2e1572");
        mCartesian.padding(0d, 0d, 0d, 0d);
        mCartesian.crosshair().enabled(true);
        mCartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        mCartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        mCartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        mCartesian.xAxis(0).labels().fontSize(8);
        mCartesian.yScale().maximum(100);
        mCartesian.yScale().minimum(0);

        mCartesian.legend().enabled(true);
        mCartesian.legend().fontFamily("@font/abel_regular");
        mCartesian.legend().fontSize(13d);
        mCartesian.legend().padding(0d, 0d, 10d, 0d);

        mAnyChartView.setChart(mCartesian);

        return view;
    }

    public void setCallback(SatkerProfileComponent.Callback callback){
        this.mCallback = callback;
    }

    public void createData(JsonObject joNational, JsonObject joSatker){
        String[] months = new String[]{"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Agt","Sep","Okt","Nov","Des"};

        mNationalSeriesData = new ArrayList<>();
        mSatkerSeriesData = new ArrayList<>();
        for (int i = 0; i < months.length; i++){
            mNationalSeriesData.add(new CustomDataEntry(months[i], joNational.get("realization").asArray().get(i).asFloat(), joNational.get("prognosis").asArray().get(i).asFloat(), joNational.get("last_year").asArray().get(i).asFloat()));
            mSatkerSeriesData.add(new CustomDataEntry(months[i], joSatker.get("realization").asArray().get(i).asFloat(), joSatker.get("prognosis").asArray().get(i).asFloat(), joSatker.get("last_year").asArray().get(i).asFloat()));
        }

        Set set = Set.instantiate();
        set.data(mSatkerSeriesData);
        Mapping realizationMapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping prognosisMapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping lastYearMapping = set.mapAs("{ x: 'x', value: 'value3' }");

        mRealization = mCartesian.line(realizationMapping);
        mRealization.name(mContext.getString(R.string.dashboard_realization));
        String realizationColor = String.format("#%06x", ContextCompat.getColor(mContext, R.color.colorRealization) & 0xffffff);
        mRealization.color(realizationColor);
        mRealization.markers().enabled(true);
        mRealization.markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        mRealization.hovered().markers().enabled(true);
        mRealization.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(8d);
        mRealization.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        mPrognosis = mCartesian.line(prognosisMapping);
        mPrognosis.name(mContext.getString(R.string.dashboard_prognosis));
        String prognosisColor = String.format("#%06x", ContextCompat.getColor(mContext, R.color.colorPrognosis) & 0xffffff);
        mPrognosis.color(prognosisColor);
        mPrognosis.markers().enabled(true);
        mPrognosis.markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        mPrognosis.hovered().markers().enabled(true);
        mPrognosis.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        mPrognosis.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        mLastYear = mCartesian.line(lastYearMapping);
        mLastYear.name(mContext.getString(R.string.dashboard_last_year));
        String lastYearColor = String.format("#%06x", ContextCompat.getColor(mContext, R.color.colorLastYear) & 0xffffff);
        mLastYear.color(lastYearColor);
        mLastYear.markers().enabled(true);
        mLastYear.markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        mLastYear.hovered().markers().enabled(true);
        mLastYear.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        mLastYear.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        mAnyChartView.setVisibility(VISIBLE);
        mBackgroundProcessLayout.setVisibility(GONE);
    }

    public void startUpdateDate(){
        mAnyChartView.setVisibility(GONE);
        mBackgroundProcessLayout.setVisibility(VISIBLE);
        mAvloadingIndicatorView.setVisibility(VISIBLE);
        mTryAgainView.setVisibility(GONE);
    }

    public void updateData(JsonObject joNational, JsonObject joSatker){
        String[] months = new String[]{"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Agt","Sep","Okt","Nov","Des"};

        mSatkerSeriesData = new ArrayList<>();
        mSatkerSeriesData = new ArrayList<>();
        for (int i = 0; i < months.length; i++){
            mSatkerSeriesData.add(new CustomDataEntry(months[i], joNational.get("realization").asArray().get(i).asFloat(), joNational.get("prognosis").asArray().get(i).asFloat(), joNational.get("last_year").asArray().get(i).asFloat()));
            mSatkerSeriesData.add(new CustomDataEntry(months[i], joSatker.get("realization").asArray().get(i).asFloat(), joSatker.get("prognosis").asArray().get(i).asFloat(), joSatker.get("last_year").asArray().get(i).asFloat()));
        }

        Set set = Set.instantiate();
        set.data(mSatkerSeriesData);
        Mapping realizationMapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping prognosisMapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping lastYearMapping = set.mapAs("{ x: 'x', value: 'value3' }");

        mRealization.data(realizationMapping);
        mPrognosis.data(prognosisMapping);
        mLastYear.data(lastYearMapping);

        mAnyChartView.setVisibility(VISIBLE);
        mBackgroundProcessLayout.setVisibility(GONE);
    }


    private void updateData(List<DataEntry> datas){
        String[] months = new String[]{"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Agt","Sep","Okt","Nov","Des"};

        Set set = Set.instantiate();
        set.data(datas);
        Mapping realizationMapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping prognosisMapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping lastYearMapping = set.mapAs("{ x: 'x', value: 'value3' }");

        mRealization.data(realizationMapping);
        mPrognosis.data(prognosisMapping);
        mLastYear.data(lastYearMapping);

        mAnyChartView.setVisibility(VISIBLE);
        mBackgroundProcessLayout.setVisibility(GONE);
    }

    public void errorUpdateData(){
        mAnyChartView.setVisibility(GONE);
        mBackgroundProcessLayout.setVisibility(View.VISIBLE);
        mAvloadingIndicatorView.setVisibility(GONE);
        mTryAgainView.setVisibility(View.VISIBLE);
    }
}
