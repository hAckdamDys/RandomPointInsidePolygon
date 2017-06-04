import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.decompose.EarClipping;

import java.awt.Polygon;
import java.awt.Point;
import java.util.*;

/**
 * Created by Adm on 2017-06-03.
 */

public class PolygonRandomPointGenerator {
    private boolean isInitialized=false;
    private TrianglePointGenerator[] trianglePointGenerators;
    private double fullArea=0;

    public PolygonRandomPointGenerator(Polygon polygon) {
        this.init(polygon);
    }

    private void init(Polygon polygon){
        if(isInitialized) return;
        //triangulate
        ArrayList<Vector2> vectors=new ArrayList<>();
        for (int i = 0; i < polygon.npoints; i++) {
            vectors.add(new Vector2(polygon.xpoints[i],polygon.ypoints[i]));
        }
        EarClipping clipping = new EarClipping();
        List<Triangle> triangles = clipping.triangulate(vectors.toArray(new Vector2[vectors.size()]));
        //first get full area of all triangles to decide percentage each triangle takes in polygon
        ArrayList<TrianglePointGenerator> trianglePointGenerators=new ArrayList<>();
        for (Triangle triangle:triangles) {
            TrianglePointGenerator trianglePointGenerator= new TrianglePointGenerator(triangle);
            fullArea+=trianglePointGenerator.getArea();
            trianglePointGenerators.add(trianglePointGenerator);
        }
        //now setup startValues and endValues so we can binary seach on triangles
        double currentArea=0;
        for (TrianglePointGenerator trianglePointGenerator:trianglePointGenerators) {
            trianglePointGenerator.setStartValue(currentArea/fullArea);
            currentArea+=trianglePointGenerator.getArea();
            trianglePointGenerator.setEndValue(currentArea/fullArea);
        }
        this.trianglePointGenerators=trianglePointGenerators.toArray(
                new TrianglePointGenerator[trianglePointGenerators.size()]
        );
        isInitialized=true;
    }

    public List<Triangle> getTriangles(){
        List<Triangle> triangles = new LinkedList<Triangle>();
        for (TrianglePointGenerator trianglePointGenerator:trianglePointGenerators) {
            triangles.add(trianglePointGenerator.getTriangle());
        }
        return triangles;
    }

    public TrianglePointGenerator[] getTrianglePointGenerators(){
        return trianglePointGenerators;
    }

    public TrianglePointGenerator generateTriangleGenerator(){
        Random random = new Random(System.currentTimeMillis());
        double r = random.nextDouble();
        int lo = 0;
        int hi = trianglePointGenerators.length - 1;
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            int mid = lo + (hi - lo) / 2;

            double currentLo=trianglePointGenerators[mid].getStartValue();
            double currentHi=trianglePointGenerators[mid].getEndValue();

            if      (r <= currentLo) hi = mid - 1;
            else if (r > currentHi) lo = mid + 1;
            else {
                return trianglePointGenerators[mid];
            }
        }
        System.out.println("Error this method should always find triangle");
        return null;
    }

    public Point generatePoint(){
        return this.generateTriangleGenerator().generatePoint();
    }

}
