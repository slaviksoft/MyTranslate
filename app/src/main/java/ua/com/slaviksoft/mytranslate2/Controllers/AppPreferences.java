package ua.com.slaviksoft.mytranslate2.Controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Created by Slavik on 27.11.2016.
 */

public class AppPreferences {

    private static String SOURCE_LANG = "source_lang";
    private static String TARGET_LANG = "target_lang";

    private static SharedPreferences getPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getSourceLanguage(Context context) {
        return getPreferences(context).getString(SOURCE_LANG, getLocaleLanguage());
    }

    public static void setSourceLanguage(Context context, String language) {
        getPreferences(context).edit().putString(SOURCE_LANG, language).commit();
    }

    public static String getTargetLanguage(Context context) {
        return getPreferences(context).getString(TARGET_LANG, "en");
    }

    public static void setTargetLanguage(Context context, String language) {
        getPreferences(context).edit().putString(TARGET_LANG, language).commit();
    }

    public static String getLocaleLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static boolean isInternetConnectionActive(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
