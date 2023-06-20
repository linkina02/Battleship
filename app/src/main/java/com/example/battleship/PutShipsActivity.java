package com.example.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class PutShipsActivity extends AppCompatActivity {

    // поля для вьюшек
    private TextView tvInformationText;
    private GridLayout glFieldOfPlayer;
    private ImageButton imageButtonOfField;
    private EditText etNameOfPlayer;
    private RadioGroup rgIsPlayer;
    private Button btnSetShips1;
    private Button btnSetShips2;
    private CheckBox cbIsAutomaticallySetShips;
    private Button btnStartPutShips;
    private RadioGroup rgIsVerticalShip;
    private Button btnSetShipOfPlayer;
    private Button btnSetNextShip;
    private Button btnFinishPutShips;
    private Button btnStartGame;

    // временные поля для создания объектов игроков, их полей и кораблей
    private Player tempPlayer; // временно хранит игрока пока заполняются данные, затем сохраняется в классе Player
    private String tempNameOfPlayer; // имя игрока
    private boolean tempIsPlayer; // кто будет играть? игрок : андроид
    private int tempIdOfPlayer; // идентификатор игрока, чтобы отличать их
    private boolean tempIsAutomaticallySetShips; // как расставлять корабли? автоматически : вручную
    private boolean tempIsVerticalShip; // направление корабля? вертикальное : горизонтальное
    private String tempNameOfShip; // имя корабля, на всякий случай
    private int tempNumberOfShip; // порядковый номер корабля
    private int tempSizeOfShip; // размер корабля
    private int tempCoordinateX; // временная переменная координаты по вертикали для хранения точки на поле
    private int tempCoordinateY; // временная переменная координаты по горизонтали для хранения точки на поле

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_ships);
        // получаем вьюшки по ID
        tvInformationText = findViewById(R.id.tvInformationText);
        glFieldOfPlayer = findViewById(R.id.glFieldOfPlayer);
        etNameOfPlayer = findViewById(R.id.etNameOfPlayer);
        rgIsPlayer = findViewById(R.id.rgIsPlayer);
        btnSetShips1 = findViewById(R.id.btnSetShips1);
        btnSetShips2 = findViewById(R.id.btnSetShips2);
        cbIsAutomaticallySetShips = findViewById(R.id.cbIsAutomaticallySetShips);
        btnStartPutShips = findViewById(R.id.btnStartPutShips);
        rgIsVerticalShip = findViewById(R.id.rgIsVerticalShip);
        btnSetShipOfPlayer = findViewById(R.id.btnSetShipOfPlayer);
        btnSetNextShip = findViewById(R.id.btnSetNextShip);
        btnFinishPutShips = findViewById(R.id.btnFinishPutShips);
        btnStartGame = findViewById(R.id.btnStartGame);

        // макет, если игроки не созданы
        if(Player.players[0] == null) {
            setTitle("Создание игроков");
            tvInformationText.setText("\nНужно создать игроков\n");
            glFieldOfPlayer.setVisibility(View.GONE);
            etNameOfPlayer.setVisibility(View.GONE);
            rgIsPlayer.setVisibility(View.GONE);
            rgIsPlayer.check(R.id.rbPlayer);
            btnSetShips1.setVisibility(View.VISIBLE);
            btnSetShips2.setVisibility(View.GONE);
            cbIsAutomaticallySetShips.setVisibility(View.GONE);
            btnStartPutShips.setVisibility(View.GONE);
            rgIsVerticalShip.setVisibility(View.GONE);
            btnSetShipOfPlayer.setVisibility(View.GONE);
            btnSetNextShip.setVisibility(View.GONE);
            btnFinishPutShips.setVisibility(View.GONE);
            btnStartGame.setVisibility(View.GONE);
            // макет, если первый игрок уже создан
        } else if (Player.players[1] == null) {
            setTitle("Создание игроков");
            tvInformationText.setText("\nНужно создать второго игрока\n");
            glFieldOfPlayer.setVisibility(View.GONE);
            etNameOfPlayer.setVisibility(View.GONE);
            rgIsPlayer.setVisibility(View.GONE);
            rgIsPlayer.check(R.id.rbАndroid);
            btnSetShips1.setVisibility(View.VISIBLE);
            btnSetShips1.setText("Создать заново первого игрока");
            btnSetShips2.setVisibility(View.VISIBLE);
            cbIsAutomaticallySetShips.setVisibility(View.GONE);
            btnStartPutShips.setVisibility(View.GONE);
            rgIsVerticalShip.setVisibility(View.GONE);
            btnSetShipOfPlayer.setVisibility(View.GONE);
            btnSetNextShip.setVisibility(View.GONE);
            btnFinishPutShips.setVisibility(View.GONE);
            btnStartGame.setVisibility(View.GONE);
            // макет, если оба игрока созданы
        } else {
            setTitle("Всё готово");
            tvInformationText.setText("Игроки созданы, \nкорабли расставлены, \nможно начинать!");
            glFieldOfPlayer.setVisibility(View.GONE);
            etNameOfPlayer.setVisibility(View.GONE);
            rgIsPlayer.setVisibility(View.GONE);
            btnSetShips1.setVisibility(View.VISIBLE);
            btnSetShips1.setText("Создать заново первого игрока");
            btnSetShips2.setVisibility(View.VISIBLE);
            btnSetShips2.setText("Создать заново второго игрока");
            cbIsAutomaticallySetShips.setVisibility(View.GONE);
            btnStartPutShips.setVisibility(View.GONE);
            rgIsVerticalShip.setVisibility(View.GONE);
            btnSetShipOfPlayer.setVisibility(View.GONE);
            btnSetNextShip.setVisibility(View.GONE);
            btnFinishPutShips.setVisibility(View.GONE);
            btnStartGame.setVisibility(View.VISIBLE);
        }
    }

    // создания первого игрока, запрос данных о нём
    public void clickSetShips1(View view) {
        tempIdOfPlayer = 1;
        setTitle("Создание первого игрока");
        tvInformationText.setText("Введите имя первого игрока. \nБудете играть сами? \nРасставить вам корабли?");
        glFieldOfPlayer.setVisibility(View.GONE);
        etNameOfPlayer.setVisibility(View.VISIBLE);
        rgIsPlayer.setVisibility(View.VISIBLE);
        rgIsPlayer.check(R.id.rbPlayer);
        btnSetShips1.setVisibility(View.GONE);
        btnSetShips2.setVisibility(View.GONE);
        cbIsAutomaticallySetShips.setVisibility(View.VISIBLE);
        cbIsAutomaticallySetShips.setChecked(false);
        btnStartPutShips.setVisibility(View.VISIBLE);
        btnStartPutShips.setText("Начать расстановку кораблей");
        rgIsVerticalShip.setVisibility(View.GONE);
        btnSetShipOfPlayer.setVisibility(View.GONE);
        btnSetNextShip.setVisibility(View.GONE);
        btnFinishPutShips.setVisibility(View.GONE);
        btnStartGame.setVisibility(View.GONE);
    }
    // создание второго игрока, запрос данных о нём
    public void clickSetShips2(View view) {
        tempIdOfPlayer = 2;
        setTitle("Создание второго игрока");
        tvInformationText.setText("Введите имя второго игрока. \nБудете играть сами? \nРасставить вам корабли?");
        glFieldOfPlayer.setVisibility(View.GONE);
        etNameOfPlayer.setVisibility(View.VISIBLE);
        rgIsPlayer.setVisibility(View.VISIBLE);
        rgIsPlayer.check(R.id.rbАndroid);
        btnSetShips1.setVisibility(View.GONE);
        btnSetShips2.setVisibility(View.GONE);
        cbIsAutomaticallySetShips.setVisibility(View.VISIBLE);
        cbIsAutomaticallySetShips.setChecked(true);
        btnStartPutShips.setVisibility(View.VISIBLE);
        btnStartPutShips.setText("Начать расстановку кораблей");
        rgIsVerticalShip.setVisibility(View.GONE);
        btnSetShipOfPlayer.setVisibility(View.GONE);
        btnSetNextShip.setVisibility(View.GONE);
        btnFinishPutShips.setVisibility(View.GONE);
        btnStartGame.setVisibility(View.GONE);
    }

    // реакция на выбор играть игроком или андроидом
    public void clickIsPlayer(View view) {
        int id = rgIsPlayer.getCheckedRadioButtonId();
        switch(id) {
            case R.id.rbАndroid:
                // если андроид, расставляем только автоматически
                cbIsAutomaticallySetShips.setVisibility(View.GONE);
                cbIsAutomaticallySetShips.setChecked(true);
                break;
                // если игрок, предлакаем выбрать ручную или автоустановку
            case R.id.rbPlayer:
                cbIsAutomaticallySetShips.setVisibility(View.VISIBLE);
                cbIsAutomaticallySetShips.setChecked(false);
                break;
        }
    }

    // реакция на выбор автоматической установки
    public void clickIsAutomaticallySetShips(View view) {
        // просто сохраняем в переменную
        tempIsAutomaticallySetShips = ((CheckBox)view).isChecked();
    }

    // начало установки кораблей в автоматическом или ручном режиме
    public void clickStartPutShips(View view) {
        // собираем данные об игроке с предыдущей формы
        tempNameOfPlayer = etNameOfPlayer.getText().toString();
        // даём имена игрокам по умолчанию
        if(tempNameOfPlayer.equals("")) {
            if(tempIdOfPlayer == 1) {
                tempNameOfPlayer = "Боцман";
            } else if (tempIdOfPlayer == 2) {
                tempNameOfPlayer = "Шкипер";
            }
        }
        int id = rgIsPlayer.getCheckedRadioButtonId();
        switch(id) {
            case R.id.rbАndroid:
                tempIsPlayer = false;
                break;
            case R.id.rbPlayer:
                tempIsPlayer = true;
                break;
            default:
                tempIsPlayer = false;
                break;
        }
        // создаём игрока
        tempPlayer = new Player(tempIsPlayer, tempNameOfPlayer);
        // узнаём как расставлять корабли
        tempIsAutomaticallySetShips = cbIsAutomaticallySetShips.isChecked();
        // если автоматически
        if (tempIsAutomaticallySetShips) {
            // запустить автоматическую расстановку кораблей
            setShipsOnFieldAutomatically(tempPlayer);
            setTitle("Корабли расставлены");
            tvInformationText.setText("Расстановка кораблей \nигрока " + tempPlayer.getNameOfPlayer() + "\nпроведена успешно");
            // нарисовать некликабельное поле со всеми расставлеными кораблями
            repaintFieldOfPlayer(tempPlayer, false);
            etNameOfPlayer.setVisibility(View.GONE);
            rgIsPlayer.setVisibility(View.GONE);
            btnSetShips1.setVisibility(View.GONE);
            btnSetShips2.setVisibility(View.GONE);
            cbIsAutomaticallySetShips.setVisibility(View.GONE);
            btnStartPutShips.setVisibility(View.VISIBLE);
            btnStartPutShips.setText("Переустановить корабли");
            rgIsVerticalShip.setVisibility(View.GONE);
            btnSetShipOfPlayer.setVisibility(View.GONE);
            btnSetNextShip.setVisibility(View.GONE);
            btnFinishPutShips.setVisibility(View.VISIBLE);
            btnStartGame.setVisibility(View.GONE);
            // если ручная установка
        } else {
            tempNumberOfShip = 0;
            //запустить последовательность действий для ручной устрановки
            setShipsOnFieldManually();
            setTitle("Ставим корабль: " + tempNameOfShip);
            tvInformationText.setText(tempPlayer.getNameOfPlayer() + ", \nвыберите направление корабля \nи точку для его установки на поле");
            //перерисовать поле кликабельное
            repaintFieldOfPlayer(tempPlayer, true);
            etNameOfPlayer.setVisibility(View.GONE);
            rgIsPlayer.setVisibility(View.GONE);
            btnSetShips1.setVisibility(View.GONE);
            btnSetShips2.setVisibility(View.GONE);
            cbIsAutomaticallySetShips.setVisibility(View.GONE);
            btnStartPutShips.setVisibility(View.GONE);
            rgIsVerticalShip.setVisibility(View.VISIBLE);
            rgIsVerticalShip.check(R.id.rbVertical);
            btnSetShipOfPlayer.setVisibility(View.GONE);
            btnSetNextShip.setVisibility(View.GONE);
            btnFinishPutShips.setVisibility(View.GONE);
            btnStartGame.setVisibility(View.GONE);
        }
        // если игрок, то поле делаем видимым, чтобы игрок видел свои корабли.
        if(tempPlayer.isItHuman() == true) {
            glFieldOfPlayer.setVisibility(View.VISIBLE);
            // если андроид, то корабли не видно
        } else {
            glFieldOfPlayer.setVisibility(View.GONE);
        }
    }

    // вызов метода автоматической установки кораблей
    private void setShipsOnFieldAutomatically(Player player) {
        player.getFieldOfPlayer().initShips(true, player);
    }

    // реакция на выбор точки на поле для установки корабля в ручном режиме
    public void clickButtonOnField(View view) {
        // получаем координаты по вертикали и горизонтали нажатой кнопки по её тегу
        imageButtonOfField = findViewById(view.getId());
        String tag = imageButtonOfField.getTag().toString();
        int b = Integer.parseInt(tag);
        tempCoordinateX = b / 10;
        tempCoordinateY = b % 10;
        String coordinates = "Выбрана точка с координатами x = " + tempCoordinateX + ", y = " + tempCoordinateY;
        // определяем направление корабля
        String orientation;
        if (rgIsVerticalShip.getCheckedRadioButtonId() == R.id.rbVertical){
            tempIsVerticalShip = true;
            orientation = "вертикальное";
        } else {
            tempIsVerticalShip = false;
            orientation = "горизонтальное";
        }
        repaintFieldOfPlayer(tempPlayer, true);// перерисоовываем макет
        // проверяем, можно ли поставить корабль с заданными параметрами
        boolean isShipCanSetToField =
                isItCanToSetShipOnField(
                        tempCoordinateX,
                        tempCoordinateY,
                        tempSizeOfShip,
                        tempIsVerticalShip,
                        tempPlayer.getFieldOfPlayer().getCellsOfPlayer());
        String isCanSetShip;
        if (isShipCanSetToField) { // если точка с учетом направления подходит
            isCanSetShip = "\nЗдесь можно разместить корабль";
            btnSetShipOfPlayer.setVisibility(View.VISIBLE); } else {
            isCanSetShip = "\nЗдесь кораль разместить нелзя!";
            btnSetShipOfPlayer.setVisibility(View.GONE);
        }
        // выводим информацию
        tvInformationText.setText(coordinates + "\nНаправление корабля: " + orientation + isCanSetShip);
    }

    // реакция на выбор ориентации корабля в ручном режиме
    public void clickIsVerticalShip(View view) {
        // выясняем направление кораля
        tempIsVerticalShip = rgIsVerticalShip.getCheckedRadioButtonId() == R.id.rbVertical ? true : false;
        String orientation = rgIsVerticalShip.getCheckedRadioButtonId() == R.id.rbVertical ? "вертикальное" : "горизонтальное";
        String coordinates;
        String isCanSetShip = "\n";// компилятор ругался
        boolean isPointSelected;
        // получаем точку для установки корабля
        if (tempCoordinateX == -1 || tempCoordinateY == -1){
            coordinates = "Координаты точки не выбраны";
            isPointSelected = false;
        } else {
            coordinates = "Выбрана точка с координатами [" +  tempCoordinateX + ", " + tempCoordinateY + "].";
            isPointSelected = true;
        }
        // когда точка на поле выбрана
        if (isPointSelected) {
            repaintFieldOfPlayer(tempPlayer, true);
            // проверяем на возможность установки корабля на поле по заданным параметрам
            boolean isShipCanSetToField =
                    isItCanToSetShipOnField(
                            tempCoordinateX,
                            tempCoordinateY,
                            tempSizeOfShip,
                            tempIsVerticalShip,
                            tempPlayer.getFieldOfPlayer().getCellsOfPlayer());
            if (isShipCanSetToField) { // если точка с учетом направления подходит
                isCanSetShip = "\nЗдесь можно разместить корабль";
                btnSetShipOfPlayer.setVisibility(View.VISIBLE); // можно установить корабль
            } else {
                isCanSetShip = "\nЗдесь кораль разместить нельзя!";
                btnSetShipOfPlayer.setVisibility(View.GONE); // нельзя установить корабль
            }
        }
        // инормация о корабле
        tvInformationText.setText(coordinates + "\nНаправление корабля: " + orientation + isCanSetShip);
    }

    // установка корабля на поле в ручном режиме
    public void clickSetShipOfPlayer(View view) {
        Point pointToPutShip = new Point(tempCoordinateX, tempCoordinateY, '0');

        // ставим корабль на поле
        Point[] coordinatesOfShip =
                tempPlayer.getFieldOfPlayer().putShipOnField(
                        pointToPutShip,
                        tempIsVerticalShip,
                        tempSizeOfShip);
        // создаём корабль и помещаем в массив кораблей игрока
        tempPlayer.getFieldOfPlayer().getShipsOfPlayer()[tempNumberOfShip] =
                new Ship(tempNameOfShip, tempIsVerticalShip, tempSizeOfShip, coordinatesOfShip);
        tvInformationText.setText("Корабль: \n" + tempNameOfShip + "\nустановлен на поле");
        // перерисовать поле с учётом поставленного корабля и сделать некликабельным
        repaintFieldOfPlayer(tempPlayer, false);
        glFieldOfPlayer.setVisibility(View.VISIBLE);
        etNameOfPlayer.setVisibility(View.GONE);
        rgIsPlayer.setVisibility(View.GONE);
        btnSetShips1.setVisibility(View.GONE);
        btnSetShips2.setVisibility(View.GONE);
        cbIsAutomaticallySetShips.setVisibility(View.GONE);
        btnStartPutShips.setVisibility(View.GONE);
        btnSetNextShip.setVisibility(View.GONE);
        btnFinishPutShips.setVisibility(View.GONE);
        btnStartGame.setVisibility(View.GONE);
        btnSetShipOfPlayer.setVisibility(View.GONE);
        rgIsVerticalShip.setVisibility(View.GONE);
        tempNumberOfShip++;
        // если не все корабли расставлены
        if (tempNumberOfShip < Field.TOTAL_NUMBER_OF_SHIPS) {
            btnSetNextShip.setVisibility(View.VISIBLE);
        // если все корабли расставлены
        } else if (tempNumberOfShip == Field.TOTAL_NUMBER_OF_SHIPS){
            btnFinishPutShips.setVisibility(View.VISIBLE);
            btnStartPutShips.setVisibility(View.VISIBLE);
            // если что, можно по новой поставить корабли
            btnStartPutShips.setText("Переустановить корабли");
            // на всякий случай, мало ли что
        } else {
            tvInformationText.setText("Упс, ХЗ, как так вышло в clickSetShipOfPlayer!");
        }
    }

    // переход к устрановке следующего корабля в ручном режиме
    public void clickSetNextShip(View view) {
        setShipsOnFieldManually();
        setTitle("Ставим корабль: " + tempNameOfShip);
        // если однопалубник, то поворачивать нет смысла
        if(tempSizeOfShip == 1) {
            rgIsVerticalShip.setVisibility(View.GONE);
        } else {
            rgIsVerticalShip.setVisibility(View.VISIBLE);
        }
        tvInformationText.setText(tempPlayer.getNameOfPlayer() + ", \nвыберите направление корабля \nи точку для его установки на поле");
        //перерисовать поле кликабельное
        repaintFieldOfPlayer(tempPlayer, true);
        glFieldOfPlayer.setVisibility(View.VISIBLE);
        etNameOfPlayer.setVisibility(View.GONE);
        rgIsPlayer.setVisibility(View.GONE);
        btnSetShips1.setVisibility(View.GONE);
        btnSetShips2.setVisibility(View.GONE);
        cbIsAutomaticallySetShips.setVisibility(View.GONE);
        btnStartPutShips.setVisibility(View.GONE);
        btnSetShipOfPlayer.setVisibility(View.GONE);
        btnSetNextShip.setVisibility(View.GONE);
        btnFinishPutShips.setVisibility(View.GONE);
        btnStartGame.setVisibility(View.GONE);
    }

    // закончить расстановку кораблей и сохранить результат
    public void clickFinishPutShips(View view) {
        etNameOfPlayer.setVisibility(View.GONE);
        rgIsPlayer.setVisibility(View.GONE);
        cbIsAutomaticallySetShips.setVisibility(View.GONE);
        btnStartPutShips.setVisibility(View.GONE);
        btnStartPutShips.setText("Начать расстановку кораблей");
        btnSetShipOfPlayer.setVisibility(View.GONE);
        btnSetNextShip.setVisibility(View.GONE);
        btnFinishPutShips.setVisibility(View.GONE);
        // сохраняем созданного игрока с полем и кораблями в статическом массиве класса
        Player.players[(tempIdOfPlayer - 1)] = tempPlayer;
        // если мы здесь, то первый игрок уже создан
        // проверяем наличие второго игрока, и в зависимости от этого
        // если второго игрока нет, предлагаем его создать
        if(Player.players[1] == null ) {
            setTitle("Создание игроков");
            tvInformationText.setText("\nНужно создать второго игрока\n");
            glFieldOfPlayer.setVisibility(View.GONE);
            etNameOfPlayer.setText("");
            rgIsPlayer.check(R.id.rbАndroid);
            btnSetShips1.setVisibility(View.VISIBLE);
            btnSetShips1.setText("Создать заново первого игрока");
            btnSetShips2.setVisibility(View.VISIBLE);
            rgIsVerticalShip.setVisibility(View.GONE);
            btnStartGame.setVisibility(View.GONE);
            // если второй игрок есть, можно переходить к игре
        } else {
            setTitle("Всё готово");
            tvInformationText.setText("Игроки созданы, \nкорабли расставлены, \nможно начинать!");
            glFieldOfPlayer.setVisibility(View.GONE);
            btnSetShips1.setVisibility(View.VISIBLE);
            btnSetShips1.setText("Создать заново первого игрока");
            btnSetShips2.setVisibility(View.VISIBLE);
            btnSetShips2.setText("Создать заново второго игрока");
            rgIsVerticalShip.setVisibility(View.GONE);
            btnStartGame.setVisibility(View.VISIBLE);
        }
    }

    // начало игры, запускаем игровую активность
    public void clickStartGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    // проверка на возможность установки корабля на поле по заданным параметрам
    private boolean isItCanToSetShipOnField(int coordinateX, int coordinateY, int sizeOfShip, boolean isVerticalShip, Point[][] cellsOfPlayer) {
        int x = coordinateX + 1;// +1 из-за технических нюансов. у нас хранится массив 12*12, а поле 10*10
        int y = coordinateY + 1;// 12*12 нужно для проверок вокруг точек
        // если корабль вертикальный
        if (isVerticalShip){
            // предварительная прорисовка силуэта корабля на поле
            for (int i = 0; i < sizeOfShip; i++) {
                if (x + i - 1 <= 9) {
                    String buttonID = "button" + (x + i - 1) + (y - 1);
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    imageButtonOfField = (ImageButton) findViewById(resID);
                    imageButtonOfField.setBackgroundResource(R.color.colorCellOfShoot);
                }
            }
            if(x >= 1 && (x + sizeOfShip - 1) <= 10) { // проверка на попадание в поле
                for (int i = (x - 1); i < (x + sizeOfShip + 1); i++) { // проверка на пересечение с другими кораблями по вертикали
                    for (int j = y - 1; j <= y + 1; j++) { // проверка на пересечение с другими кораблями по горизонтали
                        if (cellsOfPlayer[i][j].getValue() == 'O') {
                            return false;
                        }
                    }
                }
            } else
                return false;
            // если корабль горизонтальный
        } else {
            // предварительная прорисовка силуэта корабля на поле
            for (int j = 0; j < sizeOfShip; j++) {
                if (y + j - 1 <= 9) {
                    String buttonID = "button" + (x - 1) + (y + j - 1);
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    imageButtonOfField = (ImageButton) findViewById(resID);
                    imageButtonOfField.setBackgroundResource(R.color.colorCellOfShoot);
                }
            }
            if(y >= 1 && (y + sizeOfShip - 1) <= 10) {
                for (int j = (y - 1); j < (y + sizeOfShip + 1); j++) {// проверка на пересечение с другими кораблями по горизонтали
                    for (int i = x - 1; i <= x + 1; i++) {// проверка на пересечение с другими кораблями по вертикали
                        if (cellsOfPlayer[i][j].getValue() == 'O') {
                            return false;
                        }
                    }
                }
            } else
                return false;
        }
        return true;// по умолчанию, требует компилятор
    }

    // вместо цикла последовательность ручной установки кораблей на поле
    // здесь по очереди предлагается установить все корабли
    private void setShipsOnFieldManually() {
        tempCoordinateX = -1;
        tempCoordinateY = -1;
        int nos5 = Field.NUMBER_OF_SHIPS_5;
        int nos4 = Field.NUMBER_OF_SHIPS_4;
        int nos3 = Field.NUMBER_OF_SHIPS_3;
        int nos2 = Field.NUMBER_OF_SHIPS_2;
        int nos1 = Field.NUMBER_OF_SHIPS_1;
        if (tempNumberOfShip < nos5) {
            tempNameOfShip = "Пятипалубник-" + (tempNumberOfShip + 1);
            tempSizeOfShip = 5;
        } else if (nos5 <= tempNumberOfShip &&
                tempNumberOfShip < nos5 + nos4) {
            tempNameOfShip = "Четырёхпалубник-" + (tempNumberOfShip + 1 - nos5);
            tempSizeOfShip = 4;
        } else if (nos5 + nos4 <= tempNumberOfShip &&
                tempNumberOfShip < nos5 + nos4 + nos3) {
            tempNameOfShip = "Трёхпалубник-" + (tempNumberOfShip + 1 - (nos5 + nos4));
            tempSizeOfShip = 3;
        } else if (nos5 + nos4 + nos3 <= tempNumberOfShip &&
                tempNumberOfShip < nos5 + nos4 + nos3 + nos2) {
            tempNameOfShip = "Двухпалубник-" + (tempNumberOfShip + 1 - (nos5 + nos4 + nos3));
            tempSizeOfShip = 2;
        } else if (nos5 + nos4 + nos3 + nos2 <= tempNumberOfShip &&
                tempNumberOfShip < nos5 + nos4 + nos3 + nos2 + nos1) {
            tempNameOfShip = "Однопалубник-" + (tempNumberOfShip + 1 - (nos5 + nos4 + nos3 + nos2));
            tempSizeOfShip = 1;
        } else {
            // на всякий случай
            tvInformationText.setText("Упс, ХЗ, как так вышло в clickSetNextShip!");
        }
    }
    // метод перерисовки поля игрока с кораблями кликабельное или нет
    public void repaintFieldOfPlayer (Player player, boolean isClickableField){
        // получаем поле игрока
        Point[][] cellsOfPlayer = player.getFieldOfPlayer().getCellsOfPlayer();
        // проходим по всем кнопкам и ставим им соответствующий вид и кликабельность
        for (int i = 1; i < cellsOfPlayer.length - 1; i++) {
            for (int j = 1; j < cellsOfPlayer.length - 1; j++) {
                String buttonID = "button" + (i - 1) + (j - 1);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                imageButtonOfField = (ImageButton) findViewById(resID);
                imageButtonOfField.setClickable(isClickableField);
                switch (cellsOfPlayer[i][j].getValue()){
                    case '~':
                        imageButtonOfField.setBackgroundResource(R.color.colorCellSea);
                        imageButtonOfField.setImageResource(0);
                        break;
                    case '*':
                        imageButtonOfField.setBackgroundResource(R.color.colorCellMiss);
                        imageButtonOfField.setImageResource(R.drawable.miss_cell5);
                        break;
                    case 'O':
                        imageButtonOfField.setBackgroundResource(R.color.colorCellShip);
                        imageButtonOfField.setImageResource(R.drawable.ship_cell2);
                        break;
                    case 'X':
                        imageButtonOfField.setBackgroundResource(R.color.colorCellDamagedShip);
                        imageButtonOfField.setImageResource(R.drawable.damaged_cell3);
                        break;
                    case 'S':
                        imageButtonOfField.setBackgroundResource(R.color.colorCellDestroyedShip);
                        imageButtonOfField.setImageResource(R.drawable.destroyed_cell2);
                        break;
                    default:
                        // на всякий случай
                        tvInformationText.setText("Упс, чёт не так с repaintFieldOfPlayer!!!");
                }
            }
        }
    }
}