package com.example.faza.ui.PageEntrainement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.faza.R;
import com.example.faza.data.entites.Entrainement;

public class EntrainementDetailFragment extends Fragment {

    private static final String ARG_ID = "entrainement_id";
    private long entrainementId;
    private Entrainement entrainement;

    private TextView txtDate;
    private EditText edtDuree, edtCommentaire;
    private Button btnSave;

    public static EntrainementDetailFragment newInstance(long id) {
        EntrainementDetailFragment fragment = new EntrainementDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrainement_detail, container, false);

        txtDate = view.findViewById(R.id.txtDateDetail);
        edtDuree = view.findViewById(R.id.edtDureeDetail);
        edtCommentaire = view.findViewById(R.id.edtCommentaireDetail);
        btnSave = view.findViewById(R.id.btnSaveDetail);

        if (getArguments() != null) {
            entrainementId = getArguments().getLong(ARG_ID);
            entrainement = Entrainement.getById(requireContext(), entrainementId);
        }

        if (entrainement != null) {
            txtDate.setText("Séance du " + entrainement.getDateSeance());
            edtDuree.setText(String.valueOf(entrainement.getDureeMin()));
            edtCommentaire.setText(entrainement.getCommentaire() != null ? entrainement.getCommentaire() : "");
        }

        btnSave.setOnClickListener(v -> saveChanges());

        return view;
    }

    private void saveChanges() {
        if (entrainement == null) return;

        String dureeText = edtDuree.getText().toString();
        entrainement.setDureeMin(TextUtils.isEmpty(dureeText) ? 0 : Integer.parseInt(dureeText));
        entrainement.setCommentaire(edtCommentaire.getText().toString());

        int rows = entrainement.update(requireContext());
        if (rows > 0) {
            Toast.makeText(requireContext(), "Entraînement mis à jour", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigateUp();
        } else {
            Toast.makeText(requireContext(), "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
        }
    }
}
