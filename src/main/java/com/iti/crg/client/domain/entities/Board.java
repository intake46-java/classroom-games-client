/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.iti.crg.client.domain.entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Osama
 */
public abstract class Board {
    protected int rows;
    protected int cols;
    protected char[][] grid;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = ' ';
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public char[][] getGrid() {
        return grid;
    }

    public Cell[] emptyCells() {
        List<Cell> empty = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == ' ') {
                    empty.add(new Cell(r, c));
                }
            }
        }
        return empty.toArray(new Cell[0]);
    }

    public boolean isFull() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == ' ') return false;
            }
        }
        return true;
    }
}