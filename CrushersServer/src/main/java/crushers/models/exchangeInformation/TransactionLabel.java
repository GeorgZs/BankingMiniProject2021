package crushers.models.exchangeInformation;

public class TransactionLabel {
    private String label;
    private Transaction transaction;

    public TransactionLabel(){}

    public TransactionLabel(String label, Transaction transaction){
        this.label = label;
        this.transaction = transaction;
    }

    public String getLabel() {
        return label;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
