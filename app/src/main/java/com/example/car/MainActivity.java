package com.example.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    Car car, car2;
    Maze m;
    char[][] arrayMaze;
    int partyId;
    FirebaseDatabase database;
    DatabaseReference partyRef, car2Ref, mazeRef;
    TableLayout tlBoard;
    ImageButton btUp, btLeft, btRight, btMenu, btRespawn, btTitleScreen;
    ConstraintLayout clDeathScreen;
    LinearLayout game;
    MediaPlayer spiderStep, steveStep;
    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, ivSpider, deathScreen, ivHearts, imageView2;
    TextView tvScoreNumber, tvScoreNumber2;
    ArrayList <ImageView> ivList;
    LottieAnimationView animationView;
    final int[] heartsId = {R.drawable.healthbar0, R.drawable.healthbar1, R.drawable.healthbar2, R.drawable.healthbar3, R.drawable.healthbar4, R.drawable.healthbar5, R.drawable.healthbar6, R.drawable.healthbar7, R.drawable.healthbar8, R.drawable.healthbar9, R.drawable.healthbar10};
    int dimensions = 10;
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
                car = new Car(1);
                initializeGame();
                render();
                ivHearts.bringToFront();
                break;
            case 1:
                m = new Maze(dimensions);
                m.draw();
                car = new Car(1);
                connectToFirebase();
                initializeGame();
                break;
            case 2:
                m = new Maze(dimensions);
                m.draw();
                car = new Car(2);
                car.setX(dimensions - 1);
                car.setY(dimensions-1);
                connectToFirebase();
                initializeGame();
                break;
        }
    }

    public void connectToFirebase(){

        switch (car.getId()){
            case 1:
                car2 = new Car(2);
                car2.setX(dimensions-1);
                car2.setY(dimensions-1);
                break;
            case 2:
                car2 = new Car(1);
        }

        database = FirebaseDatabase.getInstance("https://online-car-8c397-default-rtdb.europe-west1.firebasedatabase.app");
        partyRef = database.getReference("games").child("1");
        mazeRef = database.getReference("games").child("1").child("maze");

        if(car.getId() == 1) {
            mazeRef.setValue(parseMazeToStringList(m.getGrid()));
            partyRef.child("car" + car.getId()).setValue(car);
            partyRef.child("car" + car2.getId()).setValue(car2);
        }

        if(car.getId() == 2) {

            mazeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arrayMaze = parseMazeToCharList((List<List<String>>) snapshot.getValue());
                m.setGrid(arrayMaze);
            }
            @Override
            public void onCancelled(DatabaseError error) {}
            });
        }

        partyRef.child("car"+car2.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                car2 = dataSnapshot.getValue(Car.class);
                render();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void actualizeFirebase (Car car) {
        partyRef.child("car" + car.getId()).setValue(car);
    }

    public List<List<String>> parseMazeToStringList(char[][] inputList){
        List<List<String>> result = new ArrayList<List<String>>();
        for (int i = 0; i < dimensions; i++) {
            result.add(new ArrayList());
            for (int j = 0; j < dimensions; j++) {
                result.get(i).add(Character.toString(inputList[i][j]));
            }
        }
        return result;
    }

    public char[][] parseMazeToCharList(List<List<String>> inputList){
        char[][] result = new char[dimensions][dimensions];
        for (int i = 0; i < dimensions; i++)
            for (int j = 0; j < dimensions; j++)
                result[i][j] = inputList.get(i).get(j).charAt(0);
        return result;
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
        ivSpider.setBackground(getDrawable(car.getSprite()));

        imageView2 = findViewById(R.id.imageView2);

        ivHearts = findViewById(R.id.ivHearts);

        tvScoreNumber = findViewById(R.id.tvScoreNumber);
        tvScoreNumber2 = findViewById(R.id.tvScoreNumber2);

        game = findViewById(R.id.llGame);
        deathScreen = findViewById(R.id.ivDeathScreen);
        clDeathScreen = findViewById(R.id.clDeathScreen);
        animationView = findViewById(R.id.animationView);
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

        spiderStep = MediaPlayer.create(MainActivity.this, R.raw.step3);
        steveStep = MediaPlayer.create(MainActivity.this, R.raw.footstep);

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
                if(imageId != 4){
                    iv.setBackground(getDrawable(R.drawable.sand));
                }else{
                    iv.setBackground(getDrawable(R.drawable.magma));
                }
                break;
            case '4':
                iv.setBackground(getDrawable(R.drawable.goal));
        }
        if (database != null) {
            if (car2.getX() == i && car2.getY() == j){
                if(car2.getId() == 1){
                    iv.setBackground(getDrawable(R.drawable.spiderinsand));
                }else{
                    iv.setBackground(getDrawable(R.drawable.goal));
                }
                iv.setRotation(car2.getDirection().get(car2.getRotation()));
            }
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
                    checkTile(c, c.getX(),c.getY()+1-dimensions);
                }else{
                    checkTile(c, c.getX(),c.getY()+1);
                }
                System.out.println("position: " + c.getX() + " - " + c.getY());
                break;
            case 1:
                if(c.getX()+1 >= dimensions){
                    checkTile(c, c.getX()+1-dimensions,c.getY());
                }else{
                    checkTile(c, c.getX()+1,c.getY());
                }
                System.out.println("position: " + c.getX() + " - " + c.getY());
                break;
            case 2:
                if(c.getY()-1 < 0){
                    checkTile(c, c.getX(),c.getY()-1+dimensions);
                }else{
                    checkTile(c, c.getX(),c.getY()-1);
                }
                System.out.println("position: " + c.getX() + " - " + c.getY());
                break;
            case 3:
                if(c.getX()-1 < 0){
                    checkTile(c, c.getX()-1+dimensions,c.getY());
                }else{
                    checkTile(c, c.getX()-1,c.getY());
                }
                System.out.println("position: " + c.getX() + " - " + c.getY());
                break;
        }
    }
    //ROTATE CAR AND ITS VIEW TO THE LEFT
    public void carLeft(Car c){
        if(c.getRotation()-1 < 0) {c.setRotation(4);}
        c.setRotation(c.getRotation() - 1);
        ivSpider.setRotation(c.getDirection().get(c.getRotation()));
    }
    //ROTATE CAR AND ITS VIEW TO RIGHT
    public void carRight(Car c){
        if(c.getRotation()+1 > 3) {c.setRotation(-1);}
        c.setRotation(c.getRotation() + 1);
        ivSpider.setRotation(c.getDirection().get(c.getRotation()));
    }

    //CHECK THE BOX WHERE THE CAR IS GOING TO MOVE.
    // 0-NO PROBLEM
    // 1-CACTUS(LOSE A HEART AND CANNOT ADVANCE)
    // 2-LAVA(DEATH REGARDLESS OF HEARTS)
    // 3-MAGMA(CAN ADVANCE BUT LOSE A HEART)
    // 4-STEVE WIN
    public void checkTile (Car car, int x, int y) {
        switch (m.getGrid()[x][y]){
            case '0':
                if (car.getId() == 1){
                    spiderStep.start();
                }else{
                    steveStep.start();
                }
                car.setX(x);
                car.setY(y);
                car.setMovements(car.getMovements()+1);
                render();
                break;
            case '1':
                if(car.getId() == 1) {
                    MediaPlayer music = MediaPlayer.create(MainActivity.this, R.raw.say2);
                    music.start();
                }else{
                    MediaPlayer music = MediaPlayer.create(MainActivity.this, R.raw.hit2);
                    music.start();
                }
                car.loseHeart();
                ivHearts.setImageResource(heartsId[car.getLife()]);
                if (car.getLife() <= 0) {
                    setDeathScreenVisible();
                    setButtonsInvisible();
                    tvScoreNumber.setText(""+car.getMovements());
                    tvScoreNumber2.setText(""+car.getMovements());
                    animationView.setVisibility(View.INVISIBLE);
                }
                break;
            case '2':
                if (car.getId() == 1){
                    spiderStep.start();
                }else{
                    steveStep.start();
                }
                car.setX(x);
                car.setY(y);
                render();
                setDeathScreenVisible();
                setButtonsInvisible();
                animationView.setVisibility(View.INVISIBLE);
                tvScoreNumber.setText(""+car.getMovements());
                tvScoreNumber2.setText(""+car.getMovements());
                ivHearts.setImageResource(heartsId[0]);
                break;
            case '3':
                if(car.getId() == 1) {
                    MediaPlayer music = MediaPlayer.create(MainActivity.this, R.raw.say2);
                    music.start();
                }else{
                    MediaPlayer music = MediaPlayer.create(MainActivity.this, R.raw.hit2);
                    music.start();
                }
                car.setX(x);
                car.setY(y);
                car.setMovements(car.getMovements()+1);
                car.loseHeart();
                ivHearts.setImageResource(heartsId[car.getLife()]);
                render();
                if (car.getLife() <= 0) {
                    setDeathScreenVisible();
                    setButtonsInvisible();
                    animationView.setVisibility(View.INVISIBLE);
                    tvScoreNumber.setText(""+car.getMovements());
                    tvScoreNumber2.setText(""+car.getMovements());
                    ivHearts.setImageResource(heartsId[0]);
                }
                break;
            case '4':
                car.setX(x);
                car.setY(y);
                car.setMovements(car.getMovements()+1);
                render();
                setWinScreenVisible();
                setButtonsInvisible();
                imageView2.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.VISIBLE);
                break;
        }
        if (database != null){
            actualizeFirebase(car);
            if (car.getX() == car2.getX() && car.getY() == car2.getY()) {
                setButtonsInvisible();
                imageView2.setVisibility(View.VISIBLE);
                if(car.getId() == 1){
                    setWinScreenVisible();
                }else{
                    setDeathScreenVisible();
                    ivHearts.setImageResource(heartsId[0]);
                    animationView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void setWinScreenVisible() {
        ivHearts.setVisibility(View.INVISIBLE);
        deathScreen.setBackground(getDrawable(R.drawable.greenscreen));
        deathScreen.bringToFront();
        LottieAnimationView lav = findViewById(R.id.animationView);
        lav.playAnimation();
        clDeathScreen.setVisibility(View.VISIBLE);
        clDeathScreen.bringToFront();
        TextView tv = findViewById(R.id.textView);
        tv.setText("You won!");
        tv = findViewById(R.id.textView3);
        tv.setText("You won!");
        MediaPlayer music = MediaPlayer.create(MainActivity.this, R.raw.winsound);
        music.start();
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
        if (car.getId() == 1){
            MediaPlayer music = MediaPlayer.create(MainActivity.this, R.raw.death);
            music.start();
        }else{
            MediaPlayer music = MediaPlayer.create(MainActivity.this, R.raw.classic_hurt);
            music.start();
        }
    }


    public void menu(){
        Intent i = new Intent(this, MenuActivity.class);
        finish();
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