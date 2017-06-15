package info.zadnjipozdrav.zadnjipozdrav;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.obituary_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final Obituary item = (Obituary) parent.getItemAtPosition(position);
                if (item != null) {
                    Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                    i.putExtra(getResources().getString(R.string.main_obituary_id), item.getId());
                    startActivity(i);
                }
            }

        });

        // Check if any boroughs have been selected as filter, if not, ask the user if he wants to
        // select it in a dialog, if he responds with yes, open settings and let him choose.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Set<String> selections = preferences.getStringSet(getString(R.string.settings_filter_borough_key), null);
        if (selections == null || selections.size() == 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int response) {
                    if (response == DialogInterface.BUTTON_POSITIVE) {
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        intent.putExtra(SettingsActivity.SETUP_FILTER, true);
                        startActivity(intent);
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.main_dialog_addFilter_question));
            builder.setPositiveButton(getString(R.string.main_dialog_addFilter_yes), dialogClickListener);
            builder.setNegativeButton(getString(R.string.main_dialog_addFilter_no), dialogClickListener);
            builder.show();
        }

        refreshList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh_menu:
                refreshList();
                return true;
            case R.id.settings_menu:
                runActivity(SettingsActivity.class);
                return true;
            case R.id.about_menu:
                runActivity(AboutActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Runs downloader and updates list view with latest obituaries.
     */
    private void refreshList() {
        Downloader downloader = new Downloader();
        downloader.execute();
    }

    /**
     * Creates activity of provided class and runs it as a child of this class.
     * @param cls  Class of activity that should be run.
     */
    private void runActivity(Class<?> cls) {
        Intent i = new Intent(MainActivity.this, cls);
        startActivity(i);
    }

    /**
     * Async Task for downloading JSON strings and parsing them into database, the operation is
     * running in the background.
     */
    private class Downloader extends AsyncTask<Void, Void, Void> {
        private DataSource dataSource;
        private ProgressDialog dialog;

        /**
         * Prior to executing we show a progress dialog to inform the user that something is heppening.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataSource = new DataSource(MainActivity.this);

            // Showing progress dialog
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage(MainActivity.this.getString(R.string.main_loading_text));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // For every URL, we first try to get the JSON from web server, then we try to parse it into
            // database.
            DownloadHelper dh = new DownloadHelper();
            dh.getCemetery(dh.getJson(MainActivity.this.getString(R.string.url_groblja)), dataSource);
            dh.getObituary(dh.getJson(MainActivity.this.getString(R.string.url_citulje)), dataSource);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            // Open Database and load all Obituaries from the SQLite database into the listview
            dataSource.open();

            List<Obituary> values = dataSource.getAllObituaries();
            CustomArrayAdapter adapter = new CustomArrayAdapter(MainActivity.this, values);
            listView.setAdapter(adapter);

            dataSource.close();
        }
    }
}
