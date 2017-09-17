package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import bean.GameState;
import handler.MinesSweeper;

public abstract class MinesSweeperBoard extends JPanel implements Runnable
{
    protected Thread animator;
    protected final int DELAY = 30;
    
    MinesSweeper game = null;
    protected  Point2D shift;
    protected  Point select;
    protected Color knownC = new Color(0, 191, 255);
    protected Color unknownC = new Color(24,116,205);
    protected Color selectC = new Color(0, 0, 150);
    protected Color failC = new Color(100, 0, 0);
    protected Color backC = new Color(0, 0, 0);
    public MinesSweeperBoard()
    {
        setBackground(backC);
        setDoubleBuffered(true);
        shift = new Point();
        select = new Point();
        addComponentListener(new cAdapter());
        addMouseMotionListener(new mmAdapter());
        addMouseListener(new mAdapter());
    }
    
    public MinesSweeper GetGame()
    {
        return game;
    }
   
    @Override
    public void paint(Graphics g2d)
    {
        super.paint(g2d);
        Graphics2D g = (Graphics2D) g2d;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        if(game != null)
        {
            setFont(g);
            for(int x = 0;x<game.getWidth();x++)
                for(int y = 0;y<game.getHeight();y++)
                    DrawCell(g, x, y);
            g.setColor(backC);
            
            g.setFont(new Font("Comic Sans MS",Font.BOLD, 36));
            g.setColor(Color.white);
            g.drawString(Double.toString(game.Time()),10,40);
            if(game.getState()== GameState.GameOver)
                g.drawString("Game over!",getWidth()/2-g.getFontMetrics().stringWidth("Game over!")/2,40);
            if(game.getState()== GameState.GameWin)
                g.drawString("Win!",getWidth()/2-g.getFontMetrics().stringWidth("Win!")/2,40);
            g.drawString(game.GetMineMarks()+"/"+game.getMines(),getWidth()-g.getFontMetrics().stringWidth(game.GetMineMarks()+"/"+game.getMines())-10,40);

            setStroke(g);
            
        }

        Toolkit.getDefaultToolkit().sync();
    }
    protected abstract void setFont(Graphics2D g);
    protected abstract void setStroke(Graphics2D g);
    protected abstract void DrawCell(Graphics2D g,int x,int y);
    
    @Override
    public void addNotify()
    {
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }

    public void cycle()
    {

    }

    public void run()
    {
        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true)
        {
            cycle();
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0)
                sleep = 2;
            try
            {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }

            beforeTime = System.currentTimeMillis();
        }
    }
    protected abstract void resized(ComponentEvent e);

    class cAdapter extends ComponentAdapter
    {

        @Override
        public void componentResized(ComponentEvent e)
        {
            super.componentResized(e);
            if(game != null)
            {
                resized(e);
            }
        }
    }
    protected abstract void selected(MouseEvent e);
    class mmAdapter extends MouseMotionAdapter
    {

        @Override
        public void mouseMoved(MouseEvent e)
        {
            super.mouseMoved(e);
            if(game != null)
            {
                selected(e);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
            super.mouseDragged(e);
            if(game != null)
            {
                selected(e);
            }
        }


    }
    class mAdapter extends MouseAdapter
    {

        @Override
        public void mouseReleased(MouseEvent e)
        {
            super.mouseReleased(e);
            if(game != null)
            {
                selected(e);
                if(e.getButton() == e.BUTTON1)
                {
                    game.ClickIn(select.x,select.y);
                    if(game.getState() == GameState.GameOver)
                        game.ShowBombs();
                }
                if(e.getButton() == e.BUTTON3)
                    game.MarkCell(select.x,select.y);
            }
        }

    }
}

