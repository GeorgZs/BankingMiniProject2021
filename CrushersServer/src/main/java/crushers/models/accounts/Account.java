package crushers.models.accounts;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import crushers.models.Bank;
import crushers.models.users.Customer;
import crushers.storage.Storable;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME, 
  include = JsonTypeInfo.As.PROPERTY, 
  property = "type"
)
@JsonSubTypes({ 
  @Type(value = PaymentAccount.class, name = "payment"), 
  @Type(value = SavingsAccount.class, name = "savings") 
})
public abstract class Account implements Storable {
  private int id = -1;
  // temporary bank number generation
  private String number = "SE " + (int)(Math.floor(Math.random() * 9000 + 1000)) + " " + (int)(Math.floor(Math.random() * 90000 + 10000)) + " " + (int)(Math.floor(Math.random() * 9000 + 1000));
  private Bank bank;
  private Customer owner;
  private double balance = 0.0;

  // for Jackson
  public String type;

  // for later, but not now
  // private LinkedHashMap<Time, Transaction> transactions;

  public Account() {
    // empty constructor for Jackson
    // temporary bank number assignment
  }

  public Account(Bank bank, Customer owner, double balance) {
    this.bank = bank;
    this.owner = owner;
    this.balance = balance;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public Bank getBank() {
    return bank;
  }

  public void setBank(Bank bank) {
    this.bank = bank;
  }

  public Customer getOwner() {
    return owner;
  }

  public void setOwner(Customer owner) {
    this.owner = owner;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

}
