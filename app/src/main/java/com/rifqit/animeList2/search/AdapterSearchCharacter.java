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

public class AdapterSearchCharacter extends RecyclerView.Adapter<AdapterSearchCharacter.CustomViewHolder> {

    private ArrayList<seachObjCharacter> seachObjCharacters;
    private Context context;

    public AdapterSearchCharacter(Context context, ArrayList<seachObjCharacter> datalist){
        this.context = context;
        this.seachObjCharacters = datalist;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder{
        public final View mView;

        TextView tittle;
        RoundedImageView img;
        CardView lklk;


        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            tittle = mView.findViewById(R.id.tittleSearchC);
            img = mView.findViewById(R.id.imgSearchC);
            lklk = mView.findViewById(R.id.lkjhg);

        }
    }
    @Override
    public AdapterSearchCharacter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.lyt_search_char,parent, false);
        return new AdapterSearchCharacter.CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(AdapterSearchCharacter.CustomViewHolder holder, final int position){
        holder.tittle.setText(seachObjCharacters.get(position).getName());
//        holder.type.setText(seachObjCharacters.get(position).get);
//        holder.score.setText(seachObjCharacters.get(position).getScore().toString());
        Picasso.with(context).load(seachObjCharacters.get(position).getImageUrl()).placeholder(R.drawable.ic_placeholder).into(holder.img);
        holder.lklk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DetailCharacter.class);
                intent.putExtra("ccccc", new Gson().toJson(seachObjCharacters.get(position)));
                context.startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount(){
        return seachObjCharacters.size();
    }}