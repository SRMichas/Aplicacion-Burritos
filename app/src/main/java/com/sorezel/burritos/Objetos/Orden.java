package com.sorezel.burritos.Objetos;

import java.io.Serializable;
import java.util.ArrayList;

public class Orden implements Serializable {

    private int folio;
    private String fecha;
    private ArrayList<Burrito> burros;
    private ArrayList<Integer> cantidad;

    public Orden(int f,String fe,ArrayList<Burrito> b,ArrayList<Integer> c){
        folio = f;
        fecha = fe;
        burros = b;
        cantidad = c;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Burrito> getBurros() {
        return burros;
    }

    public void setBurros(ArrayList<Burrito> burros) {
        this.burros = burros;
    }

    public ArrayList<Integer> getCantidad() {
        return cantidad;
    }

    public void setCantidad(ArrayList<Integer> cantidad) {
        this.cantidad = cantidad;
    }

    public double total(){
        double total = 0;
        for (int i = 0; i < burros.size(); i++) {
            total += (int) (burros.get(i).getPrecio() * cantidad.get(i));
        }
        return total;
    }
}
