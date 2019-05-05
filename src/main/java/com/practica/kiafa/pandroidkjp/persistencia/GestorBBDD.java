package com.practica.kiafa.pandroidkjp.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.practica.kiafa.pandroidkjp.modelo.Hamburguesa;

import java.util.ArrayList;

/**
 * Created by Kiafa on 03/12/2017.
 */

public class GestorBBDD {
    DatabaseHelper helper;

    /**
     * Constructor de la bbdd
     * @param context
     */
    public GestorBBDD(Context context){
        helper = new DatabaseHelper(context);
    }

    /**
     * Método para insertar hamburguesas en la bbdd
     * @param hamburguesa a insertar
     */
    public void insertHamburguer(Hamburguesa hamburguesa){
        String path;
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COLUMN_NAMES_HAMBURGUER[1], hamburguesa.getNombre());
        contentValues.put(DatabaseHelper.COLUMN_NAMES_HAMBURGUER[2], hamburguesa.getDireccion());
        contentValues.put(DatabaseHelper.COLUMN_NAMES_HAMBURGUER[3], hamburguesa.getNum_telf());
        contentValues.put(DatabaseHelper.COLUMN_NAMES_HAMBURGUER[4], hamburguesa.getEmail());

        path = uriToPath(hamburguesa.getUri_img());
        contentValues.put(DatabaseHelper.COLUMN_NAMES_HAMBURGUER[5], path);

        sqLiteDatabase.insert(helper.TABLE_HAMBURGUER,null,contentValues);
        sqLiteDatabase.close();
    }

    /**
     * Metodo para eliminar la hamburguesa de la bbdd
     * @param hamburguesa
     */
    public void deleteHamburguer(Hamburguesa hamburguesa){
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.delete(helper.TABLE_HAMBURGUER, "_id = ?", new String[]{String.valueOf(hamburguesa.getId())});
    }

    /**
     * Metodo para recoger la lista de las hamburguesas en la bbdd
     * @return Lista de hamburguesas
     */
    public ArrayList<Hamburguesa> getAlHamburguer(){
        String query = "SELECT * FROM "+helper.TABLE_HAMBURGUER;
        ArrayList<Hamburguesa> burguers = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{});
        while(cursor.moveToNext()){
            burguers.add(new Hamburguesa(
                                cursor.getInt(0), //Id
                                cursor.getString(1), //Nombre
                                Uri.parse(cursor.getString(5)), //Imagen
                                cursor.getString(3), //Numero de telefono
                                cursor.getString(2), //Direccion
                                cursor.getString(4))); //Email
        }

        return burguers;
    }

    private String uriToPath(Uri uri){
        String path = "";
        try{
            path = uri.toString();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        return path;
    }
}
