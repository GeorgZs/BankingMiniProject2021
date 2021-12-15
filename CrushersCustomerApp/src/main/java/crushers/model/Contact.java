package crushers.model;

public class Contact {
    private int id;
    private int accountId;
    private String name;
    private PaymentAccount account;
    private String description;

    public Contact(int id, String name, PaymentAccount account, String description) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.description = description;
        this.accountId = this.account.getId();
    }
    public Contact(){

    }

    @Override
    public String toString(){
        // return String.format("Contact %s (%d): %s", this.name, this.id, this.description);
        return "Contact " + this.name + " (" + this.id + "): " + this.description + "\nAccount: "+this.account.getId();
    }
    public PaymentAccount getContactAccount() {
        return this.account;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
    public void setAccount(PaymentAccount account){
        this.account = account;
    }
    public PaymentAccount getAccount(){
        return this.account;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getAccountId(){
        return this.account.getId();
    }
    public void setAccountId(int accountId){
        this.accountId = accountId;
    }
}
