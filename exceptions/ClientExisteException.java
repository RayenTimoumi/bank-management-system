package exceptions;
/** Lancée quand on tente d'ajouter un utilisateur déjà enregistré. */
public class ClientExisteException extends BanqueException {
    public ClientExisteException(String message) { super(message, "E002"); }
}
