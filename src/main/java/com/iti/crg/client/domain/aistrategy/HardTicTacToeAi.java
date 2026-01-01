/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iti.crg.client.domain.aistrategy;

import com.iti.crg.client.domain.entities.Board;
import com.iti.crg.client.domain.entities.Cell;

/**
 *
 * @author Osama
 */
public class HardTicTacToeAi implements AiStrategy {

    private char AiCharXO;
    private char opponentCharXO;

    public HardTicTacToeAi(char AiCharXO) {
        this.AiCharXO = Character.toUpperCase(AiCharXO);
        this.opponentCharXO = (this.AiCharXO == 'X') ? 'O' : 'X';
    }

    private boolean isMovesLeft(Board board) {
        return !(board.isFull());
    }

    private int checkLine(char a, char b, char c) {
        if (a != ' ' && a == b && b == c) {
            if (a == AiCharXO) {
                return +10;
            } else if (a == opponentCharXO) {
                return -10;
            }
        }
        return 0;
    }

    private int evaluate(char[][] grid) {
        for (int row = 0; row < 3; row++) {
            int score = checkLine(grid[row][0], grid[row][1], grid[row][2]);
            if (score != 0) {
                return score;
            }
        }

        for (int col = 0; col < 3; col++) {
            int score = checkLine(grid[0][col], grid[1][col], grid[2][col]);
            if (score != 0) {
                return score;
            }
        }

        int score = checkLine(grid[0][0], grid[1][1], grid[2][2]);
        if (score != 0) {
            return score;
        }

        score = checkLine(grid[0][2], grid[1][1], grid[2][0]);
        if (score != 0) {
            return score;
        }

        return 0;
    }
    private int minimax
    @Override
    public Cell AIMove(Board board) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
