package com.example.battleship;

public class Point {
    private int coordinateX;//координата точки по вертикали
    private int coordinateY;// координата точки по горизонтали
    private char value;//хранит состояние ячейки

    //конструктор для точки
    Point(int coordinateX, int coordinateY, char value) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.value = value;
    }
    // геттры/сетеры
    int getCoordinateX() {
        return coordinateX;
    }

    int getCoordinateY() {
        return coordinateY;
    }

    char getValue() {
        return value;
    }

    void setValue(char value) {
        this.value = value;
    }
}
