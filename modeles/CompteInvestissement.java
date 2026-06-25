package modeles;

import exceptions.MontantInvalidException;

/**
 * ============================================================
 * Compte d'Investissement
 * ============================================================
 * 
 * Compte pour les clients souhaitant faire fructifier leur argent
 * avec un rendement plus élevé que le compte standard.
 * 
 * Caractéristiques :
 * - Taux d'intérêt : 5% annuel (rendement élevé)
 * - Frais mensuels : 5€
 * - Solde minimum : 500€ pour ouvrir ce compte
 * - Destiné aux clients souhaitant investir
 * 
 * Mécaniques :
 * - Chaque mois : intérêts = (solde × 5%) / 12
 * - Chaque mois : frais 5€ prélevés
 * 
 * Exemple :
 * - Solde initial : 5000€
 * - Mois 1 : +20.83€ d'intérêts, -5€ de frais = 5015.83€
 */
public class CompteInvestissement extends Compte {

    private static final double TAUX_INTERET_ANNUEL = 0.05; // 5% par an (intérêts élevés)
    private static final double FRAIS_MENSUELS      = 5.0;  // 5€ de frais/mois
    public  static final double SOLDE_MINIMUM       = 500.0; // 500€ minimum pour ouvrir

    // ================================================================
    // Constructeur
    // ================================================================
    /**
     * Crée un compte d'investissement.
     * 
     * @param proprietaire Le client propriétaire du compte
     */
    public CompteInvestissement(Client proprietaire) {
        super(proprietaire);
    }

    // ================================================================
    // Implémentation des méthodes abstraites
    // ================================================================

    @Override
    public String getType() {
        return "Compte Investissement";
    }

    /**
     * Calcule et applique les intérêts mensuels.
     * Formule : solde actuel × (5% / 12 mois)
     * 
     * @return Montant des intérêts appliqués
     */
    @Override
    public double calculerInteret() {
        double interet = getSolde() * (TAUX_INTERET_ANNUEL / 12);
        setSolde(getSolde() + interet);
        ajouterAuHistorique(String.format("Rendement mensuel (5%% annuel) : +%.2f€", interet));
        return interet;
    }

    /** Déduit les frais de gestion mensuels */
    @Override
    public void appliquerFrais() throws MontantInvalidException {
        if (getSolde() < FRAIS_MENSUELS) {
            throw new MontantInvalidException("Solde insuffisant pour les frais de gestion");
        }
        setSolde(getSolde() - FRAIS_MENSUELS);
        ajouterAuHistorique(String.format("Frais de gestion : -%.2f€", FRAIS_MENSUELS));
    }
}
