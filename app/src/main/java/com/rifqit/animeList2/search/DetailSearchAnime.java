package com.rifqit.animeList2.search;

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

public class DetailSearchAnime extends AppCompatActivity {

    ImageView imgD;
    TextView ttlD, typD, memberD, episodD, synopD, webD,scoreD;
    ImageButton backk;
    Realm realm;
    FavObj favObj;
    RealmHelper realmHelper;
    ToggleButton toggleButton;
    SearchObj searchObjs = new SearchObj();

//    ProgressDialog progressDialog;
    ProgressBar progressBar;
    private String TAG = DetailSearchAnime.class.getSimpleName();
    ArrayList<recommObj> recommObjs = new ArrayList<>();
    AdapterRecommndation adapterRecommndation;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_search_anime);

        imgD = findViewById(R.id.imageSa);
        ttlD = findViewById(R.id.tittleSa);
        typD = findViewById(R.id.typeSa);
        memberD = findViewById(R.id.membersSa);
        episodD = findViewById(R.id.episodesSa);
        synopD = findViewById(R.id.synopSa);
        webD = findViewById(R.id.webSa);
        backk = findViewById(R.id.back2Sa);
        toggleButton = findViewById(R.id.toggleSa);
        scoreD = findViewById(R.id.scoreSa);
        progressBar = findViewById(R.id.progress3);

        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Realm.init(DetailSearchAnime.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        String locloc = getIntent().getStringExtra("aaaaa");
        searchObjs = new Gson().fromJson(locloc, SearchObj.class);

        final Integer malId = searchObjs.getMalId();
        final String t = searchObjs.getTitle();
        final String ty = searchObjs.getType();
        final String im = searchObjs.getImageUrl();
        final Integer m = searchObjs.getMembers();
        final String eps = searchObjs.getEpisodes().toString();
        final String syn = searchObjs.getSynopsis();
        final String wb = searchObjs.getUrl();
        Double sc = searchObjs.getScore();

        generateDataList();
        recommendation(malId);

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

                    Toast.makeText(DetailSearchAnime.this, "Disimpan ke Favorite!", Toast.LENGTH_SHORT).show();
                }
                else {
                    toggleButton.setBackgroundResource(R.drawable.ic_fav);
                    Log.e("id",malId.toString());
                    realmHelper = new RealmHelper(realm);
                    realmHelper.delete(malId);
                    Log.e("iddd",malId.toString());
                    Toast.makeText(DetailSearchAnime.this,"Item dihapus",Toast.LENGTH_SHORT).show();
                }
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
                        Toast.makeText(DetailSearchAnime.this, e.getLocalizedMessage()+111, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DetailSearchAnime.this, t.getLocalizedMessage()+11, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList() {
        recyclerView = findViewById(R.id.recyclerRecomendDetail4);
        adapterRecommndation = new AdapterRecommndation(DetailSearchAnime.this,recommObjs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailSearchAnime.this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterRecommndation);
    }
}
