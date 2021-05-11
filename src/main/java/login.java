import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class login extends JFrame {
    private JPanel user;
    private JPanel rootPanel;
    private JTextField username_field;
    private JPasswordField password_field;
    private JButton button1;

    public login(){

        add(rootPanel);
        setTitle("Bugs life");
        setSize(500,500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(username_field.equals("admin")&&password_field.equals("password")){
                    JOptionPane.showMessageDialog(null,"Correct password");
                }
                else{
                    JOptionPane.showMessageDialog(null,"wrong password");
                }
            }
        });
    }


}
