package pro.sky.telegrambot.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.TelegramBotService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Component
public class NotificationScheduler {

    public final NotificationTaskRepository notificationTaskRepository;
    private final TelegramBotService telegramBotService;

    public NotificationScheduler(NotificationTaskRepository notificationTaskRepository, TelegramBotService telegramBotService) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBotService = telegramBotService;
    }

    //@Scheduled(cron = "0 0/1 * * * *")
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 1)
    @Transactional(readOnly = true)
    public void run() {
        notificationTaskRepository.findTasks(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
        ).forEach(notificationTask ->telegramBotService.sendMassage(
                notificationTask.getChatId(),
                "Вы просили напомнить: " + notificationTask.getText()
        ) );

    }
}
