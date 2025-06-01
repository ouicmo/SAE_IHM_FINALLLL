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


    public Map<String, Integer> getDegreEntrant() {
        return chDegreEntrant;
    }

    public Scenario getChScenario(){
        return chScenario;
    }


    public LinkedHashSet<String> getChVoisinsSortant(String parVille) {
        return chVoisinsSortant.get(parVille);
    }

    public String triTopologique() {
        Map<String, Integer> degEnt = getDegreEntrant();
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
            String vers = ordre.get(i+1);
            Ville vDe  = chDistance.get(de);
            Ville vVers= chDistance.get(vers);
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
        Map<String, Integer> degEnt = getDegreEntrant();
        int distancetotal = 0;

        // 1) File des sources (degrés 0)
        ArrayList<String> sources = new ArrayList<>();
        for (var e : degEnt.entrySet()) {
            if (e.getValue() == 0) {
                sources.add(e.getKey());
            }
        }

        // 2) Tri
        List<String> ordre = new ArrayList<>();
        while (!sources.isEmpty()) {
            String s = sources.getFirst();
            ordre.add(s);
            for (String v : this.getChVoisinsSortant(s)) {
                degEnt.put(v, degEnt.get(v) - 1);
                if (degEnt.get(v) == 0) {
                    sources.add(v);
                }
            }
            int distance = 99999999;
            for (String source : sources){
                if (chDistance.get(s).getChDistanceVille(chDistance.get(source)) < distance) {
                    sources.remove(source);
                    sources.addFirst(source);
                }
            }
        }

        for (int i = 0; i < ordre.size() - 1; i++) {
            String de = ordre.get(i);
            String vers = ordre.get(i+1);
            Ville vDe  = chDistance.get(de);
            Ville vVers= chDistance.get(vers);
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
     */
    public List<String> meilleurschemins() {
        // bestChemins stocke jusqu’à 5 listes de nœuds (chacun est un chemin complet)
        List<List<String>> bestChemins   = new ArrayList<>();
        // bestDistances stocke la distance associée à chaque chemin de bestChemins
        List<Integer>      bestDistances = new ArrayList<>();

        // Structures auxiliaires pour la récursion
        List<String> cheminCourant = new ArrayList<>();
        Set<String>  visites       = new HashSet<>();

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
            sb.append(" (").append(dist).append(" km)");
            resultat.add(sb.toString());
        }
        return resultat;
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
     */
    private void explorer(
            String actuel,
            int distCourante,
            List<String> cheminCourant,
            List<List<String>> bestChemins,
            List<Integer> bestDistances
    ) {
        // 1) Ajout du nœud actuel
        cheminCourant.add(actuel);

        // 2) Condition d’enregistrement : on n’enregistre que si on est sur "VelizyA"
        //    ET si on a visité exactement toutes les villes (V + A)
        if (actuel.equals("VelizyA")) {
            // Copie du chemin courant
            List<String> copie = new ArrayList<>(cheminCourant);

            // Trouver l’indice d’insertion dans bestDistances (tri croissant)
            int idx = 0;
            while (idx < bestDistances.size() && bestDistances.get(idx) < distCourante) {
                idx++;
            }
            bestDistances.add(idx, distCourante);
            bestChemins.add(idx, copie);

            // Si plus de 5 chemins, on supprime le plus mauvais (dernière position)
            if (bestDistances.size() > 5) {
                bestDistances.remove(5);
                bestChemins.remove(5);
            }
        }
        else {
            // 3) Sinon, on explore chacun des voisins non-visités
            for (String voisin : getChVoisinsSortant(actuel)) {

                    Ville villeActuel = chDistance.get(actuel);
                    Ville villeVoisin = chDistance.get(voisin);
                    int d = 0;
                    if (villeActuel != null && villeVoisin != null) {
                        d = villeActuel.getChDistanceVille(villeVoisin);
                    }
                    explorer(voisin,
                            distCourante + d,
                            cheminCourant,
                            bestChemins,
                            bestDistances);
                }
            }


        // 4) Backtracking
        cheminCourant.remove(cheminCourant.size() - 1);
    }

}