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
import com.example.faza.data.entites.Programme;
import java.util.List;
public class ProgrammeAdapter
        extends RecyclerView.Adapter<ProgrammeAdapter.ViewHolder>
        implements ModeAffichableAdapter {

    private final List<Programme> programmes;
    private ModeAffichage mode;
    private final OnDeleteListener deleteListener;
    private final OnClickListener clickListener;

    public interface OnDeleteListener {
        void onDelete(Programme p, int position);
    }

    public interface OnClickListener {
        void onClick(Programme p);
    }

    public ProgrammeAdapter(List<Programme> programmes,
                            ModeAffichage mode,
                            OnDeleteListener delListener,
                            OnClickListener clickListener) {
        this.programmes = programmes;
        this.mode = mode;
        this.deleteListener = delListener;
        this.clickListener = clickListener;
    }

    @Override
    public ModeAffichage getMode() { return mode; }

    @Override
    public void setMode(ModeAffichage mode) {
        this.mode = mode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = (mode == ModeAffichage.EDIT)
                ? R.layout.item_programme_edit
                : R.layout.item_programme;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Programme p = programmes.get(pos);

        if (mode == ModeAffichage.READ_ONLY) {
            h.txtNom.setText(p.getNom());
            h.txtInfos.setText(p.getExercices().size() + " exercices");
            h.itemView.setOnClickListener(v -> clickListener.onClick(p));
            return;
        }

        h.editNom.setText(p.getNom());
        h.editNom.addTextChangedListener(new SimpleTextWatcher(t -> p.setNom(t)));

        h.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null)
                deleteListener.onDelete(p, pos);
        });
    }

    @Override
    public int getItemCount() {
        return programmes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNom, txtInfos;

        EditText editNom;
        Button btnDelete;

        ViewHolder(View v) {
            super(v);
            txtNom = v.findViewById(R.id.txtNomProgramme);
            txtInfos = v.findViewById(R.id.txtNbExercices);

            editNom = v.findViewById(R.id.editNomProgramme);
            btnDelete = v.findViewById(R.id.btnSupprimerProgramme);
        }
    }
}
