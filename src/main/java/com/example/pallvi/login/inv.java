package com.example.pallvi.login;

import android.provider.BaseColumns;

public class inv {

    private inv(){}

    public static abstract class invClass implements BaseColumns {

       public static final String TABLE_NAME = "user_table";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_UNAME = "name";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_UDOB = "date_of_birth";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASS = "password";
        public static final String COLUMN_IMAGE = "img";
    }
}
