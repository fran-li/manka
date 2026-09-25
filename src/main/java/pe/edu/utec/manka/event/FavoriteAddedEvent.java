package pe.edu.utec.manka.event;

import org.springframework.context.ApplicationEvent;

public class FavoriteAddedEvent extends ApplicationEvent {
    private final Long userId;
    private final String userEmail;
    private final Long dishId;
    private final String dishName;

    public FavoriteAddedEvent(Object source, Long userId, String userEmail, Long dishId, String dishName) {
        super(source);
        this.userId = userId;
        this.userEmail = userEmail;
        this.dishId = dishId;
        this.dishName = dishName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getDishId() {
        return dishId;
    }

    public String getDishName() {
        return dishName;
    }
}
