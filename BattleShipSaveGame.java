/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author user
 */
public final class BattleShipSaveGame {
    public String fileName = "savegame.txt";
    public String name = "";
    public int gridSize = 10;
    public JButton[][] buttons = new JButton[gridSize][gridSize];
    public String[][] shipPos = new String[gridSize][gridSize];
    public java.util.List destroyedShips = new ArrayList();
    public java.util.List gridsFired = new ArrayList();
    public int chances = 10;
    public int points = 0;
    public boolean gameLoaded = false;
    
    public BattleShipSaveGame(int gridSize){
        this.gridSize = gridSize;
        loadGame();
    }
    
    public boolean loadGame(){
        BufferedReader br = null;
        try {
            br = new BufferedReader(readFile());
            String st;
            while ((st = br.readLine()) != null){
                this.gameLoaded = true;
                String[] keyVal = st.split("=");
                switch(keyVal[0]){
                    case "name":
                        this.name = keyVal[1];
                        this.gameLoaded = this.gameLoaded && true;
                        break;
                    case "chances":
                        this.chances = Integer.parseInt(keyVal[1]);
                        this.gameLoaded = this.gameLoaded && true;
                        break;
                    
                    case "points":
                        this.points = Integer.parseInt(keyVal[1]);
                        this.gameLoaded = this.gameLoaded && true;
                        break;
                    
                    case "shipPos":
                        String[] pos = keyVal[1].split(";");
                        for (int i = 0; i < pos.length; i++) {
                            String[] key = pos[i].split("-");
                            for (int j = 0; j < key.length; j++) {
                                key[0] = key[0].replace("(", "");
                                key[0] = key[0].replace(")", "");
                                String[] indexes = key[0].split(",");
                                shipPos[Integer.parseInt(indexes[0])][Integer.parseInt(indexes[1])] = key[1];
                            }
                        }
                        this.gameLoaded = this.gameLoaded && true;
                        break;
                    
                    case "destroyedPos":
                        if(keyVal.length > 1){
                            String[] desPos = keyVal[1].split(",");
                            for (int i = 0; i < desPos.length; i++) {
                                this.destroyedShips.add(desPos[i]);
                            }
                            this.gameLoaded = this.gameLoaded && true;
                        }
                        break;
                    
                    case "gridsFired":
                        if(keyVal.length > 1){
                            String[] firedPos = keyVal[1].split(",");
                            for (int i = 0; i < firedPos.length; i++) {
                                this.gridsFired.add(firedPos[i]);
                            }
                            this.gameLoaded = this.gameLoaded && true;
                        }
                        break;
                        
                    default:
                        this.gameLoaded = false;
                }
                this.gameLoaded = this.gameLoaded || false;
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(BattleShipSaveGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.gameLoaded;
    }
    
    public FileWriter getFile() throws IOException{
        try {
            return new FileWriter(fileName);
        } catch (IOException ex) {
            return new FileWriter(new File(fileName));
        }
    }
    
    public FileReader readFile() throws IOException{
        try {
            return new FileReader(fileName);
        } catch (IOException ex) {
            new FileWriter(new File(fileName));
            return new FileReader(fileName);
        }
    }
    
    public void saveGame(){
        try {
            FileWriter fw = getFile();
            BufferedWriter out = new BufferedWriter(fw);
            out.write("name=" + this.name);
            out.newLine();
            out.write("chances=" + this.chances);
            out.newLine();
            out.write("points=" + this.points);
            out.newLine();
            String pos = "";
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    pos += "("+ i + "," + j + ")-" + this.shipPos[i][j] + ";";
                }
            }
            out.write("shipPos=" + pos);
            out.newLine();
            String destroyed = "";
            for (int i = 0; i < this.destroyedShips.size(); i++) {
                destroyed += this.destroyedShips.get(i) + ",";
            }
            out.write("destroyedPos=" + destroyed);
            out.newLine();
            String fired = "";
            for (int i = 0; i < this.gridsFired.size(); i++) {
                fired += this.gridsFired.get(i) + ",";
            }
            out.write("gridsFired=" + fired);
            out.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    public void resetGame(){
        new File(fileName).delete();
    }
}
