package bugs;

import java.util.ArrayList;

/**
 * @author Tay Qi Xiang on 7/6/2021
 */
public class MyRunnableCode implements Runnable{
    private String code;
    private String email;

    public MyRunnableCode(String email) {
        this.email=email;
    }

    @Override
    public void run() {
        this.code=Mail.authorization(email);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
