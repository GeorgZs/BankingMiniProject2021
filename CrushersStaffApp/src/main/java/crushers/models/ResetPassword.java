package crushers.models;

public class ResetPassword {
    private String email;
    private String password;
    private String[] securityQuestions;

    public ResetPassword(){
        //for jackson
    }

    public ResetPassword(String email, String password, String[] securityQuestions){
        this.email = email;
        this.password = password;
        this.securityQuestions = securityQuestions;
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

