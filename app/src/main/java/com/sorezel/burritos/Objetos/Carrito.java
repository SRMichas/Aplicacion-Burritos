package com.sorezel.burritos.Objetos;

import java.io.Serializable;
import java.util.ArrayList;

public class Carrito implements Serializable {

    private ArrayList<Burrito> burros;
    private Usuario user;
    private int cantidad;

    public Carrito(ArrayList<Burrito> burros, Usuario user, int cantidad) {
        this.burros = burros;
        this.user = user;
        this.cantidad = cantidad;
    }

    public ArrayList<Burrito> getBurros() {
        return burros;
    }

    public void setBurros(ArrayList<Burrito> burros) {
        this.burros = burros;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
