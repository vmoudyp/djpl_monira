package id.exorty.monira.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.charts.Resource;

import java.util.List;

import az.plainpie.PieView;
import id.exorty.monira.R;
import id.exorty.monira.helper.Util;
import id.exorty.monira.ui.model.DashboardData;
import id.exorty.monira.ui.model.DataInfo;

public class TypeOfActivityAdapter extends RecyclerView.Adapter<TypeOfActivityAdapter.ViewHolder> {
    private Context mContext;
    private int mRowLayout;
    private List<DataInfo> mDataInfos;
    private String[] mIds;
    private String[] mDescriptions;
    private Callback mCallback;

    public interface Callback{
        void onItemClick(String id, String description, String[] ids, String[] descriptions);
    }

    public TypeOfActivityAdapter(Context context, int rowLayout, Callback callback){
        this.mContext = context;
        this.mRowLayout = rowLayout;
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mRowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        DataInfo dataInfo = mDataInfos.get(position);

        viewHolder.txtDescription.setText(dataInfo.getDescription());
        viewHolder.pieViewRealization.setPercentage((float)dataInfo.getRealization());
        viewHolder.pieViewRealization.setPercentageBackgroundColor(Color.parseColor(Util.getRealizationColor(mContext)));
        viewHolder.pieViewRealization.setPercentage((int)dataInfo.getRealization());
        //viewHolder.pieViewRealization.setTextColor(Color.parseColor(Util.getRealizationColor(mContext)));

        viewHolder.pieViewPrognosis.setPercentage((float)dataInfo.getPrognosis());
        viewHolder.pieViewPrognosis.setPercentageBackgroundColor(Color.parseColor(Util.getPrognosisColor(mContext)));
        viewHolder.pieViewPrognosis.setPercentage((int)dataInfo.getPrognosis());
        //viewHolder.pieViewPrognosis.setTextColor(Color.parseColor(Util.getPrognosisColor(mContext)));

        viewHolder.pieViewEndOfYear.setPercentage((float)dataInfo.getEndOfYear());
        viewHolder.pieViewEndOfYear.setPercentageBackgroundColor(Color.parseColor(Util.getEndOfYear(mContext)));
        viewHolder.pieViewEndOfYear.setPercentage((int)dataInfo.getEndOfYear());
        //viewHolder.pieViewEndOfYear.setTextColor(Color.parseColor(Util.getEndOfYear(mContext)));

        viewHolder.mainLayout.setTag(position);
        viewHolder.mainLayout.setBackground(mContext.getResources().getDrawable(R.drawable.background_gradient_activity));
        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int pos  = (int)view.getTag();

                mCallback.onItemClick(mDataInfos.get(pos).getId(), mDataInfos.get(pos).getDescription(), mIds, mDescriptions);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mDataInfos == null ? 0 : this.mDataInfos.size();
    }

    public void updateData(List<DataInfo> dataInfos){
        mDataInfos = dataInfos;
        mIds = new String[mDataInfos.size()];
        mDescriptions = new String[mDataInfos.size()];

        for (int i = 0; i < dataInfos.size(); i++){
            mIds[i] = dataInfos.get(i).getId();
            mDescriptions[i] = dataInfos.get(i).getDescription();
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mainLayout;
        public TextView txtDescription;
        public PieView pieViewRealization;
        public PieView pieViewPrognosis;
        public PieView pieViewEndOfYear;

        public ViewHolder(View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            txtDescription = itemView.findViewById(R.id.item_description);
            pieViewRealization = itemView.findViewById(R.id.item_pie_realization);
            pieViewPrognosis = itemView.findViewById(R.id.item_pie_prognosis);
            pieViewEndOfYear = itemView.findViewById(R.id.item_pie_end_of_year);
        }
    }

}
