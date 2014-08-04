package cw;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;

public class Cell extends StackPane
{
    HexCoord loc;
    int scale;

    public Cell(HexCoord h, int s)
    {
        loc = h;
        scale = s;
        setStyle("-fx-background-color: gainsboro");
        setPosition(scale);
    }

    public Cell(int a, int b, int c, int s)
    {
        loc = new HexCoord(a, b, c);
        scale = s;
        setStyle("-fx-background-color: gainsboro");
        setPosition(scale);
    }

    public void setPosition(int scale)
    {
        setPrefSize(scale * 2, scale * 2);
        String shape = "M" + (scale + scale * Math.cos(2 * 3.14 / 6 * 0.5)) + "," + (scale + scale * Math.sin(2 * 3.14 / 6 * 0.5));
        for (int i = 1; i < 6; ++i)
        {
            double angle = 2 * 3.14 / 6 * (i + 0.5);
            double x = scale + scale * Math.cos(angle);
            double y = scale + scale * Math.sin(angle);
            shape += " L" + x + "," + y;
        }
        SVGPath shp = new SVGPath();
        shp.setContent(shape);
        setClip(shp);
        double yos = 50;
        double xos = 50;
        yos += scale * loc.x;
        yos += scale * loc.y;
        yos += 2.5 * scale * loc.z;
        xos += (scale - (Math.pow(scale, 0.91) / 5)) * loc.x;
        xos -= (scale - (Math.pow(scale, 0.91) / 5)) * loc.y;
        AnchorPane.setLeftAnchor(this, xos);
        AnchorPane.setTopAnchor(this, yos);
    }

    public String buildStr()
    {
        return loc.buildStr();
    }

    public HexCoord[] getAdjacent()
    {
        return loc.adjacent();
    }

    public int getDistance(Cell c)
    {
        return (Math.max(loc.x, c.loc.x) - Math.min(loc.x, c.loc.x)) + (Math.max(loc.y, c.loc.y) - Math.min(loc.y, c.loc.y)) + (Math.max(loc.z, c.loc.z) - Math.min(loc.z, c.loc.z));
    }

    public int mine(String type)
    {
        for (Node n : getChildren())
            if (type.equals("Rock") && n instanceof Rock)
            {
                int mined = ((Rock)n).mine();
                getChildren().remove(n);
                return mined;
            }
        return 0;
    }

    public boolean hasRock()
    {
        for (Node n : getChildren())
            if (n instanceof Rock)
                return true;
        return false;
    }

    public String toString()
    {
        return "[X=" + loc.x + " Y=" + loc.y + " Z=" + loc.z + "]";
    }

    public boolean is(Cell c)
    {
        return loc.is(c.loc);
    }
}
