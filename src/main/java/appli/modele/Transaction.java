package appli.modele;

/**
 * Représente une transaction unique “Membre vendeur → Membre acheteur”.
 * On stocke dans cet objet :
 *   le membre vendeur {@code chVendeur},
 *   le membre acheteur {@code chAcheteur},
 *   la ville du vendeur {@code chVilleVendeur},
 *   la ville de l’acheteur {@code chVilleAcheteur},
 *   la distance (en km) entre ces deux villes {@code chDistance}.
 * La distance est calculée dès la construction via
 * {@code chVilleVendeur.getChDistanceVille(chVilleAcheteur)}.
 */
public class Transaction {
    private Membre chVendeur;
    private Membre chAcheteur;
    private Ville   chVilleAcheteur;
    private Ville   chVilleVendeur;
    private int     chDistance;

    /**
     * Construit une Transaction entre deux membres.
     * Calcule automatiquement la distance (en km) entre la ville du vendeur
     * et la ville de l’acheteur via {@link Ville#getChDistanceVille(Ville)}.
     *
     * @param parVendeur  le {@link Membre} vendeur.
     * @param parAcheteur le {@link Membre} acheteur.
     */
    public Transaction(Membre parVendeur, Membre parAcheteur) {
        chVendeur       = parVendeur;
        chAcheteur      = parAcheteur;
        chVilleVendeur  = parVendeur.getChVille();
        chVilleAcheteur = parAcheteur.getChVille();
        chDistance      = chVilleVendeur.getChDistanceVille(chVilleAcheteur);
    }

    /**
     * Retourne la distance (en km) entre la ville du vendeur et celle de l’acheteur.
     *
     * @return un entier correspondant à la distance.
     */
    public int getChDistance() {
        return chDistance;
    }

    public Membre getChVendeur() {
        return chVendeur;
    }

    public Membre getChAcheteur() {
        return chAcheteur;
    }

    public Ville getChVilleAcheteur() {
        return chVilleAcheteur;
    }

    public Ville getChVilleVendeur() {
        return chVilleVendeur;
    }

    @Override
    public String toString() {
        return "Vendeur  : " + chVendeur.toString()  + "\n" +
                "Acheteur : " + chAcheteur.toString() + "\n" +
                "Distance  : " + chDistance;
    }
}
