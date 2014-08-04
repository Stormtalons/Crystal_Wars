package cw;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Player extends Label
{
    String name;
    SimpleIntegerProperty rock;
	Cell location;
	ConcurrentLinkedQueue<Action> actionList;

    public Player(String n)
    {
        super(n);
        setTextFill(Color.BLUE);
        name = n;
        rock = new SimpleIntegerProperty();
        rock.set(0);
		actionList = new ConcurrentLinkedQueue<>();
    }

	public void act(Action a)
	{
		actionList.clear();
		actionList.add(a);
	}

	public void queueAction(Action a)
	{
		actionList.add(a);
	}

	public Action getNextAction()
	{
		return actionList.peek();
	}

	public Action performAction()
	{
		return actionList.poll();
	}

	public void setLocation(Cell c)
	{
		location = c;
	}

	public Cell getLocation()
	{
		return location;
	}

    public void addRock(int amt)
	{
		rock.set(rock.get() + amt);
	}

    public boolean hasRock(int amt)
	{
		return rock.get() >= amt;
	}

    public int takeRock(int amt)
    {
        if (hasRock(amt))
        {
            rock.set(rock.get() - amt);
            return amt;
        }
        return 0;
    }
}
