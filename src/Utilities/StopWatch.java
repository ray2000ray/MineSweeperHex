package Utilities;

public class StopWatch
{
    private long startTime;
    private long stopTime;
    private boolean isStarted;
    public StopWatch()
    {
        startTime = 0;
        stopTime = 0;
        isStarted = false;
    }
    public void start()
    {
        startTime = System.currentTimeMillis();
        stopTime = 0;
        isStarted = true;
    }
    public void stop()
    {
        stopTime = System.currentTimeMillis();
        isStarted = false;
    }
    public long getInMilliseconds()
    {
        long res = stopTime - startTime;
        if(res>0)
            return res;
        else if(isStarted)
            return (System.currentTimeMillis() - startTime);
        else
            return 0;
    }
    public double  getInSeconds()
    {
        long res = stopTime - startTime;
        if(res>0)
            return res*0.001;
        else if(isStarted)
            return (System.currentTimeMillis() - startTime)*0.001;
        else
            return 0;
    }

    public void reset()
    {
        startTime = 0;
        stopTime = 0;
        isStarted = false;
    }
}
