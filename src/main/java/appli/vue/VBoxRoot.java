package appli.vue;

import appli.controleur.Controleur;
import appli.modele.Scenario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import appli.modele.Personne;

import java.util.ArrayList;

public class VBoxRoot extends VBox {

    private static Controleur chControleur;
    private TableauPane tableauPane;
    private MenuDeroulant chMenuDeroulant;
    private ArrayList<Scenario> chScenarios;


    private final ObservableList<Personne> donnees = FXCollections.observableArrayList();

    public VBoxRoot() {
        super(30);
        chControleur = new Controleur();
        chMenuDeroulant = new MenuDeroulant();
        chScenarios = new ArrayList<>();

        Label labelTitle = new Label("Tableau des Transactions");
        VBox.setMargin(labelTitle, new Insets(14));

        MenuBar menuBar = MenuDeroulant.createMenuBar();


        TableView<Personne> table = new TableView<>();
        table.setItems(donnees);


        TableColumn<Personne, String> colAcheteur = new TableColumn<>("Acheteur");
        colAcheteur.setCellValueFactory(new PropertyValueFactory<>("acheteur"));

        TableColumn<Personne, String> colVendeur = new TableColumn<>("Vendeur");
        colVendeur.setCellValueFactory(new PropertyValueFactory<>("vendeur"));

        table.getColumns().addAll(colAcheteur, colVendeur);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Champs dee saisie
        TextField acheteurField = new TextField();
        acheteurField.setPromptText("Acheteur");

        TextField vendeurField = new TextField();
        vendeurField.setPromptText("Vendeur");

        //Buton Ajouter
        Button btnAjouter = new Button("Ajouter");
        btnAjouter.setOnAction(e -> {
            String acheteur = acheteurField.getText().trim();
            String vendeur = vendeurField.getText().trim();

            if (!acheteur.isEmpty() && !vendeur.isEmpty()) {
                donnees.add(new Personne(acheteur, vendeur));
                acheteurField.clear();
                vendeurField.clear();
            }
        });


        HBox formBox = new HBox(10, acheteurField, vendeurField, btnAjouter);
        formBox.setPadding(new Insets(10));

        TableauPane tabbb = new TableauPane();



        this.getChildren().addAll(menuBar, labelTitle, table, formBox, tabbb);



    }

    public static Controleur getControleur() {
        return chControleur;
    }

    /*public TableauPane ajoutTableauPane(Scenario parScenario) {
        tableauPane.add(parScenario);
    }*/
}
