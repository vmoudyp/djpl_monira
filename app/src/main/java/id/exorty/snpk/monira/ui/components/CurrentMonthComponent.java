package id.exorty.snpk.monira.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.eclipsesource.json.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import az.plainpie.PieView;
import id.exorty.snpk.monira.R;
import id.exorty.snpk.monira.helper.Util;
import id.exorty.snpk.monira.ui.model.DashboardData;

public class CurrentMonthComponent extends LinearLayout {
    private static TextView mTxtCardHeader;
    private static PieView mPieViewRealization;
    private static PieView mPieViewPrognosis;
    private static PieView mPieViewEndOfYear;
    private static LinearLayout mBackgroundProcessLayout;
    private static AVLoadingIndicatorView mAvloadingIndicatorView;
    private static FrameLayout mForegroundContentLayout;


    protected Context mContext;

    public CurrentMonthComponent(Context context) {
        super(context);

        this.mContext = context;

        this.addView(createComponent());
    }

    private View createComponent(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.component_current_month_layout, null);

        mTxtCardHeader = view.findViewById(R.id.txt_card_header);
        mTxtCardHeader.setText(R.string.dashboard_current_month);

        mBackgroundProcessLayout = view.findViewById(R.id.background_process_layout);
        mAvloadingIndicatorView = view.findViewById(R.id.avloadingIndicatorView);

        mForegroundContentLayout = view.findViewById(R.id.foreground_content_layout);

        mPieViewRealization = view.findViewById(R.id.pie_realization);
        mPieViewRealization.setPercentageBackgroundColor(Color.parseColor(Util.getRealizationColor(mContext)));

        mPieViewPrognosis = view.findViewById(R.id.pie_prognosa);
        mPieViewPrognosis.setPercentageBackgroundColor(Color.parseColor(Util.getPrognosisColor(mContext)));

        mPieViewEndOfYear = view.findViewById(R.id.pie_end_of_year);
        mPieViewEndOfYear.setPercentageBackgroundColor(Color.parseColor(Util.getEndOfYear(mContext)));

        return view;
    }

    public static void startUpdateData(){
        mForegroundContentLayout.setVisibility(GONE);
        mBackgroundProcessLayout.setVisibility(VISIBLE);
    }

    public static void updateData(JsonObject jsonObject){
        mPieViewRealization.setPercentage(jsonObject.get("realization").asFloat());
        mPieViewPrognosis.setPercentage(jsonObject.get("prognosis").asFloat());
        mPieViewEndOfYear.setPercentage(jsonObject.get("end_of_year").asFloat());

        mForegroundContentLayout.setVisibility(VISIBLE);
        mBackgroundProcessLayout.setVisibility(GONE);
    }
}
