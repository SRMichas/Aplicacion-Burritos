package com.sorezel.burritos.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sorezel.burritos.DetalleActivity;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.R;

import java.util.ArrayList;

public class AdaptadorTop10 extends RecyclerView.Adapter<AdaptadorTop10.ViewHolder> {

    ArrayList<Burrito> lista;
    Context c;

    public AdaptadorTop10(ArrayList<Burrito> l){
        lista = l;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_lista,parent,false);
        c = parent.getContext();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder( ViewHolder vh, int i) {
        vh.indice = i;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_nom,tv_disp;
        Button btnOrd,btnDet;
        int indice;
        public ViewHolder( View v) {
            super(v);
            tv_nom = v.findViewById(R.id.txtv_nombre);
            tv_disp = v.findViewById(R.id.txtv_disp);
            btnOrd = v.findViewById(R.id.btn_ord);
            btnDet = v.findViewById(R.id.btn_det);

            btnDet.setOnClickListener(this);
            btnOrd.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if(id == R.id.btn_ord){
                Snackbar.make(view,"Ordenaste: "+ tv_nom.getText().toString()+" Cantidad: "+
                        tv_disp.getText().toString(),Snackbar.LENGTH_SHORT).show();
            }else if(id == R.id.btn_det){
                Snackbar.make(view,"Burrito: ["+" "+indice+"] " + tv_nom.getText().toString()+" Disponibles: "+
                        tv_disp.getText().toString(),Snackbar.LENGTH_SHORT).show();
                Intent it = new Intent(view.getContext(), DetalleActivity.class);
                c.startActivity(it);
            }else{
                Snackbar.make(view,"Picale a los botones Carnal",Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
