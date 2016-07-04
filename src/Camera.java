
public class Camera {
	
	private RayTracer tracer;

	public Point3D pos;
	public Point3D rot;
	public double fov;
	
	public Point3D[] rotX;
	public Point3D[] rotY;
	public Point3D[] rotZ;
	
	private Point3D focalPoint;
	
	public Camera(RayTracer tracer,
				  double x, double y, double z, 
				  double rotX, double rotY, double rotZ,
				  double fov) {
		
		this.tracer = tracer;
		
		this.pos = new Point3D(x, y, z);
		this.rot = new Point3D(rotX / 360 * (2 * Math.PI), 
							   rotY / 360 * (2 * Math.PI), 
							   rotZ / 360 * (2 * Math.PI));
		this.fov = fov / 360 * (2 * Math.PI);
		
		this.rotX = new Point3D[3];
		this.rotY = new Point3D[3];
		this.rotZ = new Point3D[3];
		
		calcRotationMatricies();
	}
	
	public void init() {
		
		double focalLength = 1 / Math.tan(fov / 2);
		
		focalPoint = tracer.rotatePoint(new Point3D(-focalLength, 0, 0));
		focalPoint.x *= -1;
		focalPoint.y *= -1;
		focalPoint.z *= -1;
	}
	
	private void calcRotationMatricies() {
		
		for(int i = 0; i < 3; ++i) {
			rotX[i] = new Point3D(0, 0, 0);
			rotY[i] = new Point3D(0, 0, 0);
			rotZ[i] = new Point3D(0, 0, 0);
			
			if(i == 0) {
				rotX[0].x = 1;
			}
			else if (i == 1) {
				rotY[1].y = 1;
			}
			else {
				rotZ[2].z = 1;
			}
		}
		
		// We use the z rotation angle for the X rotation matrix because
		// I define a rotation in the camera's X direction as a rotation 
		// around the z axis. In the Y direction, a rotation around the Y axis
		// and in the Z direction, a rotation around the Z axis.
		rotX[1].y =  Math.cos(-rot.z);
		rotX[1].z = -Math.sin(-rot.z);
		rotX[2].y =  Math.sin(-rot.z);
		rotX[2].z =  Math.cos(-rot.z);
		
		rotY[0].x =  Math.cos(-rot.y);
		rotY[0].z =  Math.sin(-rot.y);
		rotY[2].x = -Math.sin(-rot.y);
		rotY[2].z =  Math.cos(-rot.y);
		
		rotZ[0].x =  Math.cos(-rot.x);
		rotZ[0].y = -Math.sin(-rot.x);
		rotZ[1].x =  Math.sin(-rot.x);
		rotZ[1].y =  Math.cos(-rot.x);
	}
}
