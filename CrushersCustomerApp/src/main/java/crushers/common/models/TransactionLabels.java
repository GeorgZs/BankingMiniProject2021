package crushers.common.models;

public class TransactionLabels {
  private String[] labels;

  public TransactionLabels() {
    // empty for Jackson
  }

  public String[] getLabels() {
    return labels;
  }

  public void setLabels(String[] labels) {
    this.labels = labels;
  }
}
