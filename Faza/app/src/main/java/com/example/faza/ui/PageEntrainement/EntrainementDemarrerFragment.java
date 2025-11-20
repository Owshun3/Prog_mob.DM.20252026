package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.managers.ManagerGlobal;
import com.example.faza.ui.PageEntrainement.adapters.ProgrammeAdapter;

public class EntrainementDemarrerFragment extends Fragment {
    private ProgrammeAdapter adapter;
    private RecyclerView recyclerProgrammes;
    private Button btnDemarrerVierge;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entrainement_demarrer, container, false);

        recyclerProgrammes = v.findViewById(R.id.recyclerProgrammesDemarrer);
        btnDemarrerVierge = v.findViewById(R.id.btnDemarrerVierge);

        recyclerProgrammes.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProgrammeAdapter(
                ManagerGlobal.getInstance().getManagerProgramme().getProgrammes(),
                p -> {
                    Entrainement e = ManagerGlobal.getInstance()
                            .getManagerEntrainement()
                            .creerDepuisProgramme(requireContext(),p);
                    ouvrirEntrainementEnCours(e.getId());
                }
        );
        recyclerProgrammes.setAdapter(adapter);


        btnDemarrerVierge.setOnClickListener(x -> {
            Entrainement e = ManagerGlobal.getInstance()
                    .getManagerEntrainement()
                    .creerVierge(requireContext());
            ouvrirEntrainementEnCours(e.getId());
        });

        getParentFragmentManager().addOnBackStackChangedListener(() -> {
            if (adapter != null) adapter.notifyDataSetChanged();
        });


        return v;
    }

    private void ouvrirEntrainementEnCours(long id) {
        View fullscreen = requireActivity().findViewById(R.id.containerFragmentFullScreenEntrainement);
        fullscreen.setVisibility(View.VISIBLE);

        Fragment f = EntrainementEnCoursFragment.newInstance(id);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.containerFragmentFullScreenEntrainement, f)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }


}
