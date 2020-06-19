package id.exorty.monira.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.core.annotations.Line;

import java.util.ArrayList;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.ui.model.SatkerInfo;
import id.exorty.monira.ui.model.SatkerRankingInfo;

import static id.exorty.monira.helper.Config.SATKER_RANKING_COLOR_RED;

public class SatkerRankingAdapter  extends RecyclerView.Adapter<SatkerRankingAdapter.ViewHolder>{
    private Context mContext;
    private int mRowLayout;
    private List<SatkerRankingInfo> mSatkerRankingInfos;
    private List<SatkerRankingInfo> mTempSatkerRankingInfos = new ArrayList<>();

    private Callback mCallback;

    public interface Callback{
        void onClick(String satker_id, String satker_name);
    }
    public SatkerRankingAdapter(Context context, int rowLayout, Callback callback){
        this.mContext = context;
        this.mRowLayout = rowLayout;
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public SatkerRankingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mRowLayout, parent, false);
        return new SatkerRankingAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SatkerRankingAdapter.ViewHolder viewHolder, int position) {
//        if (position >= mTempSatkerRankingInfos.size())
//            return;

        SatkerRankingInfo satkerRankingInfo = mTempSatkerRankingInfos.get(position);

        viewHolder.txtRank.setText(String.valueOf(satkerRankingInfo.rank));
        viewHolder.txtSatkerName.setText(satkerRankingInfo.satker_name);

        if (satkerRankingInfo.progress_status == -1){
            viewHolder.imageProgress.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_blue));
        }else if (satkerRankingInfo.progress_status == 0){
            viewHolder.imageProgress.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_steady_blue_32dp));
        }else{
            viewHolder.imageProgress.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_red));
        }
        viewHolder.imageProgress.invalidate();

        viewHolder.mainLayout.setTag(satkerRankingInfo.satker_id + ";" + satkerRankingInfo.satker_name);
        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] datas = v.getTag().toString().split(";");
                mCallback.onClick(datas[0], datas[1]);
            }
        });

        if (satkerRankingInfo.color.equals(SATKER_RANKING_COLOR_RED)){
            viewHolder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.ranking_color_red));
        }else{
            viewHolder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.ranking_color_normal));
        }
    }

    @Override
    public int getItemCount() {
        return this.mTempSatkerRankingInfos == null ? 0 : this.mTempSatkerRankingInfos.size();
    }

    public void updateData(List<SatkerRankingInfo> satkerRankingInfos){
        mSatkerRankingInfos = satkerRankingInfos;
        mTempSatkerRankingInfos.clear();
        mTempSatkerRankingInfos.addAll(mSatkerRankingInfos);
        notifyDataSetChanged();
    }

    public void filter(String text){

        mTempSatkerRankingInfos.clear();
        if(text.equals("")){
            mTempSatkerRankingInfos.addAll(mSatkerRankingInfos);
        } else{
            text = text.toLowerCase();
            for(SatkerRankingInfo item: mSatkerRankingInfos){
                if(item.satker_name.toLowerCase().contains(text.toLowerCase())){
                    mTempSatkerRankingInfos.add(item);
                }
            }
        }

        notifyDataSetChanged();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        TextView txtRank;
        TextView txtSatkerName;
        ImageView imageProgress;

        ViewHolder(View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            txtRank = itemView.findViewById(R.id.txt_ranking);
            txtSatkerName = itemView.findViewById(R.id.txt_satker_name);
            imageProgress = itemView.findViewById(R.id.image_progress);
        }
    }
}
