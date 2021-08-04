package com.hannguyen.tvshowapp.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hannguyen.tvshowapp.models.TVShow;

import java.util.List;

public class TVShowResponse {

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("pages")
    @Expose
    private int pages;
    @SerializedName("tv_shows")
    @Expose
    private List<TVShow> tvShows;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<TVShow> getTvShows() {
        return tvShows;
    }

    public void setTvShows(List<TVShow> tvShows) {
        this.tvShows = tvShows;
    }
}