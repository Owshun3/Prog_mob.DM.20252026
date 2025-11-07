package com.example.faza.Onboarding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.faza.R;
import com.example.faza.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoFragment extends Fragment {

    private ImageView imgPhoto;
    private String photoUri;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        if (imgPhoto != null) imgPhoto.setImageBitmap(photo);

                        try {
                            File file = new File(requireContext().getFilesDir(), "photo_" + System.currentTimeMillis() + ".jpg");
                            try (FileOutputStream fos = new FileOutputStream(file)) {
                                photo.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                            }
                            photoUri = file.getAbsolutePath();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Erreur lors de la sauvegarde de la photo", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        imgPhoto = view.findViewById(R.id.img_photo);
        Button btnPhoto = view.findViewById(R.id.btn_photo);
        Button btnTerminer = view.findViewById(R.id.btn_terminer);

        btnPhoto.setOnClickListener(v -> openCamera());
        btnTerminer.setOnClickListener(v -> saveUser());

        return view;
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            cameraLauncher.launch(intent);
        } else {
            Toast.makeText(requireContext(), "Impossible d'ouvrir la caméra", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUser() {
        OnboardingData data = OnboardingData.getInstance();

        if (data.getPseudo() == null || data.getNaissance() == null || data.getPoids() == 0 || data.getTaille() == 0) {
            Toast.makeText(requireContext(), "Veuillez remplir toutes les informations", Toast.LENGTH_SHORT).show();
            return;
        }

        if (photoUri == null) {
            Toast.makeText(requireContext(), "Prenez une photo avant de continuer", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(
                data.getPseudo(),
                data.getNaissance(),
                data.getTaille(),
                data.getPoids(),
                "kg",
                "clair",
                photoUri
        );

        user.insert(requireContext());
        ((OnboardingActivity) requireActivity()).finishOnboarding();
    }
}
