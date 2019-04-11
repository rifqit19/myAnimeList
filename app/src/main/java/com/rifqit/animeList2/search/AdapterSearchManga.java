package com.rifqit.animeList2.search;

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

public class AdapterSearchManga extends RecyclerView.Adapter<AdapterSearchManga.CustomViewHolder> {

    private ArrayList<searchObjManga> searchObjMangas;
    private Context context;


    public AdapterSearchManga(Context context, ArrayList<searchObjManga> datalist){
        this.context = context;
        this.searchObjMangas = datalist;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder{
        public final View mView;

        TextView tittle,score,type;
        ImageView img;
        LinearLayout lklk;


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
    public AdapterSearchManga.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.lyt_search,parent, false);
        return new AdapterSearchManga.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterSearchManga.CustomViewHolder holder, final int position){
        holder.tittle.setText(searchObjMangas.get(position).getTitle());
        holder.type.setText(searchObjMangas.get(position).getType());
        holder.score.setText(searchObjMangas.get(position).getScore().toString());
        Picasso.with(context).load(searchObjMangas.get(position).getImageUrl()).placeholder(R.drawable.ic_placeholder).into(holder.img);
        holder.lklk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DetailSearchManga.class);
                intent.putExtra("zzzzz", new Gson().toJson(searchObjMangas.get(position)));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount(){
        return searchObjMangas.size();
    }}
