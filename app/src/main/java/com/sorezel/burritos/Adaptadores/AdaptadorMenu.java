package com.sorezel.burritos.Adaptadores;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sorezel.burritos.BD.LocalHelper;
import com.sorezel.burritos.DetalleActivity;
import com.sorezel.burritos.Imagenes;
import com.sorezel.burritos.MiDialogo;
import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.R;
import java.util.ArrayList;

public class AdaptadorMenu extends RecyclerView.Adapter<AdaptadorMenu.ViewH> {

    private boolean pasa;
    ArrayList<Burrito> list;
    Context con;
    int canti = 0;
    Activity act;
    FragmentManager fm;
    SharedPreferences sh;

    public AdaptadorMenu(ArrayList<Burrito> list,Activity a,FragmentManager frag) {
        this.list = list;
        act = a;
        fm = frag;
        sh = a.getSharedPreferences("Usuario",0);
        pasa = sh.getBoolean("bd",false);
    }

    @Override
    public ViewH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_mn_grid,parent,false);
        ViewH vh = new ViewH(v);
        con = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder( ViewH holder, int i) {
        Burrito b = list.get(i);
        holder.index = i;
        holder.txvN.setText(b.getNombre());
        try {
            holder.foto.setImageResource(Imagenes.FOTOS[b.getId()-1]);
        }catch (Exception e){
            holder.foto.setImageResource(R.drawable.burrito);
        }
    }
    private void showDialog(int layout,boolean algo){
        final Dialog dialog = new Dialog(con);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(layout);

        Window window = dialog.getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        /* Set the width of the dialog proportional to 75% of the screen width */
        window.setLayout((int) (size.x * 0.80), WindowManager.LayoutParams.WRAP_CONTENT);
        if(algo) {
            final TextView cant = dialog.findViewById(R.id.txv_dia_cant), menos = dialog.findViewById(R.id.dia_menos), mas = dialog.findViewById(R.id.dia_mas);
            Button button = dialog.findViewById(R.id.btn_dia_acep);
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
                    dialog.setContentView(R.layout.dialog_espera);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1800);
                            }catch (Exception e) {}
                            dialog.dismiss();
                            act.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showDialog(R.layout.dialog_ord_succ2,false);
                                }
                            });
                        }
                    }).start();
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
                }
            });
        }



        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewH extends RecyclerView.ViewHolder implements Toolbar.OnMenuItemClickListener,View.OnClickListener {

        Toolbar tb;
        int index;
        View view;
        ImageView foto;
        TextView txvN,txvD;
        public ViewH(View iv) {
            super(iv);
            view = iv;
            tb = iv.findViewById(R.id.modTool);
            tb.inflateMenu(R.menu.menu_prob);
            tb.setOnMenuItemClickListener(this);
            foto = iv.findViewById(R.id.icono_grid);
            foto.setOnClickListener(this);
            txvN = iv.findViewById(R.id.txv_nombre_grid);
            txvN.setOnClickListener(this);
            //txvD = iv.findViewById(R.id.txv_cant_grid);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();

            switch (id){
                case R.id.mn_det:
                    Intent in = new Intent(view.getContext(), DetalleActivity.class);
                    in.putExtra("burro",list.get(index));
                    view.getContext().startActivity(in);
                    break;
                case R.id.mn_fav:
                    int u = sh.getInt("ID",-1), bid = list.get(index).getId();
                    if( pasa ){
                        if(LocalHelper.esFavorito(u,bid)){
                            String [] data = {""+u,""+bid};
                            LocalHelper.insertaFavorito(data);
                        }
                    }else{

                    }
                    break;
                case R.id.mn_ordenar:
                    //showDialog(R.layout.dialog_canti,true);
                    Bundle b = new Bundle();
                    MiDialogo dia = new MiDialogo();
                    b.putInt("tipo",R.layout.dialog_canti);
                    b.putBoolean("prin",false);
                    b.putInt("bid",list.get(index).getId());
                    dia.setArguments(b);
                    dia.show(fm,"Dialogo");
                    break;
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            Intent in = new Intent(view.getContext(), DetalleActivity.class);
            in.putExtra("burro",list.get(index));
            view.getContext().startActivity(in);
        }
    }
}
