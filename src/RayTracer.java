
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
		translateScene();
	}
	
	public void render() {

		for(int y = 0; y < Main.SCREEN_HEIGHT; ++y) {
			for(int x = 0; x < Main.SCREEN_WIDTH; ++x) {
				Color color = trace(camera.getRay(x, y));
			}
		}
	}
	
	private Color trace(Ray ray) {
		
		double closestDistance = Double.MAX_VALUE;
		Color closestColor = scene.ambientColor;
		
		for(Triangle tri : scene.getTriangles()) {
			
			// Run the intersection algorithm
			double distance = calcMollerTrumbore(ray, tri);
			
			// distance less than zero means we didn't intersect or it doesn't matter
			if (distance > 0) {
				
				// If this triangle is closer to this pixel, use this triangle's color instead
				if (distance < closestDistance) {
					closestDistance = distance;
					closestColor = tri.color;
				}
			}
		}
		
		return closestColor;
	}
	
	private double calcMollerTrumbore(Ray ray, Triangle tri) {
		
		double distance = -1;
		
		/*
		 * Calculate the vectors for two triangle sides and then
		 * one from the ray starting point to the first triangle vertex
		 */
		Point3D r1 = new Point3D(tri.p2.x - tri.p1.x, 
								 tri.p2.y - tri.p1.y, 
								 tri.p2.z - tri.p1.z);
		Point3D r2 = new Point3D(tri.p3.x - tri.p1.x, 
								 tri.p3.y - tri.p1.y, 
								 tri.p3.z - tri.p1.z);
		Point3D c = new Point3D(ray.p.x - tri.p1.x,
								ray.p.y - tri.p1.y,
								ray.p.z - tri.p1.z);
		
		/*
		 * Construct the big matrix we will be working with
		 */
		Matrix mat = new Matrix(3);
		double[][] nums =
			{
				{r1.x, r2.x, -ray.v.x},
				{r1.y, r2.y, -ray.v.y},
				{r1.z, r2.z, -ray.v.z},
			};
		mat.fill(nums);
		
		/*
		 * Calculate the inverse of that matrix using that determinant
		 */
		Matrix inverse = calcInverse(mat);
		
		/*
		 * Multiply that inverse with Point3D 'c' to solve for t, u, and v
		 */
		
		/*
		 * If t, u, v are valid, return the distance to intersection, else return the original -1
		 */
		
		return distance;
	}
	
	private Matrix calcInverse(Matrix mat) {
		
		// Find the determinant
		double determinant = mat.getDeterminant();
		
		// Do matrix magic
		double[][] temp = new double[2][2];
		
		// First row
		Matrix m00 = new Matrix(2);
		temp[0][0] = mat.get(1, 1);
		temp[0][1] = mat.get(1, 2);
		temp[1][0] = mat.get(2, 1);
		temp[1][1] = mat.get(2, 2);
		m00.fill(temp);
		
		Matrix m10 = new Matrix(2);
		temp[0][0] = mat.get(0, 2);
		temp[0][1] = mat.get(0, 1);
		temp[1][0] = mat.get(2, 2);
		temp[1][1] = mat.get(2, 1);
		m10.fill(temp);
		
		Matrix m20 = new Matrix(2);
		temp[0][0] = mat.get(0, 1);
		temp[0][1] = mat.get(0, 2);
		temp[1][0] = mat.get(1, 1);
		temp[1][1] = mat.get(1, 2);
		m20.fill(temp);
		
		// Second row
		Matrix m01 = new Matrix(2);
		temp[0][0] = mat.get(1, 2);
		temp[0][1] = mat.get(1, 0);
		temp[1][0] = mat.get(2, 2);
		temp[1][1] = mat.get(2, 0);
		m01.fill(temp);
		
		Matrix m11 = new Matrix(2);
		temp[0][0] = mat.get(0, 0);
		temp[0][1] = mat.get(0, 2);
		temp[1][0] = mat.get(2, 0);
		temp[1][1] = mat.get(2, 2);
		m11.fill(temp);
		
		Matrix m21 = new Matrix(2);
		temp[0][0] = mat.get(0, 2);
		temp[0][1] = mat.get(0, 0);
		temp[1][0] = mat.get(1, 2);
		temp[1][1] = mat.get(1, 0);
		m21.fill(temp);
		
		// Third row
		Matrix m02 = new Matrix(2);
		temp[0][0] = mat.get(1, 0);
		temp[0][1] = mat.get(1, 1);
		temp[1][0] = mat.get(2, 0);
		temp[1][1] = mat.get(2, 1);
		m02.fill(temp);
		
		Matrix m12 = new Matrix(2);
		temp[0][0] = mat.get(0, 1);
		temp[0][1] = mat.get(0, 0);
		temp[1][0] = mat.get(2, 1);
		temp[1][1] = mat.get(2, 0);
		m12.fill(temp);
		
		Matrix m22 = new Matrix(2);
		temp[0][0] = mat.get(0, 0);
		temp[0][1] = mat.get(0, 1);
		temp[1][0] = mat.get(1, 0);
		temp[1][1] = mat.get(1, 1);
		m22.fill(temp);
		
		/*
		 * Construct the inverse from that magic
		 */
		Matrix inverse = new Matrix(3);
		double[][] invNums = 
			{
				{m00.getDeterminant(), m10.getDeterminant(), m20.getDeterminant()},
				{m01.getDeterminant(), m11.getDeterminant(), m21.getDeterminant()},
				{m02.getDeterminant(), m12.getDeterminant(), m22.getDeterminant()},
			};
		inverse.fill(invNums);
		inverse.mult(determinant);
		
		return inverse;
	}
	
	private void translateScene() {
		
		for(Triangle tri : scene.getTriangles()) {
			tri.p1.x = tri.p1.x - camera.pos.x;
			tri.p1.y = tri.p1.y - camera.pos.y;
			tri.p1.z = tri.p1.z - camera.pos.z;
			
			tri.p2.x = tri.p2.x - camera.pos.x;
			tri.p2.y = tri.p2.y - camera.pos.y;
			tri.p2.z = tri.p2.z - camera.pos.z;
			
			tri.p3.x = tri.p3.x - camera.pos.x;
			tri.p3.y = tri.p3.y - camera.pos.y;
			tri.p3.z = tri.p3.z - camera.pos.z;
		}
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
