package com.example.car;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Car car;
    Maze m;
    TableLayout tlBoard;
    ImageButton btUp, btLeft, btRight, btMenu, btRespawn, btTitleScreen;
    ConstraintLayout clDeathScreen;
    LinearLayout game;
    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, deathScreen;
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
                initializeGame();
                car = new Car(1);
                addCar(car, ivList);
                break;
            case 1:
                m = new Maze(dimensions);
                m.draw();
                //TODO UPLOAD MAZE TO FIREBASE
                initializeGame();
                car = new Car(1);
                addCar(car, ivList);
                break;
            case 2:
                //TODO RETRIEVE MAZE FROM FIREBASE
                initializeGame();

                car = new Car(1);
                addCar(car, ivList);
        }
    }

    //TODO GET A INSTANCE OF THE DATABASE ON FIREBASE
    public void connectToFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("games");

        database.getReference().child("games")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String game = snapshot.getKey();
                            System.out.println(game);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        myRef.setValue("Hello, World!");
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

        game = findViewById(R.id.llGame);
        deathScreen = findViewById(R.id.ivDeathScreen);
        clDeathScreen = findViewById(R.id.clDeathScreen);
        game.bringToFront();

        btUp = findViewById(R.id.ibMove);
        btUp.setOnClickListener(this);
        btLeft = findViewById(R.id.ibLeft);
        btLeft.setOnClickListener(this);
        btRight = findViewById(R.id.ibRight);
        btRight.setOnClickListener(this);
        btMenu = findViewById(R.id.ibMenu);
        btMenu.setOnClickListener(this);
        btRespawn = findViewById(R.id.ibRespawn);
        btRespawn.setOnClickListener(this);
        btTitleScreen = findViewById(R.id.ibTitleScreen);
        btTitleScreen.setOnClickListener(this);

    }

    //DRAW THE CAR AND ALL ELEMENTS NEAR TO IT
    public void addCar (Car car, ArrayList<ImageView> ivList) {
        int imageId = 0;
        for (int i = car.getX()-1; i < car.getX()-1 + VIEWRANGE; i++) {
            for (int j = car.getY()-1; j < car.getY()-1 + VIEWRANGE; j++){
                ImageView iv = ivList.get(imageId);
                m.draw();
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
                }
                imageId++;
            }
        }
        ivList.get(4).setBackground(getDrawable(R.drawable.spiderinsand));
    }

    //DRAW ELEMENTS NEAR TO CAR ACCORDING TO THE NUMBER IN EACH BOX OF THE MAP
    public void drawViews (int imageId, int i, int j) {
        if (imageId != 4) {
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
            }
        }
    }

    //CHECK IF THE CAR IS ON A BOUND AND IF IT IS, DRAW THE OPPOSITE SIDE OF THE BOARD (THE MAP IS LIKE A SPHERE)
    public void checkOutOfBounds () {
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
        ivList.get(4).setRotation(c.getDirection()[c.getRotation()]);
    }
    //ROTATE CAR AND ITS VIEW TO RIGHT
    public void carRight(Car c){
        if(c.getRotation()+1 > 3) {c.setRotation(-1);}
        c.setRotation(c.getRotation() + 1);
        ivList.get(4).setRotation(c.getDirection()[c.getRotation()]);
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
                checkOutOfBounds();
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
                checkOutOfBounds();
                setDeathScreenVisible();
                setButtonsInvisible();
                break;
            case '3':
                car.setX(x);
                car.setY(y);
                car.loseHeart();
                checkOutOfBounds();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibMove:
                carMove(car);
                break;
            case R.id.ibLeft:
                carLeft(car);
                break;
            case R.id.ibRight:
                carRight(car);
                break;
            case R.id.ibMenu:
                menu();
                break;
            case R.id.ibRespawn:
                //RESTART GAME
                //GENERATE A NEW MAIN ACTIVITY(GAME SCREEN), FINISH CURRENT AND START THE NEW ACTIVITY
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                break;
            case R.id.ibTitleScreen:
                menu();
                break;
        }
    }
}