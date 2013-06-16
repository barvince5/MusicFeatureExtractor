package setup;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;


import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
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
import mfeArtifacts.setup.FlagValueType;
import mfeArtifacts.setup.MFESetupType;

import customException.DateConverterException;
import customException.MFESetupException;


public final class MFESetup {
	
	private MFESetupType setup= null; 
	
	public MFESetup(String path) 
			throws MFESetupException {
		
		this.setup= this.getSettings(path);
	}
	
	private final MFESetupType getSettings(String path) 
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
	
	public final List<String> getCommands() {
		
		List<String> cmd= new LinkedList<String>();
		List<CommandType> cmdList= this.setup.getPossibleCommands().getCommand();
		Iterator<CommandType> iter= cmdList.iterator();
		while(iter.hasNext())
			iter.next().getCmd();
		
		return cmd;
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
	
	public final String getCreationSetupFileDate() 
			throws DateConverterException {
		
		return this.setup.getXMLCreationDate().toString();
	}
	
	public final List<FlagValueType> getFlags() {
		return this.setup.getFlags().getFlag();
	}
}
