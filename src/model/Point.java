package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class Point extends Circle
{
    Point(double centerX, double centerY, double radius, Color color)
    {
        super(centerX, centerY, radius,color);
    }

    boolean setColor(Color color)
    {
        boolean groupNotChanged = true;
        if (this.getFill() != null && !this.getFill().equals(color)) groupNotChanged = false;
        this.setFill(color);
        return groupNotChanged;
    }
}
