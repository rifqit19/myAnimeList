package com.rifqit.animeList2.favorite;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.R;

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
    ImageButton back,deleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        back = findViewById(R.id.backFav2);
        deleteAll = findViewById(R.id.deleteAll);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackIntent = new Intent();
                setResult(RESULT_OK,BackIntent);
                finish();
            }
        });


        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (favObjs.isEmpty()){
                     Toast.makeText(Favorite.this,"favorite kosong",Toast.LENGTH_SHORT).show();
                 }else {
                     new AlertDialog.Builder(Favorite.this)
                             .setTitle("Hapus Semua")
                             .setMessage(getString(R.string.apakah_kamu_yakinn_akan_mnghapus_semua_item_faforit_kamu))
                             .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     dialog.cancel();
                                 }
                             })
                             .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     realmHelper.deleteAll();
                                     Toast.makeText(Favorite.this,"Semua item favorit dihapus..",Toast.LENGTH_SHORT).show();
                                     adapterFav.notifyDataSetChanged();
                                 }
                             }).show();
                 }
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
//            show();
            adapterFav.notifyDataSetChanged();
        }else {
            Toast.makeText(Favorite.this,"gagal",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent BackIntent = new Intent();
        setResult(RESULT_OK,BackIntent);
        finish();
    }
}

