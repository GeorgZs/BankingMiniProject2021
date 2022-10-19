package crushers.common.models;

import crushers.common.utils.Storable;

public class BankAccount implements Storable {
  private int id = -1;
  private String type = "INVALID"; // payment / savings / loan
  private String number;
  private Bank bank;
  private User owner;
  private double balance = 0.00;
  private String name;

  // savings / loan account only
  private double interestRate = 0.00;

  public BankAccount() {
    // empty for Jackson
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }


  // savings / loan account only
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(double interestRate) {
    this.interestRate = interestRate;
  }


  public boolean isSavings() {
    return type.equals("savings");
  }

  public boolean isPayment() {
    return type.equals("payment");
  }

  public boolean isLoan() {
    return type.equals("loan");
  }

  public boolean isInvalidType() {
    return type.equals("INVALID");
  }


  @Override
  public String toString() {
    return (name != null) 
    ? owner.getLastName() + ": " + name + " - " + number + " | " + balance + " SEK"
    : owner.getLastName() + ": " + number + " | " + balance + " SEK";
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
    BankAccount other = (BankAccount) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
