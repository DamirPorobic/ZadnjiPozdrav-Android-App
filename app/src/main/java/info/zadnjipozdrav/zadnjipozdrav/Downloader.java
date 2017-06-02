package info.zadnjipozdrav.zadnjipozdrav;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Downloader extends AsyncTask<Void, Void, Void> {
    private Context context;
    private DataSource datasource;
    private ProgressDialog dialog;
    private ListView listView;

    public Downloader(Context context, ListView list) {
        this.context = context;
        datasource = new DataSource(context);
        listView = list;
    }

    /**
     * Prior to executing we show a progress dialog to inform the user that something is heppening.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        dialog = new ProgressDialog(context);
        dialog.setMessage("Ucitavanje...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        // For every URL, we first try to get the JSON from web server, then we try to parse it into
        // database.
        getBoroughs(getJson(context.getResources().getString(R.string.url_opstine)));
        getCemetery(getJson(context.getResources().getString(R.string.url_groblja)));
        getObituary(getJson(context.getResources().getString(R.string.url_citulje)));

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
        datasource.open();

        List<Obituary> values = datasource.getAllObituaries();
        CustomArrayAdapter adapter = new CustomArrayAdapter(context, values);
        listView.setAdapter(adapter);

        datasource.close();
    }

    /**
     * Tries to get content from web server for provided URL and then tries to parse the content
     * into an JSON Array and JSON Objects.
     * @param url  URL from where the JSON should be downloaded.
     * @return     A list of JSON Objects parsed from web server response. If parsing failed or an
     *             error occurred an empty list is returned.
     */
    private List<JSONObject> getJson(String url) {
        HttpHandler http = new HttpHandler();
        return parseJson(http.makeServiceCall(url));
    }

    /**
     * Parses JSON and returns a list with valid JSON objects.
     * @param jsonString  String returned by web server representing full JSON output.
     * @return            List of valid JSON objects. If parsing failed, empty list is returned.
     */
    private List<JSONObject> parseJson(String jsonString) {
        List<JSONObject> list = new ArrayList<>();

        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);

                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(jsonArray.getJSONObject(i));
                }
            } catch (final JSONException e) {
                Log.e("parseJson", "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e("parseJson", "Provided JSON String is invalid.");
        }
        return list;
    }

    /**
     * Tries to parse a List of JSON Objects into Borough entries in SQLite database.
     * @param list  List of JSON objects of type Borough that will be parsed into database
     */
    private void getBoroughs(List<JSONObject> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        datasource.open();
        for(JSONObject o : list) {
            try {
                datasource.createBorough(o.getLong("opstId"), o.getString("Naziv"));
            } catch (JSONException e) {
                Log.e("getBorough", "Failed to parse JSON object: " + e.toString());
            }
        }
        datasource.close();
    }

    /**
     * Tries to parse a List of JSON Objects into Cemetery entries in SQLite database.
     * @param list  List of JSON objects of type Cemetery that will be parsed into database
     */
    private void getCemetery(List<JSONObject> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        datasource.open();
        for(JSONObject o : list) {
            try {
                datasource.createCemetery(o.getLong("grobljeId"),
                        o.getString("naziv"),
                        o.getString("adresa"),
                        o.getDouble("latitude"),
                        o.getDouble("longitude")
                );
            } catch (JSONException e) {
                Log.e("getCemetery", "Failed to parse JSON object: " + e.toString());
            }
        }
        datasource.close();
    }

    /**
     * Tries to parse a List of JSON Objects into Obituary entries in SQLite database.
     * @param list  List of JSON objects of type Obituary that will be parsed into database
     */
    private void getObituary(List<JSONObject> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        datasource.open();
        for(JSONObject o : list) {
            try {
                datasource.createObituary(o.getLong("cituljeId"),
                        o.getString("ime"),
                        o.getString("prezime"),
                        o.getString("ImeOca"),
                        o.getString("DjevojackoPrezime"),
                        -440650000,
                        1489791200,
                        o.getString("text"),
                        1489968000,
                        1490027000,
                        null,
                        o.getInt("relId"),
                        o.getLong("grobId"),
                        o.getLong("opstinaId")
                );
            } catch (JSONException e) {
                Log.e("getObituary", "Failed to parse JSON object: " + e.toString());
            }
        }
        datasource.close();
    }
}
