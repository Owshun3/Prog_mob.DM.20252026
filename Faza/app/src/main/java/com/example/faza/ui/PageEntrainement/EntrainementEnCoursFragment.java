package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.managers.ManagerGlobal;

public class EntrainementEnCoursFragment extends Fragment {

    private static final String ARG_ID = "id_entrainement";

    private Entrainement entrainement;

    private TextView txtTitre;
    private TextView txtChrono;
    private Button btnTerminer;

    private long startTime;
    private boolean chronoRunning = false;

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
        entrainement = ManagerGlobal.getInstance().getManagerEntrainement().getById(id);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entrainement_en_cours, container, false);

        txtTitre = v.findViewById(R.id.txtTitreProgramme);
        txtChrono = v.findViewById(R.id.txtChrono);
        btnTerminer = v.findViewById(R.id.btnTerminer);

        Programme programme = entrainement.getProgramme();
        txtTitre.setText(programme != null ? programme.getNom() : "Entraînement");

        ProgrammeEditorFragment editor = ProgrammeEditorFragment.newInstanceForTraining(entrainement.getId());

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.containerProgrammeEditor, editor)
                .commit();

        startChrono();

        btnTerminer.setOnClickListener(x -> {
            getParentFragmentManager().popBackStack();
            View full = requireActivity().findViewById(R.id.containerFragmentFullScreenEntrainement);
            full.setVisibility(View.GONE);
        });

        return v;
    }

    private void startChrono() {
        chronoRunning = true;
        startTime = SystemClock.elapsedRealtime();

        txtChrono.post(new Runnable() {
            @Override
            public void run() {
                if (!chronoRunning) return;
                long t = SystemClock.elapsedRealtime() - startTime;
                int sec = (int) (t / 1000);
                int min = sec / 60;
                sec = sec % 60;
                txtChrono.setText(String.format("%02d:%02d", min, sec));
                txtChrono.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void onDestroyView() {
        chronoRunning = false;
        super.onDestroyView();
    }
}
