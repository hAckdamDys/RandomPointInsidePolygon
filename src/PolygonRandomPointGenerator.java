import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.decompose.EarClipping;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Adm on 2017-06-03.
 */

public class PolygonRandomPointGenerator {
    private boolean isInitialized = false;
    private TrianglePointGenerator[] trianglePointGenerators;
    private List<Triangle> triangles;
    private double fullArea = 0;
    private Random random = new Random(System.currentTimeMillis());
    private Point middlePoint=null;
    private Polygon polygon;
    private double futherestDistance = 0;

    public PolygonRandomPointGenerator(Polygon polygon) {
        this.polygon=polygon;
        this.init();
    }

    private void init() {
        if (isInitialized) return;
        //triangulate
        ArrayList<Vector2> vectors = new ArrayList<>();
        for (int i = 0; i < polygon.npoints; i++) {
            vectors.add(new Vector2(polygon.xpoints[i], polygon.ypoints[i]));
        }
        EarClipping clipping = new EarClipping();
        List<Triangle> triangles = clipping.triangulate(vectors.toArray(new Vector2[vectors.size()]));
        this.triangles=triangles;
        //first get full area of all triangles to decide percentage each triangle takes in polygon
        ArrayList<TrianglePointGenerator> trianglePointGenerators = new ArrayList<>();
        for (Triangle triangle : triangles) {
            TrianglePointGenerator trianglePointGenerator = new TrianglePointGenerator(triangle);
            fullArea += trianglePointGenerator.getArea();
            trianglePointGenerators.add(trianglePointGenerator);
        }
        //now setup startValues and endValues so we can binary seach on triangles
        double currentArea = 0;
        for (TrianglePointGenerator trianglePointGenerator : trianglePointGenerators) {
            trianglePointGenerator.setStartValue(currentArea / fullArea);
            currentArea += trianglePointGenerator.getArea();
            trianglePointGenerator.setEndValue(currentArea / fullArea);
        }
        this.trianglePointGenerators = trianglePointGenerators.toArray(
                new TrianglePointGenerator[trianglePointGenerators.size()]
        );
        isInitialized = true;
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public TrianglePointGenerator[] getTrianglePointGenerators() {
        return trianglePointGenerators;
    }

    public TrianglePointGenerator generateTriangleGenerator() {

        double r = random.nextDouble();
        int lo = 0;
        int hi = trianglePointGenerators.length - 1;
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            int mid = lo + (hi - lo) / 2;

            double currentLo = trianglePointGenerators[mid].getStartValue();
            double currentHi = trianglePointGenerators[mid].getEndValue();

            if (r <= currentLo) hi = mid - 1;
            else if (r > currentHi) lo = mid + 1;
            else {
                return trianglePointGenerators[mid];
            }
        }
        System.out.println("Error this method should always find triangle");
        return null;
    }

    public Point generatePoint() {
        return this.generateTriangleGenerator().generatePoint();
    }

    public Point getMiddlePoint(){
        if(middlePoint==null) {
            //average points value:
//            double xAvg = 0;
//            double yAvg = 0;
//            for (int i = 0; i < polygon.npoints; i++) {
//                xAvg += polygon.xpoints[i];
//                yAvg += polygon.ypoints[i];
//            }
//            xAvg/=polygon.npoints;
//            yAvg/=polygon.npoints;
//            middlePoint = new Point((int)xAvg,(int)yAvg);

            //midlle Area Point
            double xAvg = 0;
            double yAvg = 0;
            for (TrianglePointGenerator tPG:trianglePointGenerators) {
                double curArea = tPG.getArea();
                Vector2 center = tPG.getTriangle().getCenter();
                xAvg+=(center.x*curArea);
                yAvg+=(center.y*curArea);
            }
            xAvg/=fullArea;
            yAvg/=fullArea;
            middlePoint = new Point((int)xAvg,(int)yAvg);
        }
        return middlePoint;
    }

    public double getFurtherestDistance(){
        if(futherestDistance == 0){
            Point middlePoint = this.getMiddlePoint();
            for (int i = 0; i < polygon.npoints; i++) {
                double curDistance = middlePoint.distance(polygon.xpoints[i],polygon.ypoints[i]);
                if(curDistance>futherestDistance){
                    futherestDistance=curDistance;
                }
            }
        }
        return futherestDistance;
    }

    public double flattenDistanceFromMiddle(Point point){
        Point middlePoint = this.getMiddlePoint();//so it initialize if not initialized yet
        double furtherestDistance = this.getFurtherestDistance();
        return middlePoint.distance(point)/futherestDistance;
    }
}
