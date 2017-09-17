package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import handler.OctMinesSweeper;
import view.MinesSweeperBoard;

public class NormalMinesSweeperBoard extends MinesSweeperBoard
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float a = 1;
    public NormalMinesSweeperBoard()
    {
        super();
        game = new OctMinesSweeper();
    }

    @Override
    protected void setFont(Graphics2D g)
    {
        g.setFont(new Font("Comic Sans MS",Font.BOLD, (int)(a/1.3f)));
    }

    @Override
    protected void DrawCell(Graphics2D g, int x, int y)
    {
        if(x == select.x && y == select.y && game.IsUnknownCell(x, y))
            g.setColor(selectC);
        else if(game.IsKnownCell(x, y))
            g.setColor(knownC);
        else if(game.IsFailCell(x, y))
            g.setColor(failC);
        else
            g.setColor(unknownC);
        g.fillRect((int)(shift.getX()+(x*a)),(int)(shift.getY()+(y*a)), (int)(a),(int)(a));
        g.setColor(Color.black);
        Rectangle2D size = g.getFontMetrics().getStringBounds(game.GetCellMark(x, y), g);
        g.drawString(game.GetCellMark(x, y),(float)(shift.getX()+(x*a)+a/2-size.getWidth()/2),(float)(shift.getY()+(y*a)+a/2+size.getHeight()/3));
    }

    @Override
    protected void resized(ComponentEvent e)
    {
        if(game != null)
        {
            a = 0;
            int shiftPanel = 50;
            float wb = getWidth();
            float hb = getHeight()-shiftPanel;
            float wg = game.getWidth();
            float hg = game.getHeight();

            if(wb == 0 && hb == 0)
                return;
            shift.setLocation(0,0);
            if(wb/hb > wg/hg)
            {
                a = (hb/hg);
                shift.setLocation((wb-(a*wg))/2,shiftPanel);
            }
            if(wb/hb < wg/hg)
            {
                a = (wb/wg);
                shift.setLocation(0,shiftPanel+(hb-(a*hg))/2);
            }
            if(wb/hb == wg/hg)
            {
                a = (wb/wg);
                shift.setLocation(0,shiftPanel);
            }
            repaint();
        }
    }

    @Override
    protected void selected(MouseEvent e)
    {
        select.setLocation(-1,-1);
        if((e.getX()>shift.getX()&&e.getX()<shift.getX()+(a*game.getWidth()))&&
                (e.getY()>shift.getY()&&e.getY()<shift.getY()+(a*game.getHeight())))
        {
            select.setLocation((int)((e.getX()-shift.getX())/a),(int)((e.getY()-shift.getY())/a));
        }
    }

    @Override
    protected void setStroke(Graphics2D g)
    {
        g.setStroke(new BasicStroke(a/10f));
    }

    @Override
    public void paint(Graphics g2d)
    {
        super.paint(g2d);
        for(int x = 0;x<game.getWidth()+1;x++)
                g2d.drawLine((int)(shift.getX() + x * a),(int)(shift.getY()),(int)(shift.getX() + x * a),(int)( shift.getY() + game.getHeight() * a));
            for(int y = 0;y<game.getHeight()+1;y++)
                g2d.drawLine((int)(shift.getX()),(int)( shift.getY() + y * a), (int)(shift.getX() + game.getWidth() * a), (int)(shift.getY() + y * a));
    }
    

}
