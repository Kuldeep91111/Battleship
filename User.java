/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipapp;

/**
 *
 * @author user
 */
public class User {
    String name;
    int points;
    
    public User(String name, int points){
        this.name = name;
        this.points = points;
    }
    
    public Integer getPoints(){
        return points;
    }
}
