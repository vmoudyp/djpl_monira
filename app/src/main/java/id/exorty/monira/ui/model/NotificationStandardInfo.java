package id.exorty.monira.ui.model;

public class NotificationStandardInfo implements NotificationListItem{
    public String id;
    public String name;
    public String date_time;
    public String message;
    public String priority;

    public NotificationStandardInfo(String id, String date_time, String message, String priority){
        this.id = id;
        this.date_time = date_time;
        this.message = message;
        this.priority = priority;
    }

    public NotificationStandardInfo(String id, String name, String date_time, String message, String priority){
        this.id = id;
        this.name = name;
        this.date_time = date_time;
        this.message = message;
        this.priority = priority;
    }

    @Override
    public int getType() {
        return STANDARD_TYPE;
    }
}
