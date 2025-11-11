package com.example.faza.ui.Onboarding;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.faza.R;
import java.util.Calendar;
import java.util.Date;

public class InfosFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infos, container, false);

        EditText etPseudo = view.findViewById(R.id.et_pseudo);
        EditText etDate = view.findViewById(R.id.et_date_naissance);
        EditText etPoids = view.findViewById(R.id.et_poids);
        EditText etTaille = view.findViewById(R.id.et_taille);
        Button btnSuivant = view.findViewById(R.id.btn_suivant);

        Calendar calendar = Calendar.getInstance();
        etDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    requireContext(),
                    (picker, year, month, day) -> {
                        calendar.set(year, month, day);
                        etDate.setText(day + "/" + (month + 1) + "/" + year);
                        OnboardingData.getInstance().setNaissance(new Date(calendar.getTimeInMillis()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });

        btnSuivant.setOnClickListener(v -> {
            OnboardingData data = OnboardingData.getInstance();
            data.setPseudo(etPseudo.getText().toString());
            data.setPoids(Float.parseFloat(etPoids.getText().toString()));
            data.setTaille(Integer.parseInt(etTaille.getText().toString()));

            ((OnboardingActivity) requireActivity()).nextPage();
        });

        return view;
    }
}
