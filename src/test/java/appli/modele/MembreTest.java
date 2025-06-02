package appli.modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe {@link Membre}.
 */
public class MembreTest {

    @Test
    void testConstructeurEtGetChVille() throws Exception {
        // On suppose que "Psykokwak" est dans membres_APPLI.txt
        Membre m1 = new Membre("Psykokwak");
        assertEquals("Psykokwak", m1.getChNomMembre());
        Ville v = m1.getChVille();
        assertNotNull(v, "La ville ne doit pas être null");
        assertFalse(v.getChNom().isEmpty(), "Le nom de la ville ne doit pas être vide");
    }

    @Test
    void testMembreInexistant() {
        // Si le pseudonyme n’existe pas, on doit obtenir une IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            new Membre("UtilisateurInconnu");
        });
    }
}