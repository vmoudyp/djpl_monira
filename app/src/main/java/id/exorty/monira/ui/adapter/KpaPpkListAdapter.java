package id.exorty.monira.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.ui.model.KpaPpkListItem;
import id.exorty.monira.ui.model.SatkerProfileGroupInfo;
import id.exorty.monira.ui.model.SatkerProfileKpaPpkItemInfo;
import id.exorty.monira.ui.model.SatkerProfileSatkerItemInfo;

public class KpaPpkListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "KPA PPK List Adapter";

    private Context mContext;
    private List<KpaPpkListItem> mList;
    private Callback mCallback;

    public interface Callback{
        void onItemClick(String name, String phone_number);
    }

    public KpaPpkListAdapter(Context context, Callback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }


    class GroupViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        TextView txtName;

        GroupViewHolder(View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            txtName = itemView.findViewById(R.id.txt_name);
        }
    }

    class KpaPpkItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        TextView txtName;
        TextView txtPhoneNumber;

        KpaPpkItemViewHolder(View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPhoneNumber = itemView.findViewById(R.id.txt_phone_number);
        }
    }

    class SatkerItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        TextView txtName;
        TextView txtAddress;
        TextView txtPhoneNumber;

        SatkerItemViewHolder(View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPhoneNumber = itemView.findViewById(R.id.txt_phone_number);
            txtAddress = itemView.findViewById(R.id.txt_address);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType) {

            case KpaPpkListItem.GROUP_TYPE:
                View viewMessageGroup = inflater.inflate(R.layout.satker_profile_group_layout, parent, false);
                viewHolder = new GroupViewHolder(viewMessageGroup);
                break;

            case KpaPpkListItem.KPA_PPK_ITEM_TYPE:
                View viewKpaPpkItem = inflater.inflate(R.layout.satker_profile_kpa_ppk_item, parent, false);
                viewHolder = new KpaPpkItemViewHolder(viewKpaPpkItem);
                break;

            case KpaPpkListItem.SATKER_ITEM_TYPE:
                View viewSatkerItem = inflater.inflate(R.layout.satker_profile_satker_item, parent, false);
                viewHolder = new SatkerItemViewHolder(viewSatkerItem);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch(viewHolder.getItemViewType()) {

            case KpaPpkListItem.GROUP_TYPE:
                SatkerProfileGroupInfo satkerProfileGroupInfo = (SatkerProfileGroupInfo) mList.get(position);
                GroupViewHolder notificationMessageGroupViewHolder = (GroupViewHolder) viewHolder;

                notificationMessageGroupViewHolder.txtName.setText(satkerProfileGroupInfo.name);
                break;

            case KpaPpkListItem.KPA_PPK_ITEM_TYPE:
                SatkerProfileKpaPpkItemInfo satkerProfileKpaPpkItemInfo = (SatkerProfileKpaPpkItemInfo) mList.get(position);
                KpaPpkItemViewHolder kpaPpkItemViewHolder = (KpaPpkItemViewHolder) viewHolder;

                kpaPpkItemViewHolder.txtName.setText(satkerProfileKpaPpkItemInfo.name);
                kpaPpkItemViewHolder.txtPhoneNumber.setText(satkerProfileKpaPpkItemInfo.phone_number);
                kpaPpkItemViewHolder.mainLayout.setTag(satkerProfileKpaPpkItemInfo.name + ";" + satkerProfileKpaPpkItemInfo.phone_number);
                kpaPpkItemViewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] infos = v.getTag().toString().split(";");
                        if (infos.length == 2) {
                            if (!infos[1].trim().equals(""))
                                mCallback.onItemClick(infos[0], infos[1]);
                        }
                    }
                });
                break;

            case KpaPpkListItem.SATKER_ITEM_TYPE:

                SatkerProfileSatkerItemInfo satkerProfileSatkerItemInfo = (SatkerProfileSatkerItemInfo)mList.get(position);
                SatkerItemViewHolder satkerItemViewHolder = (SatkerItemViewHolder) viewHolder;

                satkerItemViewHolder.txtName.setText(satkerProfileSatkerItemInfo.name);
                satkerItemViewHolder.txtPhoneNumber.setText(satkerProfileSatkerItemInfo.phone_number);
                satkerItemViewHolder.txtAddress.setText(satkerProfileSatkerItemInfo.address);
                satkerItemViewHolder.mainLayout.setTag(satkerProfileSatkerItemInfo.name + ";" + satkerProfileSatkerItemInfo.phone_number);
                satkerItemViewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] infos = v.getTag().toString().split(";");
                        if (!infos[1].trim().equals(""))
                            mCallback.onItemClick(infos[0], infos[1]);
                    }
                });

                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        else
            return 0;
    }

    @Override
    public int getItemViewType(int position) { return mList.get(position).getType(); }

    public void updateData(List<KpaPpkListItem> list){
        mList = list;
        notifyDataSetChanged();
    }
}
