package com.iti.crg.client.domain.game.managers;

import com.iti.crg.client.domain.entities.GameRecord;
import com.iti.crg.client.domain.entities.Move;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoadRecordManager {
 public static GameRecord loadFromStream(String fileName) {
        GameRecord record = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            record = (GameRecord) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.getLogger(LoadRecordManager.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return record;
    }


}
