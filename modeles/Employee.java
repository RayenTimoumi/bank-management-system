package modeles;

import interfaces.Auditable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un employé de la banque.
 * Hérite de User et implémente Auditable pour le suivi des actions.
 *
 * Exemples de rôle : "Conseiller", "Caissier", "Directeur", "Analyste"
 */
public class Employee extends User implements Auditable {

    private String role;
    private double salaire;
    private final LocalDateTime dateEmbauche;
    private final List<String> historique;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // ----------------------------------------------------------------
    // Constructeur
    // ----------------------------------------------------------------
    public Employee(String nom, String email, String role, double salaire) {
        super(nom, email);
        this.role           = role;
        this.salaire        = salaire;
        this.dateEmbauche   = LocalDateTime.now();
        this.historique     = new ArrayList<>();
    }

    // ----------------------------------------------------------------
    // Méthode abstraite héritée de User
    // ----------------------------------------------------------------
    @Override
    public String getType() { return "Employé"; }

    // ----------------------------------------------------------------
    // Calcul du bonus : 10% du salaire (règle simple)
    // ----------------------------------------------------------------
    public double calculerBonus() {
        return salaire * 0.10;
    }

    // ----------------------------------------------------------------
    // Auditable
    // ----------------------------------------------------------------
    @Override
    public void enregistrerAction(String action) {
        historique.add("[" + LocalDateTime.now().format(FMT) + "] " + action);
    }

    @Override
    public List<String> obtenirHistorique() {
        return new ArrayList<>(historique);
    }

    // ----------------------------------------------------------------
    // Afficher le rôle
    // ----------------------------------------------------------------
    public void afficherRole() {
        System.out.printf("Employé: %s — Rôle: %s — Bonus: %.2f€%n", getNom(), role, calculerBonus());
    }

    // ----------------------------------------------------------------
    // Getters / Setters
    // ----------------------------------------------------------------
    public String getRole()                 { return role; }
    public double getSalaire()              { return salaire; }
    public LocalDateTime getDateEmbauche()  { return dateEmbauche; }

    public void setRole(String role)            { this.role = role; }
    public void setSalaire(double salaire)      { this.salaire = salaire; }

    @Override
    public String toString() {
        return String.format("Employé[%s] %s — Rôle: %s — %.2f€/mois", getId(), getNom(), role, salaire);
    }
}
