package com.example.pallvi.login;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;


public class CPass extends AppCompatActivity {

    DbHelper db;
    EditText old_pass, new_cpass, renew_pass, etemail1, etEmail;
    TextView id1;
    String emailHolder;

    TextView textView, getEmail;
    Button btn_cpass_save, cancel_cpass;
    /* String old_pass1,new_cpass1,renew_pass1;*/


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_pass);
        db = new DbHelper(this);


        Intent i =getIntent();
        String c_password = i.getStringExtra("change_password");
      /*  EditText etEmail1 = findViewById(R.id.etEmail);
        etEmail1.setText(c_password); */
        TextView textView=findViewById(R.id.textView2);
        textView.setText(c_password);



        //   etEmail = (EditText) findViewById(R.id.etEmail);
        getEmail=(TextView) findViewById(R.id.textView2);
        old_pass = (EditText) findViewById(R.id.old_pass);
        new_cpass = (EditText) findViewById(R.id.new_pass);
        renew_pass = (EditText) findViewById(R.id.renew_pass);
        //  email1 = (EditText) findViewById(R.id.email1);
        btn_cpass_save = (Button) findViewById(R.id.btn_cpass_save);
        cancel_cpass = (Button) findViewById(R.id.cancel_cpass);

        //    show_email=(TextView) findViewById(R.id.user_email);


        btn_cpass_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData();
            }
        });


        cancel_cpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CPass.this, MainActivity.class);
                intent.putExtra("edit_profile", emailHolder);
                startActivity(intent);
            }
        });

    }

    public void UpdateData() {

        String old_pass1 = old_pass.getText().toString().trim();
        String new_cpass1 = new_cpass.getText().toString().trim();
        String renew_pass1 = renew_pass.getText().toString().trim();


        if (old_pass == null || "".equalsIgnoreCase(old_pass1)) {
            String header = "OLD PASSWORD REQUIRE";

            Toast.makeText(getApplicationContext(), header, Toast.LENGTH_LONG).show();
        } else if (new_cpass == null || "".equalsIgnoreCase(new_cpass1)) {
            String header = "NEW PASSWORD IS REQUIRE";
            Toast.makeText(getApplicationContext(), header, Toast.LENGTH_LONG).show();
        } else if (renew_pass == null || "".equalsIgnoreCase(renew_pass1)) {
            String header = "CONFIRM PASSWORD IS REQUIRE";
            Toast.makeText(getApplicationContext(), header, Toast.LENGTH_LONG).show();
        }
        /*else if (!new_cpass.equals(renew_pass)) {
            String header = "PASSWORD DOES NOT MATCH";
            Toast.makeText(getApplicationContext(), header, Toast.LENGTH_LONG).show();
        }*/
        else {
            btn_cpass_save.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {

                            boolean isUpdate = updatepass1(getEmail.getText().toString(),new_cpass.getText().toString());


                            if (isUpdate == true)
                                Toast.makeText(CPass.this, "Data Update", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(CPass.this, "Data not Updated", Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }
    }

    public boolean updatepass1(String email, String password) {



        SQLiteDatabase data= db.getWritableDatabase();
        ContentValues values = new ContentValues();
        /* values.put(COLUMN_ID,id);*/


        values.put(inv.invClass.COLUMN_EMAIL, email);
        values.put(inv.invClass.COLUMN_PASS, password);


        data.update(inv.invClass.TABLE_NAME, values, "email = ?", new String[]{email});
        return true;


    }
}