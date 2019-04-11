package com.rifqit.animeList2.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rifqit.animeList2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.CustomViewHolder> {

    private ArrayList<SearchObj> searchObjs;
    private Context context;


    public AdapterSearch(Context context, ArrayList<SearchObj> datalist){
        this.context = context;
        this.searchObjs = datalist;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder{
        public final View mView;

        TextView tittle,score,type;
        ImageView img;
        LinearLayout lklk;


        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            tittle = mView.findViewById(R.id.tittleSearch);
            score = mView.findViewById(R.id.scoreSearch);
            type = mView.findViewById(R.id.typeSearch);
            img = mView.findViewById(R.id.imgSearch);
            lklk = mView.findViewById(R.id.lkjh);

        }
    }
    @Override
    public AdapterSearch.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.lyt_search,parent, false);
        return new AdapterSearch.CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(AdapterSearch.CustomViewHolder holder, final int position){
        holder.tittle.setText(searchObjs.get(position).getTitle());
        holder.type.setText(searchObjs.get(position).getType());
        holder.score.setText(searchObjs.get(position).getScore().toString());
        Picasso.with(context).load(searchObjs.get(position).getImageUrl()).placeholder(R.drawable.ic_placeholder).into(holder.img);
        holder.lklk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DetailSearchAnime.class);
                intent.putExtra("aaaaa", new Gson().toJson(searchObjs.get(position)));
                context.startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount(){
        return searchObjs.size();
    }}























//        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private final int VIEW_TYPE_ITEM = 0;
//    private final int VIEW_TYPE_LOADING = 1;
//    public ArrayList<SearchObj> searchObjs;
//    private Context context;
//
//    public AdapterSearch(Context context,ArrayList<SearchObj> dataList) {
//        this.context = context;
//        this.searchObjs = dataList;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == VIEW_TYPE_ITEM) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_search, parent, false);
//            return new ItemViewHolder(view);
//        } else {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
//            return new LoadingViewHolder(view);
//        }
//    }
//    private class ItemViewHolder extends RecyclerView.ViewHolder {
//
//        TextView tittle,score,type;
//        ImageView img;
//        LinearLayout linearLayout;
//
//        ItemViewHolder(View itemView){
//            super(itemView);
//
//            tittle = itemView.findViewById(R.id.tittleSearch);
//            score = itemView.findViewById(R.id.scoreSearch);
//            type = itemView.findViewById(R.id.typeSearch);
//            img = itemView.findViewById(R.id.imgSearch);
//            linearLayout = itemView.findViewById(R.id.lkjh);
//        }
//    }
//    private class LoadingViewHolder extends RecyclerView.ViewHolder {
//
//        ProgressBar progressBar;
//
//        public LoadingViewHolder(@NonNull View itemView) {
//            super(itemView);
//            progressBar = itemView.findViewById(R.id.progress_bar);
//        }
//    }
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
//        if (holder instanceof ItemViewHolder) {
//            populateItemRows((ItemViewHolder) holder, position);
//        } else if (holder instanceof LoadingViewHolder) {
//            showLoadingView((LoadingViewHolder) holder, position);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return searchObjs == null ? 0 : searchObjs.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return searchObjs.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
//    }
//
//    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
//
//    }
//
//    private void populateItemRows(ItemViewHolder holder, final int position) {
//        holder.tittle.setText(searchObjs.get(position).getTitle());
//        holder.type.setText(searchObjs.get(position).getType());
//        holder.score.setText(searchObjs.get(position).getScore().toString());
//        Picasso.with(context).load(searchObjs.get(position).getImageUrl()).placeholder(R.drawable.ic_placeholder).into(holder.img);
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context , DetailSearchAnime.class);
//                intent.putExtra("aaaaa", new Gson().toJson(searchObjs.get(position)));
//                context.startActivity(intent);
//            }
//        });
//
//    }
//
//
//}
















