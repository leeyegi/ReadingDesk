package com.example.yegilee.readingdesk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

//-----------------------------탭기능 클래스-------------------------------
public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MainTab1 tab1 = new MainTab1();
                return tab1;
            case 1:
                MainTab2 tab2 = new MainTab2();
                return tab2;
            default:
                return null;
        }
    }

    //탭의 갯수
    @Override
    public int getCount() {
        return 2;
    }

    //tab에 보여지는 text
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "독서대장 이용";
            case 1:
                return "사용패턴 분석";
        }
        return null;
    }
}

