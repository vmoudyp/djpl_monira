package id.exorty.snpk.monira.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChartView;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Calendar;

import az.plainpie.PieView;
import az.plainpie.Util;
import id.exorty.snpk.monira.R;
import id.exorty.snpk.monira.service.DataService;
import id.exorty.snpk.monira.ui.components.CurrentMonthComponent;
import id.exorty.snpk.monira.ui.components.TrendComponent;
import id.exorty.snpk.monira.ui.components.TypeOfActivity;
import id.exorty.snpk.monira.ui.components.UnderPerformer;
import static id.exorty.snpk.monira.helper.Util.getCurrentDateTime;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private PieView mPieViewRealization;
    private PieView mPieViewPrognosis;
    private PieView mPieViewEndOfYear;

    private AnyChartView anyChartView;

    private CurrentMonthComponent mCurrentMonthComponent;
    private TrendComponent mTrendComponent;
    private UnderPerformer mUnderPerformer;
    private TypeOfActivity mTypeOfActivity;

    private LinearLayout mMainContent;

    private boolean mIsInit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mMainContent = root.findViewById(R.id.main_content);

        mCurrentMonthComponent = new CurrentMonthComponent(HomeFragment.this.getContext());
        mMainContent.addView(mCurrentMonthComponent);
        mTrendComponent = new TrendComponent(HomeFragment.this.getContext());
        mMainContent.addView(mTrendComponent);
        mUnderPerformer = new UnderPerformer(HomeFragment.this.getContext());
        mMainContent.addView(mUnderPerformer);
        mTypeOfActivity = new TypeOfActivity(HomeFragment.this.getContext());
        mMainContent.addView(mTypeOfActivity);

        int year = Calendar.getInstance().get(Calendar.YEAR);

        mIsInit = true;

        getData(year);

        return root;
    }

    private void getData(int year){

        DataService dataService = new DataService(HomeFragment.this.getActivity(), new DataService.DataServiceListener() {
            @Override
            public void onStart() {
                if (!mIsInit) {
                    mCurrentMonthComponent.startUpdateData();
                    mTrendComponent.startUpdateDate();
                }
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
                mCurrentMonthComponent.updateData(jsonObject.get("current_month").asObject());
                mTrendComponent.createData(jsonObject.get("trend").asObject());
                mUnderPerformer.UpdateData(jsonObject.get("under_perfomer").asArray());
                mTypeOfActivity.UpdateData(jsonObject.get("by_activity").asArray());

                mIsInit = false;
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
                mIsInit = false;
            }

            @Override
            public void OnFailed(String message) {
                mIsInit = false;
            }
        }).GetNationalData(year);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
