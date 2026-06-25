package modeles;

/**
 * ============================================================
 * Directeur : Responsable de la banque
 * ============================================================
 * 
 * Représente un directeur de région ou directeur général.
 * Au sommet de la hiérarchie administrative.
 * 
 * Héritage :
 * - Extends Employee (récupère audit, salaire, embauche)
 * 
 * Caractéristiques :
 * - Responsable d'une région
 * - Bonus : 20% du salaire + 2000€ de base
 * 
 * Exemple :
 * - Salaire 5000€ → Bonus = (5000 × 20%) + 2000 = 1000 + 2000 = 3000€
 */
public class Directeur extends Employee {
    private final String region;  // Région gérée (ex: "Ile-de-France", "Auvergne-Rhône-Alpes")
    private static final double TAUX_BONUS = 0.20;  // 20% du salaire (+ 2000€ fixes)

    /**
     * Crée un directeur.
     * 
     * @param nom     Nom du directeur
     * @param email   Email unique
     * @param salaire Salaire mensuel en euros
     * @param region  Région gérée (ex: "Ile-de-France")
     */
    public Directeur(String nom, String email, double salaire, String region) {
        super(nom, email, "Directeur", salaire);
        this.region = region;
    }

    /**
     * Calcule le bonus annuel.
     * Formule : (salaire × 20%) + 2000€
     * 
     * Exemple : salaire 5000€ → (5000 × 20%) + 2000 = 3000€
     */
    @Override public double calculerBonus() { 
        return getSalaire() * TAUX_BONUS + 2000; 
    }
    
    /**
     * Affiche un résumé du rôle de ce directeur.
     */
    @Override public void afficherRole() {
        System.out.printf("Directeur: %s — Région: %s — Bonus: %.2f€%n", 
            getNom(), region, calculerBonus());
    }
    
    // Getter pour accéder à la région
    public String getRegion() { return region; }
}
