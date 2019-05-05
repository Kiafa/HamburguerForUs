package com.practica.kiafa.pandroidkjp.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kiafa on 03/12/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "BBDD_HAMBURGUER";
    private static final int VERSION = 1;
    public static final String TABLE_HAMBURGUER = "T_BURGUER";
    public static final String[] COLUMN_NAMES_HAMBURGUER = new String[]{"_id","NAME","ADDRESS","TELEPHONE","EMAIL","IMG_PATH"};

    /**
     * Constructor del Helper de la BBDD
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * En caso de no existir la BBDD el método creará la BBDD
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE_BURGUER =
                "CREATE TABLE " +
                        TABLE_HAMBURGUER +
                        "(" +
                        COLUMN_NAMES_HAMBURGUER[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAMES_HAMBURGUER[1] + " TEXT," +
                        COLUMN_NAMES_HAMBURGUER[2] + " TEXT," +
                        COLUMN_NAMES_HAMBURGUER[3] + " TEXT," +
                        COLUMN_NAMES_HAMBURGUER[4] + " TEXT," +
                        COLUMN_NAMES_HAMBURGUER[5] + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE_BURGUER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //No hacer nada hasta cambiar la version
    }
}
