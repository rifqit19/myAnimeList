package com.rifqit.animeList2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.favorite.FavObj;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;

public class AdapterRecommendationManga extends RecyclerView.Adapter<AdapterRecommendationManga.CustomViewHolder> {

    private ArrayList<recommObj> recommObjs;
    private Context context;
    Realm realm;
    RealmHelper realmHelper;
    FavObj favObj;

    public AdapterRecommendationManga(Context context, ArrayList<recommObj> datalist){
        this.context = context;
        this.recommObjs = datalist;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        ImageView imageView;
        TextView textView,count;

        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            count = mView.findViewById(R.id.recCount);
            imageView = mView.findViewById(R.id.coverRec);
            textView = mView.findViewById(R.id.namaAnimeRec);

        }
    }
    @Override
    public AdapterRecommendationManga.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_pager,parent, false);
        return new AdapterRecommendationManga.CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(AdapterRecommendationManga.CustomViewHolder holder, final int position){
        holder.textView.setText(recommObjs.get(position).getTitle());
        Picasso.with(context).load(recommObjs.get(position).getImageUrl()).placeholder(R.drawable.ic_placeholder).into(holder.imageView);
        holder.count.setText(recommObjs.get(position).getRecommendationCount().toString());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent z = new Intent(context, DetailRecommendManga.class);
                z.putExtra("zzzzz", new Gson().toJson(recommObjs.get(position)));
                context.startActivity(z);
            }
        });

    }
    @Override
    public int getItemCount(){
        return recommObjs.size();
    }}