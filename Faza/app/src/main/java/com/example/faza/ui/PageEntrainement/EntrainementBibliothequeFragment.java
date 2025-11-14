package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.managers.ManagerGlobal;

import java.util.List;

public class EntrainementBibliothequeFragment extends Fragment {

    private RecyclerView recyclerBibliotheque;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entrainement_bibliotheque, container, false);

        recyclerBibliotheque = view.findViewById(R.id.recyclerBibliotheque);
        recyclerBibliotheque.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Programme> programmes = ManagerGlobal.getInstance()
                .getManagerProgramme()
                .getProgrammes();

        ProgrammeAdapter adapter = new ProgrammeAdapter(programmes);
        recyclerBibliotheque.setAdapter(adapter);

        adapter.setOnProgrammeClickListener(p -> {
            ProgrammeDetailsFragment details = ProgrammeDetailsFragment.newInstance(p.getId());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerFragmentFullScreenEntrainement, details)
                    .addToBackStack(null)
                    .commit();
        });


        return view;
    }
}