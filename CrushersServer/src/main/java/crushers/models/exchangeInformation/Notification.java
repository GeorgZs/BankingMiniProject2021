package crushers.models.exchangeInformation;

import java.time.LocalDateTime;

public class Notification {
    private String notification;
    private String time;

    public Notification(){}

    public Notification(String notification){
        this.notification = notification;
        this.time = LocalDateTime.now().toString();
    }

    public String getNotification() {
        return notification;
    }

    public String getTime() {
        return time;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
