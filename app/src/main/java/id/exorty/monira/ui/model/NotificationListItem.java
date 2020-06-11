package id.exorty.monira.ui.model;

public interface NotificationListItem {
    public static final int STANDARD_TYPE = 0;
    public static final int MESSAGE_GROUP_TYPE = 1;
    public static final int SATKER_GROUP_TYPE = 2;
    public static final int MESSAGE_ITEM_TYPE = 3;
    public static final int SATKER_ITEM_TYPE = 4;

    abstract public int getType();
}
