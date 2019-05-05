package com.practica.kiafa.pandroidkjp.vista;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.practica.kiafa.pandroidkjp.R;
import com.practica.kiafa.pandroidkjp.adaptador.RVHamburguerAdapter;
import com.practica.kiafa.pandroidkjp.modelo.Hamburguesa;
import com.practica.kiafa.pandroidkjp.persistencia.GestorBBDD;

import java.util.List;

/**
 * Clase principal del activity dónde esta el recycler view y enlazará a las funciones principales
 * cómo borrar, añadir y consultar hamburguesas.
 */
public class MainViewActivity extends AppCompatActivity{
    private final static int ADD_HAMBURGUER_CODE = 1;

    private RecyclerView rvHamburguer;
    private RVHamburguerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private GestorBBDD bbdd;

    private Menu topMenu;

    private List<Hamburguesa> hamburguesaList;
    private boolean seleccionado;
    private int posSelected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        bbdd = new GestorBBDD(this);

        hamburguesaList = bbdd.getAlHamburguer();

        rvHamburguer = findViewById(R.id.rvHamburguesas);

        layoutManager = new LinearLayoutManager(this);
        rvHamburguer.setLayoutManager(layoutManager);

        adapter = new RVHamburguerAdapter(this, hamburguesaList);
        rvHamburguer.setAdapter(adapter);

        //Fab button listener
        FloatingActionButton fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHamburguer(view);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        // Inflar el menu
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.mi_delete).setVisible(false);
        menu.findItem(R.id.mi_share).setVisible(false);
        topMenu = menu;
        return true;
    }



    /**
     * Funcion para el onClick del actionbar que añade un local de hamburguesas a la lista
     * @param view boton pulsado
     */
    public void addHamburguer(View view){
        Intent intent = new Intent(this, AnyadirHamburguesaActivity.class);
        startActivityForResult(intent,ADD_HAMBURGUER_CODE);
    }

    public void deleteHamburguer(MenuItem item){

        if(seleccionado){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.conf_del_tit))
                .setMessage(getString(R.string.conf_del_mss)+" "+hamburguesaList.get(posSelected).getNombre())
                .setPositiveButton(getString(R.string.btn_accept), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteHamburg();
                    }

                })
                .setNegativeButton("No", null)
                .show();


        }
    }

    /**
     * Método para compartir una hamburguesa
     * @param item
     */
    public void shareHamburguer(MenuItem item){

        if(seleccionado){
            Hamburguesa hbg = hamburguesaList.get(posSelected);

            String texto = getString(R.string.msg_share_txt1)+" "+hbg.getNombre()+" "+getString(R.string.msg_share_txt2)+" "+hbg.getDireccion()+" "+getString(R.string.msg_share_txt3)+" "+hbg.getNum_telf();

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, hamburguesaList.get(posSelected).getUri_img());
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.msg_share_sbj));
            shareIntent.putExtra(Intent.EXTRA_TEXT,  texto);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.msg_share_title)));
        }
    }

    /**
     * Método para mostrar una hamburguesa
     * @param position posicion seleccionada
     */
    public void showHamburguer(int position){
        Intent intent = new Intent(this,ShowHamburguersActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    /**
     * Metodo para borrar una hamburguesa
     */
    private void deleteHamburg(){
        seleccionado = false;

        bbdd.deleteHamburguer(hamburguesaList.get(posSelected));
        hamburguesaList = bbdd.getAlHamburguer();
        adapter.setHamburguesaList(hamburguesaList);
        adapter.notifyItemRemoved(posSelected);
        adapter.notifyItemRangeChanged(posSelected,hamburguesaList.size());
        adapter.setSelected(seleccionado);
        showItemDelete(seleccionado);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_HAMBURGUER_CODE && resultCode==Activity.RESULT_OK){
            Hamburguesa hamburguesa =(Hamburguesa) data.getExtras().get("hamburguesa");
            bbdd.insertHamburguer(hamburguesa);
            hamburguesaList = bbdd.getAlHamburguer();
            adapter.setHamburguesaList(hamburguesaList);
            adapter.notifyItemInserted(hamburguesaList.size());
        }
    }

    /**
     * Metodo para mostrar la opción de hamburguesa seleccionada
     * @param val
     */
    public void showItemDelete(boolean val){
        topMenu.findItem(R.id.mi_delete).setVisible(val);
        topMenu.findItem(R.id.mi_share).setVisible(val);
    }

    /**
     * Metodo para definir el item seleccionada del recycler
     * @param val estado boolean
     * @param pos posicion en la que está
     */
    public void selection (boolean val, int pos){
        seleccionado = val;
        if(seleccionado){
            posSelected = pos;
        }
    }

    /**
     * Metodo que retorna el estado si hay algun objeto seleccionado
     * @return boolean
     */
    public boolean selectionStatus(){
        return seleccionado;
    }

    /**
     * Posicion seleccionada
     * @return la posicion seleccionada
     */
    public int getPosSelected(){
        return posSelected;
    }
}
