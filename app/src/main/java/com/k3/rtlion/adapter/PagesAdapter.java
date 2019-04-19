package com.k3.rtlion.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.k3.rtlion.R;

import java.util.ArrayList;

public class PagesAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ViewGroup> layouts = new ArrayList<>();
    public interface IViewPager{
        void onViewsAdded(ArrayList<ViewGroup> layouts);
    }
    private IViewPager iViewPager;

    public PagesAdapter(Context context, IViewPager iViewPager) {
        this.context = context;
        this.iViewPager = iViewPager;
    }

    public enum PagesEnum {
        Main(R.string.page_main, R.layout.frag_main),
        Settings(R.string.page_settings, R.layout.frag_settings),
        Graph(R.string.page_graph, R.layout.frag_graph),
        Scanner(R.string.page_scanner, R.layout.frag_scanner);
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
    public @NonNull Object instantiateItem(@NonNull ViewGroup collection, int position) {
        PagesEnum pagesEnum = PagesEnum.values()[position];
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) layoutInflater.
                inflate(pagesEnum.getLayoutResID(), collection, false);
        layouts.add(layout);
        collection.addView(layout);
        if(position == getCount() - 1) {
            iViewPager.onViewsAdded(layouts);
        }
        return layout;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull  Object view) {
        collection.removeView((View) view);
    }
    @Override
    public int getCount() {
        return PagesEnum.values().length;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull  Object object) {
        return view == object;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        PagesEnum pagesEnum = PagesEnum.values()[position];
        return context.getString(pagesEnum.getTitleResID());
    }
}
