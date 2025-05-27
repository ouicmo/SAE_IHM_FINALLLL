package appli.controleur;

import appli.modele.Scenario;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

public class Controleur implements EventHandler {

    @Override
    public void handle(Event event) {
        if (event.getSource() instanceof MenuItem) {
            MenuItem item = (MenuItem) event.getSource();
            Scenario scenario = null;
            try {
                scenario = new Scenario((String) item.getUserData());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println(item.getUserData() +" : "+ scenario.getTransactions());

        }
    }

}
