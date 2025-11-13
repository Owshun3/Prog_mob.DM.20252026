package com.example.faza.ui.PageEntrainement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import java.util.List;

public class EntrainementAdapter extends RecyclerView.Adapter<EntrainementAdapter.ViewHolder> {

    private final List<Entrainement> entrainements;
    private OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(Entrainement entrainement);}
    public void setOnItemClickListener(OnItemClickListener listener) {this.listener = listener;}
    public EntrainementAdapter(List<Entrainement> entrainements) {this.entrainements = entrainements;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entrainement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entrainement e = entrainements.get(position);

        holder.txtDate.setText(e.getDateSeance() != null ? e.getDateSeance() : "Date inconnue");
        /*
        String infos = "Durée : " + e.getDureeMin() + " min";
        if (e.getProgramme().getNbSeries() > 0) infos += " | Séries : " + e.getNbSeries();
        if (e.getChargeTotale() > 0) infos += " | Charge : " + e.getChargeTotale() + " kg";

        holder.txtInfos.setText(infos);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(e);
        });
        */
    }

    @Override
    public int getItemCount() {
        return entrainements != null ? entrainements.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtInfos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtInfos = itemView.findViewById(R.id.txtInfos);
        }
    }
}
