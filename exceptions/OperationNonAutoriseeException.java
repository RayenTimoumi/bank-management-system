package exceptions;
/** Lancée quand une opération n'est pas permise dans ce contexte. */
public class OperationNonAutoriseeException extends BanqueException {
    public OperationNonAutoriseeException(String message) { super(message, "E005"); }
}
