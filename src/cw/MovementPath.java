package cw;

import java.util.ArrayList;
import java.util.Stack;

public class MovementPath
{
    Cell from, to;
    ArrayList<PathCell> openList;
    ArrayList<PathCell> closedList;

    public MovementPath(Cell f, Cell t)
    {
        from = f;
        to = t;
        openList = new ArrayList<>();
        openList.add(new PathCell(from, null, to));
        closedList = new ArrayList<>();
    }

    public PathCell getLowestCost()
    {
        if (openList.size() == 0) return null;
        PathCell tr = openList.get(0);
        for (PathCell pc : openList)
            if (pc.movementCost <= tr.movementCost)
                tr = pc;
        return tr;
    }

    public void open(PathCell pc)
    {
        if (isClosed(pc)) return;
        for (int i = 0; i < openList.size(); ++i)
            if (openList.get(i).is(pc))
            {
                if (pc.movementCost < openList.get(i).movementCost)
                    openList.set(i, pc);
                return;
            }
        openList.add(pc);
    }

    public boolean isOpen(Cell c)
    {
        for (Cell arc : openList)
            if (arc.is(c))
                return true;
        return false;
    }

    public void close(PathCell pc)
    {
        if (isClosed(pc)) return;
        closedList.add(pc);
        if (!isOpen(pc)) return;
        for (int i = 0; i < openList.size(); ++i)
            if (openList.get(i).is(pc))
            {
                openList.remove(i);
                break;
            }
    }

    public boolean isClosed(Cell c)
    {
        for (Cell arc : closedList)
            if (arc.is(c))
                return true;
        return false;
    }

    public boolean isComplete()
    {
        return closedList.size() == 0 ? false : closedList.get(closedList.size() - 1).is(to);
    }

    public Stack<Cell> getPath()
    {
        if (!isComplete()) return null;
        Stack<Cell> toReturn = new Stack<>();
        PathCell current = closedList.get(closedList.size() - 1);
        while (!current.is(from))
        {
            toReturn.push(current.parent);
            current = current.cameFrom;
        }
        return toReturn;
    }
}
