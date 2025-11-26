package com.example.faza.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Serie;
import com.example.faza.data.managers.ManagerGlobal;
import org.jspecify.annotations.NonNull;
import java.util.Calendar;
import java.util.List;

public class StatistiquesFragment extends Fragment {

    private TextView txtYearNb, txtYearTime, txtYearVolume;
    private TextView txtAllNb, txtAllTime, txtAllVolume;
    private TextView txtBench, txtSquat, txtDeadlift;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_statistiques, container, false);

        txtYearNb     = v.findViewById(R.id.txtNbThisYear);
        txtYearTime   = v.findViewById(R.id.txtDureeThisYear);
        txtYearVolume = v.findViewById(R.id.txtPoidsThisYear);

        txtAllNb     = v.findViewById(R.id.txtNbAll);
        txtAllTime   = v.findViewById(R.id.txtDureeAll);
        txtAllVolume = v.findViewById(R.id.txtPoidsAll);

        txtBench = v.findViewById(R.id.txtBench);
        txtSquat = v.findViewById(R.id.txtSquat);
        txtDeadlift = v.findViewById(R.id.txtDeadlift);

        updateStats();

        return v;
    }

    private int calculerVolumeValide(Entrainement e) {
        if (e.getProgramme() == null) return 0;

        int volume = 0;

        for (Exercice ex : e.getProgramme().getExercices()) {
            for (Serie s : ex.getSeries()) {
                if (s.isValidee()) {
                    volume += s.getPoids() * s.getRepetitions();
                }
            }
        }

        return volume;
    }

    private int extraireAnnee(String date) {
        if (date == null || date.isEmpty()) return -1;

        try {
            long ts = Long.parseLong(date);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(ts);
            return cal.get(Calendar.YEAR);
        } catch (Exception e) {
            return -1;
        }
    }

    private void updateStats() {

        List<Entrainement> list =
                ManagerGlobal.getInstance().getManagerEntrainement().getAll(requireContext());

        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

        int nbThisYear = 0;
        int minThisYear = 0;
        int kgThisYear = 0;

        int nbAll = list.size();
        int minAll = 0;
        int kgAll = 0;

        for (Entrainement e : list) {
            int volumeValide = calculerVolumeValide(e);
            minAll += e.getDureeMin();
            kgAll += volumeValide;
            int yearSeance = extraireAnnee(e.getDateSeance());
            if (yearSeance == currentYear) {
                nbThisYear++;
                minThisYear += e.getDureeMin();
                kgThisYear += volumeValide;
            }
        }

        txtYearNb.setText(String.valueOf(nbThisYear));
        txtYearTime.setText(minThisYear + " min");
        txtYearVolume.setText(kgThisYear + " kg");

        txtAllNb.setText(String.valueOf(nbAll));
        txtAllTime.setText(minAll + " min");
        txtAllVolume.setText(kgAll + " kg");

        txtBench.setText("0 kg");
        txtSquat.setText("0 kg");
        txtDeadlift.setText("0 kg");
    }
}