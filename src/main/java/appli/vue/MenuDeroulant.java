package appli.vue;

import appli.controleur.Controleur;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuDeroulant extends MenuBar {

    public static MenuBar createMenuBar() {

        MenuBar menuBar = new MenuBar();


        Menu menuFichiers = new Menu("Scenarios");




        for (int i = 1; i <= 8; i++) {
            MenuItem scenario = new MenuItem("Scénario " + i);

            scenario.setOnAction(new Controleur()); {
                try {

                    scenario.setOnAction(VBoxRoot.getControleur());
                    System.out.println("Ajouter un scenario");




                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

            menuFichiers.getItems().add(scenario);

        }

        menuBar.getMenus().add(menuFichiers);
        return menuBar;






    }
}
