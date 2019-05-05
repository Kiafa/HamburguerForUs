package com.practica.kiafa.pandroidkjp.adaptador;

import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.practica.kiafa.pandroidkjp.R;
import com.practica.kiafa.pandroidkjp.modelo.Hamburguesa;
import com.practica.kiafa.pandroidkjp.vista.MainViewActivity;

import java.util.List;

/**
 * Created by kiafa on 28/11/2017.
 */

/**
 * Adaptador para el RecyclerView
 */
public class RVHamburguerAdapter extends RecyclerView.Adapter<RVHamburguerAdapter.ViewHolder>{
    private MainViewActivity activity;
    private List<Hamburguesa> hamburguesaList;
    boolean selected = false;
    private final static int TRANSITION_TIME = 350;

    /**
     * Constructor para asignar los valores del recycler
     *
     * @param _activity donde se lanza
     * @param _list lista usada
     */
    public RVHamburguerAdapter (MainViewActivity _activity, List<Hamburguesa> _list){
        activity = _activity;
        hamburguesaList = _list;
    }

    /**
     * Método para cambiar la lista usada.
     * @param _hamburguesaList
     */
    public void setHamburguesaList(List<Hamburguesa> _hamburguesaList){
        this.hamburguesaList = _hamburguesaList;
    }

    /**
     * Método para asignar que hay una posición seleccionada en los items
     * @param bool
     */
    public void setSelected(boolean bool){
        selected = bool;
    }

    @Override
    public RVHamburguerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.burguer_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Clase para asignar los atributos de la lista en la posicion seleccionada
     * @param holder holder
     * @param position posicion
     */
    @Override
    public void onBindViewHolder(final RVHamburguerAdapter.ViewHolder holder, final int position) {
        final Hamburguesa hamburguesa = hamburguesaList.get(position);
        holder.tvTitle.setText(hamburguesa.getNombre());
        holder.tvAddress.setText(hamburguesa.getDireccion());
        holder.tvTelephone.setText(hamburguesa.getNum_telf());

        Glide.with(holder.item)
                .load(hamburguesa.getUri_img())
                .apply(new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .placeholder(R.drawable.logo_kiafadeer))
                .into(holder.ivHamburguer);

        //Si esta posicion está seleccionada revertira su estado en la creacion del item,
        // (Cuando se elimina un dato)
        if(holder.isSelected){
            reverseColorToNormal(holder);
        }

        //Evento on click del listener
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected && activity.selectionStatus() && activity.getPosSelected() == position){
                    selected = false;
                    holder.isSelected = false;
                    activity.selection(selected, -1);
                    reverseColorAnimation(holder);
                    activity.showItemDelete(selected);

                }else if(!activity.selectionStatus()){
                    activity.showHamburguer(position);
                }
            }
        });

        //Evento on long click del listener
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!selected && !activity.selectionStatus()){
                    selected = true;
                    holder.isSelected = true;
                    activity.selection(selected,position);
                    startColorAnimation(holder);
                    activity.showItemDelete(selected);
                }
                return selected;
            }
        });
    }

    private void startColorAnimation(RVHamburguerAdapter.ViewHolder holder){
        TransitionDrawable drawable = (TransitionDrawable) holder.item.getBackground();
        drawable.startTransition(TRANSITION_TIME);
    }

    private void reverseColorAnimation(RVHamburguerAdapter.ViewHolder holder){
        TransitionDrawable drawable = (TransitionDrawable) holder.item.getBackground();
        drawable.reverseTransition(TRANSITION_TIME);
    }

    private void reverseColorToNormal(RVHamburguerAdapter.ViewHolder holder){
        TransitionDrawable drawable = (TransitionDrawable) holder.item.getBackground();
        drawable.reverseTransition(0);
    }

    @Override
    public int getItemCount() {
        return hamburguesaList.size();
    }

    /**
     * Clase interna para cada item del recycler view.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public boolean isSelected = false; //Si el item está en estado seleccionado
        public View item; //La vista del item
        public TextView tvTitle;
        public TextView tvAddress;
        public TextView tvTelephone;
        public ImageView ivHamburguer;
        public LinearLayout backLinearLayout;

        public ViewHolder(View view) {
            super(view);
            item = view;
            tvTitle = view.findViewById(R.id.tv_burguer_item_title);
            tvAddress = view.findViewById(R.id.tv_burguer_item_address);
            tvTelephone = view.findViewById(R.id.tv_burguer_item_telephone);
            ivHamburguer = view.findViewById(R.id.iv_hamburguer);
            backLinearLayout = view.findViewById(R.id.back_linear);
        }
    }
}
