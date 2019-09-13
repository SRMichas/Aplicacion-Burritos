package com.sorezel.burritos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.sorezel.burritos.Fragments.VacioFragment;

public class Imagenes {
    public static int[] FOTOS = {
            R.drawable.b_cochinita,
            R.drawable.b_machaca_huevo,
            R.drawable.b_pollo,
            R.drawable.b_picadillo,
            R.drawable.b_bistec,
            R.drawable.b_chorizo,
            R.drawable.b_frigol,
            R.drawable.b_chicharron,
            R.drawable.b_arrachera,
            R.drawable.b_pollo_phil,
            R.drawable.b_vegetariano,
            R.drawable.b_jamon,
            R.drawable.b_teoti,
            R.drawable.b_pescado,
            R.drawable.b_asada};

    public static Fragment errorFragment(int type){
        VacioFragment fragment = new VacioFragment();
        Bundle bun = new Bundle();
        switch (type){
            case 1: //Tiempo Agotado
                bun.putString("txt","UPS!!! Tiempo agotado");
                bun.putInt("img",R.drawable.ic_delete);
                break;
            case 2: //Sin Conexion
                bun.putString("txt","No hay conexion");
                bun.putInt("img",R.drawable.ic_delete);
                break;
            case 3: //Auth Failure
                bun.putString("txt","Auth Failure");
                bun.putInt("img",R.drawable.ic_delete);
                break;
            case 4: //Server Error
                bun.putString("txt","UPS!!! Error en el Servidor");
                bun.putInt("img",R.drawable.ic_delete);
                break;
            case 5: //Network Error
                bun.putString("txt","UPS!!! Error de Red");
                bun.putInt("img",R.drawable.ic_delete);
                break;
            case 6: //Parse Error
                bun.putString("txt","UPS!!! Error de Parseo");
                bun.putInt("img",R.drawable.ic_delete);
                break;
        }
        fragment.setArguments(bun);
        return fragment;
    }
    public static Fragment carVacio(){
        Bundle b = new Bundle();
        Fragment fragment = new VacioFragment();
        b.putString("txt","No has seleccionado ningun burrito, trata de ordenar uno :)");
        b.putInt("img",R.drawable.ic_menu_share);
        fragment.setArguments(b);
        return fragment;
    }

    public static Fragment favVacio() {
        Bundle b = new Bundle();
        Fragment fragment = new VacioFragment();
        b.putString("txt","No tienes ningun favorito, trata de añadir uno :)");
        b.putInt("img",R.drawable.ic_menu_share);
        fragment.setArguments(b);
        return fragment;
    }
}
