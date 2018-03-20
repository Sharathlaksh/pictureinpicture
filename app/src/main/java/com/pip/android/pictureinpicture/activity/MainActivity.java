package com.pip.android.pictureinpicture.activity;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Rational;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.pip.android.pictureinpicture.R;


public class MainActivity extends AppCompatActivity {

    private VideoView mVideoPlayer;
    private Button mPIP;
    private ConstraintLayout mRootView;
    private final PictureInPictureParams.Builder pipParamsBuilder =
            new PictureInPictureParams.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_playback);
        initUI();
        setVideoView();
    }

    private void initUI() {
        mRootView = (ConstraintLayout) findViewById(R.id.constraint_layout);
        mVideoPlayer = (VideoView) findViewById(R.id.videoView);

        findViewById(R.id.play).setOnClickListener(onClickListener);
        findViewById(R.id.stop).setOnClickListener(onClickListener);
        mPIP = (Button) findViewById(R.id.pip);
        mPIP.setOnClickListener(onClickListener);
    }

    private final View.OnClickListener onClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.play: {
                            setVideoView();
                            mVideoPlayer.start();
                            break;
                        }
                        case R.id.stop: {
                            mVideoPlayer.stopPlayback();
                            break;
                        }
                        case R.id.pip:
                            pictureInPictureMode();
                            break;
                    }
                }
            };

    private void setVideoView() {
        try {
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(mVideoPlayer);
            String mDefaultVideoUrl = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4";
            Uri video = Uri.parse(mDefaultVideoUrl);
            mVideoPlayer.setMediaController(mediaController);
            mVideoPlayer.setVideoURI(video);
        } catch (Exception e) {
            // TODO: handle exception
            Snackbar snackbar = Snackbar
                    .make(mRootView, "Error connecting", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    private void pictureInPictureMode() {
        Rational aspectRatio = new Rational(mVideoPlayer.getWidth(), mVideoPlayer.getHeight() + 500);
        pipParamsBuilder.setAspectRatio(aspectRatio).build();
        enterPictureInPictureMode(pipParamsBuilder.build());
    }

    @Override
    public void onUserLeaveHint() {
        if (!isInPictureInPictureMode()) {
            Rational aspectRatio = new Rational(mVideoPlayer.getWidth(), mVideoPlayer.getHeight() + 500);
            pipParamsBuilder.setAspectRatio(aspectRatio).build();
            enterPictureInPictureMode(pipParamsBuilder.build());
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode,
                                              Configuration newConfig) {
        if (isInPictureInPictureMode) {
            mPIP.setVisibility(View.GONE);
        } else {
            mPIP.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNewIntent(Intent i) {
        setVideoView();
    }

    @Override
    public void onStop() {
        if (mVideoPlayer.isPlaying()) {
            mVideoPlayer.stopPlayback();
        }
        super.onStop();
    }
}

