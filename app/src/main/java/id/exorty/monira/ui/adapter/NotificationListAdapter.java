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
import id.exorty.monira.helper.Config;
import id.exorty.monira.ui.model.NotificationGroupMessageInfo;
import id.exorty.monira.ui.model.NotificationGroupSatkerInfo;
import id.exorty.monira.ui.model.NotificationItemMessageInfo;
import id.exorty.monira.ui.model.NotificationItemSatkerInfo;
import id.exorty.monira.ui.model.NotificationListItem;
import id.exorty.monira.ui.model.NotificationStandardInfo;

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "Notification List Adapter";

    private Context mContext;
    private List<NotificationListItem> mNotifications;
    private Callback mCallback;

    public interface Callback{
        void onItemClick(int id, String description);
    }

    public NotificationListAdapter(Context context, Callback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }


    class NotificationMessageGroupViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        TextView txtMessage;

        NotificationMessageGroupViewHolder(View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            txtMessage = itemView.findViewById(R.id.txt_message);
        }
    }

    class NotificationSatkerGroupViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        TextView txtName;

        NotificationSatkerGroupViewHolder(View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            txtName = itemView.findViewById(R.id.txt_name);
        }
    }

    class NotificationMessageItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        TextView txtName;
        TextView txtDateTime;

        NotificationMessageItemViewHolder(View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDateTime = itemView.findViewById(R.id.txt_date_time);
        }
    }

    class NotificationSatkerItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        TextView txtMessage;
        TextView txtDateTime;

        NotificationSatkerItemViewHolder(View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            txtMessage = itemView.findViewById(R.id.txt_message);
            txtDateTime = itemView.findViewById(R.id.txt_date_time);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType) {

            case NotificationListItem.MESSAGE_GROUP_TYPE:
                View viewMessageGroup = inflater.inflate(R.layout.notification_message_group, parent, false);
                viewHolder = new NotificationMessageGroupViewHolder(viewMessageGroup);
                break;

            case NotificationListItem.SATKER_GROUP_TYPE:
                View viewSatkerGroup = inflater.inflate(R.layout.notification_satker_group, parent, false);
                viewHolder = new NotificationSatkerGroupViewHolder(viewSatkerGroup);
                break;

            case NotificationListItem.MESSAGE_ITEM_TYPE:
                View viewMessageItem = inflater.inflate(R.layout.notification_message_item, parent, false);
                viewHolder = new NotificationMessageItemViewHolder(viewMessageItem);
                break;

            case NotificationListItem.SATKER_ITEM_TYPE:
                View viewSatkerItem = inflater.inflate(R.layout.notification_satker_item, parent, false);
                viewHolder = new NotificationSatkerItemViewHolder(viewSatkerItem);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch(viewHolder.getItemViewType()) {

            case NotificationListItem.MESSAGE_GROUP_TYPE:
                NotificationGroupMessageInfo notificationGroupMessageInfo = (NotificationGroupMessageInfo) mNotifications.get(position);
                NotificationMessageGroupViewHolder notificationMessageGroupViewHolder = (NotificationMessageGroupViewHolder) viewHolder;

                notificationMessageGroupViewHolder.txtMessage.setText(notificationGroupMessageInfo.message);
                break;

            case NotificationListItem.SATKER_GROUP_TYPE:
                NotificationGroupSatkerInfo notificationGroupSatkerInfo = (NotificationGroupSatkerInfo) mNotifications.get(position);
                NotificationSatkerGroupViewHolder notificationSatkerGroupViewHolder = (NotificationSatkerGroupViewHolder) viewHolder;

                notificationSatkerGroupViewHolder.txtName.setText(notificationGroupSatkerInfo.name);
                break;

            case NotificationListItem.MESSAGE_ITEM_TYPE:

                NotificationItemMessageInfo notificationItemMessageInfo = (NotificationItemMessageInfo)mNotifications.get(position);
                NotificationMessageItemViewHolder notificationMessageItemViewHolder = (NotificationMessageItemViewHolder) viewHolder;

                notificationMessageItemViewHolder.txtName.setText(notificationItemMessageInfo.satker_name);
                notificationMessageItemViewHolder.txtDateTime.setText(notificationItemMessageInfo.date_time);
                if (notificationItemMessageInfo.priority.equals(Config.PRIORITY_WARNING)){
                    notificationMessageItemViewHolder.txtName.setTextColor(mContext.getResources().getColor(R.color.textColor3));
                    notificationMessageItemViewHolder.txtDateTime.setTextColor(mContext.getResources().getColor(R.color.textColor4));
                    notificationMessageItemViewHolder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notification_priority_warning));
                }else{
                    notificationMessageItemViewHolder.txtName.setTextColor(mContext.getResources().getColor(R.color.textColor1));
                    notificationMessageItemViewHolder.txtDateTime.setTextColor(mContext.getResources().getColor(R.color.textColor2));
                    notificationMessageItemViewHolder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notification_priority_danger));
                }

                notificationMessageItemViewHolder.mainLayout.setTag(notificationItemMessageInfo.satker_id + ";" + notificationItemMessageInfo.satker_name);
                notificationMessageItemViewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] satker_info = v.getTag().toString().split(";");
                        mCallback.onItemClick(Integer.valueOf(satker_info[0]),satker_info[1]);
                    }
                });

                break;

            case NotificationListItem.SATKER_ITEM_TYPE:

                NotificationItemSatkerInfo notificationItemSatkerInfo = (NotificationItemSatkerInfo)mNotifications.get(position);
                NotificationSatkerItemViewHolder notificationSatkerItemViewHolder = (NotificationSatkerItemViewHolder) viewHolder;

                notificationSatkerItemViewHolder.txtMessage.setText(notificationItemSatkerInfo.message);
                notificationSatkerItemViewHolder.txtDateTime.setText(notificationItemSatkerInfo.date_time);
                if (notificationItemSatkerInfo.priority.equals(Config.PRIORITY_WARNING)){
                    notificationSatkerItemViewHolder.txtMessage.setTextColor(mContext.getResources().getColor(R.color.textColor3));
                    notificationSatkerItemViewHolder.txtDateTime.setTextColor(mContext.getResources().getColor(R.color.textColor4));
                    notificationSatkerItemViewHolder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notification_priority_warning));
                }else{
                    notificationSatkerItemViewHolder.txtMessage.setTextColor(mContext.getResources().getColor(R.color.textColor1));
                    notificationSatkerItemViewHolder.txtDateTime.setTextColor(mContext.getResources().getColor(R.color.textColor2));
                    notificationSatkerItemViewHolder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notification_priority_danger));
                }

                notificationSatkerItemViewHolder.mainLayout.setTag(notificationItemSatkerInfo.satker_id + ";" + notificationItemSatkerInfo.satker_name);
                notificationSatkerItemViewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] satker_info = v.getTag().toString().split(";");
                        mCallback.onItemClick(Integer.valueOf(satker_info[0]),satker_info[1]);
                    }
                });

                break;
        }

    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    @Override
    public int getItemViewType(int position) { return mNotifications.get(position).getType(); }

    public void updateData(List<NotificationListItem> notifications){
        mNotifications = notifications;
        notifyDataSetChanged();
    }
}
