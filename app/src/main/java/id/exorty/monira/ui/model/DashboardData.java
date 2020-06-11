package id.exorty.monira.ui.model;

import java.util.ArrayList;
import java.util.List;

public class DashboardData {
    private int mTypeOfActivityId;
    private String mDescription;
    private double mCurrentRealization;
    private double mCurrentPrognosis;
    private double mEndOfYear;
    private List<DataInfo> mHistoricalData = new ArrayList<DataInfo>();

    public DashboardData(double currentRealization, double currentPrognosis, double endOfYear){
        this.mCurrentRealization = currentRealization;
        this.mCurrentPrognosis = currentPrognosis;
        this.mEndOfYear = endOfYear;
    }

    public DashboardData(int typeOfActivityId, String description, double currentRealization, double currentPrognosis, double endOfYear){
        this.mTypeOfActivityId = typeOfActivityId;
        this.mDescription = description;
        this.mCurrentRealization = currentRealization;
        this.mCurrentPrognosis = currentPrognosis;
        this.mEndOfYear = endOfYear;
    }

    public double getCurrentRealization(){
        return mCurrentRealization;
    }

    public double getCurrentPrognosis(){
        return mCurrentPrognosis;
    }

    public double getEndOfYear(){
        return mEndOfYear;
    }

    public List<DataInfo> getHistoricalData(){
        return mHistoricalData;
    }

    public void AddHistoryData(DataInfo dataInfo){
        this.mHistoricalData.add(dataInfo);
    }

    public void AddHistoryData(List<DataInfo> dataInfos){
        this.mHistoricalData.addAll(dataInfos);
    }
}
