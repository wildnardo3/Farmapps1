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

        // Ruta del video en la carpeta raw
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_video);
        videoView.setVideoURI(videoUri);

        // Esperar un segundo antes de iniciar el video
        new Handler().postDelayed(() -> {
            videoView.start();
        }, 1000);

        // Listener para detectar cuándo termina el video
        videoView.setOnCompletionListener(mediaPlayer -> {
            // Redirigir al usuario al LoginActivity cuando termina el video
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}
