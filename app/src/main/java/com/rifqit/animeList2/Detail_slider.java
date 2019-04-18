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
import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.favorite.FavObj;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Detail_slider extends AppCompatActivity {

    ScheduleObj scheduleObj = new ScheduleObj();

    ImageView imgD;
    TextView ttlD, typD, memberD, episodD, synopD, webD;
    ImageButton backk;
    //    Button btnFav;
    Realm realm;
    FavObj favObj;
    RealmHelper realmHelper;
    ToggleButton toggleButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_slider);

        imgD = findViewById(R.id.imageSd);
        ttlD = findViewById(R.id.tittleSd);
        typD = findViewById(R.id.typeSd);
        memberD = findViewById(R.id.memberSd);
        episodD = findViewById(R.id.episodesSd);
        synopD = findViewById(R.id.synopSd);
        webD = findViewById(R.id.webSd);
        backk = findViewById(R.id.backSd);
        toggleButton = findViewById(R.id.toggleSd);

        Realm.init(Detail_slider.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String schd = getIntent().getStringExtra("schedule");
        scheduleObj = new Gson().fromJson(schd, ScheduleObj.class);

        final Integer mal_id1 = scheduleObj.getMalId();
        final String url1 = scheduleObj.getUrl();
        final String tittle1 = scheduleObj.getTitle();
        final String imageUrl1 = scheduleObj.getImageUrl();
        final String synopsis1 = scheduleObj.getSynopsis();
        final String type1 = scheduleObj.getType();
        String airing_start1 = scheduleObj.getAiringStart();
//        final Integer episode = scheduleObj.getEpisodes();
        final Integer members1 = scheduleObj.getMembers();

        Log.e("dtl ", mal_id1.toString());
        Log.e("dtl ", url1);
        Log.e("dtl ", tittle1);
        Log.e("dtl ", imageUrl1);
        Log.e("dtl ", synopsis1);
        Log.e("dtl ", type1);
        Log.e("dtl ", airing_start1);
//        Log.e("dtl",episode.toString());
        Log.e("dtl ", members1.toString());

        ttlD.setText(tittle1);
        synopD.setText(synopsis1);
        typD.setText(type1);
        memberD.setText(members1.toString());
//        episodD.setText(episode);
        Picasso.with(this).load(imageUrl1).into(imgD);
        webD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(url1);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d("web", "Can't handle this!");
                }
            }
        });


        final FavObj favObj1 = realm.where(FavObj.class).equalTo("malId",mal_id1).findFirst();

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
                    favObj.setMalId(mal_id1);
                    favObj.setTitle(tittle1);
                    favObj.setSynopsis(synopsis1);
                    favObj.setType(type1);
                    favObj.setMembers(members1);
                    favObj.setEpisode("unknown");
                    favObj.setImageUrl(imageUrl1);
                    favObj.setUrl(url1);

                    realmHelper = new RealmHelper(realm);
                    realmHelper.save(favObj);

                    Toast.makeText(Detail_slider.this, "Disimpan ke Favorite!", Toast.LENGTH_SHORT).show();
                }
                else {
                    toggleButton.setBackgroundResource(R.drawable.ic_fav);
                    realmHelper = new RealmHelper(realm);
                    realmHelper.delete(mal_id1);
                    Toast.makeText(Detail_slider.this,"Item dihapus",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
