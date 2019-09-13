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
import androidx.fragment.app.FragmentManager;
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
import com.sorezel.burritos.BD.LocalHelper;
import com.sorezel.burritos.Imagenes;
import com.sorezel.burritos.MoinActivity;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.Objetos.Orden;
import com.sorezel.burritos.Objetos.VolleySingleton;
import com.sorezel.burritos.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainOrdenFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{

    TabLayout tbl;
    AdaptadorMain am;
    ArrayList<Orden> ord1,ord2;
    boolean pasa;
    SharedPreferences sh;
    private JsonObjectRequest jsonobj;
    private ViewPager vp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orden_main,container,false);
        ((MoinActivity)getActivity()).getSupportActionBar().setTitle("Ordenes");
        tbl = v.findViewById(R.id.tabOrdMain);
        vp = v.findViewById(R.id.pagerOrd);
        sh = getContext().getSharedPreferences("Usuario",0);
        pasa = sh.getBoolean("bd",false);
        if(pasa){
            /*ord1 = (ArrayList<Orden>) LocalHelper.retOrdenes(sh.getInt("ID",0),3);
            ord2 = (ArrayList<Orden>) LocalHelper.retOrdenes(sh.getInt("ID",0),1);*/
            load2();
        }else{
            load2();
            cargarWS();
        }


        tbl.setupWithViewPager(vp);

        return v;
    }

    private void cargarWS() {
        String url = getString(R.string.ip)+"ConsultaOrdenes.php?USID="+sh.getInt("ID",-1);
        jsonobj = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonobj);
    }

    private void load() {
        //am = new AdaptadorMain(getFragmentManager(),new String[]{"Activos","Historico"});

        Bundle b = new Bundle(),
                b2 = new Bundle();
        Fragment fragment = null, fragment2 = null;
        int algo = sh.getInt("ID",0);

        if(algo == 0){
            fragment = new VacioFragment();
            b.putString("txt","No se puede, inicie sesion");
            b.putInt("img",R.drawable.ic_error);
        }else if(ord1 != null){
            fragment = new OrdenActivoFragment();
            b.putSerializable("lista",ord1);
        }else{
            fragment = new VacioFragment();
            b.putString("txt","No hay ordenes disponibles");
            b.putInt("img",R.drawable.ic_error);
        }
        fragment.setArguments(b);
        //am.addFrag(fragment);

        /*burros2 = kk2(10);
        ListaFragment ls = new ListaFragment();
        b2.putStringArrayList("lista",burros2);
        ls.setArguments(b2);
        am.addFrag(ls);*/


        //burros2 = kk2(10);
        if(algo == 0){
            fragment = new VacioFragment();
            b.putString("txt","No se puede, inicie sesion");
            b.putInt("img",R.drawable.ic_error);
        }else if(ord2 != null){
            fragment2 = new OrdenActivoFragment();
            b2.putSerializable("lista",ord2);
        }else{
            fragment2 = new VacioFragment();
            b2.putString("txt","No hay ordenes disponibles");
            b2.putInt("img",R.drawable.ic_error);
        }

        fragment2.setArguments(b2);
        //am.addFrag(fragment2);

        //vp.setAdapter(am);
        am.replaceFrag(fragment,0);
        am.replaceFrag(fragment2,1);

    }
    private void load2() {
        am = new AdaptadorMain(getFragmentManager(),new String[]{"Activos","Historico"});
        am.addFrag(new CargarFragment());
        am.addFrag(new CargarFragment());

        vp.setAdapter(am);

        if( pasa )
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ord1 = LocalHelper.retOrdenes(sh.getInt("ID",0),3);
                    ord2 = LocalHelper.retOrdenes(sh.getInt("ID",0),1);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            load();
                        }
                    });
                }
            }).start();
    }

    public void corrige(){
        if ( tbl.getSelectedTabPosition() != 0 ){
            tbl.getTabAt(0).select();
        }else{
            FragmentManager fm = getFragmentManager();
            fm.popBackStack("menu",0);
        }
    }

    private void ordena(ArrayList<Burrito> array){
        for (int i = 0; i < array.size(); i++) {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MoinActivity)getActivity()).getSupportActionBar().setTitle("Home");
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
        JSONArray json=response.optJSONArray("ordenes");
        ord1 = new ArrayList<>();
        ord2 = new ArrayList<>();
        try {

            ArrayList<Burrito> burros = new ArrayList<>(),
            bur2 = new ArrayList<>();
            ArrayList<Integer> cant1 = new ArrayList<>(),cant2 = new ArrayList<>();
            int folio = 0,fol2 = 0;
            String fecha = "";
            int i = 0;

            while (i != json.length()){

                do{
                    JSONObject jso=null;
                    jso=json.getJSONObject(i);

                    folio = jso.optInt("Folio");
                    cant1.add(jso.optInt("Catidad"));
                    fecha = jso.optString("fecha");

                    burr = new Burrito(jso.optInt("BId"),jso.optString("BNombre"),jso.optString("BDescripcion"),jso.optInt("BCategoria")
                            ,jso.optInt("BStack"),jso.optInt("BPopularidad"),jso.optDouble("BPrecio"));

                    burros.add(burr);

                    i++;
                    try {
                        fol2 = json.getJSONObject(i).optInt("Folio");
                    }catch (Exception e){
                        fol2++;
                    }

                }while( folio == fol2 );
                ord1.add(new Orden(folio,fecha,burros,cant1));
                burros = new ArrayList<>();
                cant1 = new ArrayList<>();
            }

            json = response.optJSONArray("ordenes2");
            for (i = 0;i < json.length();i++){
                do{
                    JSONObject jso=null;
                    jso=json.getJSONObject(i);

                    folio = jso.optInt("Folio");
                    cant2.add(jso.optInt("Catidad"));
                    fecha = jso.optString("fecha");

                    burr = new Burrito(jso.optInt("BId"),jso.optString("BNombre"),jso.optString("BDescripcion"),jso.optInt("BCategoria")
                            ,jso.optInt("BStack"),jso.optInt("BPopularidad"),jso.optDouble("BPrecio"));

                    bur2.add(burr);

                    i++;
                    try {
                        fol2 = json.getJSONObject(i).optInt("Folio");
                    }catch (Exception e){
                        fol2++;
                    }

                }while( folio == fol2 );
                ord2.add(new Orden(folio,fecha,bur2,cant2));
                bur2 = new ArrayList<>();
                cant2 = new ArrayList<>();
            }
            //UsuariosAdapter adapter=new UsuariosAdapter(listaUsuarios);
            //recyclerUsuarios.setAdapter(adapter);
            //ordena(burros);
            load();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
        }
    }
}
