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
package feature.lowLevel.audio.spectrum;

import feature.lowLevel.audio.util.Window;

/**
 * Copyright Vienna University of Technology
 * 
 * @author Thomas Lidy
 * @version $Id: Spectrogram.java 163 2010-06-16 17:01:53Z mayer $
 */
public class Spectrogram {

    /**
     * @param wavedata audio signal provided as double array
     * @param fft_window_length size of fft window for spectrogram calculation
     * @param fft_window_inc increment for fft window (e.g. fft_window_length/2 means 50 % overlap)
     * @param window_type type of applied window: a constant from Window class
     * @return double matrix containing the spectrogram
     */
    public static double[][] computeSpectrogram(double[] wavedata, int fft_window_length,
            int fft_window_inc, String window_type) {
        int segSize = wavedata.length;

        int numWin = ((segSize - fft_window_length) / fft_window_inc) + 1;

        FFT FFTreal = new FFT();
        Window windowfunc = new Window(window_type);

        double[][] spectrogram = new double[numWin][fft_window_length];
        double[] frame = new double[fft_window_length];

        int pos = 0; // start position for frame (considering overlap)

        for (int i = 0; i < numWin; i++) {
            /* take a frame from the input wavedata */
            for (int k = 0; k < fft_window_length; k++) {
                frame[k] = wavedata[pos + k];
            }

            /* multiply with window function */
            frame = windowfunc.apply(frame);

            /* compute power spectrum from real FFT and construct spectrogram */
            spectrogram[i] = FFTreal.computeMagnitude(frame);
    
            pos = pos + fft_window_inc;
        }

        /*
         * NB: would need only fft_window_length/2 + 1 values. 1st value: DC, next
         * fft_window_length/2 values are spectrum
         */

        return spectrogram;
    }

}
