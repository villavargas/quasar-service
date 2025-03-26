package com.example.quasar.model;

public class Position {
    private Float x;
    private Float y;

    public Position() {}

    public Position(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public Float getX() { return x; }
    public void setX(Float x) { this.x = x; }

    public Float getY() { return y; }
    public void setY(Float y) { this.y = y; }
}
