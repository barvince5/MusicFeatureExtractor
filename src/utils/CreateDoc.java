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
