package modeles;

/**
 * Représente un client de la banque.
 * Hérite de User et ajoute des informations de contact.
 */
public class Client extends User {

    private String telephone;
    private String adresse;
    private String ville;

    // ----------------------------------------------------------------
    // Constructeurs — surcharge pour plus de flexibilité
    // ----------------------------------------------------------------

    // Constructeur minimal (nom + email suffisent)
    public Client(String nom, String email) {
        super(nom, email);
    }

    // Constructeur avec téléphone
    public Client(String nom, String email, String telephone) {
        super(nom, email);
        this.telephone = telephone;
    }

    // Constructeur complet
    public Client(String nom, String email, String telephone, String adresse, String ville) {
        super(nom, email);
        this.telephone = telephone;
        this.adresse   = adresse;
        this.ville     = ville;
    }

    // ----------------------------------------------------------------
    // Méthode abstraite héritée de User
    // ----------------------------------------------------------------
    @Override
    public String getType() { return "Client"; }

    // ----------------------------------------------------------------
    // Getters / Setters
    // ----------------------------------------------------------------
    public String getTelephone() { return telephone; }
    public String getAdresse()   { return adresse; }
    public String getVille()     { return ville; }

    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setAdresse(String adresse)     { this.adresse   = adresse; }
    public void setVille(String ville)         { this.ville     = ville; }
}
