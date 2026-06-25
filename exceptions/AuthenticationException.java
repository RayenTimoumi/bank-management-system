package exceptions;
public class AuthenticationException extends BanqueException {
    public AuthenticationException(String message) { super(message, "E005"); }
}
