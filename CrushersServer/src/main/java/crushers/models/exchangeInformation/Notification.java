package crushers.models.exchangeInformation;

import java.time.LocalDateTime;

public class Notification {
    private String notification;
    private LocalDateTime time;

    public Notification(){
        //for jackson
    }

    public Notification(String notification){
        this.notification = notification;
        this.time = LocalDateTime.now();
    }

    public String getNotification() {
        return notification;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
