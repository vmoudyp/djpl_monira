package id.exorty.monira.ui.model;

public class SatkerProfileGroupInfo implements KpaPpkListItem{
    public String name;

    public SatkerProfileGroupInfo(String name){
        this.name = name;
    }

    @Override
    public int getType() {
        return GROUP_TYPE;
    }
}
