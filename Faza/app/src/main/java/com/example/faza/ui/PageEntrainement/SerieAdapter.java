package com.example.faza.ui.PageEntrainement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faza.R;
import com.example.faza.data.entites.Serie;
import java.util.List;

public class SerieAdapter extends RecyclerView.Adapter<SerieAdapter.ViewHolder> {

    private final List<Serie> series;

    public SerieAdapter(List<Serie> series) {
        this.series = series;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_serie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Serie s = series.get(position);
        holder.txtPoids.setText(String.valueOf(s.getPoids()));
        holder.txtReps.setText(String.valueOf(s.getRepetitions()));
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPoids, txtReps;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPoids = itemView.findViewById(R.id.txtPoids);
            txtReps = itemView.findViewById(R.id.txtReps);
        }
    }
}