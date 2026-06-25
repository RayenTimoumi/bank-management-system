package modeles;

import exceptions.MontantInvalidException;

/**
 * ============================================================
 * Dépôt : Ajoute de l'argent sur un compte
 * ============================================================
 * 
 * Un dépôt est une transaction simple :
 * - On spécifie un montant
 * - On spécifie le compte qui reçoit l'argent
 * - On appelle executer() pour ajouter l'argent au solde
 * 
 * Exceptions possibles :
 * - MontantInvalidException : montant négatif ou zéro
 */
public class Depot extends Transaction {

    private final Compte compte; // Le compte qui REÇOIT l'argent

    /**
     * Crée un dépôt.
     * 
     * @param montant    La somme à déposer (doit être > 0)
     * @param compte     Le compte qui reçoit l'argent
     * @param description Raison du dépôt (ex: "Salaire", "Virement client")
     */
    public Depot(double montant, Compte compte, String description) {
        super(montant, description);
        this.compte = compte;
    }

    @Override
    public void executer() throws MontantInvalidException {
        // Dépose le montant sur le compte (ajoute au solde)
        compte.deposer(getMontant());
        // Marque cette transaction comme exécutée avec succès
        marquerExecutee();
    }

    @Override
    public String getType() { return "Dépôt"; }

    // Getter pour accéder au compte concerné
    public Compte getCompte() { return compte; }
}
