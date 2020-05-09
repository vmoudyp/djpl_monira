package id.exorty.snpk.monira.ui.components;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

import id.exorty.snpk.monira.R;
import id.exorty.snpk.monira.ui.SatkerActivity;
import id.exorty.snpk.monira.ui.adapter.UnderPerfomerAdapter;
import id.exorty.snpk.monira.ui.model.DataInfo;

public class UnderPerformer extends LinearLayout {
    protected TextView mTxtCardHeader;
    private static RecyclerView mRecyclerView;
    private static UnderPerfomerAdapter mUnderPerfomerAdapter;

    protected Context mContext;

    public UnderPerformer(Context context) {
        super(context);

        this.mContext = context;

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
            public void onItemClick(int id, String description) {
                int a = id;

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

        return view;
    }

    public static void UpdateData(JsonArray jsonArray){
        List<DataInfo> dataInfos = new ArrayList<DataInfo>();
        for (int i = 0; i < jsonArray.values().size(); i++) {
            JsonObject jo = jsonArray.get(i).asObject();
            dataInfos.add(new DataInfo(Integer.valueOf(jo.get("id").asString()),jo.get("name").asString(),jo.get("realization").asDouble(),jo.get("prognosis").asDouble(),jo.get("end_of_year").asDouble(), 0d));
        }

        mUnderPerfomerAdapter.updateData(dataInfos);
    }

}
