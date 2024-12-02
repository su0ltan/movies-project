package com.example.mainactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import java.util.Locale;

public class AppUtils {
    public static void toggleLanguage(Context context, Class<?> activityClass) {
        String currentLanguage = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                .getString("Language", Locale.getDefault().getLanguage());
        String newLanguage = currentLanguage.equals("ar") ? "en" : "ar";

        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).edit()
                .putString("Language", newLanguage).apply();

        Locale locale = new Locale(newLanguage);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
