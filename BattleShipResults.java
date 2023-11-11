/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipapp;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class BattleShipResults {
    public String fileName = "results.txt";
    
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
    
    public ArrayList<User> getUsers() throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(readFile());
        String st;
        ArrayList<User> list = new ArrayList<User>();
        while ((st = br.readLine()) != null){
            String[] keyVal = st.split("=");
            list.add(new User(keyVal[0], Integer.parseInt(keyVal[1])));
        }
        return list;
    }
    
    public void updateUsers(ArrayList<User> list){
        try {
            FileWriter fw = getFile();
            BufferedWriter out = new BufferedWriter(fw);
            for (int i = 0; i < list.size(); i++) {
                User u = list.get(i);
                out.write(u.name + "=" + u.points);
                if(i != list.size() - 1){
                    out.newLine();
                }
            }
            out.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
