import activeRecord.Personne;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPersonne {

    /**
     * Cette méthode
     * construit la table Personne et la peuple en supposant que la méthode save de la classe Personne
     * existe. Cette méthode save ajoute le tuple correspondant à l'objet personne this dans la base de données
     */
    @BeforeEach
    public void init() {

        try {
            Personne.createTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Personne p1 = new Personne("Spielberg", "Steven");
        Personne p2 = new Personne("Scott", "Ridley");
        Personne p3 = new Personne("Kubrick", "Stanley");
        Personne p4 = new Personne("Fincher", "David");

        try {
            p1.save();
            p2.save();
            p3.save();
            p4.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Écrire dans la classe de test une méthode @AfterEach chargée de supprimer toute la table
     * personne.
     * @throws Exception
     */
    @AfterEach
    public void deleteTable() throws Exception {

        Personne.deleteTable();
    }

    @Test
    public void testFindAll() throws Exception {

        ArrayList<Personne> personnes = Personne.findAll();

        //On teste avec un Assert
        assertEquals(1, personnes.get(0).getId());
        assertEquals("Spielberg", personnes.get(0).getNom());
        assertEquals("Steven", personnes.get(0).getPrenom());

        assertEquals(2, personnes.get(1).getId());
        assertEquals("Scott", personnes.get(1).getNom());
        assertEquals("Ridley", personnes.get(1).getPrenom());

        assertEquals(3, personnes.get(2).getId());
        assertEquals("Kubrick", personnes.get(2).getNom());
        assertEquals("Stanley", personnes.get(2).getPrenom());

        assertEquals(4, personnes.get(3).getId());
        assertEquals("Fincher", personnes.get(3).getNom());
        assertEquals("David", personnes.get(3).getPrenom());
    }

    @Test
    public void testFindById() throws Exception {

        Personne personne = Personne.findById(1);

        //On teste avec un Assert
        assert personne != null;
        assertEquals(1, personne.getId());
        assertEquals("Spielberg", personne.getNom());
        assertEquals("Steven", personne.getPrenom());
    }

    @Test
    public void testfindByName() throws Exception {

        ArrayList<Personne> personnes = Personne.findByName("Spielberg");

        //On teste avec un Assert
        assertEquals(1, personnes.get(0).getId());
        assertEquals("Spielberg", personnes.get(0).getNom());
        assertEquals("Steven", personnes.get(0).getPrenom());
    }
}