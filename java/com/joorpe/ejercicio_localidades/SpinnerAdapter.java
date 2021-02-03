package com.joorpe.ejercicio_localidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



import java.util.List;

class SpinnerAdapter extends ArrayAdapter<Ubicacion> {

    List<Ubicacion> ubicaciones;

    public SpinnerAdapter(@NonNull Context context, List<Ubicacion> ubicaciones) {
        super(context, 0,ubicaciones);
        this.ubicaciones = ubicaciones;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Ubicacion u = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ubicacion, parent, false);
        }
        TextView tvDescripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
        tvDescripcion.setText(u.getDescripcion());
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ubicacion u = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ubicacion, parent, false);
        }
        TextView tvDescripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
        tvDescripcion.setText(u.getDescripcion());
        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }



    @Nullable
    @Override
    public Ubicacion getItem(int position) {
        return ubicaciones.get(position);
    }

    public void setUbicaciones(List<Ubicacion> ubicaciones) {
        this.ubicaciones.clear();
        for (Ubicacion ubicacion: ubicaciones){
            this.ubicaciones.add(ubicacion);
        }
        notifyDataSetChanged();
    }
}