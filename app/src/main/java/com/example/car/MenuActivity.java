package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.VideoView;

public class MenuActivity extends AppCompatActivity implements View.OnTouchListener{

    ImageButton ibStart, ibMultiplayer, ibprueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent svc=new Intent(this, Music.class);
        startService(svc);

        WebView v = findViewById(R.id.ibPueba);




        //LINK EACH BUTTON TO A JAVA OBJECT
        ibStart = findViewById(R.id.ibStart);
        ibStart.setOnTouchListener(this);
        ibMultiplayer = findViewById(R.id.ibMultiplayer);
        ibMultiplayer.setOnTouchListener(this);

    }

    //WITH ONTOUCH LISTENER WE CAN CHECK IF THE BUTTON IS PRESSED AND WHEN IT IS RELEASED
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.ibStart:
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ibStart.setBackground(getDrawable(R.drawable.pressed_button));
                        return true;
                    case MotionEvent.ACTION_UP:
                        //INTENT IS USED TO LAUNCH A NEW ACTIVITY, IN THIS CASE MAIN ACTIVITY
                        //YOU CAN ADD INFO TO THE INTENT (intent.putExtra();) SO THAT THE ACTIVITY TOU OPEN CAN ACCESS THAT INFORMATION
                        ibStart.setBackground(getDrawable(R.drawable.not_pressed_button));
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                        return true;
                }
                break;
            case R.id.ibMultiplayer:
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ibMultiplayer.setBackground(getDrawable(R.drawable.pressed_button));
                        return true;
                    case MotionEvent.ACTION_UP:
                        ibMultiplayer.setBackground(getDrawable(R.drawable.not_pressed_button));
                        Intent i = new Intent(this, MultiplayerMenuActivity.class);
                        startActivity(i);
                        return true;
                }
                break;
        }
        return false;
    }
}