package info.zadnjipozdrav.zadnjipozdrav;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import org.json.JSONObject;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    /**
     * When alarmReceiver is triggered, we try to download a list of obituaries, but we don't parse
     * them into database, we check only if the list contains some entries.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadHelper dh = new DownloadHelper();
        List<JSONObject> list = dh.getJson(context.getString(R.string.url_citulje));
        if (list.size() > 0) {
            showNotification(context);
        }
    }

    private void showNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(context.getString(R.string.notify_message));

        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application to
        // the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Id allows you to update the notification later on, we use zero, new updates will
        // overwrite
        notificationManager.notify(1, builder.build());
    }

    /**
     * Disable or enable notifications when new obituaries are available.
     * @param context   Context
     * @param enable    True for enabling notification, false for disabling
     * @param interval  Interval in miliseconds in which the receiver will check for new obituaries.
     */
    public static void enableNotification(Context context, boolean enable, long interval) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        if (enable) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + interval,
                    interval,
                    pendingIntent);
            // Enable running notifier on startup
            packageManager.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } else {
            // Disable currently running alarm
            alarmManager.cancel(pendingIntent);

            // Disable running notifier on startup
            packageManager.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }
}
