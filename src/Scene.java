
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Scene {
	
	private Scanner data;
	private ArrayList<Triangle> tris;
	
	public Color ambientColor = new Color(0.8f, 0.8f, 0.8f);
	private Color defaultColor = new Color(0.5f, 0.5f, 0.5f);
	
	public Scene(String dir) {
		
		tris = new ArrayList<>();
		
		try {
			data = new Scanner(new File(System.getProperty("user.dir") + dir));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Scene data file does not exist. Quiting...");
			System.exit(1);
		}
		
		try {
			loadTriangleData();
		} catch (ParseException e) {
			System.err.println("Problem parsing triangle data file. Quiting...");
			System.exit(1);
		}
	}
	
	private void loadTriangleData() throws ParseException {
		
		String line = null;
		
		while(data.hasNextLine()) {
			
			line = consumeBlankLines();
			
			
			if(!line.trim().equals("<tri>")) {
				throw new ParseException("Missing opening triangle '<tri>' declaration.");
			}
			
			line = consumeBlankLines();
			
			if(!line.trim().equals("<point>")) {
				throw new ParseException("Missing opening point '<point>' declaration.");
			}
			
			line = consumeBlankLines();
			
			Point3D p1 = parsePoint(Double.parseDouble(line.trim()));

			line = consumeBlankLines();
			
			if(!line.trim().equals("</point>")) {
				throw new ParseException("Missing ending point '</point>' declaration.");
			}
			
			line = consumeBlankLines();
			
			if(!line.trim().equals("<point>")) {
				throw new ParseException("Missing opening point '<point>' declaration.");
			}
			
			line = consumeBlankLines();
			
			Point3D p2 = parsePoint(Double.parseDouble(line.trim()));
			
			line = consumeBlankLines();
			
			if(!line.trim().equals("</point>")) {
				throw new ParseException("Missing ending point '</point>' declaration.");
			}
			
			line = consumeBlankLines();
			
			if(!line.trim().equals("<point>")) {
				throw new ParseException("Missing opening point '<point>' declaration.");
			}
			
			line = consumeBlankLines();
			
			Point3D p3 = parsePoint(Double.parseDouble(line.trim()));
			
			line = consumeBlankLines();
			
			if(!line.trim().equals("</point>")) {
				throw new ParseException("Missing ending point '</point>' declaration.");
			}
			
			line = consumeBlankLines();
			
			boolean hasNoColor = false;
			Color color = defaultColor;
			if(!line.trim().equals("<color>")) {
				hasNoColor = true;
			}
			
			if(hasNoColor) {
				if(!line.trim().equals("</tri>")) {
					throw new ParseException("Missing ending triangle '</tri>' declaration.");
				}
			}
			else {
				line = consumeBlankLines();
				
				color = parseColor(Double.parseDouble(line.trim()));
				
				line = consumeBlankLines();
				
				if(!line.trim().equals("</color>")) {
					throw new ParseException("Missing ending color '</color>' declaration.");
				}
				
				line = consumeBlankLines();
				
				if(!line.trim().equals("</tri>")) {
					throw new ParseException("Missing ending triangle '</tri>' declaration.");
				}
			}
			
			tris.add(new Triangle(p1, p2, p3, color));
		}
	}
	
	private Color parseColor(double red) {
		
		double green  = data.nextDouble();
		double blue = data.nextDouble();
		
		return new Color((float)red, (float)green, (float)blue);
	}
	
	private Point3D parsePoint(double x) {
		
		double y = data.nextDouble();
		double z = data.nextDouble();
		
		return new Point3D(x, y, z);
	}
	
	private String consumeBlankLines() {
		
		String line = null;
		
		do {
			line = data.nextLine();
		} while(data.hasNext() && line.length() == 0);
		
		return line;
	}
	
	public ArrayList<Triangle> getTriangles() {return this.tris;}
}
