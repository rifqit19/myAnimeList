package com.rifqit.animeList2.season;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.rifqit.animeList2.Database.FavObj;
import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class adapterSeason extends RecyclerView.Adapter<adapterSeason.CustomViewHolder> {

    private ArrayList<seasonObj> seasonObjs;
    private Context context;
    FavObj favObj;
    RealmHelper realmHelper;
    Realm realm;

    public adapterSeason(Context context, ArrayList<seasonObj> datalist){
        this.context = context;
        this.seasonObjs = datalist;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder{
        public final View mView;

        TextView nama,eps,typ;
        ImageView cover;
        ToggleButton toggleButtonFav;

        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            nama = mView.findViewById(R.id.namaAnimeSeason);
            cover = mView.findViewById(R.id.coverSeason);
            eps = mView.findViewById(R.id.episodeLyt);
            typ = mView.findViewById(R.id.typeLyt);
            toggleButtonFav = mView.findViewById(R.id.toggleLyt);

            Realm.init(context);
            RealmConfiguration configuration = new RealmConfiguration.Builder().build();
            realm = Realm.getInstance(configuration);
            realmHelper = new RealmHelper(realm);
        }
    }
    @Override
    public adapterSeason.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.season_lyt,parent, false);
        return new adapterSeason.CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final adapterSeason.CustomViewHolder holder, final int position){
        holder.nama.setText(seasonObjs.get(position).getTitle());
        Picasso.with(context).load(seasonObjs.get(position).getImageUrl()).placeholder(R.drawable.ic_placeholder).into(holder.cover);
        holder.eps.setText(seasonObjs.get(position).getEpisodes().toString());
        holder.typ.setText(seasonObjs.get(position).getType());
        holder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , detailSeason.class);
                intent.putExtra("mmmmm", new Gson().toJson(seasonObjs.get(position)));
//                context.startActivity(intent);
                ((Activity) context).startActivityForResult(intent,2);
            }
        });

        final FavObj favObj1 = realm.where(FavObj.class).equalTo("malId",seasonObjs.get(position).getMalId()).findFirst();

        Log.e("idku ", seasonObjs.get(position).getMalId().toString());

        if (favObj1 == null) {
            holder.toggleButtonFav.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fav));
        }else{
            holder.toggleButtonFav.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_redfav));
        }
        holder.toggleButtonFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FavObj favObj2 = realm.where(FavObj.class).equalTo("malId",seasonObjs.get(position).getMalId()).findFirst();
                Log.e("checked", ""+position);
                if (favObj2 != null) {

                    holder.toggleButtonFav.setBackgroundResource(R.drawable.ic_fav);
                    realmHelper = new RealmHelper(realm);
                    realmHelper.delete(seasonObjs.get(position).getMalId());
                    Log.e("id2a",seasonObjs.get(position).getMalId().toString());
                }
                else {
                    holder.toggleButtonFav.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_redfav));

                    favObj = new FavObj();
                    favObj.setMalId(seasonObjs.get(position).getMalId());
                    favObj.setTitle(seasonObjs.get(position).getTitle());
                    favObj.setSynopsis(seasonObjs.get(position).getSynopsis());
                    favObj.setType(seasonObjs.get(position).getType());
                    favObj.setMembers(seasonObjs.get(position).getMembers());
                    favObj.setEpisode(seasonObjs.get(position).getEpisodes().toString());
                    favObj.setImageUrl(seasonObjs.get(position).getImageUrl());
                    favObj.setUrl(seasonObjs.get(position).getUrl());

                    Log.e("id1a",seasonObjs.get(position).getMalId().toString());

                    realmHelper = new RealmHelper(realm);
                    realmHelper.save(favObj);
                }
            }
        });

    }
    @Override
    public int getItemCount(){
        return seasonObjs.size();
    }}