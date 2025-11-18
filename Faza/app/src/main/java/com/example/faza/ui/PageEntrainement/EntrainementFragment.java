package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.faza.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class EntrainementFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entrainement, container, false);

        tabLayout = v.findViewById(R.id.tabLayoutEntrainement);
        viewPager = v.findViewById(R.id.viewPagerEntrainement);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override public int getItemCount() { return 2; }
            @Override public Fragment createFragment(int pos) {
                return pos == 0 ? new EntrainementDemarrerFragment() : new EntrainementBibliothequeFragment();
            }
        });

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, pos) -> tab.setText(pos == 0 ? "Démarrer" : "Bibliothèque")).attach();

        return v;
    }
}
