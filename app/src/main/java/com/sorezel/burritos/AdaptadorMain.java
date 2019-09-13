package com.sorezel.burritos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class AdaptadorMain extends FragmentStatePagerAdapter {

    ArrayList<Fragment> pesta= new ArrayList<>();
    Context con;
    String[] nom;
    public AdaptadorMain(FragmentManager fm,String [] n){
        super(fm);
        nom = n;
    }

    @Override
    public Fragment getItem(int i) {
        return pesta.get(i);
    }

    @Override
    public int getCount() {
        return pesta.size();
    }
    public void addFrag(Fragment fr){
        pesta.add(fr);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void replaceFrag(Fragment fg, int i){
        pesta.remove(i);
        pesta.add(i,fg);
        notifyDataSetChanged();

    }
    @Override
    public CharSequence getPageTitle(int position) {
        return nom[position];
    }
}
