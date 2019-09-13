package com.sorezel.burritos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grid,null);

        int col = 2;
        GridLayoutManager glm = new GridLayoutManager(getContext(),col);

        RecyclerView rc = v.findViewById(R.id.list_grid);
        Bundle b = getArguments();
        ArrayList<String> a = b.getStringArrayList("lista");
        AdaptadorGrid ag = new AdaptadorGrid(a);

        rc.setLayoutManager(glm);
        rc.setAdapter(ag);


        return v;
    }
}
