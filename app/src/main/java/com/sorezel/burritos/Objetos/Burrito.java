package com.sorezel.burritos.Objetos;

import java.io.Serializable;

public class Burrito implements Serializable {

    private int id,Stack,Categoria,Popularidad;
    private String Nombre, Descripcion;
    private double Precio;

    public Burrito(int id,String nombre, String descipcion, int categoria,  int stack,int popularidad,  double precio) {
        this.id = id;
        Stack = stack;
        Categoria = categoria;
        Popularidad = popularidad;
        Nombre = nombre;
        Descripcion = descipcion;
        Precio = precio;
    }

    public Burrito(int id, String nombre,int stack,double precio, String descipcion) {
        this.id = id;
        Stack = stack;
        Nombre = nombre;
        Descripcion = descipcion;
        Precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStack() {
        return Stack;
    }

    public void setStack(int stack) {
        Stack = stack;
    }

    public int getCategoria() {
        return Categoria;
    }

    public void setCategoria(int categoria) {
        Categoria = categoria;
    }

    public int getPopularidad() {
        return Popularidad;
    }

    public void setPopularidad(int popularidad) {
        Popularidad = popularidad;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descipcion) {
        Descripcion = descipcion;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }
}
