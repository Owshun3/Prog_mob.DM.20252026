package com.example.faza.ui.Onboarding;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.faza.ui.MainActivity;
import com.example.faza.databinding.ActivityOnboardingBinding;
import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private OnboardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<androidx.fragment.app.Fragment> fragments = new ArrayList<>();
        fragments.add(new GenreFragment());
        fragments.add(new InfosFragment());
        fragments.add(new PhotoFragment());

        adapter = new OnboardingAdapter(this, fragments);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setUserInputEnabled(false);
    }

    public void nextPage() {
        int nextItem = binding.viewPager.getCurrentItem() + 1;
        if (nextItem < adapter.getItemCount()) {
            binding.viewPager.setCurrentItem(nextItem);
        }
    }

    public void finishOnboarding() {
        startActivity(new android.content.Intent(this, MainActivity.class));
        finish();
    }
}
