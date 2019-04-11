package com.rifqit.animeList2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.rifqit.animeList2.Database.FavObj;
import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.Database.adapterFav;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Favorite extends AppCompatActivity {

    Realm realm;
    RealmHelper realmHelper;
    RecyclerView recyclerView;
    adapterFav adapterFav;
    List<FavObj> favObjs;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        back = findViewById(R.id.backFav2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                Intent BackIntent = new Intent();
                setResult(RESULT_OK,BackIntent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerFav);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(Favorite.this, 3,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mGridLayoutManager);

        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        realmHelper = new RealmHelper(realm);
        favObjs = new ArrayList<>();

        favObjs = realmHelper.getFav();

        show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapterFav.notifyDataSetChanged();
        show();
    }

    public void show(){
        adapterFav = new adapterFav(this, favObjs);
        recyclerView.setAdapter(adapterFav);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            show();
        }else {
            Toast.makeText(Favorite.this,"gagal",Toast.LENGTH_SHORT).show();
        }
    }
}