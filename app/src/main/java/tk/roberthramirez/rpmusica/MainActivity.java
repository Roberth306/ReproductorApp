package tk.roberthramirez.rpmusica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;


public class MainActivity extends AppCompatActivity implements IMusicaListener, View.OnClickListener{
    private RecyclerView rvMusica;
    private MusicObject[] musicObjects;
    private PlayerService mService;

    private ImageButton atras;
    private ImageButton stopPlay;
    private ImageButton siguiente;

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

        atras.setOnClickListener(this);
        stopPlay.setOnClickListener(this);
        siguiente.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void inicializarDatos() {
        File[] files = null;
        Uri ubiRaw = Uri.parse("android.resource://"+getPackageName()+"/raw");

        File carRaw = new File(ubiRaw.toString());
        files = carRaw.listFiles();
        Log.i("asdf", String.valueOf(carRaw.length()));

        for(int i=0;i<files.length;i++) {
            musicObjects[i] = new MusicObject(files[i].getName(), Uri.parse(files[i].getPath()));
        }

    }
    public void inicializarRawResources() {
         String r1 = getResources().getResourceEntryName(R.raw.infected_mushroom);
         String r2 = getResources().getResourceName(R.raw.maid_dragon);

         musicObjects = new MusicObject[2];
         musicObjects[0] = new MusicObject(getResources().getResourceEntryName(R.raw.infected_mushroom), Uri.parse(getResources().getResourceName(R.raw.infected_mushroom)));
         musicObjects[1] = new MusicObject(getResources().getResourceEntryName(R.raw.maid_dragon), Uri.parse(getResources().getResourceName(R.raw.maid_dragon)));
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
                mService.inicializarLista(musicObjects);
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

                break;
            case R.id.bPlayPause:

                break;
            case R.id.bSiguiente:

                break;
        }
    }

    @Override
    public void onMusicaSeleccionada(int position) {
        //todo llamar servicio
    }
}
