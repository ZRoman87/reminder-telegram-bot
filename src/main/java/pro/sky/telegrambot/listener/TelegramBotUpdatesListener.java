package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.NotificationTaskService;
import pro.sky.telegrambot.service.TelegramBotService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static final Pattern PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final TelegramBot telegramBot;
    private final NotificationTaskService notificationTaskService;
    private final TelegramBotService telegramBotService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskService notificationTaskService, TelegramBotService telegramBotService) {
        this.telegramBot = telegramBot;
        this.notificationTaskService = notificationTaskService;
        this.telegramBotService = telegramBotService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            Long chatId = update.message().chat().id();
            Message message = update.message();
            String text = message.text();
            String firstName = message.from().firstName();
            Matcher matcher = PATTERN.matcher(text);
            LocalDateTime dateTime;

            if (message != null && text != null) {

                if (text.equals("/start")) {
                    String messageText = "Hello, " + firstName + "! Для планирования задачи отправьте ее в формате: 01.01.2023 00:00 Сделать задание";
                    telegramBotService.sendMassage(chatId,messageText);
                } else if (matcher.matches() && (dateTime = parse(matcher.group(1))) != null) {
                    notificationTaskService.save(chatId,matcher.group(3),dateTime);
                    telegramBotService.sendMassage(chatId,"Ваша задача запланированна");
                } else {
                    telegramBotService.sendMassage(chatId,"Введены некорректные данные");
                }
            } else {
                telegramBotService.sendMassage(chatId,"Введите команду /start или сообщение для планирования задачи");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    @Nullable
    private LocalDateTime parse(String dateTime){
        try {
            return LocalDateTime.parse(dateTime,DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e){
            return null;
        }
    }


}
