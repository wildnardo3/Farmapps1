package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview);

        videoView = findViewById(R.id.videoView);

        // Configura la URI del video desde la carpeta raw
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_video);
        videoView.setVideoURI(videoUri);

        // Reproduce el video tras una breve pausa de 1 segundo
        new Handler().postDelayed(() -> {
            videoView.start();
        }, 1000);

        // Listener para redirigir al LoginActivity cuando termine el video
        videoView.setOnCompletionListener(mediaPlayer -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}
