package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.managers.ManagerGlobal;

import java.util.ArrayList;

public class EntrainementEnCoursFragment extends Fragment {

    private static final String ARG_ID = "id";
    private Entrainement entrainement;

    private TextView txtTitre;
    private TextView txtChrono;
    private RecyclerView recycler;
    private Button btnTerminer;

    public static EntrainementEnCoursFragment newInstance(long id) {
        Bundle b = new Bundle();
        b.putLong(ARG_ID, id);
        EntrainementEnCoursFragment f = new EntrainementEnCoursFragment();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle s) {
        super.onCreate(s);
        long id = getArguments().getLong(ARG_ID);
        entrainement = ManagerGlobal.getInstance()
                .getManagerEntrainement()
                .getById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entrainement_en_cours, container, false);

        txtTitre = v.findViewById(R.id.txtTitreProgramme);
        txtChrono = v.findViewById(R.id.txtChrono);
        recycler = v.findViewById(R.id.recyclerExercicesEnCours);
        btnTerminer = v.findViewById(R.id.btnTerminer);

        txtTitre.setText(entrainement.getProgramme() == null
                ? "Entraînement libre"
                : entrainement.getProgramme().getNom());

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        ExerciceAdapter adapter = new ExerciceAdapter(
                entrainement.getProgramme() != null
                        ? entrainement.getProgramme().getExercices()
                        : new ArrayList<>(),
                ModeAffichage.READ_ONLY,
                null
        );

        recycler.setAdapter(adapter);

        btnTerminer.setOnClickListener(x -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return v;
    }
}

