package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.faza.R;

public class EntrainementDemarrerFragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrainement_demarrer, container, false);
        Button btnStart = view.findViewById(R.id.btnEntrainementVide);

        btnStart.setOnClickListener(v->{

        });

        return view;
    }
}
