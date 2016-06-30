import java.awt.Point;
import java.util.Vector;


public class Triangle {
	
	Point3D p1, p2, p3;
	Point3D normal;
	
	public Triangle(Point3D p1, Point3D p2, Point3D p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		
		calcNormal();
	}
	
	private void calcNormal() {
		
		Point3D v1 = new Point3D(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
		Point3D v2 = new Point3D(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z);
		
		double x = v1.y * v2.z - v2.y * v1.z;
		double y = v2.x * v1.z - v1.x * v2.z;
		double z = v1.x * v2.y - v2.x * v1.y;
		
		normal = new Point3D(x, y, z);
	}
}
