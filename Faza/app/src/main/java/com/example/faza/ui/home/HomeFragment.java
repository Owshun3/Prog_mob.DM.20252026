package com.example.faza.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.entites.User;
import com.example.faza.data.managers.ManagerGlobal;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ImageView profileImage;
    private TextView profileName, profileDetails, txtTotalWeek;
    private LineChart chart;

    private Button btnEdit, btnDuree, btnVolume, btnEntrainements;

    private List<Entrainement> entrainements = new ArrayList<>();

    private enum ModeGraph { DUREE, VOLUME, ENTRAINEMENTS }
    private ModeGraph currentMode = ModeGraph.DUREE;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        profileImage = root.findViewById(R.id.profileImage);
        profileName = root.findViewById(R.id.profileName);
        profileDetails = root.findViewById(R.id.profileDetails);
        txtTotalWeek = root.findViewById(R.id.txtTotalWeek);

        chart = root.findViewById(R.id.chartStats);

        btnDuree = root.findViewById(R.id.btnDuree);
        btnVolume = root.findViewById(R.id.btnVolume);
        btnEntrainements = root.findViewById(R.id.btnNbSeances);
        btnEdit = root.findViewById(R.id.ChangementProfile);

        LinearLayout btnHistorique = root.findViewById(R.id.btnOpenHistory);
        LinearLayout btnOpenStats = root.findViewById(R.id.btnOpenStats);

        setupChart();
        refreshUserInfo();
        loadEntrainements();
        updateGraph();
        highlight(btnDuree);

        btnEdit.setOnClickListener(v -> {
            NavController nav = Navigation.findNavController(
                    requireActivity(),
                    R.id.nav_host_fragment_activity_main
            );
            nav.navigate(R.id.navigation_profile_edit);
        });

        btnDuree.setOnClickListener(v -> {
            currentMode = ModeGraph.DUREE;
            updateGraph();
            highlight(btnDuree);
        });

        btnVolume.setOnClickListener(v -> {
            currentMode = ModeGraph.VOLUME;
            updateGraph();
            highlight(btnVolume);
        });

        btnEntrainements.setOnClickListener(v -> {
            currentMode = ModeGraph.ENTRAINEMENTS;
            updateGraph();
            highlight(btnEntrainements);
        });

        btnHistorique.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, new HistoriqueFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnOpenStats.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, new StatistiquesFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return root;
    }

    @SuppressLint("SetTextI18n")
    private void refreshUserInfo() {
        User user = User.get(requireContext());

        if (user != null) {
            profileName.setText(user.getPseudo());
            profileDetails.setText(
                    "Poids : " + user.getPoids() + " " + user.getUnite() +
                            " | Taille : " + user.getTaille() + " cm"
            );

            if (user.getPhotoUri() != null && !user.getPhotoUri().isEmpty()) {
                try {
                    File file = new File(user.getPhotoUri());
                    if (file.exists()) {
                        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                        profileImage.setImageBitmap(bmp);
                    } else profileImage.setImageResource(R.drawable.ic_person);
                } catch (Exception e) {
                    profileImage.setImageResource(R.drawable.ic_person);
                }
            } else profileImage.setImageResource(R.drawable.ic_person);

        } else {
            profileName.setText("Utilisateur");
            profileDetails.setText("");
            profileImage.setImageResource(R.drawable.ic_person);
        }
    }

    private void loadEntrainements() {
        entrainements = ManagerGlobal.getInstance()
                .getManagerEntrainement()
                .getAll(requireContext());

        int total = 0;
        for (Entrainement e : entrainements) {
            total += e.getDureeMin();
        }

        txtTotalWeek.setText(total + "m");
    }

    private void setupChart() {
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    private void updateGraph() {
        switch (currentMode) {
            case DUREE:
                showGraphDuration();
                break;
            case VOLUME:
                showGraphVolume();
                break;
            case ENTRAINEMENTS:
                showGraphCount();
                break;
        }
    }

    private void showGraphDuration() {
        buildGraph(i -> entrainements.get(i).getDureeMin(), "Durée (min)");
    }

    private void showGraphVolume() {
        buildGraph(i -> 0f, "Volume (kg·rép)");
    }

    private void showGraphCount() {
        buildGraph(i -> i + 1, "Séances");
    }

    private interface ValueExtractor { float getValue(int i); }

    private void buildGraph(ValueExtractor extractor, String label) {

        if (entrainements.isEmpty()) {
            chart.clear();
            return;
        }

        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < entrainements.size(); i++) {
            entries.add(new Entry(i, extractor.getValue(i)));
        }

        LineDataSet set = new LineDataSet(entries, label);
        set.setColor(0xFFFFFFFF);
        set.setCircleColor(0xFFFFFFFF);
        set.setLineWidth(2f);
        set.setCircleRadius(3f);
        set.setValueTextColor(0xFFFFFFFF);

        chart.setData(new LineData(set));
        chart.invalidate();
    }

    private void highlight(Button b) {
        btnDuree.setAlpha(0.5f);
        btnVolume.setAlpha(0.5f);
        btnEntrainements.setAlpha(0.5f);

        b.setAlpha(1f);
    }
}
