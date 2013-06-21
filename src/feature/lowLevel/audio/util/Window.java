/*
 * Copyright 2005 University of Illinois at Urbana-Champaign
 * Portions Copyright 2005 University of East Anglia
 * Portions Copyright 2005 Sun Microsystems Inc.
 *
 * All Rights Reserved.  Use is subject to license terms.
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */
package feature.lowLevel.audio.util;

/**
 * Applies a specified window to an array. A D2K/M2K module that reads in a double array, and
 * returns the array processed by a window function. The choices of different window functions are:
 * Rectangular, Bartlett, Hanning, and Hamming.
 * <p>
 * Modified by TU Vienna to be not dependent on M2K/D2K. Modifications include:
 * <ul>
 * <li>not extending ComputeModule</li>
 * <li>removed method getPropertiesDescriptions</li>
 * <li>extracting constants for window names (HAMMING, HANNING, BARTLETT, RECTANGULAR)</li>
 * <li>added constructor Window(String windowName)</li>
 * <li>transformed method void doit() to double[] apply(double[] double1DArray)</li>
 * </ul>
 * 
 * @author Andreas Ehmann
 */
public class Window {

    public static final String HAMMING = "Hamming";
    public static final String HANNING = "Hanning";
    public static final String BARTLETT = "Bartlett";
    public static final String RECTANGULAR = "Rectangular";

    public Window(String windowName) {
        this.WindowName = windowName;
    }

    /**
     * Window type to use.
     */
    private String WindowName = Window.RECTANGULAR;

    /**
     * Sets this object's window name.
     * 
     * @param value the window name
     */
    public void setWindowName(String value) {
        this.WindowName = value;
    }

    /**
     * Gets this object's window name.
     * 
     * @return the name of the window
     */
    public String getWindowName() {
        return this.WindowName;
    }

    /**
     * Applies a specified window to the input array. Currently, four window functions are
     * implemented. Rectangular, Bartlett, Hanning, and Hamming.
     */

    // original: void doit() {
    public double[] apply(double[] double1DArray) {
        if (double1DArray == null) {
            return null;
        }

        int array1NumValues = double1DArray.length;

        int windowType = 1;

        if (this.WindowName.equals(Window.RECTANGULAR))
            windowType = 1;
        if (this.WindowName.equals(Window.BARTLETT))
            windowType = 2;
        if (this.WindowName.equals(Window.HANNING))
            windowType = 3;
        if (this.WindowName.equals(Window.HAMMING))
            windowType = 4;

        switch (windowType) {
            case 1: /* Rectangular */
                return double1DArray;
            case 2: /* Bartlett */
                if ((array1NumValues % 2) == 0) {
                    for (int d1 = 0; d1 < array1NumValues; d1++) {
                        if (d1 <= array1NumValues / 2) {
                            double1DArray[d1] = (2 * (double) d1 / ((double) array1NumValues - 1))
                                    * double1DArray[d1];
                        } else {
                            double1DArray[d1] = (2 - 2 * (double) d1
                                    / ((double) array1NumValues - 1))
                                    * double1DArray[d1];
                        }
                    }
                } else {
                    for (int d1 = 0; d1 < array1NumValues; d1++) {
                        if (d1 <= array1NumValues / 2) {
                            double1DArray[d1] = (2 * (double) d1 / ((double) array1NumValues - 1))
                                    * double1DArray[d1];
                        } else {
                            double1DArray[d1] = 2
                                    * ((array1NumValues - (double) d1 - 1) / ((double) array1NumValues - 1))
                                    * double1DArray[d1];
                        }
                    }

                }
                return double1DArray;
            case 3: /* Hanning */
                for (int d1 = 0; d1 < array1NumValues; d1++) {
                    double1DArray[d1] = (0.5 - 0.5 * Math.cos(2 * Math.PI * (d1 + 1)
                            / (array1NumValues + 1)))
                            * double1DArray[d1];
                }
                return double1DArray;
            case 4: /* Hamming */
                for (int d1 = 0; d1 < array1NumValues; d1++) {
                    double1DArray[d1] = (0.54 - 0.46 * Math.cos(2 * Math.PI * d1
                            / (array1NumValues - 1)))
                            * double1DArray[d1];
                }
                return double1DArray;

        }

        return null;
    }
}
