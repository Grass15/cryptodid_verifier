import java.io.Serializable;

public class User implements Serializable {
    private String firstname;
    private String lastname;
    private String address;
    private String email;
    private String phone;
    private String country;
    private Boolean[] verifiableCredentialStatus ;
    private Boolean verificationStatus ;
    public User(String firstname, String lastname, String address, String email, String phone, String country, Boolean[] verifiableCredentialStatus){
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.verifiableCredentialStatus = verifiableCredentialStatus;
        setVerificationStatus();
    }
    public void setVerificationStatus(){
        this.verificationStatus = true;
        for(Boolean status : this.verifiableCredentialStatus){
            if (!status){
                this.verificationStatus = false;
                break;
            }
        }
    }

}
