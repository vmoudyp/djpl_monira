package id.exorty.monira.ui.model;

public class ListItem {
    private int id;
    private String description;

    public ListItem(int id, String description){
        this.id = id;
        this.description = description;
    }

    public int getId(){
        return id;
    }

    public String getDescription(){
        return description;
    }
}
