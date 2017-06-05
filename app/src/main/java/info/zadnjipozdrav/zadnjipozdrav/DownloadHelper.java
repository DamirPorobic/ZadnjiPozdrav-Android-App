package info.zadnjipozdrav.zadnjipozdrav;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DownloadHelper {
    public static final int FULL_UPDATE = 0;
    public static final int INCREMENTAL_UPDATE = 1;

    private static final String JSON_BO_ID = "opstId";
    private static final String JSON_BO_NAME = "Naziv";

    private static final String JSON_CE_ID = "grobljeId";
    private static final String JSON_CE_NAME = "naziv";
    private static final String JSON_CE_ADDRESS = "adresa";
    private static final String JSON_CE_LATITUDE = "latitude";
    private static final String JSON_CE_LONGITUDE = "longitude";

    private static final String JSON_OB_ID = "cituljeId";
    private static final String JSON_OB_NAME = "ime";
    private static final String JSON_OB_FAMILYNAME = "prezime";
    private static final String JSON_OB_FATHERSNAME = "ImeOca";
    private static final String JSON_OB_MAIDENNAME = "DjevojackoPrezime";
    private static final String JSON_OB_BIRTHDATE = "rodjen";
    private static final String JSON_OB_DEATHDATE = "preminuo";
    private static final String JSON_OB_TEXT = "text";
    private static final String JSON_OB_FUNERALDATE = "DatumPogreba";
    private static final String JSON_OB_FUNERALTIME = "VrijemePogreba";
    private static final String JSON_OB_PICTURE = "Slika";
    private static final String JSON_OB_RELIGION_ID = "relId";
    private static final String JSON_OB_CEMETERY_ID = "grobId";
    private static final String JSON_OB_BOROUGH_ID = "opstinaId";

    /**
     * Tries to get content from web server for provided URL and then tries to parse the content
     * into an JSON Array and JSON Objects.
     * @param url  URL from where the JSON should be downloaded.
     * @return     A list of JSON Objects parsed from web server response. If parsing failed or an
     *             error occurred an empty list is returned.
     */
    public List<JSONObject> getJson(String url) {
        HttpHandler http = new HttpHandler();
        return parseJson(http.makeServiceCall(url));
    }

    /**
     * Parses JSON and returns a list with valid JSON objects.
     * @param jsonString  String returned by web server representing full JSON output.
     * @return            List of valid JSON objects. If parsing failed, empty list is returned.
     */
    public List<JSONObject> parseJson(String jsonString) {
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
    public void getBoroughs(List<JSONObject> list, DataSource dataSource) {
        if (list == null || list.isEmpty()) {
            return;
        }
        dataSource.open();
        for(JSONObject o : list) {
            try {
                dataSource.createBorough(o.getLong(JSON_BO_ID), o.getString(JSON_BO_NAME));
            } catch (JSONException e) {
                Log.e("getBorough", "Failed to parse JSON object: " + e.toString());
            }
        }
        dataSource.close();
    }

    /**
     * Tries to parse a List of JSON Objects into Cemetery entries in SQLite database.
     * @param list  List of JSON objects of type Cemetery that will be parsed into database
     */
    public void getCemetery(List<JSONObject> list, DataSource dataSource) {
        if (list == null || list.isEmpty()) {
            return;
        }

        dataSource.open();
        for(JSONObject o : list) {
            try {
                dataSource.createCemetery(o.getLong(JSON_CE_ID),
                        o.getString(JSON_CE_NAME),
                        o.getString(JSON_CE_ADDRESS),
                        o.getDouble(JSON_CE_LATITUDE),
                        o.getDouble(JSON_CE_LONGITUDE)
                );
            } catch (JSONException e) {
                Log.e("getCemetery", "Failed to parse JSON object: " + e.toString());
            }
        }
        dataSource.close();
    }

    /**
     * Tries to parse a List of JSON Objects into Obituary entries in SQLite database.
     * @param list  List of JSON objects of type Obituary that will be parsed into database
     */
    public void getObituary(List<JSONObject> list, DataSource dataSource) {
        if (list == null || list.isEmpty()) {
            return;
        }

        dataSource.open();
        for(JSONObject o : list) {
            try {
                dataSource.createObituary(o.getLong(JSON_OB_ID),
                        o.getString(JSON_OB_FAMILYNAME),
                        o.getString(JSON_OB_FAMILYNAME),
                        o.getString(JSON_OB_FATHERSNAME),
                        o.getString(JSON_OB_MAIDENNAME),
                        -440650000,    //   JSON_OB_BIRTHDATE
                        1489791200,    //   JSON_OB_DEATHDATE
                        o.getString(JSON_OB_TEXT),
                        1489968000,    //    JSON_OB_FUNERALDATE
                        1490027000,    //     JSON_OB_FUNERALTIME
                        null,          //     JSON_OB_PICTURE
                        o.getInt(JSON_OB_RELIGION_ID),
                        o.getLong(JSON_OB_CEMETERY_ID),
                        o.getLong(JSON_OB_BOROUGH_ID)
                );
            } catch (JSONException e) {
                Log.e("getObituary", "Failed to parse JSON object: " + e.toString());
            }
        }
        dataSource.close();
    }
}
