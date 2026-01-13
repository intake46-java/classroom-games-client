package com.iti.crg.client.domain.game.gamehandling;

import com.iti.crg.client.domain.entities.Cell;
import com.iti.crg.client.domain.entities.TicTacToeBoard;

public class TicTacToeGame extends TicTacToeBoard implements GameHandling {

    private char currentPlayer;
    private boolean isGameOver;

    public TicTacToeGame() {
        super();
        startGame();
    }


    @Override
    public boolean makeMove(int row, int col) {
        if (isGameOver || !isValidMove(new Cell(row, col))) {
            return false;
        }
        grid[row][col] = currentPlayer;
        return true;
    }

    @Override
    public boolean checkWin() {
        return isWinner();
    }

    @Override
    public boolean checkTie() {
        return isTie(this);
    }

    @Override
    public boolean isGameOver() {
        return isGameOver;
    }

    @Override
    public char getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void startGame() {
        for (int r = 0; r < getRows(); r++) {
            for (int c = 0; c < getCols(); c++) {
                grid[r][c] = '_';
            }
        }
        currentPlayer = 'X';
        isGameOver = false;
    }

    @Override
    public void endGame() {
        isGameOver = true;
    }

    @Override
    public void changeTurn() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }


    public boolean isValidMove(Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();
        char[][] grid = getGrid();

        return row >= 0 && row < 3
                && col >= 0 && col < 3
                && grid[row][col] == '_';
    }    
    
    

    public boolean isWinner() {
        char[][] grid = this.getGrid();
        for (int i = 0; i < this.getRows(); i++) {

            if (grid[i][0] != '_' && grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2]) {
                return true;
            }
            else if (grid[0][i] != '_' && grid[0][i] == grid[1][i] && grid[1][i] == grid[2][i]) {
                return true;
            }
        }

        if (grid[1][1] != '_') {
            if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]) {
                return true;
            } else if (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]) {
                return true;
            }
        }

        return false;
    }

    public boolean isTie(TicTacToeBoard board) {
        return !isWinner() && board.isFull();
    }

}