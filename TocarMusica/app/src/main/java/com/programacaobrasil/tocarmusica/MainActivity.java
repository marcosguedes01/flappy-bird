package com.programacaobrasil.tocarmusica;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button btnTocar;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
        initializeEventsComponents();
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer!=null && mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        super.onDestroy();
    }

    private void initializeComponents()
    {
        btnTocar = (Button)findViewById(R.id.btnTocar);
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.musica);
    }

    private void initializeEventsComponents()
    {
        btnTocar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying())
                {
                    pausarMusica();
                }
                else
                {
                    tocarMusica();
                }
            }
        });
    }

    private void tocarMusica()
    {
        if (mediaPlayer!=null){
            mediaPlayer.start();
            btnTocar.setText("Pausar");
        }
    }

    private void pausarMusica()
    {
        if (mediaPlayer!=null)
        {
            mediaPlayer.pause();
            btnTocar.setText("Tocar");
        }
    }

    private void pararMusica()
    {
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            btnTocar.setText("Tocar");
        }
    }
}
