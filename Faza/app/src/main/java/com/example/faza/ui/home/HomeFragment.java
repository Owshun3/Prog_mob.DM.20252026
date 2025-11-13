package com.example.faza.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.faza.R;
import com.example.faza.data.entites.User;

import java.io.File;

public class HomeFragment extends Fragment {

    private ImageView profileImage;
    private TextView profileName;
    private TextView profileDetails;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        profileImage = root.findViewById(R.id.profileImage);
        profileName = root.findViewById(R.id.profileName);
        profileDetails = root.findViewById(R.id.profileDetails);

        Button btnEdit = root.findViewById(R.id.ChangementProfile);

        refreshUserInfo();

        btnEdit.setOnClickListener(v -> {
            NavController nav = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            nav.navigate(R.id.navigation_profile_edit);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUserInfo();
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
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        profileImage.setImageBitmap(bitmap);
                    } else {
                        profileImage.setImageResource(R.drawable.ic_person);
                    }
                } catch (Exception e) {profileImage.setImageResource(R.drawable.ic_person);}
            } else {profileImage.setImageResource(R.drawable.ic_person);}
        } else {
            profileName.setText("Utilisateur");
            profileDetails.setText("");
            profileImage.setImageResource(R.drawable.ic_person);
        }
    }
}
