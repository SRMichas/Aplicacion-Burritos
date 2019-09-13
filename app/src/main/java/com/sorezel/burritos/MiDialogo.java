package com.sorezel.burritos;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.sorezel.burritos.Fragments.VacioFragment;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.Objetos.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MiDialogo extends DialogFragment {

    private int canti = 0;
    private View v;
    private boolean salir = false,salir2,pasa;
    private String ip;
    private JsonObjectRequest jsonobj;
    private int folio;
    private StringRequest sreq;
    private SharedPreferences sh;
    private Dialog dialog;
    private String respuesta;
    private ArrayList<Burrito> arr;
    private ArrayList<Integer> cantis;
    private int idburro;

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        /* Set the width of the dialog proportional to 75% of the screen width */
        window.setLayout((int) (size.x * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
        SharedPreferences sh = getActivity().getSharedPreferences("Usuario",0);
        pasa = sh.getBoolean("bd",false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        v = null;
        int layout = -1;
        Bundle bun = null;
        sh = getActivity().getSharedPreferences("Usuario",0);
        ip = getString(R.string.ip);
        if(getArguments() != null){
            bun = getArguments();
            layout = getArguments().getInt("tipo");
            salir2 = bun.getBoolean("prin");
            arr = (ArrayList<Burrito>) bun.getSerializable("carrito");
            cantis = bun.getIntegerArrayList("cantis");
            idburro = bun.getInt("bid");
        }
        dialog = getDialog();

        switch (layout){
            case R.layout.dialog_tipo_pago:
                v = inflater.inflate(layout,container,false);
                final Button dialogButton = v.findViewById(R.id.dia_btn_aceptar);
                if( pasa ){
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //dialog.setContentView(R.layout.dialog_espera);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.dialog_espera);
                            //ordena();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    /*try {
                                        Thread.sleep(1800);
                                    } catch (Exception e) {
                                    }*/
                                    //dialog.dismiss();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //showDialog(R.layout.dialog_ord_succ,false);
                                            dialog.setCancelable(true);
                                            salir = true;
                                            dialog.setContentView(R.layout.dialog_ord_succ);
                                            //View vi = inflater.inflate(R.layout.dialog_ord_succ,container,false);
                                            Button b = dialog.findViewById(R.id.btn_ok);
                                            b.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    salir = false;
                                                    dialog.dismiss();
                                                    getActivity().onBackPressed();
                                                }
                                            });
                                        }
                                    });
                                }
                            }).start();
                        }
                    });
                }else{
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.setContentView(R.layout.dialog_espera);
                            dialog.setCancelable(false);
                            String url = ip + "ConsultaUltOrd.php";
                            jsonobj = new JsonObjectRequest(0, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    JSONArray jsonar = response.optJSONArray("OrdFolio");
                                    try {
                                        folio = jsonar.getJSONObject(0).optInt("Folio");
                                        insertaORD();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
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
                                VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonobj);
                        }
                    });
                }

                break;
            case R.layout.dialog_ord_succ:
                v = inflater.inflate(layout,container,false);
                break;
            case R.layout.dialog_ord_succ2:
                v = inflater.inflate(layout,container,false);
                break;
            case R.layout.dialog_canti:
                v = inflater.inflate(layout,container,false);
                final TextView cant = v.findViewById(R.id.txv_dia_cant),
                        menos = v.findViewById(R.id.dia_menos),
                        mas = v.findViewById(R.id.dia_mas);
                Button button = v.findViewById(R.id.btn_dia_acep);
                menos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        canti--;
                        if(canti > 0){
                            cant.setText(""+canti);
                        }else {
                            canti = 0;
                            cant.setText(""+canti);
                        }

                    }
                });
                mas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        canti++;
                        if (canti < 99) {
                            cant.setText("" + canti);
                        } else {
                            canti = 0;
                            cant.setText(""+canti);
                        }

                    }
                });
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.dialog_espera);
                            if( pasa ){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1800);
                                        }catch (Exception e) {}
                                        //dialog.dismiss();
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //showDialog(R.layout.dialog_ord_succ2,false);
                                                dialog.setCancelable(true);
                                                salir = true;
                                                dialog.setContentView(R.layout.dialog_ord_succ2);
                                                //View vi = inflater.inflate(R.layout.dialog_ord_succ,container,false);
                                                Button b = dialog.findViewById(R.id.btn_ok);
                                                b.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        salir = false;
                                                        dialog.dismiss();
                                                        if(salir2)
                                                            getActivity().onBackPressed();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }).start();
                            }else{
                                insertaCarrito();
                            }

                        }
                    });


                break;
            case R.layout.dialog_espera:
                v = inflater.inflate(layout,container,false);
                getDialog().setCancelable(false);
                break;
            case R.layout.dialog_calificar:
                v = inflater.inflate(layout,container,false);
                final RatingBar rating = v.findViewById(R.id.rb_rate);
                final EditText edtComen = v.findViewById(R.id.edt_comen);
                final TextView tv = v.findViewById(R.id.progeso);
                Button bu = v.findViewById(R.id.btn_cal);
                bu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv.setText(""+rating.getRating());
                        califica(rating.getRating(),edtComen.getText().toString());
                    }
                });
                break;
        }
        return v;
    }

    @Override
    public void onDismiss( DialogInterface dialog) {
        super.onDismiss(dialog);
        if(salir){
            getActivity().onBackPressed();
        }

    }
    private void error(VolleyError error){
        if (error instanceof TimeoutError) {
            Toast.makeText(getContext(),"Tiempo Expirado",Toast.LENGTH_SHORT).show();
        }else if (error instanceof NoConnectionError){
            Toast.makeText(getContext(),"Error de Conexion",Toast.LENGTH_SHORT).show();
        }else if(error instanceof AuthFailureError){
            Toast.makeText(getContext(),"Error de Autorizacion",Toast.LENGTH_SHORT).show();
        }else if( error instanceof ServerError){
            Toast.makeText(getContext(),"Error de Servidor",Toast.LENGTH_SHORT).show();
        }else if( error instanceof NetworkError){
            Toast.makeText(getContext(),"Error de Red",Toast.LENGTH_SHORT).show();
        }else if( error instanceof ParseError){
            Toast.makeText(getContext(),"Error de Parseo",Toast.LENGTH_SHORT).show();
        }
    }
    private void insertaORD(){
        String url = ip + "RegistroOrden.php";
        sreq = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                respuesta = response;
                Toast.makeText(getContext(),respuesta,Toast.LENGTH_SHORT).show();
                insertaDORD();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                folio++;
                Map<String,String> parametros=new HashMap<>();
                parametros.put("folio",""+folio);
                parametros.put("uid",""+sh.getInt("ID",-1));
                parametros.put("edo",""+3);

                return parametros;
            }
        };
        sreq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(sreq);
    }
    private void insertaDORD(){
        String url = ip + "RegistroOrdenD.php";
        sreq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                eliminaCarrito();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<>();
                JSONArray jarray = new JSONArray();
                Burrito bur;
                JSONObject jobj;
                for (int i = 0; i < arr.size(); i++) {
                    jobj = new JSONObject();
                    bur = arr.get(i);
                    try {
                        jobj.put("folio",folio);
                        jobj.put("bid",bur.getId());
                        jobj.put("cant",cantis.get(i));
                    } catch (JSONException e) {
                        Log.v("error exceptiion:",""+arr.get(i).getId());
                        e.printStackTrace();
                    }
                    jarray.put(jobj);
                }
                parametros.put("params",jarray.toString());


                return parametros;
            }
        };


        /*dialog.setContentView(R.layout.dialog_ord_succ);
                dialog.setCancelable(true);
                TextView tv = dialog.findViewById(R.id.txv_dia_no_ord);
                tv.setText(response.toString());
                Button b = dialog.findViewById(R.id.btn_ok);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        salir = false;
                        dialog.dismiss();
                        getActivity().onBackPressed();
                    }
                });*/
        sreq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(sreq);
    }
    private void insertaCarrito(){
        String url = ip + "RegistroCarrito.php";
        StringRequest resques = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.setContentView(R.layout.dialog_ord_succ2);
                dialog.setCancelable(true);
                salir = true;
                //View vi = inflater.inflate(R.layout.dialog_ord_succ,container,false);
                Button b = dialog.findViewById(R.id.btn_ok);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        salir = false;
                        dialog.dismiss();
                        if(salir2)
                            getActivity().onBackPressed();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("uid",""+sh.getInt("ID",-1));
                params.put("bid",""+idburro);
                params.put("cant",""+canti);
                return params;
            }
        };
        resques.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(resques);
    }
    private void insertaFav(){
        String url = ip + "RegistroFavorito.php";
        StringRequest resques = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar.make(getView(),response.toString(),Snackbar.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("uid",""+sh.getInt("ID",-1));
                params.put("bid",""+idburro);
                return params;
            }
        };
        resques.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(resques);
    }
    private void eliminaCarrito(){
        String url = ip + "BorrarCarrito.php?UsId="+sh.getInt("ID",-1);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.setContentView(R.layout.dialog_ord_succ);
                dialog.setCancelable(true);
                TextView tv = dialog.findViewById(R.id.txv_dia_no_ord);
                tv.setText(respuesta);
                Bundle bun = new Bundle();
                bun.putString("txt","No has seleccionado ningun burrito, trata de ordenar uno :)");
                bun.putInt("img",R.drawable.ic_menu_share);
                VacioFragment vf = new VacioFragment();
                vf.setArguments(bun);
                getFragmentManager().beginTransaction().replace(R.id.frag_cont,vf,"Vacio").addToBackStack("Vacio").commit();
                Button b = dialog.findViewById(R.id.btn_ok);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        salir = false;
                        dialog.dismiss();
                        getActivity().onBackPressed();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request);
    }
    private void califica(final float cal, final String comen){
        String url = ip + "RegistroCalifica.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if( response.equals("JALO")){
                    RatingBar bar = getActivity().findViewById(R.id.ratingBar);
                    bar.setRating(cal);
                    Drawable drawable = bar.getProgressDrawable();
                    drawable.setColorFilter(getActivity().getResources().getColor(R.color.colorG7), PorterDuff.Mode.SRC_ATOP);
                    dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("uid",String.valueOf(sh.getInt("ID",-1)));
                params.put("bid",String.valueOf(idburro));
                params.put("cali",String.valueOf(cal));
                params.put("comen",comen);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request);
    }
}
