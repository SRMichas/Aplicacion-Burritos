package com.sorezel.burritos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.sorezel.burritos.Fragments.ListaFragment;
import com.sorezel.burritos.Fragments.NadaFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    AdaptadorMain am;
    ArrayList<String> burros;
    DrawerLayout DWL;
    NavigationView NGV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        burros = new ArrayList<>();

        Toolbar tb = findViewById(R.id.tool_Main);
        TabLayout tbl = findViewById(R.id.tabMain);
        ViewPager vp = findViewById(R.id.pager);
        load(vp);
        tbl.setupWithViewPager(vp);
        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DWL = findViewById(R.id.drawer_l);
        NGV = findViewById(R.id.navview);
        NGV.setNavigationItemSelectedListener(new OyeDrawer());

    }

    private void load(ViewPager vp) {
        am = new AdaptadorMain(getSupportFragmentManager(),new String[]{"Menu","Top10"});

        kk(10);
        Bundle b = new Bundle();
        ListaFragment ls = new ListaFragment();
        b.putStringArrayList("lista",burros);
        ls.setArguments(b);
        am.addFrag(ls);

        kk(4);
        ListaFragment ls2 = new ListaFragment();
        b.putStringArrayList("lista",burros);
        ls2.setArguments(b);
        NadaFragment nf = new NadaFragment();
        am.addFrag(nf);

        kk(1);
        ListaFragment ls3 = new ListaFragment();
        b.putStringArrayList("lista",burros);
        ls3.setArguments(b);
        am.addFrag(ls3);
        vp.setAdapter(am);

    }

    private void kk(int c){
        for (int i = 0; i < c; i++) {
            burros.add("");
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                DWL.openDrawer(GravityCompat.START);
                return true;
            //...
        }

        return super.onOptionsItemSelected(item);
    }
    private class OyeDrawer implements NavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            Snackbar.make(DWL,"Seleccionaste "+menuItem.getTitle(),Snackbar.LENGTH_SHORT).show();
            /*switch (id){

            }*/
            DWL.closeDrawers();
            return true;
        }
    }
}
