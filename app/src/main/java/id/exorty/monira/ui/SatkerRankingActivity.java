package id.exorty.monira.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.helper.Util;
import id.exorty.monira.service.DataService;
import id.exorty.monira.ui.adapter.SatkerRankingAdapter;
import id.exorty.monira.ui.components.Alert;
import id.exorty.monira.ui.model.SatkerRankingInfo;

import static android.view.View.GONE;

public class SatkerRankingActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SatkerRankingAdapter mSatkerRankingAdapter;

    private LinearLayout mBackgroundProcessLayout;
    private AVLoadingIndicatorView mAvloadingIndicatorView;

    private int mYear;

    private int mLoop = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satker_ranking);

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

        TextView txtCurrentDateTime = findViewById(R.id.txt_current_date_time);
        txtCurrentDateTime.setText(Util.getCurrentDateTime());

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SatkerRankingActivity.this));

        mSatkerRankingAdapter = new SatkerRankingAdapter(SatkerRankingActivity.this, R.layout.satker_ranking_item, new SatkerRankingAdapter.Callback() {
            @Override
            public void onClick(String satker_id, String satker_name) {
                Intent intent = new Intent(SatkerRankingActivity.this, SatkerActivity.class);
                intent.putExtra("id", satker_id);
                intent.putExtra("description", satker_name);

                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mSatkerRankingAdapter);

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
                mSatkerRankingAdapter.filter(s.toString());
            }
        });


        mBackgroundProcessLayout = findViewById(R.id.background_process_layout);
        mAvloadingIndicatorView = findViewById(R.id.avloadingIndicatorView);

        mYear = Util.GetSharedPreferences(SatkerRankingActivity.this, "year", Calendar.getInstance().get(Calendar.YEAR));
        TextView txtFinancialYear = findViewById(R.id.txt_financial_year);
        txtFinancialYear.setText("Tahun data : " + String.valueOf(mYear));

        getSatkerRankingData();
    }

    private void getSatkerRankingData() {
        DataService dataService = new DataService(SatkerRankingActivity.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {
                mBackgroundProcessLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(GONE);
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
                List<String> satkerIds = new ArrayList<>();
                for (int i = 0; i < jsonObject.get("previous_month").asArray().values().size(); i++){
                    satkerIds.add(jsonObject.get("previous_month").asArray().get(i).asObject().get("id").asString());
                }


                List<SatkerRankingInfo> satkerRankingInfos = new ArrayList<SatkerRankingInfo>();
                for (int i = 0; i < jsonObject.get("current_month").asArray().values().size(); i++){
                    JsonObject currentMonth = jsonObject.get("current_month").asArray().get(i).asObject();

                    String id = currentMonth.get("id").asString();

                    int pos = satkerIds.indexOf(id);

                    int progress = 0;
                    if (pos > i)
                        progress = 1;
                    else if (pos < i)
                        progress = -1;

                    SatkerRankingInfo satkerRankingInfo = new SatkerRankingInfo(i + 1, currentMonth.get("id").asString(), currentMonth.get("name").asString(), progress, currentMonth.get("color").asString());
                    satkerRankingInfos.add(satkerRankingInfo);
                }

                mSatkerRankingAdapter.updateData(satkerRankingInfos);

                mRecyclerView.setVisibility(View.VISIBLE);

                mBackgroundProcessLayout.setVisibility(GONE);

                mLoop = 0;
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
            }

            @Override
            public void OnFailed(String message, String fullMessage) {
                if (mLoop < 3){
                    mLoop++;
                    getSatkerRankingData();
                }else{
                    mLoop = 0;
                    Alert.Show(getApplicationContext(), "", message);
                }
            }
        }).GetSatkerRanking(mYear);
    }
}
