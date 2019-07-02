package com.example.mynewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ResultsAdapter extends ArrayAdapter<Result> {

    public ResultsAdapter(Context context, ArrayList<Result> results) {
        super(context, 0, results);
    }

    public void setResults(List<Result> results) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Result result = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView webTitle = convertView.findViewById(R.id.webTitle);
        webTitle.setText(result._webTitle);

        TextView webUrl = convertView.findViewById(R.id.webUrl);
        webTitle.setText(result._webUrl);

        TextView sectionName = convertView.findViewById(R.id.sectionName);
        webTitle.setText(result._sectionName);

        return convertView;
    }
}
