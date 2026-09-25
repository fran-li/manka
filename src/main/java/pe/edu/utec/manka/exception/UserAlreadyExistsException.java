package pe.edu.utec.manka.exception;

public class UserAlreadyExistsException extends DuplicateResourceException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
