package appli.modele;

public class Transaction {
    private Membre chVendeur;
    private Membre chAcheteur;
    private Ville chVilleAcheteur;
    private Ville chVilleVendeur;
    private int chDistance;


    public Transaction(Membre parVendeur,Membre parAcheteur) {
        chVendeur = parVendeur;
        chAcheteur = parAcheteur;
        chVilleVendeur = parVendeur.getChVille();
        chVilleAcheteur = parAcheteur.getChVille();
        chDistance = chVilleVendeur.getChDistanceVille(chVilleAcheteur);
    }

    public Membre getChVendeur() {
        return chVendeur;
    }

    public Membre getChAcheteur() {
        return chAcheteur;
    }

    public Ville getChVilleVendeur() {
        return chVilleVendeur;
    }

    public Ville getChVilleAcheteur() {
        return chVilleAcheteur;
    }

    public int getChDistance() {
        return chDistance;
    }

    public String toString() {
        return "Vendeur : " + chVendeur.toString() + "\nAcheteur : " + chAcheteur.toString() + "\nDistance : " + chDistance;
    }

    public static void main(String[] a) throws Exception {
        Transaction ta = new Transaction(new Membre("Bulbizarre"),new Membre("Salamèche"));
        System.out.println(ta);
    }
}
