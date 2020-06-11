package id.exorty.monira.ui.model;

public class BaseDataInfo {
    protected String mId;
    protected double mRealization;
    protected double mPrognosis;
    protected double mEndOfYear;

    public BaseDataInfo(String id, double realization, double prognosis, double endofyear){
        this.mId = id;
        this.mRealization = realization;
        this.mPrognosis = prognosis;
        this.mEndOfYear = endofyear;
    }

    public String getId(){
        return this.mId;
    }

    public double getRealization(){
        return this.mRealization;
    }

    public double getPrognosis(){
        return this.mPrognosis;
    }

    public double getEndOfYear(){
        return this.mEndOfYear;
    }

}
