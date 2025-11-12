package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.faza.ui.Onboarding.*;
import com.example.faza.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EntrainementFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrainement, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        pagerAdapter = new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return 2;
            }


            @Override
            public Fragment createFragment(int position) {
                if (position == 0) return new EntrainementDemarrerFragment();
                else return new EntrainementBibliothequeFragment();
            }
        };
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Démarrer" : "Bibliothèque")).attach();

        return view;
    }

    private void addNewEntrainement() {
        Entrainement e = new Entrainement();
        e.setIdUser(1);
        e.setDateSeance(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
        e.setDureeMin(0);
        e.setNbSeries(0);
        e.setChargeTotale(0);
        e.insert(requireContext());

        Toast.makeText(requireContext(), "Nouvel entraînement ajouté ✅", Toast.LENGTH_SHORT).show();
        loadEntrainements();
    }

    private void loadEntrainements() {
        List<Entrainement> list = Entrainement.getAll(requireContext());
        adapter = new EntrainementAdapter(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(entrainement -> {
            Bundle bundle = new Bundle();
            bundle.putLong("entrainement_id", entrainement.getId());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_dashboard_to_detail, bundle);
        });
    }
}
