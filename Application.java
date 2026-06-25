import gestionnaires.BanqueManager;
import javax.swing.*;
import modeles.*;
import ui.FenetrePrincipale;

/**
 * ============================================================
 * Point d'entrée de l'application MaBank
 * ============================================================
 * 
 * Cette classe lance directement l'interface graphique Swing
 * sans nécessiter de connexion préalable (mode administrateur).
 * 
 * Flux :
 * 1. Initialise le look & feel du système (UI native)
 * 2. Crée le gestionnaire central (BanqueManager)
 * 3. Injecte des données de démonstration
 * 4. Affiche la fenêtre principale (FenetrePrincipale)
 * 
 * IMPORTANT : Toutes les données restent uniquement en mémoire
 * pendant l'exécution. Elles sont perdues à la fermeture.
 */
public class Application {

    public static void main(String[] args) {
        // Essaie d'appliquer le look & feel natif du système d'exploitation
        // Cela rend l'interface plus cohérente avec le reste du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) {
            System.err.println("Impossible de charger le look & feel du systeme: " + e.getMessage());
        }

        // SwingUtilities.invokeLater() garantit que la GUI est créée sur le thread Swing (thread-safe)
        SwingUtilities.invokeLater(() -> {
            // Crée l'instance unique du gestionnaire de banque
            BanqueManager banque = new BanqueManager();
            
            // Pré-remplit avec des données de démonstration
            injecterDonneesDemo(banque);
            
            // Lance la fenêtre principale et la rend visible
            new FenetrePrincipale(banque).setVisible(true);
        });
    }

    /**
     * Remplit la banque avec des données de démonstration.
     * 
     * Cela permet de tester l'application avec des données initiales
     * sans devoir créer manuellement tous les clients et comptes.
     * 
     * Données injectées :
     * - 3 clients (Alice, Bob, Clara)
     * - 2 employés (Sophie, Paul)
     * - 4 comptes (1 par client + 1 investissement + 1 employé)
     * - Soldes initiaux par compte
     * 
     * ATTENTION : Ces données sont perdues à la fermeture de l'application
     * car il n'existe pas de persistance en base de données.
     */
    private static void injecterDonneesDemo(BanqueManager banque) {
        try {
            // ========== Ajouter des clients ==========
            banque.ajouterClient("Alice Martin", "alice@mail.com", "0601020304");
            banque.ajouterClient("Bob Dupont", "bob@mail.com", "0605060708");
            banque.ajouterClient("Clara Leblanc", "clara@mail.com");

            // Récupérer les clients pour les utiliser dans les comptes
            Client alice = banque.chercherClientParEmail("alice@mail.com");
            Client bob = banque.chercherClientParEmail("bob@mail.com");

            // ========== Ajouter des employés ==========
            Employee sophie = new Employee("Sophie Noir", "sophie@mabank.fr", "Conseillere", 2500);
            Employee paul = new Employee("Paul Durand", "paul@mabank.fr", "Directeur", 5000);
            banque.ajouterEmployee(sophie);
            banque.ajouterEmployee(paul);

            // ========== Créer des comptes ==========
            Compte cAlice = banque.creerCompte("client", alice);
            Compte cBob = banque.creerCompte("client", bob);
            Compte cInvest = banque.creerCompte("investissement", alice);
            Compte cSophie = banque.creerCompte("employe", sophie);

            // ========== Alimenter les comptes ==========
            cAlice.deposer(3500.00);      // Alice : compte client
            cBob.deposer(1200.00);        // Bob : compte client
            cInvest.deposer(15000.00);    // Alice : compte investissement
            cSophie.deposer(4000.00);     // Sophie : compte employé

            System.out.println("[Demo] Donnees de demonstration chargees en memoire.");
        } catch (Exception e) {
            // Capture toute erreur lors du chargement des données de démo
            System.err.println("[Demo] Erreur : " + e.getMessage());
        }
    }
}
