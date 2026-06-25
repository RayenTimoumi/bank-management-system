package exceptions;
public class DateInvalideException extends BanqueException {
    public DateInvalideException(String message) { super(message, "E007"); }
}
