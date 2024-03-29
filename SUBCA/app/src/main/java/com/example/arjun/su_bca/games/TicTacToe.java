package com.example.arjun.su_bca.games;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import com.example.arjun.su_bca.R;
import com.example.arjun.su_bca.utility;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToe extends AppCompatActivity {

    private Toolbar toolbar;
    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private TextView point1, point2;

    // Game Variables
    private int ActivePlayer = 1, pointOne = 0, pointTwo = 0, buttonsClicked = 0;
    private boolean shouldAutoPlay = false;
    private List<Integer> Player1 = new ArrayList<>();
    private List<Integer> Player2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        modeSelector();
        setupUIViews();
        initToolbar();
        allButtonFunctionings();

    }

    private void modeSelector () {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Mode Menu!")
                .setMessage("Would you like to play with your friend.\n1. Click 'Yes' to start playing with your friend.\n2. Click 'No' to play with the Computer.")
                .setPositiveButton("Yes", (dialog1, which) -> {
                    // Do the yes task.
                    shouldAutoPlay = false;
                })
                .setNegativeButton("No", (dialog12, which) -> {
                    // Do the no task.
                    shouldAutoPlay = true;
                })
                .show();
        dialog.setCancelable(false);
    }

    private void setupUIViews () {
        toolbar = findViewById(R.id.ToolbarTicTacToe);
        button1 = findViewById(R.id.ttcButton1);
        button2 = findViewById(R.id.ttcButton2);
        button3 = findViewById(R.id.ttcButton3);
        button4 = findViewById(R.id.ttcButton4);
        button5 = findViewById(R.id.ttcButton5);
        button6 = findViewById(R.id.ttcButton6);
        button7 = findViewById(R.id.ttcButton7);
        button8 = findViewById(R.id.ttcButton8);
        button9 = findViewById(R.id.ttcButton9);
        point1 = findViewById(R.id.playerPoint);
        point2 = findViewById(R.id.playerPoint2);
    }

    private void initToolbar () {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tic Tac Toe");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void allButtonFunctionings () {
        button1.setOnClickListener( v -> playGame(1, button1));
        button2.setOnClickListener( v -> playGame(2, button2));
        button3.setOnClickListener( v -> playGame(3, button3));
        button4.setOnClickListener( v -> playGame(4, button4));
        button5.setOnClickListener( v -> playGame(5, button5));
        button6.setOnClickListener( v -> playGame(6, button6));
        button7.setOnClickListener( v -> playGame(7, button7));
        button8.setOnClickListener( v -> playGame(8, button8));
        button9.setOnClickListener( v -> playGame(9, button9));
    }

    private void playGame(int buttonId, Button button) {

        if (ActivePlayer == 1) {
            button.setText("X");
            button.setTextColor(getColor(R.color.deepSkyBlue));
            Player1.add(buttonId);
            ActivePlayer = 2;
        }
        else if (ActivePlayer == 2) {
            button.setText("O");
            button.setTextColor(getColor(R.color.kawaiRed));
            Player2.add(buttonId);
            ActivePlayer = 1;
        }

        button.setClickable(false);
        buttonsClicked++;

        checkForWinner();

        if (ActivePlayer == 2) {
            if(shouldAutoPlay) {
                autoPlay();
            }
        }

    }
    
    private void autoPlay () {
        List<Integer> emptyCells = new ArrayList<>();
        
        for (int cell=1; cell<10; cell++) {
            if ( !(Player1.contains(cell) || Player2.contains(cell)) ) {
                emptyCells.add(cell);
            }
        }
        Random random = new Random();
        int randomIndex = random.nextInt(emptyCells.size());
        int cellId = emptyCells.get(randomIndex);
        Button button;
        
        switch (cellId) {
            case 1: {
                button = button1;
                break;
            }
            case 2: {
                button = button2;
                break;
            }
            case 3: {
                button = button3;
                break;
            }
            case 4: {
                button = button4;
                break;
            }
            case 5: {
                button = button5;
                break;
            }
            case 6: {
                button = button6;
                break;
            }
            case 7: {
                button = button7;
                break;
            }
            case 8: {
                button = button8;
                break;
            }
            case 9: {
                button = button9;
                break;
            }
            default: {
                button = button1;
            }
        }

        playGame(cellId, button);

    }

    private void checkForWinner () {
        boolean dontHaveWinner = true;
        int winner = -1;

        // First Row
        if (Player1.contains(1) && Player1.contains(2) && Player1.contains(3)) {
            winner = 1;
            dontHaveWinner = false;
        }
        if (Player2.contains(1) && Player2.contains(2) && Player2.contains(3)) {
            winner = 2;
            dontHaveWinner = false;
        }

        // Second Row
        if (Player1.contains(4) && Player1.contains(5) && Player1.contains(6)) {
            winner = 1;
            dontHaveWinner = false;
        }
        if (Player2.contains(4) && Player2.contains(5) && Player2.contains(6)) {
            winner = 2;
            dontHaveWinner = false;
        }

        // Third Row
        if (Player1.contains(7) && Player1.contains(8) && Player1.contains(9)) {
            winner = 1;
            dontHaveWinner = false;
        }
        if (Player2.contains(7) && Player2.contains(8) && Player2.contains(9)) {
            winner = 2;
            dontHaveWinner = false;
        }

        // First Column
        if (Player1.contains(1) && Player1.contains(4) && Player1.contains(7)) {
            winner = 1;
            dontHaveWinner = false;
        }
        if (Player2.contains(1) && Player2.contains(4) && Player2.contains(7)) {
            winner = 2;
            dontHaveWinner = false;
        }

        // Second Column
        if (Player1.contains(2) && Player1.contains(5) && Player1.contains(8)) {
            winner = 1;
            dontHaveWinner = false;
        }
        if (Player2.contains(2) && Player2.contains(5) && Player2.contains(8)) {
            winner = 2;
            dontHaveWinner = false;
        }

        // Third Column
        if (Player1.contains(3) && Player1.contains(6) && Player1.contains(9)) {
            winner = 1;
            dontHaveWinner = false;
        }
        if (Player2.contains(3) && Player2.contains(6) && Player2.contains(9)) {
            winner = 2;
            dontHaveWinner = false;
        }

        // Diagonal One
        if (Player1.contains(1) && Player1.contains(5) && Player1.contains(9)) {
            winner = 1;
            dontHaveWinner = false;
        }
        if (Player2.contains(1) && Player2.contains(5) && Player2.contains(9)) {
            winner = 2;
            dontHaveWinner = false;
        }

        // Diagonal Two
        if (Player1.contains(3) && Player1.contains(5) && Player1.contains(7)) {
            winner = 1;
            dontHaveWinner =  false;
        }
        if (Player2.contains(3) && Player2.contains(5) && Player2.contains(7)) {
            winner = 2;
            dontHaveWinner = false;
        }

        // All Buttons Clicked.
        if (dontHaveWinner) {
            if (buttonsClicked == 9) {
                winner = 0;
            }
        }

        // Show Winner Message

        if (winner != -1) {

            String message = "";
            String text = "";
            if (winner == 1) {
                message = "Blue team is the winner!";
                pointOne++;
                text = "Blue Team: " + pointOne;
                point1.setText(text);
            }
            if (winner == 2) {
                pointTwo++;
                message = "Red team is the winner!";
                text = "Red Team: " + pointTwo;
                point2.setText(text);
            }
            if (winner == 0) {
                message = "God damn!!! it's a tie.";
            }

            utility.showToast(TicTacToe.this, message);
            // reset buttons.
            resetGame();
        }

    }

    private void resetGame () {
        buttonsClicked = 0;
        Player1.clear();
        Player2.clear();
        button1.setText("");
        button1.setClickable(true);
        button2.setText("");
        button2.setClickable(true);
        button3.setText("");
        button3.setClickable(true);
        button4.setText("");
        button4.setClickable(true);
        button5.setText("");
        button5.setClickable(true);
        button6.setText("");
        button6.setClickable(true);
        button7.setText("");
        button7.setClickable(true);
        button8.setText("");
        button8.setClickable(true);
        button9.setText("");
        button9.setClickable(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}