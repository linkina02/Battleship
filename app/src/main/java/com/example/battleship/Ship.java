package com.example.battleship;

public class Ship {
    private String nameOfShip;//название корабля
    private int sizeOfShip;//размер корабля
    private int livesOfShip;//количестово жизней корабля
    private Point[] coordinatesOfShip;// точки корабля на поле
    private boolean isVerticalTypeOfShip;// тип корабля вертикальный или горизонтальный
    private boolean isNotDamagedShip;// повреждён корабль или нет
    private boolean isAliveShip;// корабль жив или нет

    //конструктор для корабля
    Ship(String nameOfShip, boolean isVerticalTypeOfShip, int sizeOfShip, Point[] coordinatesOfShip) {
        this.nameOfShip = nameOfShip;
        this.isVerticalTypeOfShip = isVerticalTypeOfShip;
        this.sizeOfShip = sizeOfShip;
        this.coordinatesOfShip = coordinatesOfShip;
        livesOfShip = sizeOfShip;
        isNotDamagedShip = true;
        isAliveShip = true;
    }

    // геттры/сетеры
    public String getNameOfShip() {
        return nameOfShip;
    }

    public int getSizeOfShip() {
        return sizeOfShip;
    }

    public int getLivesOfShip() {
        return livesOfShip;
    }

    public Point[] getCoordinatesOfShip() {
        return coordinatesOfShip;
    }

    public boolean isVerticalTypeOfShip() {
        return isVerticalTypeOfShip;
    }

    boolean isNotDamagedShip() {
        return isNotDamagedShip;
    }

    boolean isAliveShip() {
        return isAliveShip;
    }

    public void setAliveShip(boolean aliveShip) {
        isAliveShip = aliveShip;
    }

    //проверка выстрела на попадание в данный корабль
     char checkIsDamage(Point point, Player targetPlayer) {
        int x = point.getCoordinateX();
        int y = point.getCoordinateY();
        // чтоб не ругался компилятор
        char valueOfShoot = '2';
        // есть ли смысл проверять?
        if(this.isAliveShip) {
            // проверяем по всем точкам корабля
            for (int i = 0; i < coordinatesOfShip.length; i++) {
                // проверка на попадание
                if ((x == coordinatesOfShip[i].getCoordinateX()) && (y == coordinatesOfShip[i].getCoordinateY())) {
                    this.livesOfShip--;
                    // проверка на уничтожение
                    if (this.livesOfShip > 0) {
                        coordinatesOfShip[i].setValue('X');// меняем символ в точке корабля
                        // меняем символ на поле игрока
                        targetPlayer.getFieldOfPlayer()
                                .getCellsOfPlayer()
                                [coordinatesOfShip[i].getCoordinateX()]
                                [coordinatesOfShip[i].getCoordinateY()]
                                .setValue('X');
                        valueOfShoot = 'X';// просто попал
                        // если корабль был неповрежден
                        if (this.livesOfShip < this.sizeOfShip) {
                            isNotDamagedShip = false;
                        }
                    } else {
                        // если попал и убил, ставим символы потопленного корабля у корабля
                        for (int j = 0; j < coordinatesOfShip.length; j++) {
                            coordinatesOfShip[j].setValue('S');
                            // ставим эти же символы на поле, а вокруг них обстреляные точки
                            for (int k = coordinatesOfShip[j].getCoordinateX() - 1;
                                 k <= coordinatesOfShip[j].getCoordinateX() + 1; k++) {
                                for (int l = coordinatesOfShip[j].getCoordinateY() - 1;
                                     l <=coordinatesOfShip[j].getCoordinateY() + 1; l++) {
                                    Point tempPoint = targetPlayer.getFieldOfPlayer()
                                            .getCellsOfPlayer()[k][l];
                                    if(tempPoint.getValue() == 'S' ||
                                            tempPoint.getValue() == 'X' ||
                                            tempPoint.getValue() == 'O'){
                                        tempPoint.setValue('S');
                                    } else {
                                        tempPoint.setValue('*');
                                    }
                                }
                            }
                        }
                        valueOfShoot = 'S';// попал и убил
                        isAliveShip = false;
                    }
                } else {
                    if (!(valueOfShoot == 'X' || valueOfShoot == 'S')){
                        valueOfShoot = '0';// не попал
                    }
                }
            }
        } else {
            if (!(valueOfShoot == 'X' || valueOfShoot == 'S')){
                valueOfShoot = '0';// уже убит
            }
        }
        return valueOfShoot;
    }

    //проверка корабля на уничтожение
    private boolean checkIsAlive() {
        if (this.livesOfShip > 0) {
            if (this.livesOfShip < this.sizeOfShip)
                isNotDamagedShip = false;
            return true;
        } else {
            isAliveShip = false;
            return false;
        }
    }
}