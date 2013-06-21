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
