package com.example.faza.ui.PageEntrainement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faza.R;
import com.example.faza.data.entites.Exercice;
import java.util.List;

public class ExerciceAdapter extends RecyclerView.Adapter<ExerciceAdapter.ViewHolder> {

    private final List<Exercice> exercices;

    public ExerciceAdapter(List<Exercice> exercices) {
        this.exercices = exercices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercice, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercice e = exercices.get(position);

        holder.txtNom.setText(e.getNom());
        holder.txtGroupes.setText(e.getGroupePrincipal());

        SerieAdapter serieAdapter = new SerieAdapter(e.getSeries());
        holder.recyclerSeries.setAdapter(serieAdapter);
        holder.recyclerSeries.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerSeries.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return exercices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtGroupes;
        RecyclerView recyclerSeries;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNom = itemView.findViewById(R.id.txtNomExercice);
            txtGroupes = itemView.findViewById(R.id.txtGroupes);
            recyclerSeries = itemView.findViewById(R.id.recyclerSeries);
        }
    }
}