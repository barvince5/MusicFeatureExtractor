package command.info;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import utils.DateConverter;

import mfeArtifacts.setup.AuthorType.Author;
import command.Command;

/**
 * This class prints on screen informations about the authors.
 */
public final class AuthorCommand implements Command {

	private Collection<Author> authors= null;
	
	public AuthorCommand(Collection<Author> authors) {
		this.authors= authors;
	}
	
	@Override
	public void start() 
			throws Exception {
		
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
		
	}

}
