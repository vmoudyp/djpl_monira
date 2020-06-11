package id.exorty.monira.ui.components;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.helper.Config;
import id.exorty.monira.helper.Util;
import id.exorty.monira.ui.SatkerActivity;
import id.exorty.monira.ui.adapter.UnderPerfomerAdapter;
import id.exorty.monira.ui.home.HomeFragment;
import id.exorty.monira.ui.model.DataInfo;
import id.exorty.monira.ui.model.SatkerInfo;

public class UnderPerformer extends LinearLayout {
    protected TextView mTxtCardHeader;
    private RecyclerView mRecyclerView;
    private TextView mTxtSatkerRankingInfo;
    private LinearLayout mLayoutSatkerRankingInfo;
    private UnderPerfomerAdapter mUnderPerfomerAdapter;

    protected Context mContext;
    private Callback mCallback;

    public interface Callback {
        void onOtherSatkerClick();
        void onSatkerRankingClick();
    }

    public UnderPerformer(Context context, Callback callback) {
        super(context);

        this.mContext = context;
        this.mCallback = callback;

        this.addView(createComponent());
    }

    private View createComponent(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.component_under_performer_layout, null);

        mTxtCardHeader = view.findViewById(R.id.txt_card_header);
        mTxtCardHeader.setText(R.string.dashboard_under_performer);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mUnderPerfomerAdapter = new UnderPerfomerAdapter(mContext, R.layout.component_performance_item_layout, new UnderPerfomerAdapter.Callback() {
            @Override
            public void onItemClick(String id, String description) {
                String a = id;

                Intent intent = new Intent(mContext, SatkerActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("description", description);

                mContext.startActivity(intent);

//                new AlertDialog.Builder(mContext)
//                        .setTitle("SATKER INFO")
//                        .setView(R.layout.component_satker_dialog_layout)
//                        .setPositiveButton(R.string.material_dialog_ok, new Dialog.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();

            }
        });

        mRecyclerView.setAdapter(mUnderPerfomerAdapter);

        int level = Util.GetSharedPreferences(mContext, "level", -1);

        mLayoutSatkerRankingInfo = view.findViewById(R.id.layout_satker_ranking_info);
        LinearLayout layout_button = view.findViewById(R.id.layout_button);
        Button btnSatkerView = view.findViewById(R.id.btn_satker_view);
        Button btnRankingView = view.findViewById(R.id.btn_ranking_view);
        if (level != 3) {
            layout_button.setVisibility(VISIBLE);
            btnSatkerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onOtherSatkerClick();
                }
            });

            btnRankingView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onSatkerRankingClick();
                }
            });
            mLayoutSatkerRankingInfo.setVisibility(GONE);
        }else{
            layout_button.setVisibility(GONE);
            mLayoutSatkerRankingInfo.setVisibility(VISIBLE);

            mTxtSatkerRankingInfo = view.findViewById(R.id.txt_satker_ranking_info);
        }

        return view;
    }

    public void updateData(JsonArray jsonArray){
        List<DataInfo> dataInfos = new ArrayList<DataInfo>();
        for (int i = 0; i < jsonArray.values().size(); i++) {
            JsonObject jo = jsonArray.get(i).asObject();
            dataInfos.add(new DataInfo(jo.get("id").asString(),jo.get("name").asString(),jo.get("realization").asDouble(),jo.get("prognosis").asDouble(),jo.get("end_of_year").asDouble(), 0d));
        }

        mUnderPerfomerAdapter.updateData(dataInfos);
    }

    public void updateRankingInfo(int ranking, String color){
        mTxtSatkerRankingInfo.setText("Peringkat SATKER ada di posisi " + ranking + " dari bawah");
        if (color.equals(Config.SATKER_RANKING_COLOR_RED)){
            mTxtSatkerRankingInfo.setTextColor(mContext.getResources().getColor(R.color.textColor1));
            mLayoutSatkerRankingInfo.setBackgroundColor(mContext.getResources().getColor(R.color.notification_priority_danger));
        }
    }
}
