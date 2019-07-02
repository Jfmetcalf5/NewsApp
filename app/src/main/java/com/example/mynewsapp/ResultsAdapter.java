package com.example.mynewsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Result result = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        View container = convertView.findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result._webUrl));
                getContext().startActivity(browserIntent);
            }
        });

        TextView webTitle = convertView.findViewById(R.id.webTitle);
        webTitle.setText(result._webTitle);

        TextView webUrl = convertView.findViewById(R.id.webUrl);
        webUrl.setText(result._webUrl);

        TextView sectionName = convertView.findViewById(R.id.sectionName);
        sectionName.setText(result._sectionName);

        return convertView;
    }
}
