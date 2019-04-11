package com.rifqit.animeList2.season;

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
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.rifqit.animeList2.Database.FavObj;
import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.R;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class detailMore extends AppCompatActivity {

    public seasonObj seasonObjs = new seasonObj();

    ImageView imgD;
    TextView ttlD,typD,memberD,episodD,synopD,webD;
    ImageButton backk;
//    Button btnFav;
    Realm realm;
    FavObj favObj;
    RealmHelper realmHelper;
    ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_season);

        imgD = findViewById(R.id.imageD);
        ttlD = findViewById(R.id.tittleD);
        typD = findViewById(R.id.type);
        memberD = findViewById(R.id.members);
        episodD = findViewById(R.id.episodes);
        synopD = findViewById(R.id.synopD);
        webD = findViewById(R.id.web);
        backk = findViewById(R.id.back2);
//        btnFav = findViewById(R.id.btnFav);
        toggleButton = findViewById(R.id.toggle1);

        Realm.init(detailMore.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        String locloc = getIntent().getStringExtra("ssssss");
        seasonObjs = new Gson().fromJson(locloc, seasonObj.class);

        final String tittl = seasonObjs.getTitle();
        final String synop = seasonObjs.getSynopsis();
        final String typ = seasonObjs.getType();
        final Integer mal_id = seasonObjs.getMalId();
        final Integer mmbr = seasonObjs.getMembers();
        final String img = seasonObjs.getImageUrl();
        Object epsd = seasonObjs.getEpisodes();
        final String epp = epsd.toString();
        final String web = seasonObjs.getUrl();
        final Integer malId = seasonObjs.getMalId();


        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                Intent BackIntent = new Intent();
                setResult(RESULT_OK,BackIntent);
                finish();
            }
        });

        ttlD.setText(tittl);
        synopD.setText(synop);
        typD.setText(typ);
        memberD.setText(mmbr.toString());
        episodD.setText(epp);
        Picasso.with(this).load(img).into(imgD);
        webD.setOnClickListener(new View.OnClickListener() {
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

        Log.e("idku ", malId.toString());

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

//                    Toast.makeText(detailMore.this, "Disimpan ke Favorite!", Toast.LENGTH_SHORT).show();
                }
                else {
                    toggleButton.setBackgroundResource(R.drawable.ic_fav);
                    Log.e("id",malId.toString());
                    realmHelper = new RealmHelper(realm);
                    realmHelper.delete(malId);
                    Log.e("iddd",malId.toString());
//                    Toast.makeText(detailMore.this,"Item dihapus",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
