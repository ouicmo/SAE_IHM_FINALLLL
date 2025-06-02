package appli.modele;
import java.util.*;

public class    GrapheOriente {
    private TreeMap<String, LinkedHashSet<String>> chVoisinsSortant;
    private ArrayList<String> chSommets;
    private Map<String, Ville> chDistance;
    private Map<String, Integer> chDegreEntrant;
    private Scenario chScenario;

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


    public Map<String, Integer> calculerDegrésEntrants() {
        // On crée une NOUVELLE map, initialisée à 0 pour tous les sommets
        TreeMap<String, Integer> degEnt = new TreeMap<>();
        for (String sommet : chSommets) {
            degEnt.put(sommet, 0);
        }
        // Pour chaque arc u→v, on fait degEnt[v]++
        for (Map.Entry<String, LinkedHashSet<String>> entry : chVoisinsSortant.entrySet()) {
            for (String v : entry.getValue()) {
                degEnt.put(v, degEnt.get(v) + 1);
            }
        }
        return degEnt;
    }


    public Scenario getChScenario() {
        return chScenario;
    }


    public LinkedHashSet<String> getChVoisinsSortant(String parVille) {
        return chVoisinsSortant.get(parVille);
    }

    public String triTopologique() {
        Map<String, Integer> degEnt = calculerDegrésEntrants();
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

    public String triDistance() {
        Map<String, Integer> degEnt = calculerDegrésEntrants();
        int distancetotal = 0;

        // 1) File des sources (degrés 0)
        ArrayList<String> sources = new ArrayList<>();
        for (var e : degEnt.entrySet()) {
            if (e.getValue() == 0) {
                sources.add(e.getKey());
            }
        }

        // 2) Tri « à la distance »
        List<String> ordre = new ArrayList<>();
        while (!sources.isEmpty()) {
            // On prend en tête la première ville de la file
            String s = sources.remove(0);
            ordre.add(s);

            // On met à jour les degrés des successeurs de s
            for (String v : this.getChVoisinsSortant(s)) {
                degEnt.put(v, degEnt.get(v) - 1);
                if (degEnt.get(v) == 0) {
                    // => v devient une nouvelle source : on l’insère en fonction de la distance s→v
                    int dV = chDistance.get(s).getChDistanceVille(chDistance.get(v));
                    int insertPos = sources.size(); // par défaut, on le met en fin

                    // Chercher dans 'sources' l’endroit où la distance s→sources.get(i) est >= dV
                    for (int i = 0; i < sources.size(); i++) {
                        String candidat = sources.get(i);
                        int dC = chDistance.get(s).getChDistanceVille(chDistance.get(candidat));
                        if (dV < dC) {
                            insertPos = i;
                            break;
                        }
                    }
                    sources.add(insertPos, v);
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
            if (nœud.equals("VelizyV") || nœud.equals("VelizyA")) {
                nomVille = "Velizy";
            } else {
                nomVille = nœud.substring(0, nœud.length() - 1);
            }
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


    /*public List<String> meilleurschemins(String parSource) {
        Map<String, Integer> degEnt = getDegreEntrant();
        int distancetotal = 0;

        // 2) Tri en arbre
        List<String> ordre = new ArrayList<>();

        String s = parSource;
        ordre.add(s);
        for (String v : this.getChVoisinsSortant(s)) {
            degEnt.put(v, degEnt.get(v) - 1);
            if (degEnt.get(v) == 0) {
                meilleurschemins(v);
                }
            }


        return ordre;
    }*/

    public Ville getVille(String nomDuNoeud) {
        return chDistance.get(nomDuNoeud);
    }

    /*
     Retourne les 5 meilleurs chemins (distance minimale)

    public String meilleurschemins() {
        // bestChemins stocke jusqu’à 5 chemins (chacun est un chemin complet)
        List<List<String>> bestChemins = new ArrayList<>();
        // bestDistances stocke la distance associée à chaque chemin de bestChemins
        List<Integer> bestDistances = new ArrayList<>();

        // Structures auxiliaires pour la récursion
        List<String> cheminCourant = new ArrayList<>();

        // On démarre la recherche depuis parSource (ex. "VelizyV")
        explorer(
                "VelizyV",
                0,
                cheminCourant,
                bestChemins,
                bestDistances
        );

        // Formatage final : on convertit chaque chemin en String "Ville1 -> Ville2 -> ... (NNN km)"
        List<String> resultat = new ArrayList<>();
        for (int i = 0; i < bestChemins.size(); i++) {
            List<String> chemin = bestChemins.get(i);
            int dist = bestDistances.get(i);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < chemin.size(); j++) {
                String n = chemin.get(j);
                if (n.equals("VelizyV") || n.equals("VelizyA")) {
                    sb.append("Velizy");
                } else {
                    sb.append(n.substring(0, n.length() - 1));
                }
                if (j < chemin.size() - 1) {
                    sb.append(" -> ");
                }
            }
            sb.append("\n").append(dist).append(" km");
            resultat.add(sb.toString());
        }
        return bestChemins.toString();
    }

    /*
     * Parcours récursif de l’arbre des chemins possibles.
     * On n’enregistre un chemin que si on arrive sur "VelizyA" ET
     * si cheminCourant.size() == chSommets.size() (visite de tous les nœuds).
     *
     * @param actuel         nœud courant (ex. "VelizyV", "LyonV", etc.)
     * @param distCourante   distance accumulée jusqu’à 'actuel'
     * @param cheminCourant  liste des nœuds visités jusqu’à présent
     * @param bestChemins    liste des k=5 meilleurs chemins (sous forme de listes de nœuds)
     * @param bestDistances  distances correspondantes à bestChemins

    private void explorer(
            String actuel,
            int distCourante,
            List<String> cheminCourant,
            List<List<String>> bestChemins,
            List<Integer> bestDistances
    ) {
        // 1) On ajoute le nœud courant
        cheminCourant.add(actuel);

        // 2) Seulement si on est arrivé sur "VelizyA" ET qu’on a TOUT parcourt tous les sommets :
        if (actuel.equals("VelizyA")) {
            // Enregistrer le chemin complet
            List<String> copie = new ArrayList<>(cheminCourant);
            int idx = 0;
            while (idx < bestDistances.size() && bestDistances.get(idx) < distCourante) {
                idx++;
            }
            bestDistances.add(idx, distCourante);
            bestChemins.add(idx, copie);
            if (bestDistances.size() > 5) {
                bestDistances.remove(5);
                bestChemins.remove(5);
            }
        } else {
            // 3) Tant qu’on n’est pas arrivé sur VelizyA + tous sommets non visités,
            //    on poursuit la récursion sur chaque voisin :
            for (String voisin : getChVoisinsSortant(actuel)) {
                // calcul de la distance actuel→voisin
                Ville villeActuel = chDistance.get(actuel);
                Ville villeVoisin = chDistance.get(voisin);
                int d = villeActuel.getChDistanceVille(villeVoisin);
                explorer(voisin,
                        distCourante + d,
                        cheminCourant,
                        bestChemins,
                        bestDistances);
            }
        }

// 4) Backtracking (on retire avant de revenir en arrière)
        cheminCourant.remove(cheminCourant.size() - 1);
    }*/

    /**
     * Exploration récursive pour énumérer toutes les extensions topologiques (Kahn à chaque étape),
     * en ne gardant que les 5 meilleurs parcours (distance minimale).
     *
     * @param degEntCourant   Map<String,Integer> : degrés entrants à cet instant
     * @param sourcesCourant  List<String>        : liste des sommets de degré 0 “disponibles”
     * @param cheminCourant   List<String>        : liste des nœuds sélectionnés jusqu’ici
     * @param distCourante    int                : distance cumulée jusqu’au dernier nœud ajouté
     * @param bestChemins     List<List<String>> : accumule jusqu’à 5 meilleurs parcours
     * @param bestDistances   List<Integer>      : accumule les distances correspondantes
     */
    private void explorerTopologique(
            Map<String,Integer> degEntCourant,
            List<String>        sourcesCourant,
            List<String>        cheminCourant,
            int                 distCourante,
            List<List<String>>  bestChemins,
            List<Integer>       bestDistances
    ) {
        // 1) Si on a vidé toutes les sources (et qu’on a parcouru TOUT chSommets),
        //    alors le dernier ajouté doit être “VelizyA” et cheminCourant.size() == chSommets.size().
        if (cheminCourant.size() == chSommets.size()) {
            // On vient de placer le dernier sommet (VelizyA), on enregistre donc
            // la copie du chemin dans bestChemins, triée par distCourante
            List<String> copieChemin = new ArrayList<>(cheminCourant);
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
            return;
        }

        // 2) Sinon, on choisit successivement chaque sommet “u” dans sourcesCourant :
        //    c’est un clone pour éviter de modifier directement l’original au cours de la boucle
        List<String> sourcesClone = new ArrayList<>(sourcesCourant);
        for (String u : sourcesClone) {
            // A) Préparer les structures pour cette itération :
            //    1. copier degEntCourant
            Map<String,Integer> degEntNext = new TreeMap<>(degEntCourant);
            //    2. copier les sources (on retire “u” puis on met à jour avec ses voisins)
            List<String> sourcesNext = new ArrayList<>(sourcesCourant);
            sourcesNext.remove(u);

            //    3. ajouter “u” au chemin courant
            cheminCourant.add(u);

            //    4. calculer la distance entre le précédent (s’il existe) et “u”
            int distAjout = 0;
            if (cheminCourant.size() > 1) {
                String precedent = cheminCourant.get(cheminCourant.size() - 2);
                Ville vPrev   = chDistance.get(precedent);
                Ville vU      = chDistance.get(u);
                distAjout = vPrev.getChDistanceVille(vU);
            }
            int distTotalIci = distCourante + distAjout;

            // B) Mettre à jour les degrés et sources en retirant “u”
            //    Pour chaque successeur “v” de u, on décrémente degEntNext[v]—
            //    si cela tombe à 0, on l’ajoute à sourcesNext.
            for (String v : getChVoisinsSortant(u)) {
                degEntNext.put(v, degEntNext.get(v) - 1);
                if (degEntNext.get(v) == 0) {
                    sourcesNext.add(v);
                }
            }

            // C) Appel récursif avec ces nouvelles cartes / listes
            explorerTopologique(
                    degEntNext,
                    sourcesNext,
                    cheminCourant,
                    distTotalIci,
                    bestChemins,
                    bestDistances
            );

            // D) Backtracking : on retire “u” du chemin pour revenir à l’état précédent
            cheminCourant.remove(cheminCourant.size() - 1);
        }
    }

    /**
     * Retourne les 5 meilleurs parcours (distance minimale),
     * en énumérant toutes les séquences topologiques possibles
     * (en choisissant à chaque étape parmi les sommets de degré entrant 0).
     */
    public String meilleurschemins() {
        // 1) Préparer les structures “globales” pour collecter les 5 meilleurs résultats
        List<List<String>> bestChemins   = new ArrayList<>();
        List<Integer>      bestDistances = new ArrayList<>();

        // 2) Calculer une copie “fraîche” des degrés entrants pour ne pas muter chDegreEntrant
        Map<String,Integer> degEntInit = calculerDegrésEntrants();

        // 3) Construire l’ensemble initial des sources (degré 0)
        //    (ordre alphabétique pour fixer un ordre, on pourrait customiser)
        TreeSet<String> sourcesInit = new TreeSet<>();
        for (var entry : degEntInit.entrySet()) {
            if (entry.getValue() == 0) {
                sourcesInit.add(entry.getKey());
            }
        }

        // 4) Structures auxiliaires pour la récursion “topologique”
        List<String> cheminCourant = new ArrayList<>();
        // (on ne garde pas de Set de “visites” car le fait d’enlever de sources empêche
        //  naturellement la revisite – on n’explore qu’un sommet que lorsque degEnt[u]==0)

        // 5) Lancement de l’exploration récursive
        explorerTopologique(
                degEntInit,                     // copie des degrés entrants à chaque appel
                new ArrayList<>(sourcesInit),   // on travaille sur une ArrayList pour pouvoir indexer/remplacer facilement
                cheminCourant,
                0,                              // distance cumulée au départ
                bestChemins,
                bestDistances
        );

        // 6) Formater les 5 meilleurs parcours sous forme de String “Velizy -> … (X km)”
        List<String> resultatFormate = new ArrayList<>();
        for (int i = 0; i < bestChemins.size(); i++) {
            List<String> chemin = bestChemins.get(i);
            int dist = bestDistances.get(i);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < chemin.size(); j++) {
                String n = chemin.get(j);
                if (n.equals("VelizyV") || n.equals("VelizyA")) {
                    sb.append("Velizy");
                } else {
                    sb.append(n.substring(0, n.length() - 1)); // enlève le “V” ou “A”
                }
                if (j < chemin.size() - 1) {
                    sb.append(" -> ");
                }
            }
            sb.append(" (").append(dist).append(" km)");
            resultatFormate.add(sb.toString());
        }

        return resultatFormate.toString();

    }


}