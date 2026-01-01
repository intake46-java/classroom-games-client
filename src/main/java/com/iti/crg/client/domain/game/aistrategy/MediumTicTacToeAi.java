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
public class MediumTicTacToeAi implements AiStrategy {

    private boolean isBestMove;
    EasyTicTacToeAi easy;
    HardTicTacToeAi hard;

    public MediumTicTacToeAi(char AiCharXO) {
        this.isBestMove = false;
        easy = new EasyTicTacToeAi();
        hard = new HardTicTacToeAi(AiCharXO);
    }

    public boolean isBestMove() {
        return isBestMove;
    }

    @Override
    public Cell AIMove(Board board) {
        if (isBestMove) {
            isBestMove = false;
            return hard.AIMove(board);
        } else {
            isBestMove = true;
            return easy.AIMove(board);
        }
    }

}
