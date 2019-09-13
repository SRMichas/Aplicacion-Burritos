package com.sorezel.burritos.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.sorezel.burritos.MoinActivity;
import com.sorezel.burritos.Objetos.Usuario;
import com.sorezel.burritos.R;
import com.sorezel.burritos.UsuarioActivity;

public class PerfilFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((UsuarioActivity)getActivity()).getSupportActionBar().setTitle("Perfil");
        View v = inflater.inflate(R.layout.fragment_perfil,container,false);
        TextView tvnom = v.findViewById(R.id.txv_prof_name),txvcorreo = v.findViewById(R.id.txv_prof_email);
        Usuario us = null;
        if(getArguments() != null){
            Bundle b = getArguments();
            us = (Usuario) b.getSerializable("us");
        }
        tvnom.setText(us.nombreCompleto());
        txvcorreo.setText(us.getCorreo());


       /* tb.inflateMenu(R.menu.menu_user);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SharedPreferences s = getContext().getSharedPreferences("Ususario", Context.MODE_PRIVATE);
                String info = s.getString("ID",null)+" "+s.getString("Nombre",null)+" "
                        +s.getString("Correo",null)+" "+s.getString("Contrasena",null);
                Toast.makeText(getContext(),info,Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences s = getContext().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        String info = ""+s.getInt("ID",-1)+" "+s.getString("Nombre",null)+" "
                +s.getString("Correo",null)+" "+s.getString("Contrasena",null);
        Toast.makeText(getContext(),info,Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }*/
}
