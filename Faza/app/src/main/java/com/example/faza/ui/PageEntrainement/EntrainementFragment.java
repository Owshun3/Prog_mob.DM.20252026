package com.example.faza.ui.PageEntrainement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.faza.ui.Onboarding.OnboardingActivity;
import com.example.faza.ui.Onboarding.OnboardingData;
import com.example.faza.R;

import java.util.Calendar;
import java.util.Date;

public class EntrainementFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrainement, container, false);
        TableLayout tablayout = view.findViewById(R.id.tabLayoutEntrainement);
        ViewPager2 fenetre = view.findViewById(R.id.viewPagerEntrainement);

        return view;
    }
}