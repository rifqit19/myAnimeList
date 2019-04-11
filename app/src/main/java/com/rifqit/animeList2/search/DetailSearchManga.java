package com.rifqit.animeList2.search;

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

public class DetailSearchManga extends AppCompatActivity {

    ImageView imgD;
    TextView ttlD, typD, memberD, episodD, synopD, webD,scoreD;
    ImageButton backk;
    Realm realm;
    FavObj favObj;
    RealmHelper realmHelper;
    ToggleButton toggleButton;
    searchObjManga searchObjMangas = new searchObjManga();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_search_manga);

        imgD = findViewById(R.id.imageMa);
        ttlD = findViewById(R.id.tittleMa);
        typD = findViewById(R.id.typeMa);
        memberD = findViewById(R.id.membersMa);
        episodD = findViewById(R.id.episodesMa);
        synopD = findViewById(R.id.synopMa);
        webD = findViewById(R.id.webMa);
        backk = findViewById(R.id.back2Ma);
        toggleButton = findViewById(R.id.toggleMa);
        scoreD = findViewById(R.id.scoreMa);

        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Realm.init(DetailSearchManga.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        String locloc = getIntent().getStringExtra("zzzzz");
        searchObjMangas = new Gson().fromJson(locloc, searchObjManga.class);

        final Integer malId = searchObjMangas.getMalId();
        final String t = searchObjMangas.getTitle();
        final String ty = searchObjMangas.getType();
        final String im = searchObjMangas.getImageUrl();
        final Integer m = searchObjMangas.getMembers();
        final String eps = searchObjMangas.getVolumes().toString();
        final String syn = searchObjMangas.getSynopsis();
        final String wb = searchObjMangas.getUrl();
        Double sc = searchObjMangas.getScore();

        ttlD.setText(t);
        synopD.setText(syn);
        typD.setText(ty);
        memberD.setText(m.toString());
        episodD.setText(eps);
        Picasso.with(this).load(im).into(imgD);
        webD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(wb);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d("web", "Can't handle this!");
                }
            }
        });
        scoreD.setText(sc.toString());

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
                    favObj.setTitle(t);
                    favObj.setSynopsis(syn);
                    favObj.setType(ty);
                    favObj.setMembers(m);
                    favObj.setEpisode(eps);
                    favObj.setImageUrl(im);
                    favObj.setUrl(wb);

                    realmHelper = new RealmHelper(realm);
                    realmHelper.save(favObj);

                    Toast.makeText(DetailSearchManga.this, "Disimpan ke Favorite!", Toast.LENGTH_SHORT).show();
                }
                else {
                    toggleButton.setBackgroundResource(R.drawable.ic_fav);
                    Log.e("id",malId.toString());
                    realmHelper = new RealmHelper(realm);
                    realmHelper.delete(malId);
                    Log.e("iddd",malId.toString());
                    Toast.makeText(DetailSearchManga.this,"Item dihapus",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
