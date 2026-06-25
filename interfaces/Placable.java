package interfaces;

/**
 * ============================================================
 * Interface Placable
 * ============================================================
 * 
 * Contrat pour les comptes qui permettent l'investissement
 * en placements financiers (fonds, titres, etc.).
 * 
 * Implémentée par :
 * - CompteInvestissement : Pour les investissements de clients
 * 
 * Concept :
 * - Un placement est une forme d'investissement
 * - L'argent est "placé" dans des titres financiers
 * - Les placements généréent des rendements (intérêts)
 */
public interface Placable {
    
    /**
     * Investit un montant dans des placements financiers.
     * L'argent est prélevé du solde disponible.
     * 
     * @param montant Le montant à investir (doit être > 0)
     * @throws Exception Si montant invalide ou solde insuffisant
     */
    void investir(double montant) throws Exception;
    
    /**
     * Retire un montant des placements.
     * L'argent récupéré est rajouté au solde disponible.
     * 
     * @param montant Le montant à retirer (doit être > 0)
     * @throws Exception Si montant invalide ou portefeuille insuffisant
     */
    void retirerInvestissement(double montant) throws Exception;
    
    /**
     * Retourne le taux de rendement annuel (ex: 0.04 pour 4%).
     * 
     * @return Rendement exprimé en décimal (0.04 = 4% par an)
     */
    double obtenirRendement();
    
    /**
     * Calcule la valeur totale actuelle du portefeuille d'investissements.
     * 
     * @return Somme de tous les placements (en euros)
     */
    double getValeurPortefeuille();
}
