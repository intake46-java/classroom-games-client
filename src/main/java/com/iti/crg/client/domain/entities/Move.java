/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.iti.crg.client.domain.entities;

/**
 *
 * @author Osama
 */

import java.io.Serializable;

public class Move implements Serializable {
    private int row;
    private int col;
    private char playedCharacter; 

    public Move(int row, int col, char playedCharacter) {
        this.row = row;
        this.col = col;
        this.playedCharacter = playedCharacter;
    }

    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }

    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }

    public char getplayedCharacter() { return playedCharacter; }
    public void setplayedCharacter(char playedCharacter) { this.playedCharacter = playedCharacter; }
        public int getPositionIndex() {
        return row * 3 + col;
    }

}