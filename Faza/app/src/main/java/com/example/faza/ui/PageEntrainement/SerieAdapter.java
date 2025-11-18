package com.example.faza.ui.PageEntrainement;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Serie;

import java.util.List;

public class SerieAdapter extends RecyclerView.Adapter<SerieAdapter.ViewHolder> {

    public interface OnSerieDeleteListener {
        void onDelete(Serie s, int position);
    }

    private final List<Serie> series;
    private final boolean editable;
    private final OnSerieDeleteListener deleteListener;

    public SerieAdapter(List<Serie> series, boolean editable, OnSerieDeleteListener deleteListener) {
        this.series = series;
        this.editable = editable;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public SerieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_serie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SerieAdapter.ViewHolder h, int pos) {
        Serie s = series.get(pos);

        h.editPoids.setText(String.valueOf(s.getPoids()));
        h.editReps.setText(String.valueOf(s.getRepetitions()));
        h.editRir.setText(String.valueOf(s.getRir()));

        h.editPoids.setEnabled(editable);
        h.editReps.setEnabled(editable);
        h.editRir.setEnabled(editable);

        h.btnDelete.setVisibility(editable ? View.VISIBLE : View.GONE);

        h.editPoids.addTextChangedListener(new SimpleTextWatcher(text -> {
            try { s.setPoids(Double.parseDouble(text)); } catch (Exception ignored) {}
        }));

        h.editReps.addTextChangedListener(new SimpleTextWatcher(text -> {
            try { s.setRepetitions(Integer.parseInt(text)); } catch (Exception ignored) {}
        }));

        h.editRir.addTextChangedListener(new SimpleTextWatcher(text -> {
            try { s.setRir(Integer.parseInt(text)); } catch (Exception ignored) {}
        }));

        h.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onDelete(s, pos);
        });
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        EditText editPoids, editReps, editRir;
        Button btnDelete;

        ViewHolder(View v) {
            super(v);
            editPoids = v.findViewById(R.id.editPoids);
            editReps = v.findViewById(R.id.editReps);
            editRir = v.findViewById(R.id.editRir);
            btnDelete = v.findViewById(R.id.btnDeleteSerie);
        }
    }
}
