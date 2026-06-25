package modeles;

import exceptions.MontantInvalidException;
import exceptions.SoldeInsufficientException;

/**
 * ============================================================
 * Retrait : Enlève de l'argent d'un compte
 * ============================================================
 * 
 * Un retrait est une transaction simple :
 * - On spécifie un montant
 * - On spécifie le compte débité
 * - On appelle executer() pour enlever l'argent du solde
 * 
 * Exceptions possibles :
 * - MontantInvalidException : montant négatif ou zéro
 * - SoldeInsufficientException : solde insuffisant pour retirer
 */
public class Retrait extends Transaction {

    private final Compte compte; // Le compte DÉBITÉ

    /**
     * Crée un retrait.
     * 
     * @param montant    La somme à retirer (doit être > 0)
     * @param compte     Le compte débité
     * @param description Raison du retrait (ex: "Retrait GAB", "Paiement")
     */
    public Retrait(double montant, Compte compte, String description) {
        super(montant, description);
        this.compte = compte;
    }

    @Override
    public void executer() throws MontantInvalidException, SoldeInsufficientException {
        // Retire le montant du compte (enlève du solde)
        compte.retirer(getMontant());
        // Marque cette transaction comme exécutée avec succès
        marquerExecutee();
    }

    @Override
    public String getType() { return "Retrait"; }

    // Getter pour accéder au compte concerné
    public Compte getCompte() { return compte; }
}
