
package Artyleria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class czolg extends JPanel {
    private int tankX = 150; // Pocz¹tkowa pozycja pozioma czo³gu
    private int tankY = 200; // Sta³a pozycja pionowa czo³gu
    private int tankSize = 50;  // Rozmiar czo³gu
    private int barrelAngle = 0; // K¹t obrotu lufy, 0 stopni to poziomo w prawo

    public czolg() {
        setFocusable(true); // Umo¿liwia skupienie, aby JPanel móg³ odbieraæ zdarzenia klawiatury
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                        tankX -= 5;
                        break;
                    case KeyEvent.VK_RIGHT:
                        tankX += 5;
                        break;
                    case KeyEvent.VK_A:
                        barrelAngle -= 5;
                        if (barrelAngle < -90) barrelAngle = -90;
                        break;
                    case KeyEvent.VK_D:
                        barrelAngle += 5;
                        if (barrelAngle > 90) barrelAngle = 90;
                        break;
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Rysuj czo³g (kwadrat)
        g2d.fillRect(tankX - tankSize / 2, tankY - tankSize / 2, tankSize, tankSize);

        // Rysuj lufê
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3));
        double angleRad = Math.toRadians(barrelAngle);
        int barrelEndX = tankX + (int) (1.5 * tankSize * Math.cos(angleRad));
        int barrelEndY = tankY - (int) (1.5 * tankSize * Math.sin(angleRad)); // U¿ycie minusa, aby obrót by³ w poziomie
        g2d.drawLine(tankX, tankY, barrelEndX, barrelEndY);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Horizontal Tank Movement and Barrel Control");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(new czolg());
        frame.setVisible(true);
    }
}