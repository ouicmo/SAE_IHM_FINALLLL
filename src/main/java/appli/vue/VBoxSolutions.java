package appli.vue;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class VBoxSolutions extends VBox {
    private Label chLabel;

    public VBoxSolutions() {
        super(30);
        chLabel = new Label("Choisissez un scénario pour afficher la solution");

        this.getChildren().add(chLabel);

    }

    public void setChLabel(String parLabel) {
        this.chLabel.setText(parLabel);
    }
}
