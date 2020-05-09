package id.exorty.snpk.monira.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

import id.exorty.snpk.monira.R;

public class Util {
    public static String getRealizationColor(Context context){
        return String.format("#%06x", ContextCompat.getColor(context, R.color.colorRealization) & 0xffffff);
    }

    public static String getPrognosisColor(Context context){
        return String.format("#%06x", ContextCompat.getColor(context, R.color.colorPrognosis) & 0xffffff);
    }

    public static String getEndOfYear(Context context){
        return String.format("#%06x", ContextCompat.getColor(context, R.color.colorEndOfYear) & 0xffffff);
    }

    public static String getLastYear(Context context){
        return String.format("#%06x", ContextCompat.getColor(context, R.color.colorLastYear) & 0xffffff);
    }

    public static String getCurrentDateTime(){
        String format = "EEEE, dd MMMM yyyy";
        Date currentDateTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        return formatter.format(currentDateTime);
    }

    public static void SaveSharedPreferences(Context context, String key, String value){
        SharedPreferences settings = context.getSharedPreferences("id.exorty.monira", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public static String GetSharedPreferences(Context context, String key, String defaultvalue){
        SharedPreferences settings = context.getSharedPreferences("id.exorty.monira", Activity.MODE_PRIVATE);

        if (settings.getString(key, defaultvalue) != null)
            return settings.getString(key, defaultvalue);
        else
            return defaultvalue;
    }

    public static boolean HasConnection(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

}
