package com.rifqit.animeList2.TopAnime;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rifqit.animeList2.favorite.FavObj;
import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;

public class adapterTopAnime extends RecyclerView.Adapter<adapterTopAnime.CustomViewHolder> {

    private ArrayList<TopAnimeObj> topAnimeObjs;
    private Context context;
    Realm realm;
    RealmHelper realmHelper;
    FavObj favObj;

    public adapterTopAnime(Context context, ArrayList<TopAnimeObj> datalist){
        this.context = context;
        this.topAnimeObjs = datalist;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder{
        public final View mView;

        TextView ttl,score,rank;
        ImageView imgTop;
        LinearLayout top;

        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            ttl = mView.findViewById(R.id.tittlrTop);
            score = mView.findViewById(R.id.score);
            imgTop = mView.findViewById(R.id.imgRank);
            rank = mView.findViewById(R.id.rank);
            top = mView.findViewById(R.id.topAnimLyt);


        }
    }
    @Override
    public adapterTopAnime.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.toplyt,parent, false);
        return new adapterTopAnime.CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(adapterTopAnime.CustomViewHolder holder, final int position){
        holder.ttl.setText(topAnimeObjs.get(position).getTitle());
        Picasso.with(context).load(topAnimeObjs.get(position).getImageUrl()).placeholder(R.drawable.ic_placeholder).into(holder.imgTop);
        holder.rank.setText(topAnimeObjs.get(position).getRank().toString());
        holder.score.setText(topAnimeObjs.get(position).getScore().toString());
        holder.top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , topAnimeDetail.class);
                intent.putExtra("ppppp", new Gson().toJson(topAnimeObjs.get(position)));
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount(){
        return topAnimeObjs.size();
    }}