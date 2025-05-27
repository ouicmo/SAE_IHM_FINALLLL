package appli.modele;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Ville {
    private String chNom;
    private int chIndex;
    private ArrayList<Integer> chDistanceVille;

    public Ville(String parNom) throws Exception {
        chNom = parNom;
        File distance = new File("src/appli.Données/distances.txt");
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

    public String getChNom() {
        return chNom;
    }

    public int getChIndex() {
        return chIndex;
    }

    public int getChDistanceVille(Ville parVille) {
        for (int i = 0; i < chDistanceVille.size(); i++) {
            if (chDistanceVille.get(i) == chDistanceVille.get(parVille.getChIndex())) {
                return chDistanceVille.get(i);
            }
        }
        return 99999999;
    }

    public String toString() {
        return chNom;
    }

}