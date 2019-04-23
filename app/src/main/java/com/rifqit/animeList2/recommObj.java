package com.rifqit.animeList2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class recommObj{

    @SerializedName("mal_id")
    @Expose
    private Integer malId;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("recommendation_url")
    @Expose
    private String recommendationUrl;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("recommendation_count")
    @Expose
    private Integer recommendationCount;

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

    public String getRecommendationUrl() {
        return recommendationUrl;
    }

    public void setRecommendationUrl(String recommendationUrl) {
        this.recommendationUrl = recommendationUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRecommendationCount() {
        return recommendationCount;
    }

    public void setRecommendationCount(Integer recommendationCount) {
        this.recommendationCount = recommendationCount;
    }

}
