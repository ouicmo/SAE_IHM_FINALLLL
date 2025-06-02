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
        // On crée une nouvelle map, initialisée à 0 pour tous les sommets
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



    public Ville getVille(String nomDuNoeud) {
        return chDistance.get(nomDuNoeud);
    }

    /**
     * Retourne, sous forme de String, jusqu’à 1 000 parcours complets (topologiques)
     * de distance minimale, en énumérant (partiellement) toutes les extensions topologiques
     * mais en s’arrêtant dès qu’on a accumulé 1 000 solutions complètes.
     */
    public String meilleurschemins() {
        // 1) On stocke au plus 5 meilleurs parcours (mais on pose une limite).
        List<List<String>> bestChemins   = new ArrayList<>();
        List<Integer>      bestDistances = new ArrayList<>();

        // 2) Calculer une copie “fraîche” des degrés entrants pour ne pas muter chDegreEntrant
        Map<String,Integer> degEntInit = calculerDegrésEntrants();

        // 3) Construire l’ensemble initial des sources (degrés 0)
        TreeSet<String> sourcesInit = new TreeSet<>();
        for (var entry : degEntInit.entrySet()) {
            if (entry.getValue() == 0) {
                sourcesInit.add(entry.getKey());
            }
        }

        // 4) Structures auxiliaires pour la récursion
        List<String> cheminCourant = new ArrayList<>();
        int[]        compteTrouvés = new int[]{0}; // ce compteur arrêtera la récursion après 1000 parcours

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

        // 6) Formatage des meilleurs parcours trouvés (au plus 5)
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
                    sb.append(n.substring(0, n.length() - 1));
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

    /**
     * Exploration récursive “topologique” qui s’arrête dès que l’on a accumulé 1000 parcours complets.
     *
     * @param degEntCourant  Degrés entrants restants pour chaque sommet
     * @param sourcesCourant Liste des sommets de degré 0 “disponibles” à cet instant
     * @param cheminCourant  Chemin construit jusqu’à présent (liste de nœuds)
     * @param distCourante   Distance accumulée jusqu’au dernier sommet ajouté
     * @param bestChemins    Accumulateur des (jusqu’à 5) meilleurs parcours complets rencontrés
     * @param bestDistances  Accumulateur des distances correspondantes
     * @param compteTrouvés  Compteur (tableau de taille 1) des parcours complets déjà trouvés
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
        // Interruption immédiate si on a déjà trouvé 1000 parcours complets
        if (compteTrouvés[0] >= 100000000) {
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
        //    On clone les états pour ne pas polluer l’appelant.
        List<String> sourcesClone = new ArrayList<>(sourcesCourant);
        for (String u : sourcesClone) {
            if (compteTrouvés[0] >= 100000000) {
                return; // on sort si la limite est atteinte
            }

            // A) Copier degEntCourant et sourcesCourant
            Map<String,Integer> degEntNext = new TreeMap<>(degEntCourant);
            List<String>        sourcesNext = new ArrayList<>(sourcesCourant);

            // B) Retirer 'u' de sourcesNext (on l’utilise)
            sourcesNext.remove(u);

            // C) Ajouter 'u' au chemin courant
            cheminCourant.add(u);

            // D) Calculer la distance du segment précédent→u
            int distAjout = 0;
            if (cheminCourant.size() > 1) {
                String precedent = cheminCourant.get(cheminCourant.size() - 2);
                Ville  vPrev   = chDistance.get(precedent);
                Ville  vU      = chDistance.get(u);
                distAjout = vPrev.getChDistanceVille(vU);
            }
            int distTotalIci = distCourante + distAjout;

            // E) Mettre à jour les degrés des successeurs de 'u'
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