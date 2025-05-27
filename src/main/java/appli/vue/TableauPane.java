package appli.vue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import appli.modele.Personne;
import appli.modele.Scenario;

public class TableauPane extends GridPane {

    private final ObservableList<Personne> donnees = FXCollections.observableArrayList();

    public TableauPane() {
        Label labelTitle = new Label("Tableau des echanges");

        VBox.setMargin(labelTitle, new Insets(14));

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
    }

    public void add(Scenario parScenario) {

    }
}
