package pe.edu.utec.manka.event;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class DishCookedEvent extends ApplicationEvent {
    private final Long historyId;
    private final String userEmail;
    private final String userFirstName;
    private final Long dishId;
    private final String dishName;
    private final LocalDateTime cookedAt;

    public DishCookedEvent(Object source,
                           Long historyId,
                           String userEmail,
                           String userFirstName,
                           Long dishId,
                           String dishName,
                           LocalDateTime cookedAt) {
        super(source);
        this.historyId = historyId;
        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.dishId = dishId;
        this.dishName = dishName;
        this.cookedAt = cookedAt;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public Long getDishId() {
        return dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public LocalDateTime getCookedAt() {
        return cookedAt;
    }
}
