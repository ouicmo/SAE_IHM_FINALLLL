package appli.modele;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Tests unitaires pour la classe {@link Ville}.
 */
public class VilleTest {
    private static Ville villeLyon;
    private static Ville villeGrenoble;
    private static Ville villeMarseille;

    @BeforeAll
    static void init() throws Exception {
        // On suppose que "distances.txt" contient au moins les villes "Lyon", "Grenoble", "Marseille".
        villeLyon      = new Ville("Lyon");
        villeGrenoble  = new Ville("Grenoble");
        villeMarseille = new Ville("Marseille");
    }

    @Test
    void testGetChNom() {
        assertEquals("Lyon", villeLyon.getChNom());
        assertEquals("Grenoble", villeGrenoble.getChNom());
        assertEquals("Marseille", villeMarseille.getChNom());
    }

    @Test
    void testGetChIndexNonNul() {
        // Les indices doivent être ≥ 0
        assertTrue(villeLyon.getChIndex() >= 0);
        assertTrue(villeGrenoble.getChIndex() >= 0);
        assertTrue(villeMarseille.getChIndex() >= 0);
        // Différentes villes ⇒ indices différents
        assertNotEquals(villeLyon.getChIndex(), villeGrenoble.getChIndex());
        assertNotEquals(villeLyon.getChIndex(), villeMarseille.getChIndex());
        assertNotEquals(villeGrenoble.getChIndex(), villeMarseille.getChIndex());
    }

    @Test
    void testGetChDistanceVille() {
        int dLG = villeLyon.getChDistanceVille(villeGrenoble);
        int dGL = villeGrenoble.getChDistanceVille(villeLyon);
        assertEquals(dLG, dGL, "Distance doit être symétrique");

        assertTrue(dLG > 0, "Distance L→G doit être strictement positive");
    }
}