package com.sorezel.burritos.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;
import com.sorezel.burritos.AdaptadorMain;
import com.sorezel.burritos.Imagenes;
import com.sorezel.burritos.MoinActivity;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.Objetos.Usuario;
import com.sorezel.burritos.Objetos.VolleySingleton;
import com.sorezel.burritos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {

    TabLayout tbl;
    AdaptadorMain am;
    ArrayList<Burrito> burros, top;
    ViewPager vp;
    boolean prueba = false;
    JsonObjectRequest jsonobj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        boolean cosa = false;
        if (getArguments() != null) {
            Bundle b = getArguments();
            burros = (ArrayList<Burrito>) b.getSerializable("menu");
            top = (ArrayList<Burrito>) b.getSerializable("top");
            cosa = b.getBoolean("boo");
        }
        tbl = v.findViewById(R.id.tabMain);
        vp = v.findViewById(R.id.pager);
        if (cosa)
            load();
        else {
            burros = new ArrayList<>();
            top = new ArrayList<>();
            load2();
            cargarWS();
        }

        tbl.setupWithViewPager(vp);
        return v;
    }

    private void load() {
        am = new AdaptadorMain(getChildFragmentManager(), new String[]{"Menu", "Top 10"});

        Bundle b = new Bundle(),
                b2 = new Bundle();

        ArrayList<String> burros1, burros2;
        burros1 = kk2(12);

        MenuFragment mf = new MenuFragment();
        b.putSerializable("lista", burros);
        mf.setArguments(b);
        am.addFrag(mf);

        /*burros2 = kk2(10);
        ListaFragment ls = new ListaFragment();
        b2.putStringArrayList("lista",burros2);
        ls.setArguments(b2);
        am.addFrag(ls);*/


        burros2 = kk2(10);
        ListaFragment gf = new ListaFragment();
        b2.putSerializable("lista", top);
        gf.setArguments(b2);
        am.addFrag(gf);

        vp.setAdapter(am);

    }

    private void load2() {
        am = new AdaptadorMain(getChildFragmentManager(), new String[]{"Menu", "Top 10"});
        am.addFrag(new CargarFragment());
        am.addFrag(new CargarFragment());

        vp.setAdapter(am);

    }

    private ArrayList<String> kk2(int c) {
        ArrayList<String> str = new ArrayList<>();
        for (int i = 0; i < c; i++) {
            str.add("Burro #" + (i + 1));
        }
        return str;
    }

    public void corrige() {
        if (tbl.getSelectedTabPosition() != 0) {
            tbl.getTabAt(0).select();
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onPause() {
        prueba = true;
        super.onPause();
    }

    @Override
    public void onResume() {

        if (prueba) {
            Toast.makeText(getContext(), "OnResume de MAinFragment", Toast.LENGTH_SHORT).show();
            // load();
        }
        prueba = false;
        super.onResume();
        /*load(vp);
        tbl.setupWithViewPager(vp);*/
    }

    private void cargarWS() {
        String ip = getString(R.string.ip);
        String url = ip + "ConsultaBurritos.php";
        jsonobj = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonobj);
    }





    @Override
    public void onErrorResponse(VolleyError error) {
        if (error instanceof TimeoutError) {
            am.replaceFrag(Imagenes.errorFragment(1),0);
            am.replaceFrag(Imagenes.errorFragment(1),1);
        }else if (error instanceof NoConnectionError){
            am.replaceFrag(Imagenes.errorFragment(2),0);
            am.replaceFrag(Imagenes.errorFragment(2),1);
        }else if(error instanceof AuthFailureError){
            am.replaceFrag(Imagenes.errorFragment(3),0);
            am.replaceFrag(Imagenes.errorFragment(3),1);
        }else if( error instanceof ServerError){
            am.replaceFrag(Imagenes.errorFragment(4),0);
            am.replaceFrag(Imagenes.errorFragment(4),1);
        }else if( error instanceof NetworkError){
            am.replaceFrag(Imagenes.errorFragment(5),0);
            am.replaceFrag(Imagenes.errorFragment(5),1);
        }else if( error instanceof ParseError){
            am.replaceFrag(Imagenes.errorFragment(6),0);
            am.replaceFrag(Imagenes.errorFragment(6),1);
        }
        Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Burrito burr =null;
        JSONArray json=response.optJSONArray("burritos");

        try {
            for (int i=0;i<json.length();i++){
                JSONObject jso=null;
                jso=json.getJSONObject(i);

                burr = new Burrito(jso.optInt("BId"),jso.optString("BNombre"),jso.optString("BDescripcion"),jso.optInt("BCategoria")
                ,jso.optInt("BStack"),jso.optInt("BPopularidad"),jso.optDouble("BPrecio"));

                burros.add(burr);
            }
            json = response.optJSONArray("top");
            for (int i=0;i<json.length();i++){
                JSONObject jso=null;
                jso=json.getJSONObject(i);

                burr = new Burrito(jso.optInt("BId"),jso.optString("BNombre"),jso.optString("BDescripcion"),jso.optInt("BCategoria")
                        ,jso.optInt("BStack"),jso.optInt("BPopularidad"),jso.optDouble("BPrecio"));

                top.add(burr);
            }
            //UsuariosAdapter adapter=new UsuariosAdapter(listaUsuarios);
            //recyclerUsuarios.setAdapter(adapter);
            load();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
        }
    }
}
