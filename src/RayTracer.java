
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class RayTracer {
	
	private BufferedImage image;
	private Graphics2D canvas;
	private Scene scene;

	public RayTracer(BufferedImage image, Graphics2D canvas, Scene scene) {
		this.image = image;
		this.canvas = canvas;
		this.scene = scene;
	}
	
	public void render() {
		canvas.setColor(Color.BLUE);
		canvas.fillRect(100, 100, 1, 1);
	}
}
