package appli.modele;
import java.util.*;

/**
 * Modélise le graphe orienté correspondant au problème de livraisons
 * de cartes Pokémon pour l’APPLI.
 * Chaque transaction “VilleV → VilleA” du {@link Scenario} est traduite
 * en arcs :
 *   - VelizyV → VilleV
 *   - VilleV → VilleA
 *   - VilleA → VelizyA
 * On conserve :
 *   - {@code chSommets} : la liste de tous les noms de sommets
 *   - {@code chVoisinsSortant} : la table d’adjacence (TreeMap&lt;String,LinkedHashSet&lt;String&gt;&gt;)
 *   - {@code chDistance} : map (String → {@link Ville}) pour accéder à l’objet Ville
 *   - {@code chScenario} : le {@link Scenario} utilisé pour construire le graphe
 * Méthodes principales :
 *   - {@link #triTopologique()} : effectue un tri topologique simple
 *       et retourne l’ordre de parcours + distance totale
 *   - {@link #triDistance()} : variante « à la distance » : parmi les sources
 *       on choisit toujours la même “plus proche” du sommet précédent
 *   - {@link #meilleurschemins()} : énumère (jusqu’à 10 000 000) tous les chemins
 *       possibles au moyen d’une récursion
 *       topologique, et retourne les 5 plus courts.
 * </ul>
 */
public class    GrapheOriente {
    private TreeMap<String, LinkedHashSet<String>> chVoisinsSortant;
    private ArrayList<String> chSommets;
    private Map<String, Ville> chDistance;
    private Map<String, Integer> chDegreEntrant;
    private Scenario chScenario;

    /**
     * Construit le graphe orienté à partir du {@link Scenario} donné.
     *   Ajoute deux sommets spéciaux : “VelizyV” (point de départ, degré entrant 0)
     *       et “VelizyA” (sommet final, degré sortant 0).
     *   Pour chaque transaction (Membre vendeur → Membre acheteur) :
     *       - Récupère la ville du vendeur (ex. “Grenoble”) et crée le sommet “GrenobleV” si absent.
     *       - Récupère la ville de l’acheteur (ex. “Lyon”) et crée le sommet “LyonA” si absent.
     *       - Ajoute l’arc {@code VelizyV → “GrenobleV”}.
     *       - Ajoute l’arc {“GrenobleV” → “LyonA”}.
     *       - Ajoute l’arc {“LyonA” → “VelizyA”}.
     *   S’assure enfin que chaque sommet figure dans {@code chVoisinsSortant}
     *       (même si sa liste de successeurs est vide).
     * On instancie également {@code chDistance} pour chaque sommet :
     * la clé « “VilleV” » ou « “VilleA” » pointe vers l’objet {@link Ville} correspondant.
     *
     * @param parScenario le {@link Scenario} contenant les transactions
     * @throws Exception si un membre ou une ville n’est pas trouvé lors de la création
     */
    public GrapheOriente(Scenario parScenario) throws Exception {
        // 1) Initialisation des champs
        chScenario = parScenario;
        chVoisinsSortant = new TreeMap<>();
        chSommets = new ArrayList<>();
        chDistance = new LinkedHashMap<>();
        chDegreEntrant = new LinkedHashMap<>();

        // 2) A partir de Velizy
        chDistance.put("VelizyV", new Ville("Velizy"));
        chSommets.add("VelizyV");
        chDegreEntrant.put("VelizyV", 0);

        chDistance.put("VelizyA", new Ville("Velizy"));
        chSommets.add("VelizyA");
        chDegreEntrant.put("VelizyA", 0);

        // 3) Pour chaque transaction…
        for (Membre vendeur : parScenario.getTransactions().keySet()) {
            String vV = vendeur.getChVille() + "V";
            String vA = parScenario.getTransactions().get(vendeur).getChVille() + "A";

            // Ajout des sommets
            if (!chSommets.contains(vV)) {
                chSommets.add(vV);
                chDistance.put(vV, vendeur.getChVille());
            }
            if (!chSommets.contains(vA)) {
                chSommets.add(vA);
                chDistance.put(vA, parScenario.getTransactions().get(vendeur).getChVille());
            }

            // Dégrés entrants par défaut : on s’assure que chaque sommet existe dans la map
            chDegreEntrant.putIfAbsent(vV, 0);
            chDegreEntrant.putIfAbsent(vA, 0);

            // Arcs sortants, premier arc sortant : VelizyV → vV (on part toujours de Velizy)
            Set<String> s = chVoisinsSortant
                    .computeIfAbsent("VelizyV", k -> new LinkedHashSet<>());
            if (s.add(vV)) {
                chDegreEntrant.put(vV, chDegreEntrant.get(vV) + 1);
            }


            chVoisinsSortant.computeIfAbsent(vV, k -> new LinkedHashSet<>()).add(vA);
            chDegreEntrant.put(vA, chDegreEntrant.get(vA) + 1);

            Set<String> voisinsDeA = chVoisinsSortant
                    .computeIfAbsent(vA, k -> new LinkedHashSet<>());
            if (voisinsDeA.add("VelizyA")) {
                // on incrémente le degré entrant de VelizyA
                chDegreEntrant.put("VelizyA", chDegreEntrant.get("VelizyA") + 1);
            }


        }
        // garantie que chaque sommet a bien un ensemble dans la liste d'adjacence
        for (String sommet : chSommets) {
            chVoisinsSortant.computeIfAbsent(sommet, k -> new LinkedHashSet<>());
        }
    }

    /**
     * Reconstruit (à chaque appel) une nouvelle map des degrés entrants de chaque sommet,
     * sans modifier aucune variable interne.
     *   - Initialise tous les degrés à 0 pour chaque nom de sommet dans {@code chSommets}.
     *   - Parcourt chaque entrée {@code (u -> listeVoisins)} dans {@code chVoisinsSortant},
     *       et fait {@code degEnt[v]++} pour chaque voisin {@code v} de {@code u}.
     *
     * @return une {@code Map<String,Integer>} donnant, pour chaque sommet, son nombre d’arcs entrants.
     */
    public Map<String, Integer> copieChDegrésEntrants() {
        // On crée une nouvelle map qui copie chDegreEntrant pour ne pas la modifier
        Map<String, Integer> degEnt = new LinkedHashMap<>(chDegreEntrant);
        return degEnt;
    }

    /**Retournes le {@code chSommets} correspondant au graphe construit */
    public Scenario getChScenario() {
        return chScenario;
    }

    /**
     * Retourne l’ensemble des successeurs (voisins sortants) du sommet {@code parVille}.
     *
     * @param parVille le nom du sommet (ex. "VelizyV", "GrenobleV", "LyonA", …)
     * @return un {@code LinkedHashSet<String>} des voisins sortants
     */
    public LinkedHashSet<String> getChVoisinsSortant(String parVille) {
        return chVoisinsSortant.get(parVille);
    }

    /**
     * Effectue un tri topologique « classique » sur l’ensemble des sommets,
     * puis calcule la distance totale parcourue en additionnant chaque segment consécutif.
     * Le résultat est retourné sous la forme :
     *   Chemin : Velizy -> Ville1 -> Ville2 -> … -> Velizy
     *   Distance totale : XXX
     * Les doublons consécutifs (comme « Lyon » provenant de « LyonV » puis « LyonA ») sont fusionnés
     * à l’affichage pour n’apparaître qu’une fois.
     *
     * @return une {@code String} décrivant l’ordre de parcours et la distance totale.
     */
    public String triTopologique() {
        Map<String, Integer> degEnt = copieChDegrésEntrants();
        int distancetotal = 0;

        // 1) File des sources (degrés 0)
        TreeSet<String> sources = new TreeSet<>();
        for (var e : degEnt.entrySet()) {
            if (e.getValue() == 0) {
                sources.add(e.getKey());
            }
        }

        // 2) Tri Topologique
        List<String> ordre = new ArrayList<>();
        while (!sources.isEmpty()) {
            String s = sources.pollFirst();
            ordre.add(s);
            for (String v : this.getChVoisinsSortant(s)) {
                degEnt.put(v, degEnt.get(v) - 1);
                if (degEnt.get(v) == 0) {
                    sources.add(v);
                }
            }
        }

        for (int i = 0; i < ordre.size() - 1; i++) {
            String de = ordre.get(i);
            String vers = ordre.get(i + 1);
            Ville vDe = chDistance.get(de);
            Ville vVers = chDistance.get(vers);
            distancetotal += vDe.getChDistanceVille(vVers);
        }
        StringBuilder sb = new StringBuilder();
        String villePrécedente = "";

        // On parcourt l’ordre topologique
        for (int i = 0; i < ordre.size(); i++) {
            String nœud = ordre.get(i);
            // Récupère le nom de la ville sans le suffixe "V"/"A"
            String nomVille = nœud.substring(0, nœud.length() - 1);
            // On n’écrit la ville que si elle est différente de la précédente
            if (!nomVille.equals(villePrécedente)) {
                if (sb.length() > 0) {
                    sb.append(" -> ");
                }
                sb.append(nomVille);
                villePrécedente = nomVille;
            }
        }

        String chemin = sb.toString();


        return "Chemin : " + chemin + "\n" + "Distance totale :" + distancetotal;
    }

    /**
     * Variante du tri topologique qui, à chaque étape, choisit parmi les sources (degrés 0)
     * celle qui minimise la distance depuis le sommet précédent.
     * Le résultat est retourné sous la forme :
     *   Chemin : Velizy -> Ville1 -> Ville2 -> … -> Velizy
     *   Distance totale : XXX
     *
     * @return une {@code String} décrivant l’ordre « à la distance » et la distance totale.
     */
    public String triDistance() {
        Map<String, Integer> degEnt = copieChDegrésEntrants();
        int distancetotal = 0;

        // 1) File des sources (degrés 0)
        ArrayList<String> sources = new ArrayList<>();
        for (Map.Entry<String, Integer> sommet : degEnt.entrySet()) {
            if (sommet.getValue() == 0) {
                sources.add(sommet.getKey());
            }
        }

        // 2) Tri « à la distance »
        List<String> ordre = new ArrayList<>();
        while (!sources.isEmpty()) {
            // On prend en tête la première ville de la file
            String source = sources.remove(0);
            ordre.add(source);

            // On met à jour les degrés des successeurs de source
            for (String voisin : this.getChVoisinsSortant(source)) {
                degEnt.put(voisin, degEnt.get(voisin) - 1);
                if (degEnt.get(voisin) == 0) {
                    // => voisin devient une nouvelle source : on l’insère en fonction de la distance entre source et voisin
                    int dV = chDistance.get(source).getChDistanceVille(chDistance.get(voisin));
                    int insertPos = sources.size(); // par défaut, on le met en fin

                    // On cherche dans sources l’endroit où la distance source→sources.get(i) est >= dV
                    for (int i = 0; i < sources.size(); i++) {
                        String candidat = sources.get(i);
                        int dC = chDistance.get(source).getChDistanceVille(chDistance.get(candidat));
                        if (dV < dC) {
                            insertPos = i;
                            break;
                        }
                    }
                    sources.add(insertPos, voisin);
                }
            }
        }

        // 3) Calcul de la distance totale sur la liste 'ordre'
        for (int i = 0; i < ordre.size() - 1; i++) {
            String de = ordre.get(i);
            String vers = ordre.get(i + 1);
            Ville vDe = chDistance.get(de);
            Ville vVers = chDistance.get(vers);
            distancetotal += vDe.getChDistanceVille(vVers);
        }

        // 4) Construction de la chaîne “Chemin” sans doublons consécutifs
        StringBuilder sb = new StringBuilder();
        String villePrecedente = "";

        for (int i = 0; i < ordre.size(); i++) {
            String nœud = ordre.get(i);
            String nomVille;
            nomVille = nœud.substring(0, nœud.length() - 1);
            if (!nomVille.equals(villePrecedente)) {
                if (sb.length() > 0) {
                    sb.append(" -> ");
                }
                sb.append(nomVille);
                villePrecedente = nomVille;
            }
        }

        String chemin = sb.toString();
        return "Chemin : " + chemin + "\n" +
                "Distance totale : " + distancetotal;
    }



    public Ville getVille(String nomDuNoeud) {
        return chDistance.get(nomDuNoeud);
    }

    /**
     * Énumère (jusqu’à 5 000 000) tous les chemins possibles qui satisfont
     * l’ensemble des transactions , en ne gardant que les 5
     * plus courts (distance minimale).
     * Chaque parcours commence à “VelizyV”, visite toutes
     * les paires “VilleV→VilleA” dans un ordre valide, puis finit par “VelizyA”.
     * Le résultat est retourné sous forme de liste de 5 chaînes :
     *   [Velizy -> Ville1 -> … -> Velizy (XXX km),
     *    Velizy -> …,
     *    …]
     *
     * @return une {@code String} représentant la liste des 5 meilleurs parcours.
     */
    public String meilleurschemins() {
        // 1) On stocke au plus 5 meilleurs parcours (mais on pose une limite).
        List<List<String>> bestChemins = new ArrayList<>();
        List<Integer> bestDistances = new ArrayList<>();

        // 2) On fait une copie des degrés entrants
        Map<String, Integer> degEntInit = copieChDegrésEntrants();

        // 3) On construit l’ensemble initial des sources (degrés 0)
        ArrayList<String> sourcesInit = new ArrayList<>();
        for (var source : degEntInit.entrySet()) {
            if (source.getValue() == 0) {
                sourcesInit.add(source.getKey());
            }
        }

        // 4) Structures auxiliaires pour la récursion
        List<String> cheminCourant = new ArrayList<>();
        int[] compteTrouvés = new int[]{0}; // ce compteur arrêtera la récursion après X parcours

        // 5) Lancement de l’exploration récursive
        explorerTopologique(
                degEntInit,
                new ArrayList<>(sourcesInit),
                cheminCourant,
                0,                // distance accumulée au départ
                bestChemins,
                bestDistances,
                compteTrouvés
        );

        // 6) Formatage des meilleurs parcours trouvés (au plus k)
        List<String> resultatFormate = new ArrayList<>();
        int k = 5;
        for (int i = 0; i < k; i++) {
            List<String> chemin = bestChemins.get(i);
            int dist = bestDistances.get(i);
            StringBuilder sb = new StringBuilder();
            sb.append("Chemin : ");
            String villePrecedente = "";
            for (int j = 0; j < chemin.size(); j++) {
                String nœud = chemin.get(j);
                String nomVille;
                nomVille = nœud.substring(0, nœud.length() - 1);
                if (!nomVille.equals(villePrecedente)) {
                    if (sb.length() > 9) {
                        sb.append(" -> ");
                    }
                    sb.append(nomVille);
                    villePrecedente = nomVille;
                }
            }
            sb.append("\nDistance totale : ").append(dist);
            resultatFormate.add(sb.toString());
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= resultatFormate.size() - 1; i++) {
            result.append(resultatFormate.get(i)+"\n");
        }
        return result.toString();
    }

    /**
     * Exploration récursive “topologique” permettant d’énumérer jusqu’à 5 000 000
     * parcours complets, puis de conserver les 5 plus courts (distance min).
     * À chaque appel :
     *   - Si {@code cheminCourant.size() == chSommets.size()}, alors on a
     *       construit un parcours complet (dernier sommet = VelizyA) : on l’enregistre
     *       dans {@code bestChemins/bestDistances}, on incrémente {@code compteTrouvés},
     *       puis on fait un return (pas d’autres explorations sur cette branche).
     *   - Sinon, on parcourt chaque sommet {@code u} de {@code sourcesCourant}, on :
     *       - clone {@code degEntCourant} en {@code degEntNext}, et
     *           {@code sourcesCourant} en {@code sourcesNext},
     *       - retire {@code u} de {@code sourcesNext}, ajoute {@code u} à
     *           {@code cheminCourant}, calcule la distance partielle,
     *       - pour chaque successeur {@code v} de {@code u}, décrit par
     *           {@code getChVoisinsSortant(u)}, on fait {@code degEntNext.put(v, …–1)},
     *           et si cela tombe à zero on ajoute {@code v} à {@code sourcesNext},
     *       - appelle récursivement {@code explorerTopologique(degEntNext, sourcesNext, …)},
     *       - et enfin « backtrack » en retirant {@code u} de {@code cheminCourant} pour
     *           pouvoir réessayer un autre {@code u} au même niveau.
     *
     * @param degEntCourant  copies des degrés entrants à l’instant (Map<String,Integer>).
     * @param sourcesCourant l’ensemble des sommets de degré 0 disponibles (List<String>).
     * @param cheminCourant  la liste des sommets choisis dans l’ordre, pour la branche en cours.
     * @param distCourante   la distance cumulée depuis le départ (“VelizyV”) jusqu’au dernier sommet ajouté.
     * @param bestChemins    accumulateur des parcours complets (List<List<String>>) les 5 meilleurs.
     * @param bestDistances  accumulateur des distances correspondantes (List<Integer>), trié croissant.
     * @param compteTrouvés  compteur partagé (tableau int[0]) pour s’arrêter dès qu’on atteint 10 000 000 parcours complets.
     */
    private void explorerTopologique(
            Map<String,Integer> degEntCourant,
            List<String>        sourcesCourant,
            List<String>        cheminCourant,
            int                 distCourante,
            List<List<String>>  bestChemins,
            List<Integer>       bestDistances,
            int[]               compteTrouvés
    ) {
        // Interruption immédiate si on a déjà trouvé 5 000 000 parcours complets
        if (compteTrouvés[0] >= 5000000) {
            return;
        }

        // 1) Si on a visité tous les sommets, on est forcément sur "VelizyA". On enregistre.
        if (cheminCourant.size() == chSommets.size()) {
            // Copie du chemin courant
            List<String> copieChemin = new ArrayList<>(cheminCourant);

            // Insère trié par distance
            int idx = 0;
            while (idx < bestDistances.size() && bestDistances.get(idx) < distCourante) {
                idx++;
            }
            bestDistances.add(idx, distCourante);
            bestChemins.add(idx, copieChemin);
            if (bestDistances.size() > 5) {
                bestDistances.remove(5);
                bestChemins.remove(5);
            }

            // Incrémente le compteur de parcours complets trouvés
            compteTrouvés[0]++;
            return;
        }

        // 2) Sinon, pour chaque sommet u dans la liste actuelle des sources...
        //    On clone les états pour ne pas "polluer" l’appelant.
        List<String> sourcesClone = new ArrayList<>(sourcesCourant);
        for (String u : sourcesClone) {
            if (compteTrouvés[0] >= 5000000) {
                return; // on sort si la limite est atteinte
            }

            // A) Copier degEntCourant et sourcesCourant
            Map<String,Integer> degEntNext = new TreeMap<>(degEntCourant);
            List<String> sourcesNext = new ArrayList<>(sourcesCourant);

            // B) Retire 'u' de sourcesNext (on l’utilise)
            sourcesNext.remove(u);

            // C) Ajoute 'u' au chemin courant
            cheminCourant.add(u);

            // D) Calcule la distance du segment précédent→u
            int distAjout = 0;
            if (cheminCourant.size() > 1) {
                String precedent = cheminCourant.get(cheminCourant.size() - 2);
                Ville  vPrev   = chDistance.get(precedent);
                Ville  vU      = chDistance.get(u);
                distAjout = vPrev.getChDistanceVille(vU);
            }
            int distTotalIci = distCourante + distAjout;

            // E) Met à jour les degrés des successeurs de 'u'
            for (String v : getChVoisinsSortant(u)) {
                degEntNext.put(v, degEntNext.get(v) - 1);
                if (degEntNext.get(v) == 0) {
                    sourcesNext.add(v);
                }
            }

            // F) Appel récursif avec les nouvelles listes
            explorerTopologique(
                    degEntNext,
                    sourcesNext,
                    cheminCourant,
                    distTotalIci,
                    bestChemins,
                    bestDistances,
                    compteTrouvés
            );

            // G) Backtracking : retirer 'u' du chemin courant
            cheminCourant.remove(cheminCourant.size() - 1);
        }
    }



}