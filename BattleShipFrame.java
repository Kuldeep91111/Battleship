/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipapp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;

/**
 *
 * @author user
 */
public class BattleShipFrame {
    GridBagConstraints gbc = new GridBagConstraints();
    JFrame jFrame = new JFrame();
    
    public BattleShipFrame(){
        setConstraints();
    }

    
    public void setConstraints(){
        gbc.weightx = 1;
        gbc.gridheight = 1;
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(700, 800);
        jFrame.setResizable(false);
        jFrame.setLayout(new GridBagLayout());
    }
    
    public JFrame getFrame(){
        return jFrame;
    }
    
    public GridBagConstraints getGridBagConstraints(){
        return gbc;
    }
}
