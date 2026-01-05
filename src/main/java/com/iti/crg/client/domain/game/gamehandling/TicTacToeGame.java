/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iti.crg.client.domain.game.gamehandling;

import com.iti.crg.client.domain.entities.Cell;
import com.iti.crg.client.domain.entities.TicTacToeBoard;

/**
 *
 * @author dell
 */
public class TicTacToeGame extends TicTacToeBoard implements GameHandling {

    @Override
    public boolean isValidMove(Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();
        char[][] grid = getGrid();

        return row >= 0 && row < 3
                && col >= 0 && col < 3
                && grid[row][col] == ' ';
    }

    @Override
    public boolean isWinner(TicTacToeBoard board) {
        char[][] grid = board.getGrid();
        for (int i = 0; i < board.getRows(); i++) {
            // Check Rows
            if (grid[i][0] != ' ' && grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2]) {
                return true;
            } // Check Columns
            else if (grid[0][i] != ' ' && grid[0][i] == grid[1][i] && grid[1][i] == grid[2][i]) {
                return true;
            }
        }
        // Check Diagonals
        if (grid[1][1] != ' ') {
            if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]) {
                return true;
            } else if (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isTie(TicTacToeBoard board) {
        return !isWinner(board) && board.isFull();
    }

}
