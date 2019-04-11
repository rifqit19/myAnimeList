package com.rifqit.animeList2.TopManga;

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
import com.rifqit.animeList2.favorite.FavObj;
import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.R;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class topMangaDetail extends AppCompatActivity {

    ImageView imgDt;
    TextView ttlDt,typDt,memberDt,episodDt,synopDt,webDt;
    ImageButton backkt;
    Realm realm;
    FavObj favObj;
    RealmHelper realmHelper;
    public TopMangaObj topMangaObj = new TopMangaObj();
    ToggleButton toggleButton,toggleButton1;
//    Button btn1,btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_manga_detail);

        imgDt = findViewById(R.id.imageDtm);
        ttlDt = findViewById(R.id.tittleDtm);
        typDt = findViewById(R.id.typeDtm);
        memberDt = findViewById(R.id.membersDtm);
        episodDt = findViewById(R.id.episodesDtm);
        synopDt = findViewById(R.id.synopDtm);
        webDt = findViewById(R.id.webDtm);
        backkt = findViewById(R.id.back2tm);
        toggleButton = findViewById(R.id.toggle4);
        toggleButton1 = findViewById(R.id.toggle4a);

        Realm.init(topMangaDetail.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        backkt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String locloc = getIntent().getStringExtra("manga");
        topMangaObj = new Gson().fromJson(locloc, TopMangaObj.class);

        final String tittl = topMangaObj.getTitle();
        final String synop = topMangaObj.getTitle();
        final String typ = topMangaObj.getType();
        final Integer mmbr = topMangaObj.getMembers();
        final String img = topMangaObj.getImageUrl();
        final String epp = topMangaObj.getTitle();
        final String web = topMangaObj.getUrl();
        final Integer malId = topMangaObj.getMalId();
        Log.e("idd", malId.toString());

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

                        Toast.makeText(topMangaDetail.this, "Disimpan ke Favorite!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        toggleButton.setBackgroundResource(R.drawable.ic_fav);
                        Log.e("id",malId.toString());
                        realmHelper = new RealmHelper(realm);
                        realmHelper.delete(malId);
                        Log.e("iddd",malId.toString());
                        Toast.makeText(topMangaDetail.this,"Item dihapus",Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
