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

    public interface OnDeleteListener {
        void onDelete(Exercice e, int position);
    }

    private final List<Exercice> exercices;
    private final boolean editable;
    private final OnDeleteListener deleteListener;

    public ExerciceAdapter(List<Exercice> exercices, boolean editable, OnDeleteListener listener) {
        this.exercices = exercices;
        this.editable = editable;
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercice_edit, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Exercice e = exercices.get(position);

        h.txtNom.setText(e.getNom());
        h.txtResume.setText(e.getResume());

        String miniature = e.getMiniature();
        int id = 0;
        if (miniature != null && !miniature.isEmpty()) {
            String name = miniature.replace(".jpg", "").replace(".png", "");
            id = h.itemView.getContext().getResources().getIdentifier(
                    name, "drawable", h.itemView.getContext().getPackageName());
        }
        if (id == 0) id = R.drawable.exercice_placeholder;
        h.img.setImageResource(id);

        if (editable) {
            h.btnDelete.setVisibility(View.VISIBLE);
            h.btnAjouterSerie.setVisibility(View.VISIBLE);

            h.btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) deleteListener.onDelete(e, position);
            });

            h.btnAjouterSerie.setOnClickListener(v -> {
                Serie s = new Serie(0, 0);
                e.getSeries().add(s);
                notifyItemChanged(position);
            });

        } else {
            h.btnDelete.setVisibility(View.GONE);
            h.btnAjouterSerie.setVisibility(View.GONE);
        }

        SerieAdapter sa = new SerieAdapter(
                e.getSeries(),
                editable,
                (serie, posSerie) -> {
                    e.getSeries().remove(posSerie);
                    notifyItemChanged(position);
                }
        );

        h.recyclerSeries.setLayoutManager(new LinearLayoutManager(h.itemView.getContext()));
        h.recyclerSeries.setAdapter(sa);
    }

    @Override
    public int getItemCount() {
        return exercices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView txtNom;
        final TextView txtResume;
        final ImageView img;
        final View btnDelete;
        final Button btnAjouterSerie;
        final RecyclerView recyclerSeries;

        ViewHolder(@NonNull View v) {
            super(v);
            txtNom = v.findViewById(R.id.txtNomExerciceEditor);
            txtResume = v.findViewById(R.id.txtResumeExerciceEditor);
            img = v.findViewById(R.id.imgMiniatureEditor);
            btnDelete = v.findViewById(R.id.btnDeleteExercice);
            btnAjouterSerie = v.findViewById(R.id.btnAjouterSerie);
            recyclerSeries = v.findViewById(R.id.recyclerEditSeries);
        }
    }
}
