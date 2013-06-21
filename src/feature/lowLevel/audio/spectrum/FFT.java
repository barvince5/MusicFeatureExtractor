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

import feature.lowLevel.audio.transforms.FFTReal;

public class FFT {

    /*
     * @author Thomas Lidy
     */
    public double[] computeMagnitude(double[] dataReal) {
        double[] complex = new FFTReal().computeFFTReal(dataReal);
        int fftLength = complex.length / 2;

        double[] mag = new double[fftLength];

        for (int i = 0; i < fftLength; i++) {
            mag[i] = Math.sqrt(complex[2 * i] * complex[2 * i] + complex[2 * i + 1]
                    * complex[2 * i + 1]);
        }
        return mag;
    }

    /*
     * @author Thomas Lidy
     */
    public double[] computePowerSpectrum(double[] dataReal) {
        double sum = 0;
        for (int i = 0; i < dataReal.length; i++) {
            sum += dataReal[i];
        }

        if (sum == 0)
            return computeMagnitude(dataReal);

        double multiplicator = 2.0 / sum;
        double multiplicatorQuadr = multiplicator * multiplicator;

        double[] mag = computeMagnitude(dataReal);

        int fftLength = mag.length;

        /* divide / energy, multiply by 2, take power of 2 */
        // X = fft(wav(idx).*w,p.fft_size);
        // dlinear(:,i) = abs(X(1:p.fft_size/2+1)/sum(w)*2).^2; %% normalized
        // powerspectrum
        for (int i = 0; i < fftLength; i++) {
            mag[i] = mag[i] * mag[i] * multiplicatorQuadr;
        }
        return mag;
    }
}
