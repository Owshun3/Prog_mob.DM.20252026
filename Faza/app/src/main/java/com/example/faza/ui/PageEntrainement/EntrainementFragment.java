package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.faza.data.entites.Entrainement;
import com.example.faza.ui.Onboarding.*;
import com.example.faza.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
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
        tabLayout = view.findViewById(R.id.tabLayoutEntrainement);
        viewPager = view.findViewById(R.id.viewPagerEntrainement);

        pagerAdapter = new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return 2;
            }
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return new EntrainementDemarrerFragment();
                }
                else{
                    return new EntrainementBibliothequeFragment();
                }
            }
        };
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Démarrer" : "Bibliothèque")).attach();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getChildFragmentManager().getBackStackEntryCount() > 0) {
                    getChildFragmentManager().popBackStack();
                    View root = getView();
                    if (root != null) {
                        root.findViewById(R.id.containerFragmentFullScreenEntrainement).setVisibility(View.GONE);
                        root.findViewById(R.id.viewPagerEntrainement).setVisibility(View.VISIBLE);
                        root.findViewById(R.id.tabLayoutEntrainement).setVisibility(View.VISIBLE);
                    }
                } else {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
        return view;
    }
    public void afficherEntrainementEnCours() {
        View root = getView();
        if (root == null) return;

        View container = root.findViewById(R.id.containerFragmentFullScreenEntrainement);
        View viewPager = root.findViewById(R.id.viewPagerEntrainement);
        View tabLayout = root.findViewById(R.id.tabLayoutEntrainement);

        container.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.containerFragmentFullScreenEntrainement, new EntrainementEnCoursFragment())
                .addToBackStack(null)
                .commit();
    }


}
