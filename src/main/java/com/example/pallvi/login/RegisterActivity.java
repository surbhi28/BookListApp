package com.example.pallvi.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity  implements View.OnClickListener{

    Button reg, upload_btn;
    TextView tvLogin;
    RadioGroup radioSexGroup ;
    EditText etEmail, etPass, user_name, user_address, u_contact, user_dob;
    ImageView image_view;
    private static final int PICK_IMAGE_REQUEST = 0;
    private Uri mUri;
    private String currentPhotoPath = "no images";
    private static final String LOG_TAG=login.class.getSimpleName();



    DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DbHelper(this);
        image_view = (ImageView) findViewById(R.id.img_view);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        upload_btn=(Button)findViewById(R.id.btn_upload);
        user_address=(EditText)findViewById(R.id.address2);
        user_name=(EditText)findViewById(R.id.name);
        user_dob=(EditText)findViewById(R.id.etdob);
        u_contact=(EditText)findViewById(R.id.phone);
        reg = (Button)findViewById(R.id.btnReg);
        tvLogin = (TextView)findViewById(R.id.tvLogin);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPass = (EditText)findViewById(R.id.etPass);

        reg.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_image();
            }
        });
    }


    public void upload_image(){
        Intent intent;

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }

        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {

            if (resultData != null) {
                mUri = resultData.getData();
                Log.i(LOG_TAG, "Uri: " + mUri.toString());

                currentPhotoPath = mUri.toString();

                image_view.setImageBitmap(getBitmapFromUri(mUri));

            }
        }
    }


    public Bitmap getBitmapFromUri(Uri uri){
        if (uri == null || uri.toString().isEmpty())
            return null;

        int widh = image_view.getWidth();
        int height = image_view.getHeight();

        InputStream inputStream = null;
        try{
            inputStream = this.getContentResolver().openInputStream(uri);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, bmOptions);
            inputStream.close();

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;


            int scaleFactor = Math.min(photoW / widh, photoH / height);


            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            inputStream = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, bmOptions);
            inputStream.close();
            return bitmap;

        } catch (FileNotFoundException fne) {
            Log.e(LOG_TAG, "Failed to load image.", fne);
            return null;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load image.", e);
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException ioe) {

            }
        }
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnReg:
                register();
                break;
            case R.id.tvLogin:
                startActivity(new Intent(RegisterActivity.this,login.class));
                finish();
                break;
            default:

        }
    }

    private void register() {

        String uname=user_name.getText().toString().trim();
        String uadd=user_address.getText().toString().trim();
        String udob=user_dob.getText().toString();


        int selectedId = radioSexGroup.getCheckedRadioButtonId();

        Log.d(LOG_TAG,"Selected id is " +selectedId);
        String t;

        if(selectedId == -1){
            t = "No gender";
        }else {

            RadioButton ugender = (RadioButton) findViewById(selectedId);
            t = ugender.getText().toString();
        }


        String uphone=u_contact.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();


        if(TextUtils.isEmpty(uname) &&
                uadd.isEmpty() &&
                udob.isEmpty() &&
                uphone.isEmpty() &&
                email.isEmpty() &&
                pass.isEmpty() &&
                currentPhotoPath.equals("no images")&&
                t.equals("No gender")){

            displayToast("All Fields are Required");
            return;

        }else if(TextUtils.isEmpty(uname) ||
                uadd.isEmpty() ||
                udob.isEmpty() ||
                uphone.isEmpty() ||
                email.isEmpty() ||
                pass.isEmpty() ||
                currentPhotoPath.equals("no images")||
                t.equals("No gender")){

            displayToast("All Fields are Required");
            return;

        }
        else{
            addUser(uname,uadd,udob, t, uphone,email,pass, currentPhotoPath);
            finish();
        }
    }

    public void addUser(String uname, String uadd, String udob, String gender, String uphone, String email, String password, String image) {
        db = new DbHelper(this);
        SQLiteDatabase data = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(inv.invClass.COLUMN_UNAME, uname);
        values.put(inv.invClass.COLUMN_ADDRESS, uadd);
        values.put(inv.invClass.COLUMN_UDOB, udob);
        values.put(inv.invClass.COLUMN_GENDER,gender);
        values.put(inv.invClass.COLUMN_PHONE, uphone);

        values.put(inv.invClass.COLUMN_EMAIL, email);
        values.put(inv.invClass.COLUMN_PASS, password);
        values.put(inv.invClass.COLUMN_IMAGE,image);

        long id = data.insert(inv.invClass.TABLE_NAME, null, values);
        if (id>0){
            Toast.makeText(getApplicationContext(),"User Registered",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"SomeThing Went Wrong",Toast.LENGTH_LONG).show();
        }
        db.close();

        Log.d(TAG, "user inserted" + id);
    }


    private void displayToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}