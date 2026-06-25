package modeles;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe abstraite parente pour tous les utilisateurs du système.
 * Un utilisateur peut être un Client ou un Employé.
 *
 * Héritage : Employee et Client étendent cette classe.
 */
public abstract class User implements Serializable {

    // Identifiant unique généré automatiquement
    private final String id;

    // Informations communes à tout utilisateur
    private String nom;
    private String email;

    // ----------------------------------------------------------------
    // Constructeur — protégé car on ne peut pas instancier User direct
    // ----------------------------------------------------------------
    protected User(String nom, String email) {
        this.id    = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.nom   = nom;
        this.email = email;
    }

    // ----------------------------------------------------------------
    // Méthode abstraite — chaque sous-classe précise son type
    // ----------------------------------------------------------------
    public abstract String getType();

    // ----------------------------------------------------------------
    // Getters / Setters
    // ----------------------------------------------------------------
    public String getId()    { return id; }
    public String getNom()   { return nom; }
    public String getEmail() { return email; }

    public void setNom(String nom)     { this.nom   = nom; }
    public void setEmail(String email) { this.email = email; }

    // Deux utilisateurs sont égaux s'ils ont le même email (unicité)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return email.equalsIgnoreCase(((User) o).email);
    }

    @Override
    public int hashCode() { return email.toLowerCase().hashCode(); }

    @Override
    public String toString() {
        return getType() + "[" + id + "] " + nom + " <" + email + ">";
    }
}
