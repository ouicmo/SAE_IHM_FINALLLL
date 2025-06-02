package appli.vue;

import appli.controleur.Controleur;
import appli.modele.Scenario;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;

public class MenuDeroulant extends MenuBar {
    private static ArrayList<Scenario> chScenarios;

    public MenuDeroulant() {
        Menu menuFichiers = new Menu("Scenarios");

        for (int i = 0; i <= 8; i++) {
            MenuItem scenario = new MenuItem("Scénario " + i);
            scenario.setUserData("s" + i);

            scenario.setOnAction(VBoxRoot.getControleur());

            menuFichiers.getItems().add(scenario);

        }

        MenuItem newscenario = new MenuItem("Nouveau Scenario");

        this.getMenus().add(menuFichiers);
    }

}
