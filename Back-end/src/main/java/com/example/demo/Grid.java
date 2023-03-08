package com.example.demo;

import lombok.Getter;
import org.springframework.stereotype.Component;
import static com.example.demo.ConnectionHandler.encrypt;

@Component
public class Grid {
    private int userTurn = 1;
    private static final String WHITE = "#FFFFFF";
    private static final String GREEN = "#00FF00";

    @Getter
    private char winner = ' '; // stands for empty
    @Getter
    private char[][] grid;
    @Getter
    private final int width = 3;
    @Getter
    private final int height = 3;

    @Getter
    private String[][] colorGrid;

    public Grid() {
        colorGrid = new String[width][height];
        grid = new char[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                colorGrid[y][x] = WHITE;
                grid[y][x] = ' '; // stands for empty
            }
        }
    }

    public Grid mark(MarkMessage m) {
        if(winner != ' ')
            return this;

        boolean no_op = true;

        String currentToken;
        switch (userTurn) {
            case 1 -> currentToken = ConnectionHandler.user1;
            case 2 -> currentToken = ConnectionHandler.user2;
            default -> currentToken = "";
        }

        char cell = grid[m.getPosY()][m.getPosX()];
        if(encrypt(m.getToken()).equals(currentToken) && cell == ' ') {
            no_op = false;
            grid[m.getPosY()][m.getPosX()] = (userTurn == 1) ? 'x' : 'o';
            checkWinner();
        }

        if(!no_op)
            userTurn = (userTurn == 1) ? 2 : 1;

        return this;
    }

    private void checkWinner() {
        // check for row
        for(int i = 0; i < 3; i++) {
            if(grid[i][0] == ' ')
                continue;
            if(grid[i][0] == grid[i][1] && grid[i][0] == grid[i][2]) {
                winner = grid[i][0];
                colorGrid[i][0] = GREEN;
                colorGrid[i][1] = GREEN;
                colorGrid[i][2] = GREEN;
                return;
            }
        }

        // check for column
        for(int j = 0; j < 3; j++) {
            if(grid[0][j] == ' ')
                continue;
            if(grid[0][j] == grid[1][j] && grid[0][j] == grid[2][j]) {
                winner = grid[0][j];
                colorGrid[0][j] = GREEN;
                colorGrid[1][j] = GREEN;
                colorGrid[2][j] = GREEN;
                return;
            }
        }

        // check for diagonals
        if(grid[1][1] != ' ' && grid[0][0] == grid[1][1] && grid[0][0] == grid[2][2]) {
            winner = grid[1][1];
            colorGrid[0][0] = GREEN;
            colorGrid[1][1] = GREEN;
            colorGrid[2][2] = GREEN;
            return;
        }

        if(grid[1][1] != ' ' && grid[0][2] == grid[1][1] && grid[0][2] == grid[2][0]) {
            winner = grid[1][1];
            colorGrid[0][2] = GREEN;
            colorGrid[1][1] = GREEN;
            colorGrid[2][0] = GREEN;
            return;
        }

        boolean isTie = true;
        for(int i = 0; i < height; i++)
            for(int j = 0; j <width; j++)
                if (grid[i][j] == ' ') {
                    isTie = false;
                    break;
                }
        if(isTie)
            winner = 't';
    }
}
