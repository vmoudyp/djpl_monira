package id.exorty.monira.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.helper.Config;
import id.exorty.monira.ui.model.NotificationGroupMessageInfo;
import id.exorty.monira.ui.model.NotificationGroupSatkerInfo;
import id.exorty.monira.ui.model.NotificationItemMessageInfo;
import id.exorty.monira.ui.model.NotificationItemSatkerInfo;
import id.exorty.monira.ui.model.NotificationListItem;

import static id.exorty.monira.helper.Config.NOTIFICATION_GROUP_BY_MESSAGE;
import static id.exorty.monira.helper.Config.NOTIFICATION_GROUP_BY_SATKER;

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "Notification List Adapter";

    private Context mContext;
    //private List<NotificationListItem> mNotifications;
    private List<NotificationListItem> mTempNotifications;
    private JsonArray mJsonArray;
    private Callback mCallback;

    public interface Callback{
        void onItemClick(String id, String description);
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
                NotificationGroupMessageInfo notificationGroupMessageInfo = (NotificationGroupMessageInfo) mTempNotifications.get(position);
                NotificationMessageGroupViewHolder notificationMessageGroupViewHolder = (NotificationMessageGroupViewHolder) viewHolder;

                notificationMessageGroupViewHolder.txtMessage.setText(notificationGroupMessageInfo.message);
                break;

            case NotificationListItem.SATKER_GROUP_TYPE:
                NotificationGroupSatkerInfo notificationGroupSatkerInfo = (NotificationGroupSatkerInfo) mTempNotifications.get(position);
                NotificationSatkerGroupViewHolder notificationSatkerGroupViewHolder = (NotificationSatkerGroupViewHolder) viewHolder;

                notificationSatkerGroupViewHolder.txtName.setText(notificationGroupSatkerInfo.name);
                notificationSatkerGroupViewHolder.mainLayout.setTag(notificationGroupSatkerInfo.satker_id + ";" + notificationGroupSatkerInfo.name);
                notificationSatkerGroupViewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] satker_info = v.getTag().toString().split(";");
                        mCallback.onItemClick(satker_info[0],satker_info[1]);
                    }
                });
                break;

            case NotificationListItem.MESSAGE_ITEM_TYPE:

                NotificationItemMessageInfo notificationItemMessageInfo = (NotificationItemMessageInfo)mTempNotifications.get(position);
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
                        mCallback.onItemClick(satker_info[0],satker_info[1]);
                    }
                });

                break;

            case NotificationListItem.SATKER_ITEM_TYPE:

                NotificationItemSatkerInfo notificationItemSatkerInfo = (NotificationItemSatkerInfo)mTempNotifications.get(position);
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
                        mCallback.onItemClick(satker_info[0],satker_info[1]);
                    }
                });

                break;
        }

    }

    @Override
    public int getItemCount() {
        return mTempNotifications == null ? 0 : mTempNotifications.size();
    }

    @Override
    public int getItemViewType(int position) { return mTempNotifications.get(position).getType(); }

//    public void updateData(List<NotificationListItem> notifications){
//        mNotifications = notifications;
//        notifyDataSetChanged();
//
//        mTempNotifications.clear();
//        mTempNotifications.addAll(mNotifications);
//
//    }

    public void updateData(JsonArray jsonArray, String group_by){
        mTempNotifications = new ArrayList<>();
        mJsonArray = jsonArray;
        List<NotificationListItem> notificationListItems = new ArrayList<>();
        if (group_by == NOTIFICATION_GROUP_BY_SATKER){
            for (int i = 0; i < jsonArray.size(); i++){
                JsonObject joGroup = jsonArray.get(i).asObject();

                NotificationGroupSatkerInfo notificationGroupSatkerInfo = new NotificationGroupSatkerInfo(joGroup.get("id").asString(), joGroup.get("name").asString());
                mTempNotifications.add(notificationGroupSatkerInfo);

                for (int j = 0; j < joGroup.get("notifications").asArray().size(); j++){
                    JsonObject joItem = joGroup.get("notifications").asArray().get(j).asObject();

                    NotificationItemSatkerInfo notificationItemSatkerInfo = new NotificationItemSatkerInfo(joGroup.get("id").asString(), joGroup.get("name").asString(), joItem.get("message").asString(), joItem.get("date_time").asString(), joItem.get("priority").asString());
                    mTempNotifications.add(notificationItemSatkerInfo);
                }
            }

        }else if (group_by == NOTIFICATION_GROUP_BY_MESSAGE){
            for (int i = 0; i < jsonArray.size(); i++){
                JsonObject joGroup = jsonArray.get(i).asObject();

                NotificationGroupMessageInfo notificationGroupMessageInfo = new NotificationGroupMessageInfo(joGroup.get("message").asString(), joGroup.get("priority").asString());
                mTempNotifications.add(notificationGroupMessageInfo);

                for (int j = 0; j < joGroup.get("satkers").asArray().size(); j++){
                    JsonObject joItem = joGroup.get("satkers").asArray().get(j).asObject();

                    NotificationItemMessageInfo notificationItemMessageInfo = new NotificationItemMessageInfo(joItem.get("id").asString(), joItem.get("name").asString(), joItem.get("notification").asObject().get("date_time").asString(), joGroup.get("priority").asString());
                    mTempNotifications.add(notificationItemMessageInfo);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filter(String text, String group_by) {
        mTempNotifications = new ArrayList<>();

        if (group_by == NOTIFICATION_GROUP_BY_SATKER){
            for (int i = 0; i < mJsonArray.size(); i++){
                JsonObject joGroup = mJsonArray.get(i).asObject();

                if (joGroup.get("name").asString().toLowerCase().contains(text.toLowerCase())) {
                    NotificationGroupSatkerInfo notificationGroupSatkerInfo = new NotificationGroupSatkerInfo(joGroup.get("id").asString(), joGroup.get("name").asString());
                    mTempNotifications.add(notificationGroupSatkerInfo);

                    for (int j = 0; j < joGroup.get("notifications").asArray().size(); j++) {
                        JsonObject joItem = joGroup.get("notifications").asArray().get(j).asObject();

                        NotificationItemSatkerInfo notificationItemSatkerInfo = new NotificationItemSatkerInfo(joGroup.get("id").asString(), joGroup.get("name").asString(), joItem.get("message").asString(), joItem.get("date_time").asString(), joItem.get("priority").asString());
                        mTempNotifications.add(notificationItemSatkerInfo);
                    }
                }
            }

        }else if (group_by == NOTIFICATION_GROUP_BY_MESSAGE){
            for (int i = 0; i < mJsonArray.size(); i++){
                JsonObject joGroup = mJsonArray.get(i).asObject();

                NotificationGroupMessageInfo notificationGroupMessageInfo = new NotificationGroupMessageInfo(joGroup.get("message").asString(), joGroup.get("priority").asString());
                mTempNotifications.add(notificationGroupMessageInfo);

                for (int j = 0; j < joGroup.get("satkers").asArray().size(); j++){
                    JsonObject joItem = joGroup.get("satkers").asArray().get(j).asObject();

                    if (joItem.get("name").asString().toLowerCase().contains(text.toLowerCase())) {
                        NotificationItemMessageInfo notificationItemMessageInfo = new NotificationItemMessageInfo(joItem.get("id").asString(), joItem.get("name").asString(), joItem.get("notification").asObject().get("date_time").asString(), joGroup.get("priority").asString());
                        mTempNotifications.add(notificationItemMessageInfo);
                    }
                }
            }
        }
        notifyDataSetChanged();

//        mTempNotifications.clear();
//        if(text.equals("")){
//            mTempNotifications.addAll(mNotifications);
//        } else{
//            text = text.toLowerCase();
//            for(NotificationListItem item: mNotifications){
//                if(item.id.toLowerCase().contains(text) || item.name.toLowerCase().contains(text)){
//                    mTempNotifications.add(item);
//                }
//            }
//        }

        notifyDataSetChanged();
    }

}
