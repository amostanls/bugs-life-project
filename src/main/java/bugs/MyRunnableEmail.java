package bugs;

import java.util.ArrayList;

/**
 * @author Tay Qi Xiang on 4/6/2021
 */
public class MyRunnableEmail implements Runnable{
    private String email;
    private String verificationCode="";

    public MyRunnableEmail(String email) {
        this.email=email;
    }

    @Override
    public void run() {
        this.verificationCode=Mail.resetPassword(email);
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}
