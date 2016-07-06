
public class Point3D {
	
	public double x, y, z;
	
	public Point3D() {
		this(0, 0, 0);
	}
	
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D(Point3D copy) {
		this.x = copy.x;
		this.y = copy.y;
		this.z = copy.z;
	}
}
