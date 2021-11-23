package crushers;

import java.util.ArrayList;

public class Bank {

    private ArrayList<User> users;
    // private ArrayList<Clerk> clerks;
    // private ArrayList<Manager> managers;

    private String name;
    private String logo;
    private String details;
    //Image icon;


    public Bank() {
        this.name = "";
        this.logo = "";
        this.details = "";
        this.users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }
    public void registerUser(User user) {
        this.users.add(user);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

