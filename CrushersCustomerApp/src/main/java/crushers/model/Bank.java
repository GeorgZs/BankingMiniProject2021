package crushers.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// @JsonIgnoreProperties(ignoreUnknown = true, value={"logo", "customers", "name", "details", "manager"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bank {
    
    private int id;
    private String logo;

    private ArrayList<Customer> customers;
    private String name;
    private String details;
    private Manager manager;
    //Image icon;

    public Bank(String name, String details, Manager manager){ // http post constructor
        this.name = name;
        this.details = details;
        this.manager = manager;
        this.customers = new ArrayList<Customer>();
    }
    public Bank(int id, String name, String logo){
        this.id = id;
        this.name = name;
        this.logo = logo;
    } // http get constructor
    public Bank(int id){
        this.id = id;
    }
    public Bank(){ // for http

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
    public int getId(){
        return this.id;
    }
    public String getLogo(){
        return this.logo;
    }

    public void registerCustomer(Customer customer){
        this.customers.add(customer);
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Bank){
            return this.id == ((Bank)obj).getId();
        }else{
            return false;
        }
    }

}
