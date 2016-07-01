
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class RayTracer {
	
	private BufferedImage image;
	private Graphics2D canvas;
	private Scene scene;
	
	private Point3D camera;
	private Point3D rotation;
	
	private Point3D[] rotX;
	private Point3D[] rotY;
	private Point3D[] rotZ;

	public RayTracer(BufferedImage image, Graphics2D canvas, Scene scene) {
		this.image = image;
		this.canvas = canvas;
		this.scene = scene;
		
		camera = new Point3D(-2, -2, 2);
		rotation = new Point3D(Math.PI / 4, 0, 0);
		rotX = new Point3D[3];
		rotY = new Point3D[3];
		rotZ = new Point3D[3];
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
		calcRotationMatricies();
		
		rotateScene();
	}
	
	public void render() {
		canvas.setColor(Color.BLUE);
		canvas.fillRect(100, 100, 1, 1);
	}
	
	private void rotateScene() {
		
		for(Triangle tri : scene.getTriangles()) {
			tri.p1 = rotatePoint(tri.p1);
			tri.p2 = rotatePoint(tri.p2);
			tri.p3 = rotatePoint(tri.p3);
		}
	}
	
	private Point3D rotatePoint(Point3D point) {
		
		// Apply X rotation
		Point3D newPointX = new Point3D(rotX[0].z * point.x + rotX[0].y * point.y + rotX[0].x * point.z,
									    rotX[1].z * point.x + rotX[1].y * point.y + rotX[1].x * point.z,
									    rotX[2].z * point.x + rotX[2].y * point.y + rotX[2].x * point.z);
		// Apply Y rotation
		Point3D newPointY = new Point3D(rotX[0].z * newPointX.x + rotX[0].y * newPointX.y + rotX[0].x * newPointX.z,
										rotX[1].z * newPointX.x + rotX[1].y * newPointX.y + rotX[1].x * newPointX.z,
										rotX[2].z * newPointX.x + rotX[2].y * newPointX.y + rotX[2].x * newPointX.z);
		// Apply Z rotation
		Point3D newPointZ = new Point3D(rotX[0].z * newPointY.x + rotX[0].y * newPointY.y + rotX[0].x * newPointY.z,
										rotX[1].z * newPointY.x + rotX[1].y * newPointY.y + rotX[1].x * newPointY.z,
										rotX[2].z * newPointY.x + rotX[2].y * newPointY.y + rotX[2].x * newPointY.z);
		
		return newPointZ;
	}
	
	private void calcRotationMatricies() {
		
		rotX[1].y =  Math.cos(-rotation.x);
		rotX[1].z = -Math.sin(-rotation.x);
		rotX[2].y =  Math.sin(-rotation.x);
		rotX[2].z =  Math.cos(-rotation.x);
		
		rotY[0].x =  Math.cos(-rotation.y);
		rotY[0].z =  Math.sin(-rotation.y);
		rotY[2].x = -Math.sin(-rotation.y);
		rotY[2].z =  Math.cos(-rotation.y);
		
		rotY[0].x =  Math.cos(-rotation.z);
		rotY[0].y = -Math.sin(-rotation.z);
		rotY[1].x =  Math.sin(-rotation.z);
		rotY[1].y =  Math.cos(-rotation.z);
	}
}
