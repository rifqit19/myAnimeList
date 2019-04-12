package com.rifqit.animeList2.favorite;

import android.annotation.SuppressLint;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class adapterFav extends RecyclerView.Adapter<adapterFav.MyViewHolder> {
    private List<FavObj> favObjs;
    Context context;
    FavObj favObj;
    RealmHelper realmHelper;
    Realm realm;

    public adapterFav(Context context, List<FavObj> favObjs){
        this.context = context;
        this.favObjs = favObjs;
    }

    @Override
    public int getItemCount() {
        return favObjs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nama,eps,typ;
        ImageView cover;
        ToggleButton toggleButtonFav;

        public MyViewHolder(View itemView){
            super(itemView);
            nama = itemView.findViewById(R.id.namaAnimeSeason);
            cover = itemView.findViewById(R.id.coverSeason);

            eps = itemView.findViewById(R.id.episodeLyt);
            typ = itemView.findViewById(R.id.typeLyt);
            toggleButtonFav = itemView.findViewById(R.id.toggleLyt);

            Realm.init(context);
            RealmConfiguration configuration = new RealmConfiguration.Builder().build();
            realm = Realm.getInstance(configuration);
            realmHelper = new RealmHelper(realm);
        }
    }

    @Override
    public adapterFav.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.season_lyt, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final adapterFav.MyViewHolder holder, final int position) {
        final FavObj model = favObjs.get(position);
        holder.nama.setText(model.getTitle());
        holder.eps.setText(model.getEpisode());
        Picasso.with(context).load(model.getImageUrl()).placeholder(R.drawable.ic_placeholder).into(holder.cover);
        holder.typ.setText(model.getType());
        holder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailFav.class);
//                i.putExtra("hhhhh", new Gson().toJson(favObjs.get(position)));
                i.putExtra("malId",model.getMalId().toString());
                i.putExtra("tittle",model.getTitle());
                i.putExtra("img",model.getImageUrl());
                i.putExtra("type",model.getType());
                i.putExtra("member",model.getMembers().toString());
                i.putExtra("episode",model.getEpisode());
                i.putExtra("web",model.getUrl());
                i.putExtra("synopsis",model.getSynopsis());
                v.getContext().startActivity(i);
            }
        });

        final FavObj favObj1 = realm.where(FavObj.class).equalTo("malId",favObjs.get(position).getMalId()).findFirst();

        Log.e("idku ", favObjs.get(position).getMalId().toString());

        if (favObj1 == null) {
            holder.toggleButtonFav.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fav));
        }else{
            holder.toggleButtonFav.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_redfav));
        }
        holder.toggleButtonFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FavObj favObj2 = realm.where(FavObj.class).equalTo("malId",favObjs.get(position).getMalId()).findFirst();
                Log.e("checked", ""+position);
                if (favObj2 != null) {
                    holder.toggleButtonFav.setBackgroundResource(R.drawable.ic_fav);
                    Log.e("id",favObjs.get(position).getMalId().toString());
                    realmHelper = new RealmHelper(realm);
                    realmHelper.delete(favObjs.get(position).getMalId());
                    notifyItemRemoved(position);
                    Toast.makeText(context,"Item dihapus",Toast.LENGTH_SHORT).show();

                }
                else {
                    holder.toggleButtonFav.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_redfav));

                    favObj = new FavObj();
                    favObj.setMalId(favObjs.get(position).getMalId());
                    favObj.setTitle(favObjs.get(position).getTitle());
                    favObj.setSynopsis(favObjs.get(position).getSynopsis());
                    favObj.setType(favObjs.get(position).getType());
                    favObj.setMembers(favObjs.get(position).getMembers());
                    favObj.setEpisode(favObjs.get(position).getEpisode());
                    favObj.setImageUrl(favObjs.get(position).getImageUrl());
                    favObj.setUrl(favObjs.get(position).getUrl());

                    realmHelper = new RealmHelper(realm);
                    realmHelper.save(favObj);

                    Toast.makeText(context, "Disimpan ke Favorite!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}