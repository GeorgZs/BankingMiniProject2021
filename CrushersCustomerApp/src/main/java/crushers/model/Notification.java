package crushers.model;

import java.time.LocalDateTime;

public class Notification {

    private String notification;
    private LocalDateTime time;
    
    public Notification(String notification, LocalDateTime time){
        this.notification = notification;
        this.time = time;
    }

    public Notification(){
    }
    
    public String getNotification(){
        return this.notification;
    }
    public LocalDateTime getTime(){
        return this.time;
    }
}
