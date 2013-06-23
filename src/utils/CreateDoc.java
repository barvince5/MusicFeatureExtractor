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

package utils;


import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import customException.CreateDocException;

/**
 * This class is able to create a generic doc DOM.
 */
public final class CreateDoc {

	/**
	 * Private constructor, to avoid instantiation 
	 */
	private CreateDoc() {
		
	}
	
	/**
	 * creates a doc from xml content as string.
	 * @param input xml
	 * @return DOM document
	 * @throws MusicbrainzDocException in case of error.
	 */
	public final static Document create(String input) 
		throws CreateDocException {
		try {
			
			DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
			DocumentBuilder db= dbf.newDocumentBuilder();
			return db.parse(new InputSource(new StringReader(input)));
			
		} catch (ParserConfigurationException e) {
			throw new CreateDocException("ParserConfigurationException "+e.getMessage(), e);
		} catch (SAXException e) {
			throw new CreateDocException("SAXException "+e.getMessage(), e);
		} catch (IOException e) {
			throw new CreateDocException("IOException "+e.getMessage(), e);
		} catch(FactoryConfigurationError e) {
			throw new CreateDocException("FactoryConfigurationError "+e.getMessage(), e);
		} catch (Exception e) {
			throw new CreateDocException("Exception "+e.getMessage(), e);
		}
	}
}
