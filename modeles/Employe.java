package modeles;

/**
 * ============================================================
 * Employé de base de la banque
 * ============================================================
 * 
 * Représente un employé standard de la banque.
 * Ex: conseillers, caissières, agents d'accueil, etc.
 * 
 * Héritage :
 * - Extend Employee (récupère audit, salaire, embauche)
 * 
 * Caractéristiques :
 * - Poste spécifique (ex: "Caissier", "Conseiller", "Agent d'accueil")
 * - Bonus : 5% du salaire (rémunération de base)
 * 
 * Exemple :
 * - Salaire 2000€/mois → Bonus 100€
 */
public class Employe extends Employee {
    private String poste;  // Intitulé du poste (ex: "Caissier", "Conseiller")
    private static final double TAUX_BONUS = 0.05;  // 5% du salaire

    /**
     * Crée un employé de base.
     * 
     * @param nom     Nom de l'employé
     * @param email   Email unique
     * @param salaire Salaire mensuel en euros
     * @param poste   Intitulé du poste (ex: "Caissier")
     */
    public Employe(String nom, String email, double salaire, String poste) {
        super(nom, email, "Employé", salaire);
        this.poste = poste;
    }

    /**
     * Calcule le bonus annuel.
     * Formule : salaire mensuel × 5%
     * 
     * Exemple : salaire 2000€ → bonus 100€
     */
    @Override public double calculerBonus() { return getSalaire() * TAUX_BONUS; }
    
    /**
     * Affiche un résumé du rôle de cet employé.
     */
    @Override public void afficherRole() {
        System.out.printf("Employé: %s — Poste: %s — Bonus: %.2f€%n", getNom(), poste, calculerBonus());
    }
    
    // Getters / Setters
    public String getPoste() { return poste; }
    public void setPoste(String p) { poste = p; }
}
