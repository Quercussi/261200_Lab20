package com.example.demo;

import lombok.Getter;
import org.springframework.stereotype.Component;
import static com.example.demo.ConnectionHandler.encrypt;

@Component
public class Grid {
    private int userTurn = 1;

    @Getter
    private char winner = 'e'; // stands for empty
    @Getter
    private char[][] grid;
    @Getter
    private final int width = 3;
    @Getter
    private final int height = 3;

    public Grid() {
        grid = new char[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = 'e'; // stands for empty
            }
        }
    }

    public Grid mark(MarkMessage m) {
        if(winner != 'e')
            return this;

        boolean no_op = true;

        String currentToken;
        switch (userTurn) {
            case 1 -> currentToken = ConnectionHandler.user1;
            case 2 -> currentToken = ConnectionHandler.user2;
            default -> currentToken = "";
        }

        char cell = grid[m.getPosY()][m.getPosX()];
        if(encrypt(m.getToken()).equals(currentToken) && cell == 'e') {
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
            if(grid[i][0] == 'e')
                continue;
            if(grid[i][0] == grid[i][1] && grid[i][0] == grid[i][2]) {
                winner = grid[i][0];
                return;
            }
        }

        // check for column
        for(int j = 0; j < 3; j++) {
            if(grid[0][j] == 'e')
                continue;
            if(grid[0][j] == grid[1][j] && grid[0][j] == grid[2][j]) {
                winner = grid[0][j];
                return;
            }
        }

        // check for diagonals
        if(grid[1][1] != 'e' && grid[0][0] == grid[1][1] && grid[0][0] == grid[2][2]) {
            winner = grid[1][1];
            return;
        }

        if(grid[1][1] != 'e' && grid[0][2] == grid[1][1] && grid[0][2] == grid[2][0]) {
            winner = grid[1][1];
            return;
        }
    }
}
