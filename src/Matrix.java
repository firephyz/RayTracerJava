import java.util.ArrayList;

/*
 * Only supports square matricies for use in calculating the determinant.
 */

public class Matrix {
	
	private ArrayList<ArrayList<Double>> data;
	private int size;
	
	public Matrix(int size) {
		this.data = new ArrayList<>();
		this.size = size;
		
		// Construct a matrix of appropriate size
		// The structure is one of many rows of array lists
		for(int i = 0; i < size; ++i) {
			ArrayList<Double> line = new ArrayList<>();
			
			for(int j = 0; j < size; ++j) {
				line.add(0.0);
			}
			
			data.add(line);
		}
	}
	
	public void fill(double[][] nums) {
		
		/*
		 * Java doesn't know the size of the "sub arrays" in the main array
		 * so we must do manual indexing.
		 */
		for(int i = 0; i < nums.length * nums.length; ++i) {
			int x = i % size;
			int y = i / size;
			data.get(y).set(x, nums[y][x]);
		}
	}
	
	// Multiplies a matrix by the given scalar
	public void mult(double num) {
		
		for(int row = 0; row < size; ++row) {
			for(int col = 0; col < size; ++col) {
				data.get(row).set(col, data.get(row).get(col) * num);
			}
		}
	}
	
	public Matrix calcInverse() {
		
		// Find the determinant
		double determinant = getDeterminant();
		
		// Do matrix magic
		double[][] temp = new double[2][2];
		
		// First row
		Matrix m00 = new Matrix(2);
		temp[0][0] = get(1, 1);
		temp[0][1] = get(1, 2);
		temp[1][0] = get(2, 1);
		temp[1][1] = get(2, 2);
		m00.fill(temp);
		
		Matrix m10 = new Matrix(2);
		temp[0][0] = get(0, 2);
		temp[0][1] = get(0, 1);
		temp[1][0] = get(2, 2);
		temp[1][1] = get(2, 1);
		m10.fill(temp);
		
		Matrix m20 = new Matrix(2);
		temp[0][0] = get(0, 1);
		temp[0][1] = get(0, 2);
		temp[1][0] = get(1, 1);
		temp[1][1] = get(1, 2);
		m20.fill(temp);
		
		// Second row
		Matrix m01 = new Matrix(2);
		temp[0][0] = get(1, 2);
		temp[0][1] = get(1, 0);
		temp[1][0] = get(2, 2);
		temp[1][1] = get(2, 0);
		m01.fill(temp);
		
		Matrix m11 = new Matrix(2);
		temp[0][0] = get(0, 0);
		temp[0][1] = get(0, 2);
		temp[1][0] = get(2, 0);
		temp[1][1] = get(2, 2);
		m11.fill(temp);
		
		Matrix m21 = new Matrix(2);
		temp[0][0] = get(0, 2);
		temp[0][1] = get(0, 0);
		temp[1][0] = get(1, 2);
		temp[1][1] = get(1, 0);
		m21.fill(temp);
		
		// Third row
		Matrix m02 = new Matrix(2);
		temp[0][0] = get(1, 0);
		temp[0][1] = get(1, 1);
		temp[1][0] = get(2, 0);
		temp[1][1] = get(2, 1);
		m02.fill(temp);
		
		Matrix m12 = new Matrix(2);
		temp[0][0] = get(0, 1);
		temp[0][1] = get(0, 0);
		temp[1][0] = get(2, 1);
		temp[1][1] = get(2, 0);
		m12.fill(temp);
		
		Matrix m22 = new Matrix(2);
		temp[0][0] = get(0, 0);
		temp[0][1] = get(0, 1);
		temp[1][0] = get(1, 0);
		temp[1][1] = get(1, 1);
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
	
	public double getDeterminant() {
		return getDeterminantHelper(this);
	}
	
	public double get(int row, int col) {
		return data.get(row).get(col);
	}
	
	private double getDeterminantHelper(Matrix mat) {
		
		double result = 0;
		
		if(mat.size == 2) {
			result = mat.data.get(0).get(0) * mat.data.get(1).get(1) - 
					 mat.data.get(1).get(0) * mat.data.get(0).get(1);
		}
		else {
			
			int subModifier = 1;
			
			for(int i = 0; i < mat.size; ++i) {
				Matrix subMatrix = getSubMatrix(mat, i);
				result += mat.data.get(0).get(i) * subModifier * getDeterminantHelper(subMatrix);
				subModifier *= -1;
			}
		}
		
		return result;
	}
	
	/*
	 * Gets the sub matrix for calculating determinant.
	 * E.g. 
	 * 
	 * a b c d
	 * e f g h
	 * i j k l
	 * m n o p
	 * 
	 * Submatrix of the previous matrix for an 'i' of 2 =
	 * 
	 * e f h
	 * i j l
	 * m n p
	 */
	private Matrix getSubMatrix(Matrix mat, int i) {
		
		int newSize = mat.size - 1;
		int newRow = 0;
		int newCol = 0;
		
		Matrix result = new Matrix(newSize);
		
		for(int row = 1; row < mat.size; ++row) {
			for(int col = 0; col < mat.size; ++col) {
				
				// Exclude the column for a given 'i'
				if (col == i) {
					col++;
					
					if(col == mat.size) {
						continue;
					}
				}
				
				result.data.get(newRow).set(newCol, mat.data.get(row).get(col));
				
				++newCol;
			}
			newCol = 0;
			++newRow;
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		
		String result = "";
		
		for(int y = 0; y < size; ++y) {
			for(int x = 0; x < size; ++x) {
				result += data.get(y).get(x) + " ";
			}
			result += "\n";
		}
		
		return result;
	}
}
