package com.rifqit.animeList2;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyCustomPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<recommObj> recommObjs;
    LayoutInflater layoutInflater;

    public MyCustomPagerAdapter(Context context, ArrayList<recommObj> datalist){
        this.context = context;
        this.recommObjs = datalist;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean  isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_pager,container, false);

        ImageView imageView = itemView.findViewById(R.id.coverRec);
        TextView textView = itemView.findViewById(R.id.namaAnimeRec);

        Picasso.with(context).load(recommObjs.get(position).getImageUrl()).into(imageView);
        textView.setText(recommObjs.get(position).getTitle());

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
