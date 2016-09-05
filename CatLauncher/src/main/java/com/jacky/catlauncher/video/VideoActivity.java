package com.jacky.catlauncher.video;

import android.app.Activity;
import android.os.Bundle;

import com.jacky.catlauncher.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoActivity extends Activity {

    private JCVideoPlayerStandard jcVideoPlayerStandard;
    public static final String VIDEO = "Video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.video_player);

        jcVideoPlayerStandard.setUp(
                "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "嫂子闭眼睛");
        jcVideoPlayerStandard.startPlayLocic();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        JCVideoPlayer.releaseAllVideos();
    }
}
