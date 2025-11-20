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
import com.example.faza.data.entites.Programme;
import com.example.faza.data.managers.ManagerGlobal;

public class EntrainementBibliothequeFragment extends Fragment {
    private ProgrammeAdapter adapter;

    private RecyclerView recycler;
    private Button btnAjouter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entrainement_bibliotheque, container, false);

        recycler = v.findViewById(R.id.recyclerBibliotheque);
        btnAjouter = v.findViewById(R.id.btnAjouterProgramme);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProgrammeAdapter(
                ManagerGlobal.getInstance().getManagerProgramme().getProgrammes(),
                p -> ouvrirProgramme(p.getId(), ProgrammeEditorMode.READ_ONLY)
        );
        recycler.setAdapter(adapter);

        btnAjouter.setOnClickListener(x -> {
            Programme p = ManagerGlobal.getInstance()
                    .getManagerProgramme()
                    .creerProgramme(requireContext(), "Nouveau programme");
            ouvrirProgramme(p.getId(), ProgrammeEditorMode.EDIT_LIBRARY);
        });

        getParentFragmentManager().addOnBackStackChangedListener(() -> {
            if (adapter != null) adapter.notifyDataSetChanged();
        });

        return v;
    }

    private void ouvrirProgramme(long id, ProgrammeEditorMode mode) {
        View fullscreen = requireActivity().findViewById(R.id.containerFragmentFullScreenEntrainement);
        fullscreen.setVisibility(View.VISIBLE);

        Fragment f = ProgrammeEditorFragment.newInstanceForLibrary(id, mode);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.containerFragmentFullScreenEntrainement, f)
                .addToBackStack("programme_editor")
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

}
