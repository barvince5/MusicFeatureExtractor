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

/**
 * Copyright Vienna University of Technology
 * 
 * @author Simon Diesenreiter for M2K, altered by Thomas Lidy
 * @version $Id: BarkScale.java 173 2010-06-17 16:28:05Z mayer $
 */

public class BarkScale {

    private static final double[] BARK_LIMITS = { 100, 200, 300, 400, 510, 630, 770, 920, 1080,
            1270, 1480, 1720, 2000, 2320, 2700, 3150, 3700, 4400, 5300, 6400, 7700, 9500, 12000,
            15500, 22050 };

    private int sampleRate;
    private double binSizeHz;

    public BarkScale(int sampleRate, int fft_window_size) {
        this.sampleRate = sampleRate;
        this.binSizeHz = (double) sampleRate / (double) fft_window_size;
    }

    public BarkScale(int sampleRate, double binSize_inHz) {
        this.sampleRate = sampleRate;
        this.binSizeHz = binSize_inHz;
    }

    public int getNumberOfBarkBands() {
        return getNumberOfBarkBands(this.sampleRate);
    }

    public static int getNumberOfBarkBands(int sampleRate) {
        /* determine number of used bark bands */
        int i = 0;
        while ((i < BarkScale.BARK_LIMITS.length) && (BarkScale.BARK_LIMITS[i] <= sampleRate / 2)) {
            i++;
        }
        return i + 1;
    }

    public double[] apply(double[] spectrumData) {

        if (spectrumData == null) {
            return null;
        }

        int specLen = spectrumData.length;
        int barkLen = getNumberOfBarkBands();

            // initialize output vector
        double[] barkSpec = new double[barkLen];

        // f ... frequency band, curFreq ... frequency represented by band f
        // f starts from 1 skipping DC component
        int b = 0; // b ... bark band index
        barkSpec[b] = 0;
        for (int f = 1; f < specLen / 2 + 1; f++) {
            double curFreq = f * this.binSizeHz;
            while (curFreq > BarkScale.BARK_LIMITS[b]) {
                b++;
                barkSpec[b] = 0;
            }

            barkSpec[b] += spectrumData[f];
        }

        return barkSpec;
    }
}
