/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iti.crg.client.domain.game.aistrategy;

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

    private int minimax(Board board, int depth, boolean isAi) {
        char[][] grid = board.getGrid();
        int score = evaluate(grid);

        if (score == 10) {
            return score - depth;
        }
        if (score == -10) {
            return score + depth;
        }
        if (!isMovesLeft(board)) {
            return 0;
        }

        if (isAi) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (grid[i][j] == '_') {
                        grid[i][j] = AiCharXO;
                        best = Math.max(best, minimax(board, depth + 1, false));
                        grid[i][j] = '_';
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (grid[i][j] == '_') {
                        grid[i][j] = opponentCharXO;
                        best = Math.min(best, minimax(board, depth + 1, true));
                        grid[i][j] = '_';
                    }
                }
            }
            return best;
        }
    }

    private Cell findBestMove(Board board) {
        char[][] grid = board.getGrid();
        int bestVal = Integer.MIN_VALUE;
        Cell bestCell = new Cell(-1, -1);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                        if(grid[i][j]=='_'){
                            grid[i][j] = AiCharXO;
                            int moveVal = minimax(board,0,false);
                            grid[i][j]='_';
                            if(moveVal > bestVal){
                                bestCell.setRow(i);
                                bestCell.setCol(j);
                                bestVal = moveVal;
                            }
                        }
            }
        }
        return bestCell;
    }

    @Override
    public Cell AIMove(Board board) {
        return findBestMove(board);
    }

}
