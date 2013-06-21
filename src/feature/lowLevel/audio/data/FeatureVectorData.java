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

/**
 * Copyright Vienna University of Technology <br>
 * <br>
 * Encapsulates both numeric feature vector data (in matrix form) + meta-data (fileLabel, id, class)
 * 
 * @author Thomas Lidy
 * @version $Id: FeatureVectorData.java 172 2010-06-17 16:21:16Z mayer $
 */
public class FeatureVectorData {

    private String id;
    private String fileLabel; // e.g. file name
    private String classLabel;

    // double[] vector;
    private RealMatrixExt matrix;

    public String getClassLabel() {
        return classLabel;
    }

    public void setClassLabel(String classlabel) {
        this.classLabel = classlabel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileLabel() {
        return fileLabel;
    }

    public void setFileLabel(String label) {
        this.fileLabel = label;
    }

    public double[] getVector() {
        return matrix.vectorize();
    }

    public void setVector(double[] vector) {
        this.matrix = new RealMatrixExt(vector);
    }

    public RealMatrixExt getMatrix() {
        return this.matrix;
    }

    public double[][] getMatrixData() {
        return this.matrix.getData();
    }

    public void setMatrix(RealMatrixExt matrix) {
        this.matrix = matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = new RealMatrixExt(matrix);
    }
}
