package com.sorezel.burritos.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sorezel.burritos.R;

public class VacioFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vacio,container,false);
        TextView txvTexto = v.findViewById(R.id.txv_vacio);
        ImageView imgIcono = v.findViewById(R.id.img_icono);
        if(getArguments() != null){
            Bundle b = getArguments();
            txvTexto.setText(b.getString("txt"));
            imgIcono.setImageResource(b.getInt("img"));
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
