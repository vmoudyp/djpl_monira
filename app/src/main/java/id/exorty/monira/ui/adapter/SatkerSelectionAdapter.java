package id.exorty.monira.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.ui.model.SatkerInfo;

public class SatkerSelectionAdapter extends RecyclerView.Adapter<SatkerSelectionAdapter.ViewHolder> {
    private Context mContext;
    private int mRowLayout;
    private List<SatkerInfo> mSatkerInfos;
    private List<SatkerInfo> mTempSatkerInfos = new ArrayList<>();
    private Callback mCallback;

    public interface Callback {
        void onSelected(SatkerInfo satkerInfo);
    }

    public SatkerSelectionAdapter(Context context, int rowLayout, Callback callback){
        this.mContext = context;
        this.mRowLayout = rowLayout;
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public SatkerSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mRowLayout, parent, false);
        return new SatkerSelectionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SatkerSelectionAdapter.ViewHolder viewHolder, int position) {
        try {
            SatkerInfo satkerInfo = mTempSatkerInfos.get(position);

            viewHolder.mainLayout.setTag(satkerInfo);
            viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SatkerInfo satkerInfo = (SatkerInfo) v.getTag();
                    mCallback.onSelected(satkerInfo);
                }
            });
            viewHolder.txtName.setText(satkerInfo.name);
            viewHolder.txtId.setText(satkerInfo.id);
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return this.mSatkerInfos == null ? 0 : this.mSatkerInfos.size();
    }

    public void updateData(List<SatkerInfo> satkerInfos){
        mSatkerInfos = satkerInfos;
        mTempSatkerInfos.clear();
        mTempSatkerInfos.addAll(mSatkerInfos);
        notifyDataSetChanged();
    }

    public void filter(String text) {

        mTempSatkerInfos.clear();
        if(text.equals("")){
            mTempSatkerInfos.addAll(mSatkerInfos);
        } else{
            text = text.toLowerCase();
            for(SatkerInfo item: mSatkerInfos){
                if(item.id.toLowerCase().contains(text.toLowerCase()) || item.name.toLowerCase().contains(text.toLowerCase())){
                    mTempSatkerInfos.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mainLayout;
        public TextView txtName;
        public TextView txtId;

        public ViewHolder(View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            txtName = itemView.findViewById(R.id.txt_satker_name);
            txtId = itemView.findViewById(R.id.txt_satker_id);
        }
    }
}
