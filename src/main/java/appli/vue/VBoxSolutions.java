package appli.vue;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class VBoxSolutions extends VBox {
    private Label chLabel;

    public VBoxSolutions() {
        super(30);
        chLabel = new Label("Test");

        this.getChildren().add(chLabel);

    }

    public void setChLabel(String parLabel) {
        this.chLabel.setText(parLabel);
    }
}
