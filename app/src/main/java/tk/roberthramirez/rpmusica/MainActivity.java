package tk.roberthramirez.rpmusica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements IMusicaListener, View.OnClickListener{
    private RecyclerView rvMusica;
    private ArrayList<MusicObject> musicObjects = new ArrayList<MusicObject>();
    private int[] idMusic;
    private PlayerService mService;

    private SeekBar seekBar;
    private ImageButton atras;
    private ImageButton stopPlay;
    private ImageButton siguiente;
    private boolean isPaused = true;

    private ScheduledExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializos el recycler viwer

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

        /*executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "a"+seekBar.getMax(), Toast.LENGTH_SHORT).show();
                Log.i("executor", "bbbbbbbbbbbbbb");
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);*/


    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, PlayerService.class);
        //todo iniciar servicio para que no acabe
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void inicializarRawResources(){


        Field[] fields = R.raw.class.getFields();
        idMusic = new int[fields.length];

        for(int i=0; i<fields.length;i++) {

            int resourceID=0;
            try{
               resourceID =fields[i].getInt(fields[i]);
            }catch (IllegalAccessException ie){

            }
            idMusic[i] = resourceID;
            musicObjects.add(new MusicObject(getResources().getResourceEntryName(resourceID)));
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
                if(mService.isPlaying()){
                    stopPlay.setImageResource(R.drawable.ic_pause_black_18dp);
                }else{
                    stopPlay.setImageResource(R.drawable.ic_play_arrow_black_18dp);
                }
                inicializarSeekBar();
                seekBar.setProgress(mService.consultaTiempo());
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
                executor.shutdown();
                break;
        }
    }

    @Override
    public void onMusicaSeleccionada(int position) {
        //Toast.makeText(this, "S"+position, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
    
}
