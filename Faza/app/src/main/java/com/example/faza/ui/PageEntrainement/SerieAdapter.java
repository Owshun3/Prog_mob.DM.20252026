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
import com.example.faza.data.entites.Serie;
import java.util.List;
public class SerieAdapter
        extends RecyclerView.Adapter<SerieAdapter.ViewHolder>
        implements ModeAffichableAdapter {

    private final List<Serie> series;
    private ModeAffichage mode;
    private final OnDeleteListener deleteListener;

    public interface OnDeleteListener {
        void onDelete(Serie serie, int position);
    }

    public SerieAdapter(List<Serie> series, ModeAffichage mode,
                        OnDeleteListener listener) {
        this.series = series;
        this.mode = mode;
        this.deleteListener = listener;
    }

    @Override public ModeAffichage getMode() { return mode; }
    @Override public void setMode(ModeAffichage mode) { this.mode = mode; notifyDataSetChanged(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = (mode == ModeAffichage.EDIT)
                ? R.layout.item_serie_edit
                : R.layout.item_serie;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Serie s = series.get(pos);

        if (mode == ModeAffichage.READ_ONLY) {
            h.txtPoids.setText(s.getPoids() + " kg");
            h.txtReps.setText(s.getRepetitions() + " reps");
            return;
        }

        h.editPoids.setText(String.valueOf(s.getPoids()));
        h.editReps.setText(String.valueOf(s.getRepetitions()));

        h.editPoids.addTextChangedListener(new SimpleTextWatcher(
                t -> s.setPoids(t.isEmpty() ? 0 : Double.parseDouble(t))
        ));

        h.editReps.addTextChangedListener(new SimpleTextWatcher(
                t -> s.setRepetitions(t.isEmpty() ? 0 : Integer.parseInt(t))
        ));

        h.btnDel.setOnClickListener(v -> {
            if (deleteListener != null)
                deleteListener.onDelete(s, h.getBindingAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPoids, txtReps;
        EditText editPoids, editReps;
        Button btnDel;

        ViewHolder(View v) {
            super(v);
            txtPoids = v.findViewById(R.id.txtPoids);
            txtReps = v.findViewById(R.id.txtReps);

            editPoids = v.findViewById(R.id.editPoids);
            editReps = v.findViewById(R.id.editReps);
            btnDel = v.findViewById(R.id.btnSupprimerSerie);
        }
    }
}