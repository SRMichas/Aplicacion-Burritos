package com.sorezel.burritos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sorezel.burritos.BD.LocalHelper;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.Objetos.VolleySingleton;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetalleActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    private CollapsingToolbarLayout ctbl;
    private TextView txvNombre,txvCant,txvDisp,txvPrecio,txvDesc;
    private RatingBar rbar;
    private Burrito burr;
    private ImageView foto,fabFav;
    private int cont = 0,id;
    private JsonObjectRequest jsonobj;
    private ArrayList<Burrito> burros;
    boolean tipo = false;
    private FloatingActionButton fab,fabr;//,fabFav;
    private String ip;
    private SharedPreferences sh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        ip = getString(R.string.ip);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

         burr = (Burrito) getIntent().getExtras().getSerializable("burro");

        
        final Toolbar toolbar = (Toolbar) findViewById(R.id.det_tool);
        toolbar.setTitle("Detalle Burrito");
        //toolbar.inflateMenu(R.menu.menu_main);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ctbl = findViewById(R.id.collap_tool);
        ctbl.setTitle(" ");
        //ctbl.setContentScrimColor(getResources().getColor(R.color.base_color_theme_new));
        //ctbl.setStatusBarScrimColor(getResources().getColor(R.color.base_color_theme_new));
        txvNombre = findViewById(R.id.txv_nom_bur);
        txvNombre.setText(burr.getNombre());
        txvCant = findViewById(R.id.txv_det_cant);
        txvDisp = findViewById(R.id.txv_det_dispo);
        int dis = burr.getStack();
        if(dis < 4 )
            txvDisp.setTextColor(Color.RED);
        else if( dis < 9)
            txvDisp.setTextColor(Color.YELLOW);

        txvDisp.setText(""+burr.getStack());
        rbar = findViewById(R.id.ratingBar);
        rbar.setRating(burr.getPopularidad());
        txvPrecio = findViewById(R.id.txv_det_precio);
        txvPrecio.setText("$"+(int)burr.getPrecio());
        txvDesc = findViewById(R.id.txv_det_info);
        txvDesc.setText(burr.getDescripcion());
        foto = findViewById(R.id.img_foto);
        try {
            foto.setImageResource(Imagenes.FOTOS[burr.getId()-1]);
        }catch (Exception e){
            foto.setImageResource(R.drawable.burrito);
        }

        fab = findViewById(R.id.fab);
        fabFav = findViewById(R.id.img_fav);

        sh = getSharedPreferences("Usuario",0);
        id = sh.getInt("ID",-1);
        final boolean bd = sh.getBoolean("bd",false);
        if(bd){
            if(LocalHelper.esFavorito(id,burr.getId())){
                fabFav.setImageDrawable(changeDrawableColor(DetalleActivity.this,R.drawable.ic_favorite,Color.WHITE));
            }
        }else{
            tipo = true;
            cargarWS();
            /*if(buscarFav())
                fabFav.setImageDrawable(changeDrawableColor(DetalleActivity.this,R.drawable.ic_favorite,Color.WHITE));*/
        }



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( id != -1 ){
                    //String[] a = {""+1,""+2,""+3};
                    //LocalHelper.insertaCarrito(a,DetalleActivity.this);
                    //LocalHelper.busqBurrCarr3(DetalleActivity.this,0);
                    if(bd){
                        if(!LocalHelper.busqBurrCarr(id,burr.getId())){
                            if( pasa() ){
                                String[] a = {""+id,""+burr.getId(),""+Integer.parseInt(txvCant.getText().toString())};
                                LocalHelper.insertaCarrito(a);
                                Snackbar.make(fab,"Burrito añadido al carrito",Snackbar.LENGTH_SHORT).show();
                                txvCant.setBackgroundColor(Color.parseColor("#C0BCBC"));
                            }else{
                                Snackbar.make(fab,"Debe seleccionar almenos un burrito",Snackbar.LENGTH_SHORT).show();
                                txvCant.setBackgroundColor(Color.RED);
                            }
                        }else{
                            Snackbar.make(fab,"YA esta añadido",Snackbar.LENGTH_SHORT).show();
                        }
                    }else{
                        /*if(!buscarFav()){
                            if( pasa() ){
                                String[] a = {""+id,""+burr.getId(),""+Integer.parseInt(txvCant.getText().toString())};
                                //LocalHelper.insertaCarrito(a);
                                //Snackbar.make(fab,"Burrito añadido al carrito",Snackbar.LENGTH_SHORT).show();
                                fav(3);
                            }else{
                                //Snackbar.make(fab,"No es posible realizar esta acción",Snackbar.LENGTH_SHORT).show();
                                //fav(4);
                            }
                        }else{
                            Snackbar.make(fab,"YA esta añadido",Snackbar.LENGTH_SHORT).show();
                        }*/
                        if( pasa() ){
                            fab.setEnabled(false);
                            fav(3);
                        }else{
                            Snackbar.make(fab,"Debe seleccionar almenos 1 burrito",Snackbar.LENGTH_SHORT).show();
                        }

                    }
                }else{
                    Snackbar.make(fab,"No es posible realizar esta acción,inicié sesión",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        fabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( id != -1 ){
                    if(bd){
                        if(!LocalHelper.busqBurrFav(id,burr.getId())){
                            String[] data = {""+id,""+burr.getId()};
                            LocalHelper.insertaFavorito(data);
                            fabFav.setImageDrawable(changeDrawableColor(DetalleActivity.this,R.drawable.ic_favorite,getResources().getColor(R.color.colorG7)));
                            Snackbar.make(fab,"Añadido a Favoritos",Snackbar.LENGTH_SHORT).show();
                        }else{
                            String[] data = {""+id,""+burr.getId()};
                            LocalHelper.borraFavorito(data);
                            fabFav.setImageDrawable(changeDrawableColor(DetalleActivity.this,R.drawable.ic_favorite,Color.WHITE));
                            Snackbar.make(fab,"Eliminado de Favoritos",Snackbar.LENGTH_SHORT).show();
                        }
                    }else{
                        /*if(!buscarFav()){
                            String[] data = {""+id,""+burr.getId()};
                           // LocalHelper.insertaFavorito(data);
                            //fabFav.setImageDrawable(changeDrawableColor(DetalleActivity.this,R.drawable.ic_favorite,Color.WHITE));
                           // Snackbar.make(fab,"Añadido a Favoritos",Snackbar.LENGTH_SHORT).show();
                            fav(1);
                        }else{
                            String[] data = {""+id,""+burr.getId()};
                            //LocalHelper.borraFavorito(data);
                            //fabFav.setImageDrawable(changeDrawableColor(DetalleActivity.this,R.drawable.ic_favorite,Color.BLACK));
                            //Snackbar.make(fab,"Eliminado de Favoritos",Snackbar.LENGTH_SHORT).show();
                            fav(2);
                        }*/
                        fabFav.setEnabled(false);
                        fav(1);
                    }

                }else{
                    Snackbar.make(fab,"No es posible realizar esta acción,inicié sesión",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        fabr = findViewById(R.id.fab_cal);
        fabr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( id != -1 ){

                    Bundle bun = new Bundle();
                    MiDialogo dialogo = new MiDialogo();
                    bun.putInt("tipo",R.layout.dialog_calificar);
                    bun.putInt("bid",burr.getId());
                    dialogo.setArguments(bun);
                    dialogo.show(getSupportFragmentManager(),"Dialogo");

                    if( bd ){

                    }else{

                    }
                }else
                    Snackbar.make(fab,"No es posible realizar esta acción,inicié sesión",Snackbar.LENGTH_SHORT).show();
            }
        });


    }

    private Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        //mDrawable.setBounds(0,0,10,10);

        return mDrawable;
    }
    private void fav(final int type){
        String url = ip;
        int metodo = 0;
        switch (type){
            case 1: //Añadir Favorito
                url += "RegistroFavorito.php";
                metodo = Request.Method.POST;
                break;
            case 2: //Quitar Favorito
                url += "BorrarFavoritos.php";
                metodo = Request.Method.GET;
                break;
            case 3: //Añadir Carrito
                url += "RegistroCarrito.php";
                metodo = Request.Method.POST;
                break;
            case 4: //Quitar Carrito
                url += "BorrarCarritoT.php";
                metodo = Request.Method.GET;
                break;
        }
        StringRequest srequest = new StringRequest(metodo, url, new Response.Listener<String>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(String response) {
                Snackbar.make(txvDesc,response.toString(),Snackbar.LENGTH_SHORT).show();
                switch (response.toString()){
                    case "Burrito Añadido a Favoritos":
                        //fabFav.setImageDrawable(changeDrawableColor(DetalleActivity.this,R.drawable.ic_favorite,getResources().getColor(R.color.colorG7)));
                        fabFav.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.colorG7), android.graphics.PorterDuff.Mode.SRC_IN);
                        fabFav.setEnabled(true);
                        break;
                    case "Burrito Eliminado de Favoritos":
                        //fabFav.setImageDrawable(changeDrawableColor(DetalleActivity.this,R.drawable.ic_favorite,Color.WHITE));
                        fabFav.setColorFilter(ContextCompat.getColor(DetalleActivity.this, Color.WHITE), android.graphics.PorterDuff.Mode.SRC_IN);
                        fabFav.setEnabled(true);
                        break;
                    case "Burrito Añadido al Carrito":
                        fab.setEnabled(true);
                        break;
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
                params.put("uid",""+sh.getInt("ID",-1));
                params.put("bid",""+burr.getId());
                params.put("cant",""+cont);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(srequest);
    }
    private void error(VolleyError error){
        if (error instanceof TimeoutError) {
            Snackbar.make(txvDesc,"",Snackbar.LENGTH_SHORT).show();
        }else if (error instanceof NoConnectionError){
            Snackbar.make(txvDesc,"",Snackbar.LENGTH_SHORT).show();
        }else if(error instanceof AuthFailureError){
            Snackbar.make(txvDesc,"",Snackbar.LENGTH_SHORT).show();
        }else if( error instanceof ServerError){
            Snackbar.make(txvDesc,"",Snackbar.LENGTH_SHORT).show();
        }else if( error instanceof NetworkError){
            Snackbar.make(txvDesc,"",Snackbar.LENGTH_SHORT).show();
        }else if( error instanceof ParseError){
            Snackbar.make(txvDesc,"",Snackbar.LENGTH_SHORT).show();
        }
    }
    public void aumenta(View v){
        cont++;
        if(cont < 10)
            txvCant.setText("0"+cont);
        else
            txvCant.setText(""+cont);
    }
    public void disminuye(View v){
        cont--;
        if(cont >= 0){
            if(cont < 10)
                txvCant.setText("0"+cont);
            else
                txvCant.setText(""+cont);
        }else
            cont = 0;
    }
    private boolean pasa(){
        return Integer.parseInt(txvCant.getText().toString()) > 0;
    }
    private boolean buscarFav(){
        for (int i = 0; i < burros.size(); i++)
            if(burros.get(i).getId() == burr.getId())
                //Toast.makeText(this,"SI esta",Toast.LENGTH_SHORT).show();
                return true;
        return false;
    }
    private float corrige_decimal(double d){
        float val = 0;
        String pre = String.valueOf(d),pos = ""+pre.charAt(0), str = "";

        for (int i = 2; i < pre.length(); i++) {
            str += pre.charAt(i);
        }

        switch (str){
            case "0": case "5":
                try{
                    pos += str;
                    val = Float.parseFloat(pos);
                    return val;
                }catch (Exception e){

                }
                break;
                default:
                    double de = 0.5 - Float.parseFloat("0."+str);
                    if( de < 0 ){

                    }else{
                        double de2 = de + Float.parseFloat("0."+str);
                        val =  Float.parseFloat(pos) + (float)de2;
                    }
                    return val;
        }
        return val;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
    }
    private void cargarWS(){
        String ip = getString(R.string.ip);
        String url = ip+"ConsultaDetalle.php?uid="+id+"&bid="+burr.getId();
        jsonobj = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonobj);
    }

    @Override
    public void onResponse(JSONObject response) {

        if(tipo){
            try {
                JSONObject json=response.optJSONObject("calificado"),
                        json2 = response.optJSONObject("esfav"),
                        json3 = response.optJSONObject("cali");
                if( json.optInt("edo") == 1){
                    double cal = json.optDouble("cal");
                    rbar.setRating((float)cal);
                    Drawable drawable = rbar.getProgressDrawable();
                    drawable.setColorFilter(this.getResources().getColor(R.color.colorG7), PorterDuff.Mode.SRC_ATOP);
                }

                if( json2.optInt("edo") == 1){
                    fabFav.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.colorG7), android.graphics.PorterDuff.Mode.SRC_IN);
                }

                if( json3.optInt("edo") == 1){
                    double dou = json3.getDouble("prom");
                    rbar.setRating((float) dou);
                    Drawable drawable = rbar.getProgressDrawable();
                    drawable.setColorFilter(this.getResources().getColor(R.color.colorG7), PorterDuff.Mode.SRC_ATOP);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No se ha podido establecer conexión con el servidor" +
                        " "+response, Toast.LENGTH_LONG).show();
            }
        }

    }
}
