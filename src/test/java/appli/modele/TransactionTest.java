package appli.modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe {@link Transaction}.
 */
public class TransactionTest {

    @Test
    void testTransactionDistance() throws Exception {
        // On crée deux membres dont on connaît la distance entre leurs villes
        Membre mb1 = new Membre("Bulbizarre"); // ville par exemple : "Lyon"
        Membre mb2 = new Membre("Salamèche");   // ville par exemple : "Marseille"
        Transaction t = new Transaction(mb1, mb2);

        int dist = t.getChDistance();
        // On vérifie simplement que la distance est positive et cohérente
        assertTrue(dist > 0, "Distance doit être > 0 entre deux villes distinctes");

        // Vérifier le toString() contient bien "Vendeur" et "Acheteur"
        String s = t.toString();
        assertTrue(s.contains("Vendeur"),  "toString() doit contenir 'Vendeur'");
        assertTrue(s.contains("Acheteur"), "toString() doit contenir 'Acheteur'");
        assertTrue(s.contains(String.valueOf(dist)), "toString() doit contenir la distance");
    }
}
