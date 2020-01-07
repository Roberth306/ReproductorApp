package tk.roberthramirez.rpmusica;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.IOException;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{
    private Thread playerT = null;
    private final IBinder binder = new LocalBinder();

    private int[] idMusica;
    //private MusicObject[] musicObjects;

    private boolean isOld = false;
    private MediaPlayer mediaPlayer = null;

    private int puntero = 0;

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopSelf();
    }


    public class LocalBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        /*if(playerT == null || !playerT.isAlive()){
            playerT = new Thread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }*/

        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mediaPlayer.start();
        //mediaPlayer.setOnPreparedListener(this);
        //mediaPlayer.prepareAsync();


        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void inicializarLista(int[] idMusica) {
        //this.musicObjects = musicObjects;
        /*try{
            mediaPlayer.setDataSource();
        }catch (IOException ioe) {
            System.out.println("Errror");
        }*/

        this.idMusica = idMusica;
        mediaPlayer = MediaPlayer.create(this, idMusica[puntero]);

    }
    public void siguienteM() {
        puntero++;
        if(puntero>idMusica.length-1){
            puntero=0;
        }
        //todo poner play musica
        mediaPlayer.reset();
        play(idMusica[puntero]);

    }

    public void anteriorM() {
        puntero--;
        if(puntero<0){
            puntero=idMusica.length-1;
        }
        //todo poner play musica
        mediaPlayer.reset();
        play(idMusica[puntero]);
    }

    public void play(final int idMusic) {
        mediaPlayer =  MediaPlayer.create(this,idMusic);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                puntero++;
                if(puntero>=idMusica.length){
                    puntero=0;
                    mediaPlayer.stop();
                }else{
                    mediaPlayer.reset();
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),idMusica[puntero]);
                }
            }
        });

        mediaPlayer.start();
    }

    public void pause() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }

    }
    public void resume() {
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }

    }

    public boolean isActive() {
        return isOld;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mediaPlayer!=null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
    public void musicaSeleccionada(int position){
        puntero = position;
        mediaPlayer.reset();
        play(idMusica[puntero]);
    }

    public int consultaTiempo() {
        return mediaPlayer.getCurrentPosition();
    }

    public int consultaTotalTiempo() {
        return mediaPlayer.getDuration();
    }

    public void cambioTiempo(int tiempo) {
        mediaPlayer.seekTo(tiempo);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


}
