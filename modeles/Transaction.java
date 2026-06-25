package modeles;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ============================================================
 * Classe abstraite : Transaction
 * ============================================================
 * 
 * Représente une opération bancaire abstraite.
 * 
 * Une transaction a toujours :
 * - Un montant (somme d'argent)
 * - Une date (création)
 * - Une description (raison de l'opération)
 * - Un statut : exécutée ou non
 * 
 * SOUS-CLASSES :
 * - Depot : Ajoute de l'argent sur un compte
 * - Retrait : Enlève de l'argent d'un compte
 * - Virement : Transfère de l'argent entre deux comptes
 * 
 * PATTERN : Template Method
 * - executer() est implémentée dans chaque sous-classe
 * - Le comportement de chaque transaction dépend de son type
 */
public abstract class Transaction{

    private final String        id;             // Identifiant unique (ex: TXN-ABC123)
    private final double        montant;        // Somme d'argent concernée
    private final LocalDateTime date;           // Date/heure de création
    private final String        description;    // Explication de la transaction

    // Indique si la transaction a bien été exécutée en base
    private boolean executee;

    // ================================================================
    // Constructeur protégé (appelé uniquement par les sous-classes)
    // ================================================================
    protected Transaction(double montant, String description) {
        // Génère un ID unique pour cette transaction
        this.id          = "TXN-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.montant     = montant;
        this.date        = LocalDateTime.now();  // Date/heure actuelle
        this.description = description;
        this.executee    = false;  // Au départ, la transaction n'est pas exécutée
    }

    // ================================================================
    // Méthodes abstraites (implémentées par chaque sous-classe)
    // ================================================================

    /** 
     * Exécute la transaction (dépôt, retrait ou virement).
     * Levé des exceptions si l'opération échoue.
     */
    public abstract void executer() throws Exception;

    /** 
     * Retourne le type en texte lisible pour l'affichage.
     * Exemple : "Dépôt", "Retrait", "Virement"
     */
    public abstract String getType();

    // ================================================================
    // Getters et utilitaires
    // ================================================================
    public String        getId()          { return id; }
    public double        getMontant()     { return montant; }
    public LocalDateTime getDate()        { return date; }
    public String        getDescription() { return description; }
    public boolean       isExecutee()     { return executee; }
    
    /** Marque cette transaction comme exécutée avec succès */
    protected void marquerExecutee() { this.executee = true; }

    @Override
    public String toString() {
        return String.format("[%s] %s — %.2f€ — %s — %s",
            id, getType(), montant,
            date.toLocalDate(),
            executee ? "✔ Exécutée" : "En attente");
    }
}
