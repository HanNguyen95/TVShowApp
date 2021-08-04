package com.hannguyen.tvshowapp.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TVShowDetails {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("description_source")
    @Expose
    private String descriptionSource;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private Object endDate;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("runtime")
    @Expose
    private int runtime;
    @SerializedName("network")
    @Expose
    private String network;
    @SerializedName("youtube_link")
    @Expose
    private Object youtubeLink;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("image_thumbnail_path")
    @Expose
    private String imageThumbnailPath;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("rating_count")
    @Expose
    private String ratingCount;
    @SerializedName("countdown")
    @Expose
    private Object countdown;
    @SerializedName("genres")
    @Expose
    private List<String> genres = null;
    @SerializedName("pictures")
    @Expose
    private List<String> pictures = null;
    @SerializedName("episodes")
    @Expose
    private List<Episode> episodes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionSource() {
        return descriptionSource;
    }

    public void setDescriptionSource(String descriptionSource) {
        this.descriptionSource = descriptionSource;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Object getEndDate() {
        return endDate;
    }

    public void setEndDate(Object endDate) {
        this.endDate = endDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Object getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(Object youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageThumbnailPath() {
        return imageThumbnailPath;
    }

    public void setImageThumbnailPath(String imageThumbnailPath) {
        this.imageThumbnailPath = imageThumbnailPath;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Object getCountdown() {
        return countdown;
    }

    public void setCountdown(Object countdown) {
        this.countdown = countdown;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}