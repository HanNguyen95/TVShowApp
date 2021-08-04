package com.hannguyen.tvshowapp.listeners;

import com.hannguyen.tvshowapp.models.TVShow;

public interface WatchlistListener {
    void onTVShowClicked(TVShow tvShow);

    void removeTVShowFromWatchlist(TVShow tvShow,int position);
}
