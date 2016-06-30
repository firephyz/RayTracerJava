
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {
	
	public static final int SCREEN_WIDTH = 500;
	public static final int SCREEN_HEIGHT = 500;
	
	private JFrame display;
	private BufferedImage image;
	private Graphics2D canvas;

	public Main() {
		
		initDisplay();
		
		Scene scene = new Scene("/space/scene.txt");
		new RayTracer(image, canvas, scene).render();
	}
	
	public void initDisplay() {
		
		System.setProperty("sun.java2d.opengl", "true");
		image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		image.setAccelerationPriority((float)1.0);
		canvas = image.createGraphics();
		canvas.setColor(Color.BLACK);
		canvas.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

		display = new JFrame();
		display.setTitle("RayTracer");
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.setLocationRelativeTo(null);
		display.setLocation(display.getLocation().x - SCREEN_WIDTH / 2, 
						 display.getLocation().y - SCREEN_HEIGHT / 2);
		display.add(this);
		display.pack();
		display.setVisible(true);
		display.repaint();
		
		(new Timer()).scheduleAtFixedRate(new DrawTask(), 0, 40);
	}
	
	// Used to implement the screen framerate (calls repaint at a fixed rate)
	private class DrawTask extends TimerTask {
	  	@Override
	  	public void run() {
	  		display.repaint();
	  	}
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
