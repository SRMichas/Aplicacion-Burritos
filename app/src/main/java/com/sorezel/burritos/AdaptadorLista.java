package com.sorezel.burritos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.sorezel.burritos.BD.LocalHelper;
import com.sorezel.burritos.Fragments.PagarFragment;
import com.sorezel.burritos.Fragments.VacioFragment;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.Objetos.VolleySingleton;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorLista extends RecyclerView.Adapter<AdaptadorLista.ViewHolder>{

    static ArrayList<Burrito> lista;
    Context c;
    int type;
    boolean algo;
    public ArrayList<Integer> canti;
    Fragment frag;
    SharedPreferences sh;
    private boolean pasa;
    private Activity act;

    public AdaptadorLista(ArrayList<Burrito> l, int t, Fragment fa, Activity a){
        lista = l;
        type = t;
        frag = fa;
        act = a;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = null;
        switch (type){
            case 1: case 4:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_mn_grid,parent,false);
                algo = true;
                break;
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_carrito,parent,false);
                algo = false;
                break;
            case 3:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_list_ord,parent,false);
                break;
        }
        c = parent.getContext();
        sh = c.getSharedPreferences("Usuario",0);
        pasa = sh.getBoolean("bd",false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder( ViewHolder vh, int i) {
        Burrito b = lista.get(i);
        vh.indice = i;

        switch(type){
            case 1:
                vh.txvNom.setText(b.getNombre());
                try {
                    vh.foto.setImageResource(Imagenes.FOTOS[b.getId()-1]);
                }catch (Exception e){
                    vh.foto.setImageResource(R.drawable.burrito);
                }
                break;
            case 2:
                vh.txvNom.setText(b.getNombre());
                vh.txvPre.setText("$"+b.getPrecio());
                vh.txvCant.setText(""+canti.get(i));
                vh.tot = canti.get(i);
                break;
            case 3:
                vh.txvNom.setText(b.getNombre());
                vh.txvPre.setText("$"+b.getPrecio());
                vh.txvCant.setText(""+canti.get(i));
                break;
            case 4:
                vh.txvNom.setText(b.getNombre());
                try {
                    vh.foto.setImageResource(Imagenes.FOTOS[b.getId()-1]);
                }catch (Exception e){
                    vh.foto.setImageResource(R.drawable.burrito);
                }
                vh.icon.setVisibility(View.VISIBLE);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void upd(){

        notifyDataSetChanged();
    }
    private void error(VolleyError error) {
        if (error instanceof TimeoutError) {
            Toast.makeText(c, "Tiempo Expirado", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(c, "No hay Conexion", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(c, "Erorr de Autentificacion", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(c, "Servidor no Disponible", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(c, "Error de red", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(c, "Error de Parseo", Toast.LENGTH_SHORT).show();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txvNom,tv_disp,txvPre,min,plu,txvCant,txvElim;
        Button btnOrd,btnDet;
        Toolbar tb;
        int indice,tot = 0;
        ImageView foto,icon;
        View vp;
        public ViewHolder( View v) {
            super(v);
            vp = v;
            /*txvNom= v.findViewById(R.id.txtv_nombre);
            tv_disp = v.findViewById(R.id.txtv_disp);
            btnOrd = v.findViewById(R.id.btn_ord);
            btnDet = v.findViewById(R.id.btn_det);

            btnDet.setOnClickListener(this);
            btnOrd.setOnClickListener(this);*/


            switch (type){
                case 1:
                    tb = v.findViewById(R.id.modTool);
                    tb.inflateMenu(R.menu.menu_prob);
                    foto = v.findViewById(R.id.icono_grid);
                    foto.setOnClickListener(this);
                    txvNom= v.findViewById(R.id.txv_nombre_grid);
                    break;
                case 2:
                    txvNom= v.findViewById(R.id.txv_carrito_nom);
                    txvPre = v.findViewById(R.id.txv_carrito_pre);
                    min = v.findViewById(R.id.txv_carr_min);
                    min.setOnClickListener(this);
                    plu = v.findViewById(R.id.txv_carr_plus);
                    plu.setOnClickListener(this);
                    txvCant = v.findViewById(R.id.txv_carr_pedi);
                    txvElim = v.findViewById(R.id.txv_car_eliminar);
                    txvElim.setCompoundDrawablesWithIntrinsicBounds(changeDrawableColor(c,R.drawable.ic_cancel, Color.RED),null,null,null);
                    txvElim.setOnClickListener(this);
                    break;
                case 3:
                    txvNom= v.findViewById(R.id.txv_mdord_nom);
                    txvPre = v.findViewById(R.id.txv_mdord_precio);
                    txvCant = v.findViewById(R.id.txv_mdord_cant);
                    break;
                case 4:
                    tb = v.findViewById(R.id.modTool);
                    tb.inflateMenu(R.menu.menu_prob);
                    foto = v.findViewById(R.id.icono_grid);
                    foto.setOnClickListener(this);
                    txvNom= v.findViewById(R.id.txv_nombre_grid);
                    icon = v.findViewById(R.id.ic);
                    icon.setOnClickListener(this);
                    break;
            }


        }
        public Drawable changeDrawableColor(Context context, int icon, int newColor) {
            Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
            mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
            mDrawable.setBounds(0,0,10,10);
            return mDrawable;
        }
        private void fragmentVacio(){
            FragmentManager fgm = frag.getFragmentManager();
            Bundle b = new Bundle();
            b.putString("txt","No has seleccionado ningun burrito, trata de ordenar uno :)");
            b.putInt("img",R.drawable.ic_menu_share);
            VacioFragment vf = new VacioFragment();
            vf.setArguments(b);
            fgm.beginTransaction().replace(R.id.frag_cont,vf,"Vacio").addToBackStack("Vacio").commit();
        }

        public void elimina(Burrito b){
            lista.remove(b);
        }

        private void elimina(){
            String url = act.getString(R.string.ip)+"BorrarCarritoT.php?uid="+sh.getInt("ID",-1)+"&bid="+lista.get(indice).getId();
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    PagarFragment pg = (PagarFragment)frag;
                    lista.remove(indice);
                    notifyDataSetChanged();
                    pg.mod();
                    if(lista.size() == 0){
                        fragmentVacio();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(c,"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                    error(error);
                }
            });
            VolleySingleton.getIntanciaVolley(c).addToRequestQueue(request);
        }

        private void eliminaFav(){
            String url = act.getString(R.string.ip)+"BorrarFavoritos.php?uid="+sh.getInt("ID",-1)+"&bid="+lista.get(indice).getId();
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    lista.remove(indice);
                    notifyDataSetChanged();
                    if(lista.size() == 0){
                        fragmentVacio();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(c,"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                    error(error);
                }
            });
            VolleySingleton.getIntanciaVolley(c).addToRequestQueue(request);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();

            if(id == R.id.txv_carr_plus){
                PagarFragment pg = (PagarFragment)frag;
                tot++;
                if(tot <= lista.get(indice).getStack())
                    txvCant.setText(""+tot);
                else{
                    Snackbar.make(plu,"No se puede realiazar esta accion",Snackbar.LENGTH_SHORT).show();
                    tot--;
                }
                canti.set(indice,tot);
                pg.mod();
            }else if(id == R.id.txv_carr_min){
                PagarFragment pg = (PagarFragment)frag;
               tot--;
               if(tot < 0)
                   tot = 0;
               txvCant.setText(""+tot);
                canti.set(indice,tot);
                pg.mod();
            }else if( id == R.id.txv_car_eliminar ){

                if( pasa ){
                    PagarFragment pg = (PagarFragment)frag;
                    String[] a = {""+sh.getInt("ID",-1),""+lista.get(indice).getId()};
                    LocalHelper.delCarrito(a,true);
                    lista.remove(indice);
                    notifyDataSetChanged();
                    pg.mod();
                    if(lista.size() == 0){
                        fragmentVacio();
                    }
                }else{
                    elimina();
                }



            }else if( id == R.id.icono_grid){
                Intent in = new Intent(view.getContext(), DetalleActivity.class);
                in.putExtra("burro",lista.get(indice));
                view.getContext().startActivity(in);
            }else if( id == R.id.ic){
                if( pasa ){
                    SharedPreferences sh = c.getSharedPreferences("Usuario",0);
                    String[] a = {""+sh.getInt("ID",-1),""+lista.get(indice).getId()};
                    LocalHelper.borraFavorito(a);
                    lista.remove(indice);
                    notifyDataSetChanged();
                    if(lista.size() == 0){
                        fragmentVacio();
                    }
                }else{
                    eliminaFav();
                }

            }
        }
    }
}
