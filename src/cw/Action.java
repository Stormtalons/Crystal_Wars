package cw;

import java.util.ArrayList;

public class Action
{
	public static final int GET_PATH = 0, MOVE = 1;

	int action;
	ArrayList data;

	public Action(int a, Object...params)
	{
		action = a;
		data = new ArrayList();
		for (Object o : params)
			data.add(o);
	}
}