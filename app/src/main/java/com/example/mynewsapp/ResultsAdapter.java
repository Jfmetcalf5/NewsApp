package com.example.mynewsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getWebUrl()));
                getContext().startActivity(browserIntent);
            }
        });

        TextView webTitleTextView = convertView.findViewById(R.id.webTitle);
        webTitleTextView.setText(result.getwebTitle());

        TextView webUrlTextView = convertView.findViewById(R.id.webUrl);
        webUrlTextView.setText(result.getWebUrl());

        TextView sectionNameTextView = convertView.findViewById(R.id.sectionName);
        sectionNameTextView.setText(result.getSectionName());

        TextView dateTextView = convertView.findViewById(R.id.date);
        String dateString = result.getWebPublicationDate();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        try {
            Date date = format.parse(dateString);
            String formattedDate = format.format(date);
            dateTextView.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView authorTextView = convertView.findViewById(R.id.author);
        authorTextView.setText(result.getAuthor());

        return convertView;
    }
}
