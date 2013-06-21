/*
 * Copyright 2004-2010 Information & Software Engineering Group (188/1)
 *                     Institute of Software Technology and Interactive Systems
 *                     Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.ifs.tuwien.ac.at/mir/index.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package feature.lowLevel.audio.data;

import java.util.Arrays;

/**
 * Copyright Vienna University of Technology
 * 
 * @author Thomas Lidy
 * @version $Id: RealMatrixExt.java 173 2010-06-17 16:28:05Z mayer $
 */
public class RealMatrixExt extends org.apache.commons.math3.linear.Array2DRowRealMatrix {
   
	private static final long serialVersionUID = 1L;

    public static final int TYPE_UNKNOWN = -1;

    public static final int TYPE_RP = 0;

    public static final int TYPE_SSD = 1;

    public static final int TYPE_RH = 2;

    private int type = TYPE_UNKNOWN;

    /**
     * Create a new (column) RealMatrix using vector as the data for the unique column of the
     * vector.length x 1 matrix created.
     */
    public RealMatrixExt(double[] vector) {
        super(vector);
    }

    /**
     * Create a new RealMatrix using the input 2D double array as matrix.
     * 
     * @param matrix
     */
    public RealMatrixExt(double[][] matrix) {
        super(matrix);
    }

    /**
     * Create a new RealMatrix with the supplied row and column dimensions.
     * 
     * @param rowDimension
     * @param columnDimension
     */
    public RealMatrixExt(int rowDimension, int columnDimension) {
        super(rowDimension, columnDimension);
    }

    /**
     * sets type (ID) of matrix
     * 
     * @param type integer type ID (may be one of the static type IDs of this class)
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * returns type (ID) of matrix
     * 
     * @return integer type ID (probably one of the static type IDs of this class)
     */
    public int getType() {
        return type;
    }

    /**
     * vectorize the matrix (i.e. concatenate column for column to a vector )
     */
    public double[] vectorize() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        /*
         * if ((nRows == 0) || (nCols == 0)) { throw new MatrixIndexException(
         * "Matrix: either number of rows or number of columns is 0."); }
         */

        double data[][] = getDataRef();

        double[] vector = new double[nRows * nCols];
        int i = 0;

        for (int c = 0; c < nCols; c++) {
            for (int r = 0; r < nRows; r++) {
                vector[i] = data[r][c];
                i++;
            }
        }

        return vector;
    }

    /**
     * @return the sum of all rows per row (i.e. the length of the resulting double array will equal
     *         the number of rows)
     */
    public double[] sumRows() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        double data[][] = getDataRef();

        double[] result = new double[nRows];

        for (int r = 0; r < nRows; r++) {
            result[r] = 0;
            for (int c = 0; c < nCols; c++) {
                result[r] += data[r][c];
            }
        }

        return result;
    }

    /**
     * @return the mean per row (i.e. the length of the resulting double array will equal the number
     *         of rows)
     */
    public double[] meanRows() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        double[] result = sumRows();

        for (int r = 0; r < nRows; r++) {
            result[r] = result[r] / nCols;
        }

        return result;
    }

    /**
     * @return the sum of all columns (i.e. the length of the resulting double array will equal the
     *         number of columns)
     */
    public double[] sumColumns() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        double data[][] = getDataRef();

        double[] result = new double[nCols];

        for (int c = 0; c < nCols; c++) {
            result[c] = 0;
            for (int r = 0; r < nRows; r++) {
                result[c] += data[r][c];
            }
        }

        return result;
    }

    /**
     * @return the mean per column (i.e. the length of the resulting double array will equal the
     *         number of columns)
     */
    public double[] meanColumns() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        double[] result = sumColumns();

        for (int c = 0; c < nCols; c++) {
            result[c] = result[c] / nRows;
        }

        return result;
    }

    /**
     * Computes a median matrix from an array of matrices (taking the median of every single
     * element)
     * 
     * @param matrices an array of matrices
     * @return a matrix which contains the median of every element of the given matrices
     */
    public static RealMatrixExt median(RealMatrixExt[] matrices) {
        int numMat = matrices.length;
        double[] aggregator = new double[numMat];

        int nRows = matrices[0].getRowDimension();
        int nCols = matrices[0].getColumnDimension();
        // TODO: check if #rows and #cols is equal in all matrices!

        double[][] result = new double[nRows][nCols];

        boolean center = !(numMat % 2 == 0);
        int ctridx;
        if (center)
            ctridx = (numMat - 1) / 2;
        else
            ctridx = (numMat / 2) - 1;

        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                for (int m = 0; m < numMat; m++) {
                    aggregator[m] = matrices[m].getEntry(r, c); // [r][c];
                }
                Arrays.sort(aggregator);
                if (center)
                    result[r][c] = aggregator[ctridx];
                else
                    result[r][c] = (aggregator[ctridx] + aggregator[ctridx + 1]) / 2;
            }
        }

        RealMatrixExt resultM = new RealMatrixExt(result);
        resultM.setType(matrices[0].getType()); // set type id to 1st matrix in
        // array

        return resultM;

    }

}
