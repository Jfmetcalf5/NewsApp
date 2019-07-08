package com.example.mynewsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Result>> {

    ArrayAdapter<Result> resultsArrayAdapter;

    TextView blankText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blankText = findViewById(R.id.blank_view);
        blankText.setVisibility(View.GONE);

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            getSupportLoaderManager().initLoader(0, null, this).forceLoad();
            resultsArrayAdapter = new ResultsAdapter(this, ResultsLoader.resultsArray);
            ListView listView = findViewById(R.id.list_view);

            listView.setAdapter(resultsArrayAdapter);
        } else {
            hideProgressBar();
            blankText.setVisibility(View.VISIBLE);
            blankText.setText(R.string.noInternet);
        }
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        Log.i("MainActivity", "onCreateLoader Called");
        ResultsLoader resultsLoader = new ResultsLoader(this);
        return resultsLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Result>> loader, List<Result> results) {
        hideProgressBar();
        if (results != null && !results.isEmpty()) {
            resultsArrayAdapter.notifyDataSetChanged();
        } else {
            blankText.setVisibility(View.VISIBLE);
            blankText.setText(R.string.noResults);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        resultsArrayAdapter.clear();
    }

    private void hideProgressBar() {
        ProgressBar progressBar = findViewById(R.id.loading_indicator);
        progressBar.setVisibility(View.GONE);
    }

}
