import java.awt.*;

public class RandomPointInsidePolygon {
    /**
     * @param args
     */
    public static void main(String[] args) {
        PolygonPointDrawer polygonPointDrawer = new PolygonPointDrawer();
        int[] polyX = new int[]{100, 125, 150, 200, 500, 600, 700, 150, 125, 110, 90, 95, 750, 725, 300};
        int[] polyY = new int[]{100, 125, 200, 120, 100, 105, 125, 300, 250, 300, 150, 500, 300, 60, 80};
        Polygon polygon = new Polygon(polyX, polyY, polyX.length);

        PolygonRandomPointGenerator rPG = new PolygonRandomPointGenerator(polygon);

        polygonPointDrawer.addPolygon(polygon);
        polygonPointDrawer.addTriangles(rPG.getTriangles());
        for (int i = 0; i < 1000; i++) {
            polygonPointDrawer.addPoint(rPG.generatePoint());
        }

        polygonPointDrawer.show();
    }
}
