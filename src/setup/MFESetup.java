package setup;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
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


import mfeArtifacts.setup.AuthorType.Author;
import mfeArtifacts.setup.CommandType;
import mfeArtifacts.setup.MFESetupType;

import customException.DateConverterException;
import customException.MFESetupException;

/**
 * This class is important to get setting data from the setup.xml file
 */
public final class MFESetup {
	
	private MFESetupType setup= null; 

	/**
	 * This is the constructor.
	 * @throws MFESetupException in case of error.
	 */
	public MFESetup() 
			throws MFESetupException {
		
		this.setup= this.getSettings();
	}
	
	/**
	 * This method load the setting from the xml setup file.
	 * @return setup
	 * @throws MFESetupException
	 */
	private final MFESetupType getSettings() 
			throws MFESetupException { 
		
		try {
			
			InputStream file= MFESetup.class.getClassLoader().getResourceAsStream("MFESetting/setup.xml");
			JAXBContext jc= JAXBContext.newInstance("mfeArtifacts.setup");
			Unmarshaller u= jc.createUnmarshaller();
			SchemaFactory sf= SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			InputStream is= MFESetup.class.getClassLoader().getResourceAsStream("MFESetting/MFESetup.xsd");
			Schema schema= sf.newSchema(new StreamSource(is));
			u.setSchema(schema);
			u.setEventHandler(new ValidationEventHandler() {
				
				@Override
				public boolean handleEvent(ValidationEvent event) {
					return false;
				}
			});
		
			@SuppressWarnings("unchecked")
			JAXBElement<MFESetupType> je= (JAXBElement<MFESetupType>) u.unmarshal(file);
			MFESetupType setup= je.getValue();
			return setup;
		
		}catch(JAXBException e) {
			throw new MFESetupException("JAXBException "+e.getMessage(), e);
		} catch(IllegalArgumentException e) {
			throw new MFESetupException("IllegalArgumentException "+e.getMessage(), e);
		} catch(NullPointerException e) {
			throw new MFESetupException("NullPointerException "+e.getMessage(), e);
		} catch(Exception e) {
			throw new MFESetupException("Exception "+e.getMessage(), e);
		}
	}
	
	/**
	 * Gets a useful message for the user.
	 * @return help message
	 */
	public final String getHelpMessage() {
		
		String msg= "";
		List<CommandType> cmdList= this.setup.getPossibleCommands().getCommand();
		Iterator<CommandType> iter= cmdList.iterator();
		while(iter.hasNext()) {
			CommandType temp= iter.next();
			msg= msg + temp.getCmd() + " " + temp.getDescription() + '\n';
		}
			
		return msg;
	}
	
	/**
	 * Gets a map where the key is the command and the entry is its corresponding class which will
	 * be instantiated on the fly in the main class.
	 * @return commands
	 */
	public final HashMap<String, String> getCommands() {
		
		HashMap<String, String> cmd= new HashMap<String, String>();
		List<CommandType> cmdList= this.setup.getPossibleCommands().getCommand();
		Iterator<CommandType> iter= cmdList.iterator();
		while(iter.hasNext()) {
			CommandType c= iter.next();
			cmd.put(c.getCmd(), c.getClazz());
		}
		
		return cmd;
	}
	
	/**
	 * Gets a list of author.
	 * @return authors list
	 */
	public final List<Author> getAuthors() {
		List<Author> aut= this.setup.getAuthors().getAuthor();
		return aut;
	}
	
	/**
	 * Gets the number version of MFE. (e.g. 1.0.0)
	 * @return version
	 */
	public final String getVersion() {
		return this.setup.getMFENumberVersion();
	}
	
	/**
	 * Gets the name of this MFE version.
	 * @return version name.
	 */
	public final String getVersionName() {
		return this.setup.getMFENameVersion();
	}
	
	/**
	 * Gets both version name and number.
	 * @return version information.
	 */
	public final String getCompleteVersionInfo() {
		return (this.setup.getMFENumberVersion() + " " + this.setup.getMFENameVersion());
	}
	
	/**
	 * Gets the creation date of the setup xml file as string.
	 * @return creation date.
	 * @throws DateConverterException
	 */
	public final String getCreationSetupFileDate() 
			throws DateConverterException {
		
		return this.setup.getXMLCreationDate().toString();
	}
}
