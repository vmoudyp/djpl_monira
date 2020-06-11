package id.exorty.monira.ui.components;

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

import id.exorty.monira.R;
import id.exorty.monira.helper.Util;
import id.exorty.monira.ui.SatkerActivity;
import id.exorty.monira.ui.TypeOfActivityActivity;
import id.exorty.monira.ui.adapter.TypeOfActivityAdapter;
import id.exorty.monira.ui.model.DataInfo;

public class TypeOfActivity extends LinearLayout {
    private static TextView mTxtCardHeader;
    private static RecyclerView mRecyclerView;
    private static TypeOfActivityAdapter mTypeOfActivityAdapter;

    private static Context mContext;

    public TypeOfActivity(Context context) {
        super(context);

        this.mContext = context;

        this.addView(createComponent());
    }

    private View createComponent(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.component_type_of_activity_layout, null);

        mTxtCardHeader = view.findViewById(R.id.txt_card_header);
        mTxtCardHeader.setText(R.string.dashboard_type_of_activity);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mTypeOfActivityAdapter = new TypeOfActivityAdapter(mContext, R.layout.component_performance_item_layout, new TypeOfActivityAdapter.Callback() {
            @Override
            public void onItemClick(String id, String description, String[] ids, String[] descriptions) {
                int level = Util.GetSharedPreferences(mContext, "level", -1);

                if (level == 3)
                    return;

                Intent intent = new Intent(mContext, TypeOfActivityActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("description", description);
                intent.putExtra("ids", ids);
                intent.putExtra("descriptions", descriptions);

                mContext.startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mTypeOfActivityAdapter);

        return view;
    }


    public static void updateData(JsonArray jsonArray){
        List<DataInfo> dataInfos = new ArrayList<DataInfo>();
        for (int i = 0; i < jsonArray.values().size(); i++) {
            JsonObject jo = jsonArray.get(i).asObject();
            dataInfos.add(new DataInfo(jo.get("id").asString(),jo.get("name").asString(),jo.get("realization").asDouble(),jo.get("prognosis").asDouble(),jo.get("end_of_year").asDouble(), 0d));
        }

        mTypeOfActivityAdapter.updateData(dataInfos);
    }
}
