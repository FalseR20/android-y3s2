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

public class CapitalsFragment extends Fragment {
    private ArrayList<Capital> capitalsData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_capitals, container, false);

        capitalsData = new ArrayList<>();
        Collections.addAll(
                capitalsData,
                new Capital("Berlin", R.drawable.berlin, "https://www.youtube.com/watch?v=4SGLxZnqYIg"),
                new Capital("Paris", R.drawable.paris, "https://www.youtube.com/watch?v=-gHbYgFxNPg"),
                new Capital("Rome", R.drawable.rome, "https://www.youtube.com/watch?v=ytiM1nMv_xU")
        );

        ArrayList<String> capitalsTitles = new ArrayList<>();
        capitalsData.forEach(capital -> capitalsTitles.add(capital.getTitle()));


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                view.getContext(),
                android.R.layout.simple_list_item_1,
                capitalsTitles
        );
        ListView capitalsListView = view.findViewById(R.id.capitals_list);
        capitalsListView.setAdapter(adapter);

        capitalsListView.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent intent = new Intent(view.getContext(), CapitalPage.class);
            intent.putExtra("title", capitalsData.get(i).getTitle());
            intent.putExtra("pictureId", capitalsData.get(i).getPictureId());
            intent.putExtra("videoUrl", capitalsData.get(i).getVideoUrl());
            startActivity(intent);
        });

        return view;
    }

    private class Capital {
        private String title;
        private int pictureId;
        private String videoUrl;

        public Capital(String title, int pictureId, String videoUrl) {
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