package id.exorty.monira.ui.model;

public class SatkerProfileKpaPpkItemInfo implements KpaPpkListItem {
    public String name;
    public String phone_number;

    public SatkerProfileKpaPpkItemInfo(String name, String phone_number) {
        this.name = name;
        this.phone_number = phone_number;
    }

    @Override
    public int getType() {
        return KPA_PPK_ITEM_TYPE;
    }
}
