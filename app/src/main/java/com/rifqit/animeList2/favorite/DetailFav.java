package com.rifqit.animeList2.favorite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.rifqit.animeList2.R;
import com.rifqit.animeList2.Database.RealmHelper;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DetailFav extends AppCompatActivity {

    public FavObj favObj = new FavObj();
    ImageView imgF;
    TextView ttlF,typF,memberF,episodF,synopF,webF;
    ImageButton backF;
    Button btnDeleteFav,delete;
    RealmHelper realmHelper;
    Realm realm;
    ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_fav);

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);
        realmHelper = new RealmHelper(realm);

        imgF = findViewById(R.id.imageFav);
        ttlF = findViewById(R.id.tittleFav);
        typF  = findViewById(R.id.typeFav);
        memberF = findViewById(R.id.membersFav);
        episodF = findViewById(R.id.episodesFav);
        synopF = findViewById(R.id.synopFav);
        webF = findViewById(R.id.webFav);
        backF = findViewById(R.id.backFav3);
        toggleButton = findViewById(R.id.toggleFav);

        backF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackIntent = new Intent();
                setResult(RESULT_OK,BackIntent);
                finish();
            }
        });

        String locloc = getIntent().getStringExtra("ssssss");
        favObj = new Gson().fromJson(locloc, FavObj.class);

        final String tittl = getIntent().getStringExtra("tittle");
        final String synop = getIntent().getStringExtra("synopsis");
        final String typ = getIntent().getStringExtra("type");
        final Integer mmbr = Integer.parseInt(getIntent().getStringExtra("member"));
        final String img = getIntent().getStringExtra("img");
        final String epp = getIntent().getStringExtra("episode");
        final String web = getIntent().getStringExtra("web");
        final Integer malId = Integer.parseInt(getIntent().getStringExtra("malId"));

        Picasso.with(this).load(img).into(imgF);
        ttlF.setText(tittl);
        synopF.setText(synop);
        typF.setText(typ);
        memberF.setText(mmbr.toString());
        episodF.setText(epp);
        webF.setOnClickListener(new View.OnClickListener() {
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

                    Toast.makeText(DetailFav.this, "Disimpan ke Favorite!", Toast.LENGTH_SHORT).show();
                }
                else {
                    toggleButton.setBackgroundResource(R.drawable.ic_fav);
                    Log.e("id",malId.toString());
                    realmHelper = new RealmHelper(realm);
                    realmHelper.delete(malId);
                    Log.e("iddd",malId.toString());
                    Toast.makeText(DetailFav.this,"Item dihapus",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}
