package id.exorty.monira.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import id.exorty.monira.R;
import id.exorty.monira.helper.Util;

import static id.exorty.monira.helper.Util.SaveSharedPreferences;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        ImageView mBtnMenu = findViewById(R.id.menu_icon);
        mBtnMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.START))
                    drawer.closeDrawer(Gravity.START);
                else
                    drawer.openDrawer(Gravity.START);
            }
        });

        int level = Util.GetSharedPreferences(MainActivity.this, "level", -1);

        if (level == 3){
            ImageView btnCCTV = findViewById(R.id.btn_list_notifications);
            btnCCTV.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_videocam_white_24dp));
            btnCCTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, cctvActivity.class);
//                    intent.putExtra("id", mIdSatker);
//                    intent.putExtra("description", mSatkerName);
                    startActivity(intent);

                }
            });
        }else {
            ImageView btnNotificationList = findViewById(R.id.btn_list_notifications);
            btnNotificationList.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_notifications_white_24dp));
            btnNotificationList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SatkerActivity.class);
                    startActivity(new Intent(MainActivity.this, NotificationListActivity.class));
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        onExit();
    }

    private void onExit(){
        final View customLayout = getLayoutInflater().inflate(R.layout.logout_layout, null);

        View btn_logout = customLayout.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreferences(MainActivity.this, "token", "");
                SaveSharedPreferences(MainActivity.this, "full_name", "");
                SaveSharedPreferences(MainActivity.this, "level", 3);

                finish();
            }
        });

        View btn_closed = customLayout.findViewById(R.id.btn_closed);
        btn_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom)
                .setTitle("Log Out")
                .setView(customLayout)
                .setPositiveButton(R.string.material_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
