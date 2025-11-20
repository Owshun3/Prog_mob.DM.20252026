package com.example.faza.ui.PageEntrainement;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.managers.ManagerGlobal;
import com.example.faza.ui.PageEntrainement.adapters.ExerciceAdapter;
import com.example.faza.ui.PageEntrainement.modes.ProgrammeEditorMode;

public class ProgrammeEditorFragment extends Fragment {

    private static final String ARG_PROGRAMME_ID = "programme_id";
    private static final String ARG_MODE = "mode";
    private static final String ARG_ENTRAINEMENT_ID = "entrainement_id";

    private ProgrammeEditorMode mode;
    private Programme programme;
    private Entrainement entrainement;

    private TextView txtNom;
    private EditText editNom;
    private Button btnModifier;
    private Button btnEnregistrer;
    private Button btnSupprimer;
    private Button btnAjouterExercice;
    private RecyclerView recyclerExercices;

    private ExerciceAdapter exerciceAdapter;

    public static ProgrammeEditorFragment newInstanceForLibrary(long programmeId, ProgrammeEditorMode mode) {
        Bundle b = new Bundle();
        b.putLong(ARG_PROGRAMME_ID, programmeId);
        b.putString(ARG_MODE, mode.name());
        ProgrammeEditorFragment f = new ProgrammeEditorFragment();
        f.setArguments(b);
        return f;
    }

    public static ProgrammeEditorFragment newInstanceForTraining(long entrainementId) {
        Bundle b = new Bundle();
        b.putLong(ARG_ENTRAINEMENT_ID, entrainementId);
        b.putString(ARG_MODE, ProgrammeEditorMode.EDIT_TRAINING.name());
        ProgrammeEditorFragment f = new ProgrammeEditorFragment();
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_programme_editor, container, false);

        txtNom = v.findViewById(R.id.txtNomProgramme);
        editNom = v.findViewById(R.id.editNomProgramme);
        btnModifier = v.findViewById(R.id.btnModifier);
        btnEnregistrer = v.findViewById(R.id.btnEnregistrerProgramme);
        btnSupprimer = v.findViewById(R.id.btnSupprimerProgramme);
        btnAjouterExercice = v.findViewById(R.id.btnAjouterExerciceProgramme);
        recyclerExercices = v.findViewById(R.id.recyclerExercicesEditor);

        Bundle args = getArguments();
        mode = ProgrammeEditorMode.valueOf(args.getString(ARG_MODE));

        if (mode == ProgrammeEditorMode.EDIT_TRAINING) {
            long eId = args.getLong(ARG_ENTRAINEMENT_ID);
            entrainement = ManagerGlobal.getInstance().getManagerEntrainement().getById(eId);
            if (entrainement != null) {
                programme = entrainement.getProgramme();
                if (programme == null) {
                    programme = new Programme();
                    entrainement.setProgramme(programme);
                }
            }
        } else {
            long pId = args.getLong(ARG_PROGRAMME_ID);
            programme = ManagerGlobal.getInstance().getManagerProgramme().getProgrammeById(pId);
        }

        if (programme == null) {
            requireActivity().getSupportFragmentManager().popBackStack();
            View fullscreen = requireActivity().findViewById(R.id.containerFragmentFullScreenEntrainement);
            if (fullscreen != null) fullscreen.setVisibility(View.GONE);
            return v;
        }

        recyclerExercices.setLayoutManager(new LinearLayoutManager(getContext()));

        appliquerMode();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (exerciceAdapter != null) {
            exerciceAdapter.notifyDataSetChanged();
        }
    }

    private void appliquerMode() {
        if (mode == ProgrammeEditorMode.READ_ONLY) {
            txtNom.setVisibility(View.VISIBLE);
            editNom.setVisibility(View.GONE);
            btnModifier.setVisibility(View.VISIBLE);
            btnEnregistrer.setVisibility(View.GONE);
            btnSupprimer.setVisibility(View.GONE);
            btnAjouterExercice.setVisibility(View.GONE);

            txtNom.setText(getNomSafe());

            exerciceAdapter = new ExerciceAdapter(
                    programme.getExercices(),
                    false,
                    null
            );
            recyclerExercices.setAdapter(exerciceAdapter);

            btnModifier.setOnClickListener(x -> changerMode(ProgrammeEditorMode.EDIT_LIBRARY));
            return;
        }

        txtNom.setVisibility(View.GONE);
        editNom.setVisibility(View.VISIBLE);
        editNom.setText(getNomSafe());
        editNom.addTextChangedListener(new SimpleTextWatcher(programme::setNom));

        btnModifier.setVisibility(View.GONE);
        btnEnregistrer.setVisibility(View.VISIBLE);
        btnAjouterExercice.setVisibility(View.VISIBLE);

        if (mode == ProgrammeEditorMode.EDIT_LIBRARY) {
            btnSupprimer.setVisibility(View.VISIBLE);
        } else {
            btnSupprimer.setVisibility(View.GONE);
        }

        exerciceAdapter = new ExerciceAdapter(
                programme.getExercices(),
                true,
                (ex, pos) -> {
                    programme.getExercices().remove(pos);
                    exerciceAdapter.notifyItemRemoved(pos);
                }
        );
        recyclerExercices.setAdapter(exerciceAdapter);

        btnAjouterExercice.setOnClickListener(v -> ouvrirSelectionExercice());

        btnEnregistrer.setOnClickListener(v -> fermer());

        if (mode == ProgrammeEditorMode.EDIT_LIBRARY) {
            btnSupprimer.setOnClickListener(v -> {
                ManagerGlobal.getInstance().getManagerProgramme().supprimerProgramme(programme);
                fermer();
            });
        }
    }

    private void ouvrirSelectionExercice() {
        Fragment f;
        if (mode == ProgrammeEditorMode.EDIT_TRAINING && entrainement != null) {
            f = SelectionExerciceFragment.newInstanceForEntrainement(entrainement.getId());
        } else {
            f = SelectionExerciceFragment.newInstanceForProgramme(programme.getId());
        }

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerFragmentFullScreenEntrainement, f)
                .addToBackStack(null)
                .commit();
    }

    private void changerMode(ProgrammeEditorMode newMode) {
        this.mode = newMode;
        appliquerMode();
    }

    private String getNomSafe() {
        return programme != null && programme.getNom() != null ? programme.getNom() : "";
    }

    private void fermer() {
        requireActivity().getSupportFragmentManager().popBackStack();
        View fullscreen = requireActivity().findViewById(R.id.containerFragmentFullScreenEntrainement);
        if (fullscreen != null) fullscreen.setVisibility(View.GONE);
    }
}