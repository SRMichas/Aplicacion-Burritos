package com.sorezel.burritos.Fragments;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.sorezel.burritos.BD.LocalHelper;
import com.sorezel.burritos.Objetos.Usuario;
import com.sorezel.burritos.Objetos.VolleySingleton;
import com.sorezel.burritos.R;
import com.sorezel.burritos.UsuarioActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RegistroFragment extends Fragment {


    TextInputEditText edtNo,edtAM,edtAP,edtNi,edtC,edtP,edtP2;
    private StringRequest stringRequest;
    private JsonObjectRequest json;
    private Usuario us;
    private int id;
    SharedPreferences sh;
    SharedPreferences.Editor ed;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        ((UsuarioActivity)getActivity()).getSupportActionBar().setTitle("Registro");
        View v = inflater.inflate(R.layout.fragment_registrarse,container,false);
        final Button b = v.findViewById(R.id.btn_registrar);
        edtNo = v.findViewById(R.id.edt_nombre);
        edtAM = v.findViewById(R.id.edt_ape_m);
        edtAP = v.findViewById(R.id.edt_ape_p);
        edtNi = v.findViewById(R.id.edt_nick);
        edtC = v.findViewById(R.id.edt_correo);
        edtP = v.findViewById(R.id.edt_pass);
        edtP2 = v.findViewById(R.id.edt_pass2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pasa()) {
                    sh = getContext().getSharedPreferences("Usuario",0);
                    ed = sh.edit();

                    if(sh.getBoolean("bd",false)){
                        String m = edtNo.getText().toString()+" "+edtAM.getText().toString()+" "+edtAP.getText().toString()+" "+edtNi.getText().toString()
                                +" "+edtC.getText().toString()+" "+edtP.getText().toString()+" "+edtP2.getText().toString();
                        int id = LocalHelper.retUltimoIdUsuario() + 1;
                        String nom =edtNo.getText().toString(),
                                apeM = edtAM.getText().toString(),
                                apeP = edtAP.getText().toString(),
                                Ni = edtNi.getText().toString(),
                                corr = edtC.getText().toString(),
                                con = edtP.getText().toString();
                        us = new Usuario(id,nom,apeM,apeP,corr,con,Ni);
                        ed.putInt("ID",id);
                        ed.putString("Nombre",nom);
                        ed.putString("AM",apeM);
                        ed.putString("AP",apeP);
                        ed.putString("Correo",corr);
                        ed.putString("Contrasena",con);
                        ed.apply();
                        Snackbar.make(b,m, Snackbar.LENGTH_LONG).show();
                        String[] a ={""+id,nom,apeM,apeP,corr,con,Ni};
                        LocalHelper.registraUsuario(a);
                        Bundle b = new Bundle();
                        b.putSerializable("us",us);
                        PerfilFragment pf = new PerfilFragment();
                        pf.setArguments(b);
                        getFragmentManager().beginTransaction().replace(R.id.cont_us, pf).commit();
                    }else{
                        cargaID();
                    }


                }
            }
        });
        return v;
    }

    private boolean pasa(){
        boolean error = true;
        if(edtNo.getText().toString().isEmpty()){
            edtNo.setError("No debe estar vacio");
            edtNo.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_red_dark),  PorterDuff.Mode.SRC_ATOP);
            error = false;
        }
        if(edtAP.getText().toString().isEmpty()){
            edtAP.setError("No debe estar vacio");
            edtAP.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_red_dark),  PorterDuff.Mode.SRC_ATOP);
            error = false;
        }
        if(edtNi.getText().toString().isEmpty()){
            edtNi.setError("No debe estar vacio");
            edtNi.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_red_dark),  PorterDuff.Mode.SRC_ATOP);
            error = false;
        }
        if(edtC.getText().toString().isEmpty()){
            edtC.setError("No debe estar vacio");
            edtC.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_red_dark),  PorterDuff.Mode.SRC_ATOP);
            error = false;
        }
        if(!edtC.getText().toString().contains("@")){
            edtC.setError("correo electronico invalido");
            edtC.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_red_dark),  PorterDuff.Mode.SRC_ATOP);
            error = false;
        }
        if(edtP.getText().toString().isEmpty()){
            edtP.setError("No debe estar vacio");
            edtP.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_red_dark),  PorterDuff.Mode.SRC_ATOP);
            error = false;
        }
        if(edtP2.getText().toString().isEmpty()){
            edtP2.setError("No debe estar vacio");
            edtP2.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_red_dark),  PorterDuff.Mode.SRC_ATOP);
            error = false;
        }
        if(!edtP.getText().toString().equals(edtP2.getText().toString()) && error){
            edtP2.setError("Las contraseñas no concuerdarn");
            edtP2.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_red_dark),  PorterDuff.Mode.SRC_ATOP);
            error = false;
        }
        return error;
    }

    private void cargaID(){
        String ip = getString(R.string.ip), url = ip+"ConsultaUltUsuario.php";
        json = new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json=response.optJSONArray("usuario");
                try {
                    JSONObject jso=null;
                    jso=json.getJSONObject(0);
                    id = jso.optInt("UsId");
                    cargaWS();
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
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(json);
    }
    private void cargaWS(){
        String ip = getString(R.string.ip);
        String url=ip+"RegistroUsuario.php";


        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("Registrado")){
                    String nom =edtNo.getText().toString(),
                            apeM = edtAM.getText().toString(),
                            apeP = edtAP.getText().toString(),
                            Ni = edtNi.getText().toString(),
                            corr = edtC.getText().toString(),
                            con = edtP.getText().toString();
                    us = new Usuario(id,nom,apeM,apeP,corr,con,Ni);
                    ed.putInt("ID",id);
                    ed.putString("Nombre",nom);
                    ed.putString("AM",apeM);
                    ed.putString("AP",apeP);
                    ed.putString("Correo",corr);
                    ed.putString("Contrasena",con);
                    ed.apply();

                    Bundle b = new Bundle();
                    b.putSerializable("us",us);
                    PerfilFragment pf = new PerfilFragment();
                    pf.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.cont_us, pf).commit();
                    Toast.makeText(getContext(),"Se ha registrado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"No se ha registrado ",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                id++;
                String nom =edtNo.getText().toString(),
                        apeM = edtAM.getText().toString(),
                        apeP = edtAP.getText().toString(),
                        Ni = edtNi.getText().toString(),
                        corr = edtC.getText().toString(),
                        con = edtP.getText().toString();
                Map<String,String> parametros=new HashMap<>();
                parametros.put("id",""+id);
                parametros.put("nombre",nom);
                if(apeM.equals(""))
                    parametros.put("mat","");
                else
                    parametros.put("mat",apeM);
                parametros.put("pat",apeP);
                parametros.put("correo",corr);
                parametros.put("con",con);
                parametros.put("nick",Ni);

                return parametros;
            }
        };
        //request.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);

    }
    @Override
    public void onDetach() {
        super.onDetach();
        ((UsuarioActivity)getActivity()).getSupportActionBar().setTitle("Perfil");
    }
}
