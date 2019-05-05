package com.practica.kiafa.pandroidkjp.persistencia;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase para controlar las fotos en la memoria externa de la aplicación.
 * Created by Kiafa on 24/11/2017.
 */

public class ExternalFileManager {
    private String path;
    private final static String APPNAME = "Hamburguers for us";
    private final static String SHORT = "HFU_";
    private final static String EXT = ".png";
    private boolean succes;
    private File savedPath;

    /**
     * Constructor vacío, no requiere ninguna funcionalidad al iniciarse
     */
    public ExternalFileManager(){

    }

    /**
     * Método para guardar una imagen con un nombre de fichero común unido a un TIMESTAMP dentro de
     * la carpeta asignada para imagenes del sistema
     * @param bitmap bitmap a guardar
     * @return Boolean del estado, true es guardado y false que hubo problemas al guardar.
     */
    public boolean saveImage(Bitmap bitmap){
        boolean pathCre = true;
        String pathname = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/"+APPNAME;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        succes = false;

        File storageDir = new File(pathname);

        if(!storageDir.exists()){//Si no existe lo crea
            pathCre = storageDir.mkdir();
        }

        if(pathCre){//en caso de estar creado el directorio creará la imagen

            File imageFile = new File(storageDir, SHORT+timeStamp+EXT);
            try {
                OutputStream fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG,100, fos);
                fos.close();
                savedPath = imageFile;
                succes = true;
            } catch (FileNotFoundException e) {
                Log.d("EFI","ERROR AL CREAR EL FICHERO");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return succes;
    }

    /**
     * Método para recuperar la dirección del fichero en un URI
     * @return Uri path del fichero.
     */
    public Uri getUriPath(){
        Uri uri = null;
        if (succes){
            uri = Uri.fromFile(savedPath);
        }
        return uri;
    }
}
