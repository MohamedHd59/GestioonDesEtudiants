import Server.AdminService;
import org.junit.jupiter.api.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {

    private static Process serverProcess;
    private static final int PORT = 1099;
    private static AdminService adminService;

    @BeforeAll
    static void startServer() throws Exception {
        // Nettoyer le registre si besoin
        try {
            Registry registry = LocateRegistry.getRegistry(PORT);
            registry.unbind("AdminService");
        } catch (Exception e) {
            // Ignorer si non bindé
        }

        // Démarrer le serveur
        serverProcess = new ProcessBuilder("java", "-cp",
                System.getProperty("java.class.path"), "Server.Server")
                .inheritIO()
                .start();

        // Attente du serveur
        Thread.sleep(5000);

        // Récupérer l’objet distant AdminService
        adminService = (AdminService) Naming.lookup("rmi://localhost:" + PORT + "/AdminService");
        assertNotNull(adminService, "Le service AdminService devrait être disponible");
    }

    @Test
    void testLoginFunction() throws RemoteException {
        boolean result = adminService.login("mohamedHdilou@gmail.com", "1234");
        assertTrue(result, "L'authentification devrait réussir pour admin");
    }

    @Test
    void testLoginWithWrongCredentials() throws RemoteException {
        boolean result = adminService.login("admin", "wrongPassword");
        assertFalse(result, "L'authentification devrait échouer avec un mauvais mot de passe");
    }

  



    @AfterAll
    static void stopServer() throws Exception {
        try {
            Registry registry = LocateRegistry.getRegistry(PORT);
            registry.unbind("AdminService");
        } catch (Exception e) {
            System.err.println("Erreur de nettoyage: " + e.getMessage());
        }

        if (serverProcess != null) {
            serverProcess.destroyForcibly();
        }
    }
}
