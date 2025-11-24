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
import com.example.faza.data.managers.ManagerGlobal;
import org.jspecify.annotations.NonNull;
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

    private void updateStats() {

        List<Entrainement> list =
                ManagerGlobal.getInstance().getManagerEntrainement().getAll(requireContext());

        int nb = list.size();
        int totalMin = 0;
        int totalKg = 0;

        for (Entrainement e : list) {
            totalMin += e.getDureeMin();
            totalKg  += e.getChargeTotale();
        }

        txtYearNb.setText(nb + "");
        txtYearTime.setText(totalMin + " min");
        txtYearVolume.setText(totalKg + " kg");

        txtAllNb.setText(nb + "");
        txtAllTime.setText(totalMin + " min");
        txtAllVolume.setText(totalKg + " kg");

        txtBench.setText("0 kg");
        txtSquat.setText("0 kg");
        txtDeadlift.setText("0 kg");
    }
}