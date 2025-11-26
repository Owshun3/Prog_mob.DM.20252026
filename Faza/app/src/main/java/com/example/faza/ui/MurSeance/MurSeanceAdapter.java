package com.example.faza.ui.MurSeance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import org.jspecify.annotations.NonNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MurSeanceAdapter extends RecyclerView.Adapter<MurSeanceAdapter.ViewHolder> {

    public interface OnClickListener { void onClick(Entrainement e); }

    private final List<Entrainement> items;
    private final OnClickListener listener;

    public MurSeanceAdapter(List<Entrainement> items, OnClickListener l) {
        this.items = items;
        this.listener = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mur_seance, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Entrainement e = items.get(pos);

        h.txtNom.setText(e.getProgramme().getNom());
        h.txtDuree.setText(e.getDureeMin() + " min");

        String rawDate = e.getDateSeance();
        long timestamp = -1;

        if (rawDate != null && !rawDate.isEmpty()) {
            try {
                timestamp = Long.parseLong(rawDate);
            } catch (Exception ignore) {}
        }

        String dateAffichee;

        if (timestamp > 0) {
            dateAffichee = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date(timestamp));
        } else {
            dateAffichee = "—";
        }

        h.txtDate.setText(dateAffichee);

        h.itemView.setOnClickListener(v -> listener.onClick(e));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtDate, txtDuree;
        ViewHolder(View v) {
            super(v);
            txtNom = v.findViewById(R.id.txtNotificationNom);
            txtDate = v.findViewById(R.id.txtNotificationDate);
            txtDuree = v.findViewById(R.id.txtNotificationDuree);
        }
    }
}
