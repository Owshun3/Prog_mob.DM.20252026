package com.example.faza.ui.MurSeance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.entites.Serie;
import com.example.faza.data.managers.ManagerGlobal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MurSeanceDetailFragment extends Fragment {

    private static final String ARG_ID = "entrainementId";
    private long entrainementId;

    public static MurSeanceDetailFragment newInstance(long id) {
        MurSeanceDetailFragment f = new MurSeanceDetailFragment();
        Bundle b = new Bundle();
        b.putLong(ARG_ID, id);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            entrainementId = getArguments().getLong(ARG_ID, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_murseance_detail, container, false);

        TextView txtDate = v.findViewById(R.id.txtDetailDate);
        TextView txtDuree = v.findViewById(R.id.txtDetailDuree);
        TextView txtCharge = v.findViewById(R.id.txtDetailChargeTotale);

        Entrainement e = ManagerGlobal.getInstance()
                .getManagerEntrainement()
                .getById(requireContext(),entrainementId);

        if (e == null) return v;

        Programme p = ManagerGlobal.getInstance()
                .getManagerProgramme()
                .chargerProgrammeSessionComplet(requireContext(), e.getProgramme().getId());

        String dateAffiche;
        try {
            long ts = Long.parseLong(e.getDateSeance());
            dateAffiche = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date(ts));
        } catch (Exception ex) {
            dateAffiche = "-";
        }
        txtDate.setText(dateAffiche);

        txtDuree.setText(e.getDureeMin() + " min");

        double chargeTotale = 0;
        for (Exercice exo : p.getExercices()) {
            for (Serie s : exo.getSeries()) {
                if (s.isValidee()) {
                    chargeTotale += s.getPoids() * s.getRepetitions();
                }
            }
        }
        txtCharge.setText((int) chargeTotale + " kg");

        RecyclerView recycler = v.findViewById(R.id.recyclerDetailExercices);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new DetailExerciceAdapter(p.getExercices()));

        return v;
    }
}
