package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;


public class EntrainementEnCoursFragment extends Fragment {
    private TextView chronometre;
    private RecyclerView recyclerExercices;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrainement_en_cours, container, false);
        chronometre = view.findViewById(R.id.chronoText);
        recyclerExercices = view.findViewById(R.id.recyclerExercices);
        recyclerExercices.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}