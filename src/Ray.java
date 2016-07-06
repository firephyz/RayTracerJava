import java.util.Vector;

public class Ray {
	
	public Point3D p;
	public Point3D v;
	
	public Ray(Point3D start, Point3D end) {
		p = new Point3D(start);
		v = new Point3D(end);
	}
}
