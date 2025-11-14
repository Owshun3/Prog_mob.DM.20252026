package com.example.faza.ui.PageEntrainement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EntrainementAdapter extends RecyclerView.Adapter<EntrainementAdapter.ViewHolder> {

    private final List<Entrainement> entrainements;
    private OnEntrainementClickListener listener;

    public interface OnEntrainementClickListener { void onClick(Entrainement e);}
    public void setOnEntrainementClickListener(OnEntrainementClickListener l) {
        this.listener = l;
    }
    public EntrainementAdapter(List<Entrainement> entrainements) {this.entrainements = entrainements;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entrainement, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entrainement e = entrainements.get(position);

        holder.txtDate.setText(e.getDateSeance());

        holder.txtInfos.setText(e.getDureeMin() + " min");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(e);
        });
    }

    @Override
    public int getItemCount() {
        return entrainements.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtInfos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtInfos = itemView.findViewById(R.id.txtInfos);
        }
    }
}