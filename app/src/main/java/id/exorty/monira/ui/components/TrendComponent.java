package id.exorty.monira.ui.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.ui.home.HomeFragment;
import id.exorty.monira.ui.model.CustomDataEntry;
import id.exorty.monira.ui.model.DataInfo;

public class TrendComponent extends LinearLayout {
    private TextView txtCardHeader;

    private Context mContext;
    private LinearLayout mBackgroundProcessLayout;
    private AVLoadingIndicatorView mAvloadingIndicatorView;
    private AnyChartView mAnyChartView;
    private Cartesian mCartesian;
    private Line mRealization;
    private Line mPrognosis;
    private Line mLastYear;

    public TrendComponent(Context context) {
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
        mCartesian.yScale().minimum(0);
        mCartesian.yScale().maximum(100);

        mCartesian.legend().enabled(true);
        mCartesian.legend().fontFamily("@font/abel_regular");
        mCartesian.legend().fontSize(13d);
        mCartesian.legend().padding(0d, 0d, 10d, 0d);

        mAnyChartView.setChart(mCartesian);

        return view;
    }

    public void createData(JsonObject jsonObject){
        String[] months = new String[]{"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Agt","Sep","Okt","Nov","Des"};

        List<DataEntry> seriesData = new ArrayList<>();
        for (int i = 0; i < months.length; i++){
            seriesData.add(new CustomDataEntry(months[i], jsonObject.get("realization").asArray().get(i).asFloat(), jsonObject.get("prognosis").asArray().get(i).asFloat(), jsonObject.get("last_year").asArray().get(i).asFloat()));
        }

        Set set = Set.instantiate();
        set.data(seriesData);
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
    }

    public void updateData(JsonObject jsonObject){
        String[] months = new String[]{"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Agt","Sep","Okt","Nov","Des"};

        List<DataEntry> seriesData = new ArrayList<>();
        for (int i = 0; i < months.length; i++){
            seriesData.add(new CustomDataEntry(months[i], jsonObject.get("realization").asArray().get(i).asFloat(), jsonObject.get("prognosis").asArray().get(i).asFloat(), jsonObject.get("last_year").asArray().get(i).asFloat()));
        }

        Set set = Set.instantiate();
        set.data(seriesData);

        Mapping realizationMapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping prognosisMapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping lastYearMapping = set.mapAs("{ x: 'x', value: 'value3' }");

        mRealization.data(realizationMapping);
        mPrognosis.data(prognosisMapping);
        mLastYear.data(lastYearMapping);

        mAnyChartView.setVisibility(VISIBLE);
        mBackgroundProcessLayout.setVisibility(GONE);
    }
}
