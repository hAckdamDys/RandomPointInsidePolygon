import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;

import java.awt.*;
import java.util.Random;

/**
 * Created by Adm on 2017-06-04.
 */
public class TrianglePointGenerator {
    private final Triangle triangle;
    private final double area;
    private double startValue;
    private double endValue;
    private Vector2 a,b,c;
    public TrianglePointGenerator(Triangle triangle){
        this.triangle=triangle;
        Vector2[] vectors = triangle.getVertices();
        a=vectors[0];
        b=vectors[1];
        c=vectors[2];
        Vector2 tmpA=a.copy(),tmpB=b.copy(),tmpC=c.copy();
        this.area=(tmpC.subtract(tmpA)).cross((tmpB.subtract(tmpA))) / 2.0;
    }

    public Triangle getTriangle(){
        return triangle;
    }
    public void setStartValue(double startValue){
        this.startValue=startValue;
    }
    public void setEndValue(double endValue){
        this.endValue=endValue;
    }
    public double getStartValue(){
        return startValue;
    }
    public double getEndValue(){
        return endValue;
    }

    public double getArea(){
        return area;
    }
    public Point generatePoint(){
        Random random = new Random(System.currentTimeMillis());
        double r1 = Math.sqrt(random.nextDouble());
        double r2 = random.nextDouble();
        Vector2 tmpA=this.a.copy(),tmpB=this.b.copy(),tmpC=this.c.copy();
        Vector2 resultVec = tmpA.multiply(1-r1).add(tmpB.multiply(r1*(1-r2))).add(tmpC.multiply(r2*r1));
        Point resultPoint = new Point((int)resultVec.x,(int)resultVec.y);
        return resultPoint;
    }
}
