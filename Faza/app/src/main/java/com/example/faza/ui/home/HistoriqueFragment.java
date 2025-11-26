package com.example.faza.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.managers.ManagerGlobal;
import org.jspecify.annotations.NonNull;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoriqueFragment extends Fragment {

    private RecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_historique, container, false);

        recycler = v.findViewById(R.id.recyclerHistorique);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Entrainement> liste = ManagerGlobal.getInstance()
                .getManagerEntrainement()
                .getAll(requireContext());

        Collections.sort(liste, new Comparator<Entrainement>() {
            @Override
            public int compare(Entrainement e1, Entrainement e2) {
                try {
                    long t1 = Long.parseLong(e1.getDateSeance());
                    long t2 = Long.parseLong(e2.getDateSeance());
                    return Long.compare(t2, t1); // t2 en premier = plus récent en haut
                } catch (Exception ex) {
                    return 0;
                }
            }
        });

        if (liste.isEmpty()) {
            v.findViewById(R.id.txtAucun).setVisibility(View.VISIBLE);
        } else {
            recycler.setAdapter(new HistoriqueAdapter(liste));
        }

        return v;
    }
}
