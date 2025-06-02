package appli.modele;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe {@link Scenario}.
 */
public class ScenarioTest {

    @Test
    void testLectureScenario0() throws Exception {
        Scenario s0 = new Scenario("s0");
        Map<Membre, Membre> transactions = s0.getTransactions();
        // Dans scenario_0.txt, on sait qu’il y a exactement 5 lignes
        assertEquals(5, transactions.size(), "Scénario 0 doit contenir 5 transactions");

        // Vérifier qu’on a bien Psykokwak → Démanta
        Membre vendeur  = new Membre("Psykokwak");
        Membre acheteur = new Membre("Démanta");
        assertEquals(acheteur, transactions.get(vendeur));
    }

    @Test
    void testScenarioInexistant() {
        // "s9" n’existe pas, doit lever NullPointerException ou IllegalArgumentException
        assertThrows(Exception.class, () -> {
            new Scenario("s9");
        });
    }
}
