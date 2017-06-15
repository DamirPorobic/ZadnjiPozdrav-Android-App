package info.zadnjipozdrav.zadnjipozdrav;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class App extends Application {
    private static final String FIRST_RUN = "app_first_run";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        /**
         * Only on first run, make sure we start downloader that downloads borough entries, we need
         * those to provide a list for the user to chose a filter from. When the download finishes
         * we start the selectFilterActivity so the users chooses a filter.
         */
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean(FIRST_RUN, true)) {

            Downloader d = new Downloader();
            d.execute();

            AlarmReceiver.enableNotification(this, true, NotifyInterval.OneHour.getValue());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(FIRST_RUN, false);
            editor.apply();
        }
    }

    public static Context getContext() {
        return context;
    }

    private class Downloader extends AsyncTask<Void, Void, Void> {
        private DataSource dataSource;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataSource = new DataSource(App.this);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // For every URL, we first try to get the JSON from web server, then we try to parse it
            // into database.
            DownloadHelper dh = new DownloadHelper();
            dh.getBoroughs(dh.getJson(App.this.getString(R.string.url_opstine)), dataSource);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Intent intent = new Intent(App.this, SelectFilterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(App.this, 22, intent, 0);
            try {
                pendingIntent.send();
            }
            catch (PendingIntent.CanceledException e) {
                Log.e("App:onPostExecute", "Failed to start SelectFilterActivity: " + e.getMessage());
            }
        }
    }
}
