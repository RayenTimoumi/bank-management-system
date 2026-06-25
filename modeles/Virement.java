package modeles;

import exceptions.MontantInvalidException;
import exceptions.SoldeInsufficientException;

/**
 * ============================================================
 * Virement : Transfère de l'argent entre deux comptes
 * ============================================================
 * 
 * Un virement est une opération à deux comptes :
 * - Un compte SOURCE (celui qui envoie l'argent)
 * - Un compte DESTINATION (celui qui reçoit l'argent)
 * - Le montant tranféré
 * 
 * Vérifications :
 * - Les deux comptes doivent être différents
 * - Le compte source doit avoir assez d'argent
 * 
 * Exceptions possibles :
 * - MontantInvalidException : montant invalide ou comptes identiques
 * - SoldeInsufficientException : solde source insuffisant
 */
public class Virement extends Transaction {

    private final Compte source;      // Compte qui ENVOIE l'argent
    private final Compte destination; // Compte qui REÇOIT l'argent

    /**
     * Crée un virement.
     * 
     * @param montant    La somme à transférer (doit être > 0)
     * @param source     Le compte débité (celui qui envoie)
     * @param destination Le compte credité (celui qui reçoit)
     * @param description Raison du virement (ex: "Paiement loyer", "Transfer")
     */
    public Virement(double montant, Compte source, Compte destination, String description) {
        super(montant, description);
        this.source      = source;
        this.destination = destination;
    }

    @Override
    public void executer() throws MontantInvalidException, SoldeInsufficientException {
        // Vérifie qu'on ne vire pas vers le même compte (inutile)
        if (source.getNumeroCompte().equals(destination.getNumeroCompte())) {
            throw new MontantInvalidException("Source et destination identiques");
        }
        // Effectue le virement (source à destination)
        source.virerVers(destination, getMontant());
        // Marque cette transaction comme exécutée avec succès
        marquerExecutee();
    }

    @Override
    public String getType() { return "Virement"; }

    // Getters pour accéder aux comptes concernés
    public Compte getSource()      { return source; }
    public Compte getDestination() { return destination; }
}
