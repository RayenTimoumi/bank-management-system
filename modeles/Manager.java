package modeles;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * Manager : Supervise une équipe d'employés
 * ============================================================
 * 
 * Représente un manager/chef d'équipe dans une agence.
 * Responsable du bon fonctionnement de l'agence.
 * 
 * Héritage :
 * - Extends Employee (récupère audit, salaire, embauche)
 * 
 * Caractéristiques :
 * - Gère une agence (identifiée par un code)
 * - Supervise une équipe d'employés
 * - Bonus : 12% du salaire + 100€ par employé supervisé
 * 
 * Exemple :
 * - Salaire 3000€, 5 employés → Bonus = (3000 × 12%) + (5 × 100) = 360 + 500 = 860€
 */
public class Manager extends Employee {
    private final String agence;         // Code de l'agence gérée
    private final List<Employe> equipe;  // Liste des employés superviesés
    private static final double TAUX_BONUS = 0.12;  // 12% du salaire (+ bonus par employé)

    /**
     * Crée un manager.
     * 
     * @param nom     Nom du manager
     * @param email   Email unique
     * @param salaire Salaire mensuel en euros
     * @param agence  Code de l'agence gérée (ex: "Paris-01", "Lyon-03")
     */
    public Manager(String nom, String email, double salaire, String agence) {
        super(nom, email, "Manager", salaire);
        this.agence = agence;
        this.equipe = new ArrayList<>();
    }

    /**
     * Ajoute un employé à l'équipe.
     * Enregistre l'action dans l'audit trail.
     */
    public void ajouterEmploye(Employe e) { 
        equipe.add(e); 
        enregistrerAction("Ajout employé: " + e.getNom()); 
    }
    
    /** Retire un employé de l'équipe */
    public void retirerEmploye(Employe e) { equipe.remove(e); }
    
    /** Retourne une copie de l'équipe */
    public List<Employe> getEquipe() { return new ArrayList<>(equipe); }

    /**
     * Calcule le bonus annuel.
     * Formule : (salaire × 12%) + (nombre d'employés × 100)
     * 
     * Exemple : salaire 3000€, 5 employés → (3000 × 12%) + (5 × 100) = 860€
     */
    @Override public double calculerBonus() { 
        return getSalaire() * TAUX_BONUS + equipe.size() * 100; 
    }
    
    /**
     * Affiche un résumé du rôle de ce manager.
     */
    @Override public void afficherRole() {
        System.out.printf("Manager: %s — Agence: %s — Équipe: %d — Bonus: %.2f€%n",
            getNom(), agence, equipe.size(), calculerBonus());
    }
    
    // Getter pour accéder à l'agence
    public String getAgence() { return agence; }
}
