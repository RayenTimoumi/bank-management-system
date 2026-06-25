package gestionnaires;

import exceptions.*;
import java.util.*;
import modeles.*;

/**
 * ============================================================
 * Gestionnaire central de la banque (BanqueManager)
 * ============================================================
 * 
 * Cette classe est responsable de :
 * - Stocker tous les clients en mémoire (dans un tableau)
 * - Stocker tous les employés en mémoire (dans un tableau)
 * - Stocker tous les comptes en mémoire (dans un tableau)
 * - Stocker tous les transactions en mémoire (dans un tableau)
 * - Fournir des méthodes pour manipuler ces données
 * 
 * ARCHITECTURE :
 * - Utilise des tableaux Java dynamiques (redimensionnables)
 * - Implémente une logique CRUD (Create, Read, Update, Delete)
 * - Valide les opérations bancaires (soldes, montants, etc.)
 * 
 * IMPORTANT :
 * - Pas de base de données
 * - Pas de fichiers persistants
 * - Les données sont perdues à la fermeture
 */
public class BanqueManager {

    private static final int CAPACITE_INITIALE = 10;

    // ========== TABLEAUX DE STOCKAGE (mémoire) ==========
    private Client[] clients;           // Tous les clients de la banque
    private Employee[] employes;        // Tous les employés de la banque
    private Compte[] comptes;           // Tous les comptes bancaires
    private Transaction[] transactions; // Toutes les transactions effectuées

    // ========== COMPTEURS (nombre d'éléments actuels) ==========
    private int nbClients;
    private int nbEmployes;
    private int nbComptes;
    private int nbTransactions;

    // Initialise tous les tableaux avec la capacité initiale
    public BanqueManager() {
        clients = new Client[CAPACITE_INITIALE];
        employes = new Employee[CAPACITE_INITIALE];
        comptes = new Compte[CAPACITE_INITIALE];
        transactions = new Transaction[CAPACITE_INITIALE];
    }

    // ================================================================
    // GESTION DES CLIENTS
    // ================================================================
    // Surcharge de méthodes pour créer des clients avec différents
    // niveaux de complétude (flexible, seulement nom/email requis)


    public void ajouterClient(String nom, String email) throws ClientExisteException {
        ajouterClient(new Client(nom, email));
    }

    public void ajouterClient(String nom, String email, String telephone) throws ClientExisteException {
        ajouterClient(new Client(nom, email, telephone));
    }

    public void ajouterClient(String nom, String email, String telephone,
                              String adresse, String ville) throws ClientExisteException {
        ajouterClient(new Client(nom, email, telephone, adresse, ville));
    }

    public void ajouterClient(Client client) throws ClientExisteException {
        if (chercherClientParEmail(client.getEmail()) != null) {
            throw new ClientExisteException("Un client avec l'email " + client.getEmail() + " existe deja");
        }
        assurerCapaciteClients();
        clients[nbClients++] = client;
    }

    public void supprimerClient(Client client) {
        for (int i = 0; i < nbClients; i++) {
            if (clients[i].equals(client)) {
                supprimerClientIndex(i);
                break;
            }
        }

        for (int i = nbComptes - 1; i >= 0; i--) {
            if (comptes[i].getProprietaire().equals(client)) {
                supprimerCompteIndex(i);
            }
        }
    }

    public Client chercherClientParEmail(String email) {
        for (int i = 0; i < nbClients; i++) {
            if (clients[i].getEmail().equalsIgnoreCase(email)) {
                return clients[i];
            }
        }
        return null;
    }

    public Client chercherClientParNom(String nom) {
        for (int i = 0; i < nbClients; i++) {
            if (clients[i].getNom().equalsIgnoreCase(nom)) {
                return clients[i];
            }
        }
        return null;
    }

    public Set<Client> getClients() {
        Set<Client> copie = new LinkedHashSet<>();
        for (int i = 0; i < nbClients; i++) {
            copie.add(clients[i]);
        }
        return copie;
    }

    // ================================================================
    // GESTION DES EMPLOYES
    // ================================================================

    public void ajouterEmployee(Employee employee) throws OperationNonAutoriseeException {
        for (int i = 0; i < nbEmployes; i++) {
            if (employes[i].getEmail().equalsIgnoreCase(employee.getEmail())) {
                throw new OperationNonAutoriseeException("Un employe avec cet email existe deja");
            }
        }
        assurerCapaciteEmployes();
        employes[nbEmployes++] = employee;
    }

    public void supprimerEmployee(String id) {
        for (int i = 0; i < nbEmployes; i++) {
            if (employes[i].getId().equals(id)) {
                supprimerEmployeIndex(i);
                return;
            }
        }
    }

    public Employee chercherEmployeeParId(String id) {
        for (int i = 0; i < nbEmployes; i++) {
            if (employes[i].getId().equals(id)) {
                return employes[i];
            }
        }
        return null;
    }

    public List<Employee> getEmployes() {
        return new ArrayList<>(Arrays.asList(Arrays.copyOf(employes, nbEmployes)));
    }

    // ================================================================
    // GESTION DES COMPTES
    // ================================================================

    public Compte creerCompte(String type, User proprietaire)
            throws MontantInvalidException, CompteInexistantException {

        Compte compte;

        switch (type.toLowerCase()) {
            case "client" -> {
                if (!(proprietaire instanceof Client)) {
                    throw new MontantInvalidException("Un Compte Client necessite un proprietaire de type Client");
                }
                compte = new CompteClient((Client) proprietaire);
            }
            case "employe" -> {
                if (!(proprietaire instanceof Employee)) {
                    throw new MontantInvalidException("Un Compte Employe necessite un proprietaire de type Employee");
                }
                compte = new CompteEmployee((Employee) proprietaire);
            }
            case "investissement" -> {
                if (!(proprietaire instanceof Client)) {
                    throw new MontantInvalidException("Un Compte Investissement necessite un proprietaire Client");
                }
                compte = new CompteInvestissement((Client) proprietaire);
            }
            default -> throw new MontantInvalidException("Type de compte inconnu : " + type);
        }

        ajouterCompte(compte);
        return compte;
    }

    public void ajouterCompte(Compte compte) {
        assurerCapaciteComptes();
        comptes[nbComptes++] = compte;
    }

    public void fermerCompte(Compte compte) {
        for (int i = 0; i < nbComptes; i++) {
            if (comptes[i].equals(compte)) {
                supprimerCompteIndex(i);
                return;
            }
        }
    }

    public Compte chercherCompte(String numero) throws CompteInexistantException {
        for (int i = 0; i < nbComptes; i++) {
            if (comptes[i].getNumeroCompte().equals(numero)) {
                return comptes[i];
            }
        }
        throw new CompteInexistantException("Compte introuvable : " + numero);
    }

    public List<Compte> getComptesDeUser(User user) {
        List<Compte> resultat = new ArrayList<>();
        for (int i = 0; i < nbComptes; i++) {
            if (comptes[i].getProprietaire().equals(user)) {
                resultat.add(comptes[i]);
            }
        }
        return resultat;
    }

    public List<Compte> getComptes() {
        return new ArrayList<>(Arrays.asList(Arrays.copyOf(comptes, nbComptes)));
    }

    // ================================================================
    // GESTION DES TRANSACTIONS
    // ================================================================

    public void enregistrerTransaction(Transaction t) {
        assurerCapaciteTransactions();
        transactions[nbTransactions++] = t;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(Arrays.asList(Arrays.copyOf(transactions, nbTransactions)));
    }

    // ================================================================
    // STATISTIQUES
    // ================================================================

    public double calculerSoldeTotal() {
        double total = 0;
        for (int i = 0; i < nbComptes; i++) {
            total += comptes[i].getSolde();
        }
        return total;
    }

    public double calculerSoldeUser(User user) {
        double total = 0;
        for (int i = 0; i < nbComptes; i++) {
            if (comptes[i].getProprietaire().equals(user)) {
                total += comptes[i].getSolde();
            }
        }
        return total;
    }

    public double calculerMasseSalariale() {
        double total = 0;
        for (int i = 0; i < nbEmployes; i++) {
            total += employes[i].getSalaire();
        }
        return total;
    }

    public Map<String, Double> getSoldesParType() {
        Map<String, Double> soldes = new LinkedHashMap<>();
        for (int i = 0; i < nbComptes; i++) {
            String type = comptes[i].getType();
            soldes.put(type, soldes.getOrDefault(type, 0.0) + comptes[i].getSolde());
        }
        return soldes;
    }

    private void assurerCapaciteClients() {
        if (nbClients == clients.length) {
            clients = Arrays.copyOf(clients, clients.length * 2);
        }
    }

    private void assurerCapaciteEmployes() {
        if (nbEmployes == employes.length) {
            employes = Arrays.copyOf(employes, employes.length * 2);
        }
    }

    private void assurerCapaciteComptes() {
        if (nbComptes == comptes.length) {
            comptes = Arrays.copyOf(comptes, comptes.length * 2);
        }
    }

    private void assurerCapaciteTransactions() {
        if (nbTransactions == transactions.length) {
            transactions = Arrays.copyOf(transactions, transactions.length * 2);
        }
    }

    private void supprimerClientIndex(int index) {
        int elementsApres = nbClients - index - 1;
        if (elementsApres > 0) {
            System.arraycopy(clients, index + 1, clients, index, elementsApres);
        }
        clients[--nbClients] = null;
    }

    private void supprimerEmployeIndex(int index) {
        int elementsApres = nbEmployes - index - 1;
        if (elementsApres > 0) {
            System.arraycopy(employes, index + 1, employes, index, elementsApres);
        }
        employes[--nbEmployes] = null;
    }

    private void supprimerCompteIndex(int index) {
        int elementsApres = nbComptes - index - 1;
        if (elementsApres > 0) {
            System.arraycopy(comptes, index + 1, comptes, index, elementsApres);
        }
        comptes[--nbComptes] = null;
    }
}
