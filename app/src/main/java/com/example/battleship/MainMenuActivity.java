package com.example.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setTitle("Морской Бой");
        Button optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setVisibility(View.GONE);
    }

    public void onClickStartGame(View view) {
        Player.players[0] = null;
        Player.players[1] = null;
        Intent intent = new Intent(this, PutShipsActivity.class);
        startActivity(intent);
    }

    public void onClickOptions(View view) {

    }
}
