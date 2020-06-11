package id.exorty.monira.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.ui.model.ListItem;

public class ListViewAdapter extends ArrayAdapter<ListItem> {
    private List<ListItem> mItems;
    private List<ListItem> mItemsCopy;
    public ListViewAdapter(Context context, int resource, List<ListItem> objects) {
        super(context, resource, objects);
        this.mItems = objects;
        this.mItemsCopy.addAll(mItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_view_adapter_layout, parent, false);
        }

        final ListItem item = getItem(position);

        TextView description = convertView.findViewById(R.id.txt_item);
        description.setText(item.getDescription());

        TextView id = convertView.findViewById(R.id.txt_id);
        id.setText(item.getId());

        return convertView;
    }

    public void filter(String text) {
        mItems.clear();
        if(text.isEmpty()){
            mItems.addAll(mItemsCopy);
        } else{
            text = text.toLowerCase();
            for(ListItem item: mItemsCopy){
                if(item.getDescription().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)){
                    mItems.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }
}