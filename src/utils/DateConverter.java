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

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import customException.DateConverterException;

/**
 * This class has some useful methods to convert date.
 */
public final class DateConverter {

	/**
	 * Private constructor to avoid instantiation.
	 */
	private DateConverter() {
		
	}
	
	/**
	 * This method converts a the current Date in a XML Gregorian Calendar.
	 * @return XMLGregorianCalendar or null.
	 * @throws DateConverterException in case of error.
	 */
	public final static XMLGregorianCalendar CurrentDateToXMLGregorianCalendar() 
			throws DateConverterException {
		return	DateConverter.DateToXMLGregorianCalendar(new Date(System.currentTimeMillis()));
	}
	
	/**
	 * This method converts a Date in a XML Gregorian Calendar.
	 * @return XMLGregorianCalendar or null.
	 * @throws DateConverterException in case of error.
	 */
	public final static XMLGregorianCalendar DateToXMLGregorianCalendar(Date date) 
			throws DateConverterException {
		
		XMLGregorianCalendar xmlGrogerianCalendar= null;
		try {
			
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTimeZone(TimeZone.getTimeZone("CET"));
			gregorianCalendar.setTime(date);
			xmlGrogerianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
			
		} catch (DatatypeConfigurationException e) {
			throw new DateConverterException("DatatypeConfigurationException "+e.getMessage(), e);
		}
		
		return xmlGrogerianCalendar;
	}
	
	/**
	 * This method transform an xml Gregorian calendar in date.
	 * @param gc
	 * @return te corresponding date value
	 * @throws DateConverterException 
	 */
	public final static Date XMLGregorianCalendarToDate(XMLGregorianCalendar gc) 
			throws DateConverterException {
		
		if(gc == null)
			throw new DateConverterException("The input is null ");
		
		return gc.toGregorianCalendar().getTime();
	}
}
