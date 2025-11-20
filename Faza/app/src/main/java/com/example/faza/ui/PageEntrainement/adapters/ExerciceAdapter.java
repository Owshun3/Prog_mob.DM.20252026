package com.example.faza.ui.PageEntrainement.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Serie;
import com.example.faza.ui.PageEntrainement.SimpleTextWatcher;

import java.util.List;

public class ExerciceAdapter extends RecyclerView.Adapter<ExerciceAdapter.ViewHolder> {

    public interface OnExerciceDeleteListener {
        void onDelete(Exercice e, int position);
    }

    private final List<Exercice> exercices;
    private final boolean editable;
    private final OnExerciceDeleteListener deleteListener;

    public ExerciceAdapter(List<Exercice> exercices, boolean editable, OnExerciceDeleteListener deleteListener) {
        this.exercices = exercices;
        this.editable = editable;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercice, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Exercice e = exercices.get(pos);

        h.editNom.setText(e.getNom());
        h.editNom.setEnabled(editable);

        h.editNom.addTextChangedListener(new SimpleTextWatcher(text -> e.setNom(text)));

        h.btnDelete.setVisibility(editable ? View.VISIBLE : View.GONE);
        h.btnAddSerie.setVisibility(editable ? View.VISIBLE : View.GONE);

        SerieAdapter serieAdapter = new SerieAdapter(
                e.getSeries(),
                editable,
                (serie, posSerie) -> {
                    e.getSeries().remove(posSerie);
                    notifyItemChanged(pos);
                }
        );

        h.recyclerSeries.setAdapter(serieAdapter);
        h.recyclerSeries.setLayoutManager(new LinearLayoutManager(h.itemView.getContext()));

        h.btnAddSerie.setOnClickListener(v -> {
            e.ajouterSerie(new Serie(0, 0));
            notifyItemChanged(pos);
        });

        h.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onDelete(e, pos);
        });
    }


    @Override
    public int getItemCount() {
        return exercices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        EditText editNom;
        Button btnDelete, btnAddSerie;
        RecyclerView recyclerSeries;

        ViewHolder(View v) {
            super(v);
            editNom = v.findViewById(R.id.editNomExercice);
            btnDelete = v.findViewById(R.id.btnSupprimerExercice);
            recyclerSeries = v.findViewById(R.id.recyclerSeries);
            btnAddSerie = v.findViewById(R.id.btnAjouterSerie);
        }
    }
}