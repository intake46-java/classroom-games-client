/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.iti.crg.client.domain.entities;

/**
 *
 * @author Osama
 */

import com.iti.crg.client.domain.entities.Move;
import java.io.Serializable;
public class GameRecord implements Serializable {
    private final String player1;
    private final String player2;
    private final Move[] moves;

    public GameRecord(String player1, String player2, Move[] moves) {
        this.player1 = player1;
        this.player2 = player2;
        this.moves = moves;
    }

    public String getPlayer1() { return player1; }
    public String getPlayer2() { return player2; }
    public Move[] getMoves() { return moves; }
}