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

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.rifqit.animeList2.Database.GetDataService;
import com.rifqit.animeList2.Database.RetrofitCilentInstance;
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
import com.rifqit.animeList2.favorite.Favorite;
import com.rifqit.animeList2.search.SearchAct;
import com.rifqit.animeList2.season.adapterSeason;
import com.rifqit.animeList2.season.seasonObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    Spinner musim ,tahun;
//    String tahunS,musimS;
    ProgressDialog progressDoalog;
    ImageButton search;
    RelativeLayout more;
    com.rifqit.animeList2.season.adapterSeason adapterSeason;
    private String TAG = MainActivity.class.getSimpleName();
    private ArrayList<seasonObj> seasonObjs = new ArrayList<>();
    ArrayList<ScheduleObj> scheduleObjs = new ArrayList<>();
    RecyclerView recyclerView;
    TextView airing,tv,upcoming,special,ova;
    TextView manga,novel,oneShots,doujin,menhwa,menhua;
    String yr,mn;
    String mn1;
    String tahunku1,musimku1;
    MainActivity mainActivity = this;
    LinearLayout topA,topM;
    ToggleButton sideT,sideT1;
    String day;
    SliderLayout sliderLayout;
    HashMap<String, String> HashMapForURL ;
    String dayku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sliderLayout = findViewById(R.id.slider);
        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        Calendar date = Calendar.getInstance();
        final Integer yearC = date.get(Calendar.YEAR);
        Integer monthC = date.get(Calendar.MONTH);
        Integer dayy = date.get(Calendar.DAY_OF_WEEK);

        if (dayy.equals(1)){
            dayku = "sunday";
        }else if (dayy.equals(2)){
            dayku = "monday";
        }else if (dayy.equals(3)){
            dayku = "tuesday";
        }else if (dayy.equals(4)){
            dayku = "wednesday";
        }else if (dayy.equals(5)){
            dayku = "thursday";
        }else if (dayy.equals(6)){
            dayku = "friday";
        }else if (dayy.equals(7)){
            dayku = "saturday";
        }

        Log.e("dayku", dayku);

        schedule(dayku);

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

        musim.setAdapter(adapter1);
        musim.setSelection(adapter1.getCount());

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
        if (requestCode == 2 && resultCode ==RESULT_OK) {
            adapterSeason.notifyDataSetChanged();

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
            Intent l = new Intent(MainActivity.this, Favorite.class);
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
    public void schedule(final String day){
        GetDataService service = RetrofitCilentInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.getSchedule(day);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String respon = response.body().string();
                        JSONObject jsonObj = new JSONObject(respon);
//                        seasonObjs.clear();
                        JSONArray api = jsonObj.getJSONArray(day);
                        for (int i = 0; i < 6; i++){
                            JSONObject c = api.getJSONObject(i);

                            Integer mal_id1 = c.getInt("mal_id");
                            String url1 = c.getString("url");
                            String tittle1 = c.getString("title");
                            String imageUrl1 = c.getString("image_url");
                            String synopsis1 = c.getString("synopsis");
                            String type1 = c.getString("type");
                            String airing_start1 = c.getString("airing_start");
//                            Integer episodes = c.getInt("episodes");
                            Integer members1 = c.getInt("members");

                            final ScheduleObj s = new ScheduleObj();
                            s.setMalId(mal_id1);
                            s.setUrl(url1);
                            s.setImageUrl(imageUrl1);
                            s.setTitle(tittle1);
                            s.setSynopsis(synopsis1);
                            s.setType(type1);
                            s.setAiringStart(airing_start1);
//                            s.setEpisodes(episodes);
                            s.setMembers(members1);
                            scheduleObjs.add(s);

                            HashMapForURL = new HashMap<String, String>();
                            HashMapForURL.put(tittle1, imageUrl1);

                            for(String name : HashMapForURL.keySet()){

                                TextSliderView textSliderView = new TextSliderView(MainActivity.this);

                                textSliderView
                                        .description(name)
                                        .image(HashMapForURL.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                        .setOnSliderClickListener(MainActivity.this);

                                textSliderView.bundle(new Bundle());

                                textSliderView.getBundle().putString("extra",name);

                                sliderLayout.addSlider(textSliderView);

                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        Intent intentX = new Intent(MainActivity.this , Detail_slider.class);
                                        intentX.putExtra("schedule", new Gson().toJson(s));
                                        startActivity(intentX);
                                    }
                                });
                            }
                            sliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
                            sliderLayout.setCustomAnimation(new DescriptionAnimation());
                            sliderLayout.setDuration(3000);
                            sliderLayout.addOnPageChangeListener(MainActivity.this);
                        }

                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getLocalizedMessage()+111, Toast.LENGTH_SHORT).show();
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
                progressDoalog.dismiss();
//                Toast.makeText(MainActivity.this, t.getLocalizedMessage()+11, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onStop() {

        sliderLayout.startAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("slider demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
