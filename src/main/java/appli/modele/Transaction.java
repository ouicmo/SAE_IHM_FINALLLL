package appli.modele;

public class Transaction {
    private Membre chVendeur;
    private Membre chAcheteur;
    private Ville chVilleAcheteur;
    private Ville chVilleVendeur;

    public Transaction(Membre parVendeur,Membre parAcheteur) {
        chVendeur = parVendeur;
        chAcheteur = parAcheteur;
        chVilleVendeur = parVendeur.getChVille();
        chVilleAcheteur = parAcheteur.getChVille();
    }
}
