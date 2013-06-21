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
package feature.lowLevel.audio;

import java.util.Arrays;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

import feature.lowLevel.audio.data.FeatureExtractionOptions;
import feature.lowLevel.audio.data.RealMatrixExt;
import feature.lowLevel.audio.spectrum.BarkScale;
import feature.lowLevel.audio.spectrum.FFT;
import feature.lowLevel.audio.spectrum.Spectrogram;
import feature.lowLevel.audio.util.Window;


/**
 * Copyright Vienna University of Technology
 * 
 * @author Thomas Lidy
 * @version $Id: FeatureExtractor.java 167 2010-06-17 10:12:01Z frank $
 */
public class FeatureExtractor {

    private int sampleRate;
    private int sampleSizeInBits;

    /**
     * @param sampleRate the sample rate
     * @param sampleSizeInBits the sample size in bits
     */
    public FeatureExtractor(int sampleRate, int sampleSizeInBits) {
        this.sampleRate = sampleRate;
        this.sampleSizeInBits = sampleSizeInBits;
    }

    /**
     * @param wavesegment audio data
     * @param opt Feature Extraction Options fftWindowSize size of window for FFT calculation will
     *            be determined from sampleRate
     * @return RealMatrixExt array containing one RealMatrixExt for each feature set extracted
     */
    public RealMatrixExt[] extractFeatureSets(short[] wavesegment, FeatureExtractionOptions opt) {
        int fft_window_size = 1024;
        if (sampleRate <= 11025) {
            fft_window_size = 256;
        } else if (sampleRate <= 22050) {
            fft_window_size = 512;
        }

        return extractFeatureSets(wavesegment, opt, fft_window_size);
    }

    /**
     * @param waveSegment audio data
     * @param opt Feature Extraction Options
     * @param fftWindowSize size of window for FFT calculation
     * @return RealMatrixExt array containing one RealMatrixExt for each feature set extracted
     */
    public RealMatrixExt[] extractFeatureSets(short[] waveSegment, FeatureExtractionOptions opt,
            int fftWindowSize) {
        int segmentSize = waveSegment.length;
        int fi = 0; // feature set index
        RealMatrixExt[] featureSets = new RealMatrixExt[opt.getNumberOfFeatureSets()];

        /* convert to double precision and */
        /* scale wave data to adjust according to hearing threshold */

        @SuppressWarnings("unused")
		double divisor;

        if (sampleSizeInBits == 8) {
            divisor = 256;
        } else {
            divisor = 32768;
        }

        double[] wav = new double[segmentSize];

        for (int i = 0; i < segmentSize; i++) {
            wav[i] = waveSegment[i]; 
        }

        /*
         * compute spectrogram for each wave segment (with 50 % overlap in FFT computation)
         */

        double[][] spec = Spectrogram.computeSpectrogram(wav, fftWindowSize, fftWindowSize / 2, Window.HAMMING);

        int nFrames = spec.length;

        /* group to bark bands */

        BarkScale barkScale = new BarkScale(sampleRate, fftWindowSize);
        int nBands = barkScale.getNumberOfBarkBands();

        double[][] specBark = new double[nFrames][nBands];

        for (int t = 0; t < nFrames; t++) {
            specBark[t] = barkScale.apply(spec[t]);
        }

        /* decibel */
        for (int t = 0; t < nFrames; t++) {
            for (int b = 0; b < nBands; b++) {
                if (specBark[t][b] < 1) {
                    specBark[t][b] = 0; // in Matlab code this is 1
                } else {
                    specBark[t][b] = 10.0 * Math.log10(specBark[t][b]); // in
                }
            }
        }

        /* Sone */

        for (int t = 0; t < nFrames; t++) {
            for (int b = 0; b < nBands; b++) {
                if (specBark[t][b] >= 40) {
                    specBark[t][b] = Math.pow(2, ((specBark[t][b] - 40) / 10));
                } else {
                    specBark[t][b] = Math.pow((specBark[t][b] / 40), 2.642);
                }
            }
        }

        /* transpose matrix (for efficient row processing) */
        RealMatrix specBarkT = new RealMatrixExt(specBark).transpose();

        /* compute SSD features */

        if (opt.hasSSD()) {
            featureSets[fi++] = computeStatisticalSpectrumDescriptor(specBarkT, opt.getBandLimit());
        }

        if (!(opt.hasRP() || opt.hasRH()))
            return featureSets;

        /* FFT */

        double[][] specModAmp = new double[nBands][];

        FFT FFTreal = new FFT();

        for (int b = 0; b < nBands; b++) {
            // compute FFT magnitude of each band
            specModAmp[b] = FFTreal.computeMagnitude(specBarkT.getRow(b));
        }

        /* Fluctuation Strength Curve */

        // resolution of modulation frequency axis (0.17 Hz)
        double modulationFreqResolution = 1 / ((double) segmentSize / (double) sampleRate);

        for (int b = 0; b < nBands; b++) {
            specModAmp[b][0] = 0; // omit DC component (would cause deviations while blurring)
            for (int t = 1; t <= nFrames; t++) // skip DC component (avoid div/0)
            {
                double modFreq = modulationFreqResolution * t;
                double fluctWeight = 1 / (modFreq / 4 + 4 / modFreq);
                specModAmp[b][t] = specModAmp[b][t] * fluctWeight;
            }
        }


        /* Blurring */
        int bandLimit = opt.getBandLimit();
        int modAmplLimit= opt.getModAmpLimit();
        double[][] result = new double[bandLimit][modAmplLimit];

        if (nBands < bandLimit)  { 
        	// requested number of bands is > than actual # of bands
        	// fill remainder with 0
            for (int b = nBands; b < bandLimit; b++) {
                Arrays.fill(result[b], 0);
            }
            bandLimit = nBands;
        }

        for (int b = 0; b < bandLimit; b++) {
            for (int t = 1; t <= modAmplLimit; t++) { 
            	// skip DC component: start with 1
                result[b][t - 1] = specModAmp[b][t];
            }
        }

        RealMatrixExt rhythmPattern = new RealMatrixExt(result);
        rhythmPattern.setType(RealMatrixExt.TYPE_RP);

        if (opt.hasRP()) {
            featureSets[fi++] = rhythmPattern;
        }

        /* compute RH features */

        if (opt.hasRH()) {
            featureSets[fi++] = computeRhythmHistogram(rhythmPattern);
        }

        return featureSets;
    }

    private RealMatrixExt computeRhythmHistogram(RealMatrixExt rhythmPattern) {
        /* create row vector -> matrix with 1 row only */
        double[][] vector = new double[1][];
        vector[0] = rhythmPattern.sumColumns();
        RealMatrixExt result = new RealMatrixExt(vector);
        result.setType(RealMatrixExt.TYPE_RH);
        return result;
    }

    private static int N_STATISTICAL_DESCRIPTORS = 7;

    private RealMatrixExt computeStatisticalSpectrumDescriptor(RealMatrix sonogram, int bandLimit) {
        double[][] matrixSSD = new double[bandLimit][N_STATISTICAL_DESCRIPTORS];
        int nRows = sonogram.getRowDimension();

        if (nRows < bandLimit) { // requested number of bands is > than actual # of bands -> fill remainder with 0
            for (int r = nRows; r < bandLimit; r++) {
                Arrays.fill(matrixSSD[r], 0);
            }
            bandLimit = nRows;
        }

        for (int r = 0; r < bandLimit; r++) {
            matrixSSD[r] = getStatisticalSpectrumDescriptor(sonogram.getRow(r));
        }

        RealMatrixExt result = new RealMatrixExt(matrixSSD);
        result.setType(RealMatrixExt.TYPE_SSD);
        return result;
    }

    /*
     * DEACTIVATED because of destructive data change occurig in getStatisticalSpectrumDescriptor
     * private RealMatrixExt computeStatisticalSpectrumDescriptor(double[][] sonogram, int
     * bandLimit) { double[][] matrixSSD = new double[bandLimit][N_STATISTICAL_DESCRIPTORS]; int
     * nRows = sonogram.length; if (nRows < bandLimit) //requested number of bands is > than actual
     * # of bands -> fill remainder with 0 { for (int r = nRows; r < bandLimit; r++) {
     * Arrays.fill(matrixSSD[r],0); } bandLimit = nRows; } for (int r = 0; r < bandLimit; r++) {
     * matrixSSD[r] = getStatisticalSpectrumDescriptor(sonogram[r]); } RealMatrixExt result = new
     * RealMatrixExt(matrixSSD); result.setType(RealMatrixExt.TYPE_SSD); return result; }
     */

    private double[] getStatisticalSpectrumDescriptor(double[] dataRow) {
        int N = dataRow.length;
        double ssd[] = new double[N_STATISTICAL_DESCRIPTORS];

        ssd[0] = new Mean().evaluate(dataRow);
        ssd[1] = new Variance().evaluate(dataRow);
        ssd[2] = new Skewness().evaluate(dataRow);
        ssd[3] = new Kurtosis().evaluate(dataRow);

        // NOTE: be careful, sort changes data!
        // (as sonogram.getRow() above copies the data anyway, in this case there is no problem)
        // (otherwise, use dataRow.clone(); )
        Arrays.sort(dataRow);

        // median
        if (N % 2 == 0) {
            ssd[4] = (dataRow[(N / 2) - 1] + dataRow[(N / 2)]) / 2;
        } else {
            ssd[4] = dataRow[(N - 1) / 2];
        }

        ssd[5] = dataRow[0]; // min value
        ssd[6] = dataRow[N - 1]; // max value

        return ssd;
    }

}
