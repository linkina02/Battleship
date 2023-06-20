package com.example.battleship;

import java.util.ArrayList;
import java.util.Random;

public class Field {
    static final int SIZE_OF_FIELD = 10;// размер поля
    static final int NUMBER_OF_SHIPS_5 = 0;// количество пятипалубников
    static final int NUMBER_OF_SHIPS_4 = 1;// количество четырёхпалубников
    static final int NUMBER_OF_SHIPS_3 = 2;// количество трёхпалубников
    static final int NUMBER_OF_SHIPS_2 = 3;// количество двухпалубников
    static final int NUMBER_OF_SHIPS_1 = 4;// количество однопалубников
    static final int TOTAL_NUMBER_OF_SHIPS = NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4 +
            NUMBER_OF_SHIPS_3 + NUMBER_OF_SHIPS_2 + NUMBER_OF_SHIPS_1; // общее количество кораблей

    Random random = new Random();// просто рандом
    private Point[][] cellsOfPlayer;// двумерный массив ячеек поля игрока
    private Ship[] shipsOfPlayer;// массив кораблей игрока

    //конструктор для поля игрока
    Field() {
        cellsOfPlayer = new Point[SIZE_OF_FIELD + 2][SIZE_OF_FIELD + 2];//создаём квадратный массив игрового поля с дополнительными полями по периметру
        initField();
        shipsOfPlayer = new Ship[TOTAL_NUMBER_OF_SHIPS]; // создаём массив кораблей игрока

    }

    // геттры/сетеры
    public Point[][] getCellsOfPlayer() {
        return cellsOfPlayer;
    }

    public Ship[] getShipsOfPlayer() {
        return shipsOfPlayer;
    }

    // инициализация поля, расставляем символы ~ по всем координатам квадратного массива
    private void initField() {
        for (int i = 0; i < this.cellsOfPlayer.length; i++) {
            for (int j = 0; j < this.cellsOfPlayer.length; j++) {
                this.cellsOfPlayer[i][j] = new Point(i, j, '~');
            }
        }
    }

    // расстановка всех кораблей на поле игрока в автоматическом режиме
    void initShips(boolean isAutomaticallyPutShipsOnField, Player player) {
        // расстановка пятипалубников
        int k = 0;
        for (int i = 0;
             i < NUMBER_OF_SHIPS_5;
             i++) {
            k++;
            String name = "пятипалубник-" + k;
            int size = 5;
            setShipAutomatically(player, i, name, size);
        }

        // расстановка четырёхпалубников
        k = 0;
        for (int i = NUMBER_OF_SHIPS_5;
             i < (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4);
             i++) {
            k++;
            String name = "Четырёхпалубник-" + k;
            int size = 4;
            setShipAutomatically(player, i, name, size);
        }
        // расстановка трёхпалубников
        k = 0;
        for (int i = (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4);
             i < (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4 + NUMBER_OF_SHIPS_3);
             i++) {
            k++;
            String name = "Трёхпалубник-" + k;
            int size = 3;
            setShipAutomatically(player, i, name, size);
        }

        // расстановка двухпалубников
        k = 0;
        for (int i = (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4 + NUMBER_OF_SHIPS_3);
             i < (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4 + NUMBER_OF_SHIPS_3 + NUMBER_OF_SHIPS_2);
             i++) {
            k++;
            String name = "Двухпалубник-" + k;
            int size = 2;
            setShipAutomatically(player, i, name, size);
        }

        // расстановка однопалубников
        k = 0;
        for (int i = (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4 + NUMBER_OF_SHIPS_3 + NUMBER_OF_SHIPS_2);
             i < (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4 + NUMBER_OF_SHIPS_3 + NUMBER_OF_SHIPS_2 + NUMBER_OF_SHIPS_1);
             i++) {
            k++;
            String name = "Однопалубник-" + k;
            int size = 1;
            setShipAutomatically(player, i, name, size);
        }
    }

    // установка корабля с заданными параметрами на поле автоматически
    private void setShipAutomatically(Player player, int i, String name, int size) {
        Point pointToPutShip;
        Point[] coordinatesOfShip;
        boolean isVertical;
        // рандомно получаем координаты точки корабля, пока она не подойдёт
        do {
            pointToPutShip = new Point((random.nextInt(SIZE_OF_FIELD)), (random.nextInt(SIZE_OF_FIELD)), '~');
            isVertical = random.nextBoolean();
        } while(!isCorrectCoordinateForPutShip(pointToPutShip, isVertical, size));
        coordinatesOfShip = putShipOnField(pointToPutShip, isVertical, size);// ставим корабль на поле
        shipsOfPlayer[i] = new Ship(name, isVertical, size, coordinatesOfShip); // создаём корабль и помещаем в массив кораблей игрока
    }

    //проверяем, сможем ли мы разместить такой корабль на поле
    boolean isCorrectCoordinateForPutShip(Point point, boolean isVertical, int size) {
        int x = point.getCoordinateX() + 1;// +1 из-за технических нюансов. у нас хранится массив 12*12, а поле 10*10
        int y = point.getCoordinateY() + 1;// 12*12 нужно для проверок вокруг точек
        if (isVertical){
            if(x >= 1 && (x + size - 1) <= SIZE_OF_FIELD) { // проверка на попадание в поле
                for (int i = (x - 1); i < (x + size + 1); i++) { // проверка на пересечение с другими кораблями по вертикали
                    for (int j = y - 1; j <= y + 1; j++) { // проверка на пересечение с другими кораблями по горизонтали
                        if (cellsOfPlayer[i][j].getValue() == 'O') { // символ корабля
                            return false;
                        }
                    }
                }
            } else
                return false;
        } else {
            if(y >= 1 && (y + size - 1) <= SIZE_OF_FIELD) { // проверка на попадание в поле
                for (int j = (y - 1); j < (y + size + 1); j++) {// проверка на пересечение с другими кораблями по горизонтали
                    for (int i = x - 1; i <= x + 1; i++) {// проверка на пересечение с другими кораблями по вертикали
                        if (cellsOfPlayer[i][j].getValue() == 'O') { // символ корабля
                            return false;
                        }
                    }
                }
            } else
                return false;
        }
        return true;
    }

    // установка координат корабля на поле
    Point[] putShipOnField(Point point, boolean isVertical, int size) {
        int x = point.getCoordinateX() + 1;
        int y = point.getCoordinateY() + 1;
        Point[] coordinatesOfShip = new Point[size];
        if (isVertical){
            // создаём точки корабля
            for (int i = 0; i < coordinatesOfShip.length; i++) {
                coordinatesOfShip[i] = new Point(x + i, y, 'O');
            }
        } else {
            // создаём точки корабля
            for (int j = 0; j < coordinatesOfShip.length; j++) {
                coordinatesOfShip[j] = new Point(x, y + j, 'O');
            }
        }
        // ставим точки корабля на поле
        for (Point aCoordinatesOfShip : coordinatesOfShip) {
            cellsOfPlayer[aCoordinatesOfShip.getCoordinateX()][aCoordinatesOfShip.getCoordinateY()] = aCoordinatesOfShip;
        }
        return coordinatesOfShip;
    }

    // метод проверки и получения координат подбитого корабля
    public ArrayList<Point> getDamagedPointsOfAliveShip() {
        ArrayList<Point> damagedPoints = new ArrayList<>();
        for (int i = 0; i < getShipsOfPlayer().length; i++) {
            if (getShipsOfPlayer()[i].isAliveShip())
                if (!getShipsOfPlayer()[i].isNotDamagedShip())
                    for (int j = 0; j < getShipsOfPlayer()[i].getSizeOfShip(); j++) {
                        if(getShipsOfPlayer()[i].getCoordinatesOfShip()[j].getValue() == 'X')
                            damagedPoints.add(getShipsOfPlayer()[i].getCoordinatesOfShip()[j]);
                    }
        }
        return damagedPoints;
    }
}
