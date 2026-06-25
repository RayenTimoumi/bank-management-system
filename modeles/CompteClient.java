package modeles;

import exceptions.MontantInvalidException;

/**
 * ============================================================
 * Compte Standard Client
 * ============================================================
 * 
 * Compte bancaire standard pour un client.
 * C'est le compte de base, généralement avec un solde positif.
 * 
 * Caractéristiques :
 * - Taux d'intérêt : 1% annuel (appliqué mensuellement)
 * - Frais mensuels : 3€
 * - Pas de solde minimum requis
 * - Pas de découvert autorisé
 * 
 * Mécaniques :
 * - Chaque mois : intérêts = (solde × 1%) / 12
 * - Chaque mois : frais 3€ prélevés
 * 
 * Exemple :
 * - Solde initial : 1000€
 * - Mois 1 : +0.83€ d'intérêts, -3€ de frais = 997.83€
 */
public class CompteClient extends Compte {

    private static final double TAUX_INTERET_ANNUEL = 0.01;  // 1% par an (faible mais sûr)
    private static final double FRAIS_MENSUELS      = 3.0;   // 3€ de frais/mois

    // ================================================================
    // Constructeur
    // ================================================================
    /**
     * Crée un compte client.
     * 
     * @param proprietaire Le client propriétaire du compte
     */
    public CompteClient(Client proprietaire) {
        super(proprietaire);
    }

    // ================================================================
    // Implémentation des méthodes abstraites
    // ================================================================

    @Override
    public String getType() {
        return "Compte Client";
    }

    /**
     * Calcule et applique les intérêts mensuels.
     * Formule : solde actuel × (1% / 12 mois)
     * 
     * @return Montant des intérêts appliqués
     */
    @Override
    public double calculerInteret() {
        // Calcule 1/12 du taux annuel
        double interet = getSolde() * (TAUX_INTERET_ANNUEL / 12);
        // Ajoute au solde
        setSolde(getSolde() + interet);
        // Enregistre dans l'historique
        ajouterAuHistorique(String.format("Intérêts appliqés: +%.2f€ (1%% annuel)", interet));
        return interet;
    }

    /** Déduit les frais fixes mensuels */
    @Override
    public void appliquerFrais() throws MontantInvalidException {
        if (getSolde() < FRAIS_MENSUELS) {
            throw new MontantInvalidException("Solde insuffisant pour les frais mensuels");
        }
        setSolde(getSolde() - FRAIS_MENSUELS);
        ajouterAuHistorique(String.format("Frais mensuels : -%.2f€", FRAIS_MENSUELS));
    }
}
