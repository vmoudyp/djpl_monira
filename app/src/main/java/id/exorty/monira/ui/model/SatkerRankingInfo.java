package id.exorty.monira.ui.model;

public class SatkerRankingInfo {
    public int rank;
    public String satker_id;
    public String satker_name;
    public int progress_status;
    public String color;

    public SatkerRankingInfo(int rank, String satker_id, String satker_name, int progress_status, String color){
        this.rank = rank;
        this.satker_id = satker_id;
        this.satker_name = satker_name;
        this.progress_status = progress_status;
        this.color = color;
    }
}
