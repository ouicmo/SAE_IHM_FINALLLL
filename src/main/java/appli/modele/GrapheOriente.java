package appli.modele;
import java.util.*;

public class    GrapheOriente {
    private TreeMap<String, LinkedHashSet<String>> chVoisinsSortant;
    private ArrayList<String> chSommets;
    private Map<String, Ville> chDistance;
    private Map<String, Integer> chDegreEntrant;

    public GrapheOriente(Scenario parScenario) throws Exception {
        // 1) Initialisation des champs
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
            }
            if (!chSommets.contains(vA)) {
                chSommets.add(vA);
            }
            chDistance.put(vV, vendeur.getChVille());
            chDistance.put(vA, parScenario.getTransactions().get(vendeur).getChVille());

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
            // Faites plutôt :
            Set<String> voisinsDeA = chVoisinsSortant
                    .computeIfAbsent(vA, k -> new LinkedHashSet<>());
            if (voisinsDeA.add("VelizyA")) {
                // on incrémente le degré entrant de VelizyA
                chDegreEntrant.put("VelizyA", chDegreEntrant.get("VelizyA") + 1);
            }


        }
        // garantir que chaque sommet a bien un ensemble (même vide) dans la liste d'adjacence
        for (String sommet : chSommets) {
            chVoisinsSortant.computeIfAbsent(sommet, k -> new LinkedHashSet<>());
        }
    }


    public Map<String, Integer> getDegreEntrant() {
        return chDegreEntrant;
    }


    public LinkedHashSet<String> getChVoisinsSortant(String parVille) {
        return chVoisinsSortant.get(parVille);
    }

    public String triTopologique() {
        Map<String, Integer> degEnt = getDegreEntrant();
        int distancetotal = 0;

        // 1) File des sources (degrés 0)
        Deque<String> sources = new ArrayDeque<>();
        for (var e : degEnt.entrySet()) {
            if (e.getValue() == 0) {
                sources.addLast(e.getKey());
            }
        }

        // 2) Kahn
        List<String> ordre = new ArrayList<>();
        while (!sources.isEmpty()) {
            String s = sources.pollFirst();
            ordre.add(s);
            for (String v : this.getChVoisinsSortant(s)) {
                degEnt.put(v, degEnt.get(v) - 1);
                if (degEnt.get(v) == 0) {
                    sources.addLast(v);
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

        return ordre + "Distance totale :" + distancetotal;
    }


}