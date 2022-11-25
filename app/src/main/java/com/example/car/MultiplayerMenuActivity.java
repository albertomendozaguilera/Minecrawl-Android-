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
        ibJoin = findViewById(R.id.ibJoin);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Intent intent = new Intent(this, MainActivity.class);
        switch (view.getId()) {
            case R.id.ibHost:
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_UP:
                        //INTENT IS USED TO LAUNCH A NEW ACTIVITY, IN THIS CASE MAIN ACTIVITY
                        //YOU CAN ADD INFO TO THE INTENT (intent.putExtra();) SO THAT THE ACTIVITY TOU OPEN CAN ACCESS THAT INFORMATION
                        intent.putExtra("game", 1);
                        startActivity(intent);
                        return true;
                }
                break;
            case R.id.ibJoin:
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_UP:
                        //INTENT IS USED TO LAUNCH A NEW ACTIVITY, IN THIS CASE MAIN ACTIVITY
                        //YOU CAN ADD INFO TO THE INTENT (intent.putExtra();) SO THAT THE ACTIVITY TOU OPEN CAN ACCESS THAT INFORMATION
                        intent.putExtra("game", 2);
                        startActivity(intent);
                        return true;
                }
                break;
        }

        return false;
    }
}