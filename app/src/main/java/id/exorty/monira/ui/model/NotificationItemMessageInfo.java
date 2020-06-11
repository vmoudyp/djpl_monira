package id.exorty.monira.ui.model;

public class NotificationItemMessageInfo implements NotificationListItem{
    public String satker_id;
    public String satker_name;
    public String date_time;
    public String priority;

    public NotificationItemMessageInfo(String satker_id, String satker_name, String date_time, String priority){
        this.satker_id = satker_id;
        this.satker_name = satker_name;
        this.date_time = date_time;
        this.priority = priority;
    }


    @Override
    public int getType() {
        return MESSAGE_ITEM_TYPE;
    }
}
