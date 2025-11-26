package com.example.faza.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoriqueAdapter extends RecyclerView.Adapter<HistoriqueAdapter.HistoVH> {

    private final List<Entrainement> data;

    public HistoriqueAdapter(List<Entrainement> data) { this.data = data; }

    @NonNull
    @Override
    public HistoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historique, parent, false);
        return new HistoVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoVH h, int pos) {
        Entrainement e = data.get(pos);

        h.txtTitre.setText(
                e.getProgramme() != null ?
                        e.getProgramme().getNom() :
                        "Entraînement"
        );

        String dateAffiche;
        if (e.getDateSeance() == null || e.getDateSeance().isEmpty()) {
            dateAffiche = "Date inconnue";
        } else {
            try {
                long ts = Long.parseLong(e.getDateSeance());
                dateAffiche = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .format(new Date(ts));
            } catch (Exception ex) {
                dateAffiche = e.getDateSeance();
            }
        }
        h.txtDate.setText(dateAffiche);

        h.txtDuree.setText(e.getDureeMin() + " min");
    }

    @Override
    public int getItemCount() { return data.size(); }

    public static class HistoVH extends RecyclerView.ViewHolder {
        TextView txtTitre, txtDate, txtDuree;
        public HistoVH(@NonNull View v) {
            super(v);
            txtTitre = v.findViewById(R.id.itemTitre);
            txtDate = v.findViewById(R.id.itemDate);
            txtDuree = v.findViewById(R.id.itemDuree);
        }
    }
}
