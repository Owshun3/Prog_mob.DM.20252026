package com.example.faza.ui.home;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.faza.R;
import com.example.faza.User;

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

        User user = User.get(requireContext());

        if (user != null) {
            profileName.setText(user.getPseudo());

            profileDetails.setText("Poids : " + user.getPoids() + " " + user.getUnite() +
                    " | Taille : " + user.getTaille() + " cm");

            if (user.getPhotoUri() != null && !user.getPhotoUri().isEmpty()) {
                profileImage.setImageURI(Uri.parse(user.getPhotoUri()));
            } else {
                profileImage.setImageResource(R.drawable.ic_person);
            }
        } else {
            profileName.setText("Utilisateur");
            profileDetails.setText("");
            profileImage.setImageResource(R.drawable.ic_person);
        }

        return root;
    }
}
