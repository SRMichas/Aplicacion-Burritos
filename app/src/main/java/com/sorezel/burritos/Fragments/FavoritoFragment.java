package com.sorezel.burritos.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sorezel.burritos.AdaptadorLista;
import com.sorezel.burritos.Adaptadores.AdaptadorTop10;
import com.sorezel.burritos.BD.LocalHelper;
import com.sorezel.burritos.MiDialogo;
import com.sorezel.burritos.MoinActivity;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.Objetos.VolleySingleton;
import com.sorezel.burritos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoritoFragment extends Fragment {

    RecyclerView rv;
    ArrayList<Burrito> ar;
    ImageView img;
    SharedPreferences sh;
    private JsonObjectRequest jsonr;
    boolean pasa;
    MiDialogo dia;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favoritos,container,false);
        ((MoinActivity)getActivity()).getSupportActionBar().setTitle("Favoritos");
        sh = getActivity().getSharedPreferences("Usuario",0);
        Bundle b = new Bundle();
        dia = new MiDialogo();
        b.putInt("tipo",R.layout.dialog_espera);
        dia.show(getFragmentManager(),"Dialogo");

        if(getArguments() != null){
            ar = (ArrayList<Burrito>) getArguments().getSerializable("burros");
        }
        rv = v.findViewById(R.id.recy_fav);
        pasa = sh.getBoolean("bd",false);
        //if( pasa )
            creaRecycler();
        //else{
            //llenaLista();
        //}
        return v;
    }
    public void creaRecycler(){
        AdaptadorLista adapter = new AdaptadorLista(ar,4,this,getActivity());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        dia.dismiss();
    }
    @Override
    public void onResume() {
        if( pasa ){
            ar = LocalHelper.retFavoritos(sh.getInt("ID",-1));
            if(ar != null ){
                creaRecycler();
            }else{
                FragmentManager fgm = getFragmentManager();
                Bundle b = new Bundle();
                b.putString("txt","No tienes favoritos :(");
                b.putInt("img",R.drawable.ic_menu_share);
                VacioFragment vf = new VacioFragment();
                vf.setArguments(b);
                fgm.beginTransaction().replace(R.id.frag_cont,vf).commit();
            }
        }

        super.onResume();
    }
    private void llenaLista(){
        int id=sh.getInt("ID",-1);
        String url = getString(R.string.ip) + "ConsultaFavoritos.php?UsId="+id;
        jsonr = new JsonObjectRequest(0, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json=response.optJSONArray("favoritos");
                ar = new ArrayList<>();
                try {

                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jso=null;
                        jso=json.getJSONObject(i);

                        ar.add(
                                new Burrito(jso.optInt("BId"),jso.optString("BNombre"),jso.optString("BDescripcion"),jso.optInt("BCategoria")
                                        ,jso.optInt("BStack"),jso.optInt("BPopularidad"),jso.optDouble("BPrecio"))
                        );
                    }
                    creaRecycler();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                            " "+response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonr);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        ((MoinActivity)getActivity()).getSupportActionBar().setTitle("Home");
    }
}
