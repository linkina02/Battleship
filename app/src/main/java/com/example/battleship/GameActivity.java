package com.example.battleship;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    // поля для вьюшек
    private TextView tvInformationText;
    private GridLayout glFieldOfPlayer;
    private ImageButton imageButtonOfField;
    private TextView tvRatingText;
    private Button btnFire;
    private Button btnNext;
    private Button btnChangePlayer;

    // поля, необходимые для игры
    private static int idOfCurrentPlayer;
    private static boolean isClickableField;
    private int tempCoordinateX;
    private int tempCoordinateY;
    private int numberOfMove;
    private int changesOfPlayers = 0;
    private boolean repeatMove = false;
    private boolean isGameOver = false;
    private final Handler handler = new Handler();
    private String ratingShipsForPlayer1 = " осталось " + Field.TOTAL_NUMBER_OF_SHIPS + " кораблей";
    private String ratingShipsForPlayer2 = " осталось " + Field.TOTAL_NUMBER_OF_SHIPS + " кораблей";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // получаем вьюшки по ID
        tvInformationText = findViewById(R.id.tvInformationText);
        glFieldOfPlayer = findViewById(R.id.glFieldOfPlayer);
        tvRatingText = findViewById(R.id.tvRatingText);
        btnFire = findViewById(R.id.btnFire);
        btnNext = findViewById(R.id.btnNext);
        btnChangePlayer = findViewById(R.id.btnChangePlayer);

        // обнуляем координаты высрела, точнее присвыиваем им -1, так как 0 на поле используется
        tempCoordinateX = -1;
        tempCoordinateY = -1;
        numberOfMove = 1;// порядковый номер хода
        Random random = new Random();
        // рандомно определяем, кто ходит первым
        idOfCurrentPlayer = random.nextInt(2);
        setTitle("Ход №: " + numberOfMove + " игрока: " + Player.players[idOfCurrentPlayer].getNameOfPlayer());
        // определяем, кто играет андроид или игрок
        if(Player.players[idOfCurrentPlayer].isItHuman() == true){
            repaintFieldOfPlayer(idOfCurrentPlayer, true);
            // метод для хода игрока
            moveOfPlayer(idOfCurrentPlayer);
        } else {
            repaintFieldOfPlayer(idOfCurrentPlayer, false);
            // метод для хода андроида
            moveOfAndroid(idOfCurrentPlayer);
        }
    }

    // действие при нажатии кнопки на поле
    public void clickButtonOnField(View view) {
        // находим нажатую кнопку и получаем её координаты
        imageButtonOfField = findViewById(view.getId());
        String tag = imageButtonOfField.getTag().toString();
        int b = Integer.parseInt(tag);
        tempCoordinateX = b / 10;
        tempCoordinateY = b % 10;
        repaintFieldOfPlayer(idOfCurrentPlayer, true);
        // если точка выбрана на поле, проверяем её
        if (!(tempCoordinateX == -1) && !(tempCoordinateY == -1)) {
            // предварительный просмотр точки на поле
            view.setBackgroundResource(R.color.colorCellOfShoot);
            // метод предварительной проверки выбранной точки
            previewCheckPoint(idOfCurrentPlayer, tempCoordinateX, tempCoordinateY);
        }
    }

    // действие на нажатие кнопки  Огонь!
    public void clickFire(View view) {
        // метод для проверки выстрела при ручном вводе и для автовыстрела
        if (!(tempCoordinateX == -1) && !(tempCoordinateY == -1)) {
            checkPointForShot(idOfCurrentPlayer, tempCoordinateX, tempCoordinateY);
        }
        // обнуляем координаты после выстрела
        tempCoordinateX = -1;
        tempCoordinateY = -1;
        // если попал, значит делаем ещё ход, если промазал, то переход хода
        if (repeatMove) {
            btnChangePlayer.setVisibility(View.GONE);
        } else {
            btnChangePlayer.setVisibility(View.VISIBLE);
        }
    }

    // кнопка, когда андроид попал и по нажатию этой кнопки делает ещё ход
    public void clickNext(View view) {
        repaintFieldOfPlayer(idOfCurrentPlayer, false);
        btnNext.setVisibility(View.GONE);
        btnFire.setVisibility(View.GONE);
        btnChangePlayer.setVisibility(View.GONE);
        // метод для хода андроида
        moveOfAndroid(idOfCurrentPlayer);
    }

    // кнопка для перехода хода к другому игроку
    public void clickChangePlayer(View view) {
        // считаем ходы по сменам хода
        changesOfPlayers++;
        numberOfMove = 1 + changesOfPlayers/2; // один ход это 2 перехода хода
        if(idOfCurrentPlayer == 0) {
            idOfCurrentPlayer = 1;
        } else {
            idOfCurrentPlayer = 0;
        }
        setTitle("Ход №: " + numberOfMove + " игрока: " + Player.players[idOfCurrentPlayer].getNameOfPlayer());
        tvRatingText.setVisibility(View.GONE);
        // определяем, кто ходит дальше игрок или андроид
        if(Player.players[idOfCurrentPlayer].isItHuman() == true){
            repaintFieldOfPlayer(idOfCurrentPlayer, true);
            // ход игрока
            moveOfPlayer(idOfCurrentPlayer);
        } else {
            repaintFieldOfPlayer(idOfCurrentPlayer, false);
            // ход андроида
            moveOfAndroid(idOfCurrentPlayer);
        }
    }

    // ход игрока
    private void moveOfPlayer(int idOfCurrentPlayer) {
        tvInformationText.setText(Player.players[idOfCurrentPlayer].getNameOfPlayer() + ",\nвыберите точку для выстрела\n");
        glFieldOfPlayer.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.GONE);
        btnFire.setVisibility(View.GONE);
        btnChangePlayer.setVisibility(View.GONE);
        // ждём пока выберет точку на поле
    }

    // ход андроида
    private void moveOfAndroid(int idOfCurrentPlayer) {
        // обнуляем координаты перед ходом
        tempCoordinateX = -1;
        tempCoordinateY = -1;
        // пауза перед вытрелом
//        try {
//            Thread.sleep(200); // должно регулироваться из опций
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        int idOfTargetPlayer;
        if(idOfCurrentPlayer == 0) {
            idOfTargetPlayer = 1;
        } else {
            idOfTargetPlayer = 0;
        }
        // находим, кто стреляет и в кого
        Player shootPlayer = Player.players[idOfCurrentPlayer];
        Player targetPlayer = Player.players[idOfTargetPlayer];
        // вызываем обширный метод для получения точки выстрела компьютера
        // тут заложена вся логика выстрела андроида
        Point pointForShoot = shootPlayer.getCoordinateOfPointFromComputer(targetPlayer);
        // координаты получаются от 1 до 10, поэтому переводим в нашу шкалу от 0 до 9
        int tempCoordinateX = (pointForShoot.getCoordinateX() - 1);
        int tempCoordinateY = (pointForShoot.getCoordinateY() - 1);
        // пауза после выстрела
//        try {
//            Thread.sleep(200); // должно регулироваться из опций
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // если точка выбрана
        if (!(tempCoordinateX == -1) && !(tempCoordinateY == -1)) {
            // делаем выстрел
            checkPointForShot(idOfCurrentPlayer, tempCoordinateX, tempCoordinateY);
        }
        // если игра не окончена
        if(!isGameOver) {
            // если попал, то делает ещё ход,
            if (repeatMove) {
                btnChangePlayer.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
                //   если промазал - смена игрока
            } else {
                btnChangePlayer.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
            }
        } else {
            btnChangePlayer.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        }
        glFieldOfPlayer.setVisibility(View.VISIBLE);
        btnFire.setVisibility(View.GONE);
    }

    // метод предварительного просмотра точки на поле
    private void previewCheckPoint(int idOfCurrentPlayer, int coordinateX, int coordinateY){
        int x = coordinateX + 1;
        int y = coordinateY + 1;
        int idOfTargetPlayer;
        if(idOfCurrentPlayer == 0) {
            idOfTargetPlayer = 1;
        } else {
            idOfTargetPlayer = 0;
        }
        Player shootPlayer = Player.players[idOfCurrentPlayer];
        Player targetPlayer = Player.players[idOfTargetPlayer];

        // проверка целесообразности выстрела
        switch (targetPlayer.getFieldOfPlayer().getCellsOfPlayer()[x][y].getValue()) {
            // если ещё не стрелял сюда
            case '~':
                tvInformationText.setText("Выбрана точка с координатами:" +
                        "\nx = " + tempCoordinateX + ", y = " + tempCoordinateY + ". Можно выстрелить." +
                        "\nСтреляем?");
                btnFire.setVisibility(View.VISIBLE);
                break;
            // если уже стрелял сюда
            case '*':
                tvInformationText.setText("Выбрана точка с координатами:" +
                        "\nx = " + tempCoordinateX + ", y = " + tempCoordinateY + ". Уже стрелял сюда." +
                        "\nСтрелять нет смысла.");
                btnFire.setVisibility(View.GONE);
                break;
            // если ещё не стрелял сюда
            case 'O':
                tvInformationText.setText("Выбрана точка с координатами:" +
                        "\nx = " + tempCoordinateX + ", y = " + tempCoordinateY + ". Можно выстрелить!" +
                        "\nСтреляем?");
                btnFire.setVisibility(View.VISIBLE);
                break;
            // если уже попадал сюда
            case 'X':
                tvInformationText.setText("Выбрана точка с координатами:" +
                        "\nx = " + tempCoordinateX + ", y = " + tempCoordinateY + ". Уже попал сюда." +
                        "\nСтрелять нет смысла.");
                btnFire.setVisibility(View.GONE);
                break;
            // если уже потопил корабль здесь
            case'S':
                tvInformationText.setText("Выбрана точка с координатами:" +
                        "\nx = " + tempCoordinateX + ", y = " + tempCoordinateY + ". Уже потопил здесь." +
                        "\nСтрелять нет смысла.");
                btnFire.setVisibility(View.GONE);
                break;
            // это, чтобы успокоить компилятор
            default:
                tvInformationText.setText("На поле посторонние символы!");
        }
    }

    // выстрел игрока по координатам точки
    private void checkPointForShot(int idOfCurrentPlayer, int coordinateX, int coordinateY) {
        int x = coordinateX + 1;
        int y = coordinateY + 1;
        int idOfTargetPlayer;
        if(idOfCurrentPlayer == 0) {
            idOfTargetPlayer = 1;
        } else {
            idOfTargetPlayer = 0;
        }
        // определяем, кто стреляет и в кого
        Player shootPlayer = Player.players[idOfCurrentPlayer];
        Player targetPlayer = Player.players[idOfTargetPlayer];
        String message = shootPlayer.getNameOfPlayer() +
                " выбрал(а) точку [" + coordinateX + ", " + coordinateY + "].";
        // считаем количество выстрелов
        shootPlayer.setNumberOfShots(shootPlayer.getNumberOfShots() + 1);
        switch (targetPlayer.getFieldOfPlayer().getCellsOfPlayer()[x][y].getValue()) {
            // если промазал
            case '~':
                tvInformationText.setText(message + "\nМимо! \nПереход хода");
                targetPlayer.getFieldOfPlayer().getCellsOfPlayer()[x][y].setValue('*');
                repaintFieldOfPlayer(idOfCurrentPlayer, false);
                repeatMove = false;
                btnFire.setVisibility(View.GONE);
                btnChangePlayer.setVisibility(View.VISIBLE);
                break;
            // если уже стрелял сюда
            case '*':
                tvInformationText.setText(message + "\nВы уже стреляли сюда!\nПереход хода");
                repeatMove = false;
                btnFire.setVisibility(View.GONE);
                btnChangePlayer.setVisibility(View.VISIBLE);
                break;
            // если попал
            case 'O':
                Point pointToShoot = new Point(x, y, 'X');
                // считаем количество попаданий
                shootPlayer.setNumberOfHits(shootPlayer.getNumberOfHits() + 1);
                // проверяем, в какой корабль мы попали
                Ship[] shipsOfTargetPlayer = targetPlayer.getFieldOfPlayer().getShipsOfPlayer();
                for (int i = 0; i < shipsOfTargetPlayer.length; i++) {
                    Ship currentShip = shipsOfTargetPlayer[i];
                    // результату проверки присваиваем символ
                    char valueOfShoot = currentShip.checkIsDamage(pointToShoot, targetPlayer);
                    // проверяем символ
                    switch (valueOfShoot) {
                        case '0': // не попал или уже стрелял
                            break;
                        case 'X': // попал, но не убил
                            repeatMove = true;
                            break;
                        case 'S': // попал и убил
                            // проверяем наличие живых кораблей у обстреливаемого игрока
                            targetPlayer.checkNumberOfShipsAlive();
                            // проверяем, окончена ли игра
                            isGameOver = checkForWin(idOfCurrentPlayer);
                            repeatMove = true;
                            // если попали сначала проверим на победителя, если продолжаем, то стреляем ещё раз
                            if (repeatMove) {
                                // если конец игры
                                if (isGameOver) {
                                    repaintFieldOfPlayer(idOfCurrentPlayer, false);
                                    tvInformationText.setText("Все корабли игрока " + targetPlayer.getNameOfPlayer() +
                                            " уничтожены!\nПобедил игрок " + shootPlayer.getNameOfPlayer() +
                                    "\nПримите наши поздравления!");
                                    btnFire.setVisibility(View.GONE);
                                    btnNext.setVisibility(View.GONE);
                                    btnChangePlayer.setVisibility(View.GONE);
                                    tvRatingText.setVisibility(View.VISIBLE);
                                } else {
                                    tvInformationText.setText(message + "\nКорабль " +
                                            currentShip.getNameOfShip() + " уничтожен!\nСтреляйте ещё раз!");
                                }
                            }
                            break;
                        default: // на всякий случай
                            tvInformationText.setText("Ошибка в методе checkIsDamage!");
                    }
                }
                if (shootPlayer.isItHuman() == true) { // перерисовываем поле в зависимости от игрока
                    repaintFieldOfPlayer(idOfCurrentPlayer, true);
                } else {
                    repaintFieldOfPlayer(idOfCurrentPlayer, false);
                }
                btnFire.setVisibility(View.GONE);
                btnChangePlayer.setVisibility(View.GONE);
                break;
            // если уже попадал сюда
            case 'X':
                tvInformationText.setText("Вы уже попали здесь в корабль!\nПереход хода\n");
                repeatMove = false;
                btnFire.setVisibility(View.GONE);
                btnChangePlayer.setVisibility(View.VISIBLE);
                break;
                // если уже попадал сюда и потопил
            case 'S':
                tvInformationText.setText("Вы уже попали здесь в корабль\n и уничтожили его! \nПереход хода");
                repeatMove = false;
                btnFire.setVisibility(View.GONE);
                btnChangePlayer.setVisibility(View.VISIBLE);
                break;
            // это, чтобы успокоить компилятор
            default: // если обнаружатся другие символы
                tvInformationText.setText("На поле посторонние символы!");
        }
        if (!isGameOver) {
            if (idOfCurrentPlayer == 0) {
                tvRatingText.setText("Количество выстрелов: " + shootPlayer.getNumberOfShots() +
                        "\nКоличество попаданий: " + shootPlayer.getNumberOfHits() +
                        "\n У игрока " + targetPlayer.getNameOfPlayer() + ratingShipsForPlayer1);
            } else {
                tvRatingText.setText("Количество выстрелов: " + shootPlayer.getNumberOfShots() +
                        "\nКоличество попаданий: " + shootPlayer.getNumberOfHits() +
                        "\n У игрока " + targetPlayer.getNameOfPlayer() + ratingShipsForPlayer2);
            }
            tvRatingText.setVisibility(View.VISIBLE);
        }
    }

    // перерисовка поля в зависимости от игрока и кликабельности
    public void repaintFieldOfPlayer (int idOfCurrentPlayer, boolean isClickableField){
        this.isClickableField = isClickableField;
        int idOfTargetPlayer;
        if (GameActivity.idOfCurrentPlayer == 0) {
            idOfTargetPlayer = 1;
        } else {
            idOfTargetPlayer = 0;
        }
        // получение поля противника
        Point[][] cellsOfPlayer = Player.players[idOfTargetPlayer].getFieldOfPlayer().getCellsOfPlayer();
        // перебираем все кнопки на поле и изменяем их вид в зависимости от символа на поле
        for (int i = 1; i < cellsOfPlayer.length - 1; i++) {
            for (int j = 1; j < cellsOfPlayer.length - 1; j++) {
                // получаем кнопку
                String buttonID = "button" + (i - 1) + (j - 1);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                imageButtonOfField = (ImageButton) findViewById(resID);
                // ставим кликабельность
                imageButtonOfField.setClickable(GameActivity.isClickableField);
                // меняем её вид в зависимости от символа
                switch (cellsOfPlayer[i][j].getValue()) {
                    case '~':
                        imageButtonOfField.setBackgroundResource(R.color.colorCellSea);
                        imageButtonOfField.setImageResource(0);
                        break;
                    case '*':
                        imageButtonOfField.setBackgroundResource(R.color.colorCellMiss);
                        imageButtonOfField.setImageResource(R.drawable.miss_cell5);
                        break;
                    case 'O':
                        if(Player.players[GameActivity.idOfCurrentPlayer].isItHuman() == false) {
                            imageButtonOfField.setBackgroundResource(R.color.colorCellShip);
                            imageButtonOfField.setImageResource(R.drawable.ship_cell2);
                        } else {
                            imageButtonOfField.setBackgroundResource(R.color.colorCellSea);
                            imageButtonOfField.setImageResource(0);
                        }
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
                        tvInformationText.setText("Упс, чёт не так с repaintFieldOfPlayer!!!");
                }
            }
        }
    }

    // проверка победителя
    boolean checkForWin(int idOfCurrentPlayer) {
        // если корабли ещё есть у обстреливаемого игрока
        int idOfTargetPlayer;
        if(idOfCurrentPlayer == 0) {
            idOfTargetPlayer = 1;
        } else {
            idOfTargetPlayer = 0;
        }
        // определяем, кто стреляет и в кого
        Player shootPlayer = Player.players[idOfCurrentPlayer];
        Player targetPlayer = Player.players[idOfTargetPlayer];
        String messageAboutShips = "";
        if(targetPlayer.getNumberOfShipsAlive() > 0) {
            // если он последний
            if(targetPlayer.getNumberOfShipsAlive() == 1) {
                messageAboutShips = " остался последний корабль!";
                // если не последний
            } else {
                String wordShip = "";
                // если их больше или равно 5
                if (targetPlayer.getNumberOfShipsAlive() >= 5) {
                    wordShip = (" кораблей!");
                    // если 2, 3 или 4, такие заморочки, чтобы русский язык был соблюдён
                } else {
                    if(targetPlayer.getNumberOfShipsAlive() >= 2){
                        wordShip = (" корабля!");
                    }
                }
                messageAboutShips = " осталось " + targetPlayer.getNumberOfShipsAlive() + wordShip;
            }
            if(idOfCurrentPlayer == 0){
                ratingShipsForPlayer1 = messageAboutShips;
            } else {
                ratingShipsForPlayer2 = messageAboutShips;
            }
            return false;
            // если корабли кончились, то у нас победитель,  выдаём информацию и закругляемся
        } else {
            tvRatingText.setText("Все корабли игрока" + " " + targetPlayer.getNameOfPlayer() + " уничтожены!\nИгра окончена!" +
            "\nИгрок " + shootPlayer.getNameOfPlayer() + " победил(а)!" +
            "\nКоличество ходов: " + numberOfMove +
            "\nКоличество выстрелов игрока " + Player.players[0].getNameOfPlayer() + ": " + Player.players[0].getNumberOfShots() +
            "\nКоличество выстрелов игрока " + Player.players[1].getNameOfPlayer() + ": " + Player.players[1].getNumberOfShots() +
            "\nКоличество попаданий игрока " + Player.players[0].getNameOfPlayer() + ": " + Player.players[0].getNumberOfHits() +
            "\nКоличество попаданий игрока " + Player.players[1].getNameOfPlayer() + ": " + Player.players[1].getNumberOfHits());
            return true;
        }
    }
}
