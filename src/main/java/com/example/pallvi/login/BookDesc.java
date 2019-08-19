package com.example.pallvi.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BookDesc extends AppCompatActivity {
    private static final String LOG_TAG = BookDesc.class.getSimpleName();
    ImageButton img_btn;
    Button read_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desc_layout);

        Toolbar bar = (Toolbar) findViewById(R.id.up_button);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Book book = (Book) i.getSerializableExtra("Object");

        getSupportActionBar().setTitle(book.getTitle());

        Log.d(MainActivity.class.getName(), book.getImage());

        final String url = book.getLink();
        Log.d(BookListing.class.getName(), book.getLink());
        img_btn = (ImageButton) findViewById(R.id.imageButton);

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                share(url);
            }
        });

        read_book = (Button) findViewById(R.id.read_button);
        read_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Intent.ACTION_VIEW);
                a.setData(Uri.parse(url));
                startActivity(a);
            }
        });

        ImageView imgbig = (ImageView) findViewById(R.id.imageView);

        Glide.with(getApplicationContext()).load(book.getImage()).into(imgbig);

        TextView name = (TextView) findViewById(R.id.book_title);
        name.setText(book.getTitle());

        TextView auth = (TextView) findViewById(R.id.book_auth);
        auth.setText(book.getAuthor());

        TextView publisher = (TextView) findViewById(R.id.book_publ);
        publisher.setText(book.getPublish());

        TextView price = (TextView) findViewById(R.id.book_price);
        price.setText(book.getPrice());

        TextView currency = (TextView) findViewById(R.id.code);
        currency.setText(book.getCurr());

        TextView desc = (TextView) findViewById(R.id.book_desc);
        desc.setText("Description: " + book.getDescr());

    }

    private void share(String link) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Insert Subject here");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}