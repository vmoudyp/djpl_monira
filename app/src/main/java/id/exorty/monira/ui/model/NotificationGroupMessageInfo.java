package id.exorty.monira.ui.model;

public class NotificationGroupMessageInfo implements NotificationListItem{
    public String message;
    public String priority;

    public NotificationGroupMessageInfo(String message, String priority){
        this.message = message;
        this.priority = priority;
    }

    @Override
    public int getType() {
        return MESSAGE_GROUP_TYPE;
    }
}
