package com.hannguyen.tvshowapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hannguyen.tvshowapp.R;
import com.hannguyen.tvshowapp.adapters.EpisodesAdapter;
import com.hannguyen.tvshowapp.adapters.ImageSliderAdapter;
import com.hannguyen.tvshowapp.broadcast.NetworkBroadCast;
import com.hannguyen.tvshowapp.databinding.ActivityTVShowDetailsBinding;
import com.hannguyen.tvshowapp.databinding.LayoutEpisodesBottomSheetBinding;
import com.hannguyen.tvshowapp.models.TVShow;
import com.hannguyen.tvshowapp.utilities.TempDataHolder;
import com.hannguyen.tvshowapp.viewmodels.TVShowDetailsViewModel;

import java.util.List;
import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTVShowDetailsBinding activityTVShowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private TVShow tvShow;
    private Boolean isTVShowAvailableInWatchlist = false;
    private NetworkBroadCast networkBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_v_show_details);
        networkBroadCast = new NetworkBroadCast();
        activityTVShowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_t_v_show_details);
        doInitialozation();
    }

    private void doInitialozation() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTVShowDetailsBinding.imageBack.setOnClickListener(v -> onBackPressed());
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        checkTVShowInWatchlist();
        getTVShowDetails();
    }

    private void checkTVShowInWatchlist(){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId())).
                subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow->{
                    isTVShowAvailableInWatchlist = true;
                    activityTVShowDetailsBinding.imageWatchList.setImageResource(R.drawable.ic_check);
                    compositeDisposable.dispose();
                }));
    }

    private void getTVShowDetails() {
        activityTVShowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(this, tvShowDetailsResponse -> {
            activityTVShowDetailsBinding.setIsLoading(false);
            if (tvShowDetailsResponse.getTvShowDetails() != null) {
                if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                    loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                }
                activityTVShowDetailsBinding.setTvShowImageURL(tvShowDetailsResponse.getTvShowDetails().getImagePath());
                activityTVShowDetailsBinding.imageTVShow.setVisibility(View.VISIBLE);
                activityTVShowDetailsBinding.setDescription(
                        String.valueOf(HtmlCompat.fromHtml(tvShowDetailsResponse.getTvShowDetails().getDescription(),
                                HtmlCompat.FROM_HTML_MODE_LEGACY))
                );
                activityTVShowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                activityTVShowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                activityTVShowDetailsBinding.textReadMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (activityTVShowDetailsBinding.textReadMore.getText().toString().equals("Read more")) {
                            activityTVShowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                            activityTVShowDetailsBinding.textDescription.setEllipsize(null);
                            activityTVShowDetailsBinding.textReadMore.setText(R.string.read_less);
                        } else {
                            activityTVShowDetailsBinding.textDescription.setMaxLines(4);
                            activityTVShowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                            activityTVShowDetailsBinding.textReadMore.setText(R.string.read_more);
                        }
                    }
                });
                activityTVShowDetailsBinding.setRating(String.format(Locale.getDefault(), "%.2f", Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())));
                if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
                    activityTVShowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres().get(0));
                } else {
                    activityTVShowDetailsBinding.setGenre("N/A");
                }
                activityTVShowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                activityTVShowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                activityTVShowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                activityTVShowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);
                activityTVShowDetailsBinding.buttonWebsite.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                    startActivity(intent);
                });
                activityTVShowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                activityTVShowDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                activityTVShowDetailsBinding.buttonEpisodes.setOnClickListener(v -> {
                    if (episodesBottomSheetDialog == null) {
                        episodesBottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                        layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(TVShowDetailsActivity.this),
                                R.layout.layout_episodes_bottom_sheet,
                                findViewById(R.id.episodesContainer), false);
                        episodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                        layoutEpisodesBottomSheetBinding.episodesRecyclerView.setAdapter(new EpisodesAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes()));
                        layoutEpisodesBottomSheetBinding.textTitle.setText(String.format("Episodes | %s", tvShow.getName()));
                        layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(v1 -> episodesBottomSheetDialog.dismiss());
                    }
                    //---optional section start ---//
                    FrameLayout frameLayout = episodesBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    if (frameLayout != null) {
                        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                        bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    //---optional section ens ---//
                    episodesBottomSheetDialog.show();
                });
                activityTVShowDetailsBinding.imageWatchList.setOnClickListener(v ->
                {
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    if(isTVShowAvailableInWatchlist){
                        compositeDisposable.add(tvShowDetailsViewModel.removeTVShowFromWatchlist(tvShow)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(()->{
                            isTVShowAvailableInWatchlist = false;
                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                            activityTVShowDetailsBinding.imageWatchList.setImageResource(R.drawable.ic_watchlist);
                            Toast.makeText(getApplicationContext(), "Removed from watchlist", Toast.LENGTH_SHORT).show();
                            compositeDisposable.dispose();
                        }));
                    }else{
                        compositeDisposable.add(tvShowDetailsViewModel.addToWatchlist(tvShow)
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    TempDataHolder.IS_WATCHLIST_UPDATED= true;
                                    activityTVShowDetailsBinding.imageWatchList.setImageResource(R.drawable.ic_check);
                                    Toast.makeText(TVShowDetailsActivity.this, "Added to watchlist", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();
                                }));
                    }
                });
                activityTVShowDetailsBinding.imageWatchList.setVisibility(View.VISIBLE);
                loadBasicTVShowDetails();
            }
        });

    }

    private void loadImageSlider(List<String> sliderImages) {
        activityTVShowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTVShowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTVShowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicator(sliderImages.size());
        activityTVShowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicator(int count) {
        ImageView[] indicator = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicator.length; i++) {
            indicator[i] = new ImageView(getApplicationContext());
            indicator[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive
            ));
            indicator[i].setLayoutParams(layoutParams);
            activityTVShowDetailsBinding.layoutSliderIndicators.addView(indicator[i]);

        }
        activityTVShowDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCount = activityTVShowDetailsBinding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityTVShowDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active));
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive));
            }
        }
    }

    private void loadBasicTVShowDetails() {
        activityTVShowDetailsBinding.setTvShowName(tvShow.getName());
        activityTVShowDetailsBinding.setNetworkCountry(tvShow.getNetwork() + "(" + tvShow.getCountry() + ")");
        activityTVShowDetailsBinding.setStatus(tvShow.getStatus());
        activityTVShowDetailsBinding.setStartedDate(tvShow.getStartDate());
        activityTVShowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.textNetWorkCountry.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.textStarted.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkBroadCast,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkBroadCast);
    }
}