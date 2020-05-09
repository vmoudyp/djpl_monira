package id.exorty.snpk.monira.ui.typeofactivity;

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

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.List;

import id.exorty.snpk.monira.R;
import id.exorty.snpk.monira.helper.Util;
import id.exorty.snpk.monira.service.DataService;
import id.exorty.snpk.monira.ui.SatkerActivity;
import id.exorty.snpk.monira.ui.TypeOfActivityActivity;
import id.exorty.snpk.monira.ui.components.CurrentMonthComponent;
import id.exorty.snpk.monira.ui.components.TrendComponent;
import id.exorty.snpk.monira.ui.home.HomeFragment;
import id.exorty.snpk.monira.ui.model.DashboardData;
import id.exorty.snpk.monira.ui.model.DataInfo;
import id.exorty.snpk.monira.ui.model.ViewModelParam;

import static id.exorty.snpk.monira.helper.Util.GetSharedPreferences;
import static id.exorty.snpk.monira.helper.Util.SaveSharedPreferences;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static CurrentMonthComponent mCurrentMonthComponent;
    private static TrendComponent mTrendComponent;
    private static int mIdTypeOfActivity;
    private static int mYear;
    private static String[] mTypeOfActivityNames;
    private static List<String> mTypeOfActivityIds;
    private static int mIndex;
    private static JsonObject mData;

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index, JsonObject data) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        mData = data;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        mIndex = 1;
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_SECTION_NUMBER);
        }

//        ViewModelParam viewModelParam = new ViewModelParam();
//        viewModelParam.index = mIndex;

        pageViewModel.setIndex(mIndex);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_type_of_activity, container, false);
        final LinearLayout tabContent = root.findViewById(R.id.tab_content);

        int id_type_of_activity = Integer.valueOf(GetSharedPreferences(getActivity(), "id_type_of_activity", "-1"));
        int year = Integer.valueOf(GetSharedPreferences(getActivity(), "year", "0"));

        mCurrentMonthComponent = new CurrentMonthComponent(PlaceholderFragment.this.getContext());
        tabContent.addView(mCurrentMonthComponent);

        mTrendComponent = new TrendComponent(PlaceholderFragment.this.getContext());
        tabContent.addView(mTrendComponent);

        pageViewModel.getData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer index) {
                switch (index){
                    case 1:
                        mCurrentMonthComponent.updateData(mData.get("all").asObject().get("current_month").asObject());
                        mTrendComponent.createData(mData.get("all").asObject().get("trend").asObject());
                        break;
                    case 2:
                        mCurrentMonthComponent.updateData(mData.get("employee").asObject().get("current_month").asObject());
                        mTrendComponent.createData(mData.get("employee").asObject().get("trend").asObject());
                        break;
                    case 3:
                        mCurrentMonthComponent.updateData(mData.get("goods").asObject().get("current_month").asObject());
                        mTrendComponent.createData(mData.get("goods").asObject().get("trend").asObject());
                        break;
                    case 4:
                        mCurrentMonthComponent.updateData(mData.get("capital").asObject().get("current_month").asObject());
                        mTrendComponent.createData(mData.get("capital").asObject().get("trend").asObject());
                        break;
                }

            }
        });

//
//        pageViewModel.getData().observe(this, new Observer<JsonObject>() {
//            @Override
//            public void onChanged(@Nullable JsonObject jsonObject) {
//                String a = "";
//            }
//        });


        return root;
    }

    private void getData(boolean isInit){
    }
}