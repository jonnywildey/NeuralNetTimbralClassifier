package com.matrix;

public class Matrix {

	class MatrixException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	protected int size;
	protected Object[][] array;
	protected boolean verbose;

	public Matrix(int size) {
		if (size > 0) {
			this.size = size;
			this.array = new Object[size][size];
		} else {
			// throw new MatrixException();
		}

	}

	/** inits matrix with value */
	public void initValue(Object value) {
		for (int i = 0; i < this.size; ++i) {
			for (int j = 0; j < this.size; ++j) {
				array[i][j] = value;
			}
		}
	}

	/** return cell between 0 and size of matrix - 1 */
	public Object getCell(int column, int row) {
		return array[column][row];
	}

	/** Set value of cell between 0 and size - 1 **/
	public void setCell(int column, int row, Object object) {
		this.array[column][row] = object;
	}

	/** return row **/
	public Object[] getRow(int row) {
		Object[] newRow = new Object[size];
		for (int i = 0; i < size; ++i) {
			newRow[i] = array[i][row];
		}
		return newRow;
	}

	/** return column **/
	public Object[] getColumn(int column) {
		Object[] newColumn = new Object[size];
		for (int i = 0; i < size; ++i) {
			newColumn[i] = array[column][i];
		}
		return newColumn;
	}

	/** get matrix as 2d array */
	public Object[][] getMatrix() {
		return array;
	}

	/** set whole column with value **/
	public void setColumn(int column, Object value) {
		for (int i = 0; i < size; ++i) {
			array[column][i] = value;
		}
	}

	/** set whole column with value **/
	public void setRow(int row, Object value) {
		for (int i = 0; i < size; ++i) {
			array[i][row] = value;
		}
	}

	public int getSize() {
		return this.size;
	}

	public String toString() {
		String str = new String();
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				str = array[i][j].toString();
			}
		}
		return str;
	}

}
