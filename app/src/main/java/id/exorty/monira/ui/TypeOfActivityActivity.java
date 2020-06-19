package id.exorty.monira.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.google.android.material.tabs.TabLayout;
import com.wang.avi.AVLoadingIndicatorView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.exorty.monira.R;
import id.exorty.monira.helper.Util;
import id.exorty.monira.service.DataService;
import id.exorty.monira.ui.components.Alert;
import id.exorty.monira.ui.model.ListItem;
import id.exorty.monira.ui.model.SatkerInfo;
import id.exorty.monira.ui.typeofactivity.SectionsPagerAdapter;

import static android.view.View.GONE;
import static id.exorty.monira.helper.Util.GetSharedPreferences;
import static id.exorty.monira.helper.Util.SaveSharedPreferences;

public class TypeOfActivityActivity extends AppCompatActivity {
    private final String CUSTOM_ADAPTER_TEXT = "text";

    private TextView mTxtTypeActivityName;
    private String mIdTypeOfActivity;

    private int mYear;
    private String[] mTypeOfActivityIds;
    private String[] mTypeOfActivityNames;

    private TabLayout mTabs;
    private LinearLayout mBackgroundProcessLayout;
    private AVLoadingIndicatorView mAvloadingIndicatorView;
    private LinearLayout mTryAgainView;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean isFirstTime = true;

    private int mLoop = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_type_of_activity);

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

        Intent intent = getIntent();

        mIdTypeOfActivity = intent.getStringExtra("id");
        String descriptionn = intent.getStringExtra("description");

        mTypeOfActivityIds = intent.getStringArrayExtra("ids");
        mTypeOfActivityNames = intent.getStringArrayExtra("descriptions");

        List<Map<String, Object>> dialogItemList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < mTypeOfActivityIds.length; i++){
            Map<String, Object> itemMap = new HashMap<String, Object>();
            itemMap.put(CUSTOM_ADAPTER_TEXT, mTypeOfActivityNames[i]);

            dialogItemList.add(itemMap);
        }

        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, dialogItemList,
                R.layout.list_view_adapter_layout,
                new String[]{CUSTOM_ADAPTER_TEXT},
                new int[]{R.id.txt_item});

        String token = GetSharedPreferences(TypeOfActivityActivity.this, "token", "");

        mYear = Util.GetSharedPreferences(TypeOfActivityActivity.this, "year", Calendar.getInstance().get(Calendar.YEAR));
        TextView txtFinancialYear = findViewById(R.id.txt_financial_year);
        txtFinancialYear.setText("Tahun data : " + String.valueOf(mYear));

        SaveSharedPreferences(TypeOfActivityActivity.this, "id_type_of_activity", String.valueOf(mIdTypeOfActivity));

        mTxtTypeActivityName = findViewById(R.id.txt_activity_description);
        mTxtTypeActivityName.setText(descriptionn);
        mTxtTypeActivityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TypeOfActivityActivity.this, R.style.AlertDialogCustom);

                builder.setTitle("Silahkan pilih Jenis Kegiatan");
                builder.setPositiveButton(R.string.material_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mIdTypeOfActivity = mTypeOfActivityIds[which];
                        mTxtTypeActivityName.setText(mTypeOfActivityNames[which]);

                        getData();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        TextView txtCurrentDateTime = findViewById(R.id.txt_current_date_time);
        txtCurrentDateTime.setText(Util.getCurrentDateTime());

        mBackgroundProcessLayout = findViewById(R.id.background_process_layout);
        mAvloadingIndicatorView = findViewById(R.id.avloadingIndicatorView);
        mTryAgainView = findViewById(R.id.layout_try_again);
        mTryAgainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        mTabs = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.view_pager);

        getData();
    }

    private void getData(){
        DataService dataService = new DataService(TypeOfActivityActivity.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {
                mViewPager.setVisibility(GONE);
                mBackgroundProcessLayout.setVisibility(View.VISIBLE);
                mAvloadingIndicatorView.setVisibility(View.VISIBLE);
                mTryAgainView.setVisibility(GONE);
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
                if (isFirstTime) {
                    mSectionsPagerAdapter = new SectionsPagerAdapter(TypeOfActivityActivity.this, getSupportFragmentManager(), jsonObject);
                    mSectionsPagerAdapter.notifyDataSetChanged();
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                    mTabs.setupWithViewPager(mViewPager);
                    isFirstTime = false;
                }else{
                    mSectionsPagerAdapter.updateData(jsonObject);
                }

                mViewPager.setVisibility(View.VISIBLE);
                mBackgroundProcessLayout.setVisibility(GONE);

                mLoop = 0;
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
                String a = message;
            }

            @Override
            public void OnFailed(String message, String fullMessage) {
//                if (mLoop < 3){
//                    mLoop++;
//                    getData();
//                }else{
                    mLoop = 0;
                    mViewPager.setVisibility(GONE);
                    mBackgroundProcessLayout.setVisibility(View.VISIBLE);
                    mAvloadingIndicatorView.setVisibility(GONE);
                    mTryAgainView.setVisibility(View.VISIBLE);
//                }
            }
        }).GetActivityDetailData(mIdTypeOfActivity, mYear);
    }
}