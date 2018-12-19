package me.azhar.androidutility;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    String SrcPath = "/storage/emulated/0/Movies/Mr. Bean - The Bus Stop.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        VideoView myVideoView = (VideoView)findViewById(R.id.myvideoview);
        myVideoView.setVideoPath(SrcPath);
        myVideoView.setMediaController(new MediaController(this));
        myVideoView.requestFocus();
        myVideoView.start();
    }
}
