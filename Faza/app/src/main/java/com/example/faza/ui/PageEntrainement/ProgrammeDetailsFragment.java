package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.managers.ManagerGlobal;

public class ProgrammeDetailsFragment extends Fragment {

    private static final String ARG_PROGRAMME_ID = "programme_id";

    private Programme programme;
    private TextView txtTitreProgramme;
    private RecyclerView recyclerExercicesProgramme;

    public static ProgrammeDetailsFragment newInstance(long programmeId) {
        ProgrammeDetailsFragment fragment = new ProgrammeDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PROGRAMME_ID, programmeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null || !args.containsKey(ARG_PROGRAMME_ID)) {
            throw new IllegalStateException("ProgrammeDetailsFragment : id programme manquant");
        }
        long programmeId = args.getLong(ARG_PROGRAMME_ID);
        programme = ManagerGlobal.getInstance()
                .getManagerProgramme()
                .getProgrammeParId(programmeId);
        if (programme == null) {
            throw new IllegalStateException("ProgrammeDetailsFragment : programme introuvable pour id=" + programmeId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_programme_details, container, false);

        txtTitreProgramme = view.findViewById(R.id.txtTitreProgramme);
        recyclerExercicesProgramme = view.findViewById(R.id.recyclerExercicesProgramme);

        txtTitreProgramme.setText(programme.getNom());

        ExerciceAdapter exerciceAdapter = new ExerciceAdapter(programme.getExercices());
        recyclerExercicesProgramme.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerExercicesProgramme.setAdapter(exerciceAdapter);

        return view;
    }
}
