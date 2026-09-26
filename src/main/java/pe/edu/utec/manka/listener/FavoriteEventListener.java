package pe.edu.utec.manka.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pe.edu.utec.manka.event.FavoriteAddedEvent;
import pe.edu.utec.manka.service.ActivityLogService;

@Component
public class FavoriteEventListener {
    private final ActivityLogService activityLogService;

    public FavoriteEventListener(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @Async("mankaTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onFavoriteAdded(FavoriteAddedEvent event) {
        activityLogService.logFavoriteAdded(
                event.getUserEmail(),
                event.getDishId(),
                event.getDishName()
        );
    }
}
