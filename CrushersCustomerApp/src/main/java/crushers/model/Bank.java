package crushers.model;

import java.util.ArrayList;

public class Bank {
    
    private ArrayList<Customer> customers;
    // private ArrayList<Clerk> clerks;
    // private ArrayList<Manager> managers;

    String name;
    //Image icon;

    public Bank(String name){
        this.name = name;
        this.customers = new ArrayList<Customer>();
    }
    
    public ArrayList<Customer> getCustomers(){
        return this.customers;
    }
    public void registerCustomer(Customer customer){
        this.customers.add(customer);
    }

    public String toString(){
        return this.name;
    }

}
