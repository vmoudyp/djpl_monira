package id.exorty.monira;

import android.app.Application;
import android.app.job.JobInfo;
import android.content.Context;

import org.acra.ACRA;
import org.acra.BuildConfig;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.HttpSenderConfigurationBuilder;
import org.acra.config.LimiterConfigurationBuilder;
import org.acra.config.ToastConfigurationBuilder;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

public class App extends Application {
    private static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this)
                .setBuildConfigClass(BuildConfig.class)
                .setReportFormat(StringFormat.JSON);
        builder.getPluginConfigurationBuilder(ToastConfigurationBuilder.class).setResText(R.string.crash_toast_text);
        builder.getPluginConfigurationBuilder(HttpSenderConfigurationBuilder.class)
                .setUri("https://collector.tracepot.com/21eda81b")
                .setHttpMethod(HttpSender.Method.POST)
//                .setBasicAuthLogin("*****")
//                .setBasicAuthPassword("*****")
                .setEnabled(true);
        builder.getPluginConfigurationBuilder(ToastConfigurationBuilder.class).setEnabled(true);

        this.mContext = getApplicationContext();

        ACRA.init(this, builder);
    }

    public static Context getContext() {
        return mContext;
    }
}
