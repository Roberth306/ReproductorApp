package tk.roberthramirez.rpmusica;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RAdapter extends RecyclerView.Adapter<RAdapter.MusicaViewHolder> {
    private MusicObject[] musicList;
    private IMusicaListener listener;

    public RAdapter(MusicObject[] musicList, IMusicaListener listener) {
        this.musicList = musicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MusicaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list,parent, false);
        return new MusicaViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicaViewHolder holder, int position) {
        MusicObject music = musicList[position];
        holder.bindMusica(music);
    }

    @Override
    public int getItemCount() {
        return musicList.length;
    }

    public class MusicaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvTitulo;
        private IMusicaListener listener;

        public MusicaViewHolder(@NonNull View itemView, IMusicaListener listener) {
            super(itemView);
            this.tvTitulo = itemView.findViewById(R.id.tvTitulo);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public void bindMusica(MusicObject currentMusic){
            tvTitulo.setText(currentMusic.getNombre());
        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                listener.onMusicaSeleccionada(getAdapterPosition());
            }
        }
    }
}
