package id.exorty.snpk.monira.ui.model;

public class BaseDataInfo {
    protected int mId;
    protected double mRealization;
    protected double mPrognosis;
    protected double mEndOfYear;

    public BaseDataInfo(int id, double realization, double prognosis, double endofyear){
        this.mId = id;
        this.mRealization = realization;
        this.mPrognosis = prognosis;
        this.mEndOfYear = endofyear;
    }

    public int getId(){
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
