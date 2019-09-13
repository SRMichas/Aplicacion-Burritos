package com.sorezel.burritos.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sorezel.burritos.AdaptadorLista;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.R;

import java.util.ArrayList;

public class Top10Fragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,null);
        RecyclerView rc = v.findViewById(R.id.lista);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        if( getActivity() != null){
            Bundle b = getArguments();
            ArrayList<Burrito> a = (ArrayList<Burrito>) b.getSerializable("lista");
            AdaptadorLista al = new AdaptadorLista(a,1,this,getActivity());
            //Toast.makeText(getContext(),"no NULL, "+a.size(),Toast.LENGTH_SHORT).show();
            DividerItemDecoration dd = new DividerItemDecoration(rc.getContext(),llm.getOrientation());
            rc.setLayoutManager(llm);
            rc.addItemDecoration(dd);
            rc.setAdapter(al);
        }


        return v;
    }
}
