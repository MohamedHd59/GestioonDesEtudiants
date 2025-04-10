import Entite.Etudiant;
import Entite.etdClass;
import client.Client;
import org.junit.jupiter.api.*;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class ClientTest {

    private Client client;
    private static String uniqueEmail;

    @BeforeEach
    void setUp() throws Exception {
        try {
            client = new Client();
            // Vérifier que le client est bien initialisé
            assumeTrue(client != null, "Le client doit être initialisé");
            
            uniqueEmail = "test." + System.currentTimeMillis() + "@example.com";
            
            // Tester la connexion avec le serveur
            boolean connected = false;
            for (int i = 0; i < 5; i++) {
                try {
                    if (client.login("mohamedHdilou@gmail.com", "1234")) {
                        connected = true;
                        break;
                    }
                    Thread.sleep(1000); // Attendre avant de réessayer
                } catch (Exception e) {
                    // Continuer à essayer en cas d'échec
                }
            }
            assumeTrue(connected, "La connexion au serveur doit réussir");
        } catch (Exception e) {
            fail("Initialisation échouée: " + e.getMessage());
        }
    }

    @Test
    void testLogin() throws RemoteException {
        // Teste la méthode de login
        assertTrue(client.login("mohamedHdilou@gmail.com", "1234"), "Le login doit réussir");
    }

    @Test
    void testChargerClasses() throws RemoteException {
        // Teste la méthode de chargement des classes
        ArrayList<etdClass> classes = client.chargercmbClass();
        assertNotNull(classes, "La liste des classes ne doit pas être nulle");
        assertFalse(classes.isEmpty(), "La liste des classes ne doit pas être vide");
    }

    @Test
    void testAjouterEtudiant() throws RemoteException {
        // Teste l'ajout d'un étudiant
        boolean result = client.ajouterEtudiant(
            "Test", "Etudiant", uniqueEmail, 
            Date.valueOf("2000-01-01"), 1
        );
        assertTrue(result, "L'ajout de l'étudiant doit réussir");
    }

    @Test
    void testRechercherEtudiant() throws RemoteException {
        // Teste la recherche d'un étudiant par nom
        ArrayList<Etudiant> resultats = client.searchETD("Dupont");
        assertNotNull(resultats, "Les résultats de la recherche ne doivent pas être nuls");
        assertFalse(resultats.isEmpty(), "Les résultats de la recherche ne doivent pas être vides");
    }

    @Test
    void testModifierEtudiant() throws RemoteException {
        // 1. Créer un étudiant
        String email = "modif." + System.currentTimeMillis() + "@example.com";
        client.ajouterEtudiant("Amodifier", "Test", email, Date.valueOf("2000-01-01"), 1);
        
        // 2. Récupérer son ID
        ArrayList<Etudiant> etudiants = client.searchETD("Amodifier");
        if (!etudiants.isEmpty()) {
            int id = etudiants.get(0).getId();
            
            // 3. Modifier l'étudiant
            boolean result = client.modifierEtudiant(
                id, 
                "Modifie", 
                "Test", 
                email,
                Date.valueOf("2001-01-01"), 
                2
            );
            assertTrue(result, "La modification de l'étudiant doit réussir");
        } else {
            fail("Étudiant non trouvé pour modification");
        }
    }

    @Test
    void testSupprimerEtudiant() throws RemoteException {
        // 1. Créer un étudiant à supprimer
        String email = "delete." + System.currentTimeMillis() + "@example.com";
        client.ajouterEtudiant("Asupprimer", "Test", email, Date.valueOf("2000-01-01"), 1);
        
        // 2. Récupérer son ID
        ArrayList<Etudiant> etudiants = client.searchETD("Asupprimer");
        if (!etudiants.isEmpty()) {
            int id = etudiants.get(0).getId();
            
            // 3. Supprimer l'étudiant
            boolean result = client.supprimerEtudiant(id);
            assertTrue(result, "La suppression de l'étudiant doit réussir");
            
            // 4. Vérifier que l'étudiant a été supprimé
            etudiants = client.searchETD("Asupprimer");
            assertTrue(etudiants.isEmpty(), "L'étudiant supprimé ne doit plus apparaître dans la recherche");
        } else {
            fail("Étudiant non trouvé pour suppression");
        }
    }
}
