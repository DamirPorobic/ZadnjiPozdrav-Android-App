package info.zadnjipozdrav.zadnjipozdrav;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
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
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String intervalString = context.getString(R.string.notify_interval);
            int interval = prefs.getInt(intervalString, 1000 * 60);

            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + interval,
                    interval,
                    pendingIntent);
        }
    }

}
