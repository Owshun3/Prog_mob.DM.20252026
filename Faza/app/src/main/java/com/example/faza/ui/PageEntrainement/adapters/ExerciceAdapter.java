package com.example.faza.ui.PageEntrainement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Serie;

import java.util.List;

public class ExerciceAdapter extends RecyclerView.Adapter<ExerciceAdapter.ViewHolder> {

    public interface OnExerciceDeleteListener {
        void onDelete(Exercice ex, int position);
    }

    private final List<Exercice> exercices;
    private final boolean editable;
    private final boolean trainingMode;
    private final OnExerciceDeleteListener deleteListener;

    public ExerciceAdapter(List<Exercice> exercices, boolean editable, boolean trainingMode, OnExerciceDeleteListener deleteListener) {
        this.exercices = exercices;
        this.editable = editable;
        this.trainingMode = trainingMode;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercice_edit, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Exercice e = exercices.get(pos);

        h.txtNom.setText(e.getNom());
        h.txtResume.setText(e.getResume());

        String miniature = e.getMiniature();
        int id = 0;
        if (miniature != null && !miniature.isEmpty()) {
            String name = miniature.replace(".jpg", "").replace(".png", "");
            id = h.itemView.getContext().getResources().getIdentifier(
                    name, "drawable", h.itemView.getContext().getPackageName());
        }
        h.imgMiniature.setImageResource(id);

        h.recyclerSeries.setLayoutManager(new LinearLayoutManager(h.itemView.getContext()));
        h.recyclerSeries.setAdapter(new SerieAdapter(
                e.getSeries(),
                editable,
                trainingMode,
                (s, position) -> {
                    e.getSeries().remove(position);
                    notifyItemChanged(pos);
                }
        ));

        if (editable && !trainingMode) {
            h.btnAjouterSerie.setVisibility(View.VISIBLE);
            h.btnAjouterSerie.setOnClickListener(v -> {
                e.ajouterSerie(new Serie(0, 0));
                notifyItemChanged(pos);
            });
        } else {
            h.btnAjouterSerie.setVisibility(View.GONE);
        }

        if (editable && deleteListener != null) {
            h.btnDelete.setVisibility(View.VISIBLE);
            h.btnDelete.setOnClickListener(v -> deleteListener.onDelete(e, pos));
        } else {
            h.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return exercices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMiniature;
        TextView txtNom, txtResume;
        ImageView btnDelete;
        Button btnAjouterSerie;
        RecyclerView recyclerSeries;

        ViewHolder(@NonNull View v) {
            super(v);
            imgMiniature = v.findViewById(R.id.imgMiniatureEditor);
            txtNom = v.findViewById(R.id.txtNomExerciceEditor);
            txtResume = v.findViewById(R.id.txtResumeExerciceEditor);
            btnDelete = v.findViewById(R.id.btnDeleteExercice);
            btnAjouterSerie = v.findViewById(R.id.btnAjouterSerie);
            recyclerSeries = v.findViewById(R.id.recyclerSeries);
        }
    }
}
