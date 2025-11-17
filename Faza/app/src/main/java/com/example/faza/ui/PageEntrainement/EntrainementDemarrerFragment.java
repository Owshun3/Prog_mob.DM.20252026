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
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.managers.ManagerGlobal;
public class EntrainementDemarrerFragment extends Fragment {

    private RecyclerView recycler;
    private Button btnVierge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entrainement_demarrer, container, false);

        recycler = v.findViewById(R.id.recyclerProgrammesDemarrer);
        btnVierge = v.findViewById(R.id.btnDemarrerVierge);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        ProgrammeAdapter adapter = new ProgrammeAdapter(
                ManagerGlobal.getInstance().getManagerProgramme().getProgrammes(),
                ModeAffichage.READ_ONLY,
                null,
                p -> {
                    Entrainement e = ManagerGlobal.getInstance()
                            .getManagerEntrainement()
                            .creerDepuisProgramme(p);
                    ouvrirEntrainementEnCours(e.getId());
                }
        );

        recycler.setAdapter(adapter);

        btnVierge.setOnClickListener(x -> {
            Entrainement e = ManagerGlobal.getInstance()
                    .getManagerEntrainement()
                    .creerVierge();
            ouvrirEntrainementEnCours(e.getId());
        });

        return v;
    }

    private void ouvrirEntrainementEnCours(long id) {
        Fragment f = EntrainementEnCoursFragment.newInstance(id);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerFragmentFullScreenEntrainement, f)
                .addToBackStack(null)
                .commit();
    }
}
