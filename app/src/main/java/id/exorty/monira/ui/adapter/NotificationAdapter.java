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
import id.exorty.monira.ui.model.NotificationStandardInfo;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context mContext;
    private int mRowLayout;
    private List<NotificationStandardInfo> mNotificationItemInfos;

    public NotificationAdapter(Context context, int rowLayout){
        this.mContext = context;
        this.mRowLayout = rowLayout;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mRowLayout, parent, false);
        return new NotificationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder viewHolder, int position) {
        NotificationStandardInfo notificationItemInfo = mNotificationItemInfos.get(position);

        viewHolder.txtMessage.setText(notificationItemInfo.message);
        viewHolder.txtDateTime.setText(notificationItemInfo.date_time);
        if (notificationItemInfo.priority.equals(Config.PRIORITY_WARNING)){
            viewHolder.txtMessage.setTextColor(mContext.getResources().getColor(R.color.textColor3));
            viewHolder.txtDateTime.setTextColor(mContext.getResources().getColor(R.color.textColor4));
            viewHolder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notification_priority_warning));
        }else if (notificationItemInfo.priority.equals(Config.PRIORITY_DANGER)){
            viewHolder.txtMessage.setTextColor(mContext.getResources().getColor(R.color.textColor1));
            viewHolder.txtDateTime.setTextColor(mContext.getResources().getColor(R.color.textColor2));
            viewHolder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notification_priority_danger));
        }
    }

    @Override
    public int getItemCount() {
        return this.mNotificationItemInfos == null ? 0 : this.mNotificationItemInfos.size();
    }

    public void updateData(List<NotificationStandardInfo> notificationItemInfos){
        mNotificationItemInfos = notificationItemInfos;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        TextView txtMessage;
        TextView txtDateTime;

        ViewHolder(View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            txtMessage = itemView.findViewById(R.id.txt_message);
            txtDateTime = itemView.findViewById(R.id.txt_date_time);
        }
    }
}
