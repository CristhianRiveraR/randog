package com.example.randog.resources;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.randog.R;
import com.example.randog.models.Perro;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    private ArrayList<Perro> listPerro;
    private Context context;
    private LayoutInflater inflater;

    public Adaptador(Context context, ArrayList<Perro> listEntidad) {
        this.context = context;
        this.listPerro = listEntidad;
    }

    @Override
    public int getCount() {
        return listPerro.size();
    }

    @Override
    public Object getItem(int position) {
        return listPerro.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Perro perro = (Perro) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item_perro, null);
        ImageView imgDog = (ImageView) convertView.findViewById(R.id.imgDog);
        TextView tvPesoImg = (TextView) convertView.findViewById(R.id.tvPesoImg);

        new DownLoadImageTask(imgDog).execute(perro.getUrl());

        tvPesoImg.setText(perro.getFileSizeBytes());

        return convertView;
    }
}
