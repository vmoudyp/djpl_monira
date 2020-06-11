package id.exorty.monira.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.helper.Util;
import id.exorty.monira.service.DataService;
import id.exorty.monira.ui.adapter.SatkerSelectionAdapter;
import id.exorty.monira.ui.components.Alert;
import id.exorty.monira.ui.components.SatkerCurrentMonthComponent;
import id.exorty.monira.ui.components.SatkerProfileComponent;
import id.exorty.monira.ui.components.TrendComponent;
import id.exorty.monira.ui.model.SatkerInfo;

import static id.exorty.monira.helper.Util.getStandardPhoneNumber;

public class SatkerActivity extends AppCompatActivity {
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private final int PERMISSIONS_REQUEST_SEND_SMS = 2;
    private final int PERMISSIONS_REQUEST_CALL_PHONE = 3;

    private SatkerCurrentMonthComponent mSatkerCurrentMonthComponent;
    private TrendComponent mTrendComponent;
    private SatkerProfileComponent mSatkerProfileComponent;
    private static int mLevel;
    private static String mIdSatker = "";
    private static String mSatkerName;
    private static int mYear;

    private TextView mTxtSatkerName;
    private List<SatkerInfo> mSatkerInfos;

    private SatkerSelectionAdapter mSatkerSelectionAdapter;
    private AlertDialog mDialog;

    private int mSatkerListLoop = 0;
    private int mDataLoop = 0;

    private String mPhoneNumber;
    private JsonObject mNationalData;
    private JsonObject mSatkerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satker);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView btnBack = findViewById(R.id.menu_icon);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView btnCCTV = findViewById(R.id.btn_cctv);
        btnCCTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SatkerActivity.this, cctvActivity.class);
                intent.putExtra("id", mIdSatker);
                intent.putExtra("description", mSatkerName);
                startActivity(intent);
            }
        });


        mLevel = Util.GetSharedPreferences(SatkerActivity.this, "level", -1);

        Intent intent = getIntent();
        mIdSatker = intent.getStringExtra("id");
        mSatkerName = intent.getStringExtra("description");

        mNationalData = Json.parse(Util.GetSharedPreferences(SatkerActivity.this, "national_data_current_month", "")).asObject();

        mTxtSatkerName = findViewById(R.id.txt_satker_name);
        mTxtSatkerName.setText(mSatkerName);
        mTxtSatkerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLevel == 3)
                    return;

                final View customLayout = getLayoutInflater().inflate(R.layout.satker_selection_layout, null);
                final EditText editText = customLayout.findViewById(R.id.edit_query);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mSatkerSelectionAdapter.filter(s.toString());
                    }
                });

                final RecyclerView recyclerView = customLayout.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(SatkerActivity.this));
                mSatkerSelectionAdapter = new SatkerSelectionAdapter(SatkerActivity.this, R.layout.satker_selection_item, new SatkerSelectionAdapter.Callback() {
                    @Override
                    public void onSelected(SatkerInfo satkerInfo) {
                        mIdSatker = satkerInfo.id;
                        mTxtSatkerName.setText(satkerInfo.name);

                        getData(false);

                        mDialog.dismiss();
                    }
                });
                mSatkerSelectionAdapter.updateData(mSatkerInfos);
                recyclerView.setAdapter(mSatkerSelectionAdapter);
                mSatkerSelectionAdapter.filter("");

                AlertDialog.Builder builder = new AlertDialog.Builder(SatkerActivity.this, R.style.AlertDialogCustom);
                builder.setTitle("Silahkan pilih Satker");
                builder.setView(customLayout);
                builder.setPositiveButton(R.string.material_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mDialog = builder.create();
                mDialog.show();
            }
        });

        TextView txtCurrentDateTime = findViewById(R.id.txt_current_date_time);
        txtCurrentDateTime.setText(Util.getCurrentDateTime());

        LinearLayout mainContent = findViewById(R.id.main_content);

        mSatkerCurrentMonthComponent = new SatkerCurrentMonthComponent(SatkerActivity.this);
        mainContent.addView(mSatkerCurrentMonthComponent);

        mTrendComponent = new TrendComponent(SatkerActivity.this);
        mainContent.addView(mTrendComponent);

        mSatkerProfileComponent = new SatkerProfileComponent(SatkerActivity.this, new SatkerProfileComponent.Callback() {
            @Override
            public void onWACall(String phoneNUmber) {
                mPhoneNumber = phoneNUmber;
                requestCall();
            }

            @Override
            public void onWAText(String phoneNUmber) {
                mPhoneNumber = phoneNUmber;
                sendWAText();
            }
        });
        mainContent.addView(mSatkerProfileComponent);

        mYear = Calendar.getInstance().get(Calendar.YEAR);

        getSatkerList();
        if (mIdSatker != null) {
            getData(true);
        }
    }

    private void getSatkerList() {
        DataService dataService = new DataService(SatkerActivity.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {
                mSatkerCurrentMonthComponent.startUpdateData();
                mTrendComponent.startUpdateDate();
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
                mSatkerInfos = new ArrayList<>();
                for (JsonValue jv : jsonArray) {
                    JsonObject jo = jv.asObject();
                    mSatkerInfos.add(new SatkerInfo(jo.get("id").asString(), jo.get("name").asString()));
                }

                if (mIdSatker == null) {
                    mIdSatker = jsonArray.get(0).asObject().get("id").asString();
                    mSatkerName = jsonArray.get(0).asObject().get("name").asString();
                    mSatkerData = jsonArray.get(0).asObject();
                    mTxtSatkerName.setText(jsonArray.get(0).asObject().get("name").asString());

                    mSatkerCurrentMonthComponent.updateData(mNationalData, mSatkerData);
                    mTrendComponent.createData(mSatkerData.get("trend").asObject());
                    //mSatkerProfileComponent.updateData(mSatkerData.get("profile").asObject());
                }

                mSatkerListLoop = 0;
            }

            @Override
            public void OnFailed(String message) {
                if (mSatkerListLoop < 3) {
                    mSatkerListLoop++;
                    getSatkerList();
                } else {
                    mSatkerListLoop = 0;
                    Alert.Show(getApplicationContext(), "", message);
                }
            }
        }).GetListOfSatker();
    }

    private void getSatkerData() {
        DataService dataService = new DataService(SatkerActivity.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {
                mSatkerProfileComponent.startUpdateData();
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
                mSatkerProfileComponent.updateData(jsonObject);

                mDataLoop = 0;
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
            }

            @Override
            public void OnFailed(String message) {
                if (mSatkerListLoop < 3) {
                    mSatkerListLoop++;
                    getSatkerData();
                } else {
                    mSatkerListLoop = 0;
                    Alert.Show(getApplicationContext(), "", message);
                }
            }
        }).GetSatkerInfo(mIdSatker);
    }

    private void getData(boolean isInit) {
        DataService dataService = new DataService(SatkerActivity.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {
                mSatkerCurrentMonthComponent.startUpdateData();
                mTrendComponent.startUpdateDate();
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
                mSatkerCurrentMonthComponent.updateData(mNationalData, jsonObject);
                if (isInit)
                    mTrendComponent.createData(jsonObject.get("trend").asObject());
                else
                    mTrendComponent.updateData(jsonObject.get("trend").asObject());

                mDataLoop = 0;

                getSatkerData();

            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {

            }

            @Override
            public void OnFailed(String message) {
                if (mDataLoop < 3) {
                    mDataLoop++;
                    getData(false);
                } else {
                    mDataLoop = 0;
                    Alert.Show(getApplicationContext(), "", message);
                }
            }
        }).GetSatkerData(mIdSatker, mYear);
    }

    private void sendWAText() {
        try {
            String contactNumber = getStandardPhoneNumber(mPhoneNumber);
            //String contactNumber = getStandardPhoneNumber("+6281295929340");


            String whatsAppRoot = "http://api.whatsapp.com/";
            String number = "send?phone=" + getStandardPhoneNumber(contactNumber); //here the mobile number with its international prefix
            String text = "&text=PESAN ANDA";
            String uri = whatsAppRoot + number + text;

            PackageManager packageManager = getPackageManager();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent);
            }
        } catch (Exception e) {
            requestSendSMS();
        }
    }

    private void sendSMS() {
        try {
            String contactNumber = getStandardPhoneNumber(mPhoneNumber);

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("smsto:"));
            i.setType("vnd.android-dir/mms-sms");
            i.putExtra("address", new String(contactNumber));
            i.putExtra("sms_body", "");
            startActivity(Intent.createChooser(i, "Send sms via:"));
        } catch (Exception e) {
            Toast.makeText(SatkerActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void makeCall() {
        try {
            String contactNumber = getStandardPhoneNumber(mPhoneNumber);

            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + contactNumber));
            //here the word 'tel' is important for making a call...
            startActivity(intent);
        } catch (Exception e) {

        }
    }

    private void callWA(){
        try {
            String contactNumber = getStandardPhoneNumber(mPhoneNumber);
            Cursor cursor = getContentResolver()
                    .query(
                            ContactsContract.Data.CONTENT_URI,
                            new String[]{ContactsContract.Data._ID},
                            ContactsContract.RawContacts.ACCOUNT_TYPE + " = 'com.whatsapp' " +
                                    "AND " + ContactsContract.Data.MIMETYPE + " = 'vnd.android.cursor.item/vnd.com.whatsapp.video.call' " +
                                    "AND " + ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE '%" + contactNumber + "%'",
                            null,
                            ContactsContract.Contacts.DISPLAY_NAME
                    );

            if (cursor == null) {
                // throw an exception
            }

            long id = -1;
            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
            }

            if (!cursor.isClosed()) {
                cursor.close();
            }

            if (id != -1) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);

                intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" + id), "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
                intent.setPackage("com.whatsapp");

                startActivity(intent);
            }else{
                Toast.makeText(SatkerActivity.this,
                        "Nomor tidak ada dalam daftar kontak", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(SatkerActivity.this,
                    "WhatsApp cannot be opened : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    public void requestContactPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(SatkerActivity.this,
//                        android.Manifest.permission.READ_CONTACTS)) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("Read Contacts permission");
//                    builder.setPositiveButton(android.R.string.ok, null);
//                    builder.setMessage("Please enable access to contacts.");
//                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @TargetApi(Build.VERSION_CODES.M)
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            requestPermissions(
//                                    new String[]
//                                            {android.Manifest.permission.READ_CONTACTS}
//                                    , PERMISSIONS_REQUEST_READ_CONTACTS);
//                        }
//                    });
//                    builder.show();
//                } else {
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{android.Manifest.permission.READ_CONTACTS},
//                            PERMISSIONS_REQUEST_READ_CONTACTS);
//                }
//            } else {
//                callWA();
//            }
//        } else {
//            callWA();
//        }
//    }

    public void requestSendSMS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SatkerActivity.this,
                        android.Manifest.permission.SEND_SMS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Send SMS permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable to send sms.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.SEND_SMS}
                                    , PERMISSIONS_REQUEST_SEND_SMS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.SEND_SMS},
                            PERMISSIONS_REQUEST_SEND_SMS);
                }
            } else {
                sendSMS();
            }
        } else {
            sendSMS();
        }
    }

    public void requestCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SatkerActivity.this,
                        android.Manifest.permission.SEND_SMS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Send SMS permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable to send sms.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.CALL_PHONE}
                                    , PERMISSIONS_REQUEST_CALL_PHONE);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.CALL_PHONE},
                            PERMISSIONS_REQUEST_CALL_PHONE);
                }
            } else {
                sendSMS();
            }
        } else {
            sendSMS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_SEND_SMS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                } else {
                    Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
