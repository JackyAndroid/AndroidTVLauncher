/*
 * Copyright (c) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.tvleanback.data;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.media.Rating;
import android.util.Log;

import com.example.android.tvleanback.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * FetchVideoService is responsible for fetching the videos from the Internet and inserting the
 * results into a local SQLite database.
 */
public class FetchVideoService extends IntentService {
    private static final String TAG = "FetchVideoService";

    private static final String TAG_MEDIA = "videos";
    private static final String TAG_GOOGLE_VIDEOS = "googlevideos";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_STUDIO = "studio";
    private static final String TAG_SOURCES = "sources";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_CARD_THUMB = "card";
    private static final String TAG_BACKGROUND = "background";
    private static final String TAG_TITLE = "title";

    /**
     * Creates an IntentService with a default name for the worker thread.
     */
    public FetchVideoService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        try {
            JSONObject videoData = fetchJSON(getResources().getString(R.string.catalog_url));
            buildMedia(videoData);
        } catch (IOException e) {
            Log.e(TAG, "An error occurred when fetching videos.", e);
        } catch (JSONException e) {
            Log.e(TAG, "A JSON error occurred when fetching videos.", e);
        }
    }

    private void buildMedia(JSONObject jsonObj) throws JSONException {

        JSONArray categoryArray = jsonObj.getJSONArray(TAG_GOOGLE_VIDEOS);
        List<ContentValues> videosToInsert = new ArrayList<>();

        for (int i = 0; i < categoryArray.length(); i++) {
            JSONArray videoArray;

            JSONObject category = categoryArray.getJSONObject(i);
            String categoryName = category.getString(TAG_CATEGORY);
            videoArray = category.getJSONArray(TAG_MEDIA);

            for (int j = 0; j < videoArray.length(); j++) {
                JSONObject video = videoArray.getJSONObject(j);

                // If there are no URLs, skip this video entry.
                JSONArray urls = video.optJSONArray(TAG_SOURCES);
                if (urls == null || urls.length() == 0) {
                    continue;
                }

                String title = video.getString(TAG_TITLE);
                String description = video.getString(TAG_DESCRIPTION);
                String videoUrl = (String) urls.get(0); // Get the first video only.
                String bgImageUrl = video.getString(TAG_BACKGROUND);
                String cardImageUrl = video.getString(TAG_CARD_THUMB);
                String studio = video.getString(TAG_STUDIO);

                ContentValues videoValues = new ContentValues();
                videoValues.put(VideoContract.VideoEntry.COLUMN_CATEGORY, categoryName);
                videoValues.put(VideoContract.VideoEntry.COLUMN_NAME, title);
                videoValues.put(VideoContract.VideoEntry.COLUMN_DESC, description);
                videoValues.put(VideoContract.VideoEntry.COLUMN_VIDEO_URL, videoUrl);
                videoValues.put(VideoContract.VideoEntry.COLUMN_CARD_IMG, cardImageUrl);
                videoValues.put(VideoContract.VideoEntry.COLUMN_BG_IMAGE_URL, bgImageUrl);
                videoValues.put(VideoContract.VideoEntry.COLUMN_STUDIO, studio);

                // Fixed defaults.
                videoValues.put(VideoContract.VideoEntry.COLUMN_CONTENT_TYPE, "video/mp4");
                videoValues.put(VideoContract.VideoEntry.COLUMN_IS_LIVE, false);
                videoValues.put(VideoContract.VideoEntry.COLUMN_AUDIO_CHANNEL_CONFIG, "2.0");
                videoValues.put(VideoContract.VideoEntry.COLUMN_PRODUCTION_YEAR, 2014);
                videoValues.put(VideoContract.VideoEntry.COLUMN_DURATION, 0);
                videoValues.put(VideoContract.VideoEntry.COLUMN_RATING_STYLE,
                        Rating.RATING_5_STARS);
                videoValues.put(VideoContract.VideoEntry.COLUMN_RATING_SCORE, 3.5f);
                videoValues.put(VideoContract.VideoEntry.COLUMN_PURCHASE_PRICE,
                        getResources().getString(R.string.buy_2));
                videoValues.put(VideoContract.VideoEntry.COLUMN_RENTAL_PRICE,
                        getResources().getString(R.string.rent_2));
                videoValues.put(VideoContract.VideoEntry.COLUMN_ACTION,
                        getResources().getString(R.string.global_search));

                // TODO: Get these dimensions.
                videoValues.put(VideoContract.VideoEntry.COLUMN_VIDEO_WIDTH, 1280);
                videoValues.put(VideoContract.VideoEntry.COLUMN_VIDEO_HEIGHT, 720);

                videosToInsert.add(videoValues);
            }
        }

        getContentResolver().bulkInsert(VideoContract.VideoEntry.CONTENT_URI,
                videosToInsert.toArray(new ContentValues[videosToInsert.size()]));
    }

    /**
     * Fetch JSON object from a given URL.
     *
     * @return the JSONObject representation of the response
     * @throws JSONException
     * @throws IOException
     */
    private JSONObject fetchJSON(String urlString) throws JSONException, IOException {
        BufferedReader reader = null;
        java.net.URL url = new java.net.URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
                    "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            return new JSONObject(json);
        } finally {
            urlConnection.disconnect();
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "JSON feed closed", e);
                }
            }
        }
    }
}
