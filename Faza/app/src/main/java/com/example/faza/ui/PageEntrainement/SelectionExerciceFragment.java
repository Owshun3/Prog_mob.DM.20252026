package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.managers.ManagerGlobal;
import com.example.faza.ui.PageEntrainement.adapters.ExerciceSelectionAdapter;
import com.example.faza.ui.PageEntrainement.modes.SelectionExerciceMode;

import java.util.ArrayList;
import java.util.List;

public class SelectionExerciceFragment extends Fragment {

    private static final String ARG_MODE = "mode_selection";
    private static final String ARG_PROGRAMME_ID = "programme_id";
    private static final String ARG_ENTRAINEMENT_ID = "entrainement_id";

    private SelectionExerciceMode mode;
    private long programmeId;
    private long entrainementId;

    private EditText editRecherche;
    private Spinner spinnerGroupe;
    private RecyclerView recycler;
    private ExerciceSelectionAdapter adapter;

    public static SelectionExerciceFragment newInstanceForProgramme(long programmeId) {
        SelectionExerciceFragment f = new SelectionExerciceFragment();
        Bundle b = new Bundle();
        b.putString(ARG_MODE, SelectionExerciceMode.PROGRAMME.name());
        b.putLong(ARG_PROGRAMME_ID, programmeId);
        f.setArguments(b);
        return f;
    }

    public static SelectionExerciceFragment newInstanceForEntrainement(long entrainementId) {
        SelectionExerciceFragment f = new SelectionExerciceFragment();
        Bundle b = new Bundle();
        b.putString(ARG_MODE, SelectionExerciceMode.ENTRAINEMENT.name());
        b.putLong(ARG_ENTRAINEMENT_ID, entrainementId);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selection_exercice, container, false);

        editRecherche = v.findViewById(R.id.editRechercheExercice);
        spinnerGroupe = v.findViewById(R.id.spinnerGroupeMuscle);
        recycler = v.findViewById(R.id.recyclerSelectionExercices);

        Bundle args = getArguments();
        if (args != null) {
            mode = SelectionExerciceMode.valueOf(args.getString(ARG_MODE));
            programmeId = args.getLong(ARG_PROGRAMME_ID, -1);
            entrainementId = args.getLong(ARG_ENTRAINEMENT_ID, -1);
        }

        List<Exercice> catalogue = ManagerGlobal.getInstance().getManagerExercice().getTous();

        adapter = new ExerciceSelectionAdapter(catalogue, this::onExerciceChoisi);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

        List<String> groupes = adapter.getGroupesPrincipaux();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, groupes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroupe.setAdapter(spinnerAdapter);

        editRecherche.addTextChangedListener(new SimpleTextWatcher(text -> {
            List<Exercice> filtres =
                    ManagerGlobal.getInstance().getManagerExercice().rechercher(text);
            adapter.updateList(filtres);
        }));

        spinnerGroupe.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                lancerFiltre();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        return v;
    }

    private void lancerFiltre() {
        String q = editRecherche.getText() != null ? editRecherche.getText().toString() : "";
        String g = spinnerGroupe.getSelectedItem() != null ? spinnerGroupe.getSelectedItem().toString() : "";

        List<Exercice> base = ManagerGlobal.getInstance().getManagerExercice().getTous();
        List<Exercice> res = new ArrayList<>();

        String qLower = q.trim().toLowerCase();
        String gLower = g.trim().toLowerCase();

        for (Exercice e : base) {
            String nom = e.getNom() != null ? e.getNom().toLowerCase() : "";
            String gp = e.getGroupePrincipal() != null ? e.getGroupePrincipal().toLowerCase() : "";

            boolean okNom = qLower.isEmpty() || nom.contains(qLower);
            boolean okGroupe = gLower.isEmpty() || gp.contains(gLower);

            if (okNom && okGroupe) {
                res.add(e);
            }
        }

        adapter.updateList(res);
    }


    private void onExerciceChoisi(Exercice base) {
        Exercice copie = base.copie();
        if (mode == SelectionExerciceMode.PROGRAMME) {
            Programme p = ManagerGlobal.getInstance().getManagerProgramme().getProgrammeById(programmeId);
            if (p != null) {
                p.ajouterExercice(copie);
            }
        } else if (mode == SelectionExerciceMode.ENTRAINEMENT) {
            Entrainement e = ManagerGlobal.getInstance().getManagerEntrainement().getById(entrainementId);
            if (e != null) {
                Programme p = e.getProgramme();
                if (p == null) {
                    p = new Programme();
                    e.setProgramme(p);
                }
                p.ajouterExercice(copie);
            }
        }

        requireActivity().getSupportFragmentManager().popBackStack();
    }
}