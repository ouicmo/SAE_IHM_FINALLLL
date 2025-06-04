package appli.modele;

import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void testEqualsIdentique() throws Exception {
        // Test pour vérifier que deux membres avec le même pseudonyme sont égaux
        Membre membre1Bis = new Membre("Psykokwak");
        Membre membre1 = new Membre("Psykokwak");
        assertTrue(membre1.equals(membre1Bis), "Les membres avec le même pseudonyme doivent être égaux");
    }

    @Test
    void testEqualsDifferent() throws Exception {
        // Test pour vérifier que deux membres avec des pseudonymes différents ne sont pas égaux
        Membre membre1 = new Membre("Psykokwak");
        Membre membre2 = new Membre("Machoc");
        assertFalse(membre1.equals(membre2), "Les membres avec des pseudonymes différents ne doivent pas être égaux");
    }

    @Test
    void testHashCodeIdentique() throws Exception {
        // Test pour vérifier que deux membres avec le même pseudonyme ont le même code de hachage
        Membre membre1Bis = new Membre("Psykokwak");
        Membre membre1 = new Membre("Psykokwak");
        assertEquals(membre1.hashCode(), membre1Bis.hashCode(), "Les membres avec le même pseudonyme doivent avoir le même hashCode");
    }

    @Test
    void testHashCodeDifferent() throws Exception {
        // Test pour vérifier que deux membres avec des pseudonymes différents ont des codes de hachage différents
        Membre membre1 = new Membre("Psykokwak");
        Membre membre2 = new Membre("Machoc");
        assertNotEquals(membre1.hashCode(), membre2.hashCode(), "Les membres avec des pseudonymes différents doivent avoir des hashCode différents");
    }

    @Test
    void testToString() throws Exception {
        // Test pour vérifier que la méthode toString retourne le pseudonyme correct
        Membre membre1 = new Membre("Psykokwak");
        assertEquals("Psykokwak", membre1.toString(), "La méthode toString doit retourner le pseudonyme du membre");
    }


}
