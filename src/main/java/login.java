import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class login extends JFrame {

    private JPanel rootPanel;
    private JTextField username_field;
    private JPasswordField password_field;
    private JButton button1;
    private JPanel title_panel;
    private JPanel input_panel;

    public login() {

        add(rootPanel);
        setTitle("Bugs life");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setVisible(true);

        Border panelBorder = BorderFactory.createMatteBorder(0, 0, 0, 5, Color.black);
        title_panel.setBorder(panelBorder);

        Border titleBorder = BorderFactory.createMatteBorder(0, 0, 4, 0, Color.gray);
        input_panel.setBorder(titleBorder);


        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username=username_field.getText().trim();
                String password=String.valueOf(password_field.getPassword()).trim();
                if (username.equals("admin") && password.equals("password")) {
                    JOptionPane.showMessageDialog(null, "Correct password");
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong username or password");
                }
                username_field.setText("");
                password_field.setText("");
            }
        });
    }


}
