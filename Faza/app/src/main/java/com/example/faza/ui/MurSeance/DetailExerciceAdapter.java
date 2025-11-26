package com.example.faza.ui.MurSeance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faza.R;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Serie;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.List;

public class DetailExerciceAdapter extends RecyclerView.Adapter<DetailExerciceAdapter.ViewHolder> {

    private final List<Exercice> exercices;

    public DetailExerciceAdapter(List<Exercice> list) {
        List<Exercice> filtered = new ArrayList<>();
        for (Exercice e : list) {
            boolean keep = false;
            for (Serie s : e.getSeries()) {
                if (s.isValidee()) { keep = true; break; }
            }
            if (keep) filtered.add(e);
        }
        this.exercices = filtered;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_exercice, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Exercice e = exercices.get(pos);
        h.txtNom.setText(e.getNom());

        StringBuilder sb = new StringBuilder();

        for (Serie s : e.getSeries()) {
            if (!s.isValidee()) continue;
            sb.append("• ")
                    .append(s.getPoids()).append(" kg x ")
                    .append(s.getRepetitions()).append(" reps\n");
        }

        h.txtSeries.setText(sb.toString().trim());
    }

    @Override
    public int getItemCount() {
        return exercices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtSeries;
        ViewHolder(View v) {
            super(v);
            txtNom = v.findViewById(R.id.txtDetailNomExercice);
            txtSeries = v.findViewById(R.id.txtDetailSeries);
        }
    }
}
