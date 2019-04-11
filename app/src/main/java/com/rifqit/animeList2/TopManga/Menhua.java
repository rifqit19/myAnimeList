package com.rifqit.animeList2.TopManga;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.rifqit.animeList2.Database.GetDataService;
import com.rifqit.animeList2.R;
import com.rifqit.animeList2.Database.RetrofitCilentInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Menhua extends AppCompatActivity {

    private String TAG = Menhua.class.getSimpleName();
    private ArrayList<TopMangaObj> topMangaObjs = new ArrayList<>();
    AdapterTopManga adapterTopManga;
    ProgressDialog progressDoalog;
    RecyclerView recyclerView;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menhua);

        back = findViewById(R.id.backT6);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        generateDataList();
        progressDoalog = new ProgressDialog(Menhua.this);
        progressDoalog.setMessage("Loading...");
        progressDoalog.show();
        getTop("manga",1,"manhua");
    }
    public void getTop(String type,Integer page,String subtype){
        GetDataService service = RetrofitCilentInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.getTop(type,page,subtype);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String respon = response.body().string();
                        JSONObject jsonObj = new JSONObject(respon);
                        topMangaObjs.clear();
                        JSONArray api = jsonObj.getJSONArray("top");
                        for (int i = 0; i < api.length(); i++){
                            JSONObject c = api.getJSONObject(i);

                            Integer mal_id = c.getInt("mal_id");
                            Integer rank = c.getInt("rank");
                            String url = c.getString("url");
                            String tittle = c.getString("title");
                            String imageUrl = c.getString("image_url");
                            String start = c.getString("start_date");
//                            String end = c.getString("end_date");
                            String type = c.getString("type");
                            Double score = c.getDouble("score");
//                            Integer episode = c.getInt("episodes");
                            Integer members = c.getInt("members");

                            TopMangaObj ssn = new TopMangaObj();
                            ssn.setMalId(mal_id);
                            ssn.setRank(rank);
                            ssn.setUrl(url);
                            ssn.setImageUrl(imageUrl);
                            ssn.setTitle(tittle);
                            ssn.setStartDate(start);
                            ssn.setEndDate("unknown");
                            ssn.setScore(score);
                            ssn.setType(type);
                            ssn.setVolumes(0);
                            ssn.setMembers(members);
                            topMangaObjs.add(ssn);

                        }
                        adapterTopManga.notifyDataSetChanged();
//                        swipe.setRefreshing(false);
                    } catch (JSONException e) {
                        Toast.makeText(Menhua.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "Souldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void
                        run() {
                            Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LoCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(Menhua.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void generateDataList() {
        recyclerView = findViewById(R.id.recyclerMenhua);
        adapterTopManga = new AdapterTopManga(Menhua.this,topMangaObjs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Menhua.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterTopManga);
    }
}