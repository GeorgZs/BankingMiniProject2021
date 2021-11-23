package crushers.model;

public class Contact {
    private int id;
    private String name;
    private PaymentAccount account;
    private String description;

    public Contact(int id, String name, PaymentAccount account, String description) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.description = description;
    }
}
