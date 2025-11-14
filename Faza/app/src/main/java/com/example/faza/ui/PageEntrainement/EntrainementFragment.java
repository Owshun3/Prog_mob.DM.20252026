package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.faza.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
                    View container = getView().findViewById(R.id.containerFragmentFullScreenEntrainement);
                    container.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
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
        View pager = root.findViewById(R.id.viewPagerEntrainement);
        View tabs = root.findViewById(R.id.tabLayoutEntrainement);

        container.setVisibility(View.VISIBLE);
        pager.setVisibility(View.GONE);
        tabs.setVisibility(View.GONE);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.containerFragmentFullScreenEntrainement, new EntrainementEnCoursFragment())
                .addToBackStack(null)
                .commit();
    }


}
