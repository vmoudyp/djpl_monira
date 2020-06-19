package id.exorty.monira.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import id.exorty.monira.ui.home.HomeFragment;
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
    private LinearLayout mTryAgainView;

    private int mYear;

    private int mLoop = 0;

    private String mNationalData;

    private Button mBtnBySatker;
    private Button mBtnByMessage;

    private String mGroupBy = NOTIFICATION_GROUP_BY_SATKER;
    private JsonArray mBySatkerJsonArray;
    private JsonArray mByMessageJsonArray;

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
                mGroupBy = NOTIFICATION_GROUP_BY_SATKER;
                if (mBySatkerJsonArray == null)
                    getNotificationListData(mGroupBy);
                else
                    mNotificationListAdapter.updateData(mBySatkerJsonArray, mGroupBy);
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
                mGroupBy = NOTIFICATION_GROUP_BY_MESSAGE;
                if (mByMessageJsonArray == null)
                    getNotificationListData(mGroupBy);
                else
                    mNotificationListAdapter.updateData(mByMessageJsonArray, mGroupBy);

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
            public void onItemClick(String id, String description) {
                Intent intent = new Intent(NotificationListActivity.this, SatkerActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("description", description);

                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mNotificationListAdapter);

        EditText editText =findViewById(R.id.edit_query);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mNotificationListAdapter.filter(s.toString(), mGroupBy);
            }
        });


        mBackgroundProcessLayout = findViewById(R.id.background_process_layout);
        mAvloadingIndicatorView = findViewById(R.id.avloadingIndicatorView);
        mTryAgainView = findViewById(R.id.layout_try_again);
        mTryAgainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNotificationListData(NOTIFICATION_GROUP_BY_SATKER);
            }
        });

        mYear = Util.GetSharedPreferences(NotificationListActivity.this, "year", Calendar.getInstance().get(Calendar.YEAR));

        getNotificationListData(NOTIFICATION_GROUP_BY_SATKER);
    }

    private void getNotificationListData(final String group_by) {
        DataService dataService = new DataService(NotificationListActivity.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {
                mRecyclerView.setVisibility(GONE);
                mBackgroundProcessLayout.setVisibility(View.VISIBLE);
                mAvloadingIndicatorView.setVisibility(View.VISIBLE);
                mTryAgainView.setVisibility(GONE);
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {

            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
                //mNotificationListAdapter.updateData(notificationListItems);
                if (group_by == NOTIFICATION_GROUP_BY_SATKER)
                    mBySatkerJsonArray = jsonArray;
                else
                    mByMessageJsonArray = jsonArray;

                mNotificationListAdapter.updateData(jsonArray, group_by);

                mRecyclerView.setVisibility(View.VISIBLE);
                mBackgroundProcessLayout.setVisibility(GONE);

                mLoop = 0;
            }

            @Override
            public void OnFailed(String message, String fullMessage) {
//                if (mLoop < 3){
//                    mLoop++;
//                    getNotificationListData(group_by);
//                }else{
                    mLoop = 0;
                    mRecyclerView.setVisibility(GONE);
                    mBackgroundProcessLayout.setVisibility(View.VISIBLE);
                    mAvloadingIndicatorView.setVisibility(GONE);
                    mTryAgainView.setVisibility(View.VISIBLE);
//                }
            }
        }).GetNotificationList(mYear, group_by);
    }

}
