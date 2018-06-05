package bit.basemodule;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;

import java.util.concurrent.TimeUnit;

import bit.basemodule.utility.AppSharedPref;
import okhttp3.OkHttpClient;

public class BaseApplication extends Application {
    private static final int maxRequests = 20;
    private static final int maxRequestsPerHost = 5;
    private static final int timeoutTime = 30;
    private static String YUMCHEK_DIRECTORY;
    private static Context mContext;
    private static SharedPreferences sharedPreferences;
    private static AppSharedPref appSharedPreference;
    private static OkHttpClient okHttpClientInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
        AppSharedPref.initialize(this);
        setHttpObject();
        createAppFolder();

    }

    public void setHttpObject() {
        try {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(timeoutTime, TimeUnit.SECONDS);
            builder.connectTimeout(timeoutTime, TimeUnit.SECONDS);
            builder.writeTimeout(timeoutTime, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);
            if (BuildConfig.DEBUG) {
                builder.addNetworkInterceptor(new StethoInterceptor());
            }
            okHttpClientInstance = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAppFolder() {
        YUMCHEK_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/" + "Test";
        File folder = new File(YUMCHEK_DIRECTORY);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public static OkHttpClient getOkHttpClientInstance() {
        return okHttpClientInstance;
    }
}
