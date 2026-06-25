package interfaces;

/**
 * ============================================================
 * Interface Assurable
 * ============================================================
 * 
 * Contrat pour les comptes qui peuvent être assurés
 * contre les risques (perte, vol, etc.).
 * 
 * Implémentée par :
 * - CompteInvestissement : Assurance sur les placements
 * - CompteClient : Assurance optionnelle
 * 
 * Concept :
 * - Un compte peut être assuré ou non
 * - L'assurance a un coût : la prime
 * - Les primes sont prélevées périodiquement (mensuellement ou annuellement)
 */
public interface Assurable {
    
    /**
     * Souscrit une assurance pour ce compte.
     * Une fois assuré, des primes seront facturées régulièrement.
     */
    void souscrireAssurance();
    
    /**
     * Annule l'assurance pour ce compte.
     * Plus aucune prime ne sera facturée.
     */
    void resilerAssurance();
    
    /**
     * Calcule le montant de la prime d'assurance due.
     * Si non assuré, retourne 0.0 (pas de frais).
     * 
     * @return Montant de la prime en euros
     */
    double calculerPrimeAssurance();
    
    /**
     * Vérifie si ce compte est actuellement assuré.
     * 
     * @return true si assuré, false sinon
     */
    boolean estAssure();
}
