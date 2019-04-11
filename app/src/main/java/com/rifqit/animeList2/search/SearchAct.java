package com.rifqit.animeList2.search;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.rifqit.animeList2.EndlessOnScrollListener;
import com.rifqit.animeList2.GetDataService;
import com.rifqit.animeList2.R;
import com.rifqit.animeList2.RetrofitCilentInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerType,page;
    private String TAG = SearchAct.class.getSimpleName();
    private ArrayList<SearchObj> searchObjs = new ArrayList<>();
    AdapterSearch adapterSearch;
    private ArrayList<searchObjManga> searchObjMangas= new ArrayList<>();
    AdapterSearchManga adapterSearchManga;
    private ArrayList<seachObjCharacter> seachObjCharacters= new ArrayList<>();
    AdapterSearchCharacter adapterSearchCharacter;
    ProgressDialog progressDoalog;
    RecyclerView recyclerView,recyclerView1,recyclerView2;
    ImageButton back,refresh;
    String tilt;
    Integer hal = 1;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tilt = getIntent().getStringExtra("search");
        progressBar = findViewById(R.id.progress_bar1);
//        progressBar.setVisibility(View.GONE);

        spinnerType = findViewById(R.id.spinnerType);
        back = findViewById(R.id.backSearch);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(SearchAct.this);

        progressDoalog = new ProgressDialog(SearchAct.this);
        progressDoalog.setMessage("Loading...");
    }

    public void getSrc(String tt, String ttt, final Integer tttt){
        if (tttt.equals(1)){
            progressDoalog.show();
            progressBar.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.VISIBLE);
        }
        GetDataService service = RetrofitCilentInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.getSearch(tt,ttt,tttt);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDoalog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String respon = response.body().string();
                        JSONObject jsonObj = new JSONObject(respon);
                        if (tttt.equals(1)){
                            searchObjs.clear();
                           progressDoalog.dismiss();
                        }else {
                            progressBar.setVisibility(View.GONE);
                        }
                        JSONArray api = jsonObj.getJSONArray("results");

                        boolean isSave = true;
                        for (int i = 0; i < api.length(); i++){
                            JSONObject c = api.getJSONObject(i);

                            String titt = c.getString("title");
                            Integer malId = c.getInt("mal_id");
                            String url = c.getString("url");
                            String imgUrll = c.getString("image_url");
//                            Boolean airing = c.getBoolean("airing");
                            String synop = c.getString("synopsis");
                            String ty = c.getString("type");
                            Integer episodes = c.getInt("episodes");
                            Double score = c.getDouble("score");
                            String start = c.getString("start_date");
                            String end = c.getString("end_date");
                            Integer mmbr = c.getInt("members");
                            String rated = c.getString("rated");

                            if (rated.equalsIgnoreCase("Rx")){
                                isSave = false;
                                break;
                            }

                            SearchObj s = new SearchObj();
                            s.setTitle(titt);
                            s.setMalId(malId);
//                            s.setAiring(airing);
                            s.setEndDate(end);
                            s.setEpisodes(episodes);
                            s.setType(ty);
                            s.setMembers(mmbr);
                            s.setSynopsis(synop);
                            s.setImageUrl(imgUrll);
                            s.setUrl(url);
                            s.setScore(score);
                            s.setStartDate(start);
                            s.setRated(rated);

                            if (isSave){
                                searchObjs.add(s);
                            }


                        }
                        adapterSearch.notifyDataSetChanged();
                    } catch (JSONException e) {
//                        Toast.makeText(SearchAct.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDoalog.dismiss();
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(SearchAct.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void generateDataListAnime() {
        recyclerView = findViewById(R.id.recyclerSearch);
        adapterSearch = new AdapterSearch(this,searchObjs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchAct.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterSearch);
        recyclerView.addOnScrollListener(new EndlessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(Integer current_page) {
                String typpp1 = spinnerType.getSelectedItem().toString();
                getSrc(typpp1,tilt,current_page);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String typpp1 = spinnerType.getSelectedItem().toString();
        if (typpp1.equals("anime")){
            generateDataListAnime();
            getSrc(typpp1,tilt,1);
        }else if (typpp1.equals("character")){
            generateDataListCharacter();
            getSrcC(typpp1,tilt,1);
        }else if (typpp1.equals("manga")){
            generateDataListManga();
            getSrcManga(typpp1,tilt,1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void getSrcManga(String tt, String ttt, final Integer tttt){
        if (tttt.equals(1)){
            progressDoalog.show();
            progressBar.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.VISIBLE);
        }
        GetDataService service = RetrofitCilentInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.getSearch(tt,ttt,tttt);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDoalog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String respon = response.body().string();
                        JSONObject jsonObj = new JSONObject(respon);
                        if (tttt.equals(1)){
                            searchObjs.clear();
                            progressDoalog.dismiss();
                        }else {
                            progressBar.setVisibility(View.GONE);
                        }
                        JSONArray api = jsonObj.getJSONArray("results");

                        for (int i = 0; i < api.length(); i++){

                            JSONObject c = api.getJSONObject(i);
                            String titt = c.getString("title");
                            Integer malId = c.getInt("mal_id");
                            String url = c.getString("url");
                            String imgUrll = c.getString("image_url");
//                            Boolean publishing = c.getBoolean("publishing");
                            String synop = c.getString("synopsis");
                            String ty = c.getString("type");
                            Integer episodes = c.getInt("volumes");
                            Double score = c.getDouble("score");
                            String start = c.getString("start_date");
                            String end = c.getString("end_date");
                            Integer mmbr = c.getInt("members");
                            Integer rated = c.getInt("chapters");

                            searchObjManga ss = new searchObjManga();

                            ss.setTitle(titt);
                            ss.setMalId(malId);
//                            ss.setPublishing(publishing);
                            ss.setEndDate(end);
                            ss.setVolumes(episodes);
                            ss.setType(ty);
                            ss.setMembers(mmbr);
                            ss.setSynopsis(synop);
                            ss.setImageUrl(imgUrll);
                            ss.setUrl(url);
                            ss.setScore(score);
                            ss.setStartDate(start);
                            ss.setChapters(rated);

                            searchObjMangas.add(ss);

                        }
                        adapterSearchManga.notifyDataSetChanged();
                    } catch (JSONException e) {
//                        Toast.makeText(SearchAct.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDoalog.dismiss();
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(SearchAct.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataListManga() {
        recyclerView1 = findViewById(R.id.recyclerSearch);
        adapterSearchManga = new AdapterSearchManga(this,searchObjMangas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchAct.this);
        recyclerView1.setLayoutManager(linearLayoutManager);
        recyclerView1.setAdapter(adapterSearchManga);
        recyclerView.addOnScrollListener(new EndlessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(Integer current_page) {
                String typpp1 = spinnerType.getSelectedItem().toString();
                getSrcManga(typpp1,tilt,current_page);
            }
        });
    }
    public void getSrcC(String tt, String ttt, final Integer tttt){
        if (tttt.equals(1)){
            progressDoalog.show();
            progressBar.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.VISIBLE);
        }
        GetDataService service = RetrofitCilentInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.getSearch(tt,ttt,tttt);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String respon = response.body().string();
                        JSONObject jsonObj = new JSONObject(respon);
                        if (tttt.equals(1)){
                            searchObjs.clear();
                            progressDoalog.dismiss();
                        }else {
                            progressBar.setVisibility(View.GONE);
                        }
                        JSONArray api = jsonObj.getJSONArray("results");
                        for (int i = 0; i < api.length(); i++){
                            JSONObject c = api.getJSONObject(i);

                            String titt = c.getString("name");
                            Integer malId = c.getInt("mal_id");
                            String url = c.getString("url");
                            String imgUrll = c.getString("image_url");

                            seachObjCharacter sss = new seachObjCharacter();
                            sss.setName(titt);
                            sss.setMalId(malId);
                            sss.setImageUrl(imgUrll);
                            sss.setUrl(url);
                            seachObjCharacters.add(sss);
                        }
                        adapterSearchCharacter.notifyDataSetChanged();
                    } catch (JSONException e) {
//                        Toast.makeText(SearchAct.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDoalog.dismiss();
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(SearchAct.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataListCharacter() {
        recyclerView2 = findViewById(R.id.recyclerSearch);
        adapterSearchCharacter = new AdapterSearchCharacter(this,seachObjCharacters);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchAct.this);
        recyclerView2.setLayoutManager(linearLayoutManager);
        recyclerView2.setAdapter(adapterSearchCharacter);
        recyclerView.addOnScrollListener(new EndlessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(Integer current_page) {
                String typpp1 = spinnerType.getSelectedItem().toString();
                getSrcC(typpp1,tilt,current_page);
            }
        });
    }
}



