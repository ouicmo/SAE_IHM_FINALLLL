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
    private static VBoxSolutions chVBoxSolutions;


    public VBoxRoot() {
        super(30);
        chControleur = new Controleur();
        chMenuDeroulant = new MenuDeroulant();
        chTableauPane = new TableauPane();
        chVBoxSolutions = new VBoxSolutions();

        Label labelTitle = new Label("Tableau des Transactions");

        Label labelSolution = new Label("Solutions :");

        this.getChildren().addAll(chMenuDeroulant, labelTitle, chTableauPane,labelSolution, chVBoxSolutions);


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

    public static VBoxSolutions getVBoxSolutions() {return chVBoxSolutions; }

}
