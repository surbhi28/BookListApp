package com.example.pallvi.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Use extends AppCompatActivity {

    Toolbar Top_bar;
    TextView about,details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use);

        Top_bar = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(Top_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        about=(TextView)findViewById(R.id.use1);
        details=(TextView)findViewById(R.id.use2);

        about.setText("How to use the Book Listing App");
        details.setText("Booking List App it's a kind of Search based on BOOKS.GOOGLE.COM which has been made on Udacity Android Basic Nanodegree course as a #7 partial project. [HTTP Networking, JSON Parsing, Threads & Parallelism, ListView, Adapter, Loader]");
    }
}