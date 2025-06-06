package View;

import Session.LoginSession;
import View.Librarian.Librarian;
import View.User.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Thư viện MINI");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        LoginSession session = LoginSession.getInstance();
        if (session.isAdmin()) {
            Librarian librarian = new Librarian(session.getFullName());
            setContentPane(librarian.getMainPanel());
        } else {
            User user = new User(session.getFullName());
            setContentPane(user);  // Set the User panel directly
        }

        setVisible(true);
    }
}