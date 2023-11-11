/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipapp;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class BattleShipApp {
    public static int gridSize = 10;
    BattleShipSaveGame bsg = new BattleShipSaveGame(gridSize);
    JButton[][] buttons = bsg.buttons;
    String[][] shipPos = bsg.shipPos;
    int shipTypes[] = {5, 4, 3, 2, 1};
    java.util.List destroyedShips = bsg.destroyedShips;
    java.util.List gridsFired = bsg.gridsFired;
    int chances = bsg.chances;
    int points = bsg.points;
    BattleShipFrame bsf = new BattleShipFrame();
    BattleShipResults bsr = new BattleShipResults();
    JFrame frame;
    GridBagConstraints gbc = bsf.getGridBagConstraints();
    String name = bsg.name;
    JScrollPane scroll;
    String headers[] = { "Name", "Score" };
    ArrayList<User> list = new ArrayList<>();
    JTable  table = new JTable();
    DefaultTableModel model = new DefaultTableModel();
    static boolean load = false;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BattleShipApp bsa = new BattleShipApp();
        bsa.initFrame();
    }
    
    public void initFrame(){
        JFrame frame = new JFrame();
        frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
        JButton submit = new JButton("Enter Name");
        JButton loadGame = new JButton("Load Game");
        JTextField jtf = new JTextField(20);
        JLabel lb = new JLabel("");
        lb.setForeground(Color.red);
        frame.setLayout(new FlowLayout());
        frame.setSize(new Dimension(250, 350));
        frame.add(jtf);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(submit);
        frame.add(loadGame);
        frame.add(lb);
        frame.repaint();
        frame.setVisible(true);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = jtf.getText();
                if(temp.length() > 0){
                    setName(temp);
                    shipPos = new String[gridSize][gridSize];
                    buttons = new JButton[gridSize][gridSize];
                    destroyedShips = new ArrayList();
                    chances = bsg.chances;
                    points = 0;
                    lb.setText("");
                    frame.setVisible(false);
                    frame.dispose();
                    createGrid();
                }else{
                    lb.setText("Please enter a valid name");
                }
            }
        });
        
        loadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(bsg.loadGame()){
                    shipPos = bsg.shipPos;
                    destroyedShips = bsg.destroyedShips;
                    buttons = bsg.buttons;
                    name = bsg.name;
                    load = true;
                    frame.setVisible(false);
                    frame.dispose();
                    init();
                    createGrid();
                }else{
                    lb.setText("No game found");
                }
            }
        });
    }
    
    public void setName(String name){
        this.name = name;
        bsg.name = name;
    }
    
    public void init(){
        this.bsg = new BattleShipSaveGame(gridSize);
        this.buttons = bsg.buttons;
        this.shipPos = bsg.shipPos;
        this.destroyedShips = bsg.destroyedShips;
        this.chances = bsg.chances;
        this.points = bsg.points;
    }
    
    public static void sort(ArrayList<User> list){
        Collections.sort(list, (u1, u2) -> u2.getPoints().compareTo(u1.getPoints()));
    }
    
    public void createGrid(){
        bsg.name = name;
        frame = bsf.getFrame();
        GridLayout gr = new GridLayout(gridSize, gridSize);
        JPanel jp1 = new JPanel();
        jp1.setSize(700, 700);
        JPanel jp2 = new JPanel();
        jp2.setLayout(new BoxLayout(jp2, BoxLayout.Y_AXIS));
        JLabel lb = new JLabel("Welcome " + this.name);
        JLabel lb2 = new JLabel("Points Gained: " + points);
        JLabel lb3 = new JLabel("Chances Remaining: " + chances);
        lb.setFont(new Font(lb.getName(), Font.PLAIN, 30));
        lb.setAlignmentX(Component.CENTER_ALIGNMENT);
        lb2.setAlignmentX(Component.CENTER_ALIGNMENT);
        lb3.setAlignmentX(Component.CENTER_ALIGNMENT);
        jp2.add(lb);
        jp2.add(lb2);
        jp2.add(lb3);
        jp1.setLayout(gr);
        if(destroyedShips.size() == shipTypes.length || chances <= 0){
            bsg.resetGame();
            init();
        }
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setActionCommand(Integer.toString(i) + Integer.toString(j));
                buttons[i][j].addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String text = ((JButton) e.getSource()).getActionCommand();
                        System.out.println(text);
                        if(text.contains("Ship")){
                            int shipNumber = Integer.parseInt(text.substring(text.length() -1));
                            if(destroyedShips.contains(text)){
                                System.out.println("Ship has already been sinked");
                            }else{
                                destroyedShips.add(text);
                                System.out.println(text + " is destroyed");
                                switch(shipNumber){
                                    case 5:
                                        points += 2;
                                        break;
                                    case 4:
                                        points += 4;
                                        break;
                                    case 3:
                                        points += 6;
                                        break;
                                    case 2:
                                        points += 8;
                                        break;
                                    case 1:
                                        points += 10;
                                        break;
                                    default:
                                        points += 0;
                                }
                                lb2.setText("Points Gained: " + points);
                            }
                        }else{
                            if(gridsFired.contains(text)){
                                System.out.println("Square was already fired");
                            }else{
                                gridsFired.add(text);
                                chances--;
                                System.out.println("Missed");
                            }
                        }
                        lb3.setText("Chances Remaining: " + chances);
                        bsg.name = name;
                        bsg.chances = chances;
                        bsg.points = points;
                        bsg.destroyedShips = destroyedShips;
                        bsg.gridsFired = gridsFired;
                        bsg.saveGame();
                        if(destroyedShips.size() == shipTypes.length){
                            showResults(lb3, jp1, jp2);
                            return;
                        }
                        if(chances <= 0){
                            showResults(lb3, jp1, jp2);
                            return;
                        }
                    }
                    
                });
                jp1.add(buttons[i][j]);
            }
        }
        jp1.setVisible(true);
        jp2.setVisible(true);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        jp2.setSize(700, 100);
        frame.add(jp2, gbc);
        gbc.weighty = 1;
        gbc.gridy = 1;
        frame.add(jp1, gbc);
        frame.repaint();
        frame.setVisible(true);
        
        if(bsg.gameLoaded == true && load){
            shipPos = bsg.shipPos;
            loadShipPos();
        }else{
            setShipPositions();
        }
    }
    
    public void setShipPositions(){
        Random r = new Random();
        for (int i = 0; i < shipTypes.length; i++) {
            int randomX = r.nextInt(gridSize - shipTypes[i]);
            int randomY = r.nextInt(gridSize - shipTypes[i]);
            Boolean b = r.nextBoolean();
            Boolean is_valid = true;
            for(int j = 0; j < shipTypes[i]; j++){
                if(b){
                    //System.out.println("Horizontal");
                    if(randomY + shipTypes[i] > gridSize - 1){
                        is_valid = false;
                    }else{
                        if(shipPos[randomX][randomY + j] != null) setShipPositions();
                    }
                }else{
                    //System.out.println("Vertical");
                    if(randomX + shipTypes[i] > gridSize - 1){
                        is_valid = false;
                    }else{
                        if(shipPos[randomX + j][randomY] != null) setShipPositions();
                    }
                }
                
                if(j == shipTypes[i] -1 && is_valid){
                    for(int k = 0; k < shipTypes[i]; k++){
                        JButton tempBtn;
                        if(b){
                            tempBtn = buttons[randomX][randomY + k];
                            setUI(tempBtn, "Ship " + shipTypes[i]);
                            shipPos[randomX][randomY + k] = "Ship " + shipTypes[i];
                        }else{
                            tempBtn = buttons[randomX + k][randomY];
                            setUI(tempBtn, "Ship " + shipTypes[i]);
                            shipPos[randomX + k][randomY] = "Ship " + shipTypes[i];
                        }
                    }
                }
                
                if(!is_valid){
                    setShipPositions();
                }
            }
            shipTypes[i] = 0;
        }
        bsg.shipPos = shipPos;
        bsg.saveGame();
    }
    
    public void loadShipPos(){
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if(!"null".equals(shipPos[i][j])){
                    JButton tempBtn = buttons[i][j];
                    setUI(tempBtn, shipPos[i][j]);
                }
            }
        }
    }
    
    public void setUI(JButton jb, String shipText){
//        jb.setText(shipText);
//        jb.setBackground(Color.BLACK);
//        jb.setForeground(Color.WHITE);
        jb.setActionCommand(shipText);
    }
    
    public void showResults(JLabel lb3, JPanel jp1, JPanel jp2){
        try {
            bsg.resetGame();
            JLabel res = new JLabel("Ships Hit");
            res.setAlignmentX(Component.CENTER_ALIGNMENT);
            jp2.add(res);
            for (int i = 0; i < destroyedShips.size(); i++) {
                JLabel l = new JLabel(destroyedShips.get(i).toString());
                l.setAlignmentX(Component.CENTER_ALIGNMENT);
                jp2.add(l);
            }
            System.out.println("Out of chances");
            lb3.setText("Chances Remaining: " + chances);
            User u = new User(name, points);
            list = bsr.getUsers();
            list.add(u);
            sort(list);
            bsr.updateUsers(list);
            ArrayList<String> ar = new ArrayList<String>();
            model.setColumnIdentifiers(headers);
            table.setModel(model);
            scroll = new JScrollPane(table);
            for (int i = 0; i < list.size(); i++) {
                Object[] data = {list.get(i).name, list.get(i).points};
                model.addRow(data);
            }
            scroll.setSize(700, 700);
            scroll.setVisible(true);
            frame.remove(jp1);
            JPanel p = new JPanel(new GridLayout());
            p.setSize(700, 700);
            p.add(scroll, BorderLayout.CENTER);
            frame.add(p);
            frame.repaint();
            init();
            bsg.resetGame();
            return;
        } catch (IOException ex) {
            Logger.getLogger(BattleShipApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
