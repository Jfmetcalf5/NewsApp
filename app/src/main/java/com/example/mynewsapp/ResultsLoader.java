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

    String stringUrl = "http://content.guardianapis.com/search?show-references=true&api-key=26c9d5e4-98f2-4abc-8ed2-eba0bea2e26b";

    static ArrayList<Result> resultsArray = new ArrayList<Result>();

    @Override
    public void deliverResult(@Nullable List<Result> data) {
        super.deliverResult(data);
    }

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
        if (TextUtils.isEmpty(resultJSON)) {
            return null;
        }

        try {

            JSONObject baseJsonResponse = new JSONObject(resultJSON);

            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray resultArray = response.getJSONArray("results");

            for (int i = 0; i < resultArray.length(); i++) {

                JSONObject currentResult = resultArray.getJSONObject(i);

                String sectionName = currentResult.getString("sectionName");

                String webUrl = currentResult.getString("webUrl");

                String webTitle = currentResult.getString("webTitle");

                String webPublicationDate = currentResult.getString("webPublicationDate");

                String authorPlaceholder = "No Author";

                if (currentResult.getJSONArray("references") != null) {
                    JSONArray referencesArray = currentResult.getJSONArray("references");
                    for (int r = 0; r < referencesArray.length(); r++) {
                        JSONObject currentReference = resultArray.getJSONObject(i);
                        String author = currentReference.getString("author");
                        authorPlaceholder = author;
                    }
                }

                Result result = new Result(sectionName, webTitle, webUrl, webPublicationDate, authorPlaceholder);

                resultsArray.add(result);
                Log.e("ResultLoader", "Adding result" + result.getSectionName() + ", " + result.getWebUrl()+ ", " + result.getwebTitle());
            }

        } catch (JSONException e) {
            Log.e("ResultsLoader", "Problem parsing the JSON results", e);
        }

        return resultsArray;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("ResultLoader", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("ResultLoader", "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
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