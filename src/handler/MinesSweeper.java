package handler;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import Utilities.MathHelper;
import Utilities.StopWatch;
import bean.GameState;

public abstract class MinesSweeper
{

    public final int MINE = -1;
    public final int ONE = 1;
    public final int TWO = 2;
    public final int THREE = 3;
    public final int FOUR = 4;
    public final int FIVE = 5;
    public final int SIX = 6;
    public final int SEVEN = 7;
    public final int EIGHT = 8;
    public final int MINEMARK = -2;
    public final int QUESTIONMARK = -3;
    public final int WRONGMINEMARK = -5;
    public final int UNKNOWN = -4;
    public final int ZERO = 0;
    public final int FAIL = -6;
    protected int width = 9;
    protected int height = 9;
    protected int mines = 10;
    protected int filed[][];
    protected int mask[][];
    private GameState state;
    protected Random rnd = new Random();
    protected StopWatch stopwatch;

    public MinesSweeper()
    {
        setUpMassives();
        stopwatch = new StopWatch();
    }
    private void setUpMassives()
    {
        this.filed = new int[width][height];
        this.mask = new int[width][height];
    }
    public void LoadGame(int width, int height, int mines)
    {
        if (width > 0)
            this.width = width;
        if (height > 0)
            this.height = height;
        if (mines > 0)
            this.mines = mines;
        setUpMassives();
        this.computeFiled();
        this.clearMask();
        this.state = GameState.WaitForStart;
        stopwatch.reset();
    }

    public void RestartGame()
    {
        this.clearMask();
        this.state = GameState.WaitForStart;
    }

    private void computeFiled()
    {
        clearFiled();
        int m = 0;
        int rx = 0;int ry = 0;
        while(m<mines)
        {
            rx = rnd.nextInt(width);
            ry= rnd.nextInt(height);
            if(filed[rx][ry] != MINE)
            {
                this.filed[rx][ry] = MINE;
                m+=1;
            }
        }
        int count = 0;
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                if (filed[x][y] != MINE)
                {
                    int[] points = GetSurroundValues(x, y);
                    count = 0;
                    for (int c : points)
                        if (c == MINE)
                            count++;
                    filed[x][y] = count;
                }
    }

    public int[] GetSurroundValues(int x, int y)
    {
        ArrayList<Point> points = GetSurroundCells(x, y);
        int[] p = new int[points.size()];
        for (int i = 0; i < points.size(); i++)
        {
            p[i] = filed[points.get(i).x][points.get(i).y];
        }
        return p;
    }

    public abstract ArrayList<Point> GetSurroundCells(int x, int y);

    public boolean InRange(int x, int y)
    {
        if (x >= 0 && x < width && y >= 0 && y < height)
            return true;
        return false;
    }

    private void clearMask()
    {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                mask[x][y] = UNKNOWN;
    }

    private void clearFiled()
    {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                filed[x][y] = UNKNOWN;
    }

    public void ShowBombs()
    {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                if (filed[x][y] == MINE && mask[x][y] != FAIL)
                    mask[x][y] = MINE;
                else if (mask[x][y] == MINEMARK)
                    mask[x][y] = WRONGMINEMARK;
    }

    private void openSquare(int x, int y)
    {
        if (filed[x][y] == ZERO && mask[x][y] != ZERO)
            ClearNullSquares(x, y);
        else
            mask[x][y] = filed[x][y];
    }

    public void ClearNullSquares(int x, int y)
    {
        if (filed[x][y] == ZERO && mask[x][y] != ZERO)
        {
            mask[x][y] = ZERO;
            ArrayList<Point> points = GetSurroundCells(x, y);
            for (Point a : points)
            {
                openSquare(a.x, a.y);
            }
        }
    }
    public double Time()
    {
        return MathHelper.round(stopwatch.getInSeconds(),2);
    }

    public void ClickIn(int x, int y)
    {
        if (InRange(x, y))
        {
            if (getState() != GameState.GameOver && getState() != GameState.GameWin)
            {
                if(getState() == GameState.WaitForStart)
                {
                    state = GameState.InGame;
                    stopwatch.start();
                }
                if (mask[x][y] == UNKNOWN || mask[x][y] == QUESTIONMARK)
                {
                    if (filed[x][y] == MINE)
                    {
                        mask[x][y] = FAIL;
                        FindWrongMineMarks();
                        state = GameState.GameOver;
                        stopwatch.stop();
                    }
                    else if (filed[x][y] == ZERO)
                        ClearNullSquares(x, y);
                    else
                        mask[x][y] = filed[x][y];
                }
                if (IsWin())
                {
                    state = GameState.GameWin;
                    stopwatch.stop();
                }
            }
        }
    }
    private void  FindWrongMineMarks()
    {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                if(mask[x][y] == MINEMARK && filed[x][y] != MINE)
                    mask[x][y] = WRONGMINEMARK;
    }
    public boolean IsKnownCell(int x, int y)
    {
        if (mask[x][y] >= ZERO)
            return true;
        else
            return false;
    }

    public boolean IsUnknownCell(int x, int y)
    {
        if (mask[x][y] < MINE && mask[x][y] >= WRONGMINEMARK)
            return true;
        else
            return false;
    }
    public boolean IsFailCell(int x, int y)
    {
        if (mask[x][y] == FAIL)
            return true;
        else
            return false;
    }
    public void MarkCell(int x, int y)
    {
        if (getState() != GameState.GameOver && getState() != GameState.GameWin)
        {
            if(getState() == GameState.WaitForStart)
            {
                state = GameState.InGame;
                stopwatch.start();
            }
            if (mask[x][y] == UNKNOWN)
            {
                mask[x][y] = MINEMARK;
                return;
            }
            if (mask[x][y] == MINEMARK)
            {
                mask[x][y] = QUESTIONMARK;
                return;

            }
            if (mask[x][y] == QUESTIONMARK)
            {
                mask[x][y] = UNKNOWN;
                return;
            }
        }
    }

    public String GetCellMark(int x, int y)
    {
        switch (mask[x][y])
        {
            case ONE:
            {
                return "1";
            }
            case TWO:
            {
                return "2";
            }
            case THREE:
            {
                return "3";
            }
            case FOUR:
            {
                return "4";
            }
            case FIVE:
            {
                return "5";
            }
            case SIX:
            {
                return "6";
            }
            case SEVEN:
            {
                return "7";
            }
            case EIGHT:
            {
                return "8";
            }
            case QUESTIONMARK:
            {
                return "?";
            }
            case MINEMARK:
            {
                return "!";
            }
            case MINE:
            {
                return "*";
            }
            case WRONGMINEMARK:
            {
                return "!?";
            }
            case FAIL:
            {
                return "*";
            }
            default:
            {
                return "";
            }
        }
    }

    public boolean IsWin()
    {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                if (mask[x][y] == UNKNOWN || mask[x][y] == QUESTIONMARK || mask[x][y] == MINEMARK)
                    if (filed[x][y] != MINE)
                        return false;

        return true;
    }
    public int GetMineMarks()
    {
        int c = 0;
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                if(mask[x][y] == MINEMARK)
                    c++;
        return c;
    }
    /**
     * @return the width
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * @return the mines
     */
    public int getMines()
    {
        return mines;
    }

    /**
     * @return the state
     */
    public GameState getState()
    {
        return state;
    }
}