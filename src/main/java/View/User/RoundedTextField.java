package view.User;

import javax.swing.*;
import java.awt.*;

class RoundedTextField extends JTextField {
    private int radius;

    public RoundedTextField(int radius) {
        this.radius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground() != null ? getBackground() : Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(Color.WHITE);
        ((Graphics2D) g).drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
    }
}
