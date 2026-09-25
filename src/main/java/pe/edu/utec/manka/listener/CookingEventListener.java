package pe.edu.utec.manka.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pe.edu.utec.manka.event.DishCookedEvent;
import pe.edu.utec.manka.service.ActivityLogService;
import pe.edu.utec.manka.service.EmailService;

@Component
public class CookingEventListener {
    private final ActivityLogService activityLogService;
    private final EmailService emailService;

    public CookingEventListener(ActivityLogService activityLogService, EmailService emailService) {
        this.activityLogService = activityLogService;
        this.emailService = emailService;
    }

    @Async("mankaTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDishCooked(DishCookedEvent event) {
        activityLogService.logDishCooked(
                event.getUserEmail(),
                event.getDishId(),
                event.getDishName(),
                event.getCookedAt()
        );
        emailService.sendDishCookedEmail(
                event.getUserEmail(),
                event.getUserFirstName(),
                event.getDishName(),
                event.getCookedAt()
        );
    }
}
