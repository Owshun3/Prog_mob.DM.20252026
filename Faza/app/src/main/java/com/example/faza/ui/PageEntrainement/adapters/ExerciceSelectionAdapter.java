package com.example.faza.ui.PageEntrainement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Exercice;

import java.util.ArrayList;
import java.util.List;

public class ExerciceSelectionAdapter extends RecyclerView.Adapter<ExerciceSelectionAdapter.ViewHolder> {

    public interface OnExerciceClickListener {
        void onClick(Exercice e);
    }

    private final List<Exercice> items = new ArrayList<>();
    private final OnExerciceClickListener listener;

    public ExerciceSelectionAdapter(List<Exercice> source, OnExerciceClickListener listener) {
        if (source != null) items.addAll(source);
        this.listener = listener;
    }

    public void updateList(List<Exercice> source) {
        items.clear();
        if (source != null) items.addAll(source);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercice_selection, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Exercice e = items.get(position);

        h.txtNom.setText(e.getNom() != null ? e.getNom() : "");

        String gp = e.getGroupePrincipal() != null ? e.getGroupePrincipal() : "";
        String gs = e.getGroupeSecondaire() != null ? e.getGroupeSecondaire() : "";

        if (gp.isEmpty() && gs.isEmpty()) h.txtGroupes.setText("");
        else if (!gp.isEmpty() && !gs.isEmpty()) h.txtGroupes.setText(gp + " / " + gs);
        else if (!gp.isEmpty()) h.txtGroupes.setText(gp);
        else h.txtGroupes.setText(gs);

        String miniature = e.getMiniature();
        int id = 0;
        if (miniature != null && !miniature.isEmpty()) {
            String name = miniature.replace(".jpg", "").replace(".png", "");
            id = h.itemView.getContext().getResources().getIdentifier(
                    name, "drawable", h.itemView.getContext().getPackageName());
        }
        if (id == 0) id = R.drawable.exercice_placeholder;

        h.img.setImageResource(id);

        h.itemView.setOnClickListener(v -> listener.onClick(e));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView img;
        final TextView txtNom;
        final TextView txtGroupes;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgMiniatureExercice);
            txtNom = itemView.findViewById(R.id.txtNomExerciceSelection);
            txtGroupes = itemView.findViewById(R.id.txtGroupesExerciceSelection);
        }
    }

    public List<String> getGroupesPrincipaux() {
        List<String> groupes = new ArrayList<>();
        groupes.add("Tous");
        for (Exercice e : items) {
            String g = e.getGroupePrincipal();
            if (g != null && !g.trim().isEmpty() && !groupes.contains(g)) groupes.add(g);
        }
        return groupes;
    }
}
