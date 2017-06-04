import java.awt.*;

public class RandomPointInsidePolygon {
    /**
     * @param args
     */
    public static void main(String[] args) {
        PolygonPointDrawer polygonPointDrawer = new PolygonPointDrawer();
        int[] polyX = new int[]{100, 125, 150, 200, 400, 500, 600, 700, 150, 125, 110,  90,  95, 500, 750, 725, 300};
        int[] polyY = new int[]{100, 125, 200, 120, 180, 100, 105, 125, 300, 250, 300, 150, 500, 375, 300,  60,  80};
        Polygon polygon = new Polygon(polyX, polyY, polyX.length);

        PolygonRandomPointGenerator rPG = new PolygonRandomPointGenerator(polygon);

        polygonPointDrawer.addPolygon(polygon);
        polygonPointDrawer.addTriangles(rPG.getTriangles());
        for (int i = 0; i < 10000; i++) {
            Point generatedPoint = rPG.generatePoint();
            float genColor = (float) rPG.flattenDistanceFromMiddle(generatedPoint);
            polygonPointDrawer.addPoint(generatedPoint,Color.getHSBColor(4f*(1-genColor)%1f,0.8f*(1-genColor),0.8f*(1-genColor)));
        }
        polygonPointDrawer.show();
    }
}
