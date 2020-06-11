package id.exorty.monira.ui.model;

public class SatkerProfileSatkerItemInfo implements KpaPpkListItem {
    public String name;
    public String address;
    public String phone_number;

    public SatkerProfileSatkerItemInfo(String name, String phone_number, String address) {
        this.name = name;
        this.phone_number = phone_number;
        this.address = address;
    }

    @Override
    public int getType() {
        return SATKER_ITEM_TYPE;
    }
}
