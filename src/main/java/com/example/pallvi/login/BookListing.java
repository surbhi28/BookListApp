package com.example.pallvi.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class BookListing extends AppCompatActivity {

    // for search book name
    public static final String LOG_TAG = BookListing.class.getName();

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?maxResults=40&q=";
    ProgressBar progressBar;
    // for search book name close
    String query, BOOK_URL;
    BookAsyncTask task;
    Connection connection;
    /**
     * Adapter for the list of books
     */
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_listing);


        Toolbar bar = (Toolbar) findViewById(R.id.up_button1);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // PROCESS BAR STARTS
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);

        Intent intent = getIntent();
        query = intent.getStringExtra("search string");
        // Find a reference to the {@link ListView} in the layout
        final List<Book> books = new ArrayList<Book>();

        final ListView bookListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, books);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        connection = new Connection(this);
        if (connection.isConnected()) {

            BOOK_URL = BASE_URL + query;
            BookAsyncTask task = new BookAsyncTask();
            task.execute(BOOK_URL);

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object b = parent.getItemAtPosition(position);
                Book a = (Book) b;
                Intent i = new Intent(BookListing.this, BookDesc.class);
                i.putExtra("Object", a);
                startActivity(i);
            }
        });
    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Book> doInBackground(String... urls) {
            // Create URL object
            URL url = createUrl(urls[0]);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            List<Book> books = extractBookFromJson(jsonResponse);

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return books;
        }

        protected void onPostExecute(List<Book> data) {
            // Clear the adapter of previous data
            mAdapter.clear();
            progressBar.setVisibility(View.GONE);

            // If there is a valid list of {@link Books}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(30000 /* milliseconds */);
            urlConnection.setConnectTimeout(45000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error Response Code :" + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Problem Retrieving JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
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
    /**
     * Return an {@link Book} object by parsing out information
     * about the query book from the input bookJSON string.
     */
    private List<Book> extractBookFromJson(String bookJSON) {
        if (bookJSON.isEmpty()) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<>();
        String author, price, publisher, currency, description;
        try {
            JSONObject rootObject = new JSONObject(bookJSON);
            JSONArray items = rootObject.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject object = items.getJSONObject(i);

                JSONObject details = object.getJSONObject("volumeInfo");

                String title = details.getString("title");

                if (details.has("authors")) {
                    JSONArray authorsArray = details.getJSONArray("authors");
                    author = authorsArray.getString(0).replace(",", "");
                } else {
                    author = "No author";
                }

                if (details.has("publisher"))
                {
                    publisher = details.getString("publisher");
                } else
                    {
                    publisher = "No publisher";
                }


                if (details.has("description")) {
                    description = details.getString("description");
                } else {
                    description = "No publisher";
                }

                JSONObject links = object.getJSONObject("accessInfo");
                String link = links.getString("webReaderLink");


                JSONObject img = details.getJSONObject("imageLinks");
                String image = img.getString("smallThumbnail");
                String bimage = img.getString("thumbnail");


                JSONObject detail = object.getJSONObject("saleInfo");
                if (detail.has("listPrice")) {
                    JSONObject list = detail.getJSONObject("listPrice");
                    price = list.getString("amount");
                } else {
                    price = "Not for sale";
                }

                if (detail.has("currencyCode")) {
                    JSONObject curr = detail.getJSONObject("currencyCode");
                    currency = curr.getString("currencyCode");
                } else {
                    currency = "Not for sale";
                }


                Book book = new Book(image, bimage, title, link, author, publisher, price, currency, description);
                books.add(book);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }
        return books;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu refresh) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void refresh() {

        if (connection.isConnected()) {
            BookAsyncTask task = new BookAsyncTask();
            task.execute(BOOK_URL);
        } else
            {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
}
