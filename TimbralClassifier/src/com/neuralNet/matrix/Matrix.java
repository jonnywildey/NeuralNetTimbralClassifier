package com.neuralNet.matrix;

/**
 * Basic matrix *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class Matrix {

	/**
	 * The Class MatrixException.
	 * 
	 * @author Jonny Wildey
	 * @version 1.0
	 */
	class MatrixException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	protected int size;
	protected Object[][] array;
	protected boolean verbose;

	/**
	 * Instantiates a new matrix.
	 * 
	 * @param size
	 *            the size
	 */
	public Matrix(int size) {
		if (size > 0) {
			this.size = size;
			this.array = new Object[size][size];
		} else {
			// throw new MatrixException();
		}

	}

	/**
	 * return cell between 0 and size of matrix - 1.
	 * 
	 * @param column
	 *            the column
	 * @param row
	 *            the row
	 * @return the cell
	 */
	public Object getCell(int column, int row) {
		return array[column][row];
	}

	/**
	 * return column *.
	 * 
	 * @param column
	 *            the column
	 * @return the column
	 */
	public Object[] getColumn(int column) {
		Object[] newColumn = new Object[size];
		for (int i = 0; i < size; ++i) {
			newColumn[i] = array[column][i];
		}
		return newColumn;
	}

	/**
	 * get matrix as 2d array.
	 * 
	 * @return the matrix
	 */
	public Object[][] getMatrix() {
		return array;
	}

	/**
	 * return row *.
	 * 
	 * @param row
	 *            the row
	 * @return the row
	 */
	public Object[] getRow(int row) {
		Object[] newRow = new Object[size];
		for (int i = 0; i < size; ++i) {
			newRow[i] = array[i][row];
		}
		return newRow;
	}

	/**
	 * Gets the size.
	 * 
	 * @return the size
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * inits matrix with value.
	 * 
	 * @param value
	 *            the value
	 */
	public void initValue(Object value) {
		for (int i = 0; i < this.size; ++i) {
			for (int j = 0; j < this.size; ++j) {
				array[i][j] = value;
			}
		}
	}

	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Set value of cell between 0 and size - 1 *.
	 * 
	 * @param column
	 *            the column
	 * @param row
	 *            the row
	 * @param object
	 *            the object
	 */
	public void setCell(int column, int row, Object object) {
		this.array[column][row] = object;
	}

	/**
	 * set whole column with value *.
	 * 
	 * @param column
	 *            the column
	 * @param value
	 *            the value
	 */
	public void setColumn(int column, Object value) {
		for (int i = 0; i < size; ++i) {
			array[column][i] = value;
		}
	}

	/**
	 * set whole column with value *.
	 * 
	 * @param row
	 *            the row
	 * @param value
	 *            the value
	 */
	public void setRow(int row, Object value) {
		for (int i = 0; i < size; ++i) {
			array[i][row] = value;
		}
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
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
