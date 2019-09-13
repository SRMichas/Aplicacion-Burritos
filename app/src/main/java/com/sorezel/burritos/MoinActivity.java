package com.sorezel.burritos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.sorezel.burritos.BD.LocalHelper;
import com.sorezel.burritos.Fragments.CargarFragment;
import com.sorezel.burritos.Fragments.FavoritoFragment;
import com.sorezel.burritos.Fragments.MainFragment;
import com.sorezel.burritos.Fragments.MainOrdenFragment;
import com.sorezel.burritos.Fragments.PagarFragment;
import com.sorezel.burritos.Fragments.VacioFragment;
import com.sorezel.burritos.Objetos.Burrito;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MoinActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    ArrayList<Burrito> burros,top;
    DrawerLayout DWL;
    NavigationView NGV;
    LocalHelper LH;
    boolean pasa;
    SharedPreferences sh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_moin);
        sh = getSharedPreferences("Usuario",0);
        pasa = sh.getBoolean("bd",false);

        Toolbar tb = findViewById(R.id.tool_Moin);
        tb.setTitle("Home");

        if(pasa){
            LH = new LocalHelper(this);
            LH.openDataBase();
            burros = LocalHelper.retBurros();
            top = LocalHelper.retTopBurros();
            Toast.makeText(this,"Entro local",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Entro remoto",Toast.LENGTH_SHORT).show();
        }
        Bundle b = new Bundle();
        final MainFragment mf = new MainFragment();
        b.putSerializable("menu",burros);
        b.putSerializable("top",top);
        b.putBoolean("boo",pasa);
        mf.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.frag_cont,mf,"menu").addToBackStack("menu").commit();

        setSupportActionBar(tb);


        //Toast.makeText(MoinActivity.this,""+LocalHelper.retBurros().size(),Toast .LENGTH_SHORT).show();
        DWL = findViewById(R.id.drawer_l);
        NGV = findViewById(R.id.navi_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,DWL,tb,R.string.open,R.string.close);
        DWL.addDrawerListener(toggle);
        toggle.syncState();
        NGV.setNavigationItemSelectedListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //getSupportActionBar().setTitle("Home");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_l);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fg = getCurrentFragment();
            if(fg instanceof MainFragment){
                MainFragment mainf = (MainFragment) fg;
                mainf.corrige();
            }else if( fg instanceof MainOrdenFragment){
                MainOrdenFragment mainf = (MainOrdenFragment) fg;
                mainf.corrige();
            }else{
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack("menu",0);
            }
        }
        //Fragment f = getCurrentFragment();
        //String tag = f.getTag();



    }
    private Fragment getCurrentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Toast.makeText(this,"Seleccionaste "+item.getTitle(),Toast .LENGTH_SHORT).show();
        SharedPreferences sh = getSharedPreferences("Usuario",0);
        ArrayList<Burrito> burros;
        Fragment fragment = null;
        Bundle b = new Bundle();
        int ID = sh.getInt("ID",-1);
        switch (id){
            case R.id.mn_carrito:
                /*RelativeLayout f = findViewById(R.id.frag_cont);
                f.removeAllViews();*/
                //Bundle b = new Bundle();
                if(ID != -1){
                    getSupportActionBar().setTitle("Carrito");
                    fragment = new CargarFragment();
                    b.putInt("t",1);
                    b.putBoolean("b",pasa);
                    fragment.setArguments(b);
                    /*burros = LocalHelper.retCarrito(ID);
                    //Snackbar.make(DWL,""+LocalHelper.algo(ID),Snackbar.LENGTH_SHORT).show();
                    if(burros != null){
                        fragment = new PagarFragment();
                        b.putSerializable("burros",burros);
                        fragment.setArguments(b);
                    }else if( ID == -1){
                        fragment = new VacioFragment();
                        b.putString("txt","UPS! no se puede recuperar la informacion.Trata iniciar sersión");
                        b.putInt("img",R.drawable.ic_error);
                        fragment.setArguments(b);
                    }else{
                        fragment = new VacioFragment();
                        b.putString("txt","No has seleccionado ningun burrito, trata de ordenar uno :)");
                        b.putInt("img",R.drawable.ic_menu_share);
                        fragment.setArguments(b);
                    }*/
                }else{
                    fragment = new VacioFragment();
                    b.putString("txt","UPS! no se puede recuperar la informacion.Trata iniciar sersión");
                    b.putInt("img",R.drawable.ic_error);
                    fragment.setArguments(b);
                }

                //b.putStringArrayList("lista",s);
                //pf.setArguments(b);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,fragment,"carrito").addToBackStack("carrito").commit();
                break;
            case R.id.mn_perfil:
               // am.replaceFrag(new DosFragment(),tbl.getSelectedTabPosition());
                Intent in = new Intent(this,UsuarioActivity.class);
                startActivity(in);
                break;
            case R.id.mn_fav:
                if(ID != -1){
                    getSupportActionBar().setTitle("Favoritos");
                    fragment = new CargarFragment();
                    b.putInt("t",2);
                    b.putBoolean("b",pasa);
                    fragment.setArguments(b);
                    /*if(pasa){
                        ID = sh.getInt("ID",-1);
                        burros = LocalHelper.retFavoritos(ID);

                        if(burros != null){
                            fragment = new FavoritoFragment();
                            b.putSerializable("burros",burros);
                            fragment.setArguments(b);
                        }--else if( ID == -1){
                            fragment = new VacioFragment();
                            b.putString("txt","UPS! no se puede recuperar la informacion.Trata iniciar sersión");
                            b.putInt("img",R.drawable.ic_error);
                            fragment.setArguments(b);
                        }--else{
                            fragment = new VacioFragment();
                            b.putString("txt","No tienes favoritos :(");
                            b.putInt("img",R.drawable.ic_menu_share);
                            fragment.setArguments(b);
                        }
                    }else{
                        //fragment = new FavoritoFragment();
                        //hazAlgo(1,"ConsultaFavoritos.php");
                    }*/
                }else{
                    fragment = new VacioFragment();
                    b.putString("txt","UPS! no se puede recuperar la informacion.Trata iniciar sersión");
                    b.putInt("img",R.drawable.ic_error);
                    fragment.setArguments(b);
                }


                //FavoritoFragment ff = new FavoritoFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,fragment,"favorito").addToBackStack("favorito").commit();
                break;
            case R.id.mn_orden:
                fragment = new MainOrdenFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,fragment,"orden").addToBackStack("orden").commit();
                break;
            /*case R.id.eliminar:
                LH.elimina();
                break;
            case R.id.del_sh:
                SharedPreferences.Editor she = sh.edit();
                she.clear().apply();
                break;
            case R.id.inser:
                Object[] a = {9,"brayan","ramirez","soria","elias@gmail.com","hola","cakas"};
                //LH.registraUsuario(a);
                //LocalHelper.borraUsuarios();
                //LocalHelper.totalUser();
                LocalHelper.borraCarr();
                break;*/
            case R.id.local:
                sh.edit().putBoolean("bd",true).apply();
                LH = new LocalHelper(this);
                LH.openDataBase();
                pasa = true;
                break;
            case R.id.remota:
                sh.edit().putBoolean("bd",false).apply();
                pasa = false;
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_l);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void hazAlgo(int type, String php) {
        String url = getString(R.string.ip);
        switch (type){
            case 1://Carrito
                break;
            case 2://Fav
                url +=php+"?UsId="+sh.getInt("ID",-1);
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }
}
