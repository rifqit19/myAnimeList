package com.rifqit.animeList2.season;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rifqit.animeList2.Database.GetDataService;
import com.rifqit.animeList2.MainActivity;
import com.rifqit.animeList2.R;
import com.rifqit.animeList2.Database.RetrofitCilentInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class more extends AppCompatActivity {

    TextView mss, thh;
    ProgressDialog progressDoalog;
    ImageButton back3;
    com.rifqit.animeList2.season.adapterMore adapterMore;
    private String TAG = MainActivity.class.getSimpleName();
    private ArrayList<seasonObj> seasonObjs = new ArrayList<>();
    RecyclerView recyclerView;
    SwipeRefreshLayout swipe;
    String mm, yy, mn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        android.support.v7.widget.Toolbar toolbarS = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbarS);
        ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(toolbarS);

        mss = findViewById(R.id.musimm);
        thh = findViewById(R.id.tahunn);
        back3 = findViewById(R.id.back3);
        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                Intent BackIntent = new Intent();
//                BackIntent.putExtra("idm",mal_id);
                setResult(RESULT_OK,BackIntent);
                finish();
            }
        });

        Calendar date = Calendar.getInstance();
        Integer yearC1 = date.get(Calendar.YEAR);
        Integer monthC1 = date.get(Calendar.MONTH);

        Bundle bb = getIntent().getExtras();

        String th1 = bb.getString("thth");
        String ms1 = bb.getString("msms");

        if (th1.equals("tahun") || ms1.equals("musim")) {
            yy = yearC1.toString();
            String yr1 = yearC1.toString();
            if (monthC1.equals(1) || monthC1.equals(2) || monthC1.equals(3)) {
                mm = "winter";
            } else if (monthC1.equals(4) || monthC1.equals(5) || monthC1.equals(6)) {
                mm = "spring";
            } else if (monthC1.equals(7) || monthC1.equals(8) || monthC1.equals(9)) {
                mm = "summer";
            } else if (monthC1.equals(4) || monthC1.equals(5) || monthC1.equals(6)) {
                mm = "fall";
            }
        } else {
            mm = ms1;
            yy = th1;
        }

        mss.setText(mm);
        thh.setText(yy);

        generateDataList();

        getSeason(yy, mm);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        MenuItem searchIem = menu.findItem(R.id.search);
        searchIem.setTitle("cari");
        final SearchView searchView = (SearchView) searchIem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapterMore.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void getSeason(String thn, String msm) {
        progressDoalog = new ProgressDialog(more.this);
        progressDoalog.setMessage("Loading...");
        progressDoalog.show();
        GetDataService service = RetrofitCilentInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.getSeason(thn, msm);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String respon = response.body().string();
                        JSONObject jsonObj = new JSONObject(respon);
                        JSONArray api = jsonObj.getJSONArray("anime");
                        for (int i = 0; i < api.length(); i++) {
                            JSONObject c = api.getJSONObject(i);

                            Integer mal_id = c.getInt("mal_id");
                            String url = c.getString("url");
                            String tittle = c.getString("title");
                            String imageUrl = c.getString("image_url");
                            String synopsis = c.getString("synopsis");
                            String type = c.getString("type");
                            String airing_start = c.getString("airing_start");
                            String episode = c.getString("episodes");
                            Integer members = c.getInt("members");
                            Object episodes = episode;

                            seasonObj ssn = new seasonObj();

                            ssn.setMalId(mal_id);
                            ssn.setUrl(url);
                            ssn.setImageUrl(imageUrl);
                            ssn.setTitle(tittle);
                            ssn.setSynopsis(synopsis);
                            ssn.setType(type);
                            ssn.setAiringStart(airing_start);
                            ssn.setEpisodes(episodes);
                            ssn.setMembers(members);

                            JSONArray o = c.getJSONArray("genres");

                            boolean isSave = true;

                            for (int e = 0; e < o.length(); e++) {
                                JSONObject g = o.getJSONObject(e);

                                String name = g.getString("name");

                                if (name.equalsIgnoreCase("Hentai")) {
                                    isSave = false;
                                    break;
                                }
                            }

                            if (isSave)
                                seasonObjs.add(ssn);

                        }
                        adapterMore.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(more.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(more.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList() {
        recyclerView = findViewById(R.id.recyclerSeasonMore);
        adapterMore = new adapterMore(this, seasonObjs);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(more.this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterMore);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode ==RESULT_OK) {
            adapterMore.notifyDataSetChanged();

        }
    }
}
