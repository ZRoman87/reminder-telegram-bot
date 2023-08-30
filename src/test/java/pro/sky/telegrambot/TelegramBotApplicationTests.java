package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotApplicationTests {

    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private User user;
    @Mock
    private SendResponse sendResponse;
    @Mock
    private SendMessage sendMessage;
    @Mock
    private TelegramBot telegramBotMock;
    @Mock
    private NotificationTaskRepository notificationTaskRepositoryMock;
    @InjectMocks
    private TelegramBotUpdatesListener out;
    @Test
    public void processTestStartMessage(){

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        when(message.from()).thenReturn(user);
        when(message.text()).thenReturn("/start");
        when(user.firstName()).thenReturn("Roman");
        when(telegramBotMock.execute(any())).thenReturn(sendResponse);

        assertEquals(-1,out.process(List.of(update)));
        verify(telegramBotMock, atLeastOnce()).execute(any());

    }
    @Test
    public void processTestTaskMessage(){

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);

        when(message.text()).thenReturn("01.01.2022 20:00 Сделать домашнюю работу");

        assertEquals(-1,out.process(List.of(update)));
        verify(notificationTaskRepositoryMock, atLeastOnce()).save(any());

    }
    @Test
    public void runTest(){
        NotificationTask task = new NotificationTask(1L,"Сделай домашнюю работу", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        out.run();

        when(notificationTaskRepositoryMock.findTasks(any())).thenReturn(List.of(task));

        assertEquals(List.of(task),notificationTaskRepositoryMock.findTasks(any()));
        verify(notificationTaskRepositoryMock, atLeastOnce()).findTasks(any());
    }
}
