package modeles;

import exceptions.MontantInvalidException;
import exceptions.SoldeInsufficientException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Classe abstraite parente pour tous les types de comptes.
 * On ne peut pas instancier Compte directement.
 *
 * Héritage : CompteClient, CompteEmployee, CompteInvestissement étendent cette classe.
 */
public abstract class Compte implements Serializable {

    // Numéro unique du compte (généré automatiquement)
    private final String   numeroCompte;

    // Le propriétaire est un User (Client ou Employee)
    private final User     proprietaire;

    // Date d'ouverture du compte
    private final LocalDate dateCreation;

    // Solde courant du compte
    private double solde;

    // Historique des opérations sur ce compte
    private final List<String> historique;

    // ----------------------------------------------------------------
    // Constructeur — protégé car seules les sous-classes l'utilisent
    // ----------------------------------------------------------------
    protected Compte(User proprietaire) {
        this.numeroCompte = "CPT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.proprietaire = proprietaire;
        this.solde        = 0.0;
        this.dateCreation = LocalDate.now();
        this.historique   = new ArrayList<>();
    }

    // ----------------------------------------------------------------
    // Méthodes abstraites — chaque sous-classe les implémente à sa façon
    // ----------------------------------------------------------------

    /** Retourne le type du compte (ex: "Compte Client") */
    public abstract String getType();

    /** Calcule et applique les intérêts mensuels */
    public abstract double calculerInteret();

    /** Applique les frais mensuels de gestion */
    public abstract void appliquerFrais() throws MontantInvalidException;

    // ----------------------------------------------------------------
    // Opérations bancaires communes (non redéfinissables = final)
    // ----------------------------------------------------------------

    /** Dépose un montant sur le compte */
    public final void deposer(double montant) throws MontantInvalidException {
        if (montant <= 0) {
            throw new MontantInvalidException("Le montant doit être positif");
        }
        solde += montant;
        ajouterAuHistorique(String.format("Dépôt : +%.2f€  →  Solde : %.2f€", montant, solde));
    }

    /** Retire un montant du compte */
    public final void retirer(double montant) throws MontantInvalidException, SoldeInsufficientException {
        if (montant <= 0) {
            throw new MontantInvalidException("Le montant doit être positif");
        }
        if (montant > solde) {
            throw new SoldeInsufficientException(
                String.format("Solde insuffisant : %.2f€ disponible, %.2f€ demandé", solde, montant));
        }
        solde -= montant;
        ajouterAuHistorique(String.format("Retrait : -%.2f€  →  Solde : %.2f€", montant, solde));
    }

    /** Vire un montant vers un autre compte */
    public final void virerVers(Compte destination, double montant)
            throws MontantInvalidException, SoldeInsufficientException {
        retirer(montant);
        destination.deposer(montant);
        ajouterAuHistorique(String.format("Virement vers %s : -%.2f€", destination.getNumeroCompte(), montant));
        destination.ajouterAuHistorique(String.format("Virement reçu de %s : +%.2f€", numeroCompte, montant));
    }

    // ----------------------------------------------------------------
    // Historique interne
    // ----------------------------------------------------------------
    protected void ajouterAuHistorique(String message) {
        historique.add("[" + LocalDate.now() + "] " + message);
    }

    public List<String> getHistorique() {
        return new ArrayList<>(historique); // copie défensive
    }

    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------
    public String  getNumeroCompte() { return numeroCompte; }
    public User    getProprietaire() { return proprietaire; }
    public double  getSolde()        { return solde; }
    public LocalDate getDateCreation(){ return dateCreation; }

    // Setter protégé pour que les sous-classes puissent modifier le solde
    protected void setSolde(double solde) { this.solde = solde; }

    @Override
    public String toString() {
        return String.format("%s [%s] — %.2f€ — %s", getType(), numeroCompte, solde, proprietaire.getNom());
    }
}
