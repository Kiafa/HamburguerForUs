  package com.practica.kiafa.pandroidkjp.vista;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.practica.kiafa.pandroidkjp.R;

  /**
   *Clase para realizar un pequeño mensaje de bienvenida al usuario
   */
  public class LoadingViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_view);

        //Esconde la barra de navegacion
        int uiOption = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(uiOption);

        new LoadingView(this).execute();
    }

    /**
     * Inner class para esperar <5s antes de que lanze otra actividad y mantener la imagen en
     * pantalla
     */
    private class LoadingView extends AsyncTask<Void, Void, Boolean> {
        private Activity activity;

        /**
         * Constructor privado para la Inner class AsyncTask
         * Le pasan la actividad donde está invocada para utilizarla más adelante
         * e invocar la siguiente actividad (MainViewActivity)
         * @param a Activity
         */
        public LoadingView (Activity a){
            activity=a;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean prueba = false;
            try{
                synchronized(this){
                    int counter=0;

                    //Espera 4 segundos hasta que la aplicacion cambie de actividad.
                    while(counter<4){
                        this.wait(850);
                        counter++;
                    }
                }
                prueba = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return prueba;
        }

        @Override
        protected void onPostExecute(Boolean result){
            //change view
            Intent intent = new Intent(activity.getApplicationContext(),MainViewActivity.class);
            activity.startActivity(intent);
            finish();
        }
    }
}
