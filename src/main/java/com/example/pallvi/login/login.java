package com.example.pallvi.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity{

    private static final String LOG_TAG = login.class.getSimpleName();
    Button login, register;
    EditText etEmail, etPass;
    DbHelper db;
    String img, name1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DbHelper(this);

        login = (Button) findViewById(R.id.btnLogin);
        register = (Button) findViewById(R.id.btnReg);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,RegisterActivity.class));
            }
        });

    }


    private void login(){
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();

        if(getUser(email,pass)){
            // Going to Dashboard activity after login success message.
            Intent intent = new Intent(login.this, MainActivity.class);

            // Sending Email to Dashboard Activity using intent.
            intent.putExtra("UserEmail", email);
            intent.putExtra("img1",img);
            intent.putExtra("username", name1);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Wrong email/password",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getUser(String email, String pass) {

        String[] projection = {
                inv.invClass.COLUMN_EMAIL,
                inv.invClass.COLUMN_UNAME,
                inv.invClass.COLUMN_IMAGE

        };
        String selection=
                inv.invClass.COLUMN_EMAIL + "=? AND " +
                        inv.invClass.COLUMN_PASS + "=?";
        String [] selectionArgs = new String[] { email,pass };

        SQLiteDatabase data = db.getReadableDatabase();
        Cursor cursor;
        cursor = data.query(
                inv.invClass.TABLE_NAME,
                projection,
                selection ,
                selectionArgs,null,null,null
        );

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            int name = cursor.getColumnIndex(inv.invClass.COLUMN_UNAME);
            name1=cursor.getString(name);
            Log.d(LOG_TAG, name1);

            int pic = cursor.getColumnIndex(inv.invClass.COLUMN_IMAGE);
            img = cursor.getString(pic);

            return true;

        }

        cursor.close();
        db.close();

        return false;
    }

}