package com.example.faza.ui.PageEntrainement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Programme;

import java.util.List;

public class ProgrammeAdapter extends RecyclerView.Adapter<ProgrammeAdapter.ViewHolder> {

    public interface OnProgrammeClickListener {
        void onClick(Programme p);
    }

    private final List<Programme> programmes;
    private final OnProgrammeClickListener listener;

    public ProgrammeAdapter(List<Programme> programmes, OnProgrammeClickListener listener) {
        this.programmes = programmes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProgrammeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_programme, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammeAdapter.ViewHolder h, int pos) {

        Programme p = programmes.get(pos);
        h.txtNom.setText(p.getNom());
        int nbExos = p.getExercices() != null ? p.getExercices().size() : 0;
        h.txtInfos.setText(nbExos + " exercices");

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(p);
        });
    }

    @Override
    public int getItemCount() {
        return programmes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtInfos;

        ViewHolder(View v) {
            super(v);
            txtNom = v.findViewById(R.id.txtNomProgramme);
            txtInfos = v.findViewById(R.id.txtInfosProgramme);
        }
    }
}
