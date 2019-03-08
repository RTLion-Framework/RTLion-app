package com.k3.rtlion;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PagesAdapter extends PagerAdapter {

    private Context context;
    public PagesAdapter(Context context) {
        this.context = context;
    }

    private enum PagesEnum {
        Main(R.string.page_main, R.layout.layout_splash);
        private int titleResID, layoutResID;
        PagesEnum(int titleResID, int layoutResID) {
            this.titleResID = titleResID;
            this.layoutResID = layoutResID;
        }
        public int getTitleResID() {
            return titleResID;
        }
        public int getLayoutResID() {
            return layoutResID;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        PagesEnum pagesEnum = PagesEnum.values()[position];
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) layoutInflater.
                inflate(pagesEnum.getLayoutResID(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return PagesEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        PagesEnum pagesEnum = PagesEnum.values()[position];
        return context.getString(pagesEnum.getTitleResID());
    }

}
