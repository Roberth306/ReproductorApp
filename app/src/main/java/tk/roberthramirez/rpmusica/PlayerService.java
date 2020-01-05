package tk.roberthramirez.rpmusica;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.IOException;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener{
    private Thread playerT = null;
    private final IBinder binder = new LocalBinder();
    private MusicObject[] musicObjects;
    private boolean isOld = false;
    private MediaPlayer mediaPlayer = null;

    private int puntero = 0;




    public class LocalBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try{
            mediaPlayer.setDataSource(getApplicationContext(), musicObjects[puntero].getUbicacion());
        }catch (IOException ioe) {
            System.out.println("Errror");
        }
        mediaPlayer.setOnPreparedListener(this);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(playerT == null || !playerT.isAlive()){
            playerT = new Thread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    public void inicializarLista(MusicObject[] musicObjects) {
        this.musicObjects = musicObjects;
    }

    public void siguienteM() {
        puntero++;
        if(puntero>musicObjects.length-1){
            puntero=0;
        }
        //todo poner play musica
    }

    public void anteriorM() {
        puntero--;
        if(puntero<0){
            puntero=musicObjects.length-1;
        }
        //todo poner play musica
    }

    public void play() {

    }

    public void stop() {

    }

    public boolean isActive() {
        return isOld;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
