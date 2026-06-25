package exceptions;
/** Lancée quand un compte ou une ressource demandée est introuvable. */
public class CompteInexistantException extends BanqueException {
    public CompteInexistantException(String message) { super(message, "E003"); }
}
