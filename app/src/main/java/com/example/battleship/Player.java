package com.example.battleship;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private boolean isItHuman;// Играет человек или компьютер
    private String nameOfPlayer; // Имя игрока
    private int numberOfShipsAlive; // Количество живых кораблей игрока
    private Field fieldOfPlayer; // Поле игрока
    static int numberOfPlayers; // Число игроков
    private int idOfPlayer; // идентификатор игрока для жребия для первого хода
    private int numberOfShots; // количество выстрелов, сделанных игроком
    private int numberOfHits; // количество попаданий по кораблям соперника
    private boolean isShipsSet; // Проверка, расставлены ли корабли на поле

    // все данные хранятся в классе в виде статических переменных
    public static String names[] = new String[2];
    public static boolean isHumans[] = new boolean[2];
    public static boolean isAutomaticals[] = new boolean[2];
    public static Player[] players = new Player[2];

    // конструктор
    public Player(boolean isItHuman, String name){
        numberOfPlayers++;
        this.isItHuman = isItHuman;
        this.nameOfPlayer = name;
        fieldOfPlayer = new Field();
        idOfPlayer += numberOfPlayers;
        numberOfShots = 0;
        numberOfHits = 0;
        numberOfShipsAlive = Field.TOTAL_NUMBER_OF_SHIPS;
        isShipsSet = false;
    }

    // геттры/сетеры
    public boolean isItHuman() {
        return isItHuman;
    }

    public String getNameOfPlayer() {
        return nameOfPlayer;
    }

    public int getNumberOfShipsAlive() {
        return numberOfShipsAlive;
    }

    public Field getFieldOfPlayer() {
        return fieldOfPlayer;
    }

    public int getIdOfPlayer() {
        return idOfPlayer;
    }

    public int getNumberOfShots() {
        return numberOfShots;
    }

    public int getNumberOfHits() {
        return numberOfHits;
    }

    public boolean isShipsSet() {
        return isShipsSet;
    }

    public void setNumberOfHits(int numberOfHits) {
        this.numberOfHits = numberOfHits;
    }

    public void setNumberOfShots(int numberOfShots) {
        this.numberOfShots = numberOfShots;
    }

    public void setShipsSet(boolean shipsSet) {
        isShipsSet = shipsSet;
    }

    // автоматически выдаёт точку для выстрела компьютера,
    // здесь заложеня вся логика для выстрелов компа
    Point getCoordinateOfPointFromComputer(Player targetPlayer) {
        int coordinateX; // временные координаты точки для проверки
        int coordinateY;
        boolean isRepeat;// повторять ли итерацию
        int k = 0;// счётчик
        Random random = new Random();
        System.out.println(" ... ");
//        try {
//            Thread.sleep(200);// делаем задержку, чтобы немного замедлить комп
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // получаем координаты попаданий в повреждённый корабль соперника, потопленные исключаем
        ArrayList<Point> damagedPoints = targetPlayer.fieldOfPlayer.getDamagedPointsOfAliveShip();
//        boolean haveTargetPlayerDamagedShips = damagedPoints.size() == 0 ? false : true;
        switch (damagedPoints.size()) {
            case 0:
                // если таких точек нет, стреляем по полю рандомно,
                // но игнорируем уже подбитые корабли и места вокруг них,
                //  а также места, куда уже стрелял
                do {
                    isRepeat = false;
                    coordinateX = random.nextInt(Field.SIZE_OF_FIELD) + 1;
                    coordinateY = random.nextInt(Field.SIZE_OF_FIELD) + 1;
                    // исключвем области вокруг потопленных кораблей,
                    // так как подбитых не обнаружено, так как корбали рядом ставить нельзя
                    for (int i = (coordinateX - 1); i <= (coordinateX + 1); i++) {
                        for (int j = (coordinateY - 1); j <= (coordinateY + 1); j++) {
                            if (targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'X' ||
                                    targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'S') {
                                isRepeat = true;
                            }
                        }
                    }
                } while (isRepeat
                        // исключаем также точки, чьё значение не соответствует символам ~ и O
                        || !(targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == '~'
                        || targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == 'O'));
                break;

            // если одна подбитая точка, а корабль не потоплен, стреляем вокруг этой точки
            case 1:
                do {
                    isRepeat = false;
                    do {
                        // генерируем точку вокруг подбитой точки рандомно
                        coordinateX = damagedPoints.get(0).getCoordinateX() + random.nextInt(3) - 1;
                        coordinateY = damagedPoints.get(0).getCoordinateY() + random.nextInt(3) - 1;
                        // проверяем, чтобы точка не выходила за пределы игрового поля
                    } while (coordinateX < 1 || coordinateX > Field.SIZE_OF_FIELD ||
                            coordinateY < 1 || coordinateY > Field.SIZE_OF_FIELD);
                    // проверяем точки в окрестностях на наличие убитых кораблей, так как их обстреливать нет смысла
                    for (int i = (coordinateX - 1); i <= (coordinateX + 1); i++) {
                        for (int j = (coordinateY - 1); j <= (coordinateY + 1); j++) {
                            if (targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'X' ||
                                    targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'S') {
                                k++;// счётчик убитых клеток, если их больше одной, туда стрелять нет смысла
                            }
                        }
                    }
                    // если кроме нашей подбитой точки окрестностях имеются ещё такие, то нет смысла туда стрелять
                    if (k > 1) {
                        isRepeat = true;
                        k = 0;
                    }
                } while (isRepeat
                        // также нам нужны только символы ~ и O
                        || !((targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == '~'
                        || targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == 'O')
                        // а также нам нужно стрелять по кресту, по диагонали от исходной точки выстрелы не нужны
                        && (coordinateX == damagedPoints.get(0).getCoordinateX()
                        || coordinateY == damagedPoints.get(0).getCoordinateY())));
                break;

            // в случае, если попаданий больше одного, нужно стрелять по линии, то есть вариантов только 2
            default:
                // числа для 2 вариантов координат выстрела
                int r1;
                int r2;
                boolean b = random.nextBoolean();
                if (b) {
                    r1 = 0;
                    r2 = 1;
                } else {
                    r2 = 0;
                    r1 = 1;
                }
                // если линия попаданий по вертикали
                if (damagedPoints.get(0).getCoordinateX() == damagedPoints.get(1).getCoordinateX()) {
                    // массив коориднат по горизонтали
                    int[] coordinatesY = {damagedPoints.get(0).getCoordinateY() - 1,
                            damagedPoints.get(damagedPoints.size() - 1).getCoordinateY() + 1};
                    // по вертикали одна координата
                    coordinateX = damagedPoints.get(0).getCoordinateX();
                    // выбираем одну из точек
                    coordinateY = coordinatesY[r1];
                    // проверяем на попадание в игроваое поле, если нет, то берём другую точку
                    if (coordinateX < 1 || coordinateX > Field.SIZE_OF_FIELD ||
                            coordinateY < 1 || coordinateY > Field.SIZE_OF_FIELD) {
                        coordinateY = coordinatesY[r2];
                    }
                    // проверяем на наличие точек убитых кораблей в окрестностьях выбранной точки
                    for (int i = (coordinateX - 1); i <= (coordinateX + 1); i++) {
                        for (int j = (coordinateY - 1); j <= (coordinateY + 1); j++) {
                            if (targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'X' ||
                                    targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'S') {
                                k++;
                            }
                        }
                    }
                    // если есть совпадения, или точка не соответствует символам ~ и O, то берём другую точку
                    // по сути это срабатывает, когда r1 не вышла за пределы поля, но всё равно не проходит по прошлым проверкам
                    // если r1  отсеялось, то в этой замене уже нет смысла, до неё просто не дойдёт дело
                    if (k > 1
                            || !(targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == '~'
                            || targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == 'O')) {
                        coordinateY = coordinatesY[r2];
                    }
                    // если линия попаданий по горизонтали, делаем все те же манипуляции, только по вертикали
                } else if (damagedPoints.get(0).getCoordinateY() == damagedPoints.get(1).getCoordinateY()) {
                    // массив коориднат по вертикали
                    int[] coordinatesX = {damagedPoints.get(0).getCoordinateX() - 1,
                            damagedPoints.get(damagedPoints.size() - 1).getCoordinateX() + 1};
                    // по горизонтали одна координата
                    coordinateY = damagedPoints.get(0).getCoordinateY();
                    // выбираем одну из точек
                    coordinateX = coordinatesX[r1];
                    // проверяем на попадание в игроваое поле, если нет, то берём другую точку
                    if (coordinateX < 1 || coordinateX > Field.SIZE_OF_FIELD ||
                            coordinateY < 1 || coordinateY > Field.SIZE_OF_FIELD) {
                        coordinateX = coordinatesX[r2];
                    }
                    // проверяем на наличие точек убитых кораблей в окрестностьях выбранной точки
                    for (int i = (coordinateX - 1); i <= (coordinateX + 1); i++) {
                        for (int j = (coordinateY - 1); j <= (coordinateY + 1); j++) {
                            if (targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'X' ||
                                    targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'S') {
                                k++;
                            }
                        }
                    }
                    // если есть совпадения, или точка не соответствует символам ~ и O, то берём другую точку
                    // по сути это срабатывает, когда r1 не вышла за пределы поля, но всё равно не проходит по прошлым проверкам
                    // если r1  отсеялось, то в этой замене уже нет смысла, до неё просто не дойдёт дело
                    if (k > 1
                            || !(targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == '~'
                            || targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == 'O')) {
                        coordinateX = coordinatesX[r2];
                    }
                    // это пришлось написать, чтобы не ругался компилятор, на самом деле это не выполняется никогда
                    // Если корабль не вертикальный и не горизонтальный, чего не может быть, если он ещё жив,
                    // то делаем просто рандомный выстрел
                } else {
                    do {
                        isRepeat = false;
                        // рандомная координата
                        coordinateX = random.nextInt(Field.SIZE_OF_FIELD) + 1;
                        coordinateY = random.nextInt(Field.SIZE_OF_FIELD) + 1;
                        // проверка на попадание в поле
                        for (int i = (coordinateX - 1); i <= (coordinateX + 1); i++) {
                            for (int j = (coordinateY - 1); j <= (coordinateY + 1); j++) {
                                if (targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'X' ||
                                        targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'S') {
                                    isRepeat = true;
                                }
                            }
                        }
                        // проверка на выполнение предыдущих условий, а также, что символы ~ и O
                    } while (isRepeat
                            || !(targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == '~'
                            || targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == 'O'));
                }
        }
//        try {
//            Thread.sleep(200); // пауза после выбора точки
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // наконец, возвращаем точку для выстрела компьютера
        return new Point(coordinateX, coordinateY, '0');
    }

    // проверка на количество живых кораблей у игрока
    int checkNumberOfShipsAlive() {
        int k = 0;// счётчик живых кораблей
        // получаем у поля игрока переменную с количеством кораблей пробегаем по нему,
        // чтобы посчитать количество живых кораблей у игрока после выстрела в него,
        // чтобы проверить окончание игры
        for (int i = 0; i < fieldOfPlayer.getShipsOfPlayer().length; i++) {
            if (fieldOfPlayer.getShipsOfPlayer()[i].isAliveShip()) {
                k++;
            }
        }
        numberOfShipsAlive = k;
        return k;
    }


}
