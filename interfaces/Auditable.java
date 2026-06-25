package interfaces;

import java.util.List;

/**
 * ============================================================
 * Interface Auditable
 * ============================================================
 * 
 * Contrat pour les classes qui doivent enregistrer un historique
 * de leurs actions (audit trail).
 * 
 * Implémentée par :
 * - Employee (enregistre les actions effectuées)
 * 
 * Objectif :
 * - Traçabilité : Savoir qui a fait quoi et quand
 * - Conformité : Preuves des opérations effectuées
 * - Dépannage : Identifier les problèmes en analysant l'historique
 */
public interface Auditable {
    
    /**
     * Enregistre une action dans l'historique.
     * 
     * @param action Description de l'action effectuée
     *               Exemple : "Nouveau client créé", "Transaction effectuée"
     */
    void enregistrerAction(String action);
    
    /**
     * Récupère tout l'historique des actions enregistrées.
     * 
     * @return Liste des actions (chacune avec timestamp)
     *         Exemple : "[21/05/2026 14:30:45] Nouveau client créé"
     */
    List<String> obtenirHistorique();
}
