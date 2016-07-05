
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class RayTracer {
	
	private BufferedImage image;
	private Graphics2D canvas;
	private Scene scene;
	
	private Camera camera;

	public RayTracer(BufferedImage image, Graphics2D canvas, Scene scene) {
		this.image = image;
		this.canvas = canvas;
		this.scene = scene;
		
		camera = new Camera(this, -2, -2, 2, 90, 0, 0, 70);
		camera.init();
		
		rotateScene();
	}
	
	public void render() {

		for(int y = 0; y < Main.SCREEN_HEIGHT; ++y) {
			for(int x = 0; x < Main.SCREEN_WIDTH; ++x) {
				Color color = trace(camera.getRay(x, y));
			}
		}
	}
	
	private Color trace(Ray ray) {
		
		return new Color(0, 0, 0);
	}
	
	private void rotateScene() {
		
		for(Triangle tri : scene.getTriangles()) {
			tri.p1 = rotatePoint(tri.p1);
			tri.p2 = rotatePoint(tri.p2);
			tri.p3 = rotatePoint(tri.p3);
			
			// Recalculate the normal after the rotation
			/*
			 * To-Do: Instead of doing the whole cross product as this function does,
			 * maybe we can just rotate the original normal according to the
			 * rotation matricies.
			 */
			tri.calcNormal();
		}
	}
	
	public Point3D rotatePoint(Point3D point) {
		
		// Apply X rotation
		Point3D newPointX = new Point3D(camera.rotX[0].x * point.x + camera.rotX[0].y * point.y + camera.rotX[0].z * point.z,
									    camera.rotX[1].x * point.x + camera.rotX[1].y * point.y + camera.rotX[1].z * point.z,
									    camera.rotX[2].x * point.x + camera.rotX[2].y * point.y + camera.rotX[2].z * point.z);
		// Apply Y rotation
		Point3D newPointY = new Point3D(camera.rotY[0].x * newPointX.x + camera.rotY[0].y * newPointX.y + camera.rotY[0].z * newPointX.z,
										camera.rotY[1].x * newPointX.x + camera.rotY[1].y * newPointX.y + camera.rotY[1].z * newPointX.z,
										camera.rotY[2].x * newPointX.x + camera.rotY[2].y * newPointX.y + camera.rotY[2].z * newPointX.z);
		// Apply Z rotation
		Point3D newPointZ = new Point3D(camera.rotZ[0].x * newPointY.x + camera.rotZ[0].y * newPointY.y + camera.rotZ[0].z * newPointY.z,
										camera.rotZ[1].x * newPointY.x + camera.rotZ[1].y * newPointY.y + camera.rotZ[1].z * newPointY.z,
										camera.rotZ[2].x * newPointY.x + camera.rotZ[2].y * newPointY.y + camera.rotZ[2].z * newPointY.z);
		
		return newPointZ;
	}
}
