package com.sorezel.burritos.Objetos;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int id;
    private String Nombre;
    private String ApellidoPaterno;
    private String ApellidoMaterno;
    private String Correo;
    private String Contraseña;
    private String NickName;

    public Usuario(int id, String nombre, String apellidoMaterno, String apellidoPaterno, String correo, String contraseña,String nick) {
        this.id = id;
        Nombre = nombre;
        ApellidoPaterno = apellidoPaterno;
        ApellidoMaterno = apellidoMaterno;
        Correo = correo;
        Contraseña = contraseña;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        ApellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        ApellidoMaterno = apellidoMaterno;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }
    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String nombreCompleto(){
        String men = "";
        if(ApellidoMaterno.equals(""))
            men = Nombre+" "+ApellidoMaterno+" "+ApellidoPaterno;
        else
            men = Nombre+" "+ApellidoPaterno;
        return men;
    }
}
