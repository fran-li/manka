package pe.edu.utec.manka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActivityLogService {
    private static final Logger log = LoggerFactory.getLogger(ActivityLogService.class);

    public void logDishCooked(String email, Long dishId, String dishName, LocalDateTime cookedAt) {
        log.info("ASYNC ACTIVITY - user={} cooked dishId={} dish={} at={}",
                email, dishId, dishName, cookedAt);
    }

    public void logFavoriteAdded(String email, Long dishId, String dishName) {
        log.info("ASYNC ACTIVITY - user={} added favorite dishId={} dish={}",
                email, dishId, dishName);
    }
}
