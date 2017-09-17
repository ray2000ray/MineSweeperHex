/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import handler.HexMinesSweeper;


public class HexMinesSweeperBoard extends MinesSweeperBoard
{
    float R = 0;
    float r = 0;
    Polygon etalonPoly;
    Polygon[][] polyFiled;

    public HexMinesSweeperBoard()
    {
        super();
        etalonPoly = new Polygon();
        game = new HexMinesSweeper();
    }

    @Override
    protected void setFont(Graphics2D g)
    {
        g.setFont(new Font("Comic Sans MS",Font.BOLD, (int)((r*2)/1.3f)));
    }

    @Override
    protected void setStroke(Graphics2D g)
    {
        g.setStroke(new BasicStroke((r*2)/10f));
    }

    @Override
    protected void DrawCell(Graphics2D g, int x, int y)
    {
        if(polyFiled == null)
            return;
        g.setColor(unknownC);
        if(x == select.x && y == select.y && game.IsUnknownCell(x, y))
            g.setColor(selectC);
        else if(game.IsKnownCell(x, y))
            g.setColor(knownC);
        if(game.IsFailCell(x, y))
            g.setColor(failC);
            
        g.fillPolygon(polyFiled[x][y]);
        g.setColor(Color.black);
        setStroke(g);
        g.drawPolygon(polyFiled[x][y]);
        Rectangle2D size = g.getFontMetrics().getStringBounds(game.GetCellMark(x, y), g);
        float fy = (float)(shift.getY()+(y*r*2)+r+size.getHeight()/3);;
        if(x%2 != 0)
            fy += r;
        float fx = (float)(shift.getX()+((x* 1.5 + 0.5)*R)+R/2-size.getWidth()/2);
        g.drawString(game.GetCellMark(x, y),fx,fy);
    }

    @Override
    protected void resized(ComponentEvent e)
    {
        R = 1;
        r = 1;
        int shiftPanel = 50;
        float wb = getWidth();
        float hb = getHeight()-shiftPanel;
        float wg = ((HexMinesSweeper)game).WidthInOuterRadius();
        float hg = ((HexMinesSweeper)game).HeightInOuterRadius();
        if(wb == 0 && hb == 0)
                return;
        shift.setLocation(0,shiftPanel);
        if(wb/hb > wg/hg)
        {
            r = hb/((HexMinesSweeper)game).HeightInInnerRadius();
            R = (float)((r*2)/Math.sqrt(3));
            shift.setLocation((wb-R*((HexMinesSweeper)game).WidthInOuterRadius())/2,shiftPanel);
        }
        if(wb/hb < wg/hg)
        {
            R = (float)(wb/((HexMinesSweeper)game).WidthInOuterRadius());
            r = (float)((Math.sqrt(3)*R)/2);
            shift.setLocation(0,(hb - r * ((HexMinesSweeper)game).HeightInInnerRadius())/2);
        }
        if(wb/hb == wg/hg)
        {
            R = (float)(wb/((HexMinesSweeper)game).WidthInOuterRadius());
            r = (float)((Math.sqrt(3)*R)/2);
        }
        CreateFiled();
    }

    @Override
    protected void selected(MouseEvent e)
    {
        select.setLocation(-1,-1);
        for(int x = 0;x<game.getWidth();x++)
            for(int y = 0;y<game.getHeight();y++)
                if(polyFiled[x][y].contains(e.getX(),e.getY()))
                    select.setLocation(x, y);
    }

    private void CreateFiled()
    {
        int sx = 0;
        int sy = 0;
        polyFiled = new Polygon[game.getWidth()][game.getHeight()];
        for(int x = 0;x<game.getWidth();x++)
            for(int y = 0;y<game.getHeight();y++)
            {
                polyFiled[x][y] = CreateEllipse(R, R,6); //new Polygon(xpoints, ypoints, y)
                sx = (int)(shift.getX()+R+(x*R*2)-(R/2*x));
                if(x%2==0)
                    sy = (int)(shift.getY()+r+(y*r*2));
                else
                    sy = (int)(shift.getY()+2*r+(y*r*2));
                polyFiled[x][y].translate(sx,sy);
            }
    }
    private Polygon CreateEllipse(float xRadius,float yRadius,int numberOfEdges)
    {
        Polygon vertices = new Polygon();
        float stepSize = (float)((Math.PI*2) / numberOfEdges);
        vertices.addPoint((int)xRadius, 0);
        for( int i = 0;i<numberOfEdges;i++)
            vertices.addPoint((int)(xRadius * Math.cos(stepSize * (i+1))),(int)( -yRadius * Math.sin(stepSize * (i+1))));
        return vertices;
    }
}

