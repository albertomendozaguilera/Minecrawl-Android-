package com.example.car;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class MultiplayerMenuActivity extends AppCompatActivity implements View.OnTouchListener{

    ImageButton ibHost, ibJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_menu);

        ibHost = findViewById(R.id.ibHost);
        ibHost.setOnTouchListener(this);
        ibJoin = findViewById(R.id.ibJoin);
        ibJoin.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.ibHost:
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ibHost.setBackground(getDrawable(R.drawable.pressed_button));
                        return true;
                    case MotionEvent.ACTION_UP:
                        //INTENT IS USED TO LAUNCH A NEW ACTIVITY, IN THIS CASE MAIN ACTIVITY
                        //YOU CAN ADD INFO TO THE INTENT (intent.putExtra();) SO THAT THE ACTIVITY TOU OPEN CAN ACCESS THAT INFORMATION
                        ibHost.setBackground(getDrawable(R.drawable.not_pressed_button));
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("game", 1);
                        startActivity(intent);
                        return true;
                }
                break;
            case R.id.ibJoin:
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ibJoin.setBackground(getDrawable(R.drawable.pressed_button));
                        return true;
                    case MotionEvent.ACTION_UP:
                        //INTENT IS USED TO LAUNCH A NEW ACTIVITY, IN THIS CASE MAIN ACTIVITY
                        //YOU CAN ADD INFO TO THE INTENT (intent.putExtra();) SO THAT THE ACTIVITY TOU OPEN CAN ACCESS THAT INFORMATION
                        ibJoin.setBackground(getDrawable(R.drawable.not_pressed_button));
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("game", 2);
                        startActivity(intent);
                        return true;
                }
                break;
        }

        return false;
    }
}