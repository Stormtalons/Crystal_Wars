package cw;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class Rock extends StackPane
{
    Label txt;
    int val;

    public Rock()
    {
        setPrefSize(500, 500);
        setStyle("-fx-background-color: gray");
        getChildren().add(txt = new Label((val = (int)(Math.random() * 5) + 1) + ""));
    }

    public int mine()
    {
        int tr = val;
        val = 0;
        txt.setText(val + "");
        return tr;
    }
}
