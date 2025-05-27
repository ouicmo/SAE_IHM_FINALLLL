package appli.modele;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;


public class Membre {
    private String chNomMembre;
    private Ville chVille;

    public Membre(String parNomMembre) throws Exception {
        chNomMembre = parNomMembre;
        /*Lecture des distances du fichier distances.txt permettant d'obtenir un dictionnaire memberdic tel que (Membre,Ville)*/
        File memberliste = new File("data/membres_APPLI.txt");
        Scanner scan2 = new Scanner(memberliste);
        Map<String, String> memberdic = new LinkedHashMap<>();
        while (scan2.hasNextLine()) {
            String line = scan2.nextLine();
            String[] split = line.split(" ");
            memberdic.put(split[0], split[1]);
        }
        chVille = new Ville(memberdic.get(parNomMembre));
    }

    public String getChNomMembre() {
        return chNomMembre;
    }

    public Ville getChVille() {
        return chVille;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Membre)) return false;
        Membre m = (Membre) o;
        return chNomMembre.equals(m.chNomMembre);
    }

    @Override
    public int hashCode() {
        return chNomMembre.hashCode();
    }


    public String toString() {
        return chNomMembre;
    }
}
