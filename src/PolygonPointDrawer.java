import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Adm on 2017-06-03.
 */
public class PolygonPointDrawer {

    private List<Polygon> polygons = new LinkedList<>();
    private List<Polygon> triPolygons = new LinkedList<>();
    private List<Point> points = new LinkedList<>();

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
        this.points.add(p);
    }

    public void show() {
        JPanel jPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLUE);
                for (Polygon p : polygons) {
                    g.fillPolygon(p);
                }
                g.setColor(Color.GREEN);
                for (Polygon p : triPolygons) {
                    g.drawPolygon(p);
                }
                g.setColor(Color.RED);
                for (Point p : points) {
                    g.fillOval((int) p.getX() - 5, (int) p.getY() - 5, 10, 10);
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
