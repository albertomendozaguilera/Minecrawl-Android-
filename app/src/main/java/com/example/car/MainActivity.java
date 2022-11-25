package com.example.car;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    Car car;
    Maze m;
    int partyId;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TableLayout tlBoard;
    ImageButton btUp, btLeft, btRight, btMenu, btRespawn, btTitleScreen;
    ConstraintLayout clDeathScreen;
    LinearLayout game;
    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, ivSpider, deathScreen;
    ArrayList <ImageView> ivList;
    int dimensions = 20;
    final int VIEWRANGE = 3;

    //ONCREATE IS EXECUTED THE FIRST (LIKE A MAIN IN JAVA)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ACTIVITY IS A SCREEN(XML FILE) AND A CLASS WITH THE LOGIC OF THAT SCREEN
        //IN THIS CASE THIS ACTIVITY IS: activity_main.xml and MainActivity.java

        //TO OPEN A NEW ACTIVITY(A NEW SCREEN) WE USE INTENT, YOU ALSO CAN ADD INFO TO THAT INTENT TO BE SENT TO THE NEW ACTIVITY
        //HERE WITH GET INTENT, WE ARE RECEIVING INFO FROM MENU ACTIVITY WHEN IN THE MENU SCREEN WE PRESS THE BUTTON START
        Intent intent = getIntent();
        int game = intent.getIntExtra("game", 0);
        //GAME=0: SINGLEPLAYER      GAME=1: MULTIPLAYER-HOST      GAME=2: MULTIPLAYER-CLIENT
        switch (game) {
            case 0:
                m = new Maze(dimensions);
                m.draw();
                m.getGrid()[dimensions-1][dimensions-1] = '4';
                initializeGame();
                car = new Car(1);
                render();
                break;
            case 1:
                m = new Maze(dimensions);
                m.draw();
                car = new Car(1);
                //TODO UPLOAD MAZE TO FIREBASE
                connectToFirebase();
                initializeGame();
                render();
                break;
            case 2:
                //TODO RETRIEVE MAZE FROM FIREBASE
                initializeGame();

                car = new Car(2);
                render();
        }
    }

    //TODO GET A INSTANCE OF THE DATABASE ON FIREBASE
    public void connectToFirebase(){
        database = FirebaseDatabase.getInstance("https://online-car-8c397-default-rtdb.europe-west1.firebasedatabase.app");
        myRef = database.getReference("games");
        myRef.child("1");
        System.out.println("yuuup     " + myRef.toString());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                if (snap.getKey().toString() == "game" + car.getId()){

                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });



    }


    //LINK EACH VIEW(ELEMENTS LIKE BUTTONS, IMAGES..) TO A JAVA OBJECT SO YOU CAN WORK WITH IT
    @SuppressLint("WrongViewCast")
    public void initializeGame () {

        tlBoard = findViewById(R.id.tbBoard);

        ivList = new ArrayList();
        iv1 = findViewById(R.id.iv1);
        ivList.add(iv1);
        iv2 = findViewById(R.id.iv2);
        ivList.add(iv2);
        iv3 = findViewById(R.id.iv3);
        ivList.add(iv3);
        iv4 = findViewById(R.id.iv4);
        ivList.add(iv4);
        iv5 = findViewById(R.id.iv5);
        ivList.add(iv5);
        iv6 = findViewById(R.id.iv6);
        ivList.add(iv6);
        iv7 = findViewById(R.id.iv7);
        ivList.add(iv7);
        iv8 = findViewById(R.id.iv8);
        ivList.add(iv8);
        iv9 = findViewById(R.id.iv9);
        ivList.add(iv9);

        ivSpider = findViewById(R.id.ivSpider);

        game = findViewById(R.id.llGame);
        deathScreen = findViewById(R.id.ivDeathScreen);
        clDeathScreen = findViewById(R.id.clDeathScreen);
        game.bringToFront();

        btUp = findViewById(R.id.ibMove);
        btUp.setOnTouchListener(this);
        btLeft = findViewById(R.id.ibLeft);
        btLeft.setOnTouchListener(this);
        btRight = findViewById(R.id.ibRight);
        btRight.setOnTouchListener(this);
        btMenu = findViewById(R.id.ibMenu);
        btMenu.setOnTouchListener(this);
        btRespawn = findViewById(R.id.ibRespawn);
        btRespawn.setOnTouchListener(this);
        btTitleScreen = findViewById(R.id.ibTitleScreen);
        btTitleScreen.setOnTouchListener(this);

    }

    //DRAW THE CAR AND ALL ELEMENTS NEAR TO IT
    public void addCar (Car car, ArrayList<ImageView> ivList) {
        render();
    }

    //DRAW ELEMENTS NEAR TO CAR ACCORDING TO THE NUMBER IN EACH BOX OF THE MAP
    public void drawViews (int imageId, int i, int j) {
            ImageView iv = ivList.get(imageId);
            switch (m.getGrid()[i][j]) {
                case '0':
                    iv.setBackground(getDrawable(R.drawable.sand));
                    break;
                case '1':
                    iv.setBackground(getDrawable(R.drawable.cactusinsand));
                    break;
                case '2':
                    iv.setBackground(getDrawable(R.drawable.lavainsand));
                    break;
                case '3':
                    iv.setBackground(getDrawable(R.drawable.magma));
                    break;
                case '4':
                    //TODO WIN

        }
    }

    //CHECK IF THE CAR IS ON A BOUND AND IF IT IS, DRAW THE OPPOSITE SIDE OF THE BOARD (THE MAP IS LIKE A SPHERE)
    public void render() {
        int posX = 0;
        int posY = 0;
        int imageId = 0;
        for (int i = car.getX()-1; i < car.getX()-1 + VIEWRANGE; i++) {
            if(i >= dimensions){
                posX = 0;
            }else if (i < 0){
                posX = dimensions - 1;
            }else{
                posX = i;
            }
            for (int j = car.getY()-1; j < car.getY()-1 + VIEWRANGE; j++){
                if(j >= dimensions){
                    posY = 0;
                }else if (j < 0){
                    posY = dimensions - 1;
                }else{
                    posY = j;
                }
                drawViews(imageId, posX, posY);
                imageId++;
            }
        }
    }

    //MOVE THE CAR TO THE NEXT BOX ACCORDING TO ITS ROTATION
    public void carMove(Car c) {
        switch (c.getRotation()){
            case 0:
                if(c.getY()+1 >= dimensions){
                    checkCrash(c, c.getX(),c.getY()+1-dimensions);
                }else{
                    checkCrash(c, c.getX(),c.getY()+1);
                }
                System.out.println("position: " + c.getX() + " - " + c.getY());
                break;
            case 1:
                if(c.getX()+1 >= dimensions){
                    checkCrash(c, c.getX()+1-dimensions,c.getY());
                }else{
                    checkCrash(c, c.getX()+1,c.getY());
                }
                System.out.println("position: " + c.getX() + " - " + c.getY());
                break;
            case 2:
                if(c.getY()-1 < 0){
                    checkCrash(c, c.getX(),c.getY()-1+dimensions);
                }else{
                    checkCrash(c, c.getX(),c.getY()-1);
                }
                System.out.println("position: " + c.getX() + " - " + c.getY());
                break;
            case 3:
                if(c.getX()-1 < 0){
                    checkCrash(c, c.getX()-1+dimensions,c.getY());
                }else{
                    checkCrash(c, c.getX()-1,c.getY());
                }
                System.out.println("position: " + c.getX() + " - " + c.getY());
                break;
        }
    }
    //ROTATE CAR AND ITS VIEW TO THE LEFT
    public void carLeft(Car c){
        if(c.getRotation()-1 < 0) {c.setRotation(4);}
        c.setRotation(c.getRotation() - 1);
        ivSpider.setRotation(c.getDirection()[c.getRotation()]);
    }
    //ROTATE CAR AND ITS VIEW TO RIGHT
    public void carRight(Car c){
        if(c.getRotation()+1 > 3) {c.setRotation(-1);}
        c.setRotation(c.getRotation() + 1);
        ivSpider.setRotation(c.getDirection()[c.getRotation()]);
    }

    //CHECK THE BOX WHERE THE CAR IS GOING TO MOVE.
    // 0-NO PROBLEM
    // 1-CACTUS(LOSE A HEART AND CANNOT ADVANCE)
    // 2-LAVA(DEATH REGARDLESS OF HEARTS)
    // 3-MAGMA(CAN ADVANCE BUT LOSE A HEART)
    public void checkCrash (Car car, int x, int y) {
        switch (m.getGrid()[x][y]){
            case '0':
                car.setX(x);
                car.setY(y);
                render();
                break;
            case '1':
                car.loseHeart();
                if (car.getLife() <= 0) {
                    setDeathScreenVisible();
                    setButtonsInvisible();
                }
                break;
            case '2':
                car.setX(x);
                car.setY(y);
                render();
                setDeathScreenVisible();
                setButtonsInvisible();
                break;
            case '3':
                car.setX(x);
                car.setY(y);
                car.loseHeart();
                render();
                if (car.getLife() <= 0) {
                    setDeathScreenVisible();
                    setButtonsInvisible();
                }
                break;
        }
    }

    //IN CASE OF DEATH EXECUTE NEXT TWO FUNCTIONS
    public void setButtonsInvisible() {
        btMenu.setVisibility(View.INVISIBLE);
        btLeft.setVisibility(View.INVISIBLE);
        btRight.setVisibility(View.INVISIBLE);
        btUp.setVisibility(View.INVISIBLE);
    }
    public void setDeathScreenVisible() {
        deathScreen.setVisibility(View.VISIBLE);
        deathScreen.bringToFront();
        clDeathScreen.setVisibility(View.VISIBLE);
        clDeathScreen.bringToFront();
    }


    //TODO OPEN MENU ACTIVITY
    public void menu(){
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }

    //LISTENER TO ALL BUTTONS(VIEW) IN THE SCREEN


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.ibMove:
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btUp.setBackground(getDrawable(R.drawable.pressed_button));
                        return true;
                    case MotionEvent.ACTION_UP:
                        btUp.setBackground(getDrawable(R.drawable.not_pressed_button));
                        carMove(car);
                        return true;
                }
                break;
            case R.id.ibLeft:
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btLeft.setBackground(getDrawable(R.drawable.left_turn_button_pressed));
                        return true;
                    case MotionEvent.ACTION_UP:
                        btLeft.setBackground(getDrawable(R.drawable.left_turn_button));
                        carLeft(car);
                        return true;
                }
                break;
            case R.id.ibRight:
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btRight.setBackground(getDrawable(R.drawable.right_turn_button_pressed));
                        return true;
                    case MotionEvent.ACTION_UP:
                        btRight.setBackground(getDrawable(R.drawable.right_turn_button));
                        carRight(car);
                        return true;
                }
                break;
            case R.id.ibMenu:
            case R.id.ibTitleScreen:
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btMenu.setBackground(getDrawable(R.drawable.pressed_button));
                        btTitleScreen.setBackground(getDrawable(R.drawable.pressed_button));
                        return true;
                    case MotionEvent.ACTION_UP:
                        btMenu.setBackground(getDrawable(R.drawable.not_pressed_button));
                        btTitleScreen.setBackground(getDrawable(R.drawable.not_pressed_button));
                        menu();
                        return true;
                }
                break;
            case R.id.ibRespawn:
                //RESTART GAME
                //GENERATE A NEW MAIN ACTIVITY(GAME SCREEN), FINISH CURRENT AND START THE NEW ACTIVITY
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btRespawn.setBackground(getDrawable(R.drawable.pressed_button));
                        return true;
                    case MotionEvent.ACTION_UP:
                        btRespawn.setBackground(getDrawable(R.drawable.not_pressed_button));
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        return true;
                }
                break;
        }
        return false;
    }
}