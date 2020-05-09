package id.exorty.snpk.monira.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import id.exorty.snpk.monira.R;
import id.exorty.snpk.monira.helper.Util;
import id.exorty.snpk.monira.service.DataService;
import id.exorty.snpk.monira.ui.components.CurrentMonthComponent;
import id.exorty.snpk.monira.ui.components.TrendComponent;
import id.exorty.snpk.monira.ui.components.TypeOfActivity;
import id.exorty.snpk.monira.ui.typeofactivity.PlaceholderFragment;
import id.exorty.snpk.monira.ui.typeofactivity.SectionsPagerAdapter;

import static id.exorty.snpk.monira.helper.Util.SaveSharedPreferences;

public class TypeOfActivityActivity extends AppCompatActivity {
    private static TextView mTxtTypeActivityName;
    private static int mIdTypeOfActivity;
    private static int mYear;
    private static String[] mTypeOfActivityNames;
    private static List<String> mTypeOfActivityIds;

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
        int id = intent.getIntExtra("id", -1);
        String descriptionn = intent.getStringExtra("description");

        int year = Calendar.getInstance().get(Calendar.YEAR);

        SaveSharedPreferences(TypeOfActivityActivity.this, "id_type_of_activity", String.valueOf(id));
        SaveSharedPreferences(TypeOfActivityActivity.this, "year", String.valueOf(year));

        mTxtTypeActivityName = findViewById(R.id.txt_activity_description);
        mTxtTypeActivityName.setText(descriptionn);
        mTxtTypeActivityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        TextView txtCurrentDateTime = findViewById(R.id.txt_current_date_time);
        txtCurrentDateTime.setText(Util.getCurrentDateTime());

        DataService dataService = new DataService(TypeOfActivityActivity.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
                SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(TypeOfActivityActivity.this, getSupportFragmentManager(), jsonObject);
                ViewPager viewPager = findViewById(R.id.view_pager);
                viewPager.setAdapter(sectionsPagerAdapter);

                TabLayout tabs = findViewById(R.id.tabs);
                tabs.setupWithViewPager(viewPager);
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {

            }

            @Override
            public void OnFailed(String message) {

            }
        }).GetActivityDetailData(id,year);
    }
}