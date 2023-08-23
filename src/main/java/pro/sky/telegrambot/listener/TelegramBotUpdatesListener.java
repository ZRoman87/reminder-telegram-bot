package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    //@Autowired
    private TelegramBot telegramBot;
    private final NotificationTaskRepository notificationTaskRepository;

    public TelegramBotUpdatesListener(NotificationTaskRepository notificationTaskRepository, TelegramBot telegramBot) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            if (update.message().text().equals("/start")) {
                SendMessage message = new SendMessage(update.message().chat().id(), "Hello, " + update.message().from().firstName());
                telegramBot.execute(message);
            }

            Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
            Matcher matcher = pattern.matcher(update.message().text());
            if (matcher.matches()) {
                String date = matcher.group(1);
                String text = matcher.group(3);

                NotificationTask task = new NotificationTask();
                task.setScheduledDispatchDateTime(LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                task.setText(text);
                task.setChatId(update.message().chat().id());

                notificationTaskRepository.save(task);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        List<NotificationTask> list = notificationTaskRepository.findTasks(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        list.forEach(l -> telegramBot.execute(new SendMessage(l.getChatId(), l.getText())));
    }


}
