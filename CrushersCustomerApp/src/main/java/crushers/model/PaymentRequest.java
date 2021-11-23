package crushers.model;

import java.util.Date;

public class PaymentRequest {
    
    private String ID;
    private PaymentAccount fromAccount;
    private String amount;
    private Date date;
    private String description;

    public PaymentRequest(String ID, PaymentAccount fromAccount, String amount, Date date, String description) {
        this.ID = ID;
        this.fromAccount = fromAccount;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }
}
