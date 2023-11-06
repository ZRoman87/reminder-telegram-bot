package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TelegramBotService {

    private Logger logger = LoggerFactory.getLogger(TelegramBotService.class);
    private final TelegramBot telegramBot;

    public TelegramBotService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendMassage(long chatId, String text) {
        SendResponse sendResponse = telegramBot.execute(new SendMessage(chatId, text));
        if (!sendResponse.isOk()) {
            logger.error("Message send was failed due to: " + sendResponse.description());
        }
    }
}
