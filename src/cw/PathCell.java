package cw;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class PathCell extends Cell
{
    Label l;
    int movementCost;
    PathCell cameFrom;
    Cell parent;

    public PathCell(Cell p, PathCell cf, Cell target)
    {
        super(p.loc, p.scale);
        parent = p;
        cameFrom = cf;
        movementCost = (cf == null ? 0 : cf.movementCost) + getDistance(target);
        l = new Label();
        getChildren().add(l);
    }

    public void setLabel(String s)
    {
        Platform.runLater(() -> l.setText(s));
    }
}