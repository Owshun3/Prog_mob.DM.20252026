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

    private final List<Programme> programmes;
    private OnProgrammeClickListener listener;

    public interface OnProgrammeClickListener {
        void onClick(Programme programme);
    }

    public void setOnProgrammeClickListener(OnProgrammeClickListener l) {
        this.listener = l;
    }

    public ProgrammeAdapter(List<Programme> programmes) {
        this.programmes = programmes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_programme, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Programme p = programmes.get(position);

        holder.txtNom.setText(p.getNom());
        holder.txtNbExercices.setText(p.getExercices().size() + " exercices");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(p);
        });
    }

    @Override
    public int getItemCount() {
        return programmes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtNbExercices;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNom = itemView.findViewById(R.id.txtNomProgramme);
            txtNbExercices = itemView.findViewById(R.id.txtNbExercices);
        }
    }
}