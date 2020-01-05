package tk.roberthramirez.rpmusica;

import android.net.Uri;



public class MusicObject {
    private String nombre;
    private int mDuracion;
    private Uri ubicacion;

    public MusicObject(String nombre, Uri ubicacion) {
        this.nombre = nombre;
        //this.mDuracion = mDuracion;
        this.ubicacion = ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getmDuracion() {
        return mDuracion;
    }

    public Uri getUbicacion() {
        return ubicacion;
    }
}
