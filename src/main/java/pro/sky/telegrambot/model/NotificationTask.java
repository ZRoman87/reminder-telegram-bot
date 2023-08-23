package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;
@Entity
public class NotificationTask {

    @Id
    @GeneratedValue
    private Integer id;
    private Long chatId;
    private String text;
    private LocalDateTime scheduledDispatchDateTime;

    public NotificationTask(Integer id, Long chatId, String text, LocalDateTime scheduledDispatchDateTime) {
        this.id = id;
        this.chatId = chatId;
        this.text = text;
        this.scheduledDispatchDateTime = scheduledDispatchDateTime;
    }

    public NotificationTask() {
    }

    public Integer getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getScheduledDispatchDateTime() {
        return scheduledDispatchDateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setScheduledDispatchDateTime(LocalDateTime scheduledDispatchDateTime) {
        this.scheduledDispatchDateTime = scheduledDispatchDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationTask)) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(text, that.text) && Objects.equals(scheduledDispatchDateTime, that.scheduledDispatchDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, text, scheduledDispatchDateTime);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", text='" + text + '\'' +
                ", scheduledDispatchDateTime=" + scheduledDispatchDateTime +
                '}';
    }
}
