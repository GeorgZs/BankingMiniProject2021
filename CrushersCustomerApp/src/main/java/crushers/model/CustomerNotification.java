package crushers.model;

import java.time.LocalDateTime;

public class CustomerNotification {

    private String notification;
    private LocalDateTime time;
    private Customer targetCustomer;
    
    public CustomerNotification(String notification, Customer targetCustomer){
        this.notification = notification;
        this.time = LocalDateTime.now();
        this.targetCustomer = targetCustomer;
    }

    public CustomerNotification(){
    }
    
    public String getNotification(){
        return this.notification;
    }
    public LocalDateTime getTime(){
        return this.time;
    }
    public Customer getTargetCustomer(){
        return this.targetCustomer;
    }
    public void setNotification(String notification){
        this.notification = notification;
    }
    public void setTime(LocalDateTime time){
        this.time = time;
    }
    public void setTargetCustomer(Customer targetCustomer){
        this.targetCustomer = targetCustomer;
    }
}
