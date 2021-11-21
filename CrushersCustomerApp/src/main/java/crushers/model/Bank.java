package crushers.model;

import java.util.ArrayList;

public class Bank {
    
    private ArrayList<User> users;
    // private ArrayList<Clerk> clerks;
    // private ArrayList<Manager> managers;

    String name;
    //Image icon;

    public Bank(String name){
        this.name = name;
        this.users = new ArrayList<User>();
    }
    
    public ArrayList<User> getUsers(){
        return this.users;
    }
    public void registerUser(User user){
        this.users.add(user);
    }

}
