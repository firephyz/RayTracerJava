import java.util.Vector;

public class Ray {
	
	public Point3D p;
	public Point3D v;
	
	public Ray(double x, double y, double z, double vx, double vy, double vz) {
		p = new Point3D(x, y, z);
		v = new Point3D(vx, vy, vz);
	}
}
