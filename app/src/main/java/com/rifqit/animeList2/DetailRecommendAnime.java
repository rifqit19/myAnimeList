package com.rifqit.animeList2;

import android.app.ProgressDialog;
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
import com.rifqit.animeList2.Database.GetDataService;
import com.rifqit.animeList2.Database.RealmHelper;
import com.rifqit.animeList2.Database.RetrofitCilentInstance;
import com.rifqit.animeList2.favorite.FavObj;
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

public class DetailRecommendAnime extends AppCompatActivity {

    ImageView imgD;
    TextView ttlD, typD, memberD, episodD, synopD, webD,scoreD;
    ImageButton backk;
    Realm realm;
    FavObj favObj;
    RealmHelper realmHelper;
    ToggleButton toggleButton;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    private String TAG = DetailRecommendAnime.class.getSimpleName();
    ArrayList<DetailObjRecommend> detailObjRecommends = new ArrayList<>();
    AdapterRecommndation adapterRecommndation;
    ArrayList<recommObj> recommObjs = new ArrayList<>();
    RecyclerView recyclerView;
    recommObj recommObjs1 = new recommObj();
    String url1,imageUrl1,tittle1,synop,typ;
    Integer eps,mmbr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recommend_anime);

        imgD = findViewById(R.id.imageDra);
        ttlD = findViewById(R.id.tittleDra);
        typD = findViewById(R.id.typeDra);
        memberD = findViewById(R.id.membersDra);
        episodD = findViewById(R.id.episodesDra);
        synopD = findViewById(R.id.synopDra);
        webD = findViewById(R.id.webDra);
        backk = findViewById(R.id.back2Dra);
        toggleButton = findViewById(R.id.toggleDra);
        scoreD = findViewById(R.id.scoreDra);
        progressBar = findViewById(R.id.progressDra);

        Realm.init(DetailRecommendAnime.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);


        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        String locloc = getIntent().getStringExtra("lllll");
        recommObjs1 = new Gson().fromJson(locloc, recommObj.class);

        final Integer malid = recommObjs1.getMalId();
        Log.e("IDOKE", malid.toString());

        generateDataList();
        recommendation(malid);
        detailReccomend(malid);

        final FavObj favObj1 = realm.where(FavObj.class).equalTo("malId",malid).findFirst();

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
                    favObj.setMalId(malid);
                    favObj.setTitle(tittle1);
                    favObj.setSynopsis(synop);
                    favObj.setType(typ);
                    favObj.setMembers(mmbr);
                    favObj.setEpisode(eps.toString());
                    favObj.setImageUrl(imageUrl1);
                    favObj.setUrl(url1);

                    realmHelper = new RealmHelper(realm);
                    realmHelper.save(favObj);

                    Toast.makeText(DetailRecommendAnime.this, "Disimpan ke Favorite!", Toast.LENGTH_SHORT).show();
                }
                else {
                    toggleButton.setBackgroundResource(R.drawable.ic_fav);
                    Log.e("id",malid.toString());
                    realmHelper = new RealmHelper(realm);
                    realmHelper.delete(malid);
                    Log.e("iddd",malid.toString());
                    Toast.makeText(DetailRecommendAnime.this,"Item dihapus",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public void detailReccomend(Integer id){
        progressDialog = new ProgressDialog(DetailRecommendAnime.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
//        progressBar.setVisibility(View.VISIBLE);
        GetDataService service = RetrofitCilentInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.getDetailRecomendationAnime(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
//                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String respon = response.body().string();
                        JSONObject jsonObj = new JSONObject(respon);

                        Integer mal_id1 = jsonObj.getInt("mal_id");
                        url1 = jsonObj.getString("url");
                        tittle1 = jsonObj.getString("title");
                        imageUrl1 = jsonObj.getString("image_url");
                        typ = jsonObj.getString("type");
                        Double scr = jsonObj.getDouble("score");
                        eps = jsonObj.getInt("episodes");
                        synop = jsonObj.getString("synopsis");
                        mmbr = jsonObj.getInt("members");


                        final DetailObjRecommend s = new DetailObjRecommend();
                        s.setMalId(mal_id1);
                        s.setUrl(url1);
                        s.setImageUrl(imageUrl1);
                        s.setTitle(tittle1);
                        s.setEpisodes(eps);
                        s.setMembers(mmbr);
                        s.setType(typ);
                        s.setScore(scr);
                        s.setSynopsis(synop);
                        detailObjRecommends.add(s);

                        Picasso.with(DetailRecommendAnime.this).load(imageUrl1).into(imgD);
                        ttlD.setText(tittle1);
                        typD.setText(typ);
                        scoreD.setText(scr.toString());
                        episodD.setText(eps.toString());
                        synopD.setText(synop);
                        memberD.setText(mmbr.toString());
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


                        Log.e("oooo", s.getImageUrl());
                        Log.e("oooo", s.getUrl());
                        Log.e("oooo", s.getSynopsis());
                        Log.e("oooo", s.getTitle());
                        Log.e("oooo", s.getType());
                        Log.e("oooo", s.getEpisodes().toString());
                        Log.e("oooo", s.getMalId().toString());
                        Log.e("oooo", s.getScore().toString());
                        Log.e("oooo", s.getMembers().toString());

                    } catch (JSONException e) {
                        Toast.makeText(DetailRecommendAnime.this, e.getLocalizedMessage()+111, Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(DetailRecommendAnime.this, t.getLocalizedMessage()+11, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void recommendation(Integer id){
//        progressDialog = new ProgressDialog(DetailSearchAnime.this);
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

                            final recommObj s1 = new recommObj();
                            s1.setMalId(mal_id1);
                            s1.setUrl(url1);
                            s1.setImageUrl(imageUrl1);
                            s1.setTitle(tittle1);
                            s1.setRecommendationUrl(recommendationUrl);
                            s1.setRecommendationCount(recommendationCount);
                            recommObjs.add(s1);

                        }
//                        myCustomPagerAdapter.notifyDataSetChanged();
                        adapterRecommndation.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(DetailRecommendAnime.this, e.getLocalizedMessage()+111, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DetailRecommendAnime.this, t.getLocalizedMessage()+11, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList() {
        recyclerView = findViewById(R.id.recyclerRecomendDetailDra);
        adapterRecommndation = new AdapterRecommndation(DetailRecommendAnime.this,recommObjs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailRecommendAnime.this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterRecommndation);
    }
}
