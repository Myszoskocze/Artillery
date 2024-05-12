
package Artyleria;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mapa extends JPanel {
    private final List<Point> points;
    private final Random random = new Random();

    public Mapa(int numberOfPoints, int width, int height) {
        setPreferredSize(new Dimension(width, height));
        points = new ArrayList<>();
        generateRandomPoints(numberOfPoints, width, height);
    }

    private void generateRandomPoints(int numberOfPoints, int width, int height) {
        // Dodaj pierwszy punkt na lewym krañcu
        points.add(new Point(0, random.nextInt(height)));

        // Generuj pozosta³e punkty, pomijaj¹c pierwszy i ostatni
        for (int i = 1; i < numberOfPoints - 1; i++) {
            int x = 1 + random.nextInt(width - 2);
            int y = random.nextInt(height);
            points.add(new Point(x, y));
        }

        // Dodaj ostatni punkt na prawym krañcu
        points.add(new Point(width - 1, random.nextInt(height)));

        // Sortowanie punktów po wspó³rzêdnej x
        points.sort((p1, p2) -> Integer.compare(p1.x, p2.x));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSmoothCurveAndColoredAreas((Graphics2D) g);
    }

    private void drawSmoothCurveAndColoredAreas(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Przygotowanie do rysowania krzywych i obszarów
        GeneralPath path = new GeneralPath();
        path.moveTo(points.get(0).x, points.get(0).y);

        // Generowanie krzywych Béziera na podstawie punktów
        if (points.size() > 1) {
            Point p1 = points.get(0);
            for (int i = 1; i < points.size(); i++) {
                Point p2 = points.get(i);
                Point p3 = i < points.size() - 1 ? points.get(i + 1) : null;

                // Oblicz punkty kontrolne dla krzywych Béziera
                int ctrlX1 = p1.x + (p2.x - p1.x) / 3;
                int ctrlY1 = p1.y + (p2.y - p1.y) / 3;
                int ctrlX2 = p2.x - (p3 != null ? (p3.x - p1.x) / 3 : (p2.x - p1.x) / 3);
                int ctrlY2 = p2.y - (p3 != null ? (p3.y - p1.y) / 3 : (p2.y - p1.y) / 3);

                path.curveTo(ctrlX1, ctrlY1, ctrlX2, ctrlY2, p2.x, p2.y);
                p1 = p2;
            }
        }

        // Rysowanie obszaru pod krzyw¹
        g2.setColor(Color.GREEN);
        GeneralPath areaBelow = new GeneralPath(path);
        areaBelow.lineTo(getWidth(), getHeight());
        areaBelow.lineTo(0, getHeight());
        areaBelow.closePath();
        g2.fill(areaBelow);

        // Rysowanie obszaru nad krzyw¹
        g2.setColor(Color.BLUE);
        GeneralPath areaAbove = new GeneralPath(path);
        areaAbove.lineTo(getWidth(), 0);
        areaAbove.lineTo(0, 0);
        areaAbove.closePath();
        g2.fill(areaAbove);

        // Rysowanie krzywej
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.draw(path);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Smooth Curve Map with Colored Areas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Mapa(10, 800, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}