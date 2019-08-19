package com.example.pallvi.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    Button btnLogout, add_party, view_trans;
    TextView view_email;
    Button btn_cpass, e_profile, search_btn;
    DbHelper db;
    static String emailHolder, nameHolder;
    static String imgHolder;
    ImageView imageView;
    Toolbar mTopToolbar;
    EditText searchbar;
    DbHelper dbHelper;
    static Uri imagUri;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        view_email = (TextView) findViewById(R.id.view_email);
        e_profile = (Button) findViewById(R.id.edit_profile);
        btn_cpass = (Button) findViewById(R.id.btn_cpass);
        imageView = (ImageView) findViewById(R.id.img_view);
        searchbar = (EditText) findViewById(R.id.search_bar);
        search_btn = (Button) findViewById(R.id.btn_search);
        btn=(Button)findViewById(R.id.use);

        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);

        //TO PASS USER NAME OR EMAIL FROM ONE PAGE TO ANOTHER
        Intent intent = getIntent();
        emailHolder = intent.getStringExtra("UserEmail");
        imgHolder = intent.getStringExtra("img1");
        nameHolder=intent.getStringExtra("username");


        getSupportActionBar().setTitle("Welcome " + nameHolder);


        imagUri = Uri.parse(imgHolder);
        // Setting up received email to TextView.
        view_email.setText(emailHolder);
        // SENDING IMAGE
        imageView.setImageURI(imagUri);
        // Receiving User Email Send By MainActivity.

        //   imageView.setImageBitmap(getBitmapFromUri(imagUri));
        // either we use setImageURI or we will use setImageBitmap. We cant use both at single time.
        //CODE ENDS PASS


        // SEARCH BUTTON STARTS HERE
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchbar.getText().toString().replace("", "+");
                Intent intent1 = new Intent(MainActivity.this, BookListing.class);
                intent1.putExtra("search string", query);
                startActivity(intent1);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                use();
            }
        });

    }


    private void use(){
    Intent use1 = new Intent(MainActivity.this,Use.class);
    use1.putExtra("email", emailHolder);
    startActivity(use1);
    }

    private void logout() {
        finish();
        startActivity(new Intent(MainActivity.this, login.class));
    }


    public void c_pass() {

        Intent i = new Intent(this, CPass.class);
        i.putExtra("change_password", emailHolder);
        startActivity(i);
    }

    public void edit_prof() {
        Intent intent = new Intent(MainActivity.this, EditProfile.class);
        intent.putExtra("edit_profile", emailHolder);
        startActivity(intent);
    }

    // optional menu or overflow menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:
                edit_prof();
                return true;
            case R.id.btn_cpass:
                c_pass();
                return true;
            case R.id.btnLogout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}