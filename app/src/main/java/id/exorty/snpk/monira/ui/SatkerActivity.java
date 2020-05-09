package id.exorty.snpk.monira.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.exorty.snpk.monira.R;
import id.exorty.snpk.monira.helper.Util;
import id.exorty.snpk.monira.service.DataService;
import id.exorty.snpk.monira.ui.components.CurrentMonthComponent;
import id.exorty.snpk.monira.ui.components.TrendComponent;
import id.exorty.snpk.monira.ui.home.HomeFragment;
import id.exorty.snpk.monira.ui.model.DashboardData;
import id.exorty.snpk.monira.ui.model.DataInfo;

import static id.exorty.snpk.monira.helper.Util.SaveSharedPreferences;

public class SatkerActivity extends AppCompatActivity {
    private static CurrentMonthComponent mCurrentMonthComponent;
    private static TrendComponent mTrendComponent;
    private static int mIdSatker;
    private static int mYear;
    private static String[] mSatkerNames;
    private static List<String> mSatkerIds;
    private TextView mTxtSatkerName;

    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satker);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmer();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView btnBack = findViewById(R.id.menu_icon);
        btnBack .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        mIdSatker = intent.getIntExtra("id", -1);
        String descriptionn = intent.getStringExtra("description");


        mTxtSatkerName = findViewById(R.id.txt_satker_name);
        mTxtSatkerName.setText(descriptionn);
        mTxtSatkerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SatkerActivity.this);
                builder.setTitle("Silahkan pilih Satker");
                builder.setItems(mSatkerNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mIdSatker = Integer.valueOf(mSatkerIds.get(which));
                        mTxtSatkerName.setText(mSatkerNames[which]);

                        getData(false);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        TextView txtCurrentDateTime = findViewById(R.id.txt_current_date_time);
        txtCurrentDateTime.setText(Util.getCurrentDateTime());

        LinearLayout mainContent = findViewById(R.id.main_content);

        mCurrentMonthComponent = new CurrentMonthComponent(SatkerActivity.this);
        mCurrentMonthComponent.setVisibility(View.GONE);
        mainContent.addView(mCurrentMonthComponent);

        mTrendComponent = new TrendComponent(SatkerActivity.this);
        mTrendComponent.setVisibility(View.GONE);
        mainContent.addView(mTrendComponent);

        mYear = Calendar.getInstance().get(Calendar.YEAR);

        getDataSatkerList();
        getData(true);
    }

    private void getDataSatkerList() {
        DataService dataService = new DataService(SatkerActivity.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
                List<String> satkerNames = new ArrayList<String>();
                mSatkerIds = new ArrayList<String>();
                for (JsonValue jv : jsonArray) {
                    JsonObject jo = jv.asObject();

                    mSatkerIds.add(jo.get("id").asString());
                    satkerNames.add(jo.get("name").asString());

                }
                mSatkerNames = new String[satkerNames.size()];
                satkerNames.toArray(mSatkerNames);
            }

            @Override
            public void OnFailed(String message) {

            }
        }).GetListOfSatker();
    }

    private void getData(boolean isInit){
        DataService dataService = new DataService(SatkerActivity.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
                mCurrentMonthComponent.updateData(jsonObject);
                if (isInit)
                    mTrendComponent.createData(jsonObject.get("trend").asObject());
                else
                    mTrendComponent.updateData(jsonObject.get("trend").asObject());

                mCurrentMonthComponent.setVisibility(View.VISIBLE);
                mTrendComponent.setVisibility(View.VISIBLE);

                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {

            }

            @Override
            public void OnFailed(String message) {

            }
        }).GetSatkerData(mIdSatker, mYear);
    }
}
