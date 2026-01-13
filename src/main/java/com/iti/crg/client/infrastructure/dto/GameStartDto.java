package com.iti.crg.client.infrastructure.dto;

public class GameStartDto {
    private String opponent;
    private char mySymbol; // 'X' or 'O'
    private boolean isTurn;

    public GameStartDto(String opponent, char mySymbol, boolean isTurn) {
        this.opponent = opponent;
        this.mySymbol = mySymbol;
        this.isTurn = isTurn;
    }

    public String getOpponent() { return opponent; }
    public char getMySymbol() { return mySymbol; }
    public boolean isTurn() { return isTurn; }
}