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
import com.rifqit.animeList2.Database.FavObj;
import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.R;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DetailCharacter extends AppCompatActivity {

    ImageButton backk;
    ToggleButton toggleButton;
    ImageView img;
    TextView name,web;
    Realm realm;
    FavObj favObj;
    RealmHelper realmHelper;
    seachObjCharacter seachObjCharacters = new seachObjCharacter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_character);

        backk = findViewById(R.id.back2Ch);
        toggleButton = findViewById(R.id.toggleCh);
        img = findViewById(R.id.imageC);
        name = findViewById(R.id.nameC);
        web = findViewById(R.id.webCh);

        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String locloc = getIntent().getStringExtra("ccccc");
        seachObjCharacters = new Gson().fromJson(locloc, seachObjCharacter.class);

        final String nm = seachObjCharacters.getName();
        final String im = seachObjCharacters.getImageUrl();
        final String ur = seachObjCharacters.getUrl();
        final Integer malId = seachObjCharacters.getMalId();

        Realm.init(DetailCharacter.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        Picasso.with(this).load(im).into(img);
        name.setText(nm);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(ur);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d("web", "Can't handle this!");
                }
            }
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
                    favObj.setTitle(nm);
                    favObj.setImageUrl(im);
                    favObj.setUrl(ur);
                    favObj.setSynopsis("unknown");
                    favObj.setType("unknown");
                    favObj.setEpisode("unknown");
                    favObj.setMembers(0);

                    realmHelper = new RealmHelper(realm);
                    realmHelper.save(favObj);

                    Toast.makeText(DetailCharacter.this, "Disimpan ke Favorite!", Toast.LENGTH_SHORT).show();
                }
                else {
                    toggleButton.setBackgroundResource(R.drawable.ic_fav);
                    Log.e("id",malId.toString());
                    realmHelper = new RealmHelper(realm);
                    realmHelper.delete(malId);
                    Log.e("iddd",malId.toString());
                    Toast.makeText(DetailCharacter.this,"Item dihapus",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
