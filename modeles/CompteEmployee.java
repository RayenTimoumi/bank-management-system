package modeles;

import exceptions.MontantInvalidException;

/**
 * Compte réservé aux employés de la banque.
 * - Taux d'intérêt : 2% annuel (avantage employé)
 * - Frais mensuels : 0€ (exonéré)
 */
public class CompteEmployee extends Compte {

    private static final double TAUX_INTERET_ANNUEL = 0.02; // 2%

    // ----------------------------------------------------------------
    // Constructeur
    // ----------------------------------------------------------------
    public CompteEmployee(Employee proprietaire) {
        super(proprietaire);
    }

    // ----------------------------------------------------------------
    // Implémentation des méthodes abstraites
    // ----------------------------------------------------------------

    @Override
    public String getType() {
        return "Compte Employé";
    }

    /** Les employés bénéficient d'un taux préférentiel de 2% annuel */
    @Override
    public double calculerInteret() {
        double interet = getSolde() * (TAUX_INTERET_ANNUEL / 12);
        setSolde(getSolde() + interet);
        ajouterAuHistorique(String.format("Intérêts (taux préférentiel) : +%.2f€", interet));
        return interet;
    }

    /** Les employés sont exonérés de frais mensuels */
    @Override
    public void appliquerFrais() throws MontantInvalidException {
        // Aucun frais pour les comptes employé
        ajouterAuHistorique("Frais mensuels : 0€ (employé exonéré)");
    }
}
