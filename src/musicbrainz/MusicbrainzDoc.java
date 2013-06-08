package musicbrainz;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import customException.MusicbrainzDocException;

public final class MusicbrainzDoc {

	/**
	 * Private constructor, to avoid instantiation 
	 */
	private MusicbrainzDoc() {
		
	}
	
	/**
	 * creates a doc from xml content as string.
	 * @param input xml
	 * @return DOM document
	 * @throws MusicbrainzDocException in case of error.
	 */
	public final static Document createDoc(String input) 
			throws MusicbrainzDocException {
		try {
			
			DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
			DocumentBuilder db= dbf.newDocumentBuilder();
			return db.parse(new InputSource(new StringReader(input)));
			
		} catch (ParserConfigurationException e) {
			throw new MusicbrainzDocException("ParserConfigurationException "+e.getMessage(), e);
		} catch (SAXException e) {
			throw new MusicbrainzDocException("SAXException "+e.getMessage(), e);
		} catch (IOException e) {
			throw new MusicbrainzDocException("IOException "+e.getMessage(), e);
		} catch(FactoryConfigurationError e) {
			throw new MusicbrainzDocException("FactoryConfigurationError "+e.getMessage(), e);
		} catch (Exception e) {
			throw new MusicbrainzDocException("Exception "+e.getMessage(), e);
		}
	}
}
