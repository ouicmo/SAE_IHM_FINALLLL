package appli.vue;

import appli.modele.GrapheOriente;
import appli.modele.Membre;
import appli.modele.Scenario;
import appli.modele.Ville;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Map;

public class TableauPane extends TableView<String> {
    private ArrayList<Membre> listeMembre;

    public TableauPane() {
        TableColumn<Membre,Membre> colTrans = new TableColumn<>("Transactions");
        colTrans.setCellValueFactory(new PropertyValueFactory<>("transactions"));

        TableColumn<Ville,String> colVilles = new TableColumn<>("Villes des transactions");
        colVilles.setCellValueFactory(new PropertyValueFactory<>("villes"));

        this.getColumns().addAll(colTrans, colVilles);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    public void setTableauPane(GrapheOriente parGrapheOriente) {
        VBoxRoot.getTableauPane().getItems().clear();
        Scenario scenariochoisi = parGrapheOriente.getChScenario();
        Map<Membre, Membre> transactionsduscen = scenariochoisi.getTransactions();
        for (Membre membre : transactionsduscen.values()) {
            //this.getItems().add(membre);
        }
    }
}
