package appli.controleur;

import appli.modele.GrapheOriente;
import appli.modele.Scenario;
import appli.vue.VBoxRoot;
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
            GrapheOriente grapheduscen;
            try {
                grapheduscen = new GrapheOriente(scenario);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            VBoxRoot.getTableauPane().setTableauPane(grapheduscen);
            VBoxRoot.getVBoxSolutions().setChLabel(grapheduscen.meilleurschemins());

        }
    }

}
