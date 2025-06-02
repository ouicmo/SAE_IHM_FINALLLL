package appli.modele;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Représente un scénario de transactions “vendeur → acheteur” pour l’APPLI.
 * Chaque objet Scenario lit un fichier texte nommé {@code scenario_X.txt} dans le répertoire {@code data/},
 * où « X » va de 0 à 8. Le constructeur prend en paramètre une clef de la forme « s0 », « s1 », …, « s8 »,
 * et construit une map {@code chTransactions} dont la clé est un objet {@link Membre} (vendeur)
 * et la valeur est un autre {@link Membre} (acheteur).
 * Le fichier attendu pour « s0 » est {@code data/scenario_0.txt}, pour « s1 » c’est {@code data/scenario_1.txt}, etc.
 * Chaque ligne du fichier doit être au format :
 *   PseudonymeVendeur -> PseudonymeAcheteur
 * Exemple de contenu de {@code data/scenario_0.txt} :
 *   Psykokwak -> Démanta
 *   Machoc     -> Crocrodil
 *   Chapignon  -> Leuphorie
 *   Leuphorie  -> Ramoloss
 *   Écrapince  -> Minidraco
 * L’objet construit fournit ensuite :
 *   - {@link #getTransactions()} : la map complète {@code Map<Membre,Membre>} des transactions.
 *   - {@link #getVendeurs()}    : la liste {@code ArrayList<Membre>} de tous les vendeurs.
 *   - {@link #getAcheteurs()}   : la liste {@code ArrayList<Membre>} de tous les acheteurs.
 */
public class Scenario {
    private String chScenarioChoisi;
    private Map<Membre, Membre> chTransactions;

    /**
     * Construit un Scenario à partir d’une clef de la forme « s0 » … « s8 ».
     * Le constructeur crée une map {@code scenarios} qui associe :
     *   "s0" -> "scenario_0"
     *   "s1" -> "scenario_1"
     *   ...
     *   "s8" -> "scenario_8"
     * Puis lit le fichier {@code data/scenario_X.txt} correspondant.
     * Chaque ligne du fichier doit être au format « vendeur -> acheteur ».
     * Pour chaque ligne, on crée deux objets {@link Membre} :
     * un pour le vendeur et un pour l’acheteur, que l’on stocke dans la map {@code chTransactions}.
     *
     * @param parScenario la clef du scénario (“s0” à “s8”)
     * @throws Exception si le fichier n’existe pas, n’est pas lisible,
     *                   ou si un pseudonyme de membre est introuvable dans {@code membres_APPLI.txt}.
     */
    public Scenario(String parScenario) throws Exception {
        // Construire la table de correspondance “s0”→“scenario_0”, …, “s8”→“scenario_8”
        Map<String, String> scenarios = new LinkedHashMap<>();
        for (int i = 0; i < 9; i++) {
            scenarios.put("s" + i, "scenario_" + i);
        }
        chScenarioChoisi = parScenario;

        // Chemin attendu : data/scenario_X.txt
        File scenarioFile = new File("data/" + scenarios.get(chScenarioChoisi) + ".txt");
        Scanner scan3 = new Scanner(scenarioFile);

        chTransactions = new LinkedHashMap<>();

        // Chaque ligne “vendeur -> acheteur”
        while (scan3.hasNextLine()) {
            String line = scan3.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] split = line.split("\\s*->\\s*");
            if (split.length == 2) {
                Membre vendeur  = new Membre(split[0]);
                Membre acheteur = new Membre(split[1]);
                chTransactions.put(vendeur, acheteur);
            }
        }
        scan3.close();
    }

    /**
     * Retourne la map complète des transactions (vendeur → acheteur).
     *
     * @return {@code Map<Membre, Membre>} des transactions lues dans le fichier.
     */
    public Map<Membre, Membre> getTransactions() {
        return chTransactions;
    }

    /**
     * Retourne la liste des vendeurs (toutes les clés de {@link #getTransactions()}).
     *
     * @return {@code ArrayList<Membre>} contenant chaque membre vendeur.
     */
    public ArrayList<Membre> getVendeurs() {
        ArrayList<Membre> vendeurs = new ArrayList<>();
        for (Membre m : chTransactions.keySet()) {
            vendeurs.add(m);
        }
        return vendeurs;
    }

    /**
     * Retourne la liste des acheteurs (toutes les valeurs de {@link #getTransactions()}).
     *
     * @return {@code ArrayList<Membre>} contenant chaque membre acheteur.
     */
    public ArrayList<Membre> getAcheteurs() {
        ArrayList<Membre> acheteurs = new ArrayList<>();
        for (Membre m : chTransactions.keySet()) {
            acheteurs.add(chTransactions.get(m));
        }
        return acheteurs;
    }
}
