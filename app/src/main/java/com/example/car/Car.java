package com.example.car;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private int id;
    private int life;
    private int movements;
    private int x;
    private int y;
    private int rotation;
    private List<Integer> direction;

    public Car(){}

    public Car(int id) {
        this.id = id;
        this.life = 10;
        this.movements = 0;
        this.x = 1;
        this.y = 1;
        this.rotation = 3;
        this.direction = new ArrayList<Integer>() {
            {
                add(90);
                add(180);
                add(-90);
                add(0);
            }
        };
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getMovements() {
        return movements;
    }

    public void setMovements(int movements) {
        this.movements = movements;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public List<Integer> getDirection() {
        return direction;
    }

    public void loseHeart () {
        this.life--;
    }

}
