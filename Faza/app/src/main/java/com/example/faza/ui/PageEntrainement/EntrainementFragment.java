package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EntrainementFragment extends Fragment {

    private RecyclerView recyclerView;
    private EntrainementAdapter adapter;
    private List<Entrainement> entrainements;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrainement, container, false);

        Button btnNew = view.findViewById(R.id.btnNewEntrainement);
        recyclerView = view.findViewById(R.id.recyclerEntrainements);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadEntrainements();

        btnNew.setOnClickListener(v -> addNewEntrainement());

        return view;
    }

    private void addNewEntrainement() {
        Entrainement e = new Entrainement();
        e.setIdUser(1);
        e.setDateSeance(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
        e.setDureeMin(0);
        e.setNbSeries(0);
        e.setChargeTotale(0);
        e.insert(requireContext());

        Toast.makeText(requireContext(), "Nouvel entraînement ajouté ✅", Toast.LENGTH_SHORT).show();
        loadEntrainements();
    }

    private void loadEntrainements() {
        List<Entrainement> list = Entrainement.getAll(requireContext());
        adapter = new EntrainementAdapter(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(entrainement -> {
            Bundle bundle = new Bundle();
            bundle.putLong("entrainement_id", entrainement.getId());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_dashboard_to_detail, bundle);
        });
    }
}
