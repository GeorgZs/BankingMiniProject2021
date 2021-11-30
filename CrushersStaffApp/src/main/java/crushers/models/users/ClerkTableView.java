package crushers.models.users;

public class ClerkTableView {
    private int clerkID;
    private String clerkFirstName;
    private String clerkLastName;
    private String clerkEmail;
    private String clerkAddress;

    public ClerkTableView(int clerkID, String clerkFirstName, String clerkLastName, String clerkEmail, String clerkAddress) {
        this.clerkID = clerkID;
        this.clerkFirstName = clerkFirstName;
        this.clerkLastName = clerkLastName;
        this.clerkEmail = clerkEmail;
        this.clerkAddress = clerkAddress;
    }

    public int getClerkID() {
        return clerkID;
    }

    public void setClerkID(int clerkID) {
        this.clerkID = clerkID;
    }

    public String getClerkFirstName() {
        return clerkFirstName;
    }

    public void setClerkFirstName(String clerkFirstName) {
        this.clerkFirstName = clerkFirstName;
    }

    public String getClerkLastName() {
        return clerkLastName;
    }

    public void setClerkLastName(String clerkLastName) {
        this.clerkLastName = clerkLastName;
    }

    public String getClerkEmail() {
        return clerkEmail;
    }

    public void setClerkEmail(String clerkEmail) {
        this.clerkEmail = clerkEmail;
    }

    public String getClerkAddress() {
        return clerkAddress;
    }

    public void setClerkAddress(String clerkAddress) {
        this.clerkAddress = clerkAddress;
    }
}
