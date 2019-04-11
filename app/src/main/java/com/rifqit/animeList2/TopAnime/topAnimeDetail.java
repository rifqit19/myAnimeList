package com.rifqit.animeList2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.rifqit.animeList2.Database.FavObj;
import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.TopAnime.TopAnimeObj;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class topAnimeDetail extends AppCompatActivity {

    ImageView imgDt;
    TextView ttlDt,typDt,memberDt,episodDt,synopDt,webDt;
    ImageButton backkt;
    Realm realm;
    FavObj favObj;
    RealmHelper realmHelper;
    public TopAnimeObj topAnimeObj = new TopAnimeObj();
    ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_anime_detail);

        imgDt = findViewById(R.id.imageDt);
        ttlDt = findViewById(R.id.tittleDt);
        typDt = findViewById(R.id.typeDt);
        memberDt = findViewById(R.id.membersDt);
        episodDt = findViewById(R.id.episodesDt);
        synopDt = findViewById(R.id.synopDt);
        webDt = findViewById(R.id.webDt);
        backkt = findViewById(R.id.back2t);
        toggleButton = findViewById(R.id.toggle3);

        Realm.init(topAnimeDetail.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        backkt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String locloc = getIntent().getStringExtra("ppppp");
        topAnimeObj = new Gson().fromJson(locloc, TopAnimeObj.class);

        final String tittl = topAnimeObj.getTitle();
        final String synop = topAnimeObj.getTitle();
        final String typ = topAnimeObj.getType();
        final Integer mmbr = topAnimeObj.getMembers();
        final String img = topAnimeObj.getImageUrl();
        final String epp = topAnimeObj.getTitle();
        final String web = topAnimeObj.getUrl();
        final Integer malId = topAnimeObj.getMalId();

        Picasso.with(this).load(img).into(imgDt);
        ttlDt.setText(tittl);
        synopDt.setText(synop);
        typDt.setText(typ);
        memberDt.setText(mmbr.toString());
        episodDt.setText(epp);
        webDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(web);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }else {
                    Log.d("web", "Can't handle this!");
                } }
        });


        final FavObj favObj1 = realm.where(FavObj.class).equalTo("malId",malId).findFirst();

        if (favObj1 == null) {
            toggleButton.setChecked(false);
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_fav));

        }else{
            toggleButton.setChecked(true);
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_redfav));
        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_redfav));

                    favObj = new FavObj();
                    favObj.setMalId(malId);
                    favObj.setTitle(tittl);
                    favObj.setSynopsis(synop);
                    favObj.setType(typ);
                    favObj.setMembers(mmbr);
                    favObj.setEpisode(epp);
                    favObj.setImageUrl(img);
                    favObj.setUrl(web);

                    realmHelper = new RealmHelper(realm);
                    realmHelper.save(favObj);

                    Toast.makeText(topAnimeDetail.this, "Disimpan ke Favorite!", Toast.LENGTH_SHORT).show();
                }
                else {
                    toggleButton.setBackgroundResource(R.drawable.ic_fav);
                    Log.e("id",malId.toString());
                    realmHelper = new RealmHelper(realm);
                    realmHelper.delete(malId);
                    Log.e("iddd",malId.toString());
                    Toast.makeText(topAnimeDetail.this,"Item dihapus",Toast.LENGTH_SHORT).show();
                }
            }
        });





    }
}
