package com.rifqit.animeList2.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rifqit.animeList2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.CustomViewHolder> {

    private ArrayList<SearchObj> searchObjs;
    private Context context;


    public AdapterSearch(Context context, ArrayList<SearchObj> datalist){
        this.context = context;
        this.searchObjs = datalist;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder{
        public final View mView;

        TextView tittle,score,type;
        RoundedImageView img;
        CardView lklk;


        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            tittle = mView.findViewById(R.id.tittleSearch);
            score = mView.findViewById(R.id.scoreSearch);
            type = mView.findViewById(R.id.typeSearch);
            img = mView.findViewById(R.id.imgSearch);
            lklk = mView.findViewById(R.id.lkjh);
        }
    }
    @Override
    public AdapterSearch.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.lyt_search,parent, false);
        return new AdapterSearch.CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(AdapterSearch.CustomViewHolder holder, final int position){
        holder.tittle.setText(searchObjs.get(position).getTitle());
        holder.type.setText(searchObjs.get(position).getType());
        holder.score.setText(searchObjs.get(position).getScore().toString());
        Picasso.with(context).load(searchObjs.get(position).getImageUrl()).placeholder(R.drawable.ic_placeholder).into(holder.img);
        holder.lklk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DetailSearchAnime.class);
                intent.putExtra("aaaaa", new Gson().toJson(searchObjs.get(position)));
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount(){
        return searchObjs.size();
    }}