package com.rifqit.animeList2;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.rifqit.animeList2.TopAnime.Airing;
import com.rifqit.animeList2.TopAnime.Ova;
import com.rifqit.animeList2.TopAnime.Special;
import com.rifqit.animeList2.TopAnime.Tv;
import com.rifqit.animeList2.TopManga.Doujin;
import com.rifqit.animeList2.TopManga.Manga;
import com.rifqit.animeList2.TopManga.Menhua;
import com.rifqit.animeList2.TopManga.Menhwa;
import com.rifqit.animeList2.TopManga.Novel;
import com.rifqit.animeList2.TopManga.OneShots;
import com.rifqit.animeList2.search.SearchAct;
import com.rifqit.animeList2.season.adapterSeason;
import com.rifqit.animeList2.season.seasonObj;

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

public class MainActivity extends AppCompatActivity{

    Spinner musim ,tahun;
//    String tahunS,musimS;
    ProgressDialog progressDoalog;
    ImageButton search;
    RelativeLayout more;
    com.rifqit.animeList2.season.adapterSeason adapterSeason;
    private String TAG = MainActivity.class.getSimpleName();
    private ArrayList<seasonObj> seasonObjs = new ArrayList<>();
    RecyclerView recyclerView;
    TextView airing,tv,upcoming,special,ova;
    TextView manga,novel,oneShots,doujin,menhwa,menhua;
    String yr,mn;
    String mn1;
    String tahunku1,musimku1;
    MainActivity mainActivity = this;
    LinearLayout topA,topM;
    ToggleButton sideT,sideT1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        Calendar date = Calendar.getInstance();
        final Integer yearC = date.get(Calendar.YEAR);
        Integer monthC = date.get(Calendar.MONTH);

        musim = findViewById(R.id.spinnerMusim);
        tahun = findViewById(R.id.spinnerTahun);
        search = findViewById(R.id.search);
        more = findViewById(R.id.more);
        airing = findViewById(R.id.airing);
        tv = findViewById(R.id.tv);
//        upcoming = findViewById(R.id.upcoming);
        special = findViewById(R.id.special);
        ova = findViewById(R.id.ova1);
        manga = findViewById(R.id.manga);
        novel = findViewById(R.id.novel);
        oneShots = findViewById(R.id.oneShots);
        doujin = findViewById(R.id.doujin);
        menhua = findViewById(R.id.menhua);
        menhwa = findViewById(R.id.menhwa);
        sideT = findViewById(R.id.sideTa);
        sideT1 = findViewById(R.id.sideTa1);
        topA = findViewById(R.id.linearTopAnime);
        topM = findViewById(R.id.linearTopManga);

        sideT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sideT.setBackgroundResource(R.drawable.ic_down);
                    topA.setVisibility(View.VISIBLE);
                }
                else {
                    sideT.setBackgroundResource(R.drawable.ic_side);
                    topA.setVisibility(View.GONE);

                }
            }
        });

        sideT1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sideT1.setBackgroundResource(R.drawable.ic_down);
                    topM.setVisibility(View.VISIBLE);
                }
                else {
                    sideT1.setBackgroundResource(R.drawable.ic_side);
                    topM.setVisibility(View.GONE);

                }
            }
        });


        airing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent q = new Intent(MainActivity.this, Airing.class);
                startActivity(q);
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent w = new Intent(MainActivity.this, Tv.class);
                startActivity(w);
            }
        });
//        upcoming.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent e = new Intent(MainActivity.this, Upcoming.class);
//                startActivity(e);
//            }
//        });
        special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(MainActivity.this, Special.class);
                startActivity(r);

            }
        });
        ova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(MainActivity.this, Ova.class);
                startActivity(t);
            }
        });

        manga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent y = new Intent(MainActivity.this, Manga.class);
                startActivity(y);
            }
        });
        novel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(MainActivity.this, Novel.class);
                startActivity(p);
            }
        });
        doujin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(MainActivity.this, Doujin.class);
                startActivity(u);
            }
        });
        menhwa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Menhwa.class);
                startActivity(i);
            }
        });

        menhua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(MainActivity.this, Menhua.class);
                startActivity(o);
            }
        });
        oneShots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this, OneShots.class);
                startActivity(a);
            }
        });
//        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.musim, android.R.layout.simple_spinner_item);
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.custom_spinner) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) view.findViewById(R.id.textSpinner)).setText("");
                    ((TextView) view.findViewById(R.id.textSpinner)).setHint(getItem(getCount()));
                }
                return view;
            }
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1.add("spring");
        adapter1.add("summer");
        adapter1.add("winter");
        adapter1.add("fall");
        adapter1.add("musim");

//        ArrayAdapter<CharSequence>adapter2 = ArrayAdapter.createFromResource(this,R.array.tahun, android.R.layout.simple_spinner_item);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MainActivity.this, R.layout.custom_spinner) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) view.findViewById(R.id.textSpinner)).setText("");
                    ((TextView) view.findViewById(R.id.textSpinner)).setHint(getItem(getCount()));
                }
                return view;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.add("2016");
        adapter2.add("2017");
        adapter2.add("2018");
        adapter2.add("2019");
        adapter2.add("tahun");


        tahun.setAdapter(adapter2);
        tahun.setSelection(adapter2.getCount());
//        tahun.setOnItemSelectedListener(MainActivity.this);

        musim.setAdapter(adapter1);
        musim.setSelection(adapter1.getCount());
//        musim.setOnItemSelectedListener(MainActivity.this);

        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading...");
        progressDoalog.show();


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tahunku = tahun.getSelectedItem().toString();
                String musimku = musim.getSelectedItem().toString();
                if (tahunku.equals("tahun")||musimku.equals("musim")){
                    Toast.makeText(MainActivity.this,"musim atau tahunmu kosong:)",Toast.LENGTH_SHORT).show();
                }else {
                    progressDoalog = new ProgressDialog(MainActivity.this);
                    progressDoalog.setMessage("Loading...");
                    progressDoalog.show();
                    getSeason(tahunku,musimku);
                }
                Log.e("eeee",tahunku);
                Log.e("eeee",musimku);
            }
        });

//        String tahunS = tahun.getSelectedItem().toString();
//        String musimS = musim.getSelectedItem().toString();

        if (monthC.equals(1)||monthC.equals(2)||monthC.equals(3)){
            mn = "winter";
        }else if (monthC.equals(4)||monthC.equals(5)||monthC.equals(6)){
            mn = "spring";
        }else if (monthC.equals(7)||monthC.equals(8)||monthC.equals(9)){
            mn = "summer";
        }else if (monthC.equals(10)||monthC.equals(11)||monthC.equals(12)){
            mn = "fall";
        }

        yr = yearC.toString();

        generateDataList();
        getSeason(yr,mn);

        Log.e("ppp", yr);
        Log.e("ppp", mn);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tahunku1 = tahun.getSelectedItem().toString();
                musimku1 = musim.getSelectedItem().toString();

                Intent i = new Intent(MainActivity.this, com.rifqit.animeList2.season.more.class);
                Bundle b = new Bundle();
                b.putString("thth", tahunku1);
                b.putString("msms",musimku1);

                Log.e("eeee",tahunku1);
                Log.e("eeee",musimku1);
                i.putExtras(b);
                startActivityForResult(i,2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {

            String tahunkuu = tahun.getSelectedItem().toString();
            String musimkuu = musim.getSelectedItem().toString();

            Calendar date = Calendar.getInstance();
            final Integer yearC1 = date.get(Calendar.YEAR);
            Integer month1C = date.get(Calendar.MONTH);

            if (month1C.equals(1)||month1C.equals(2)||month1C.equals(3)){
                mn1 = "winter";
            }else if (month1C.equals(4)||month1C.equals(5)||month1C.equals(6)){
                mn1 = "spring";
            }else if (month1C.equals(7)||month1C.equals(8)||month1C.equals(9)){
                mn1 = "summer";
            }else if (month1C.equals(10)||month1C.equals(11)||month1C.equals(12)){
                mn1 = "fall";
            }

            String yr1 = yearC1.toString();

            Log.e("rrr",yr1);
            Log.e("rrr",mn1);
            Log.e("rrr",tahunkuu);
            Log.e("rrr",musimkuu);

            if (tahunkuu.equals("tahun")||musimkuu.equals("musim")){
                getSeason(yr1,mn1);
            }else {
                getSeason(tahunkuu,musimkuu);
            }

        }else {
            Toast.makeText(MainActivity.this,"gagal",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        MenuItem searchIem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchIem.getActionView();
        searchView.setQueryHint("masukan judul..");
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate!=null) {
            searchPlate.setBackgroundColor(Color.parseColor("#FF9003"));
 int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText!=null) {
                searchText.setTextColor(Color.parseColor("#FFFF"));
                searchText.setHintTextColor(Color.parseColor("#FFFF"));
            }
        }
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent s = new Intent(MainActivity.this, SearchAct.class);
                s.putExtra("search",query);
                startActivity(s);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.favorite){
            Intent l = new Intent(MainActivity.this,Favorite.class);
//            startActivity(l);
            mainActivity.startActivityForResult(l,2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getSeason(String thn,String msm){
        GetDataService service = RetrofitCilentInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.getSeason(thn,msm);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String respon = response.body().string();
                        JSONObject jsonObj = new JSONObject(respon);
                        seasonObjs.clear();
                        JSONArray api = jsonObj.getJSONArray("anime");
                        for (int i = 0; i < api.length(); i++){
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

                            for (int e = 0; e < o.length(); e++){
                                JSONObject g = o.getJSONObject(e);

                                String name = g.getString("name");

                                if (name.equalsIgnoreCase("Hentai")){
                                    isSave = false;
                                    break;
                                }
                            }
                            if (isSave)
                                seasonObjs.add(ssn);
                        }
                        adapterSeason.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void generateDataList() {
        recyclerView = findViewById(R.id.recyclerSeason);
        adapterSeason = new adapterSeason(this,seasonObjs);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 3,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterSeason);
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//    }

}
