package exceptions;

import java.time.LocalDateTime;

/**
 * Exception de base pour toutes les erreurs métier de la banque.
 * Toutes les autres exceptions héritent de celle-ci.
 */
public abstract class BanqueException extends Exception {

    private final String        codeErreur;
    private final LocalDateTime date;

    public BanqueException(String message, String codeErreur) {
        super(message);
        this.codeErreur = codeErreur;
        this.date       = LocalDateTime.now();
    }

    public String        getCodeErreur() { return codeErreur; }
    public LocalDateTime getDate()       { return date; }

    @Override
    public String toString() {
        return "[" + codeErreur + "] " + getMessage();
    }
}
