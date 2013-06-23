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

package command.info;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import utils.DateConverter;

import mfeArtifacts.setup.AuthorType.Author;
import command.Command;
import command.CommandParameter;

/**
 * This class prints on screen informations about the authors.
 */
public final class AuthorCommand implements Command {

	private CommandParameter par;
	
	/**
	 * Standard command constructor which gets as input the command parameter.
	 * @param par contains all necessary data (user input and setup data).
	 */
	public AuthorCommand(CommandParameter par) {
		
		if(par == null)
			throw new NullPointerException("AuthorCommand has a null CommandParameter");
		
		this.par= par;
	}
	
	@Override
	public void start() 
			throws Exception {
		
		String name, surname, alias, email, role;
		Date joined;
		
		List<Author> authors= par.getMFESetup().getAuthors();
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
