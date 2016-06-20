package com.jacky.launcher_42;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.sgottard.sofa.BrowseFragment;
import com.sgottard.sofa.RowsFragment;
import com.sgottard.sofa.support.BrowseSupportFragment;
import com.sgottard.sofa.support.RowsSupportFragment;
import com.sgottard.sofademo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Sebastiano Gottardo on 03/07/15.
 */
public class DemoActivity extends FragmentActivity {

    private BrowseFragment browseFragment;
    private BrowseSupportFragment browseSupportFragment;
    private android.support.v17.leanback.app.BrowseFragment browseStockFragment;
    private android.support.v17.leanback.app.BrowseSupportFragment browseStockSupportFragment;

    private View.OnClickListener searchClickListener;
    private OnItemViewClickedListener browseClickListener;

    private ArrayList<Video[]> videoList;
    private ArrayObjectAdapter adapter;

    /**
     * The combination of the following four parameters regulates the behavior of this demo.
     * useStandardAdapter specifies that a standard adapter will be used for
     * BrowseFragment (each header on the left has a matching row on the right).
     * useStockBrowseFragment specifies that the {@link android.support.v17.leanback.app.BrowseFragment}
     * component will be used instead of the custom one that is offered by the Sofa library.
     * useSupportVersion specifies that the support version of BrowseFragment will be
     * used.
     * displayFocusFragment specifies whether to display an additional fragment, in order
     * to showcase how custom fragments and manual focus handling can be achieved.
     */

    private final boolean useStandardAdapter = false;
    private final boolean useStockBrowseFragment = false;
    private final boolean useSupportVersion = false;
    private final boolean displayFocusFragment = true;
    private ImageView main_bg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_layout);

        main_bg = (ImageView) findViewById(R.id.main_bg);

        browseFragment = new BrowseFragment();
        browseSupportFragment = new BrowseSupportFragment();
        browseStockFragment = new android.support.v17.leanback.app.BrowseFragment();
        browseStockSupportFragment = new android.support.v17.leanback.app.BrowseSupportFragment();

        searchClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DemoActivity.this, "Search clicked!", Toast.LENGTH_SHORT).show();
            }
        };

        browseClickListener = new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                Toast.makeText(itemViewHolder.view.getContext(), ((Video) item).title, Toast.LENGTH_SHORT).show();
            }
        };

        parseData();

        if (useStandardAdapter) {
            loadRowsStandard();
        } else {
            loadRowsCustom(displayFocusFragment, useSupportVersion);
        }

        if (useSupportVersion) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.container, useStockBrowseFragment ? browseStockSupportFragment : browseSupportFragment, "BrowseFragment");
            transaction.commit();
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.container, useStockBrowseFragment ? browseStockFragment : browseFragment, "BrowseFragment");
            transaction.commit();
        }
    }

    /**
     * Parses the test data.
     */
    private void parseData() {
        videoList = new ArrayList<>();
        try {
            JsonElement root = new JsonParser().parse(new BufferedReader(new InputStreamReader(getResources().getAssets().open("data.json"))));
            JsonArray array = root.getAsJsonObject().get("DataArray").getAsJsonArray();

            Gson gson = new Gson();

            for (JsonElement element : array) {
                String category = element.getAsJsonObject().get("category").getAsString();
                JsonArray videoArray = element.getAsJsonObject().get("videos").getAsJsonArray();
                JsonObject videoObject;
                Video[] categoryVideos = new Video[videoArray.size()];
                Video video;
                for (int i = 0, limit = videoArray.size(); i < limit; i++) {
                    videoObject = videoArray.get(i).getAsJsonObject();
                    video = gson.fromJson(videoObject, Video.class);
                    video.category = category;
                    video.source = videoObject.get("sources").getAsJsonArray().get(0).getAsString();
                    video.card = videoObject.get("card").getAsString();
                    categoryVideos[i] = video;
                }
                videoList.add(categoryVideos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the video data into the adapter. Videos are loaded using the standard
     * BrowseFragment behavior, regardless of what type of BrowseFragment component is being used
     * (more specifically, the original Leanback one or the custom one provided by this library).
     *
     * @see BrowseFragment
     * @see android.support.v17.leanback.app.BrowseFragment
     */
    private void loadRowsStandard() {
        adapter = new ArrayObjectAdapter(new ListRowPresenter());

        for (Video[] categoryVideos : videoList) {
            ArrayObjectAdapter videoCategoryAdapter = new ArrayObjectAdapter(new VideoPresenter());
            videoCategoryAdapter.addAll(0, Arrays.asList(categoryVideos));
            ListRow row = new ListRow(new HeaderItem(categoryVideos[0].category), videoCategoryAdapter);
            adapter.add(row);
        }

        if (useStockBrowseFragment) {
            if (useSupportVersion) {
                browseStockSupportFragment.setAdapter(adapter);
                browseStockSupportFragment.setOnItemViewClickedListener(browseClickListener);
                browseStockSupportFragment.setOnSearchClickedListener(searchClickListener);
                browseStockSupportFragment.setTitle("Google Videos");
            } else {
                browseStockFragment.setAdapter(adapter);
                browseStockFragment.setOnItemViewClickedListener(browseClickListener);
                browseStockFragment.setOnSearchClickedListener(searchClickListener);
                browseStockFragment.setTitle("Google Videos");
            }
        } else {
            if (useSupportVersion) {
                browseSupportFragment.setAdapter(adapter);
                browseSupportFragment.setOnItemViewClickedListener(browseClickListener);
                browseSupportFragment.setOnSearchClickedListener(searchClickListener);
                browseSupportFragment.setTitle("Google Videos");
            } else {
                browseFragment.setAdapter(adapter);
                browseFragment.setOnItemViewClickedListener(browseClickListener);
                browseFragment.setOnSearchClickedListener(searchClickListener);
                browseFragment.setTitle("Google Videos");
            }
        }
    }

    /**
     * Loads the videos data into the adapter. Videos are loaded in two separate fragments, in order
     * to showcase how easily the use of {@link BrowseFragment} is. In addition, a custom fragment
     * that showcases manual focus handling can be displayed.
     *
     * @param addFocusTest displays an additional fragment that demonstrates how custom fragments
     *                     can be loaded and how manual focus should be handled.
     */
    private void loadRowsCustom(boolean addFocusTest, boolean useSupportVersion) {
        adapter = new ArrayObjectAdapter();

        int split = 3;
        int rowsFragmentCount = videoList.size() / split + (videoList.size() % split != 0 ? 1 : 0);

        for (int i = 0; i < rowsFragmentCount; i++) {
            if (useSupportVersion) {
                RowsSupportFragment rowsSupportFragment = new RowsSupportFragment();
                rowsSupportFragment.enableRowScaling(true);
            } else {
                RowsFragment rowsFragment = new RowsFragment();
                rowsFragment.enableRowScaling(true);
            }

            ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
            String category = "Category";

            for (int j = 0; j < split && (i * split + j) < videoList.size(); j++) {
                Video[] videos = videoList.get(i * split + j);
                ArrayObjectAdapter adapter = new ArrayObjectAdapter(new VideoPresenter());
                adapter.addAll(0, Arrays.asList(videos));
                rowsAdapter.add(new ListRow(adapter));
                category = videos[0].category;
            }

            ArrayObjectAdapter fragmentAdapter = new ArrayObjectAdapter();
            if (useSupportVersion) {
                RowsSupportFragment rowsSupportFragment = new RowsSupportFragment();
                rowsSupportFragment.enableRowScaling(true);
                rowsSupportFragment.setAdapter(rowsAdapter);
                fragmentAdapter.add(rowsSupportFragment);
            } else {
                RowsFragment rowsFragment = new RowsFragment();
                rowsFragment.enableRowScaling(true);
                rowsFragment.setAdapter(rowsAdapter);
                fragmentAdapter.add(rowsFragment);
            }
            adapter.add(new ListRow(new HeaderItem(category), fragmentAdapter));
        }

        if (addFocusTest) {
            ArrayObjectAdapter thirdAdapter = new ArrayObjectAdapter();
            thirdAdapter.add(useSupportVersion ? new DemoSupportFocusFragment() : new DemoFocusFragment());

            ListRow thirdRow = new ListRow(new HeaderItem("Focus Test"), thirdAdapter);
            adapter.add(thirdRow);
        }

        if (useSupportVersion) {
            browseSupportFragment.setAdapter(adapter);
            browseSupportFragment.setOnSearchClickedListener(searchClickListener);
            browseSupportFragment.setOnItemViewClickedListener(browseClickListener);
            browseSupportFragment.setTitle("Cool Launcher");
        } else {
            browseFragment.setAdapter(adapter);
            browseFragment.setOnSearchClickedListener(searchClickListener);
            browseFragment.setOnItemViewClickedListener(browseClickListener);
            browseFragment.setOnItemViewSelectedListener(new ItemViewSelectedListener());
            browseFragment.setTitle("Cool Launcher");
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Video) {
                Video video = (Video) item;
                Glide.with(DemoActivity.this)
                        .load(video.card)
                        .centerCrop()
                        .crossFade()
                        .into(main_bg);
            }
        }
    }

    /**
     * Presenter class for the {@link Video} object.
     */
    static class VideoPresenter extends Presenter {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
            VideoHolder viewHolder = new VideoHolder(view);
            viewHolder.root.setFocusable(true);
            viewHolder.root.setFocusableInTouchMode(true);
            viewHolder.root.setCardType(ImageCardView.CARD_TYPE_INFO_UNDER);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            Video video = (Video) item;
            VideoHolder holder = (VideoHolder) viewHolder;

            holder.root.setTitleText(video.title);
//            holder.root.setContentText(video.studio);
            holder.root.setMainImageDimensions(200, 200);
            Glide.with(holder.root.getMainImageView().getContext())
                    .load(video.card)
                    .fitCenter()
                    .into(holder.root.getMainImageView());
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
            ImageCardView cardView = (ImageCardView) viewHolder.view;
            cardView.setBadgeImage(null);
            cardView.setMainImage(null);
        }

        static class VideoHolder extends Presenter.ViewHolder {
            private ImageCardView root;

            public VideoHolder(View view) {
                super(view);

                root = (ImageCardView) view;
            }
        }
    }

    /**
     * Model class that represents a video.
     */
    private class Video {
        String category;
        String source;

        @SerializedName("title")
        String title;

        @SerializedName("background")
        String background;

        @SerializedName("card")
        String card;

        @SerializedName("studio")
        String studio;
    }
}
