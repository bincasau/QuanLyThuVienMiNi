package View;

import Session.LoginSession;
import View.Librarian.Book;
import View.User.Dashboard;

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
            // Giao diện thủ thư
            Book bookFrame = new Book(session.getUsername());
            setContentPane(bookFrame.getContentPane());
        } else {
            // Giao diện độc giả
            Dashboard dashboardFrame = new Dashboard(session.getUsername());
            setContentPane(dashboardFrame.getContentPane());
        }
    }
} 