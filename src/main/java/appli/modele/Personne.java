package appli.modele;

import javafx.beans.property.SimpleStringProperty;

public class Personne {
    private final SimpleStringProperty acheteur;
    private final SimpleStringProperty vendeur;


    public Personne(String acheteur, String vendeur) {
        this.acheteur = new SimpleStringProperty(acheteur);
        this.vendeur = new SimpleStringProperty(vendeur);
    }


    public String getAcheteur() {
        return acheteur.get();
    }


    public void setAcheteur(String acheteur) {
        this.acheteur.set(acheteur);
    }


    public String getVendeur() {
        return vendeur.get();
    }


    public void setVendeur(String vendeur) {
        this.vendeur.set(vendeur);
    }


    public SimpleStringProperty acheteurProperty() {
        return acheteur;
    }

    public SimpleStringProperty vendeurProperty() {
        return vendeur;
    }
}
