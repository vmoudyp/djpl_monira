package id.exorty.monira.ui.typeofactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.helper.Util;
import id.exorty.monira.service.DataService;
import id.exorty.monira.ui.SatkerActivity;
import id.exorty.monira.ui.TypeOfActivityActivity;
import id.exorty.monira.ui.components.CurrentMonthComponent;
import id.exorty.monira.ui.components.TrendComponent;
import id.exorty.monira.ui.home.HomeFragment;
import id.exorty.monira.ui.model.DashboardData;
import id.exorty.monira.ui.model.DataInfo;
import id.exorty.monira.ui.model.ViewModelParam;

import static id.exorty.monira.helper.Util.GetSharedPreferences;
import static id.exorty.monira.helper.Util.SaveSharedPreferences;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_DATA = "data";
    private static int mIndex = 1;
    private static JsonObject mData = null;

    private static CurrentMonthComponent mCurrentMonthComponent;
    private static TrendComponent mTrendComponent;

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index, JsonObject jsonObject) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        if (jsonObject != null)
            bundle.putString(ARG_DATA, jsonObject.toString());
        else
            bundle.putString(ARG_DATA, null);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_SECTION_NUMBER);
            if (getArguments().getString(ARG_DATA) != null)
                mData = Json.parse(getArguments().getString(ARG_DATA)).asObject();
            else
                mData = null;
        }
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);

        JsonObject jsonObject = Json.object();
        jsonObject.add("index", mIndex);
        if (mData != null)
            jsonObject.add("data", mData);
        pageViewModel.setIndex(jsonObject);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_type_of_activity, container, false);
        final LinearLayout tabContent = root.findViewById(R.id.tab_content);

        mCurrentMonthComponent = new CurrentMonthComponent(PlaceholderFragment.this.getContext());
        tabContent.addView(mCurrentMonthComponent);

        mTrendComponent = new TrendComponent(PlaceholderFragment.this.getContext());
        tabContent.addView(mTrendComponent);

        pageViewModel.getData().observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(@Nullable JsonObject jsonObject) {
                int index = jsonObject.get("index").asInt();
                if (jsonObject.names().contains("data")) {
                    JsonObject data = jsonObject.get("data").asObject();
                    switch (index) {
                        case 1:
                            mCurrentMonthComponent.updateData(data.get("all").asObject().get("current_month").asObject());
                            mTrendComponent.createData(data.get("all").asObject().get("trend").asObject());
                            break;
                        case 2:
                            mCurrentMonthComponent.updateData(data.get("employee").asObject().get("current_month").asObject());
                            mTrendComponent.createData(data.get("employee").asObject().get("trend").asObject());
                            break;
                        case 3:
                            mCurrentMonthComponent.updateData(data.get("goods").asObject().get("current_month").asObject());
                            mTrendComponent.createData(data.get("goods").asObject().get("trend").asObject());
                            break;
                        case 4:
                            mCurrentMonthComponent.updateData(data.get("capital").asObject().get("current_month").asObject());
                            mTrendComponent.createData(data.get("capital").asObject().get("trend").asObject());
                            break;
                    }
                }

//                currentMonthComponent.invalidate();
//                trendComponent.invalidate();
            }
        });

        return root;
    }

    public void updateDate(JsonObject jsonObject){
        switch (mIndex) {
            case 1:
                mCurrentMonthComponent.updateData(jsonObject.get("all").asObject().get("current_month").asObject());
                mTrendComponent.createData(jsonObject.get("all").asObject().get("trend").asObject());
                break;
            case 2:
                mCurrentMonthComponent.updateData(jsonObject.get("employee").asObject().get("current_month").asObject());
                mTrendComponent.createData(jsonObject.get("employee").asObject().get("trend").asObject());
                break;
            case 3:
                mCurrentMonthComponent.updateData(jsonObject.get("goods").asObject().get("current_month").asObject());
                mTrendComponent.createData(jsonObject.get("goods").asObject().get("trend").asObject());
                break;
            case 4:
                mCurrentMonthComponent.updateData(jsonObject.get("capital").asObject().get("current_month").asObject());
                mTrendComponent.createData(jsonObject.get("capital").asObject().get("trend").asObject());
                break;
        }
    }
}