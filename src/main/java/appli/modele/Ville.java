package appli.modele;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Représente une ville et permet d’accéder aux distances (en kilomètres)
 * qui la séparent de toutes les autres villes définies dans le fichier
 * {@code src/Données/distances.txt}.
 * Chaque ligne du fichier {@code distances.txt} a la forme :
 *   VilleX d12 d13 d14 ...
 * où {@code d12}, {@code d13}, etc. sont des entiers représentant les
 * distances depuis {@code VilleX} vers chacune des autres villes (dans
 * l’ordre d’apparition des villes dans le fichier).
 */
public class Ville {
    private String chNom;
    private int chIndex;
    private ArrayList<Integer> chDistanceVille;

    /**
     * Construit l’objet {@code Ville} pour le nom de ville spécifié.
     * Lit le fichier {@code data/distances.txt} pour construire :
     *   la liste {@code chDistanceVille} des distances (en km) depuis
     *       {@code parNom} vers toutes les autres villes,
     *   l’indice {@code chIndex} correspondant à la position de cette
     *       ville dans le fichier (0-based).
     *
     *
     * @param parNom le nom exact de la ville (doit figurer dans {@code distances.txt}).
     * @throws Exception si le fichier ne peut pas être lu ou si la ville
     *                   {@code parNom} n’y figure pas.
     */
    public Ville(String parNom) throws Exception {
        chNom = parNom;
        File distance = new File("data/distances.txt");
        Scanner scan = new Scanner(distance);

        Map<String, ArrayList<Integer>> DistanceVille = new LinkedHashMap<>();
        Map<String, Integer> indicesVille = new LinkedHashMap<>();
        chIndex = 0;
        while (scan.hasNextLine()) {
            String line = scan.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] split = line.split("\\s+");
            String ville = split[0];

            // 1) Enregistrement de l'indice
            indicesVille.put(ville, chIndex++);

            // 2) Lecture des distances
            ArrayList<Integer> listeDist = new ArrayList<>(split.length - 1);
            for (int i = 1; i < split.length; i++) {
                listeDist.add(Integer.parseInt(split[i]));
            }
            DistanceVille.put(ville, listeDist);
        }
        scan.close();
        chDistanceVille = DistanceVille.get(parNom);
        chIndex = indicesVille.get(parNom);
    }

    /**
     * Retourne le nom de cette ville.
     *
     * @return le nom (String) de la ville.
     */
    public String getChNom() {
        return chNom;
    }

    /**
     * Retourne l’index de cette ville tel qu’il apparaît dans
     * {distances.txt}.
     *
     * @return l’index (int) de la ville.
     */
    public int getChIndex() {
        return chIndex;
    }

    /**
     * Renvoie la distance entre cette ville et la ville donnée en paramètre.
     * Pour cela, on utilise {@code parVille.getChIndex()} pour récupérer
     * la position dans {@code chDistanceVille}. Si {@code parVille} est
     * {@code null} ou hors borne, retourne {@code 99999999}.
     *
     * @param parVille l’autre ville (objet {@link Ville}) dont on veut la distance.
     * @return la distance (int) en kilomètres, ou {@code 99999999}
     *         si {@code parVille} n’est pas valide.
     */
    public int getChDistanceVille(Ville parVille) {
        for (int i = 0; i < chDistanceVille.size(); i++) {
            if (chDistanceVille.get(i) == chDistanceVille.get(parVille.getChIndex())) {
                return chDistanceVille.get(i);
            }
        }
        return 99999999;
    }

    /**
     * Compares les noms des villes.
     * @param parVille l’autre ville (objet {@link Ville}) dont on veut la comparaison.
     *
     * @return  true (boolean) si la ville est la même false (boolean) sinon.
     */
    public boolean compareTo(Ville parVille) {
        if (chNom.compareTo(parVille.getChNom()) != 0) {
            return false;
        }
        return true;
    }

    /**
     * Retourne le nom de cette ville.
     *
     * @return le nom (String) de la ville.
     */
    public String toString() {
        return chNom;
    }

}