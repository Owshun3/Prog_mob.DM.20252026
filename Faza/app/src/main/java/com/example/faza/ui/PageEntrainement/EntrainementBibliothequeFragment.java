package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;

public class EntrainementBibliothequeFragment extends Fragment {
    private RecyclerView recyclerBibliotheque;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrainement_bibliotheque, container, false);
        recyclerBibliotheque = view.findViewById(R.id.recyclerBibliotheque);
        recyclerBibliotheque.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
}