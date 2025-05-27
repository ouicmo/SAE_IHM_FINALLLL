module com.example.sae_ihm_finallll {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.sae_ihm_finallll to javafx.fxml;
    exports com.example.sae_ihm_finallll;
}