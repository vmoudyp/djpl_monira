package id.exorty.monira.ui.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.ui.adapter.NotificationAdapter;
import id.exorty.monira.ui.model.NotificationItemMessageInfo;
import id.exorty.monira.ui.model.NotificationItemSatkerInfo;
import id.exorty.monira.ui.model.NotificationStandardInfo;

public class NotificationComponent extends LinearLayout {
    private static RecyclerView mRecyclerView;
    private static NotificationAdapter mNotificationsAdapter;

    protected Context mContext;

    public NotificationComponent(Context context) {
        super(context);

        this.mContext = context;

        this.addView(createComponent());
    }

    private View createComponent(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.component_notification_layout, null);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mNotificationsAdapter = new NotificationAdapter(mContext, R.layout.notification_standard);
        mRecyclerView.setAdapter(mNotificationsAdapter);

        return view;
    }

    public static void updateData(JsonArray jsonArray){
        List<NotificationStandardInfo> notificationStandardInfo = new ArrayList<NotificationStandardInfo>();
        for (int i = 0; i < jsonArray.values().size(); i++) {
            JsonObject jo = jsonArray.get(i).asObject();
            notificationStandardInfo.add(new NotificationStandardInfo(jo.get("id").asString(), jo.get("date_time").asString(), jo.get("message").asString(), jo.get("priority").asString()));
        }

        mNotificationsAdapter.updateData(notificationStandardInfo);
    }

}
