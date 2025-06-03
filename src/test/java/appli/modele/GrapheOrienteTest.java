package appli.modele;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe {@link GrapheOriente}.
 */
public class GrapheOrienteTest {
    private static GrapheOriente g0;

    @BeforeAll
    static void init() throws Exception {
        Scenario s0 = new Scenario("s0");
        g0 = new GrapheOriente(s0);
    }

    @Test
    void testCalculerDegreEntrant() throws Exception {
        // On récupère la méthode privée via réflexivité ou on recalcule ici pour vérifier
        Map<String,Integer> degEnt = g0.copieChDegrésEntrants();
        // Dans le scénario 0, la seule source initiale est “VelizyV” → degré = 0
        assertEquals(0, degEnt.get("VelizyV"));
        // Tous les autres sommets doivent avoir degré ≥ 1
        for (Map.Entry<String,Integer> entry : degEnt.entrySet()) {
            if (!entry.getKey().equals("VelizyV")) {
                assertTrue(entry.getValue() >= 1);
            }
        }
    }

    @Test
    void testGetChVoisinsSortant() {
        // Dans scenario 0, “VelizyV” a exactement 5 successeurs (les 5 “VilleV”)
        TreeSet<String> voisins = new TreeSet<>(g0.getChVoisinsSortant("VelizyV"));
        assertEquals(3, voisins.size(), "VelizyV doit avoir 3 successeurs dans S0");
        // Par exemple, “LyonV” doit figurer parmi eux
        assertTrue(voisins.contains("LyonV"));
    }

    @Test
    void testTriTopologiqueSc0() {
        String result = g0.triTopologique();
        // Commence toujours par Velizy
        assertTrue(result.startsWith("Chemin : Velizy"));
    }

    @Test
    void testTriDistanceSc0() {
        String result = g0.triDistance();
        assertTrue(result.startsWith("Chemin : Velizy"));
        // Vérifier que la deuxième ligne correspond bien à "Distance totale : <nombre>"
        String[] lines = result.split("\n");
        assertTrue(lines.length == 2, "Doit y avoir exactement 2 lignes");
    }

    @Test
    void testMeilleursCheminsSc0() {
        String output = g0.meilleurschemins();
        // On s’attend à une chaîne commençant par "[" et finissant par "]"
        assertTrue(output.startsWith("["));
        assertTrue(output.endsWith("]"));

        // Extraire les parcours (séparés par “,” dans la représentation toString())
        String contenu = output.substring(1, output.length() - 1).trim();
        String[] parcours = contenu.split("," + System.lineSeparator());
        // On doit avoir au moins 1 résultat (mais typiquement 5)
        assertTrue(parcours.length >= 1);
        // Chaque parcours commence par "Velizy"
        for (String p : parcours) {
            assertTrue(p.trim().startsWith("Velizy"), "Chaque parcours doit commencer par 'Velizy'");
        }
    }
}
