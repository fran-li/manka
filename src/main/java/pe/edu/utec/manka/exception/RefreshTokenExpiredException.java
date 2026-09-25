package pe.edu.utec.manka.exception;

public class RefreshTokenExpiredException extends InvalidTokenException {
    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
