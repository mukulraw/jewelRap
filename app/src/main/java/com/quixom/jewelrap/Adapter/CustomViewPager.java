package com.quixom.jewelrap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;
import android.widget.Toast;

import com.quixom.jewelrap.Model.ViewPagerModel;

/**
 * Created by C32 on 9/21/2016.
 */
public class CustomViewPager extends PagerAdapter {

    Context mContext;

    public CustomViewPager(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ViewPagerModel modelObject = ViewPagerModel.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getViewpager_layout(), container, false);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 0){
                    Toast.makeText(mContext," view stock ",Toast.LENGTH_SHORT).show();
                }
                if(position == 1){
                    Toast.makeText(mContext," sales ",Toast.LENGTH_SHORT).show();
                }
             /*   Log.e("get layout "," "+modelObject.getViewpager_layout()+" postion "+ position);
                Toast.makeText(mContext,"layout",Toast.LENGTH_SHORT).show();*/
            }
        });
        container.addView(layout);
        return layout;
     //   return super.instantiateItem(container, position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    @Override
    public int getCount() {
        return ViewPagerModel.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
