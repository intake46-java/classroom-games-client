package com.iti.crg.client.infrastructure.dto; // Adjust package for Server side

public class GameMoveDto {
    private String player;
    private int row;
    private int col;
    private char symbol; // 'X' or 'O'

    public GameMoveDto(String player, int row, int col, char symbol) {
        this.player = player;
        this.row = row;
        this.col = col;
        this.symbol = symbol;
    }

    // Getters
    public String getPlayer() { return player; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public char getSymbol() { return symbol; }
}