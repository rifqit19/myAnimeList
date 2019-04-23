package com.rifqit.animeList2.season;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.rifqit.animeList2.AdapterRecommndation;
import com.rifqit.animeList2.Database.GetDataService;
import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.Database.RetrofitCilentInstance;
import com.rifqit.animeList2.R;
import com.rifqit.animeList2.favorite.FavObj;
import com.rifqit.animeList2.recommObj;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class detailSeason extends AppCompatActivity {

    public seasonObj seasonObjs = new seasonObj();

    ImageView imgD;
    TextView ttlD, typD, memberD, episodD, synopD, webD;
    ImageButton backk;
//    Button btnFav;
    Realm realm;
    FavObj favObj;
    RealmHelper realmHelper;
    ToggleButton toggleButton;

//    ProgressDialog progressDialog;
    ProgressBar progressBar;
    private String TAG = detailSeason.class.getSimpleName();
    ArrayList<recommObj> recommObjs = new ArrayList<>();
    AdapterRecommndation adapterRecommndation;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_main);
        imgD = findViewById(R.id.imageM);
        ttlD = findViewById(R.id.tittleM);
        typD = findViewById(R.id.typeM);
        memberD = findViewById(R.id.membersM);
        episodD = findViewById(R.id.episodesM);
        synopD = findViewById(R.id.synopM);
        webD = findViewById(R.id.webM);
        backk = findViewById(R.id.back2M);
//        btnFav = findViewById(R.id.btnFavM);
        toggleButton = findViewById(R.id.toggle2);
        progressBar = findViewById(R.id.progress2);

        Realm.init(detailSeason.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                Intent BackIntent = new Intent();
//                BackIntent.putExtra("idm",mal_id);
                setResult(RESULT_OK,BackIntent);
                finish();
            }
        });

        String locloc = getIntent().getStringExtra("mmmmm");
        seasonObjs = new Gson().fromJson(locloc, seasonObj.class);

        final String tittl = seasonObjs.getTitle();
        final String synop = seasonObjs.getSynopsis();
        final String typ = seasonObjs.getType();
        final Integer mmbr = seasonObjs.getMembers();
        final String img = seasonObjs.getImageUrl();
        Object epsd = seasonObjs.getEpisodes();
        final String epp = epsd.toString();
        final String web = seasonObjs.getUrl();
        final  Integer malId = seasonObjs.getMalId();

        generateDataList();
        recommendation(malId);

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
                    favObj.setTitle(tittl);
                    favObj.setSynopsis(synop);
                    favObj.setType(typ);
                    favObj.setMembers(mmbr);
                    favObj.setEpisode(epp);
                    favObj.setImageUrl(img);
                    favObj.setUrl(web);

                    realmHelper = new RealmHelper(realm);
                    realmHelper.save(favObj);

                    Toast.makeText(detailSeason.this, "Disimpan ke Favorite!", Toast.LENGTH_SHORT).show();
                }
                else {
                    toggleButton.setBackgroundResource(R.drawable.ic_fav);
                    Log.e("id2d",malId.toString());
                    realmHelper = new RealmHelper(realm);
                    realmHelper.delete(malId);
                    Log.e("iddd",malId.toString());
                    Toast.makeText(detailSeason.this,"Item dihapus",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent BackIntent = new Intent();
        setResult(RESULT_OK,BackIntent);
        finish();
    }


    public void recommendation(Integer id){
//        progressDialog = new ProgressDialog(detailSeason.this);
//        progressDialog.setMessage("Loading....");
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        GetDataService service = RetrofitCilentInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.getRecomendation(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String respon = response.body().string();
                        JSONObject jsonObj = new JSONObject(respon);
                        JSONArray api = jsonObj.getJSONArray("recommendations");
                        for (int i = 0; i < api.length(); i++){
                            JSONObject c = api.getJSONObject(i);

                            Integer mal_id1 = c.getInt("mal_id");
                            String url1 = c.getString("url");
                            String tittle1 = c.getString("title");
                            String recommendationUrl = c.getString("recommendation_url");
                            String imageUrl1 = c.getString("image_url");
                            Integer recommendationCount = c.getInt("recommendation_count");

                            final recommObj s = new recommObj();
                            s.setMalId(mal_id1);
                            s.setUrl(url1);
                            s.setImageUrl(imageUrl1);
                            s.setTitle(tittle1);
                            s.setRecommendationUrl(recommendationUrl);
                            s.setRecommendationCount(recommendationCount);
                            recommObjs.add(s);

                        }
//                        myCustomPagerAdapter.notifyDataSetChanged();
                        adapterRecommndation.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(detailSeason.this, e.getLocalizedMessage()+111, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "Souldn't get json from server.1");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void
                        run() {
                            Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LoCat for possible errors!1",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(detailSeason.this, t.getLocalizedMessage()+11, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList() {
        recyclerView = findViewById(R.id.recyclerRecomendDetail2);
        adapterRecommndation = new AdapterRecommndation(detailSeason.this,recommObjs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(detailSeason.this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterRecommndation);
    }
}
