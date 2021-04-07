package com.cubezytech.fuelrefill.Applications;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Creating an extended library configuration.
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("e30178be-7077-4fc7-9441-293425ae8835")
                .withCrashReporting(true)
                .withLogs()
                .withStatisticsSending(true)
                .withNativeCrashReporting(true)
                .build();
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this);
    }
}
