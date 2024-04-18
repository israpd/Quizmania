package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SignUpScreen extends JDialog {
    private JPanel pnlSignUp;
    private JTextField txtUsername;
    private JPasswordField fieldPassword;
    private JButton btnSignUp;
    private JButton btnSignIn;
    private Controller controller;


    public SignUpScreen(Controller controller) {
        this.controller = controller;
        setTitle("Sign Up");
        setContentPane(pnlSignUp);
        setResizable(false);
        setPreferredSize(new Dimension(600, 460));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });

        setVisible(true);

        btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SigninScreen signinScreen = new SigninScreen();
            }
        });
    }



    private void signUp() {
        String username = txtUsername.getText();
        String password = new String(fieldPassword.getPassword());

        try {
           controller.createUser(username, password);
           JOptionPane.showMessageDialog(this, "User created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Unable to create user.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

    }
}
