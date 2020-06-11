package id.exorty.monira.ui.components;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.ui.adapter.KpaPpkListAdapter;
import id.exorty.monira.ui.model.KpaPpkListItem;
import id.exorty.monira.ui.model.SatkerProfileGroupInfo;
import id.exorty.monira.ui.model.SatkerProfileKpaPpkItemInfo;
import id.exorty.monira.ui.model.SatkerProfileSatkerItemInfo;

public class SatkerProfileComponent extends LinearLayout{
    private TextView mTxtCardHeader;
    private RecyclerView mRecyclerView;
    private LinearLayout mBackgroundProcessLayout;
    private AVLoadingIndicatorView mAvloadingIndicatorView;

    private KpaPpkListAdapter mKpaPpkListAdapter;
    protected Context mContext;

    private Callback mCallback;

    public interface Callback {
        void onWACall(String phoneNUmber);
        void onWAText(String phoneNUmber);
    }

    public SatkerProfileComponent(Context context, Callback callback) {
        super(context);

        this.mContext = context;
        this.mCallback = callback;

        this.addView(createComponent());
    }

    private View createComponent(){
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.component_satker_dialog_layout, null);

        mTxtCardHeader = view.findViewById(R.id.txt_card_header);
        mTxtCardHeader.setText(R.string.dashboard_satker_info);

        mBackgroundProcessLayout = view.findViewById(R.id.background_process_layout);
        mAvloadingIndicatorView = view.findViewById(R.id.avloadingIndicatorView);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mKpaPpkListAdapter = new KpaPpkListAdapter(mContext, new KpaPpkListAdapter.Callback() {
            @Override
            public void onItemClick(final String name, final String phone_number) {
                final View customLayout = inflater.inflate(R.layout.call_or_text_selection_layout, null);

                View btn_call = customLayout.findViewById(R.id.btn_call);
                btn_call.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onWACall(phone_number);
                    }
                });

                View btn_text = customLayout.findViewById(R.id.btn_text);
                btn_text.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onWAText(phone_number);
                    }
                });

                new AlertDialog.Builder(mContext, R.style.AlertDialogCustom)
                        .setTitle("Menghubungi : " + name)
                        .setView(customLayout)
                        .setPositiveButton(R.string.material_dialog_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

        mRecyclerView.setAdapter(mKpaPpkListAdapter);

        return view;
    }

    public void startUpdateData(){
        mRecyclerView.setVisibility(GONE);
        mBackgroundProcessLayout.setVisibility(VISIBLE);
    }

    public void updateData(JsonObject jsonObject){
        List<KpaPpkListItem> list = new ArrayList<>();

        SatkerProfileGroupInfo item = new SatkerProfileGroupInfo("KPA");
        list.add(item);

        SatkerProfileKpaPpkItemInfo item1 = new SatkerProfileKpaPpkItemInfo(jsonObject.get("KPA").asObject().get("name").asString(), jsonObject.get("KPA").asObject().get("phone").asString());
        list.add(item1);

        item = new SatkerProfileGroupInfo("PPK");
        list.add(item);

        JsonArray arr = jsonObject.get("PPK").asArray();
        for (JsonValue jv : arr){
            JsonObject jo = jv.asObject();

            item1 = new SatkerProfileKpaPpkItemInfo(jo.get("name").asString(), jo.get("phone").asString());
            list.add(item1);
        }

        item = new SatkerProfileGroupInfo("Satker");
        list.add(item);


        SatkerProfileSatkerItemInfo item2 = new SatkerProfileSatkerItemInfo(jsonObject.get("INFO").asObject().get("name").asString(), jsonObject.get("INFO").asObject().get("phone").asString(), jsonObject.get("INFO").asObject().get("address").asString());
        list.add(item2);

        mKpaPpkListAdapter.updateData(list);

        mRecyclerView.setVisibility(VISIBLE);
        mBackgroundProcessLayout.setVisibility(GONE);
    }


}
