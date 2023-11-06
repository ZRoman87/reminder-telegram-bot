package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.entity.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    @Query(value = "SELECT * FROM notification_task WHERE scheduled_dispatch_date_time = :scheduled_dispatch_date_time", nativeQuery = true)
    List<NotificationTask> findTasks(@Param("scheduled_dispatch_date_time") LocalDateTime scheduled_dispatch_date_time);

}
