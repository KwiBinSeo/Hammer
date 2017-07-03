package com.example.vip.hammer;

import android.provider.BaseColumns;

/**
 * Created by vip on 2017-05-22.
 */

public final class DBcontract {


    /* Inner class that defines the table contents */
    public static abstract class DBEntry implements BaseColumns {

        public static final String TABLE_NAME ="Accelerometer";
        public static final String COLUMN_NAME_TITLE = "t_title";
        public static final String COLUMN_NAME_x = "t_x";
        public static final String COLUMN_NAME_y = "t_y";
        public static final String COLUMN_NAME_z = "t_z";
        public static final String COLUMN_NAME_TIME="t_time";
    }

    public static abstract class DBEntry2 implements BaseColumns {

        public static final String TABLE_NAME ="Gyroscope";
        public static final String COLUMN_NAME_TITLE = "t_title";
        public static final String COLUMN_NAME_x = "t_x";
        public static final String COLUMN_NAME_y = "t_y";
        public static final String COLUMN_NAME_z = "t_z";
        public static final String COLUMN_NAME_TIME="t_time";
    }

    public static abstract class DBEntry3 implements BaseColumns {

        public static final String TABLE_NAME ="MIC";
        public static final String COLUMN_NAME_TITLE = "t_title";
        public static final String COLUMN_NAME_MIC = "t_mic";
        public static final String COLUMN_NAME_TIME="t_time";
    }

    public static abstract class DBEntry4 implements BaseColumns {

        public static final String TABLE_NAME ="PhoneAccelerometer";
        public static final String COLUMN_NAME_TITLE = "t_title";
        public static final String COLUMN_NAME_x = "t_x";
        public static final String COLUMN_NAME_y = "t_y";
        public static final String COLUMN_NAME_z = "t_z";
        public static final String COLUMN_NAME_TIME="t_time";
    }
}
