package appli.vue;

import appli.modele.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Map;

public class TableauPane extends TableView<Transaction> {
    private ArrayList<Membre> listeMembre;

    public TableauPane() {
        TableColumn<Transaction,Membre> colVendeur = new TableColumn<>("Vendeur");
        colVendeur.setCellValueFactory(new PropertyValueFactory<>("chVendeur"));

        TableColumn<Transaction,Ville> colVilleVendeur = new TableColumn<>("Ville Vendeur");
        colVilleVendeur.setCellValueFactory(new PropertyValueFactory<>("chVilleVendeur"));

        TableColumn<Transaction,Membre> colAcheteur = new TableColumn<>("Acheteur");
        colAcheteur.setCellValueFactory(new PropertyValueFactory<>("chAcheteur"));

        TableColumn<Transaction,Ville> colVilleAcheteur = new TableColumn<>("Ville Acheteur");
        colVilleAcheteur.setCellValueFactory(new PropertyValueFactory<>("chVilleAcheteur"));

        TableColumn<Transaction,Integer> colDistance = new TableColumn<>("Distance");
        colDistance.setCellValueFactory(new PropertyValueFactory<>("chDistance"));

        this.getColumns().addAll(colVendeur,colVilleVendeur,colAcheteur, colVilleAcheteur, colDistance);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    public void setTableauPane(GrapheOriente parGrapheOriente) {
        VBoxRoot.getTableauPane().getItems().clear();
        Scenario scenariochoisi = parGrapheOriente.getChScenario();
        Map<Membre, Membre> transactionsduscen = scenariochoisi.getTransactions();
        System.out.println(transactionsduscen);
        for (Membre vendeur : transactionsduscen.keySet()) {
            Membre acheteur = transactionsduscen.get(vendeur);
            Transaction transaction = new Transaction(vendeur, acheteur);
            System.out.println(transaction);
            this.getItems().add(transaction);
        }
    }
}
