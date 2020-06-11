package id.exorty.monira.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.util.Calendar;

import id.exorty.monira.R;
import id.exorty.monira.helper.Util;
import id.exorty.monira.service.DataService;
import id.exorty.monira.ui.SatkerActivity;
import id.exorty.monira.ui.SatkerRankingActivity;
import id.exorty.monira.ui.components.Alert;
import id.exorty.monira.ui.components.CurrentMonthComponent;
import id.exorty.monira.ui.components.NotificationComponent;
import id.exorty.monira.ui.components.SatkerCurrentMonthComponent;
import id.exorty.monira.ui.components.SatkerTrendComponent;
import id.exorty.monira.ui.components.TrendComponent;
import id.exorty.monira.ui.components.TypeOfActivity;
import id.exorty.monira.ui.components.UnderPerformer;

import static android.view.View.GONE;

public class HomeFragment extends Fragment {

    private NotificationComponent mNotificationComponent;
    private CurrentMonthComponent mCurrentMonthComponent;
    private SatkerCurrentMonthComponent mSatkerCurrentMonthComponent;
    private TrendComponent mTrendComponent;
    private SatkerTrendComponent mSatkerTrendComponent;
    private UnderPerformer mUnderPerformer;
    private TypeOfActivity mTypeOfActivity;

    private LinearLayout mMainContent;

    private AlertDialog mDialogDataInfo;

    private boolean mIsInit;
    private int mYear;
    private int mLoop = 0;
    private int mLevel;
    private String mSatkerId;

    private int mSatkerRanking;

    private JsonObject mNationalCurrentMonthObject;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mMainContent = root.findViewById(R.id.main_content);

        mLevel = Util.GetSharedPreferences(HomeFragment.this.getContext(), "level", -1);

        TextView txtSatkerName = root.findViewById(R.id.txt_satker_name);
        if (mLevel == 3){
            txtSatkerName.setVisibility(View.VISIBLE);
        }else{
            txtSatkerName.setVisibility(GONE);
        }

        TextView txtCurrentDateTime = root.findViewById(R.id.txt_current_date_time);
        txtCurrentDateTime.setText(Util.getCurrentDateTime());
        txtCurrentDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogDataInfo.show();
            }
        });

        mNotificationComponent = new NotificationComponent(HomeFragment.this.getContext());
        mMainContent.addView(mNotificationComponent);


        if (mLevel == 3){
            mSatkerId = Util.GetSharedPreferences(HomeFragment.this.getContext(), "id_satker", "");
            String satkerName = Util.GetSharedPreferences(HomeFragment.this.getContext(), "satker_name", "");

            txtSatkerName.setText(satkerName);

            mSatkerCurrentMonthComponent = new SatkerCurrentMonthComponent(HomeFragment.this.getContext());
            mMainContent.addView(mSatkerCurrentMonthComponent);
            mTrendComponent = new TrendComponent(HomeFragment.this.getContext());
            mMainContent.addView(mTrendComponent);
            mUnderPerformer = new UnderPerformer(HomeFragment.this.getContext(), new UnderPerformer.Callback() {
                @Override
                public void onOtherSatkerClick() {
                    if (mLevel != 3) {
                        Intent intent = new Intent(getContext(), SatkerActivity.class);
                        intent.putExtra("id", "");
                        intent.putExtra("description", "");

                        getContext().startActivity(intent);
                    }
                }

                @Override
                public void onSatkerRankingClick() {
                    Intent intent = new Intent(getContext(), SatkerRankingActivity.class);
                    getContext().startActivity(intent);
                }
            });
            mMainContent.addView(mUnderPerformer);
        }else {
            mCurrentMonthComponent = new CurrentMonthComponent(HomeFragment.this.getContext());
            mMainContent.addView(mCurrentMonthComponent);
            mTrendComponent = new TrendComponent(HomeFragment.this.getContext());
            mMainContent.addView(mTrendComponent);
            mUnderPerformer = new UnderPerformer(HomeFragment.this.getContext(), new UnderPerformer.Callback() {
                @Override
                public void onOtherSatkerClick() {
                    Intent intent = new Intent(getContext(), SatkerActivity.class);
                    intent.putExtra("id", -1);
                    intent.putExtra("description", "");

                    getContext().startActivity(intent);
                }

                @Override
                public void onSatkerRankingClick() {
                    Intent intent = new Intent(getContext(), SatkerRankingActivity.class);
                    getContext().startActivity(intent);
                }
            });
            mMainContent.addView(mUnderPerformer);
            mTypeOfActivity = new TypeOfActivity(HomeFragment.this.getContext());
            mMainContent.addView(mTypeOfActivity);
        }

        mYear = Calendar.getInstance().get(Calendar.YEAR);

        mIsInit = true;

        String national_data = Util.GetSharedPreferences(HomeFragment.this.getContext(), "national_data","");
        String national_data_current_month = Util.GetSharedPreferences(HomeFragment.this.getContext(), "national_data_current_month","");

        if (national_data.equals("")) {
            getNationalData();
        }else{
            try {
                JsonObject jsonObject = Json.parse(national_data).asObject();
                initContent(jsonObject);
            }catch (Exception e){
                getNationalData();
            }
        }

        return root;
    }

    private void initContent(JsonObject jsonObject){
        try {
            View dataInfoLayout = getLayoutInflater().inflate(R.layout.custom_dialog_data_info, null);

            TextView txtFilled = dataInfoLayout.findViewById(R.id.txt_filled);
            txtFilled.setText(String.valueOf(jsonObject.get("contribution").asObject().get("filled").asInt()));

            TextView txtNotFilled = dataInfoLayout.findViewById(R.id.txt_not_filled);
            txtNotFilled.setText(String.valueOf(jsonObject.get("contribution").asObject().get("not_filled").asInt()));

            TextView txtUnderFiftyPercent = dataInfoLayout.findViewById(R.id.txt_under_fifty_percent);
            txtUnderFiftyPercent.setText(String.valueOf(jsonObject.get("contribution").asObject().get("under_50_percentage").asInt()));

            TextView txtTotal = dataInfoLayout.findViewById(R.id.txt_total);
            txtTotal.setText(String.valueOf(jsonObject.get("contribution").asObject().get("total_satker").asInt()));

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
            builder.setTitle(R.string.dashboard_data_info);

            builder.setView(dataInfoLayout);
            builder.setNegativeButton(R.string.material_dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            mDialogDataInfo = builder.create();

            mNotificationComponent.setVisibility(GONE);
            if (jsonObject.names().contains("notifications")) {
                if (!jsonObject.get("notifications").asArray().isEmpty()) {
                    mNotificationComponent.updateData(jsonObject.get("notifications").asArray());
                    mNotificationComponent.setVisibility(View.VISIBLE);
                }
            }

            mNationalCurrentMonthObject = jsonObject.get("current_month").asObject();

            mTrendComponent.createData(jsonObject.get("trend").asObject());
            mUnderPerformer.updateData(jsonObject.get("under_perfomer").asArray());

            if (mLevel == 3) {
                getSatkerData();
            } else {
                mTypeOfActivity.updateData(jsonObject.get("by_activity").asArray());
                mCurrentMonthComponent.updateData(mNationalCurrentMonthObject);
            }
        }catch (Exception e){
            Alert.Show(getContext(), "", e.getMessage());
        }
    }

    private void getNationalData(){

        DataService dataService = new DataService(HomeFragment.this.getActivity(), new DataService.DataServiceListener() {
            @Override
            public void onStart() {
                if (!mIsInit) {
                    if (mLevel == 3){
                        mSatkerCurrentMonthComponent.startUpdateData();
                    }else {
                        mCurrentMonthComponent.startUpdateData();
                        mTrendComponent.startUpdateDate();
                    }
                }
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
                try {
                    View dataInfoLayout = getLayoutInflater().inflate(R.layout.custom_dialog_data_info, null);

                    TextView txtFilled = dataInfoLayout.findViewById(R.id.txt_filled);
                    txtFilled.setText(String.valueOf(jsonObject.get("contribution").asObject().get("filled").asInt()));

                    TextView txtNotFilled = dataInfoLayout.findViewById(R.id.txt_not_filled);
                    txtNotFilled.setText(String.valueOf(jsonObject.get("contribution").asObject().get("not_filled").asInt()));

                    TextView txtUnderFiftyPercent = dataInfoLayout.findViewById(R.id.txt_under_fifty_percent);
                    txtUnderFiftyPercent.setText(String.valueOf(jsonObject.get("contribution").asObject().get("under_50_percentage").asInt()));

                    TextView txtTotal = dataInfoLayout.findViewById(R.id.txt_total);
                    txtTotal.setText(String.valueOf(jsonObject.get("contribution").asObject().get("total_satker").asInt()));

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
                    builder.setTitle(R.string.dashboard_data_info);

                    builder.setView(dataInfoLayout);
                    builder.setNegativeButton(R.string.material_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    mDialogDataInfo = builder.create();

                    mNotificationComponent.setVisibility(GONE);
                    if (jsonObject.names().contains("notifications"))
                        if (!jsonObject.get("notifications").asArray().isEmpty()) {
                            mNotificationComponent.updateData(jsonObject.get("notifications").asArray());
                            mNotificationComponent.setVisibility(View.VISIBLE);
                        }

                    Util.SaveSharedPreferences(HomeFragment.this.getContext(), "national_data", jsonObject.toString());
                    Util.SaveSharedPreferences(HomeFragment.this.getContext(), "national_data_current_month", jsonObject.get("current_month").asObject().toString());

                    mNationalCurrentMonthObject = jsonObject.get("current_month").asObject();

                    mUnderPerformer.updateData(jsonObject.get("under_perfomer").asArray());
                    mTypeOfActivity.updateData(jsonObject.get("by_activity").asArray());

                    if (mLevel == 3) {
                        getSatkerData();
                    } else {
                        mCurrentMonthComponent.updateData(mNationalCurrentMonthObject);
                        mTrendComponent.createData(jsonObject.get("trend").asObject());
                    }
                    mIsInit = false;
                    mLoop = 0;
                }catch (Exception e){
                    mLoop = 0;
                    Alert.Show(getContext(), "", e.getMessage());
                }
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
                mIsInit = false;
            }

            @Override
            public void OnFailed(String message) {
                mIsInit = false;

                if (mLoop < 3){
                    mLoop++;
                    getNationalData();
                }else{
                    mLoop = 0;
                    Alert.Show(getContext(), "", message);
                }
            }
        }).GetNationalData(mYear);
    }

    private void getSatkerData(){
        DataService dataService = new DataService(HomeFragment.this.getActivity(), new DataService.DataServiceListener() {
            @Override
            public void onStart() {
                if (!mIsInit) {
                    mSatkerCurrentMonthComponent.startUpdateData();
                    mTrendComponent.startUpdateDate();
                }
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {

                mSatkerCurrentMonthComponent.updateData(mNationalCurrentMonthObject, jsonObject);
                mTrendComponent.updateData(jsonObject.get("trend").asObject());

                mIsInit = false;
                mLoop = 0;

                if (mLevel == 3){
                    getSatkerRanking();
                }
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
                mIsInit = false;
            }

            @Override
            public void OnFailed(String message) {
                mIsInit = false;

                if (mLoop < 3){
                    mLoop++;
                    getSatkerData();
                }else{
                    mLoop = 0;
                    Alert.Show(getContext(), "", message);
                }
            }
        }).GetSatkerData(mSatkerId, mYear);
    }

    private void getSatkerRanking(){
        DataService dataService = new DataService(HomeFragment.this.getActivity(), new DataService.DataServiceListener() {
            @Override
            public void onStart() {
                if (!mIsInit) {
                }
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
                mUnderPerformer.updateRankingInfo(jsonObject.get("current_month").asObject().get("rank").asInt(), jsonObject.get("current_month").asObject().get("color").asString());

                mIsInit = false;
                mLoop = 0;
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
                mIsInit = false;
            }

            @Override
            public void OnFailed(String message) {
                mIsInit = false;

                if (mLoop < 3){
                    mLoop++;
                    getSatkerRanking();
                }else{
                    mLoop = 0;
                    Alert.Show(getContext(), "", message);
                }
            }
        }).GetSatkerRanking(mYear, mSatkerId);
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
