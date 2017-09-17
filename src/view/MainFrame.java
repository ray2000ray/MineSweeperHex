package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;



public class MainFrame extends JFrame implements ActionListener
{
    JMenuBar MenuBar;
    JMenu GameMenu;
    JMenuItem newGameItem;
    JMenuItem resetGameItem;
    JMenuItem exitItem;
    JMenu FiledMenu;
    JMenu filedSizeMenu;
    JMenu filedTypeMenu;
    JMenuItem noviceItem;
    JMenuItem amateurItem;
    JMenuItem expertItem;
    JMenuItem octFiledItem;
    JMenuItem hexFiledItem;
    MinesSweeperBoard board;
    int w = 9;
    int h = 9;
    int b = 10;
    public MainFrame()
    {
        super();
        MenuBar = new JMenuBar();
            GameMenu = new JMenu("Menu");
            FiledMenu = new JMenu("Setting");
                newGameItem = new JMenuItem("New game");
                resetGameItem = new JMenuItem("Reset");
                exitItem = new JMenuItem("Exit");
                newGameItem.addActionListener(this);
                resetGameItem.addActionListener(this);
                exitItem.addActionListener(this);

                filedSizeMenu = new JMenu("Size");
                filedTypeMenu = new JMenu("Mode");
                    noviceItem = new JMenuItem("Easy 9x9 10 mines");
                    amateurItem = new JMenuItem("Normal 16x16 40 mines");
                    expertItem = new JMenuItem("Hard 16x30 99 mines");
                    noviceItem.addActionListener(this);
                    amateurItem.addActionListener(this);
                    expertItem.addActionListener(this);
                filedSizeMenu.add(noviceItem);
                filedSizeMenu.add(amateurItem);
                filedSizeMenu.add(expertItem);
                    octFiledItem = new JMenuItem("Nomal");
                    octFiledItem.addActionListener(this);
                    hexFiledItem = new JMenuItem("hexagonal");
                    hexFiledItem.addActionListener(this);
                filedTypeMenu.add(octFiledItem);
                filedTypeMenu.add(hexFiledItem);
        GameMenu.add(newGameItem);
        GameMenu.add(resetGameItem);
        GameMenu.add(exitItem);
        FiledMenu.add(filedSizeMenu);
        FiledMenu.add(filedTypeMenu);
        MenuBar.add(GameMenu);
        MenuBar.add(FiledMenu);
        setJMenuBar(MenuBar);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(500,500);
        board = new NormalMinesSweeperBoard();
        getContentPane().add(board);
        board.setSize(getSize());
        board.GetGame().LoadGame(w,h,b);
        board.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if(source == exitItem)
            System.exit(0);
        if(source == newGameItem)
            board.GetGame().LoadGame(w,h,b);
        if(source == resetGameItem)
            board.GetGame().RestartGame();
        if(source == noviceItem)
        {
            w = 9; h = 9;b = 10;
            board.GetGame().LoadGame(w, h,b);
            board.resized(null);
        }
        if(source == amateurItem)
        {
            w = 16; h = 16;b = 40;
            board.GetGame().LoadGame(w, h,b);
            board.resized(null);
        }
        if(source == expertItem)
        {
            w = 30; h = 16;b = 99;
            board.GetGame().LoadGame(w, h,b);
            board.resized(null);
        }
        if(source == octFiledItem)
        {
            board = new NormalMinesSweeperBoard();
            getContentPane().removeAll();
            getContentPane().add(board);
            board.setSize(getContentPane().getSize());
            board.GetGame().LoadGame(w,h,b);
        }
        if(source == hexFiledItem)
        {
            board = new HexMinesSweeperBoard();
            getContentPane().removeAll();
            getContentPane().add(board);
            board.setSize(getContentPane().getSize());
            board.GetGame().LoadGame(w,h,b);
        }
    }
}
