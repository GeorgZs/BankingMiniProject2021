package crushers.models.exchangeInformation;

import crushers.models.users.Customer;

public class CustomerNotification extends ManagerNotification{
    private Customer targetCustomer;

    public CustomerNotification(){
        //for jackson
    }

    public CustomerNotification(String notification, Customer customer){
        super(notification);
        this.targetCustomer = customer;
    }

    public Customer getTargetCustomer() {
        return targetCustomer;
    }

    public void setTargetCustomer(Customer targetCustomer) {
        this.targetCustomer = targetCustomer;
    }
}
