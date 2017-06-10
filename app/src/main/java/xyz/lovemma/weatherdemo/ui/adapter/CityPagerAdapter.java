package xyz.lovemma.weatherdemo.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import xyz.lovemma.weatherdemo.ui.fragment.CityFragment;

/**
 * Created by OO on 2017/6/7.
 */

public class CityPagerAdapter extends FragmentStatePagerAdapter {
    List<CityFragment> mFragments = new ArrayList<>();

    public CityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    private static final String TAG = "CityPagerAdapter";

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void addFragment(CityFragment fragment) {
        mFragments.add(fragment);
        notifyDataSetChanged();
    }

    public void removeFragment(int position) {
        mFragments.remove(position);
        notifyDataSetChanged();
    }
}
