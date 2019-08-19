package com.example.pallvi.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
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


public class EditProfile  extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int PICK_IMAGE_REQUEST = 0;
    private String currentPhotoPath = "no images";
    private boolean mInventoryHasChanged = false;

    EditText p_name, p_dob,p_add, p_phone;
    Button p_cncl, p_update, img_btn;
    TextView email;
    DbHelper db;
    RadioGroup radioSexGroup ;
    ImageView img;

    String edit_prof,name;
    int genid;
    Cursor cursor;
    DbHelper dbHelper;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_prof);


        Intent i =getIntent();
        edit_prof = i.getStringExtra("edit_profile");
        TextView textView=findViewById(R.id.textView5);
        textView.setText(edit_prof);


        dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] projection = {
                inv.invClass.COLUMN_ID,
                inv.invClass.COLUMN_UNAME,
                inv.invClass.COLUMN_ADDRESS,
                inv.invClass.COLUMN_UDOB,
                inv.invClass.COLUMN_GENDER,
                inv.invClass.COLUMN_PHONE,
                inv.invClass.COLUMN_IMAGE
        };
        String selection = inv.invClass.COLUMN_EMAIL + "=?";
        String[] selectionArgs = new String[]{edit_prof};

        cursor = database.query(inv.invClass.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        radioSexGroup=(RadioGroup)findViewById(R.id.radioSex);
        email=(TextView)findViewById(R.id.textView5);
        img_btn=(Button)findViewById(R.id.btn_img_upload);
        img=(ImageView)findViewById(R.id.img_view);
        p_name=(EditText)findViewById(R.id.et_p_name);
        p_add=(EditText)findViewById(R.id.et_p_add);
        p_dob=(EditText)findViewById(R.id.et_p_dob);
        p_phone=(EditText)findViewById(R.id.et_p_phone);
        p_update=(Button)findViewById(R.id.btn_update);
        p_cncl=(Button)findViewById(R.id.et_p_cncl);


        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nname = cursor.getColumnIndex(inv.invClass.COLUMN_UNAME);
            int nadd = cursor.getColumnIndex(inv.invClass.COLUMN_ADDRESS);
            int ndob = cursor.getColumnIndex(inv.invClass.COLUMN_UDOB);
            int ngender = cursor.getColumnIndex(inv.invClass.COLUMN_GENDER);
            int nphone = cursor.getColumnIndex(inv.invClass.COLUMN_PHONE);
            int nimage = cursor.getColumnIndex(inv.invClass.COLUMN_IMAGE);


            // Extract out the value from the Cursor for the given column index
            name = cursor.getString(nname);
            String image = cursor.getString(nimage);
            String address = cursor.getString(nadd);
            String dob = cursor.getString(ndob);
            String gender = cursor.getString(ngender);
            String phone = cursor.getString(nphone);

            if(gender.contains("Female")){
                 genid = R.id.radioFemale;
                radioSexGroup.check(genid);
            }
            else {
                genid = R.id.radioMale;
                radioSexGroup.check(genid);
            }



            // Update the views on the screen with the values from the database
            p_name.setText(name);
            p_add.setText(address);
            p_dob.setText(dob);
            p_phone.setText(phone);

            Log.i(LOG_TAG, "String" + image);



            if ((image == null) || (image.equals("no images"))) {
                img.setImageResource(R.mipmap.ic_launcher);
            } else {
                mUri = Uri.parse(image);
                currentPhotoPath = mUri.toString();

                Log.i(LOG_TAG, "Uri: " + mUri);

                ViewTreeObserver viewTreeObserver = img.getViewTreeObserver();
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        img.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        img.setImageBitmap(getBitmapFromUri(mUri));
                    }
                });
            }
            //monitor activity so we can protect user
            p_name.setOnTouchListener(mTouchListener);
            p_add.setOnTouchListener(mTouchListener);
            p_dob.setOnTouchListener(mTouchListener);
            radioSexGroup.setOnTouchListener(mTouchListener);
            p_phone.setOnTouchListener(mTouchListener);
            img.setOnTouchListener(mTouchListener);


        }

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageSelector();
            }
        });

        p_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
                Intent i = new Intent(EditProfile.this,MainActivity.class);
                i.putExtra("UserEmail", edit_prof);
                i.putExtra("img1",currentPhotoPath);
                //i.putExtra("username",name);
                startActivity(i);


            }
        });

        p_cncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });


    }

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mInventoryHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mInventoryHasChanged = true;
            return false;
        }
    };

    public void cancel(){
        Intent i = new Intent(EditProfile.this,MainActivity.class);
        i.putExtra("UserEmail", edit_prof);
        i.putExtra("img1",currentPhotoPath);
        //i.putExtra("username",name);
        startActivity(i);
    }

    public void openImageSelector() {
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
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.  Pull that uri using "resultData.getData()"

            if (resultData != null) {
                mUri = resultData.getData();
                Log.i(LOG_TAG, "Uri: " + mUri.toString());

                currentPhotoPath = mUri.toString();

                img.setImageBitmap(getBitmapFromUri(mUri));

            }
        }
    }

    public Bitmap getBitmapFromUri(Uri uri) {

        if (uri == null || uri.toString().isEmpty())
            return null;

        // Get the dimensions of the View
        int targetW = img.getWidth();
        int targetH = img.getHeight();

        InputStream input = null;
        try {
            input = this.getContentResolver().openInputStream(uri);

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            input = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();
            return bitmap;

        } catch (FileNotFoundException fne) {
            Log.e(LOG_TAG, "Failed to load image.", fne);
            return null;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load image.", e);
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException ioe) {

            }
        }
    }

    private void saveProduct() {
        //Read values from text fields
        String nam = p_name.getText().toString().trim();
        String addr = p_add.getText().toString().trim();
        String doob = p_dob.getText().toString().trim();
        int selectedId = radioSexGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        RadioButton ugender = (RadioButton) findViewById(selectedId);

        String t = ugender.getText().toString();

        String phn = p_phone.getText().toString().trim();

        if (TextUtils.isEmpty(nam) && TextUtils.isEmpty(addr)
                && TextUtils.isEmpty(doob) && currentPhotoPath.equals("no images")
                && TextUtils.isEmpty(phn) && TextUtils.isEmpty(t)) {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(nam)) {
            Toast.makeText(this, "Name is Required", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(addr)) {
            Toast.makeText(this, "Address is Required", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(doob)) {
            Toast.makeText(this, "DOB is Required", Toast.LENGTH_LONG).show();
            return;
        }
        if ((currentPhotoPath == "no images") || (TextUtils.isEmpty(currentPhotoPath))) {
            Toast.makeText(this, "Upload a photo", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(t)) {
            Toast.makeText(this, "Gender is required", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(phn)) {
            Toast.makeText(this, "Phone Number is Required", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(inv.invClass.COLUMN_UNAME, nam);
        values.put(inv.invClass.COLUMN_ADDRESS, addr);
        values.put(inv.invClass.COLUMN_UDOB, doob);
        values.put(inv.invClass.COLUMN_GENDER, t);
        values.put(inv.invClass.COLUMN_PHONE, phn);
        values.put(inv.invClass.COLUMN_IMAGE, currentPhotoPath);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selection = inv.invClass.COLUMN_EMAIL + "=?";

        String[] selectionArgs = new String[]{edit_prof};
        int rowsAffected = database.update(inv.invClass.TABLE_NAME, values, selection, selectionArgs);
        if (rowsAffected != 0) {
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error in updating", Toast.LENGTH_SHORT).show();

        }
    }

    }



