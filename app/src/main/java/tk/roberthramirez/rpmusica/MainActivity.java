package tk.roberthramirez.rpmusica;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements IMusicaListener, View.OnClickListener{
    private RecyclerView rvMusica;
    private MusicObject[] musicObjects;
    private int[] idMusic;
    private PlayerService mService;

    private SeekBar seekBar;
    private ImageButton atras;
    private ImageButton stopPlay;
    private ImageButton siguiente;
    private boolean isPaused = true;

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    // Runnable update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializos el recycler viwer
        //inicializarDatos();
        inicializarRawResources();
        rvMusica = findViewById(R.id.rvMusica);
        rvMusica.setAdapter(new RAdapter(musicObjects, this));
        rvMusica.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        atras = findViewById(R.id.bAnterior);
        stopPlay = findViewById(R.id.bPlayPause);
        siguiente = findViewById(R.id.bSiguiente);
        seekBar = findViewById(R.id.sbProgress);

        atras.setOnClickListener(this);
        stopPlay.setOnClickListener(this);
        siguiente.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mService.cambioTiempo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, PlayerService.class);
        //todo iniciar servicio para que no acabe
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "a"+seekBar.getMax(), Toast.LENGTH_SHORT).show();
                Log.i("aaaaaaaaaa", "bbbbbbbbbbbbbb");
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);


        //Toast.makeText(this, executor.isShutdown()+" "+executor.isTerminated(), Toast.LENGTH_LONG).show();
    }

    /*public void inicializarDatos() {
        File[] files = null;
        Uri ubiRaw = Uri.parse("android.resource://"+getPackageName()+"/raw");

        File carRaw = new File(ubiRaw.toString());
        files = carRaw.listFiles();
        Log.i("asdf", String.valueOf(carRaw.length()));

        for(int i=0;i<files.length;i++) {
            musicObjects[i] = new MusicObject(files[i].getName(), Uri.parse(files[i].getPath()));
        }

    }*/
    public void inicializarRawResources(){
         String r1 = getResources().getResourceEntryName(R.raw.infected_mushroom);

         musicObjects = new MusicObject[2];
         musicObjects[0] = new MusicObject(getResources().getResourceEntryName(R.raw.infected_mushroom), Uri.parse(getResources().getResourceName(R.raw.infected_mushroom)));

        Field[] fields = R.raw.class.getFields();
        idMusic = new int[fields.length];

        for(int i=0; i<fields.length;i++) {
            int resourceID=0;
            try{
               resourceID =fields[i].getInt(fields[i]);
            }catch (IllegalAccessException ie){

            }
            idMusic[i] = resourceID;
        }

    }


    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            mService = binder.getService();

            //Comprobamos si el servicio ya ha sido creado

            if(mService.isActive()){
                //todo actualizar actividad
            }else {
                mService.inicializarLista(idMusic);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bAnterior:
                mService.anteriorM();
                inicializarSeekBar();
                break;
            case R.id.bPlayPause:
                if(isPaused){
                    stopPlay.setImageResource(R.drawable.ic_pause_black_18dp);
                    mService.resume();
                    isPaused=false;
                }else{
                    stopPlay.setImageResource(R.drawable.ic_play_arrow_black_18dp);
                    mService.pause();
                    isPaused=true;
                }
                break;
            case R.id.bSiguiente:
                mService.siguienteM();
                inicializarSeekBar();
                break;
        }
    }

    @Override
    public void onMusicaSeleccionada(int position) {
        Toast.makeText(this, "S"+position, Toast.LENGTH_LONG).show();
        mService.musicaSeleccionada(position);
        inicializarSeekBar();
    }


    public void inicializarSeekBar() {
        //todo llamar metodo para obtener tiempo total cancion
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(0);
        }
        seekBar.setProgress(0);
        seekBar.setMax(mService.consultaTotalTiempo());
        Toast.makeText(this, "a"+seekBar.getMax(), Toast.LENGTH_LONG).show();


    }
}
