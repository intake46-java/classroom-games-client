package com.iti.crg.client.controllers.utils;

public class ScoreManager {

    private static String p1Name;
    private static String p2Name;
    private static int p1Score = 0;
    private static int p2Score = 0;
    private static boolean isSinglePlayer;
    private static String difficulty;

    public static void startNewSession(String p1, String p2, boolean single, String diff) {
        p1Name = p1;
        p2Name = p2;
        isSinglePlayer = single;
        difficulty = diff;
    }

    public static void increaseScorePlayer1() { p1Score++; }
    public static void increaseScorePlayer2() { p2Score++; }

    public static int getP1Score() { return p1Score; }
    public static int getP2Score() { return p2Score; }

    public static String getP1Name() { return p1Name; }
    public static String getP2Name() { return p2Name; }

    public static boolean isSinglePlayerMode() { return isSinglePlayer; }
    public static String getDifficulty() { return difficulty; }

    public static void setP1Score(int p1Score) {
        ScoreManager.p1Score = p1Score;
    }

    public static void setP2Score(int p2Score) {
        ScoreManager.p2Score = p2Score;
    }
    
    
}