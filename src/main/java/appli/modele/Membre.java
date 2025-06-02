package appli.modele;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Représente un membre de l’APPLI (vendeur ou acheteur).
 * Chaque membre possède un pseudonyme (String) et une {@link Ville}
 * associée qu’on lit depuis le fichier {@code src/Données/membres_APPLI.txt}.
 * Le fichier {@code membres_APPLI.txt} doit contenir, sur chaque ligne :
 *   Pseudonyme NomVille
 * Exemple : « Psykokwak Grenoble » signifiant que « Psykokwak » habite à
 * Grenoble.
 */
public class Membre {
    private String chNomMembre;
    private Ville chVille;

    /**
     * Construit un {@code Membre} à partir de son pseudonyme.
     * Lit le fichier {@code src/Données/membres_APPLI.txt} pour associer le membre à sa {@code Ville}. Si le pseudonyme
     * {@code parNomMembre} n’existe pas, lèvera une Exception.
     *
     * @param parNomMembre le pseudonyme du membre (String), clé dans le fichier.
     * @throws Exception si l’on ne peut pas lire le fichier ou si le membre est introuvable.
     */
    public Membre(String parNomMembre) throws Exception {
        chNomMembre = parNomMembre;
        File memberliste = new File("src/Données/membres_APPLI.txt");
        Scanner scan2 = new Scanner(memberliste);
        Map<String, String> memberdic = new LinkedHashMap<>();

        while (scan2.hasNextLine()) {
            String line = scan2.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] split = line.split("\\s+");
            if (split.length >= 2) {
                memberdic.put(split[0], split[1]);
            }
        }
        scan2.close();

        String nomVille = memberdic.get(parNomMembre);
        if (nomVille == null) {
            throw new IllegalArgumentException("Membre introuvable : " + parNomMembre);
        }
        chVille = new Ville(nomVille);
    }

    /**
     * Retourne le pseudonyme de ce membre.
     *
     * @return le pseudonyme (String).
     */
    public String getChNomMembre() {
        return chNomMembre;
    }

    /**
     * Retourne l’objet {@link Ville} correspondant à la ville du membre.
     *
     * @return la {@link Ville} où habite ce membre.
     */
    public Ville getChVille() {
        return chVille;
    }

    // Modifies la méthode equals pour permettre de comparer deuẋ membres uniquement avec leur pseudo
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Membre)) return false;
        Membre m = (Membre) o;
        return chNomMembre.equals(m.chNomMembre);
    }

    // Modifies le hashcode pour permettre de comparer deuẋ membres uniquement avec leur pseudo dans un TreeSet par exemple
    @Override
    public int hashCode() {
        return chNomMembre.hashCode();
    }

    @Override
    public String toString() {
        return chNomMembre;
    }
}
