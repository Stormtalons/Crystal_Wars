package cw;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Predicate;

public class Main extends Application
{
    public static void main (String[] args) throws Exception {launch();}

    ScrollPane view;
    AnchorPane grid;
    static HashMap<String, Cell> cells;
    static int scale = 10;
    static Player p1;
    static Label p1Rock;

	static boolean run = true;
    Runnable gameLoop = () ->
    {
		while (run)
		{
			Action next = p1.getNextAction();

			try {Thread.sleep(1000/60);} catch (Exception e) {}
		}
    };

    public void start(Stage stg)
    {
        cells = new HashMap<>();
        grid = new AnchorPane();

        grid.setOnScroll(evt ->
        {
            if (evt.getDeltaY() < 0 && scale > 10)
            {
                scale -= 5;

                for (Cell c : cells.values())
                {
                    c.setPosition(scale);
                }
            }
            else
            if (evt.getDeltaY() > 0 && scale < 50)
            {
                scale += 5;

                for (Cell c : cells.values())
                {
                    c.setPosition(scale);
                }
            }
        });

        grid.setPrefWidth(1000);
        grid.setPrefHeight(1000);

        buildGrid();

        placePlayer();

        placeRocks();

        grid.getChildren().addAll(cells.values());

        view = new ScrollPane();
        view.setContent(grid);
        Scene sce = new Scene(view, 1600, 1250);
        stg.setScene(sce);
        stg.show();
    }

    public void buildGrid()
    {
        Cell root = new Cell(0, 0, 0, scale);
        addCell(root);
        spread(root, h -> h.z >= 0 && h.z < 30 && (h.x >= 0 || h.x >= h.y) && Math.abs(h.x) + Math.abs(h.y) < 70);
    }

    public void spread(Cell c, Predicate<HexCoord> eval)
    {
        for (HexCoord h : c.getAdjacent())
            if (eval.test(h) && cells.get(h.buildStr()) == null)
            {
                Cell nc = new Cell(h, scale);
                addCell(nc);
                spread(nc, eval);
            }
    }

    public void addCell(Cell c)
    {
        c.setOnMouseClicked(evt ->
        {
            if (evt.getButton().equals(MouseButton.PRIMARY))
            {
				p1.act(new Action(Action.GET_PATH, p1.getLocation(), c));
                movePlayer(p1.getLocation(), c);
            }
        });
        cells.put(c.buildStr(), c);
    }

    public ArrayList<Cell> getAdjacentCells(Cell c, Predicate<Cell> eval)
    {
        ArrayList<Cell> tr = new ArrayList<>();
        for (HexCoord h : c.getAdjacent())
            if (cells.get(h.buildStr()) != null && eval.test(cells.get(h.buildStr())))
                tr.add(cells.get(h.buildStr()));
        return tr;
    }

    public void movePlayer(Cell from, Cell to)
    {
        new Thread(() ->
        {
            Stack<Cell> path = getPath(from, to);
            if (path == null) return;
            while (!path.empty())
            {
                Cell c = path.pop();
                Platform.runLater(() ->
                {
                    p1.getLocation().getChildren().remove(p1);
                    c.getChildren().add(p1);
                    p1.setLocation(c);
                });
                try {Thread.sleep(100);} catch (Exception e) {}
            }
        }).start();
    }

    public Stack<Cell> getPath(Cell from, Cell to)
    {
        MovementPath path = new MovementPath(from, to);
        WHILE: while (!path.isComplete())
        {
            PathCell pc = path.getLowestCost();
            if (pc == null) return null;
            path.close(pc);
            ArrayList<Cell> adj = getAdjacentCells(pc, c -> (!path.isClosed(c) && (c.equals(to) || !c.hasRock())));
            for (Cell c : adj)
            {
                PathCell newpc = new PathCell(c, pc, to);
                if (c.is(to))
                {
                    path.close(newpc);
                    break WHILE;
                }
                path.open(newpc);
            }
        }

        return path.getPath();
    }

    public void placePlayer()
    {
        p1 = new Player("Andy");
        p1Rock = new Label("Rock: " + 0);
        AnchorPane.setLeftAnchor(p1Rock, 5.0);
        AnchorPane.setTopAnchor(p1Rock, 5.0);
        grid.getChildren().add(p1Rock);
        p1.rock.addListener(evt -> p1Rock.setText("Rock: " + p1.rock.get()));
        int i = (int)Math.floor(Math.random() * cells.size());
        p1.setLocation(cells.values().toArray(new Cell[0])[i]);
        p1.getLocation().getChildren().add(p1);
    }

    public void placeRocks()
    {
        for (Cell c : cells.values())
            if (Math.random() * 10 > 5 && !c.getChildren().contains(p1))
                c.getChildren().add(new Rock());
    }
}
