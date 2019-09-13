package com.sorezel.burritos.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sorezel.burritos.Adaptadores.AdaptadorOrden;
import com.sorezel.burritos.Objetos.Orden;
import com.sorezel.burritos.R;

import java.util.ArrayList;

public class OrdenActivoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ord_act,container,false);
        RecyclerView rv = v.findViewById(R.id.rv_ord);
        ArrayList<Orden> ar = (ArrayList<Orden>) getArguments().getSerializable("lista");
        AdaptadorOrden ao = new AdaptadorOrden(ar);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(ao);
        return v;
    }
}
