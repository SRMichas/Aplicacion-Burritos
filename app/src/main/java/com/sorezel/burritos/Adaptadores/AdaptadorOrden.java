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

import com.sorezel.burritos.DetalleOrdenActivity;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.Objetos.Orden;
import com.sorezel.burritos.R;

import java.util.ArrayList;

public class AdaptadorOrden extends RecyclerView.Adapter<AdaptadorOrden.ViewHolder>{

    ArrayList<Orden> ordenes;
    private int type;
    private Context c;
    public AdaptadorOrden(ArrayList<Orden> ord){
        ordenes = ord;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_orden,parent,false);
        ViewHolder vh = new ViewHolder(v);
        c = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int i) {
        Orden ord = ordenes.get(i);
        vh.idx = i;
        vh.txvFch.setText(ord.getFecha());
        vh.txvNo.setText("Orden #"+ord.getFolio());
        vh.txvTot.setText("$"+ord.total());
        vh.s.setText(""+ordenes.size());
    }

    @Override
    public int getItemCount() {
        return ordenes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ArrayList<Burrito> burr;
        TextView txvFch,txvNo,txvTot,s;
        int idx;
        public ViewHolder( View iv) {
            super(iv);
            txvFch = iv.findViewById(R.id.txv_pord_fecha);
            txvNo = iv.findViewById(R.id.txv_pord_no);
            txvTot = iv.findViewById(R.id.txv_pord_total);
            s = iv.findViewById(R.id.txv_status);
            Button boto = iv.findViewById(R.id.btn_detalle);
            boto.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent in = new Intent(c, DetalleOrdenActivity.class);
            in.putExtra("orden",ordenes.get(idx));
            c.startActivity(in);
        }
    }
}
