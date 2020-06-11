package id.exorty.monira.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Calendar;

import id.exorty.monira.R;
import id.exorty.monira.helper.Util;
import id.exorty.monira.service.DataService;

import static id.exorty.monira.helper.Util.GetSharedPreferences;

public class SplashScreen extends Activity {
    private static final long SPLASH_DISPLAY_LENGTH = 600;
    private int mLoop = 0;

    private AVLoadingIndicatorView mAvloadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        mAvloadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.avloadingIndicatorView);

        String token = GetSharedPreferences(SplashScreen.this, "token", "");

        if (token.equals("")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }else{
            initApp();
        }
    }

    private void initApp(){
        int year = Calendar.getInstance().get(Calendar.YEAR);

        DataService dataService = new DataService(SplashScreen.this, new DataService.DataServiceListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void OnSuccess(JsonObject jsonObject, String message) {
                Util.SaveSharedPreferences(SplashScreen.this,"national_data", jsonObject.toString());
                Util.SaveSharedPreferences(SplashScreen.this,"national_data_current_month", jsonObject.get("current_month").asObject().toString());

                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);

                finish();
            }

            @Override
            public void OnSuccess(JsonArray jsonArray, String message) {
            }

            @Override
            public void OnFailed(String message) {
                if (message.equals("Provide token is expired")
                        || message.equals("Token not provided.")){
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    if (mLoop < 3) {
                        mLoop++;
                        initApp();
                    } else {
                        mLoop = 0;
                        Toast.makeText(SplashScreen.this,
                                "Failed to get data from server. Please contact admin : " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).GetNationalData(year);
    }
}
