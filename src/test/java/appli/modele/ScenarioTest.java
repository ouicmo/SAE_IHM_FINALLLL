package appli.modele;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe {@link Scenario}, en s’appuyant
 * sur le fichier réel {@code data/scenario_0.txt} contenant :
 * <pre>
 *   Psykokwak   -> Démanta
 *   Machoc      -> Crocrodil
 *   Chapignon   -> Leuphorie
 *   Leuphorie   -> Ramoloss
 *   Écrapince   -> Minidraco
 * </pre>
 */
public class ScenarioTest {

    @Test
    void testConstructeurEtGetTransactions() throws Exception {
        Scenario s0 = new Scenario("s0");
        Map<Membre, Membre> transactions = s0.getTransactions();

        // On sait que scenario_0.txt comporte 5 lignes
        assertEquals(5, transactions.size(), "Le scénario 0 doit contenir 5 transactions");

        // Vérifier chacune des paires attendues
        Membre v1 = new Membre("Psykokwak");
        Membre a1 = new Membre("Démanta");
        assertEquals(a1, transactions.get(v1), "Psykokwak devrait vendre à Démanta");

        Membre v2 = new Membre("Machoc");
        Membre a2 = new Membre("Crocrodil");
        assertEquals(a2, transactions.get(v2), "Machoc devrait vendre à Crocrodil");

        Membre v3 = new Membre("Chapignon");
        Membre a3 = new Membre("Leuphorie");
        assertEquals(a3, transactions.get(v3), "Chapignon devrait vendre à Leuphorie");

        Membre v4 = new Membre("Leuphorie");
        Membre a4 = new Membre("Ramoloss");
        assertEquals(a4, transactions.get(v4), "Leuphorie devrait vendre à Ramoloss");

        Membre v5 = new Membre("Écrapince");
        Membre a5 = new Membre("Minidraco");
        assertEquals(a5, transactions.get(v5), "Écrapince devrait vendre à Minidraco");
    }

    @Test
    void testGetVendeursEtAcheteurs() throws Exception {
        Scenario s0 = new Scenario("s0");

        ArrayList<Membre> vendeurs  = s0.getVendeurs();
        ArrayList<Membre> acheteurs = s0.getAcheteurs();

        // Il y a exactement 5 vendeurs et 5 acheteurs
        assertEquals(5, vendeurs.size(),  "Doit y avoir 5 vendeurs");
        assertEquals(5, acheteurs.size(), "Doit y avoir 5 acheteurs");

        // Vérifier que “Psykokwak” figure dans la liste des vendeurs
        assertTrue(vendeurs.stream().anyMatch(m -> m.getChNomMembre().equals("Psykokwak")));
        // et que “Démanta” figure dans la liste des acheteurs
        assertTrue(acheteurs.stream().anyMatch(m -> m.getChNomMembre().equals("Démanta")));

        // Vérifier que “Leuphorie” figure à la fois comme acheteur (ligne 3)
        // et comme vendeur (ligne 4)
        assertTrue(acheteurs.stream().anyMatch(m -> m.getChNomMembre().equals("Leuphorie")));
        assertTrue(vendeurs.stream().anyMatch(m -> m.getChNomMembre().equals("Leuphorie")));
    }

    @Test
    void testScenarioInexistant() {
        // Clef hors de “s0” à “s8” doit lever Exception
        assertThrows(Exception.class, () -> {
            new Scenario("s9");
        });
    }
}
