package com.bichan.shop.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

/**
 * Created by cuong on 5/24/2017.
 */

public class FragmentAdapter extends SmartFragmentStatePagerAdapter {
    private ArrayList<Fragment> list;
    private ArrayList<String> title;
    private Context mContext;

    public FragmentAdapter(Context mContext, FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
        title = new ArrayList<String>();
        this.mContext = mContext;
    }
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    public void addTitle(String e){
        title.add(e);
    }
    public void clearTitle(){
        title.clear();
    }
}
