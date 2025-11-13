package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;

public class EntrainementDemarrerFragment extends Fragment {
    private Button btnCommencer;
    private RecyclerView recyclerProgrammes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrainement_demarrer, container, false);


        btnCommencer = view.findViewById(R.id.btnCommencer);
        recyclerProgrammes = view.findViewById(R.id.recyclerProgrammes);

        btnCommencer.setOnClickListener(v -> {
            Fragment parent = getParentFragment();
            if (parent instanceof EntrainementFragment) {
                ((EntrainementFragment) parent).afficherEntrainementEnCours();
            }
        });

        recyclerProgrammes.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}