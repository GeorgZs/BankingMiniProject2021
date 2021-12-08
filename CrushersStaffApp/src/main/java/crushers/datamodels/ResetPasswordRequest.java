package crushers.datamodels;

public class ResetPasswordRequest {
    private String email;
    private String password;
    private String[] securityQuestions;

    public ResetPasswordRequest(){
        // empty for jackson
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String[] getSecurityQuestions() {
        return securityQuestions;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSecurityQuestions(String[] securityQuestions) {
        this.securityQuestions = securityQuestions;
    }
}
