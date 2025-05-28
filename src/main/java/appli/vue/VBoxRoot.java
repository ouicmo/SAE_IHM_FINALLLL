package appli.vue;

import appli.controleur.Controleur;
import appli.modele.Membre;
import appli.modele.Scenario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class VBoxRoot extends VBox {
    private static Controleur chControleur;
    private static TableauPane chTableauPane;
    private static MenuDeroulant chMenuDeroulant;


    public VBoxRoot() {
        super(30);
        chControleur = new Controleur();
        chMenuDeroulant = new MenuDeroulant();
        chTableauPane = new TableauPane();

        Label labelTitle = new Label("Tableau des Transactions");
        VBox.setMargin(labelTitle, new Insets(10));


        HBox formBox = new HBox(10);
        formBox.setPadding(new Insets(10));



        this.getChildren().addAll(chMenuDeroulant, labelTitle, chTableauPane, formBox);



    }

    public static Controleur getControleur() {
        return chControleur;
    }

    public static MenuDeroulant getMenuDeroulant() {
        return chMenuDeroulant;
    }

    public static TableauPane getTableauPane() {
        return chTableauPane;
    }

    /*public TableauPane ajoutTableauPane(Scenario parScenario) {
        tableauPane.add(parScenario);
    }*/
}
