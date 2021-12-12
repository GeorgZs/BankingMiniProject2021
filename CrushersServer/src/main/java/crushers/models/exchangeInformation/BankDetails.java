package crushers.models.exchangeInformation;

public class BankDetails {

    private String name;
    private String logo;
    private String details;

    public BankDetails() {
    }

    public BankDetails(String name, String logo, String details) {
        this.name = name;
        this.logo = logo;
        this.details = details;
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
