package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faza.R;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.managers.ManagerGlobal;

public class ProgrammeEditorFragment extends Fragment {

    private static final String ARG_ID = "id";
    private static final String ARG_MODE = "mode";
    private Programme programme;
    private ModeAffichage mode;

    private EditText editNom;
    private TextView txtNom;
    private Button btnEdit, btnSave, btnDelete, btnAddEx;
    private RecyclerView recycler;

    public static ProgrammeEditorFragment newInstance(long id, ModeAffichage mode) {
        Bundle b = new Bundle();
        b.putLong(ARG_ID, id);
        b.putString(ARG_MODE, mode.name());
        ProgrammeEditorFragment f = new ProgrammeEditorFragment();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle s) {
        super.onCreate(s);
        long id = getArguments().getLong(ARG_ID);
        programme = ManagerGlobal.getInstance().getManagerProgramme().getProgrammeParId(id);
        mode = ModeAffichage.valueOf(getArguments().getString(ARG_MODE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_programme_editor, container, false);

        editNom = v.findViewById(R.id.editNomProgramme);
        txtNom = v.findViewById(R.id.txtNomProgramme);
        btnEdit = v.findViewById(R.id.btnModifier);
        btnSave = v.findViewById(R.id.btnEnregistrerProgramme);
        btnDelete = v.findViewById(R.id.btnSupprimerProgramme);
        btnAddEx = v.findViewById(R.id.btnAjouterExerciceProgramme);
        recycler = v.findViewById(R.id.recyclerExercicesEditor);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        appliquerMode();

        return v;
    }

    private void appliquerMode() {
        if (mode == ModeAffichage.READ_ONLY) {
            txtNom.setText(programme.getNom());
            editNom.setVisibility(View.GONE);
            txtNom.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            btnAddEx.setVisibility(View.GONE);

            ExerciceAdapter adapter = new ExerciceAdapter(
                    programme.getExercices(),
                    ModeAffichage.READ_ONLY,
                    null
            );
            recycler.setAdapter(adapter);

            btnEdit.setOnClickListener(v -> changerMode(ModeAffichage.EDIT));
            return;
        }

        txtNom.setVisibility(View.GONE);
        editNom.setVisibility(View.VISIBLE);
        editNom.setText(programme.getNom());
        editNom.addTextChangedListener(new SimpleTextWatcher(t -> programme.setNom(t)));

        btnEdit.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
        btnAddEx.setVisibility(View.VISIBLE);

        ExerciceAdapter adapter = new ExerciceAdapter(
                programme.getExercices(),
                ModeAffichage.EDIT,
                (ex, pos) -> {
                    programme.getExercices().remove(pos);
                    recycler.getAdapter().notifyItemRemoved(pos);
                }
        );
        recycler.setAdapter(adapter);

        btnAddEx.setOnClickListener(x -> {
            Exercice e = new Exercice("Nouvel exercice");
            programme.ajouterExercice(e);
            recycler.getAdapter().notifyItemInserted(programme.getExercices().size() - 1);
        });

        btnSave.setOnClickListener(x -> requireActivity().getSupportFragmentManager().popBackStack());

        btnDelete.setOnClickListener(x -> {
            ManagerGlobal.getInstance().getManagerProgramme().supprimerProgramme(programme);
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void changerMode(ModeAffichage m) {
        mode = m;
        appliquerMode();
    }
}
