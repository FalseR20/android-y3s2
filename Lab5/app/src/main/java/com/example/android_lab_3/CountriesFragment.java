package com.example.android_lab_3;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;


public class CountriesFragment extends Fragment {
    private ArrayList<Country> countriesData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countries, container, false);

        countriesData = new ArrayList<>();
        Collections.addAll(
                countriesData,
                new Country("Germany", R.drawable.germany, "https://www.youtube.com/watch?v=rcVb6l4TpHw"),
                new Country("France", R.drawable.france, "https://www.youtube.com/watch?v=e5Pu24Ve-vo"),
                new Country("Italy", R.drawable.italy, "https://www.youtube.com/watch?v=GA4JThRBvck")
        );

        ArrayList<String> countriesTitles = new ArrayList<>();
        countriesData.forEach(capital -> countriesTitles.add(capital.getTitle()));


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                view.getContext(),
                android.R.layout.simple_list_item_1,
                countriesTitles
        );
        ListView countriesListView = view.findViewById(R.id.countries_list);
        countriesListView.setAdapter(adapter);

        countriesListView.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent intent = new Intent(view.getContext(), CapitalPage.class);
            intent.putExtra("title", countriesData.get(i).getTitle());
            intent.putExtra("pictureId", countriesData.get(i).getPictureId());
            intent.putExtra("videoUrl", countriesData.get(i).getVideoUrl());
            startActivity(intent);
        });

        return view;
    }

    private class Country {
        private String title;
        private int pictureId;
        private String videoUrl;

        public Country(String title, int pictureId, String videoUrl) {
            this.title = title;
            this.pictureId = pictureId;
            this.videoUrl = videoUrl;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setPictureId(int pictureId) {
            this.pictureId = pictureId;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getTitle() {
            return title;
        }

        public int getPictureId() {
            return pictureId;
        }

        public String getVideoUrl() {
            return videoUrl;
        }
    }
}