package com.example.faza.data.Service;

import android.content.Context;

import com.example.faza.R;
import com.example.faza.data.entites.Exercice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExerciceCsvLoader {
    /*
    public static List<Exercice> chargerDepuisRaw(Context context) {
        List<Exercice> exercices = new ArrayList<>();

        InputStream is = context.getResources().openRawResource(R.raw.exercices);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {
            String ligne;
            boolean first = true;
            while ((ligne = reader.readLine()) != null) {

                if (first) {
                    first = false;
                    continue;
                }

                Exercice e = parseLigne(ligne);
                if (e != null) exercices.add(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { reader.close(); } catch (IOException ignored) {}
        }

        return exercices;
    }

    private static Exercice parseLigne(String line) {
        try {
            String[] c = line.split(";", -1);

            Exercice e = new Exercice();
            e.setNom(c[1]);
            e.setGroupePrincipal(c[2]);
            e.setGroupeSecondaire(c[3]);
            e.setDescription("");
            e.setUrlVideo(c[5]);
            e.setMiniature(c[4]);

            return e;

        } catch (Exception ex) {
            return null;
        }
    }

     */
}