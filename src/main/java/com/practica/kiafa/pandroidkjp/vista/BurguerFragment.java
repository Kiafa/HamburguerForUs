package com.practica.kiafa.pandroidkjp.vista;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.practica.kiafa.pandroidkjp.R;
import com.practica.kiafa.pandroidkjp.modelo.Hamburguesa;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BurguerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BurguerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BurguerFragment extends Fragment {

    //Parámetros para la inicialización de atributos
    public static final String ARG_BURGUER_NAME = "name";
    public static final String ARG_BURGUER_ADDRESS = "address";
    public static final String ARG_BURGUER_TEL = "telephone";
    public static final String ARG_BURGUER_EMAIL = "email";
    public static final String ARG_BURGUER_IMG = "path";

    //Parámetros del fragmento
    private String bName;
    private String bAddress;
    private String bTel;
    private String bEmail;
    private String bImg;


    private OnFragmentInteractionListener mListener;

    /**
     * Constructor públic vacío
     */
    public BurguerFragment() {
        // Required empty public constructor
    }


    /**
     * Metodo para instanciar un elemento del fragmento
     * @param name nombre
     * @param address direccion
     * @param tel telefono
     * @param email email
     * @param pathImg Path img
     * @return Fragmento hecho
     */
    public static BurguerFragment newInstance(String name, String address, String tel, String email, String pathImg) {
        BurguerFragment fragment = new BurguerFragment();
        Bundle args = new Bundle();

        args.putString(ARG_BURGUER_NAME, name);
        args.putString(ARG_BURGUER_ADDRESS, address);
        args.putString(ARG_BURGUER_TEL, tel);
        args.putString(ARG_BURGUER_EMAIL, email);
        args.putString(ARG_BURGUER_IMG, pathImg);

        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bName = getArguments().getString(ARG_BURGUER_NAME);
            bAddress = getArguments().getString(ARG_BURGUER_ADDRESS);
            bTel = getArguments().getString(ARG_BURGUER_TEL);
            bEmail = getArguments().getString(ARG_BURGUER_EMAIL);
            bImg = getArguments().getString(ARG_BURGUER_IMG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflar el layout de este fragmento y asignar los valores.

        View view = inflater.inflate(R.layout.fragment_burguer, container, false);


        TextView tvName = (TextView)view.findViewById(R.id.frg_burguer_name);
        TextView tvAdress = (TextView)view.findViewById(R.id.frg_burguer_address);
        TextView tvTelephone = (TextView)view.findViewById(R.id.frg_burguer_telephone);
        TextView tvEmail = (TextView)view.findViewById(R.id.frg_burguer_email);
        ImageView ivImage = (ImageView)view.findViewById(R.id.frg_burguer_image);

        tvName.setText(bName);
        tvAdress.setText(bAddress);
        tvEmail.setText(bEmail);
        tvTelephone.setText(bTel);

        Glide.with(view)
                .load(Uri.parse(bImg))
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.logo_kiafadeer)
                        .centerCrop())
                .into(ivImage);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
