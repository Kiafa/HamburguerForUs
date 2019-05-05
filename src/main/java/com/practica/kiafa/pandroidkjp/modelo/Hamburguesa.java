package com.practica.kiafa.pandroidkjp.modelo;

/**
 * Created by Kiafa on 30/10/2017.
 */

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


/**
 * Clase hamburguesa que representa un local con sus propiedades
 * nombre, la imagen, numero de telefono, direccion y e-mail.
 */
public class Hamburguesa implements Parcelable {
    private int id;
    private String nombre;
    private Uri uri_img;
    private String num_telf;
    private String direccion;
    private String email;

    /**
     * Constructor publico para crear restaurantes de hamburguesas
     * @param nombre nombre
     * @param path_img imagen
     * @param num_telf telefono
     * @param direccion direccion
     * @param email e-mail
     */
    public Hamburguesa(@NonNull String nombre, Uri path_img, String num_telf, @NonNull String direccion, String email) {
        this.nombre = nombre;
        this.uri_img = path_img;
        this.num_telf = num_telf;
        this.direccion = direccion;
        this.email = email;
    }

    /**
     * Constructor publico para crear restaurantes de hamburguesas desde la BBDD
     * @param nombre nombre
     * @param path_img imagen
     * @param num_telf telefono
     * @param direccion direccion
     * @param email e-mail
     */
    public Hamburguesa(int _id, @NonNull String nombre, Uri path_img, String num_telf, @NonNull String direccion, String email) {
        this.id = _id;
        this.nombre = nombre;
        this.uri_img = path_img;
        this.num_telf = num_telf;
        this.direccion = direccion;
        this.email = email;
    }

    /**
     * Método para guardar la hamburguera en formato parcellable
     * @param in
     */
    protected Hamburguesa(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        uri_img = in.readParcelable(Uri.class.getClassLoader());
        num_telf = in.readString();
        direccion = in.readString();
        email = in.readString();
    }

    /**
     * Métodos de la clase parcelable para poder pasar el objeto entre actividades.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeParcelable(uri_img, flags);
        dest.writeString(num_telf);
        dest.writeString(direccion);
        dest.writeString(email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Hamburguesa> CREATOR = new Creator<Hamburguesa>() {
        @Override
        public Hamburguesa createFromParcel(Parcel in) {
            return new Hamburguesa(in);
        }

        @Override
        public Hamburguesa[] newArray(int size) {
            return new Hamburguesa[size];
        }
    };

    /**
     * Obtener el nombre
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtener el path de la img en una Uri
     * @return uri img
     */
    public Uri getUri_img() {
        return uri_img;
    }

    /**
     * Obtener el telefono
     * @return num_telf
     */
    public String getNum_telf() {
        return num_telf;
    }

    /**
     * Obtener la dirección
     * @return direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Obtener el email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtener el ID
     * @return id
     */
    public int getId() {
        return id;
    }
}
