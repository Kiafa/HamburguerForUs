package com.practica.kiafa.pandroidkjp.vista;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.practica.kiafa.pandroidkjp.persistencia.ExternalFileManager;
import com.practica.kiafa.pandroidkjp.R;
import com.practica.kiafa.pandroidkjp.modelo.Hamburguesa;

public class AnyadirHamburguesaActivity extends AppCompatActivity {
    private final static int PHOTO_CODE = 1;
    private final static int PERMISSION_REQ = 3;
    private final static String BURGUER = "hamburguesa";

    private Hamburguesa hamburguesa;
    private Uri photoUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_hamburguesa);
    }

    /**
     * Método que comprobará si tiene permisos, en caso de tenerlos pasara al chooser intent
     * en caso de no hacerlo, pedirá los permisos
     *
     * @param view control donde se lanza
     */
    public void onClickTakePhoto(View view){
        if(checkPermissionCamera() && checkPermissionSD()){
                takePhoto();
        }else{
            //Si la version de android es Marshmallow o superior pedirá permisos de cámara.
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQ);
            }else{
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQ);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        boolean permission = true;
        switch (requestCode){
            case PERMISSION_REQ:

                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){

                    //Si va bien tomará la foto
                    if(grantResults.length>0 &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                        takePhoto();
                    }
                }else{
                    if(grantResults.length>0 &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        takePhoto();
                    }
                }
        }
    }

    /**
     * Método para comprobar los permisos de la aplicación
     *
     * @return true si tiene todos los permisos
     */
    private boolean checkPermissionSD(){
        boolean pWriteSD = false;
        int pChWrite, pChRead;

        //Comprobará si tiene el permiso de escribir en la SD
        pChWrite = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        pChRead = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);


        if(pChWrite == PackageManager.PERMISSION_GRANTED && pChRead == PackageManager.PERMISSION_GRANTED){
            pWriteSD = true;
        }

        return pWriteSD;
    }

    private boolean checkPermissionCamera(){
        boolean pCamera = false;
        int permissionCh;

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            permissionCh = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);

            if(permissionCh == PackageManager.PERMISSION_GRANTED){
                pCamera = true;
            }
        }else{
            pCamera = true;
        }

        return pCamera;
    }

    /**
     * Método que creará el chooser para seleccionar entre tomar una foto o elegir una de la galería.
     */
    private void takePhoto(){
        String pickTitle = "Seleccionar foto";

        //Intent para seleccionar una img
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");//Apunta a la carpeta image
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);


        //Intent de cámara
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { takePhotoIntent });

        startActivityForResult(chooserIntent, PHOTO_CODE);
    }

    /**
     * Respuesta para cuando se pulsa el boton de crear
     * Se pasara la hamburgesa creada a la actividad desde la que esta es llamada y finalizara.
     *
     * @param view el componente que activa el onClick
     */
    public void onClickOkButton(View view){
        validateBurguer();
        if(hamburguesa != null){
            Intent returnIntent = new Intent();
            returnIntent.putExtra(BURGUER, hamburguesa);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    }

    /**
     *  Respuesta para cuando se pulsa el boton de cancelar, preguntará al usuario si quiere cancelar
     *  la creación del restaurante
     */
    public void onBackPressed(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    /**
     * Valida los campos de la hamburguesa rellenados
     * @return Hamburguesa con los datos comprobados
     */
    private void validateBurguer(){

        TextView tvName = (TextView) findViewById(R.id.et_hamburguer_name);
        TextView tvAddress = (TextView) findViewById(R.id.et_hamburguer_address);
        TextView tvPhone = (TextView) findViewById(R.id.et_hamburguer_tel);
        TextView tvEmail = (TextView) findViewById(R.id.et_hamburguer_mail);

        String name = tvName.getText().toString();
        String address = tvAddress.getText().toString();
        String phone = tvPhone.getText().toString();
        String email = tvEmail.getText().toString();

        if(!name.equals("") && !address.equals("") && phone.length()>7){
            hamburguesa = new Hamburguesa(name, photoUri,phone,address,email);
        }else{
            hamburguesa = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage(R.string.err_burg_name_null)
                    .setTitle(R.string.err_burg_null)
                    .setPositiveButton(R.string.btn_accept,null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }


    }

    /**
     * Método para tratar la selección del usuario y asignar una foto
     *
     * @param requestCode cod requerido.
     * @param resultCode cod de resultado.
     * @param data intent devuelto con la información.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ExternalFileManager efm = new ExternalFileManager();

        /* Tras muchas pruebas, he salido con un apaño, este metodo llamado desde la camara dará
         * null pero llamado desde el chooser de fotos de la galeria saldrá el nombre del archivo
         * asi que aseguramos que si es null será de la cámara*/
        String aux = null;
        if(data != null){
            aux = data.getDataString();
        }

        if(aux == null){

            try{
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                //Si la foto se guarda satisfactoriamente la mostrará en el control
                if(efm.saveImage(bitmap)){
                    ImageView imageView = findViewById(R.id.hamburguer_photo);
                    photoUri = efm.getUriPath();

                    Glide.with(this)
                            .load(photoUri)
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .centerInside())
                            .into(imageView);
                }
            }catch (NullPointerException e){
                Toast.makeText(this, getString(R.string.err_cam_tak), Toast.LENGTH_LONG).show();
            }

        }else{

            ImageView imageView = findViewById(R.id.hamburguer_photo);
            photoUri = data.getData();
            Glide.with(this)
                    .load(photoUri)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .centerInside())
                    .into(imageView);
        }
    }
}
