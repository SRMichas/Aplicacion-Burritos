package com.sorezel.burritos.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.sorezel.burritos.BD.LocalHelper;
import com.sorezel.burritos.DetalleActivity;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.Objetos.Usuario;
import com.sorezel.burritos.Objetos.VolleySingleton;
import com.sorezel.burritos.R;
import com.sorezel.burritos.UsuarioActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LogInFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{


    private JsonObjectRequest jsonobj;
    private Usuario us;
    private Button b;
    private TextInputEditText txvCorr,txvPass;
    private TextView txvMsgError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((UsuarioActivity)getActivity()).getSupportActionBar().setTitle("Iniciar Sesión");
        View v = inflater.inflate(R.layout.fragment_login,container,false);
        b = v.findViewById(R.id.btn_log);
        txvCorr = v.findViewById(R.id.ipt_correo);
        txvPass = v.findViewById(R.id.ipt_contra);
        txvMsgError = v.findViewById(R.id.txv_Error);
        final SharedPreferences sh = getActivity().getSharedPreferences("usuario",0);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m1 = txvCorr.getText().toString(),m2 = txvPass.getText().toString();
                //Toast.makeText(getContext(),m1+"\n"+m2,Toast.LENGTH_SHORT).show();
                if(sh.getBoolean("bd",false)){
                    us = LocalHelper.retUser2(m1,m2);
                    ingresa();
                }else{
                    cargarWS(m1,m2);
                }
            }
        });
        TextView link = v.findViewById(R.id.txv_link);
        link.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.cont_us,new RegistroFragment()).commit();
            }
        });
        return v;
    }
    private void ingresa(){
        if( us != null){
            Snackbar.make(b,"Logeado como "+us.nombreCompleto(),Snackbar.LENGTH_SHORT).show();
            Bundle b = new Bundle();
            PerfilFragment pf = new PerfilFragment();
            b.putSerializable("us",us);
            pf.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.cont_us,pf).commit();
            SharedPreferences s = getContext().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor e = s.edit();
            e.putInt("ID",us.getId());
            e.putString("Nombre",us.getNombre());
            e.putString("AM",us.getApellidoMaterno());
            e.putString("AP",us.getApellidoPaterno());
            e.putString("Correo",us.getCorreo());
            e.putString("Contrasena",us.getContraseña());
            e.putString("nick",us.getNickName());
            e.apply();
            e.commit();
        }else{
            txvMsgError.setVisibility(View.VISIBLE);
            txvCorr.setText("");
            txvPass.setText("");
        }
    }
    private void cargarWS(String corr,String pass){
        String ip = getString(R.string.ip);
        String url = ip+"ConsultaUsuario2.php?Uscorreo="+corr+"&Uscontrasena="+pass;
        jsonobj = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonobj);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getActivity(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Burrito burr =null;
        JSONArray json=response.optJSONArray("usuario");

        try {
                JSONObject jso=null;
                jso=json.getJSONObject(0);

                us = new Usuario(jso.optInt("UsID"),jso.optString("Usnombre"),jso.optString("UsapeMat")
                        ,jso.optString("UsapePat"),jso.optString("Uscorreo"),jso.optString("Uscontrasena")
                        ,jso.optString("UsNickName"));
                ingresa();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
        }
    }
}
