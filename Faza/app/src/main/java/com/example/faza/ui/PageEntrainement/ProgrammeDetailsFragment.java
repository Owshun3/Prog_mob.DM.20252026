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

    private static final String ARG_ID = "id_programme";
    private Programme programme;
    private TextView txtTitre;
    private RecyclerView recyclerExercices;

    public static ProgrammeDetailsFragment newInstance(long id) {
        ProgrammeDetailsFragment f = new ProgrammeDetailsFragment();
        Bundle b = new Bundle();
        b.putLong(ARG_ID, id);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = getArguments().getLong(ARG_ID);
        programme = ManagerGlobal.getInstance().getManagerProgramme().getProgrammeParId(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_programme_details, container, false);

        txtTitre = v.findViewById(R.id.txtTitreProgramme);
        recyclerExercices = v.findViewById(R.id.recyclerExercicesProgramme);

        txtTitre.setText(programme.getNom());
        recyclerExercices.setLayoutManager(new LinearLayoutManager(getContext()));

        ExerciceAdapter adapter = new ExerciceAdapter(
                programme.getExercices(),
                ModeAffichage.READ_ONLY,
                null
        );

        recyclerExercices.setAdapter(adapter);

        return v;
    }
}

