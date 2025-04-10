package Server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {

    protected Server() throws Exception {
        super();
    }

    public static void main(String[] args) {
        try {
            // Créez un registre RMI sur le port 1099
            LocateRegistry.createRegistry(1099);
            
            // Créez une instance de AdminDAO qui implémente AdminService
            AdminService adminService = new AdminDAO();
            
            // Lier l'objet adminService à un nom dans le registre RMI
            Naming.rebind("rmi://localhost:1099/AdminService", adminService);
            
            // Message indiquant que le serveur est prêt
            System.out.println("🚀 Serveur RMI démarré sur le port 1099...");
        } catch (Exception ex) {
            System.out.println("Erreur: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
