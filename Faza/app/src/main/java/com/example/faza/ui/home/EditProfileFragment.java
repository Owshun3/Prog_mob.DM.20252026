package com.example.faza.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.faza.R;
import com.example.faza.data.DatabaseHelper;
import com.example.faza.data.entites.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditProfileFragment extends Fragment {

    private EditText edtPseudo, edtTaille, edtPoids;
    private Spinner spinnerUnite, spinnerTheme;
    private ImageView photoPreview;
    private Button btnChangePhoto, btnSave;

    private User user;
    private String newPhotoUri = null;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        photoPreview = view.findViewById(R.id.photoPreview);
        btnChangePhoto = view.findViewById(R.id.btnChangePhoto);

        edtPseudo = view.findViewById(R.id.edtPseudo);
        edtTaille = view.findViewById(R.id.edtTaille);
        edtPoids = view.findViewById(R.id.edtPoids);

        spinnerUnite = view.findViewById(R.id.spinnerUnite);
        spinnerTheme = view.findViewById(R.id.spinnerTheme);

        btnSave = view.findViewById(R.id.btnSaveProfile);

        user = DatabaseHelper.getInstance(requireContext()).getUser();

        edtPseudo.setText(user.getPseudo());
        edtTaille.setText(String.valueOf(user.getTaille()));
        edtPoids.setText(String.valueOf(user.getPoids()));

        ArrayAdapter<String> themeAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"clair", "sombre"}
        );
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(themeAdapter);
        spinnerTheme.setSelection("sombre".equalsIgnoreCase(user.getTheme()) ? 1 : 0);

        ArrayAdapter<String> uniteAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"kg", "lb"}
        );
        uniteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnite.setAdapter(uniteAdapter);
        spinnerUnite.setSelection("lb".equalsIgnoreCase(user.getUnite()) ? 1 : 0);

        if (user.getPhotoUri() != null) {
            File file = new File(user.getPhotoUri());
            if (file.exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                photoPreview.setImageBitmap(bmp);
            } else {
                photoPreview.setImageResource(R.drawable.ic_person);
            }
        }

        initLaunchers();
        btnChangePhoto.setOnClickListener(v -> showPhotoMenu());
        btnSave.setOnClickListener(v -> saveProfile());

        Log.d("TEST_THEME", "Theme depuis base = " + user.getTheme());

        return view;
    }

    private void showPhotoMenu() {
        PopupMenu popup = new PopupMenu(requireContext(), btnChangePhoto);
        popup.getMenu().add("Prendre une photo");
        popup.getMenu().add("Importer depuis la galerie");

        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Prendre une photo")) {
                cameraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            } else {
                galleryLauncher.launch("image/*");
            }
            return true;
        });

        popup.show();
    }

    private void initLaunchers() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");

                        if (bitmap != null) {
                            photoPreview.setImageBitmap(bitmap);
                            saveBitmapToInternal(bitmap);
                        }
                    }
                });

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    requireContext().getContentResolver(),
                                    uri
                            );
                            photoPreview.setImageBitmap(bitmap);
                            saveBitmapToInternal(bitmap);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Erreur import image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveBitmapToInternal(Bitmap bitmap) {
        try {
            File file = new File(requireContext().getFilesDir(),
                    "profile_" + System.currentTimeMillis() + ".jpg");

            try (FileOutputStream fos = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            }

            newPhotoUri = file.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erreur sauvegarde photo", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfile() {
        if (edtPseudo.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Pseudo obligatoire", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edtTaille.getText().toString().trim().isEmpty() ||
                edtPoids.getText().toString().trim().isEmpty()) {

            Toast.makeText(requireContext(), "Taille et poids obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        user.setPseudo(edtPseudo.getText().toString());
        user.setTaille(Integer.parseInt(edtTaille.getText().toString()));
        user.setPoids(Float.parseFloat(edtPoids.getText().toString()));
        user.setUnite(spinnerUnite.getSelectedItem().toString().toLowerCase());

        String theme = spinnerTheme.getSelectedItem().toString().toLowerCase();
        user.setTheme(theme);

        if (user.getTheme().equals("sombre")) {
            Log.d("TEST_THEME2", "Theme depuis base = " + user.getTheme());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            Log.d("TEST_THEME3", "Theme depuis base = " + user.getTheme());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if (newPhotoUri != null) {user.setPhotoUri(newPhotoUri);}

        user.update(requireContext());
        Toast.makeText(requireContext(), "Profil mis à jour", Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}