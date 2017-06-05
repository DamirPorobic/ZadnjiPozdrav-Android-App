package info.zadnjipozdrav.zadnjipozdrav;

import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.io.BufferedReader;

public class HttpHandler {
    private final int timeout = 5000;   // Timeout in miliseconds

    public String makeServiceCall(String requestUrl) {
        String response = null;

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(timeout);     // Connection timeout
            conn.setReadTimeout(timeout * 2);    // Timeout for reading the server response
            InputStream input = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(input);
        } catch (MalformedURLException e) {
            Log.e("makeServiceCall", "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e("makeServiceCall", "ProtocolException: " + e.getMessage());
        } catch (SocketTimeoutException e) {
            Log.e("makeServiceCall", "Request to server timed out.");
        } catch (IOException e) {
            Log.e("makeServiceCall", "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e("makeServiceCall", "Exception: " + e.getMessage());
        }
        return response;
    }

    /**
     * Parses input stream returned by web servers into string.
     * @param inputStream  Input stream returned from web server that will be parsed.
     * @return             String representing response from web server.
     */
    private String convertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e("convertStreamToString", "Error while parsing server response: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e("convertStreamToString", "Error while closing input stream: " + e.getMessage());
            }
        }
        return stringBuilder.toString();
    }
}
