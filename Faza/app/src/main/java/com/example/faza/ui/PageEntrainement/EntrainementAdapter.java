package com.example.faza.ui.PageEntrainement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;

import java.util.List;


public class EntrainementAdapter
        extends RecyclerView.Adapter<EntrainementAdapter.ViewHolder>
        implements ModeAffichableAdapter {

    private final List<Entrainement> entrainements;
    private ModeAffichage mode;
    private final OnDeleteListener deleteListener;
    private final OnClickListener clickListener;

    public interface OnDeleteListener {
        void onDelete(Entrainement e, int position);
    }

    public interface OnClickListener {
        void onClick(Entrainement e);
    }

    public EntrainementAdapter(List<Entrainement> entrainements,
                               ModeAffichage mode,
                               OnDeleteListener delListener,
                               OnClickListener clickListener) {
        this.entrainements = entrainements;
        this.mode = mode;
        this.deleteListener = delListener;
        this.clickListener = clickListener;
    }

    @Override public ModeAffichage getMode() { return mode; }
    @Override public void setMode(ModeAffichage mode) { this.mode = mode; notifyDataSetChanged(); }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = (mode == ModeAffichage.READ_ONLY)
                ? R.layout.item_entrainement
                : R.layout.item_entrainement_edit;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Entrainement e = entrainements.get(pos);

        if (mode == ModeAffichage.READ_ONLY) {
            h.txtDate.setText(e.getDateSeance().toString());
            h.txtInfos.setText(e.getProgramme().getNom());
            h.itemView.setOnClickListener(v -> clickListener.onClick(e));
            return;
        }

        h.editProgramme.setText(e.getProgramme().getNom());
        h.editDate.setText(e.getDateSeance().toString());

        h.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null)
                deleteListener.onDelete(e, pos);
        });
    }

    @Override
    public int getItemCount() { return entrainements.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtInfos;

        EditText editProgramme, editDate;
        Button btnDelete;

        ViewHolder(View v) {
            super(v);

            txtDate = v.findViewById(R.id.txtDate);
            txtInfos = v.findViewById(R.id.txtInfos);

            editProgramme = v.findViewById(R.id.editNomProgrammeAssocie);
            editDate = v.findViewById(R.id.editDateEntrainement);
            btnDelete = v.findViewById(R.id.btnSupprimerEntrainement);
        }
    }
}
