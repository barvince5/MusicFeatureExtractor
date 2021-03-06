/* Copyright 2013 Antonio Collarino, Vincenzo Barone

This file is part of Music Feature Extractor (MFE).

Music Feature Extractor (MFE) is free software; you can redistribute it 
and/or modify it under the terms of the GNU Lesser General Public License 
as published by the Free Software Foundation; either version 3 of the 
License, or (at your option) any later version.

Music Feature Extractor (MFE) is distributed in the hope that it will be 
useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser 
General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with Music Feature Extractor (MFE).  If not, see 
http://www.gnu.org/licenses/.  */

package httpGET;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownServiceException;

import customException.GetHttpException;

/**
 * Class with methods able to get http page given a URL.
 */
public final class GetHttpPage {
	
	private static Object lock;
	private static int timeOut, wait;
	private static long minTime;
	private static GetHttpPage instance= null;
	
	/**
	 * This is the private constructor for the singleton pattern.<br>
	 * Note: Default wait is 1 second and default timeout is 7 seconds
	 */
	private GetHttpPage() {
		GetHttpPage.wait= 1000; 	// 1000 milli second is the default value.
		GetHttpPage.timeOut= 7000; 	// 7000 milli seconds is the default value
		GetHttpPage.lock= new Object();
		GetHttpPage.minTime= 1000000000; //in nano second. (it is one second)
	}
	
	/**
	 * This method (Singleton) return the only instance of GetHttpPage class.<br>
	 * Note: Default wait is 1 second and default timeout is 7 seconds
	 * @return GetHttpPage
	 */
	public final static GetHttpPage getInstance() {
		
		if(GetHttpPage.instance == null)
			synchronized(GetHttpPage.class) {
				if(GetHttpPage.instance == null)
					GetHttpPage.instance= new GetHttpPage();
			}
			
		return GetHttpPage.instance;
	}
	
	/**
	 * This method allows to set a different timeOut.<br>
	 * Note1: it must be greater that 1000 [ms] and less that 20000 [ms].<br>
	 * NOte2: The default value is 7 seconds (7000 ms).
	 * @param timeOut in milli seconds
	 * @throws GetHttpException if the timeout is not correct.
	 */
	public final void changeTimeout(int timeOut) 
			throws  GetHttpException {
		
		if(timeOut < 1000 || timeOut > 20000)
			throw new GetHttpException("The timeout vale is not correct");
		GetHttpPage.timeOut= timeOut;
	}
	
	/**
	 * This method allows to set a different wait value.<br>
	 * Note1: it must be greater that 100 [ms] and less that 20000 [ms].<br>
	 * NOte2: The default value is 1 second (1000 ms).
	 * @param wait in milli seconds
	 * @throws GetHttpException if the timeout is not correct.
	 */
	public final void changeWait(int wait) 
			throws GetHttpException {
		
		if(wait < 100 || wait > 20000)
			throw new GetHttpException("The wait value is not correct");
		GetHttpPage.wait= wait;
	}
	
	/**
	 * This method return a web page, or empty string.
	 * @param url
	 * @return Webpage content as string.
	 * @throws GetHttpException
	 */
	public final String getWebPageAsString(URL url) 
			throws GetHttpException {
		
		HttpURLConnection conn= null;
		BufferedReader rd= null;
		String line= null;
		String result = "";
		
		try {
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(GetHttpPage.timeOut);
			
			synchronized(GetHttpPage.lock) {
				long startTime = System.nanoTime();  
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				long estimatedTime = System.nanoTime() - startTime;
				try {
					if(estimatedTime < minTime)
						Thread.sleep(GetHttpPage.wait);
				} catch(InterruptedException e) {
					return result; // Do nothing, just return
				}
			}
				
			while((line = rd.readLine()) != null)
					result += line;
			rd.close();
			
		} catch (SocketTimeoutException e) {
			throw new GetHttpException("SocketTimeoutException "+e.getMessage(), e);
		}catch (UnknownServiceException e) {
			throw new GetHttpException("UnknownServiceException " + e.getMessage(), e);	
		} catch (IOException e) {
			throw new GetHttpException("IOException " + e.getMessage(), e);
		} catch (NullPointerException e) {
			throw new GetHttpException("Url is null", e);
		} catch (Exception e) {
			throw new GetHttpException("Exception "+e.getMessage(), e);
		}
		
		return result;
	}
	
	/**
	 * This method return a web page, or empty string.
	 * @param url
	 * @return webpage content as string.
	 * @throws GetHttpException
	 */
	public final String getWebPageAsString(String url) 
			throws GetHttpException {
		
		try {
			return this.getWebPageAsString(new URL(url));
		} catch (MalformedURLException e) {
			throw new GetHttpException("MalformedURLException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new GetHttpException("Exception "+e.getMessage(), e);
		}
	}
	
	/**
	 * This method return a web page, or empty file.
	 * @param url
	 * @param outputFile
	 * @return webpage content as file.
	 * @throws GetHttpException
	 */
	public final File getWebPageAsFile(URL url, String outputFile) 
			throws GetHttpException {
		
		File file= null;
		try {
			file= new File(outputFile);
			FileWriter fr = new FileWriter(file);
			fr.write(this.getWebPageAsString(url));
			fr.close();	
		} catch (NullPointerException e) {
			throw new GetHttpException("Input not valid "+e.getMessage(), e);
		} catch (IOException e) {
			throw new GetHttpException("IOException "+e.getMessage(), e);
		} catch (Exception e) {
			throw new GetHttpException("Exception "+e.getMessage(), e);
		}
		
		return file;
	}
	
	/**
	 * This method return a web page, or empty file.
	 * @param url
	 * @param outputFile
	 * @return webpage content as file.
	 * @throws GetHttpException
	 */
	public final File getWebPageAsFile(String url, String outputFile) 
			throws GetHttpException {
		
		try {
			return this.getWebPageAsFile(new URL(url), outputFile);
		} catch (MalformedURLException e) {
			throw new GetHttpException("MalformedURLException", e);
		} catch (Exception e) {
			throw new GetHttpException("Exception "+e.getMessage(), e);
		}
	}
}
