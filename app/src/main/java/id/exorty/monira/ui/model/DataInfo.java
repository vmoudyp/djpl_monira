package id.exorty.monira.ui.model;

public class DataInfo extends BaseDataInfo {
    private String mDescription;
    private double mLastYear;

    public DataInfo(String id, String description, double realization, double prognosis, double lastYear){
        super(id, realization,prognosis,0d);
        this.mDescription = description;
        this.mRealization = realization;
        this.mLastYear = lastYear;
    }

    public DataInfo(String id, String description, double realization, double prognosis, double end_of_year, double lastYear){
        super(id, realization,prognosis,end_of_year);
        this.mDescription = description;
        this.mRealization = realization;
        this.mLastYear = lastYear;
    }

    public String getDescription(){
        return this.mDescription;
    }

    public Double getLastYear(){
        return this.mLastYear;
    }
}
