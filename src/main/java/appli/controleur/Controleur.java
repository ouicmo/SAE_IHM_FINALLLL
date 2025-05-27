package appli.controleur;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

public class Controleur implements EventHandler {

    @Override
    public void handle(Event event) {
        if (event.getSource() instanceof MenuItem) {
            MenuItem item = (MenuItem) event.getSource();
        }
    }

}
