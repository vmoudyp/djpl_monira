package id.exorty.monira.ui.components;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.eclipsesource.json.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import az.plainpie.PieView;
import id.exorty.monira.R;
import id.exorty.monira.helper.Util;

public class CurrentMonthComponent extends LinearLayout {
    private TextView mTxtCardHeader;
    private PieView mPieViewRealization;
    private PieView mPieViewPrognosis;
    private PieView mPieViewEndOfYear;
    private LinearLayout mBackgroundProcessLayout;
    private AVLoadingIndicatorView mAvloadingIndicatorView;
    private LinearLayout mTryAgainView;
    private FrameLayout mForegroundContentLayout;

    protected Context mContext;

    private Callback mCallback;

    public interface Callback {
        void onReload();
    }

    public CurrentMonthComponent(Context context) {
        super(context);

        this.mContext = context;

        this.addView(createComponent());
    }

    private View createComponent(){
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.component_current_month_layout, null);

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

        mForegroundContentLayout = view.findViewById(R.id.foreground_content_layout);

        mPieViewRealization = view.findViewById(R.id.pie_realization);
        mPieViewRealization.setPercentageBackgroundColor(Color.parseColor(Util.getRealizationColor(mContext)));
        mPieViewRealization.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float value = (float)v.getTag();

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogCustom);
                builder.setTitle(R.string.dashboard_realization_value);

                final View customLayout = inflater.inflate(R.layout.custom_dialog_project_value, null);
                TextView textView = customLayout.findViewById(R.id.txt_item);
                textView.setText("Rp. " + String.format("%,.0f", value));
                // Set above view in alert dialog.
                builder.setView(customLayout);
                builder.setNegativeButton(R.string.material_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mPieViewPrognosis = view.findViewById(R.id.pie_prognosa);
        mPieViewPrognosis.setPercentageBackgroundColor(Color.parseColor(Util.getPrognosisColor(mContext)));
        mPieViewPrognosis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float value = (float)v.getTag();

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogCustom);
                builder.setTitle(R.string.dashboard_prognosis_value);

                final View customLayout = inflater.inflate(R.layout.custom_dialog_project_value, null);
                TextView textView = customLayout.findViewById(R.id.txt_item);
                textView.setText("Rp. " + String.format("%,.0f", value));
                // Set above view in alert dialog.
                builder.setView(customLayout);
                builder.setNegativeButton(R.string.material_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mPieViewEndOfYear = view.findViewById(R.id.pie_end_of_year);
        mPieViewEndOfYear.setPercentageBackgroundColor(Color.parseColor(Util.getEndOfYear(mContext)));
        mPieViewEndOfYear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float value = (float)v.getTag();

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogCustom);
                builder.setTitle(R.string.dashboard_end_of_year_value);

                final View customLayout = inflater.inflate(R.layout.custom_dialog_project_value, null);
                TextView textView = customLayout.findViewById(R.id.txt_item);
                textView.setText("Rp. " + String.format("%,.0f", value));
                // Set above view in alert dialog.
                builder.setView(customLayout);
                builder.setNegativeButton(R.string.material_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    public void setCallback(Callback callback){
        this.mCallback = callback;
    }

    public void startUpdateData(){
        mForegroundContentLayout.setVisibility(GONE);
        mBackgroundProcessLayout.setVisibility(VISIBLE);
        mAvloadingIndicatorView.setVisibility(VISIBLE);
        mTryAgainView.setVisibility(GONE);
    }

    public void updateData(JsonObject jsonObject){
        mPieViewRealization.setPercentage(jsonObject.get("realization").asFloat());
        mPieViewPrognosis.setPercentage(jsonObject.get("prognosis").asFloat());
        mPieViewEndOfYear.setPercentage(jsonObject.get("end_of_year").asFloat());

        mForegroundContentLayout.setVisibility(View.VISIBLE);
        mBackgroundProcessLayout.setVisibility(View.GONE);

        if (jsonObject.names().contains("realization_amount"))
            mPieViewRealization.setTag(jsonObject.get("realization_amount").asFloat());
        else
            mPieViewRealization.setTag(0f);

        if (jsonObject.names().contains("prognosis_amount"))
            mPieViewPrognosis.setTag(jsonObject.get("prognosis_amount").asFloat());
        else
            mPieViewPrognosis.setTag(0f);

        if (jsonObject.names().contains("end_of_year_amount"))
            mPieViewEndOfYear.setTag(jsonObject.get("end_of_year_amount").asFloat());
        else
            mPieViewEndOfYear.setTag(0f);
    }

    public void errorUpdateData(){
        mForegroundContentLayout.setVisibility(View.GONE);
        mBackgroundProcessLayout.setVisibility(View.VISIBLE);
        mAvloadingIndicatorView.setVisibility(GONE);
        mTryAgainView.setVisibility(View.VISIBLE);
    }

}
