package exceptions;
/** Lancée quand le solde est trop bas pour effectuer une opération. */
public class SoldeInsufficientException extends BanqueException {
    public SoldeInsufficientException(String message) { super(message, "E001"); }
}
