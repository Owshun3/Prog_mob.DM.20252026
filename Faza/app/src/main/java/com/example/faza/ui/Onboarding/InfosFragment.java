package com.example.faza.ui.Onboarding;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.faza.R;
import java.util.Calendar;
import java.util.Date;

public class InfosFragment extends Fragment {

    private EditText etPseudo, etDate, etPoids, etTaille;
    private Button btnSuivant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_infos, container, false);

        etPseudo = view.findViewById(R.id.et_pseudo);
        etDate = view.findViewById(R.id.et_date_naissance);
        etPoids = view.findViewById(R.id.et_poids);
        etTaille = view.findViewById(R.id.et_taille);
        btnSuivant = view.findViewById(R.id.btn_suivant);

        Calendar calendar = Calendar.getInstance();

        etDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    requireContext(),
                    (picker, year, month, day) -> {
                        calendar.set(year, month, day);
                        Date date = new Date(calendar.getTimeInMillis());

                        etDate.setText(day + "/" + (month + 1) + "/" + year);
                        OnboardingData.getInstance().setNaissance(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });

        btnSuivant.setOnClickListener(v -> validateAndContinue());

        return view;
    }

    private void validateAndContinue() {

        String pseudo = etPseudo.getText().toString().trim();
        String poidsTxt = etPoids.getText().toString().trim();
        String tailleTxt = etTaille.getText().toString().trim();

        if (pseudo.isEmpty()) {
            Toast.makeText(getContext(), "Pseudo obligatoire", Toast.LENGTH_SHORT).show();
            return;
        }

        if (poidsTxt.isEmpty()) {
            Toast.makeText(getContext(), "Poids obligatoire", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tailleTxt.isEmpty()) {
            Toast.makeText(getContext(), "Taille obligatoire", Toast.LENGTH_SHORT).show();
            return;
        }

        if (OnboardingData.getInstance().getNaissance() == null) {
            Toast.makeText(getContext(), "Date de naissance obligatoire", Toast.LENGTH_SHORT).show();
            return;
        }

        float poids;
        int taille;

        try {
            poids = Float.parseFloat(poidsTxt);
            taille = Integer.parseInt(tailleTxt);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Valeurs invalides", Toast.LENGTH_SHORT).show();
            return;
        }

        if (poids <= 0 || taille <= 0) {
            Toast.makeText(getContext(), "Les valeurs doivent être positives", Toast.LENGTH_SHORT).show();
            return;
        }

        OnboardingData data = OnboardingData.getInstance();
        data.setPseudo(pseudo);
        data.setPoids(poids);
        data.setTaille(taille);

        ((OnboardingActivity) requireActivity()).nextPage();
    }
}
