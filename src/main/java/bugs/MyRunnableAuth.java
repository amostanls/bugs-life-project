package bugs;

/**
 * @author Tay Qi Xiang on 4/6/2021
 */
public class MyRunnableAuth implements Runnable{
    private String email;
    private String verificationCode="";

    public MyRunnableAuth(String email) {
        this.email=email;
    }

    @Override
    public void run() {
        this.verificationCode=Mail.authorization(email);
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}
