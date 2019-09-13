package com.sorezel.burritos.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sorezel.burritos.BD.LocalHelper;
import com.sorezel.burritos.Imagenes;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.Objetos.VolleySingleton;
import com.sorezel.burritos.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CargarFragment extends Fragment {

    private SharedPreferences sh;
    private JsonObjectRequest jsonr;
    protected Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sh = getActivity().getSharedPreferences("Usuario",0);
        return inflater.inflate(R.layout.cargar,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() == null)
            return;
        final Bundle b = getArguments();
        int tipo = b.getInt("t");
        boolean pass = b.getBoolean("b");
        String url = getString(R.string.ip);
        switch (tipo){
            case 1://Carrito
                if( !pass ){
                    url +="ConsultaCarrito.php?UsId="+sh.getInt("ID",-1);
                    jsonr = new JsonObjectRequest(0, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray json=response.optJSONArray("carrito");
                            //JSONObject jobj = response.optJSONObject("respuesta");
                            int val = response.optJSONArray("respuesta").optInt(0);
                            if( json != null && val == 1){
                                ArrayList<Burrito> ar = new ArrayList<>();
                                ArrayList<Integer> ai = new ArrayList<>();
                                try {

                                    for (int i = 0; i < json.length(); i++) {
                                        JSONObject jso;
                                        jso=json.getJSONObject(i);

                                        ar.add(
                                                new Burrito(jso.optInt("BId"),jso.optString("BNombre"),jso.optString("BDescripcion"),jso.optInt("BCategoria")
                                                        ,jso.optInt("BStack"),jso.optInt("BPopularidad"),jso.optDouble("BPrecio"))
                                        );
                                        ai.add(jso.optInt("Cant"));
                                    }
                                    if( ar.size() != 0){
                                        fragment = new PagarFragment();
                                        b.putSerializable("burros",ar);
                                        b.putBoolean("algo",true);
                                        b.putIntegerArrayList("canti",ai);
                                        fragment.setArguments(b);
                                    }else{
                                        fragment = new VacioFragment();
                                        b.putString("txt","No tienes favoritos :(");
                                        b.putInt("img",R.drawable.ic_menu_share);
                                        fragment.setArguments(b);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                                            " "+response, Toast.LENGTH_LONG).show();
                                }
                            }else if( val == 0){
                                fragment = Imagenes.carVacio();
                            }

                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,fragment,"carrito").addToBackStack("carrito").commit();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error(error);
                        }
                    });
                    VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonr);
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final int ID = sh.getInt("ID",-1);
                            final ArrayList<Burrito> burros = LocalHelper.retCarrito(ID);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(burros != null){
                                        fragment = new PagarFragment();
                                        b.putSerializable("burros",burros);
                                        fragment.setArguments(b);
                                    }else{
                                        fragment = new VacioFragment();
                                        b.putString("txt","No has seleccionado ningun burrito, trata de ordenar uno :)");
                                        b.putInt("img",R.drawable.ic_menu_share);
                                        fragment.setArguments(b);
                                    }
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,fragment,"carrito").addToBackStack("carrito").commit();
                                }
                            });
                        }
                    }).start();
                }
                break;
            case 2://Favoritos
                if( !pass){
                    url +="ConsultaFavoritos.php?UsId="+sh.getInt("ID",-1);
                    jsonr = new JsonObjectRequest(0, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray json=response.optJSONArray("favoritos");
                            JSONArray json2 = response.optJSONArray("respuesta");
                            int val = json2.optInt(0);
                            if( json != null && val == 1){
                                ArrayList<Burrito> ar = new ArrayList<>();
                                try {

                                    for (int i = 0; i < json.length(); i++) {
                                        JSONObject jso;
                                        jso=json.getJSONObject(i);

                                        ar.add(
                                                new Burrito(jso.optInt("BId"),jso.optString("BNombre"),jso.optString("BDescripcion"),jso.optInt("BCategoria")
                                                        ,jso.optInt("BStack"),jso.optInt("BPopularidad"),jso.optDouble("BPrecio"))
                                        );
                                    }
                                    if( ar.size() != 0){
                                        fragment = new FavoritoFragment();
                                        b.putSerializable("burros",ar);
                                        fragment.setArguments(b);
                                    }else{
                                        fragment = new VacioFragment();
                                        b.putString("txt","No tienes favoritos :(");
                                        b.putInt("img",R.drawable.ic_menu_share);
                                        fragment.setArguments(b);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                                            " "+response, Toast.LENGTH_LONG).show();
                                }
                            }else if( val == 0){
                                fragment = Imagenes.favVacio();
                            }
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,fragment,"favorito").addToBackStack("favorito").commit();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error(error);
                        }
                    });
                    VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonr);
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final int ID = sh.getInt("ID",-1);
                            final ArrayList<Burrito> burros = LocalHelper.retFavoritos(ID);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(burros != null){
                                        fragment = new FavoritoFragment();
                                        b.putSerializable("burros",burros);
                                        fragment.setArguments(b);
                                    }else{
                                        fragment = new VacioFragment();
                                        b.putString("txt","No tienes favoritos :(");
                                        b.putInt("img",R.drawable.ic_menu_share);
                                        fragment.setArguments(b);
                                    }
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,fragment,"favorito").addToBackStack("favorito").commit();
                                }
                            });
                        }
                    }).start();
                }
                break;
            case 3://Ordenes
                break;
                default:
                    break;
        }

    }
    private void error(VolleyError error){
        Fragment fragment;
        if (error instanceof TimeoutError) {
            fragment = Imagenes.errorFragment(1);
        }else if (error instanceof NoConnectionError){
            fragment = Imagenes.errorFragment(2);
        }else if(error instanceof AuthFailureError){
            fragment = Imagenes.errorFragment(3);
        }else if( error instanceof ServerError){
            fragment = Imagenes.errorFragment(4);
        }else if( error instanceof NetworkError){
            fragment = Imagenes.errorFragment(5);
        }else if( error instanceof ParseError){
            fragment = Imagenes.errorFragment(6);
        }else{
            fragment = new Fragment();
        }
        Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.frag_cont,fragment,"ErrorVolley").addToBackStack("ErrorVolley").commit();
    }
}
