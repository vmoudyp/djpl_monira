package id.exorty.monira.ui.model;

public class NotificationGroupSatkerInfo implements NotificationListItem{
    public String satker_id;
    public String name;

    public NotificationGroupSatkerInfo(String satker_id, String name){
        this.satker_id = satker_id;
        this.name = name;
    }

    @Override
    public int getType() {
        return SATKER_GROUP_TYPE;
    }
}
