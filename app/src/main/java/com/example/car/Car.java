package com.example.car;

public class Car {
    private int id;
    private int life;
    private int x;
    private int y;
    private int rotation;
    private final int[] direction = {90, 180, -90, 0};

    public Car(int id) {
        this.id = id;
        this.life = 10;
        this.x = 1;
        this.y = 1;
        this.rotation = 3;
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

    public int[] getDirection() {
        return direction;
    }

    public void loseHeart () {
        this.life--;
    }

}
