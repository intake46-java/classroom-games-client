package com.iti.crg.client.domain.game.managers;

import com.iti.crg.client.domain.entities.Move;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoadRecordManager {
     public static Move[] loadFromStream(String fileName){
           Move[] moves = null;
          try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
          moves= (Move[]) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
             System.getLogger(LoadRecordManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
         }
          return moves;
     }

}
