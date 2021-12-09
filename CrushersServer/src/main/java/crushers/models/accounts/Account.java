package crushers.models.accounts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
  private String number;

  @JsonIgnoreProperties({ "details", "manager" })
  private Bank bank;
  
  @JsonIgnoreProperties({ "password", "securityQuestions", "lastLoginAt" })
  private Customer owner;
  private double balance = 0.00;
  protected double interestRate = 0.00;

  // for later, but not now
  // private LinkedHashMap<Time, Transaction> transactions;

  public Account() {
    // empty constructor for Jackson
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

  public double getInterestRate() {
    if(this.interestRate == 0.00){
      this.interestRate = 0.20;
      return this.interestRate;
    }
    else{
      return this.interestRate;
    }
  }

  public void setInterestRate(double interestRate) {
    if(this.interestRate != 0.00){
      this.interestRate = interestRate;
    }

  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Account other = (Account) obj;
    if (id != other.id)
      return false;
    return true;
  }

}
