package com.sorezel.burritos;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.Objetos.Orden;

import java.util.ArrayList;

public class DetalleOrdenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_orden);
        Toolbar tb = findViewById(R.id.tool_det_ord);
        tb.setTitle("Detalle Orden");
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Orden ord = null;
        ArrayList<Burrito> burros = null;
        ArrayList<Integer> cantis = null;


        if (getIntent() != null) {
            ord = (Orden) getIntent().getSerializableExtra("orden");
            burros = ord.getBurros();
            cantis = ord.getCantidad();
        }

        TextView txvCant = findViewById(R.id.txv_drod_canti),txvTot = findViewById(R.id.txv_dord_tot),
        txvFch = findViewById(R.id.txv_dord_fch);
        RecyclerView rv = findViewById(R.id.recycler_det_ord);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        AdaptadorLista a = new AdaptadorLista(ord.getBurros(),3,null,this);
        a.canti = cantis;
        rv.setLayoutManager(llm);
        rv.setAdapter(a);
        txvCant.setText(""+burros.size());
        txvTot.setText("$"+ord.total());
        tb.setTitle("Orden #"+ord.getFolio());
        txvFch.setText(ord.getFecha());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if( id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
