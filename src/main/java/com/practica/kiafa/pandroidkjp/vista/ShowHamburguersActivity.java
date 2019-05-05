package com.practica.kiafa.pandroidkjp.vista;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.practica.kiafa.pandroidkjp.R;
import com.practica.kiafa.pandroidkjp.modelo.Hamburguesa;
import com.practica.kiafa.pandroidkjp.persistencia.GestorBBDD;

import java.util.ArrayList;
import java.util.List;

public class ShowHamburguersActivity extends AppCompatActivity implements BurguerFragment.OnFragmentInteractionListener{
    private List<Hamburguesa> alBurguers;
    private GestorBBDD bbdd;
    private int position;
    private final static int CALL_CODE = 5;

    private boolean inRange = false;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_hamburguers);

        Bundle bundle = getIntent().getExtras();

        bbdd = new GestorBBDD(this);
        alBurguers = bbdd.getAlHamburguer();

        position = (int) bundle.get("position");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(position);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor gyrosSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(getGyrosSensorEvent(),gyrosSensor,SensorManager.SENSOR_DELAY_UI);

    }

    /**
     * Clase que crea el sensor para pasar entre vistas con el gyroscopio
     * @return SensorEventListener
     */
    private SensorEventListener getGyrosSensorEvent(){
        SensorEventListener listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(inRange){
                    if(sensorEvent.values[2] > 2.5f){
                        int auxPos = mViewPager.getCurrentItem();
                        if(auxPos>0){
                            auxPos--;
                            mViewPager.setCurrentItem(auxPos);
                        }
                    }else if(sensorEvent.values[2] < -2.5f){
                        int auxPos = mViewPager.getCurrentItem();
                        if(auxPos<alBurguers.size()){
                            auxPos++;
                            mViewPager.setCurrentItem(auxPos);
                        }
                    }
                }
                if(sensorEvent.values[2] < 1.5f && sensorEvent.values[2] > -1.5f){
                    inRange = true;//comprueba que el telefono esté en una posicion fija
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //Do nothing
            }
        };

        return listener;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void callHamburguer(View view){
        int pos = mViewPager.getCurrentItem();

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+alBurguers.get(pos).getNum_telf()));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},CALL_CODE);
        }else {

            try{
                startActivity(callIntent);
            }catch (android.content.ActivityNotFoundException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CALL_CODE:

                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){

                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        callHamburguer(null);
                    }
                }else{
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        callHamburguer(null);
                    }
                }
        }
    }

    public void mapHamburguer(View view) {
        String city = "Madrid";
        int pos = mViewPager.getCurrentItem();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q="+alBurguers.get(pos).getDireccion()+"+"+city));
        startActivity(intent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return BurguerFragment.newInstance(
                    alBurguers.get(position).getNombre(),
                    alBurguers.get(position).getDireccion(),
                    alBurguers.get(position).getNum_telf(),
                    alBurguers.get(position).getEmail(),
                    alBurguers.get(position).getUri_img().toString());
        }

        @Override
        public int getCount() {

            return alBurguers.size();
        }
    }
}
