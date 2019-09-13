package com.sorezel.burritos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sorezel.burritos.Fragments.LogInFragment;
import com.sorezel.burritos.Fragments.PerfilFragment;
import com.sorezel.burritos.Objetos.Usuario;

import java.nio.BufferUnderflowException;

public class UsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        Toolbar tb = findViewById(R.id.tool_us);
        tb.setTitle("Perfil");
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sh = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        int ruta = sh.getInt("ID",-1);

        if(ruta == -1){
            getSupportFragmentManager().beginTransaction().add(R.id.cont_us,new LogInFragment()).commit();
        }else{
            Bundle b = new Bundle();
            Usuario us = new Usuario(sh.getInt("ID",-1),sh.getString("Nombre",null),sh.getString("AM",null),
                    sh.getString("AP",null),sh.getString("Correo",null),sh.getString("Contrasena",null),sh.getString("nick",null));
            b.putSerializable("us",us);
            PerfilFragment pf = new PerfilFragment();
            pf.setArguments(b);
            getSupportFragmentManager().beginTransaction().add(R.id.cont_us,pf).commit();
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        SharedPreferences s = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.algo:

                String info = ""+s.getInt("ID",-1)+" "+s.getString("Nombre",null)+" "
                        +s.getString("Correo",null)+" "+s.getString("Contrasena",null);
                Toast.makeText(this,info,Toast.LENGTH_SHORT).show();
                break;
            case R.id.mn_log_out:
                SharedPreferences.Editor she = s.edit();
                she.clear().apply();
                finish();
                break;
            case R.id.mn_config:
                Intent in = new Intent(this,ConfiguracionActivity.class);
                startActivity(in);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
