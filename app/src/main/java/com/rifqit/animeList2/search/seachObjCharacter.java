package com.rifqit.animeList2.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class seachObjCharacter {

    @SerializedName("mal_id")
    @Expose
    private Integer malId;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("name")
    @Expose
    private String name;
//    @SerializedName("alternative_names")
//    @Expose
//    private List<Object> alternativeNames = null;
//    @SerializedName("anime")
//    @Expose
//    private List<Anime> anime = null;
//    @SerializedName("manga")
//    @Expose
//    private List<Object> manga = null;

    public Integer getMalId() {
        return malId;
    }

    public void setMalId(Integer malId) {
        this.malId = malId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public List<Object> getAlternativeNames() {
//        return alternativeNames;
//    }
//
//    public void setAlternativeNames(List<Object> alternativeNames) {
//        this.alternativeNames = alternativeNames;
//    }

//    public List<Anime> getAnime() {
//        return anime;
//    }

//    public void setAnime(List<Anime> anime) {
//        this.anime = anime;
//    }
//
//    public List<Object> getManga() {
//        return manga;
//    }
//
//    public void setManga(List<Object> manga) {
//        this.manga = manga;
//    }
}
