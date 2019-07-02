package com.example.mynewsapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ResultsLoader extends AsyncTaskLoader<List<Result>> {

    String stringUrl = "https://content.guardianapis.com/search?api-key=26c9d5e4-98f2-4abc-8ed2-eba0bea2e26b";

    static ArrayList<Result> resultsArray = new ArrayList<Result>();

    public ResultsLoader(Context context) {
        super(context);
    }

    @Nullable
    @Override
    public List<Result>loadInBackground() {

        return sendRequest();
    }

    private List<Result> sendRequest() {
        URL url = createUrl(stringUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("ResultLoader", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Result> results = extractResultsFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return results;
    }

    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("ResultLoader", "Problem building the URL ", e);
        }
        return url;
    }

    private List<Result> extractResultsFromJson(String resultJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(resultJSON)) {
            return null;
        }

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(resultJSON);

            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray resultArray = response.getJSONArray("results");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < resultArray.length(); i++) {

                JSONObject currentResult = resultArray.getJSONObject(i);


                String id = currentResult.getString("id");

                String sectionName = currentResult.getString("sectionName");

                String webUrl = currentResult.getString("webUrl");

                String webTitle = currentResult.getString("webTitle");

                Result result = new Result(id, sectionName, webTitle, webUrl);

                resultsArray.add(result);
                Log.e("MainActivity", "Adding result" + result._sectionName + ", " + result._webUrl + ", " + result._id);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return resultsArray;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("ResultLoader", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("ResultLoader", "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}