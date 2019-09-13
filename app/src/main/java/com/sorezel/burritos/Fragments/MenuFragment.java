package com.sorezel.burritos.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sorezel.burritos.AdaptadorGrid;
import com.sorezel.burritos.Adaptadores.AdaptadorMenu;
import com.sorezel.burritos.BD.RemoteHelper;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class MenuFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grid,null);

        int col = 2;
        GridLayoutManager glm = new GridLayoutManager(getContext(),col);

        RecyclerView rc = v.findViewById(R.id.list_grid);
        Bundle b = getArguments();
        ArrayList<Burrito> a = (ArrayList<Burrito>) b.getSerializable("lista");
        AdaptadorMenu am = new AdaptadorMenu(a,getActivity(),getFragmentManager());

        rc.setLayoutManager(glm);
        rc.setAdapter(am);


        return v;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
