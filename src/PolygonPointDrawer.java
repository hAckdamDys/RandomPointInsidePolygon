import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Adm on 2017-06-03.
 */
public class PolygonPointDrawer {

    private List<Polygon> polygons = new LinkedList<>();
    private List<Polygon> triPolygons = new LinkedList<>();
    private List<Point> points = new LinkedList<>();
    private List<Color> pointColors = new LinkedList<>();

    public void addPolygon(Polygon p) {
        this.polygons.add(p);
    }

    public void addTriangles(Collection<Triangle> triangles) {
        for (Triangle triangle : triangles) {
            Polygon triPol = new Polygon();
            for (Vector2 v : triangle.getVertices()) {
                triPol.addPoint((int) v.x, (int) v.y);
            }
            this.triPolygons.add(triPol);
        }
    }

    public void addPoint(Point p) {
        addPoint(p, Color.RED);
    }

    public void addPoint(Point p, Color c) {
        this.points.add(p);
        this.pointColors.add(c);
    }

    public void show() {
        JPanel jPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.black);
                g.setColor(Color.WHITE);
                for (Polygon p : polygons) {
                    g.fillPolygon(p);
                }
                Graphics2D g2=(Graphics2D)g;
                g2.setStroke(new BasicStroke(2));
                g.setColor(Color.RED);
                for (Polygon p : triPolygons) {
                    g.drawPolygon(p);
                }
                Iterator<Color> colorIterator = pointColors.iterator();
                for (Point p : points) {
                    g.setColor(colorIterator.next());
                    g.fillOval((int) p.getX() - 3, (int) p.getY() - 3, 6, 6);
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 600);
            }
        };
        JFrame mainMap = new JFrame();
        mainMap.setResizable(false);
        mainMap.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainMap.add(jPanel);
        mainMap.pack();
        mainMap.setVisible(true);
    }


}
