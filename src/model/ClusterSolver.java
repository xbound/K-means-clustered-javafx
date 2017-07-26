package model;

import javafx.scene.paint.Color;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class ClusterSolver extends Thread
{
    private Point[] points,clusters;
    private DistanceFunction distFunction;
    private static Map<FunctionType,DistanceFunction> functions = new HashMap<>();
    private static Color[] colors =
            {
                 Color.BLUE,Color.RED,Color.GREEN,Color.ORANGE,Color.PURPLE,Color.YELLOW,Color.DEEPSKYBLUE,Color.CRIMSON,
                    Color.HOTPINK,Color.LAWNGREEN,Color.MAGENTA,Color.YELLOWGREEN,Color.THISTLE,Color.WHEAT,Color.GOLDENROD
            };
    private boolean running;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public ClusterSolver()
    {
        functions.put(FunctionType.EUCLID, (x1, y1, x2, y2) -> Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
        functions.put(FunctionType.MANHATTAN, (x1, y1, x2, y2) -> Math.abs(x1 - x2) + Math.abs(y1 - y2));
        functions.put(FunctionType.CHEBYSHEV,((x1, y1, x2, y2) -> Math.max(Math.abs(x1-x2),Math.abs(y1-y2))));
    }

    public Point[] getPoints()
    {
        return points;
    }

    public Point[] getClusters()
    {
        return clusters;
    }

    public synchronized boolean isRunning()
    {
        return running;
    }
    public synchronized void setRunning(boolean running)
    {
        this.running = running;
        support.firePropertyChange("running",!this.running,running);
    }

    public void setDistanceFunction(FunctionType distFunction)
    {
        this.distFunction = functions.get(distFunction);
    }

    public void generatePoints(int amount, double width, double height, double radius)
    {
        if (this.points == null) this.points = new Point[amount];
        for (int i = 0; i < amount; i++)
        {
            double x = (width - 1.00) * Math.random();
            double y = (height - 1.00) * Math.random();
            this.points[i] = new Point(x,y,radius,Color.WHITE);
        }
    }

    public void generateClusters(int amount, double width, double height, double radius)
    {
        if (this.clusters == null) this.clusters = new Point[amount];
        for (int i = 0; i < amount; i++)
        {
            double x = (width - 1.00) * Math.random();
            double y = (height - 1.00) * Math.random();
            this.clusters[i] = new Point(x,y,radius,colors[i]);
        }
    }

    public boolean assignPointsToClusters()
    {
        boolean groupNotChanged = true;
        for(Point p : points)
        {
            double minDistance = distFunction.func(p.getCenterX(),p.getCenterY(),clusters[0].getCenterX(),clusters[0].getCenterY());;
            Color group = (Color) clusters[0].getFill();
            for (int idx = 1; idx < clusters.length; idx++)
            {
                double distance = distFunction.func(p.getCenterX(),p.getCenterY(),clusters[idx].getCenterX(),clusters[idx].getCenterY());
                if (distance < minDistance)
                {
                    minDistance = distance;
                    group = (Color) clusters[idx].getFill();
                }
            }
            boolean colorNotChanged = p.setColor(group);
            groupNotChanged = groupNotChanged && colorNotChanged;
        }
        return groupNotChanged;
    }

    public void recalculateClustersPosition()
    {
        for (Point c:clusters)
        {
            double avgX = Arrays.stream(points).filter(p-> p.getFill().equals(c.getFill()))
                    .mapToDouble(Point::getCenterX).summaryStatistics().getAverage();
            double avgY = Arrays.stream(points).filter(p-> p.getFill().equals(c.getFill()))
                    .mapToDouble(Point::getCenterY).summaryStatistics().getAverage();
            c.setStroke(Color.BLACK);
            c.setCenterX(avgX);
            c.setCenterY(avgY);
        }
    }

    public void onStartEnd(PropertyChangeListener listener)
    {
        support.addPropertyChangeListener("running",listener);
    }

    @Override
    public void run()
    {
        while (isRunning())
        {
            if (!assignPointsToClusters())
            {
                recalculateClustersPosition();
            }
            else setRunning(false);
        }
    }

    @FunctionalInterface
    private interface DistanceFunction {double func(double x1, double y1, double x2, double y2);}

    public enum FunctionType {EUCLID,MANHATTAN,CHEBYSHEV}
}
