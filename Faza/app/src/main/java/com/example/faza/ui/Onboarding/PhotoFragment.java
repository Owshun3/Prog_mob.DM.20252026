package com.example.faza.ui.Onboarding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
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
import com.example.faza.data.entites.User;

import java.io.File;
import java.io.FileOutputStream;

public class PhotoFragment extends Fragment {

    private ImageView imgPhoto;
    private String photoUri = "";

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        if (photo == null) return;

                        imgPhoto.setImageBitmap(photo);

                        photoUri = saveBitmapToInternal(photo);
                        OnboardingData.getInstance().setPhotoUri(photoUri);
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri == null) return;

                    try {
                        Bitmap bitmap;

                        if (Build.VERSION.SDK_INT >= 28) {
                            ImageDecoder.Source source = ImageDecoder.createSource(requireContext().getContentResolver(), uri);
                            bitmap = ImageDecoder.decodeBitmap(source);
                        } else {
                            bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
                        }

                        imgPhoto.setImageBitmap(bitmap);

                        photoUri = saveBitmapToInternal(bitmap);
                        OnboardingData.getInstance().setPhotoUri(photoUri);

                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Impossible d’importer l’image", Toast.LENGTH_SHORT).show();
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
        Button btnGalerie = view.findViewById(R.id.btn_galerie);
        Button btnTerminer = view.findViewById(R.id.btn_terminer);

        btnPhoto.setOnClickListener(v -> openCamera());
        btnGalerie.setOnClickListener(v -> galleryLauncher.launch("image/*"));
        btnTerminer.setOnClickListener(v -> saveUser());

        return view;
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            cameraLauncher.launch(intent);
        } else {
            Toast.makeText(requireContext(), "Caméra indisponible", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveBitmapToInternal(Bitmap bitmap) {
        try {
            File file = new File(requireContext().getFilesDir(),
                    "photo_" + System.currentTimeMillis() + ".jpg");

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            return file.getAbsolutePath();

        } catch (Exception e) {
            return "";
        }
    }

    private void saveUser() {

        OnboardingData data = OnboardingData.getInstance();

        if (data.getPseudo() == null ||
                data.getNaissance() == null ||
                data.getPoids() == 0 ||
                data.getTaille() == 0) {

            Toast.makeText(requireContext(),
                    "Veuillez remplir toutes les informations", Toast.LENGTH_SHORT).show();
            return;
        }

        if (photoUri == null || photoUri.isEmpty()) {photoUri = "";}

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
