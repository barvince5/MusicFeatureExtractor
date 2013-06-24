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
 * Copyright Vienna University of Technology
 * 
 * @author Thomas Lidy
 * @version $Id: FeatureExtractionOptions.java 214 2011-05-07 11:03:32Z mayer $
 */
public class FeatureExtractionOptions {

	private boolean extractRH;
	private boolean extractRP;
	private boolean extractSSD;
	
    private String inputBaseDir;
    
    private int bandLimit;
    private int modAmplLimit;
    private int stepWidth;
    private int skipLeadInFadeOut;
    
    /**
     * Constructor of FeatureExtractionOptions with default values.
     * Default values are: 
     * - extractRH, extractRP, extractSSD = true (extract all features)
     * - inputBaseDir = empty string
     * - bandLimit = 24
     * - modAmpimit = 60
     * - stepWidth = 3
     * - skipLeadInFadeOut = 1
     */
    public FeatureExtractionOptions() {
    	
    	this.extractRH= true;
    	this.extractRP= true;
    	this.extractSSD= true;
    	this.inputBaseDir= "";
    	this.bandLimit= 24;
    	this.modAmplLimit= 60;
    	this.stepWidth= 3;
    	this.skipLeadInFadeOut= 1;
    }
    
    /**
     * Constructor of FeatureExtractionOptions.
     * @param extractRH RH extraction flag
     * @param extractRP RP extraction flag
     * @param extractSSD SSD extraction flag
     * @param inputBaseDir
     * @param bandLimit
     * @param modAmpLimit
     * @param stepWidth
     * @param skipLeadInFadeOut
     */
    public FeatureExtractionOptions(boolean extractRH, boolean extractRP, boolean extractSSD, 
    		String inputBaseDir, int bandLimit, int modAmpLimit, int stepWidth, int skipLeadInFadeOut) {
    	
    	this.extractRH= extractRH;
    	this.extractRP= extractRP;
    	this.extractSSD= extractSSD;
    	this.inputBaseDir= inputBaseDir;
    	this.bandLimit= bandLimit;
    	this.modAmplLimit= modAmpLimit;
    	this.stepWidth= stepWidth;
    	this.skipLeadInFadeOut= skipLeadInFadeOut;
    }

    public int getNumberOfFeatureSets() {
    	
    	int num= 0;
    	if (this.extractRH)
    		++num;
    	if (this.extractRP)
    		++num;
    	if(this.extractSSD)
    		++num;
    	
    	return num;
    }
    
    public void enableRH() {
    	this.extractRH= true;
    }
    
    public boolean hasRH() {
    	return this.extractRH;
    }
    
    
    public void disableRH() {
    	this.extractRH= false;
    }
    
    public void enableRP() {
    	this.extractRP= true;
    }
    
    public boolean hasRP() {
    	return this.extractRP;
    }
    
    public void disableRP() {
    	this.extractRP= false;
    }
    
    public void enableSSD() {
    	this.extractSSD= true;
    }
    
    public boolean hasSSD() {
    	return this.extractSSD;
    }
    
    public void disableSSD() {
    	this.extractSSD= false;
    }
    
    public void enableAll() {
    	this.extractRH= true;
    	this.extractRP= true;
    	this.extractSSD= true;
    }
    
    public void disableAll() {
    	this.extractRH= false;
    	this.extractRP= false;
    	this.extractSSD= false;
    }
    
    
    public void setInputBaseDir(String dir) {
    	this.inputBaseDir= dir;
    }
        
    public String getInputBaseDir() {
    	return this.inputBaseDir;
    }
    
    public int getBandLimit() {
    	return this.bandLimit;
    }
    
    public int getModAmpLimit() {
    	return this.modAmplLimit;
    }
    
    public int getStepWidth() {
    	return this.stepWidth;
    }
    
    public int getSkipLIFO() {
    	return this.skipLeadInFadeOut;
    }
}
