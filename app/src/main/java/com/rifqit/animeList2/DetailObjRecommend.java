package com.rifqit.animeList2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailObjRecommend {


    @SerializedName("mal_id")
    @Expose
    private Integer malId;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
//    @SerializedName("trailer_url")
//    @Expose
//    private String trailerUrl;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("episodes")
    @Expose
    private Integer episodes;
    @SerializedName("score")
    @Expose
    private Double score;
    @SerializedName("members")
    @Expose
    private Integer members;
    @SerializedName("synopsis")
    @Expose
    private String synopsis;

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

//    public String getTrailerUrl() {
//        return trailerUrl;
//    }
//
//    public void setTrailerUrl(String trailerUrl) {
//        this.trailerUrl = trailerUrl;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Integer episodes) {
        this.episodes = episodes;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

}
