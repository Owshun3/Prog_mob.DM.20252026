package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.managers.ManagerGlobal;

public class EntrainementBibliothequeFragment extends Fragment {

    private RecyclerView recycler;
    private Button btnAjouter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entrainement_bibliotheque, container, false);

        recycler = v.findViewById(R.id.recyclerBibliotheque);
        btnAjouter = v.findViewById(R.id.btnAjouterProgramme);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        ProgrammeAdapter adapter = new ProgrammeAdapter(
                ManagerGlobal.getInstance().getManagerProgramme().getProgrammes(),
                ModeAffichage.READ_ONLY,
                null,
                p -> ouvrirProgramme(p.getId(), ModeAffichage.READ_ONLY)
        );

        recycler.setAdapter(adapter);

        btnAjouter.setOnClickListener(x -> {
            Programme p = new Programme();
            ManagerGlobal.getInstance().getManagerProgramme().ajouterProgramme(p);
            ouvrirProgramme(p.getId(), ModeAffichage.EDIT);
        });

        return v;
    }

    private void ouvrirProgramme(long id, ModeAffichage mode) {
        Fragment f = ProgrammeEditorFragment.newInstance(id, mode);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerFragmentFullScreenEntrainement, f)
                .addToBackStack(null)
                .commit();
    }
}
