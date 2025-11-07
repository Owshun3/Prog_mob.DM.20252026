package com.example.faza.Onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.faza.R;

public class GenreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre, container, false);

        Button btnHomme = view.findViewById(R.id.btn_homme);
        Button btnFemme = view.findViewById(R.id.btn_femme);

        View.OnClickListener listener = v -> {
            String genre = (v.getId() == R.id.btn_homme) ? "Homme" : "Femme";
            OnboardingData.getInstance().setGenre(genre);

            ((OnboardingActivity) requireActivity()).nextPage();
        };

        btnHomme.setOnClickListener(listener);
        btnFemme.setOnClickListener(listener);

        return view;
    }
}
