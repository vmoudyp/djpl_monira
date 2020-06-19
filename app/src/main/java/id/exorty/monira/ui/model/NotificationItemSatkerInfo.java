package id.exorty.monira.ui.model;

public class NotificationItemSatkerInfo implements NotificationListItem{
    public String satker_id;
    public String satker_name;
    public String message;
    public String date_time;
    public String priority;

    public NotificationItemSatkerInfo(String satker_id, String satker_name, String message, String date_time, String priority){
        this.satker_id = satker_id;
        this.satker_name = satker_name;
        this.message = message;
        this.date_time = date_time;
        this.priority = priority;
    }

    @Override
    public int getType() {
        return SATKER_ITEM_TYPE;
    }

}
