/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.iti.crg.client.domain.aistrategy;

import com.iti.crg.client.domain.entities.Board;
import com.iti.crg.client.domain.entities.Cell;
import java.util.Random;

/**
 *
 * @author Osama
 */
public class EasyTicTacToeAi implements AiStrategy{
    private Random random = new Random();
    @Override
    public Cell AIMove(Board board) {
       Cell[] emptyCells = board.emptyCells();
       if (emptyCells.length == 0) return null;
       Cell randomCell = emptyCells[random.nextInt(emptyCells.length)];
       return randomCell;  
    }


}
