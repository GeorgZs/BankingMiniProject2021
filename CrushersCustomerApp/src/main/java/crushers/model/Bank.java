package crushers.model;

import java.util.ArrayList;

public class Bank {
    
    private ArrayList<Customer> customers;
    private String name;
    private String details;
    private Manager manager;
    //Image icon;

    public Bank(String name, String details, Manager manager){
        this.name = name;
        this.details = details;
        this.manager = manager;
        this.customers = new ArrayList<Customer>();
    }

    public String toString(){
        return this.name;
    }
    public String getName(){
        return this.name;
    }
    public String getDetails(){
        return this.details;
    }
    public Manager getManager(){
        return this.manager;
    }
    public ArrayList<Customer> getCustomers(){
        return this.customers;
    }

    public void registerCustomer(Customer customer){
        this.customers.add(customer);
    }

}
