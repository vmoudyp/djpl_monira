package id.exorty.monira.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.helper.Util;
import id.exorty.monira.service.DataService;
import id.exorty.monira.ui.adapter.NotificationListAdapter;
import id.exorty.monira.ui.adapter.SatkerRankingAdapter;
import id.exorty.monira.ui.components.Alert;
import id.exorty.monira.ui.model.NotificationGroupMessageInfo;
import id.exorty.monira.ui.model.NotificationGroupSatkerInfo;
import id.exorty.monira.ui.model.NotificationItemMessageInfo;
import id.exorty.monira.ui.model.NotificationItemSatkerInfo;
import id.exorty.monira.ui.model.NotificationListItem;
import id.exorty.monira.ui.model.SatkerRankingInfo;

import static android.view.View.GONE;
import static id.exorty.monira.helper.Config.NOTIFICATION_GROUP_BY_MESSAGE;
import static id.exorty.monira.helper.Config.NOTIFICATION_GROUP_BY_SATKER;

public class NotificationListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private NotificationListAdapter mNotificationListAdapter;

    private LinearLayout mBackgroundProcessLayout;
    private AVLoadingIndicatorView mAvloadingIndicatorView;

    private List<NotificationListItem> notificationListItems;
    private int mYear;

    private int mLoop = 0;

    List<NotificationListItem> notificationListItemsBySatker;
    List<NotificationListItem> notificationListItemsByMessage;

    private String mNationalData;

    private Button mBtnBySatker;
    private Button mBtnByMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView btnBack = findViewById(R.id.menu_icon);
        btnBack .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnBySatker = findViewById(R.id.btn_by_satker);
        mBtnBySatker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotificationListAdapter.updateData(notificationListItemsBySatker);
                mBtnBySatker.setSelected(true);
                mBtnBySatker.invalidate();
                mBtnByMessage.setEnabled(true);
                mBtnByMessage.invalidate();
            }
        });
        mBtnBySatker.setSelected(true);
        mBtnBySatker.invalidate();

        mBtnByMessage = findViewById(R.id.btn_by_message);
        mBtnByMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationListItemsByMessage == null){
                    getNotificationListData(NOTIFICATION_GROUP_BY_MESSAGE);
                }else {
                    mNotificationListAdapter.updateData(notificationListItemsByMessage);
                }
                mBtnByMessage.setSelected(true);
                mBtnByMessage.invalidate();
                mBtnBySatker.setEnabled(true);
                mBtnBySatker.invalidate();
            }
        });
        mBtnByMessage.setEnabled(true);
        mBtnByMessage.invalidate();

        Intent intent = getIntent();
        mNationalData = Util.GetSharedPreferences(NotificationListActivity.this,"national_data","");

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(NotificationListActivity.this));

        mNotificationListAdapter = new NotificationListAdapter(NotificationListActivity.this, new NotificationListAdapter.Callback() {
            @Override
            public void onItemClick(int id, String description) {
                Intent intent = new Intent(NotificationListActivity.this, SatkerActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("description", description);

                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mNotificationListAdapter);

        mBackgroundProcessLayout = findViewById(R.id.background_process_layout);
        mAvloadingIndicatorView = findViewById(R.id.avloadingIndicatorView);

        mYear = Calendar.getInstance().get(Calendar.YEAR);

        getNotificationListData(NOTIFICATION_GROUP_BY_SATKER);
    }

    private void getNotificationListData(final String group_by) {
        DataService dataService = new DataService(NotificationListActivity.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {
                mBackgroundProcessLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(GONE);
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {

            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
                List<NotificationListItem> notificationListItems = new ArrayList<>();
                if (group_by == NOTIFICATION_GROUP_BY_SATKER){
                    for (int i = 0; i < jsonArray.size(); i++){
                        JsonObject joGroup = jsonArray.get(i).asObject();

                        NotificationGroupSatkerInfo notificationGroupSatkerInfo = new NotificationGroupSatkerInfo(joGroup.get("id").asString(), joGroup.get("name").asString());
                        notificationListItems.add(notificationGroupSatkerInfo);

                        for (int j = 0; j < joGroup.get("notifications").asArray().size(); j++){
                            JsonObject joItem = joGroup.get("notifications").asArray().get(j).asObject();

                            NotificationItemSatkerInfo notificationItemSatkerInfo = new NotificationItemSatkerInfo(joGroup.get("id").asString(), joGroup.get("name").asString(), joItem.get("message").asString(), joItem.get("date_time").asString(), joItem.get("priority").asString());
                            notificationListItems.add(notificationItemSatkerInfo);
                        }
                    }
                    notificationListItemsBySatker =  notificationListItems;

                }else if (group_by == NOTIFICATION_GROUP_BY_MESSAGE){
                    for (int i = 0; i < jsonArray.size(); i++){
                        JsonObject joGroup = jsonArray.get(i).asObject();

                        NotificationGroupMessageInfo notificationGroupMessageInfo = new NotificationGroupMessageInfo(joGroup.get("message").asString(), joGroup.get("priority").asString());
                        notificationListItems.add(notificationGroupMessageInfo);

                        for (int j = 0; j < joGroup.get("satkers").asArray().size(); j++){
                            JsonObject joItem = joGroup.get("satkers").asArray().get(j).asObject();

                            NotificationItemMessageInfo notificationItemMessageInfo = new NotificationItemMessageInfo(joItem.get("id").asString(), joItem.get("name").asString(), joItem.get("notification").asObject().get("date_time").asString(), joGroup.get("priority").asString());
                            notificationListItems.add(notificationItemMessageInfo);
                        }
                    }
                    notificationListItemsByMessage =  notificationListItems;
                }

                mNotificationListAdapter.updateData(notificationListItems);

                mRecyclerView.setVisibility(View.VISIBLE);
                mBackgroundProcessLayout.setVisibility(GONE);

                mLoop = 0;
            }

            @Override
            public void OnFailed(String message) {
                if (mLoop < 3){
                    mLoop++;
                    getNotificationListData(group_by);
                }else{
                    mLoop = 0;
                    Alert.Show(getApplicationContext(), "", message);
                }
            }
        }).GetNotificationList(mYear, group_by);
    }

}
