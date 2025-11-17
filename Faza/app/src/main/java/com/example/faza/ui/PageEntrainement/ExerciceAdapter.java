package com.example.faza.ui.PageEntrainement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faza.R;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Serie;
import java.util.List;

public class ExerciceAdapter
        extends RecyclerView.Adapter<ExerciceAdapter.ViewHolder>
        implements ModeAffichableAdapter {

    private final List<Exercice> exercices;
    private ModeAffichage mode;
    private final OnDeleteListener deleteListener;

    public interface OnDeleteListener {
        void onDelete(Exercice exercice, int position);
    }

    public ExerciceAdapter(List<Exercice> exercices, ModeAffichage mode,
                           OnDeleteListener listener) {
        this.exercices = exercices;
        this.mode = mode;
        this.deleteListener = listener;
    }

    @Override public ModeAffichage getMode() { return mode; }
    @Override public void setMode(ModeAffichage mode) { this.mode = mode; notifyDataSetChanged(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = (mode == ModeAffichage.EDIT)
                ? R.layout.item_exercice_edit
                : R.layout.item_exercice;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Exercice e = exercices.get(pos);

        if (mode == ModeAffichage.READ_ONLY) {
            h.txtNom.setText(e.getNom());
            return;
        }

        h.editNom.setText(e.getNom());
        h.editNom.addTextChangedListener(new SimpleTextWatcher(
                t -> e.setNom(t)
        ));

        SerieAdapter serieAdapter =
                new SerieAdapter(e.getSeries(), ModeAffichage.EDIT,
                        (serie, i) -> {
                            e.getSeries().remove(i);
                            notifyItemChanged(pos);
                        });

        h.recyclerSeries.setAdapter(serieAdapter);
        h.recyclerSeries.setLayoutManager(new LinearLayoutManager(h.itemView.getContext()));

        h.btnAdd.setOnClickListener(v -> {
            e.ajouterSerie(new Serie(0, 0));
            serieAdapter.notifyItemInserted(e.getSeries().size() - 1);
        });

        h.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null)
                deleteListener.onDelete(e, h.getBindingAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return exercices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom;
        EditText editNom;
        Button btnDelete, btnAdd;
        RecyclerView recyclerSeries;

        ViewHolder(View v) {
            super(v);
            txtNom = v.findViewById(R.id.txtNomExercice);

            editNom = v.findViewById(R.id.editNomExercice);
            btnDelete = v.findViewById(R.id.btnSupprimerExercice);
            btnAdd = v.findViewById(R.id.btnAjouterSerie);
            recyclerSeries = v.findViewById(R.id.recyclerEditSeries);
        }
    }
}
