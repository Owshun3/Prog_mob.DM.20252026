package com.example.faza.ui.PageEntrainement.adapters;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Serie;

import java.util.HashSet;
import java.util.List;

public class ExerciceAdapter extends RecyclerView.Adapter<ExerciceAdapter.ViewHolder> {

    public interface OnExerciceDeleteListener {
        void onDelete(Exercice ex, int position);
    }

    private final List<Exercice> exercices;
    private final boolean editable;
    private final boolean trainingMode;
    private final OnExerciceDeleteListener deleteListener;
    private final HashSet<Integer> expanded = new HashSet<>();

    public ExerciceAdapter(List<Exercice> exercices,
                           boolean editable,
                           boolean trainingMode,
                           OnExerciceDeleteListener deleteListener) {
        this.exercices = exercices;
        this.editable = editable;
        this.trainingMode = trainingMode;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ExerciceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercice_edit, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciceAdapter.ViewHolder h, @SuppressLint("RecyclerView") int pos) {
        int realPos = h.getBindingAdapterPosition();
        if (realPos == RecyclerView.NO_POSITION) return;

        Exercice e = exercices.get(realPos);

        h.txtNom.setText(e.getNom());
        h.txtResume.setText(e.getResume());

        int id = 0;
        if (e.getMiniature() != null && !e.getMiniature().isEmpty()) {
            String name = e.getMiniature().replace(".jpg", "").replace(".png", "");
            id = h.itemView.getContext().getResources().getIdentifier(
                    name, "drawable", h.itemView.getContext().getPackageName());
        }
        if (id == 0) id = R.drawable.exercice_placeholder;
        h.imgMiniature.setImageResource(id);

        if (editable && deleteListener != null) {
            h.btnDelete.setVisibility(View.VISIBLE);
            h.btnDelete.setOnClickListener(v -> {
                int rp = h.getBindingAdapterPosition();
                if (rp != RecyclerView.NO_POSITION) {
                    deleteListener.onDelete(e, rp);
                }
            });
        } else {
            h.btnDelete.setVisibility(View.GONE);
        }

        boolean isExpanded = expanded.contains(realPos);
        h.imgIndicator.setImageResource(
                isExpanded ? R.drawable.ic_arrow_drop_up : R.drawable.ic_arrow_drop_down
        );

        h.recyclerSeries.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        h.btnAjouterSerie.setVisibility(isExpanded && editable ? View.VISIBLE : View.GONE);

        h.itemView.setOnClickListener(v -> {
            int rp = h.getBindingAdapterPosition();
            if (rp == RecyclerView.NO_POSITION) return;

            if (expanded.contains(rp)) expanded.remove(rp);
            else expanded.add(rp);

            notifyItemChanged(rp);
        });

        SerieAdapter serieAdapter = new SerieAdapter(
                e.getSeries(),
                editable,
                trainingMode,
                (s, seriePos) -> {
                    e.getSeries().remove(seriePos);
                    int rp = h.getBindingAdapterPosition();
                    if (rp != RecyclerView.NO_POSITION) notifyItemChanged(rp);
                }
        );

        h.recyclerSeries.setLayoutManager(new LinearLayoutManager(h.itemView.getContext()));
        h.recyclerSeries.setAdapter(serieAdapter);

        h.btnAjouterSerie.setOnClickListener(v -> {
            e.ajouterSerie(new Serie(0, 0));
            int rp = h.getBindingAdapterPosition();
            if (rp != RecyclerView.NO_POSITION) notifyItemChanged(rp);
        });
    }

    @Override
    public int getItemCount() {
        return exercices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMiniature, btnDelete;
        TextView txtNom, txtResume;
        RecyclerView recyclerSeries;
        Button btnAjouterSerie;
        ImageView imgIndicator;
        ViewHolder(@NonNull View v) {
            super(v);
            imgMiniature = v.findViewById(R.id.imgMiniatureEditor);
            txtNom = v.findViewById(R.id.txtNomExerciceEditor);
            txtResume = v.findViewById(R.id.txtResumeExerciceEditor);
            btnDelete = v.findViewById(R.id.btnDeleteExercice);
            recyclerSeries = v.findViewById(R.id.recyclerSeries);
            btnAjouterSerie = v.findViewById(R.id.btnAjouterSerie);
            imgIndicator = v.findViewById(R.id.imgExpandIndicator);
        }
    }
}
