package Artyleria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TerrainMap extends JPanel implements KeyListener {
    private final int width = 1200;
    private final int height = 800;
    private final int[] terrainY = new int[width];
    private int tankX = 100;
    private int tankY;
    private int movementPoints = 100;
    private boolean isFiring = false;
    private double firingAngle = 45.0;
    private double projectileVelocity = 80.0;
    private final double gravity = 0.981;
    private int projectileX, projectileY;

    public TerrainMap() {
        setPreferredSize(new Dimension(width, height));
        generateTerrain();
        addKeyListener(this);
        setFocusable(true);
    }

    private void generateTerrain() {
        Random random = new Random();
        for (int i = 0; i < width; i++) {
            terrainY[i] = height - 200 - random.nextInt(200);
        }
        tankY = terrainY[tankX];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.CYAN);
        g.setColor(new Color(34, 139, 34));
        // Draw the terrain
        for (int i = 0; i < width - 1; i++) {
            g.drawLine(i, terrainY[i], i + 1, terrainY[i + 1]);
        }
        // Draw the tank
        g.setColor(Color.BLACK);
        g.fillRect(tankX, tankY - 20, 40, 20);
        g.drawString("Movement Points: " + movementPoints, 10, 20);
        g.drawString("Firing Angle: " + firingAngle, 10, 40);

        // Draw the projectile if firing
        if (isFiring) {
            g.setColor(Color.RED);
            g.fillOval(projectileX, projectileY, 10, 10);
        }
    }

    private void shoot() {
    if (!isFiring && movementPoints > 0) {
        isFiring = true;
        projectileX = tankX + 20;  
        projectileY = tankY - 10;  

        double angleRad = Math.toRadians(firingAngle);
        
        
        final double[] velocities = new double[2];
        velocities[0] = projectileVelocity * Math.cos(angleRad);  
        velocities[1] = -projectileVelocity * Math.sin(angleRad);  

        Timer timer = new Timer(30, e -> {
            if (!isFiring) return;

            projectileX += velocities[0];
            projectileY += velocities[1];
            velocities[1] += gravity;  

            
            if (projectileY >= terrainY[Math.min(projectileX, width - 1)] || projectileX < 0 || projectileX >= width) {
                createCrater(projectileX);
                isFiring = false;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        timer.start();
        movementPoints--;  
    }
}

    private void createCrater(int impactX) {
        int craterRadius = 20;  
        for (int i = Math.max(impactX - craterRadius, 0); i < Math.min(impactX + craterRadius, width); i++) {
            terrainY[i] = Math.max(terrainY[i] + 10, 0); 
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                if (tankX > 0 && movementPoints > 0) {
                    tankX -= 5;
                    tankY = terrainY[tankX];
                    movementPoints--;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (tankX < width - 40 && movementPoints > 0) {
                    tankX += 5;
                    tankY = terrainY[tankX];
                    movementPoints--;
                }
                break;
            case KeyEvent.VK_UP:
                firingAngle = Math.min(firingAngle + 1, 90); 
                break;
            case KeyEvent.VK_DOWN:
                firingAngle = Math.max(firingAngle - 1, 0);
                break;
            case KeyEvent.VK_SPACE:
                shoot();
                break;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Artillery Game Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TerrainMap());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}