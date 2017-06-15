package info.zadnjipozdrav.zadnjipozdrav;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootReceiver extends BroadcastReceiver {

    /**
     * Called when the phone boots up and starts the notification alarm that checks for new updates
     * in the obituary table.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String intervalString = context.getString(R.string.settings_notify_interval_key);
            long interval = prefs.getLong(intervalString, NotifyInterval.OneHour.getValue());
            AlarmReceiver.enableNotification(context, true, interval);
        }
    }

}
