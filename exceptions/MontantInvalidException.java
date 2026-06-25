package exceptions;
/** Lancée quand un montant est nul, négatif ou incohérent. */
public class MontantInvalidException extends BanqueException {
    public MontantInvalidException(String message) { super(message, "E004"); }
}
