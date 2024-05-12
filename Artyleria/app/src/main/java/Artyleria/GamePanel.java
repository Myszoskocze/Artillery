package Artyleria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, MouseListener {
    private final int panelWidth = 800;
    private final int panelHeight = 600;
    private Timer timer;

    public GamePanel() {
        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setBackground(Color.GREEN);
        this.addMouseListener(this);
        timer = new Timer(1000 / 60, this);  // aktualizacja 60 razy na sekundê
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Tutaj rysujemy elementy gry, np. czo³gi, pociski
        g.setColor(Color.RED);
        g.fillOval(100, panelHeight - 50, 50, 50);  // przyk³adowy czo³g
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Aktualizacja stanu gry
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Obs³uga klikniêcia - wystrzelenie pocisku
        int mouseX = e.getX();
        int mouseY = e.getY();
        fireProjectile(100, panelHeight - 25, calculateAngle(100, panelHeight - 25, mouseX, mouseY), 100);
    }

    private double calculateAngle(int startX, int startY, int targetX, int targetY) {
        // Obliczanie k¹ta do celu
        double angle = Math.toDegrees(Math.atan2(targetY - startY, targetX - startX));
        return angle;
    }

    private void fireProjectile(int startX, int startY, double angle, double velocity) {
        double x = startX;
        double y = startY;
        double time = 0;
        double deltaTime = 0.1;

        while (y >= 0 && x >= 0 && x <= panelWidth) {
            x = startX + velocity * time * Math.cos(Math.toRadians(angle));
            y = startY - (velocity * time * Math.sin(Math.toRadians(angle)) - 0.5 * 9.81 * time * time);
            time += deltaTime;
            repaint();  // Rysowanie trajektorii pocisku
            try {
                Thread.sleep(10);  // Czekanie na aktualizacjê ekranu
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}