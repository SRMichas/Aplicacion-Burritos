package com.sorezel.burritos.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sorezel.burritos.AdaptadorLista;
import com.sorezel.burritos.BD.LocalHelper;
import com.sorezel.burritos.MiDialogo;
import com.sorezel.burritos.MoinActivity;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PagarFragment extends Fragment {

    RecyclerView rv;
    TextView txvTotal,txvCant;
    ArrayList<Burrito>a;
    private static Context c ;
    AdaptadorLista al;
    int cont = 0,ID;
    SharedPreferences sh;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cont = 0;
        ((MoinActivity)getActivity()).getSupportActionBar().setTitle("Carrito");
        View v = inflater.inflate(R.layout.fragment_carrito,null);
        sh = getActivity().getSharedPreferences("Usuario",0);
        c = getActivity();
        rv = v.findViewById(R.id.rcv_orden);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        txvCant = v.findViewById(R.id.txv_cant_burr);
        txvTotal = v.findViewById(R.id.txv_total);
        Button boton = v.findViewById(R.id.btnPagar);

       if( getArguments() != null){
            Bundle b = getArguments();
            a = (ArrayList<Burrito>) b.getSerializable("burros");
            boolean alg = b.getBoolean("algo",false);
            al = new AdaptadorLista(a,2,this,getActivity());
            if( alg )
                al.canti = b.getIntegerArrayList("canti");
            else
                al.canti = LocalHelper.canti;
            rv.setLayoutManager(llm);
            rv.setAdapter(al);
            txvCant.setText(""+a.size());
            txvTotal.setText("$"+total());
            SharedPreferences sh = getContext().getSharedPreferences("Usuario",0);
            ID = sh.getInt("ID",-1);
        }
       boton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //respalda();
               //showDialog(R.layout.dialog_tipo_pago,true);
               Bundle b = new Bundle();
               MiDialogo dia = new MiDialogo();
               b.putInt("tipo",R.layout.dialog_tipo_pago);
               b.putBoolean("prin",true);
               b.putSerializable("carrito",a);
               b.putIntegerArrayList("cantis",al.canti);
               dia.setArguments(b);
               dia.show(getFragmentManager(),"Dialogo");
           }
       });

        return v;
    }
    private void showDialog(int layout,boolean algo){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        /* Set the width of the dialog proportional to 75% of the screen width */
        window.setLayout((int) (size.x * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
        //window.setGravity(Gravity.CENTER);

        if(algo){
            final Button dialogButton = (Button) dialog.findViewById(R.id.dia_btn_aceptar);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButton.setEnabled(false);
                    dialog.setContentView(R.layout.dialog_espera);
                    dialog.setCancelable(false);
                    //ordena();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1800);
                            }catch (Exception e) {}
                            dialog.dismiss();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showDialog(R.layout.dialog_ord_succ,false);
                                }
                            });
                        }
                    }).start();
               /* final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        dialog.setContentView(R.layout.dialog_ord_succ);
                        dialog.setCancelable(true);
                        Button dialogBOk = dialog.findViewById(R.id.btn_ok);
                        dialogBOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        t.cancel();
                    }
                },1500);*/

                    //dialog.dismiss();
                }
            });
        }else{
            dialog.setCancelable(false);
            Button dialogBOk = dialog.findViewById(R.id.btn_ok);
            //dialogBOk.setText("");
            dialogBOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    getActivity().onBackPressed();
                }
            });
        }
        dialog.show();
    }

    private void respalda(){
        for (int i = 0; i < a.size(); i++) {
            String[] data = {""+al.canti.get(i),""+ID,""+a.get(i).getId()};
            LocalHelper.udpCarrito(data);
        }
    }
    private void ordena(){
        int lastOrd = LocalHelper.ultimaOrden();
        if(lastOrd == -1) lastOrd = 0;
        lastOrd ++;
        Date currentTime = Calendar.getInstance().getTime();
        String[] data = {""+lastOrd,""+ID,""+3,currentTime.toString()};
        LocalHelper.insertaOrden(data);
        for (int i = 0; i < a.size(); i++) {
            Burrito bur = a.get(i);
            String[] data2 = {""+lastOrd,""+bur.getId(),""+al.canti.get(i)};

            LocalHelper.insertaOrdenD(data2);
        }
        LocalHelper.delCarrito(new String[]{""+ID},false);
        FragmentManager fgm = getFragmentManager();
        Bundle b = new Bundle();
        b.putString("txt","No has seleccionado ningun burrito, trata de ordenar uno :)");
        b.putInt("img",R.drawable.ic_menu_share);
        VacioFragment vf = new VacioFragment();
        vf.setArguments(b);
        fgm.beginTransaction().replace(R.id.frag_cont,vf).commit();
    }
    @Override
    public void onStop() {
        super.onStop();
        //cont++;
        //Toast.makeText(getContext(),"OnStop ejecutado #"+cont,Toast.LENGTH_SHORT).show();
        if (sh.getBoolean("bd", false)) {
            respalda();
        }else{

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MoinActivity)getActivity()).getSupportActionBar().setTitle("Home");
    }

    private int total(){
        int tot = 0;
        for (int i = 0; i < a.size(); i++) {
            tot += (int)(a.get(i).getPrecio() * al.canti.get(i));
        }
        //Snackbar.make(txvTotal,"entro pagar",Snackbar.LENGTH_SHORT).show();
        return tot;

    }
    public void mod(){

        txvTotal.setText("$"+total());
        txvCant.setText(""+a.size());

    }
    public static FragmentActivity instancia(){
        return null;
    }

}
