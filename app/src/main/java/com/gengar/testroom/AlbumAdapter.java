package com.gengar.testroom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AlbumAdapter extends ArrayAdapter<AlbumSearch>{


    public AlbumAdapter(Activity context, List<AlbumSearch> words){
        super(context,0,words);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItamView = convertView;
        if(listItamView == null){
            listItamView = LayoutInflater.from(getContext()).inflate(R.layout.album_itam,parent,false);
        }

        AlbumSearch current = getItem(position);

        TextView tracName = listItamView.findViewById(R.id.track);
        tracName.setText(current.getAlbumName());

        TextView artistName = listItamView.findViewById(R.id.artist);
        artistName.setText(current.getArtistName());

        TextView lenghtText = listItamView.findViewById(R.id.lenght);
        lenghtText.setText(Float.toString(current.getLenght()));

        return listItamView;
    }
}
