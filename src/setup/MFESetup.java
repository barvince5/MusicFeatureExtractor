package setup;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import utils.DateConverter;

import mfeArtifacts.setup.AuthorType.Author;
import mfeArtifacts.setup.FlagValueType;
import mfeArtifacts.setup.MFESetupType;

import customException.DateConverterException;
import customException.MFESetupException;


public final class MFESetup {
	
	private MFESetupType setup= null; 
	
	public MFESetup(File setting) 
			throws MFESetupException {
		
		this.setup= this.getSettings(setting);
	}
	
	private final MFESetupType getSettings(File file) 
			throws MFESetupException { 
		
		try {
			
			JAXBContext jc= JAXBContext.newInstance("mfeArtifacts.setup");
			Unmarshaller u= jc.createUnmarshaller();
			SchemaFactory sf= SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			InputStream is= MFESetup.class.getClassLoader().getResourceAsStream("MFESetting/MFESetup.xsd");
			Schema schema= sf.newSchema(new StreamSource(is));
			u.setSchema(schema);
			u.setEventHandler(new ValidationEventHandler() {
				
				@Override
				public boolean handleEvent(ValidationEvent event) {
					System.err.println(event.getMessage());
					return false;
				}
			});
		
			@SuppressWarnings("unchecked")
			JAXBElement<MFESetupType> je= (JAXBElement<MFESetupType>) u.unmarshal(file);
			MFESetupType setup= je.getValue();
			
			return setup;
		
		}catch(JAXBException e) {
			throw new MFESetupException("JAXBException "+e.getMessage(), e);
		} catch(NullPointerException e) {
			throw new MFESetupException("NullPointerException "+e.getMessage(), e);
		} catch(Exception e) {
			throw new MFESetupException(e.getMessage(), e);
		}
	}
	
	public final List<String> getHelpMessage() {
		return this.setup.getPossibleCommands().getCommand();
	}
	
	public final List<String> getCommands() {
		return this.setup.getPossibleCommands().getCommand();
	}
	
	public final List<Author> getAuthors() {
		List<Author> aut= this.setup.getAuthors().getAuthor();
		return aut;
	}
	
	public final String getVersion() {
		return this.setup.getMFENumberVersion();
	}
	
	public final String getVersionName() {
		return this.setup.getMFENameVersion();
	}
	
	public final Date getCreationSetupFileDate() 
			throws DateConverterException {
		
		return DateConverter.XMLGregorianCalendarToDate(this.setup.getXMLCreationDate());
	}
	
	public final List<FlagValueType> getFlags() {
		return this.setup.getFlags().getFlag();
	}
}
