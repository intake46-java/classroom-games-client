package com.iti.crg.client.domain.entities;

public class Player {
    private String username;
    private int score;
    private int status; // 1 = online, 0 = offline

    public Player(String username, int score, int status) {
        this.username = username;
        this.score = score;
        this.status = status;
    }

    public String getUsername() { return username; }
    public int getScore() { return score; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}
