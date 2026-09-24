package pe.edu.utec.manka.event;

import org.springframework.context.ApplicationEvent;

public class UserRegisteredEvent extends ApplicationEvent {
    private final Long userId;
    private final String email;
    private final String firstName;

    public UserRegisteredEvent(Object source, Long userId, String email, String firstName) {
        super(source);
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }
}
