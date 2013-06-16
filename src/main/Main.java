package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import mfeArtifacts.setup.AuthorType.Author;
import mfeArtifacts.setup.FlagValueType;

import setup.MFESetup;
import utils.DateConverter;
import utils.Plotter;


public class Main {

	public static void main(String[] args) {
		
		boolean hlFlag= false, llFlag= false;
		String path= "";
		String helpMSG= "";
		List<Author> authors= null;
		
		try {
			
			if(args.length == 0) {
				System.err.println("No input");
				System.exit(1);
			}
			
			//SETUP PHASE
			MFESetup setup= new MFESetup("MFESetting/setup.xml");
			
			//set the flags
			Iterator<FlagValueType> iter1= setup.getFlags().iterator();
			while(iter1.hasNext()) {
				FlagValueType flag= iter1.next();
				if(flag.getName().equals("HighLevelFlag"))
					hlFlag= flag.isValue();
				if(flag.getName().equals("LowLevelFlag"))
					llFlag= flag.isValue();
			}
			
			//set the flags values
			helpMSG= setup.getHelpMessage();
			
			//set the authors
			authors= setup.getAuthors();
			
			//START PROGRAM PHASE
			String userCommand= args[0];
			int inputNumberValues= args.length - 1; //-1 for command word
			
			//check the command and set the variables or flags
			switch(userCommand) {
				
				case "-plot": 
					if(inputNumberValues >= 1) {
						Plotter.plot(args);
					} else {
						System.err.println("Command error"+'\n'+helpMSG);
					}
					break;

				case "-all":
					if(inputNumberValues == 1) {
						
						hlFlag= true;
						llFlag= true;
						if(args[1].equals("."))
							path= System.getProperty("user.dir");
						else
							path= args[1];
						
						System.out.println("ARTISTS PHASE: Please wait...");
						MasterMetadata.artistMetadata(path, hlFlag);
						System.out.println("ALBUMS PHASE: Please wait...");
						MasterMetadata.albumMetadata(path, hlFlag);
						System.out.println("SONGS PHASE: Please wait...");
						MasterMetadata.songMetadata(path, hlFlag, llFlag);
						
					} else {
						System.err.println("Command error"+'\n'+helpMSG);
					}
					break;
					
				case "-hl":
					if(inputNumberValues == 1) {
						
						//because for song there are both low and high extraction feature
						hlFlag= true;
						llFlag= false; //no low level feature extraction for song
						if(args[1].equals("."))
							path= System.getProperty("user.dir");
						else
							path= args[1];
						
						System.out.println("ARTISTS PHASE: Please wait...");
						MasterMetadata.artistMetadata(path, hlFlag);
						System.out.println("ALBUMS PHASE: Please wait...");
						MasterMetadata.albumMetadata(path, hlFlag);
						System.out.println("SONGS PHASE: Please wait...");
						MasterMetadata.songMetadata(path, hlFlag, llFlag);
						
					} else {
						System.err.println("Command error"+'\n'+helpMSG);
					}
						
					break;
					
				case "-ll":
					if(inputNumberValues == 1) {
						
						//because for song there are both low and high extraction feature
						hlFlag= false; //no low level feature extraction for song
						llFlag= true;
						if(args[1].equals("."))
							path= System.getProperty("user.dir");
						else
							path= args[1];
						
						System.out.println("SONGS PHASE: Please wait...");
						MasterMetadata.songMetadata(path, hlFlag, llFlag);
						
					} else {
						System.err.println("Command error"+'\n'+helpMSG);
					}
					
					break;
					
				case "-help":
					System.out.println(helpMSG);
					break;
					
				case "-author":
					String name, surname, alias, email, role;
					Date joined;
					Iterator<Author> iter= authors.iterator();
					while(iter.hasNext()) {
						Author aut= iter.next();
						name= aut.getName();
						surname= aut.getSurname();
						alias= aut.getAlias();
						email= aut.getEmail();
						role= aut.getRole();
						joined= DateConverter.XMLGregorianCalendarToDate(aut.getJoined());
						SimpleDateFormat dt= new SimpleDateFormat("YYYY-MM-dd");
						System.out.println("Name: "+name+'\n'+"Surname: "+surname+'\n'+"Alias: "+alias);
						System.out.println("E-mail: "+email+'\n'+"Role: "+role+'\n'+"Joined: "+dt.format(joined));
						System.out.println("========");
					}
					break;
					
				case "-version":
					System.out.println("Version: "+setup.getVersion()+" "+setup.getVersionName());
					break;
					
				case "-cluster":
					if(inputNumberValues == 3) {
						//TODO
					} else {
						System.err.println("Command error"+'\n'+helpMSG);
					}
					break;
					
				default:
					System.err.println("Command error"+'\n'+helpMSG);
					break;

			}
					
		} catch(Exception e) {
			MasterMetadata.shutDownMFE();
			System.err.println("MFE is shutting down");
		}
	}
}
