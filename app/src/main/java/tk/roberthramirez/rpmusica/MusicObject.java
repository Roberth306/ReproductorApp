package tk.roberthramirez.rpmusica;

import android.net.Uri;



public class MusicObject {
    private String nombre;
    private int mDuracion;
    private Uri ubicacion;

    public MusicObject(String nombre) {
        this.nombre = nombre;
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
