package com.rifqit.animeList2.TopManga;

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
import com.rifqit.animeList2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterTopManga extends RecyclerView.Adapter<AdapterTopManga.CustomViewHolder> {

    private ArrayList<TopMangaObj> topMangaObjs;
    private Context context;

    public AdapterTopManga(Context context, ArrayList<TopMangaObj> datalist){
        this.context = context;
        this.topMangaObjs = datalist;
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
    public AdapterTopManga.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.toplyt,parent, false);
        return new AdapterTopManga.CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(AdapterTopManga.CustomViewHolder holder, final int position){
        holder.ttl.setText(topMangaObjs.get(position).getTitle());
        Picasso.with(context).load(topMangaObjs.get(position).getImageUrl()).placeholder(R.drawable.ic_placeholder).into(holder.imgTop);
        holder.rank.setText(topMangaObjs.get(position).getRank().toString());
        holder.score.setText(topMangaObjs.get(position).getScore().toString());
        holder.top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , topMangaDetail.class);
                intent.putExtra("manga", new Gson().toJson(topMangaObjs.get(position)));
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount(){
        return topMangaObjs.size();
    }}