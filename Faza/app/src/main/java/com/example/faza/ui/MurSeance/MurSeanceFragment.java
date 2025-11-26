package com.example.faza.ui.MurSeance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.managers.ManagerGlobal;
import java.util.List;

public class MurSeanceFragment extends Fragment {

    private RecyclerView recycler;
    private MurSeanceAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mur_seance, container, false);

        recycler = v.findViewById(R.id.recyclerNotifications);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Entrainement> seances =
                ManagerGlobal.getInstance()
                        .getManagerEntrainement()
                        .getAll(requireContext());

        adapter = new MurSeanceAdapter(seances, this::ouvrirDetail);
        recycler.setAdapter(adapter);

        return v;
    }

    private void ouvrirDetail(Entrainement e) {
        Bundle b = new Bundle();
        b.putLong("entrainementId", e.getId());

        NavHostFragment.findNavController(this)
                .navigate(R.id.navigation_mur_seance_detail, b);
    }
}