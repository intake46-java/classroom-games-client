//package com.iti.crg.client.controllers;
//
//import com.iti.crg.client.domain.entities.Cell;
//import com.iti.crg.client.domain.game.aistrategy.AiStrategy;
//import com.iti.crg.client.domain.game.aistrategy.EasyTicTacToeAi;
//import com.iti.crg.client.domain.game.aistrategy.HardTicTacToeAi;
//import com.iti.crg.client.domain.game.aistrategy.MediumTicTacToeAi;
//import com.iti.crg.client.domain.game.gamecontext.GameContext;
//import com.iti.crg.client.domain.game.gamecontext.OfflineSingleGameContext;
//import com.iti.crg.client.domain.game.gamehandling.GameHandling;
//import com.iti.crg.client.domain.game.gamehandling.TicTacToeGame;
//import java.net.URL;
//import java.util.ResourceBundle;
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//
//import javafx.scene.control.Button;
//import javafx.scene.layout.GridPane;
//
//public class GameBoardController implements Initializable, Runnable{
//
//
//    @FXML
//    private GridPane boardGrid;
//    @FXML
//    private Button b0;
//    @FXML
//    private Button b1;
//    @FXML
//    private Button b2;
//    @FXML
//    private Button b3;
//    @FXML
//    private Button b4;
//    @FXML
//    private Button b5;
//    @FXML
//    private Button b6;
//    @FXML
//    private Button b7;
//    @FXML
//    private Button b8;
//    
//    private OfflineSingleGameContext gameContext;
////    private TicTacToeGame ticTacToeGame;
////    private char myChar = 'x';
////    private char aiChar = 'o';
//    private volatile boolean humanTurn = true;
////    private AiStrategy aiStrategy;
//    Thread aiThread;
//    private volatile boolean gameRunning = true;
//    /**
//     * Initializes the controller class.
//     */
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // TODO
//        gameContext = new OfflineSingleGameContext(new HardTicTacToeAi('O') , new TicTacToeGame());  // -> will be change
////        aiStrategy = new EasyTicTacToeAi();
////        ticTacToeGame = new TicTacToeGame();
//        
//        aiThread = new Thread(this);
//        aiThread.start();
//    }    
//    
//    @FXML
//    private void handleMove(ActionEvent event) {
//        Button clickedButton = (Button) event.getSource();
//
//        System.out.println("Clicked: " + clickedButton.getId());
//        if(gameRunning){
//            switch(clickedButton.getId()){
//                case "b0":
//                    if(handleMoveHelper(0,0,'x'))
//                        b0.setText("X");
//                    break;
//                    
//                case "b1":
//                    if(handleMoveHelper(0,1,'x'))
//                        b1.setText("X");
//                    break;
//                    
//                case "b2":
//                   if(handleMoveHelper(0,2,'x'))
//                        b2.setText("X");
//                    break;
//                    
//                case "b3":
//                    if(handleMoveHelper(1,0,'x'))
//                        b3.setText("X");
//                    break;
//                    
//                case "b4":
//                    if(handleMoveHelper(1,1,'x'))
//                        b4.setText("X");
//                    break;
//                    
//                case "b5":
//                    if(handleMoveHelper(1,2,'x'))
//                        b5.setText("X");
//                    break;
//                    
//                case "b6":
//                    if(handleMoveHelper(2,0,'x'))
//                        b6.setText("X");
//                    break;
//                    
//                case "b7":
//                    if(handleMoveHelper(2,1,'x'))
//                        b7.setText("X");
//                    break;
//                    
//                case "b8":
//                    if(handleMoveHelper(2,2,'x'))
//                        b8.setText("X");
//                    break;
//                    
//            }
//            
//        }
//    }
//
//    public boolean handleMoveHelper(int row, int col , char c){
//        
//        if(((TicTacToeGame)gameContext.getGame()).isValidMove(new Cell(row,col))){
////            b1.setText("X");
//            ((TicTacToeGame)gameContext.getGame()).setGridCell(row,col,c);
//            if(((TicTacToeGame)gameContext.getGame()).isWinner()){
//                System.out.println("x wins");
//                gameRunning = false;
//            }
//            humanTurn = !humanTurn;
//            return true;
//        }
//        return false;
//    }
//    
//    private void setButton(int row, int col, char c){
//        if(row == 0 && col == 0)
//            b0.setText(c+"");
//        else if(row == 0 && col == 1)
//            b1.setText(c+"");
//        else if(row == 0 && col == 2)
//            b2.setText(c+"");
//        else if(row == 1 && col == 0)
//            b3.setText(c+"");
//        else if(row == 1 && col == 1)
//            b4.setText(c+"");
//        else if(row == 1 && col == 2)
//            b5.setText(c+"");
//        else if(row == 2 && col == 0)
//            b6.setText(c+"");
//        else if(row == 2 && col == 1)
//            b7.setText(c+"");
//        else if(row == 2 && col == 2)
//            b8.setText(c+"");
//    } 
//    
//    @Override
//    public void run() {
//        System.out.println("i entered the run");
//        while(gameRunning){
//            if(!humanTurn){
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    break;
//                }
//                System.out.println("the length = " + ((TicTacToeGame)gameContext.getGame()).emptyCells().length);
//                if(((TicTacToeGame)gameContext.getGame()).emptyCells().length == 0)
//                    break;
//                Cell c = gameContext.aiStrategy.AIMove(((TicTacToeGame)gameContext.getGame()));
//                
//                Platform.runLater(() -> {
//                    setButton(c.getRow(), c.getCol(), 'O');
//                });
//
//                
//                ((TicTacToeGame)gameContext.getGame()).setGridCell(c.getRow(),c.getCol(),'o');
//                if(((TicTacToeGame)gameContext.getGame()).isWinner()){
//                    System.out.println("o wins");
//                    gameRunning = false;
//                }
//                humanTurn = true;
//            } 
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                break;
//            }
//        }
//    }
//
//}
